package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import lombok.Getter;
import lombok.Setter;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractBaseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.Child;
import org.joda.time.LocalDate;
import org.joda.time.Years;


public class ChildViewModel extends AbstractBaseViewModel {
    @Getter @Setter
    String age;
    @Getter @Setter
    String birthdate;
    @Getter @Setter
    String genre;

    public ChildViewModel(Child child){
        setAge(child.getBirthdate() == null ? null : asString(Years.yearsBetween(child.getBirthdate(), LocalDate.now()).getYears()));
        setBirthdate(asString(child.getBirthdate()));
        setGenre(child.getGender().getValue());
    }
}
