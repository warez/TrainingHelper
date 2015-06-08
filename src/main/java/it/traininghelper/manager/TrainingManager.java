package it.traininghelper.manager;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import it.traininghelper.dataobject.Training;
import it.traininghelper.dataobject.TrainingImage;
import it.traininghelper.valueobject.PageableResult;
import it.traininghelper.valueobject.TrainingImageVO;
import it.traininghelper.valueobject.TrainingVO;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    public void generateDocument(HttpServletResponse response, List<String> trainingIds) throws Exception {

        OutputStream out = response.getOutputStream();
        HtmlGenerator generator = new HtmlGenerator();

        List<TrainingVO> trainingVOs = get(trainingIds);
        StringBuilder str = generator.generateHtml(trainingVOs);

        InputStream is = new ByteArrayInputStream(str.toString().getBytes());

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, out);
        // step 3
        document.open();
        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
        //step 5
        document.close();
    }
}
