package org.incode.eurocommercial.ecpcrm.module.application.service.api.vm.websiteuserdetail;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.module.application.service.api.ApiService;

import lombok.Data;
import static org.incode.eurocommercial.ecpcrm.module.application.service.api.ApiService.asString;

@Data(staticConstructor = "create")
public class ChildViewModel {
    private final String age;
    private final String birthdate;
    private final String genre;

    public static ChildViewModel fromChild(Child child) {
        int age = Years.yearsBetween(child.getBirthdate(), LocalDate.now()).getYears();
        return ChildViewModel.create(
                ApiService.asString(age),
                ApiService.asString(child.getBirthdate()),
                child.getGender().getValue()
        );
    }
}
