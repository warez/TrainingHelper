package it.traininghelper.manager;

import com.google.appengine.api.users.User;
import com.google.common.io.Resources;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFileImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import it.traininghelper.dataobject.Training;
import it.traininghelper.dataobject.TrainingImage;
import it.traininghelper.valueobject.PageableResult;
import it.traininghelper.valueobject.TrainingImageVO;
import it.traininghelper.valueobject.TrainingVO;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by warez on 29/05/15.
 */
public class TrainingManager {

    private TrainingConverter converter = new TrainingConverter();

    public PageableResult<TrainingVO> get(int page, int size, User loggedUser) {

        int count = ObjectifyService.ofy().load().type(Training.class).count();

        List<Training> result;

        Query<Training> query = ObjectifyService.ofy().load().
                type(Training.class).filter("user", loggedUser.getUserId()).order("-data").offset(page * size);

        if(size != -1)
            query = query.limit(size);

        result = query.list();

        Map<Long, List<TrainingImage>> images = converter.loadImagePerTraining(result);

        PageableResult<TrainingVO> ret = converter.getPageableResult(result, images, page, count);
        return ret;

    }



    private List<TrainingVO> get(List<String> trainingIds) {

        List<Long> trainingLongIds = new ArrayList<>();
        for(String s: trainingIds)
            trainingLongIds.add( Long.parseLong(s) );

        Map<Long, Training> result = ObjectifyService.ofy().load().type(Training.class).
                ids(trainingLongIds.toArray(new Long[trainingIds.size()]));

        List<Training> trainings = new ArrayList<>();
        for(Long id: trainingLongIds)
            trainings.add(result.get(id));

        Map<Long, List<TrainingImage>> images = converter.loadImagePerTraining(trainings);

        PageableResult<TrainingVO> ret = converter.getPageableResult(trainings, images, -1, -1);
        return ret.getResult();
    }

    public void delete(Long trainingId, User user) {
        ObjectifyService.ofy().delete().type(Training.class).id(trainingId);
    }

    public TrainingVO get(Long trainingId) {
        Training training =  ObjectifyService.ofy().load().type(Training.class).id(trainingId).get();
        List<TrainingImage> images = converter.loadImagePerTraining(training);

        TrainingVO ret = converter.convert(training, images);

        return ret;
    }

    public TrainingVO createOrUpdate(TrainingVO voBefore, User loggedUser) {

        Training trainingBefore = converter.convert(voBefore);

        List<Ref<TrainingImage>> imageRefEntities = new ArrayList<>();
        List<TrainingImage> images = new ArrayList<>();

        for(TrainingImageVO image: voBefore.getImages()) {
            TrainingImage imageEntityBefore = converter.convert(image);
            Key<TrainingImage> imageKey = ObjectifyService.ofy().save().entity(imageEntityBefore).now();
            Ref<TrainingImage> imageRef = ObjectifyService.ofy().load().key(imageKey);

            imageRefEntities.add(imageRef);

            imageEntityBefore = imageRef.get();
            images.add(imageEntityBefore);
        }

        trainingBefore.setImages(imageRefEntities);
        trainingBefore.setUser(loggedUser.getUserId());

        Key<Training> key = ObjectifyService.ofy().save().entity(trainingBefore).now();
        Training trainingAfter = ObjectifyService.ofy().load().key(key).get();

        TrainingVO ret = converter.convert(trainingAfter, images);
        return ret;

    }

    public class MySpecialImageProviderAwareHtmlPipelineContext extends HtmlPipelineContext {

        MySpecialImageProviderAwareHtmlPipelineContext () {
            super(null);
        }

        public HtmlPipelineContext clone () {
            HtmlPipelineContext ctx = null;
            try {
                ctx = super.clone();
                ctx.setImageProvider(new Base64ImageProvider());
            } catch (Exception e) {
                //handle exception
            }
            return ctx;
        }

    }

    class Base64ImageProvider extends AbstractImageProvider {

        @Override
        public Image retrieve(String src) {
            int pos = src.indexOf("base64,");
            try {
                if (src.startsWith("data") && pos > 0) {
                    byte[] img = Base64.decode(src.substring(pos + 7));
                    return Image.getInstance(img);
                }
                else {
                    return Image.getInstance(src);
                }
            } catch (BadElementException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        }

        @Override
        public String getImageRootPath() {
            return null;
        }
    }

    public void generateHTMLDocument(HttpServletResponse response, List<String> trainingIds) throws Exception {

        ServletOutputStream os = response.getOutputStream();

        HtmlGenerator generator = new HtmlGenerator();

        List<TrainingVO> trainingVOs = get(trainingIds);
        StringBuilder str = generator.generateHtml(trainingVOs, true);

        try {
            os.print(str.toString());
            os.flush();
        } finally {
            if(os != null)
                os.close();
        }
    }

    public void generatePDFDocument(HttpServletResponse response, List<String> trainingIds) throws Exception {

        OutputStream out = response.getOutputStream();
        HtmlGenerator generator = new HtmlGenerator();

        List<TrainingVO> trainingVOs = get(trainingIds);
        StringBuilder str = generator.generateHtml(trainingVOs, false);

        InputStream is = new ByteArrayInputStream(str.toString().getBytes());


        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, out);
        // step 3
        document.open();


        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        CssFile cssFileTest = XMLWorkerHelper.getCSS(new FileInputStream(new File(
                Resources.getResource(HtmlGenerator.DOCUMENT_CSS).toURI())));
        cssResolver.addCss(cssFileTest);

        // HTML
        MySpecialImageProviderAwareHtmlPipelineContext htmlContext = new MySpecialImageProviderAwareHtmlPipelineContext();
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(is);

        document.close();


        byte[] buffer = new byte[10240];

        try {
            for (int length = 0; (length = is.read(buffer)) > 0;) {
                out.write(buffer, 0, length);
            }
        }
        finally {
            try { out.close(); } catch (IOException ignore) {}
            try { is.close(); } catch (IOException ignore) {}
        }

    }
}
