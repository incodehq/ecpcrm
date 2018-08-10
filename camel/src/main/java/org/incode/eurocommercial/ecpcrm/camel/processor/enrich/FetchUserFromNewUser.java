package org.incode.eurocommercial.ecpcrm.camel.processor.enrich;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;

import org.apache.isis.schema.common.v1.OidDto;
import org.apache.isis.schema.common.v1.ValueDto;
import org.apache.isis.schema.ixn.v1.ActionInvocationDto;
import org.apache.isis.schema.ixn.v1.InteractionDto;

import org.incode.eurocommercial.ecpcrm.camel.processor.ProcessorAbstract;
import org.incode.eurocommercial.ecpcrm.camel.processor.util.ParamUtil;
import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

public class FetchUserFromNewUser extends ProcessorAbstract {
    @Override
    public void process(final Exchange exchange) {
        final ActionInvocationDto invocation = (ActionInvocationDto) exchange.getIn().getBody(InteractionDto.class).getExecution();

        String email = ParamUtil.getParamValue(invocation, "Email", ValueDto::getString);
        String firstName = ParamUtil.getParamValue(invocation, "First Name", ValueDto::getString);
        String lastName = ParamUtil.getParamValue(invocation, "Last Name", ValueDto::getString);
        boolean subscribed = ParamUtil.getParamValue(invocation, "Promotional Emails", ValueDto::isBoolean);
        OidDto centerOid = ParamUtil.getParamValue(invocation, "Center", ValueDto::getReference);

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setCenter(centerOid);
        userDto.setPromotionalEmails(subscribed);

        enrichmentService.enrichMessageWithLocalDto(exchange.getIn(), userDto);
    }

    @BeanInject EnrichmentService enrichmentService;
}
