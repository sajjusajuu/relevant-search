package com.swiggy.search;

import com.swiggy.search.api.Api;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Hello world!
 *
 */
public class App extends Application<ServerConf>  {

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    @Override
    public void run(ServerConf serverConf, Environment environment) throws Exception {
        final Api resource = new Api();
        environment.jersey().register(resource);

        final ServerHealthCheck healthCheck =
                new ServerHealthCheck();
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

    }
}
