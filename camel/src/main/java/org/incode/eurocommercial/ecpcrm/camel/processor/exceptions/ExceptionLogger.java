package org.incode.eurocommercial.ecpcrm.camel.processor.exceptions;

import com.google.common.base.Throwables;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.isisaddons.module.publishmq.dom.statusclient.StatusMessage;
import org.isisaddons.module.publishmq.dom.statusclient.StatusMessageClient;

import org.incode.eurocommercial.ecpcrm.camel.processor.util.MessageUtil;

import lombok.Setter;

public class ExceptionLogger implements Processor {

    @Setter
    protected StatusMessageClient statusMessageClient;

    @Override
    public void process(final Exchange exchange) {

        final Object camelExceptionCaught = exchange.getProperties().get(Exchange.EXCEPTION_CAUGHT);
        final Exception exceptionIfAny = exchange.getException();
        final Exception exception =
                exceptionIfAny != null
                        ? exceptionIfAny
                        : camelExceptionCaught instanceof Exception
                            ? (Exception) camelExceptionCaught
                            : null;
        if(exception != null) {
            final StatusMessage statusMessage = statusMessageFrom(exception, exchange);
            statusMessageClient.log(statusMessage);
        }
    }

    private static StatusMessage statusMessageFrom(
            final Exception exception,
            final Exchange exchange) {

        if(exception instanceof ExceptionWithStatusMessage) {
            final ExceptionWithStatusMessage ewsm = (ExceptionWithStatusMessage) exception;
            return ewsm.getStatusMessage();
        }

        final Message message = exchange.getIn();
        final String transactionId = MessageUtil.transactionIdFrom(message);
        final int sequence = MessageUtil.sequenceFrom(message);

        final String stackTrace = Throwables.getStackTraceAsString(exception);
        String exceptionMessage = exception.getMessage();
        if(exceptionMessage == null) {
            exceptionMessage = exception.getClass().getSimpleName();
        }

        final StatusMessage.Builder messageBuilder = StatusMessage.builder(transactionId, sequence, exceptionMessage)
                .withDetail(stackTrace);
        MessageUtil.targetFrom(message).ifPresent(
                target -> messageBuilder.withOid(target.getObjectType(), target.getObjectIdentifier())
        );
        return messageBuilder.build();
    }

}
