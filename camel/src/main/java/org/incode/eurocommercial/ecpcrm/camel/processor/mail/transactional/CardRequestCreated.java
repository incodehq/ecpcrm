package org.incode.eurocommercial.ecpcrm.camel.processor.mail.transactional;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;

import org.apache.isis.schema.common.v1.EnumDto;
import org.apache.isis.schema.common.v1.OidDto;
import org.apache.isis.schema.common.v1.ValueDto;
import org.apache.isis.schema.ixn.v1.ActionInvocationDto;
import org.apache.isis.schema.ixn.v1.InteractionDto;

import org.incode.eurocommercial.ecpcrm.camel.processor.ProcessorAbstract;
import org.incode.eurocommercial.ecpcrm.camel.processor.enrich.EnrichmentService;
import org.incode.eurocommercial.ecpcrm.camel.processor.util.ParamUtil;
import org.incode.eurocommercial.ecpcrm.canonical.center.v1.CenterDto;
import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

public class CardRequestCreated extends ProcessorAbstract {
    @Override
    public void process(final Exchange exchange) throws Exception {
        final ActionInvocationDto invocation = (ActionInvocationDto) exchange.getIn().getBody(InteractionDto.class).getExecution();

        OidDto userOid = ParamUtil.getParamValue(invocation, "User", ValueDto::getReference);
        EnumDto typeEnum = ParamUtil.getParamValue(invocation, "Type", ValueDto::getEnum);

        UserDto userDto = enrichmentService.retrieveDto(exchange.getIn(), userOid, UserDto.class);
        CenterDto centerDto = enrichmentService.retrieveDto(exchange.getIn(), userDto.getCenter(), CenterDto.class);

        if (typeEnum.getEnumName().equals("SEND_TO_HOME")) {
            transactionalMailService.sendTemplateMail("Card Request Received", userDto, centerDto);
        } else {
            transactionalMailService.sendTemplateMail("Card Pickup", userDto, centerDto);
        }
    }

    @BeanInject TransactionalMailService transactionalMailService;
    @BeanInject EnrichmentService enrichmentService;
}
