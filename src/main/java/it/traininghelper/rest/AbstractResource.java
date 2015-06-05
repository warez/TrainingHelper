package it.traininghelper.rest;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by warez on 04/06/15.
 */
public abstract class AbstractResource {

    protected User checkUser(@Context HttpServletRequest req) {
        User user = getLoggedUser();
        if (user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        return user;
    }

    protected User getLoggedUser() {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        return user;
    }

}
