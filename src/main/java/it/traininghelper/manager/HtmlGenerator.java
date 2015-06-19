package it.traininghelper.manager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import it.traininghelper.valueobject.TrainingImageVO;
import it.traininghelper.valueobject.TrainingVO;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by warez on 08/06/15.
 */
public class HtmlGenerator {

    public static final String DOCUMENT_CSS = "document.css";

    private static final SimpleDateFormat format = new SimpleDateFormat("hh:mm dd-MM-yyyy");

    public StringBuilder generateHtml(List<TrainingVO> trainings, boolean addCss) {
        StringBuilder builder = new StringBuilder();
        openBody(builder, addCss);

        for(TrainingVO training: trainings)
            appendTrainingBody(builder, training);

        closeBody(builder);
        return builder;
    }

    private StringBuilder appendTrainingBody(StringBuilder builder, TrainingVO training) {
        StringBuilder ret = builder.append("<div class='training_div'>")
                .append("<div class='training_title'><p>")
                .append(training.getNome())
                .append(" (")
                .append(format.format(training.getData()))
                .append(") </p></div>")
                .append("<div class='training_body'>")
                .append(training.getDescrizione())
                .append("</div>")
                .append("<div class='training_imgs'>");

        if(training.getImages() != null)
            for(TrainingImageVO image: training.getImages())
                ret = appendImage(ret, image);

        ret = ret.append("</div>").append("</div>");
        return ret;
    }

    private StringBuilder appendImage(StringBuilder ret, TrainingImageVO img) {
        ret = ret.append("<div class='training_img'>")
                .append("<figure><img class='training_img' src='")
                .append(new String(img.getImage()))
                .append("'/>");

        if(img.getDescription() != null) {
            ret = ret.append("<figcaption>")
                    .append(img.getDescription())
                    .append("</figcaption>");
        }

        ret = ret.append("</figure></div>");
        return ret;
    }

    public StringBuilder openBody(StringBuilder builder, boolean addCss) {
        builder.append("<html><head>");

        if(addCss)
            addCss(builder, DOCUMENT_CSS);

        builder.append("</head><body>");
        return builder;
    }

    private StringBuilder addCss(StringBuilder builder, String cssResourceName) {

        try {
            URL url = Resources.getResource(cssResourceName);
            String text = Resources.toString(url, Charsets.UTF_8);
            builder.append("<style>").append(text).append("</style>");

        } catch(Exception e) {

        }
        return builder;
    }

    public StringBuilder closeBody(StringBuilder builder) {
        builder.append("</body></html>");
        return builder;
    }

}
