package it.traininghelper.system;

/**
 * Created by warez on 29/05/15.
 */

import com.googlecode.objectify.ObjectifyService;
import it.traininghelper.dataobject.Training;
import it.traininghelper.dataobject.TrainingImage;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run.  This is
 * required to let JSP's access Ofy.
 **/
public class OfyHelper implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        ObjectifyService.register(Training.class);
        ObjectifyService.register(TrainingImage.class);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}