package it.traininghelper.manager;

import com.googlecode.objectify.Ref;
import it.traininghelper.dataobject.Training;
import it.traininghelper.dataobject.TrainingImage;
import it.traininghelper.valueobject.PageableResult;
import it.traininghelper.valueobject.TrainingImageVO;
import it.traininghelper.valueobject.TrainingVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by warez on 31/05/15.
 */
public class TrainingConverter {

    public PageableResult<TrainingVO> getPageableResult(List<Training> result,
                                                        Map<Long,List<TrainingImage>> images,
                                                        int page, int count) {

        List<TrainingVO> trainingVOs = convertTrainingList(result, images);
        for(TrainingVO vo: trainingVOs) {
            List<TrainingImage> trainingImages = images.get(vo.getId());
            List<TrainingImageVO> imagesVOs = convertImageList(trainingImages);
            vo.setImages(imagesVOs.toArray(new TrainingImageVO[imagesVOs.size()]));
        }

        PageableResult<TrainingVO> ret = new PageableResult<>(page, count, trainingVOs);
        return ret;
    }

    private List<TrainingImageVO> convertImageList(List<TrainingImage> trainingImages) {
        List<TrainingImageVO> ret = new ArrayList<>();

        if(trainingImages == null)
            return ret;

        for(TrainingImage t: trainingImages)
            ret.add( convert(t) );
        return ret;
    }

    public List<TrainingVO> convertTrainingList(List<Training> trainings, Map<Long, List<TrainingImage>> images) {
        List<TrainingVO> ret = new ArrayList<>();

        for(Training t: trainings) {
            List<TrainingImage> im = images.get(t.getId());
            List<TrainingImageVO> trainingImageVOs = convertImageList(im);
            TrainingVO vo = convert(t);
            vo.setImages(trainingImageVOs.toArray(new TrainingImageVO[trainingImageVOs.size()]));

            ret.add(vo);
        }

        return ret;
    }

    public TrainingVO convert(Training training) {
        TrainingVO t = new TrainingVO();
        t.setData(training.getData());
        t.setDescrizione(training.getDescrizione());
        t.setId(training.getId());
        t.setNome(training.getNome());

        return t;
    }

    public Training convert(TrainingVO training) {
        Training t = new Training();
        t.setData(training.getData());
        t.setDescrizione(training.getDescrizione());
        t.setId(training.getId());
        t.setNome(training.getNome());

        return t;
    }

    public TrainingImage convert(TrainingImageVO image) {
        TrainingImage ret = new TrainingImage();
        ret.setId(image.getId());
        ret.setDate(image.getDate());
        ret.setDescription(image.getDescription());
        ret.setImage(image.getImage());
        return ret;
    }

    public TrainingVO convert(Training trainingAfter, List<TrainingImage> images) {
        TrainingVO vo = convert(trainingAfter);
        List<TrainingImageVO> trainingImageVOs = convertImageList(images);
        vo.setImages(trainingImageVOs.toArray(new TrainingImageVO[images.size()]));
        return vo;
    }

    public TrainingImageVO convert(TrainingImage image) {
        TrainingImageVO ret = new TrainingImageVO();
        ret.setId(image.getId());
        ret.setDate(image.getDate());
        ret.setImage(image.getImage());
        ret.setDescription(image.getDescription());
        return ret;
    }

    public Map<Long, List<TrainingImage>> loadImagePerTraining(List<Training> result) {
        Map<Long, List<TrainingImage>> images = new HashMap<>();

        for(Training t: result) {
            List<TrainingImage> trainingImages = loadImagePerTraining(t);
            images.put(t.getId(), trainingImages);
        }
        return images;
    }

    public List<TrainingImage> loadImagePerTraining(Training t) {

        List<TrainingImage> ret = new ArrayList<>();

        if(t.getImages() == null)
            return ret;

        for(Ref<TrainingImage> ti: t.getImages()) {
            TrainingImage trainingImage = ti.get();
            ret.add(trainingImage);
        }

        return ret;
    }
}
