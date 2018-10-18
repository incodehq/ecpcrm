package org.incode.eurocommercial.ecpcrm.module.api.service.vm;

import com.google.common.base.Strings;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import javax.swing.text.html.Option;
import java.util.Optional;

public abstract class AbstractRequestViewModel extends AbstractBaseViewModel {

    abstract public Result isValid(AuthenticationDevice device, User user);

    public LocalDate asLocalDate(final String localDate) {
        try {
            return DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(localDate);
        } catch (Exception e) {
            // See ECPCRM-173 => birthdate is optional, no need to error if we can't parse it.
            return null;
        }
    }

    public Boolean asBoolean(String s) {
        s = Strings.nullToEmpty(s);
        switch (s.toUpperCase()) {
            case "TRUE":
                return Boolean.TRUE;
            case "FALSE":
                return Boolean.FALSE;
            default:
                return null;
        }
    }

    public Title asTitle(final String title) {
        try {
            return Title.valueOf(title.toUpperCase());
        }
        catch(Exception e){
            return null;
        }
    }
}
