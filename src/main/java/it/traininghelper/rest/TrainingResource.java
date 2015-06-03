package it.traininghelper.rest;

import it.traininghelper.manager.TrainingManager;
import it.traininghelper.valueobject.PageableResult;
import it.traininghelper.valueobject.TrainingVO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("training")
public class TrainingResource {

    private TrainingManager trainingManager;


    public TrainingResource() {
        trainingManager = new TrainingManager();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public PageableResult<TrainingVO> list(@QueryParam("page") int page, @QueryParam("size") int size) {
        return trainingManager.get(page, size);
    }


    @GET
    @Path("document")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrainingVO> getTrainingForDocument(@QueryParam("trainingIds") List<Long> trainingIds) {
        return trainingManager.get(trainingIds);
    }

    @DELETE
    @Path("{id}")
    public void deleteTraining(@PathParam("id") Long trainingId) {
        trainingManager.delete(trainingId);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TrainingVO getTraining(@PathParam("id") Long trainingId) {
        return trainingManager.get(trainingId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TrainingVO createOrUpdateTraining(TrainingVO training) {
        return trainingManager.createOrUpdate(training);
    }

}
