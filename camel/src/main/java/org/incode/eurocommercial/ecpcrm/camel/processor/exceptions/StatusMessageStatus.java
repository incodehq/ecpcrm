package org.incode.eurocommercial.ecpcrm.camel.processor.exceptions;

import lombok.Getter;

/**
 * -ve values are an error, +ve values are ok
 *
 * Inspired by the HTTP status codes.
 */
public enum StatusMessageStatus {

    NO_PARTY_IN_CODA(-404),
    NO_BANK_MANDATE_IN_ESTATIO(-440),
    NO_BANK_ACCOUNT_IN_ESTATIO(-442),
    MANDATE_MOVED(-444),
    GENERAL_ERROR(-500),
    CODA_SOAP_FAILURE(-520),
    ESTATIO_REST_FAILURE(-522),
    INVOICE_FOUND(200),
    INVOICE_CREATED(201);

    @Getter
    private final int code;

    StatusMessageStatus(final int code) {
        this.code = code;
    }
}
