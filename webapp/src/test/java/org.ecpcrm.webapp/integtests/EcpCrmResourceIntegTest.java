package org.ecpcrm.webapp.integtests;

import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.restapi.EcpCrmResource;

public class EcpCrmResourceIntegTest extends  EcpCrmResourceAbstract {

    @Test @Ignore
    public void testCardRequest() throws Exception {
        //given
        EcpCrmResource ecpCrmResource = new EcpCrmResource();

        //when
        Response resp = ecpCrmResource.cardRequest("deviceNameYea", "devSecret", "piofjiwfjiof");

        //then
        Response resp_302 = Result.error(302, "Invalid parameter").asResponse();
        assert (resp).equals(resp_302);
    }

}
