package org.incode.eurocommercial.ecpcrm.camel.processor.mail.transactional;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;

import org.incode.eurocommercial.ecpcrm.camel.processor.ProcessorAbstract;
import org.incode.eurocommercial.ecpcrm.camel.processor.util.MessageUtil;
import org.incode.eurocommercial.ecpcrm.canonical.center.v1.CenterDto;
import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

public class SubscribedToPromotionalEmails extends ProcessorAbstract {
    @Override
    public void process(final Exchange exchange) throws Exception {
        final UserDto userDto = MessageUtil.getHeader(exchange.getIn(), UserDto.class, "default");
        final CenterDto centerDto = MessageUtil.getHeader(exchange.getIn(), CenterDto.class, "default");

        transactionalMailService.sendTemplateMail("Subscription Confirmation", userDto, centerDto);
    }

    @BeanInject TransactionalMailService transactionalMailService;
}
