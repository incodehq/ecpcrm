package org.incode.eurocommercial.ecpcrm.camel.processor.exceptions;

import org.isisaddons.module.publishmq.dom.statusclient.StatusMessage;

import lombok.Getter;

public class ExceptionWithStatusMessage extends RuntimeException {

    @Getter
    private final StatusMessage statusMessage;

    public ExceptionWithStatusMessage(
            final StatusMessage statusMessage) {
        this(statusMessage, null);
    }

    public ExceptionWithStatusMessage(
            final StatusMessage statusMessage, final Exception cause) {
        super(messageFrom(statusMessage, cause), cause);
        this.statusMessage = statusMessage;
    }

    private static String messageFrom(final StatusMessage statusMessage, final Exception cause) {
        return statusMessage != null &&
               statusMessage.getMessage() != null &&
               statusMessage.getMessage().value != null
                    ? statusMessage.getMessage().value
                    : cause != null
                            ? cause.getMessage() != null
                                ? cause.getMessage()
                                : cause.getClass().getSimpleName()
                            : "Failed";
    }
}
