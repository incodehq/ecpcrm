package org.incode.eurocommercial.ecpcrm.camel.processor;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;

import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.apache.isis.applib.util.JaxbUtil;
import org.apache.isis.schema.ixn.v1.InteractionDto;

import org.isisaddons.module.publishmq.dom.statusclient.StatusMessageClient;

import org.incode.eurocommercial.ecpcrm.camel.processor.util.Util;

import lombok.Setter;

public abstract class ProcessorAbstract implements Processor {

    @Setter
    protected StatusMessageClient statusMessageClient;

    protected String transactionIdFrom(final Message inMessage) {
        final InteractionDto ixn = (InteractionDto) inMessage.getBody();
        return ixn.getTransactionId();
    }

    /**
     * Convenience for subclasses.
     */
    protected static <T> T getHeader(final Message message, Class<T> dtoClass, final String role) {
        final Map<String,T> headerMap = message.getHeader(dtoClass.getName(), Map.class);
        return headerMap != null? headerMap.get(role): null;
    }
    /**
     * Convenience for subclasses., 
     */
    public static void setHeader(final Message message, Object dto, final String role) {
        final String className = dto.getClass().getName();
        Map<String,Object> headerMap = message.getHeader(className, Map.class);
        if(headerMap == null) {
            headerMap = Maps.newHashMap();
            message.setHeader(className, headerMap);
        }
        headerMap.put(role, dto);
    }

    protected static String elseDefault (final String x) {
        return Util.coalesce(x, "default");
    }


    /**
     * Convenience for subclasses.
     */
    protected static String toXml(Object dto) {
        try {

            final JAXBContext context = JAXBContext.newInstance(dto.getClass());
            final Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final StringWriter sw = new StringWriter();
            marshaller.marshal(dto, sw);
            final String xml = sw.toString();

            return xml;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience for subclasses.
     */
    protected <T> T fromXml(final String resourceName, final Class<T> cls) throws java.io.IOException {
        return JaxbUtil.fromXml(getClass(), resourceName, Charsets.UTF_8, cls);
    }


}
