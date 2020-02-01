package eti.query.demonstration;

import eti.query.demonstration.country.control.CountryResource;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/demo")
public class DemoApplication extends Application {

    /**
     * Set all the classes needed for launching this rest application
     *
     * @return the Set containing the specified classes.
     */
    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(CountryResource.class);
        return resources;
    }
}