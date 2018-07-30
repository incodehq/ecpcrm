package org.incode.eurocommercial.ecpcrm.camel.processor;

import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.util.JaxbUtil;
import org.apache.isis.schema.ixn.v1.InteractionDto;

import org.isisaddons.module.publishmq.dom.servicespi.PublisherServiceUsingActiveMq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assume.assumeThat;

public class E2e_Test {

    PublisherServiceUsingActiveMq service;

    @Before
    public void setUp() throws Exception {

        assumeThat(System.getProperty("e2e"), is(notNullValue()));

        service = new PublisherServiceUsingActiveMq();
        final HashMap<String, String> props = Maps.newHashMap();

        props.put(PublisherServiceUsingActiveMq.KEY_VM_TRANSPORT_URL, "tcp://localhost:61616");
        service.init(props);
    }

    @Test
    public void publish() throws Exception {

        final URL resource = Resources.getResource(getClass(), "InteractionDto.xml");
        final String s = Resources.toString(resource, Charsets.UTF_8);
        final InteractionDto dto = JaxbUtil.fromXml(new StringReader(s), InteractionDto.class);

        service.republish(dto);
    }


}