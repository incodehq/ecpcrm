package org.incode.eurocommercial.ecpcrm.module.api.service.vm;

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

    public Boolean asBoolean(final String s) {
        if (s.toUpperCase().equals("TRUE")){
            return Boolean.TRUE;
        }
        else if (s.toUpperCase().equals("FALSE")){
            return Boolean.FALSE;
        }
        else {
            return null;
        }
    }

    public Title asTitle(final String title) {
        try {
            return Title.valueOf(title.toUpperCase());
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}
