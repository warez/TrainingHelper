package it.traininghelper.rest;

import com.google.appengine.api.users.User;
import it.traininghelper.manager.TrainingManager;
import it.traininghelper.valueobject.PageableResult;
import it.traininghelper.valueobject.TrainingVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("training")
public class TrainingResource extends AbstractResource {

    public static final String HTML_TYPE = "html";
    public static final String PDF_TYPE = "pdf";
    private TrainingManager trainingManager;


    public TrainingResource() {
        trainingManager = new TrainingManager();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public PageableResult<TrainingVO> list(
            @Context HttpServletRequest req,
            @QueryParam("page") int page, @QueryParam("size") int size) {

        User user = checkUser(req);
        return trainingManager.get(page, size, user);
    }

    @POST
    @Path("document/{type}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void getTrainingForHTMLDocument(
            @Context HttpServletRequest req,
            @Context HttpServletResponse res,
            @PathParam("type") String type,
            @FormParam("trainingIds") List<String> trainingIds) throws Exception {

        checkUser(req);

        if(type.equalsIgnoreCase(HTML_TYPE))
            trainingManager.generateHTMLDocument(res, trainingIds);
        else if(type.equalsIgnoreCase(PDF_TYPE))
            trainingManager.generatePDFDocument(res, trainingIds);
    }

    @DELETE
    @Path("{id}")
    public void deleteTraining(
            @Context HttpServletRequest req,
            @PathParam("id") Long trainingId) {

        User user = checkUser(req);
        trainingManager.delete(trainingId, user);
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
    public TrainingVO createOrUpdateTraining(
            @Context HttpServletRequest req, TrainingVO training) throws IOException {

        User user = checkUser(req);
        return trainingManager.createOrUpdate(training, user);
    }

}
