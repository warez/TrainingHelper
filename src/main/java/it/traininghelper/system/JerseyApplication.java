package it.traininghelper.system;

import it.traininghelper.rest.LoginResource;
import it.traininghelper.rest.TrainingResource;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by warez on 30/05/15.
 */
public class JerseyApplication extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(TrainingResource.class);
        s.add(LoginResource.class);
        return s;
    }

}
