package org.ecpcrm.webapp.integtests;

import org.apache.isis.core.integtestsupport.IntegrationTestAbstract3;

import org.incode.eurocommercial.ecpcrm.module.application.EcpCrmApplicationModule;

public class EcpCrmResourceAbstract extends IntegrationTestAbstract3 {

    protected EcpCrmResourceAbstract() {
        super(new EcpCrmApplicationModule());
    }
}

