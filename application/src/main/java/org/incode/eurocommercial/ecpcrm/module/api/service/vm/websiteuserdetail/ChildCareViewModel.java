package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.Child;

import lombok.Data;

@Data(staticConstructor = "create")
public class ChildCareViewModel {
    private final String names;
    private final int count;

    public static ChildCareViewModel fromChild(Child child) {
        int age = Years.yearsBetween(child.getBirthdate(), LocalDate.now()).getYears();
        return ChildCareViewModel.create(
                child.getName(),
                0
        );
    }
}
