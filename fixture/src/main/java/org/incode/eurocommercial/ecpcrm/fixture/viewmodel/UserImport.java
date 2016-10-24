package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

import lombok.Getter;
import lombok.Setter;

public class UserImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String enabled;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String gender;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String title;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String firstName;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String lastName;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String email;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String centerReference;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String cardNumber;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String promotionalEmails;

    @Override
    public List<Class> importAfter() {
        return Lists.newArrayList();
    }

    @Override
    public List<Object> handleRow(final FixtureScript.ExecutionContext executionContext, final ExcelFixture excelFixture, final Object previousRow) {
        return importData(previousRow);
    }

    @Override
    public List<Object> importData(Object previousRow) {

        System.out.println(getEmail());

        Gender gender = Gender.valueOf(getGender());
        Title title = Title.valueOf(getTitle());
        Center center = centerRepository.findByRreference(getCenterReference());
        //TODO Find or create card

        userRepository.newUser(asBoolean(getEnabled()), gender, title, getFirstName(), getLastName(), getEmail(), center, null, asBoolean(getPromotionalEmails()));


        return null;
    }

    private boolean asBoolean(final String enabled1) {
        if (enabled1 == null){
            return false;
        }
        return Integer.parseInt(enabled1) != 0;
    }

    @Inject
    private UserRepository userRepository;

    @Inject
    private CenterRepository centerRepository;
}