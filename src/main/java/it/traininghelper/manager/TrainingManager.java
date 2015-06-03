package it.traininghelper.manager;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import it.traininghelper.dataobject.Training;
import it.traininghelper.dataobject.TrainingImage;
import it.traininghelper.valueobject.PageableResult;
import it.traininghelper.valueobject.TrainingImageVO;
import it.traininghelper.valueobject.TrainingVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by warez on 29/05/15.
 */
public class TrainingManager {

    private TrainingConverter converter = new TrainingConverter();

    public PageableResult<TrainingVO> get(int page, int size) {

        int count = ObjectifyService.ofy().load().type(Training.class).count();

        List<Training> result = null;

        Query<Training> query = ObjectifyService.ofy().load().type(Training.class).offset(page * size);

        if(size != -1)
            query = query.limit(size);

        result = query.order("data").list();

        Map<Long, List<TrainingImage>> images = converter.loadImagePerTraining(result);

        PageableResult<TrainingVO> ret = converter.getPageableResult(result, images, page, count);
        return ret;

    }

    public List<TrainingVO> get(List<Long> trainingIds) {

        Map<Long, Training> result = ObjectifyService.ofy().load().type(Training.class).
                ids(trainingIds.toArray(new Long[trainingIds.size()]));

        List<Training> trainings = new ArrayList<>();
        for(Long id: trainingIds)
            trainings.add(result.get(id));

        Map<Long, List<TrainingImage>> images = converter.loadImagePerTraining(trainings);

        PageableResult<TrainingVO> ret = converter.getPageableResult(trainings, images, -1, -1);
        return ret.getResult();
    }

    public void delete(Long trainingId) {
        ObjectifyService.ofy().delete().type(Training.class).id(trainingId);
    }

    public TrainingVO get(Long trainingId) {
        Training training =  ObjectifyService.ofy().load().type(Training.class).id(trainingId).get();
        List<TrainingImage> images = converter.loadImagePerTraining(training);

        TrainingVO ret = converter.convert(training, images);

        return ret;
    }

    public TrainingVO createOrUpdate(TrainingVO voBefore) {

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

        Key<Training> key = ObjectifyService.ofy().save().entity(trainingBefore).now();
        Training trainingAfter = ObjectifyService.ofy().load().key(key).get();

        TrainingVO ret = converter.convert(trainingAfter, images);
        return ret;

    }

}
