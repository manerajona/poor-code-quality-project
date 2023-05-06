package com.manerajona;

import com.manerajona.common.constants.DatabaseConstants;
import com.manerajona.common.constants.ResourceConstants;
import com.manerajona.config.ServiceLocatorFeature;
import com.manerajona.ports.input.rs.LoanResource;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Application {

    public static void main(String... args) throws Exception {
        databaseMigration();
        startHttpServer();
    }

    private static void databaseMigration() {
        Flyway.configure()
                .dataSource(DatabaseConstants.URL, "root", "password")
                .locations("classpath:db")
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }

    private static void startHttpServer() throws IOException {
        URI uri = URI.create(ResourceConstants.URI);
        ResourceConfig configuration = new ResourceConfig(LoanResource.class, ServiceLocatorFeature.class);

        GrizzlyHttpServerFactory.createHttpServer(uri, configuration).start();
        System.out.println("Server started on " + uri);
    }
}
