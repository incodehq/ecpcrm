package org.incode.eurocommercial.ecpcrm.webapp.custom;

import org.isisaddons.module.togglz.glue.spi.TogglzModuleFeatureManagerProviderAbstract;

import org.incode.eurocommercial.ecpcrm.dom.EcpCrmFeature;

/**
 * Registered in META-INF/services, as per http://www.togglz.org/documentation/advanced-config.html
 */
public class EcpCrmTogglzModuleFeatureManagerProvider extends TogglzModuleFeatureManagerProviderAbstract {

    public EcpCrmTogglzModuleFeatureManagerProvider() {
        super(EcpCrmFeature.class);
    }

}