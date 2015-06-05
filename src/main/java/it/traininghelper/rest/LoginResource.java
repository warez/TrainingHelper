package it.traininghelper.rest;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import it.traininghelper.valueobject.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Created by warez on 03/06/15.
 */
@Path("user")
public class LoginResource extends AbstractResource{

    @GET
    @Path("loginInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getLoggedUser(@Context HttpServletRequest req) {

        UserService userService = UserServiceFactory.getUserService();
        User user = getLoggedUser();
        String url;

        if(user != null)
            url = userService.createLogoutURL(req.getRequestURI());
        else
            url = userService.createLoginURL(req.getRequestURI());

        return new UserInfo(user,url);
    }

    @GET
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    public Response login(@Context UriInfo uriInfo) throws Exception {

        User user = getLoggedUser();
        if(user != null) {
            URI uri = uriInfo.getBaseUriBuilder().uri("/").build();
            return Response.seeOther(uri).build();
        }

        UserService userService = UserServiceFactory.getUserService();
        String url = userService.createLoginURL("/");
        URI uri = uriInfo.getBaseUriBuilder().uri(url).build();
        return Response.seeOther(uri).build();

    }

    @GET
    @Path("logout")
    @Produces(MediaType.TEXT_HTML)
    public Response logout(@Context UriInfo uriInfo) throws Exception {

        User user = getLoggedUser();
        if(user == null) {
            URI uri = uriInfo.getBaseUriBuilder().uri("/").build();
            return Response.seeOther(uri).build();
        }

        UserService userService = UserServiceFactory.getUserService();
        String url = userService.createLogoutURL("/");

        URI uri = uriInfo.getBaseUriBuilder().uri(url).build();
        return Response.seeOther(uri).build();

    }

}
