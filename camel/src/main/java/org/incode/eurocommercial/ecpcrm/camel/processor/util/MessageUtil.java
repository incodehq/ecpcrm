package org.incode.eurocommercial.ecpcrm.camel.processor.util;

import java.io.StringReader;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import org.apache.camel.Message;

import org.apache.isis.applib.util.JaxbUtil;
import org.apache.isis.schema.common.v1.OidDto;
import org.apache.isis.schema.ixn.v1.InteractionDto;
import org.apache.isis.schema.ixn.v1.MemberExecutionDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageUtil {

    public static <T> T getHeader(final Message message, Class<T> dtoClass, final String role) {
        return getHeaderWithRole(message, dtoClass, elseDefault(role));
    }

    private static <T> T getHeaderWithRole(final Message message, final Class<T> dtoClass, final String role) {
        final Map<String,T> headerMap = message.getHeader(dtoClass.getName(), Map.class);
        return headerMap != null? headerMap.get(role): null;
    }

    public static void setHeader(final Message message, Object dto, final String role) {
        setHeaderWithRole(message, dto, elseDefault(role));
    }

    private static void setHeaderWithRole(final Message message, final Object dto, final String role) {
        final Class<?> dtoClass = dto.getClass();
        Map<String,Object> headerMap = message.getHeader(dtoClass.getName(), Map.class);
        if(headerMap == null) {
            headerMap = Maps.newHashMap();
            message.setHeader(dtoClass.getName(), headerMap);
        }
        headerMap.put(role, dto);
    }

    private static String elseDefault (final String role) {
        return Util.coalesce(role, "default");
    }

    public static <T> T getBody(final Message message, final Class<T> dtoClass) {
        final Object body = message.getBody();
        if(body instanceof String && dtoClass != String.class) {
            final String bodyStr = (String) body;
            return JaxbUtil.fromXml(new StringReader(bodyStr), dtoClass);
        }
        return (T) body;
    }

    public static InteractionDto interactionFrom(final Message message) {
        return MessageUtil.getBody(message, InteractionDto.class);
    }
    public static String transactionIdFrom(final Message inMessage) {
        final InteractionDto ixn = interactionFrom(inMessage);
        return ixn.getTransactionId();
    }
    public static int sequenceFrom(final Message inMessage) {
        final InteractionDto ixn = interactionFrom(inMessage);
        final MemberExecutionDto execution = ixn.getExecution();
        return execution != null ? execution.getSequence() : 0;
    }
    public static Optional<OidDto> targetFrom(final Message inMessage) {
        final InteractionDto ixn = interactionFrom(inMessage);
        final MemberExecutionDto execution = ixn.getExecution();
        final OidDto target = execution.getTarget();
        if(target != null && target.getObjectType() != null && target.getObjectIdentifier() != null) {
            return Optional.of(target);
        } else {
            return Optional.empty();
        }
    }

}
