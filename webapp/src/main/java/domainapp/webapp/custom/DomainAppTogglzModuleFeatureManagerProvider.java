package domainapp.webapp.custom;

import org.isisaddons.module.togglz.glue.spi.TogglzModuleFeatureManagerProviderAbstract;

import domainapp.dom.DomainAppFeature;

/**
 * Registered in META-INF/services, as per http://www.togglz.org/documentation/advanced-config.html
 */
public class DomainAppTogglzModuleFeatureManagerProvider extends TogglzModuleFeatureManagerProviderAbstract {

    public DomainAppTogglzModuleFeatureManagerProvider() {
        super(DomainAppFeature.class);
    }

}