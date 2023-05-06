package com.manerajona.config;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.HK2RuntimeException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.DuplicatePostProcessor;

import java.io.IOException;

public class ServiceLocatorFeature implements Feature {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public boolean configure(FeatureContext context) {
        try {
            serviceLocator.getService(DynamicConfigurationService.class)
                    .getPopulator()
                    .populate(
                            new ClasspathDescriptorFileFinder(this.getClass().getClassLoader()),
                            new DuplicatePostProcessor()
                    );
        } catch (IOException e) {
            System.out.println("Failed configuring services");
            e.printStackTrace();
            throw new HK2RuntimeException(e);
        }
        return true;
    }

}