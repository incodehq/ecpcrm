package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractBaseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Gender;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildViewModel_Test extends AbstractBaseViewModel {
    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Test
    public void child_happyCase() {
        //given
        final Child child = new Child();
        child.setBirthdate(LocalDate.parse("1970-01-01"));
        child.setGender(Gender.FEMALE);

        //when
        final ChildViewModel viewModel = new ChildViewModel(child);

        //then
        assertThat(viewModel.getBirthdate()).isEqualTo(asString(child.getBirthdate()));
        assertThat(viewModel.getGenre()).isEqualTo(child.getGender().getValue());
        assertThat(viewModel.getAge()).isEqualTo(asString(Years.yearsBetween(child.getBirthdate(), LocalDate.now()).getYears()));
    }

    @Test
    public void child_birthDate_is_null() {
        //given
        final Child child = new Child();
        child.setBirthdate(null);
        child.setGender(Gender.FEMALE);

        //when
        final ChildViewModel viewModel = new ChildViewModel(child);
        
        //then
        assertThat(viewModel.getBirthdate()).isNull();
    }
}
