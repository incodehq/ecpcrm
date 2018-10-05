package org.incode.eurocommercial.ecpcrm.module.api.service.vm;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public abstract class AbstractBaseViewModel {

    public String asString(final int i) {
        return "" + i;
    }

    public String asString(final boolean bool) {
        return bool ? "true" : "false";
    }

    public String asString(final LocalDate localDate) {
        return localDate == null ? null : localDate.toString("dd/MM/yyyy");
    }

    public String asDateString(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toString("dd/MM/yyyy");
    }

}
