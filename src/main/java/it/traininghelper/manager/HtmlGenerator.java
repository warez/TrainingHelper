package it.traininghelper.manager;

import it.traininghelper.valueobject.TrainingImageVO;
import it.traininghelper.valueobject.TrainingVO;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by warez on 08/06/15.
 */
public class HtmlGenerator {

    private static final SimpleDateFormat format = new SimpleDateFormat("hh:mm dd-MM-yyyy");

    public StringBuilder generateHtml(List<TrainingVO> trainings) {
        StringBuilder builder = new StringBuilder();
        openBody(builder);

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
        return ret.append("<div class='training_img'>")
                .append("<figure><img class='training_img' src='")
                .append(new String(img.getImage()))
                .append("'/>")
                .append("<figcaption>")
                .append(img.getDescription())
                .append("</figcaption>")
                .append("</figure></div>");
    }

    public StringBuilder openBody(StringBuilder builder) {
        builder.append("<html><body>");
        return builder;
    }

    public StringBuilder closeBody(StringBuilder builder) {
        builder.append("</body></html>");
        return builder;
    }

}
