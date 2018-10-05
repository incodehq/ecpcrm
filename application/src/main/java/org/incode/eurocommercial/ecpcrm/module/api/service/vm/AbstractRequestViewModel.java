package org.incode.eurocommercial.ecpcrm.module.api.service.vm;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public abstract class AbstractRequestViewModel extends AbstractBaseViewModel {

    abstract public Result isValid(AuthenticationDevice device, User user);

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

}
