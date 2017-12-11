package org.incode.eurocommercial.ecpcrm.app.services.api.vm.websiteuserdetail;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;

import lombok.Data;
import static org.incode.eurocommercial.ecpcrm.app.services.api.ApiService.asString;

@Data(staticConstructor = "create")
public class ChildViewModel {
    private final String age;
    private final String birthdate;
    private final String genre;

    public static ChildViewModel fromChild(Child child) {
        int age = Years.yearsBetween(child.getBirthdate(), LocalDate.now()).getYears();
        return ChildViewModel.create(
                asString(age),
                asString(child.getBirthdate()),
                child.getGender().getValue()
        );
    }
}
