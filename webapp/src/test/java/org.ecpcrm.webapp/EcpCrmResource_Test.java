package org.ecpcrm.webapp;

import javax.ws.rs.core.Response;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.restapi.EcpCrmResource;

public class EcpCrmResource_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock Response mockResponse;



    @Test @Ignore
    public void testCardRequest() throws Exception{
        //given
        EcpCrmResource ecpCrmResource = new EcpCrmResource();

        //when
        Response resp = ecpCrmResource.cardRequest("deviceNameYea", "devSecret", "piofjiwfjiof");

        //then
        Response resp_302 = Result.error(302, "Invalid parameter").asResponse();
        assert(resp).equals(resp_302);

//        context.checking(new Expectations(){{
//            oneOf(mockResponse)
//
//        }});
    }

}
