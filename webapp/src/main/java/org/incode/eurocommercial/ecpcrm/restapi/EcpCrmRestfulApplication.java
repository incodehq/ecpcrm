package org.incode.eurocommercial.ecpcrm.restapi;

import org.apache.isis.viewer.restfulobjects.server.RestfulObjectsApplication;

public class EcpCrmRestfulApplication extends RestfulObjectsApplication {

    public EcpCrmRestfulApplication() {
        super();
        addClass(EcpCrmResource.class);
    }
}
