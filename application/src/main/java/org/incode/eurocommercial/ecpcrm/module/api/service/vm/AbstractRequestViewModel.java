package org.incode.eurocommercial.ecpcrm.module.api.service.vm;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public abstract class AbstractRequestViewModel {

    abstract public Result isValid(AuthenticationDevice device);

    public LocalDate asLocalDate(final String localDate) {
        try {
            return DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(localDate);
        } catch (IllegalArgumentException e) {
            // See ECPCRM-173 => birthdate is optional, no need to error if we can't parse it.
            return null;
        }
    }

    public Boolean asBoolean(final String s) {
        return s == null ? null : s.toUpperCase().equals("TRUE");
    }

    public Title asTitle(final String title) {
        return title == null ? null : Title.valueOf(title.toUpperCase());
    }

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
