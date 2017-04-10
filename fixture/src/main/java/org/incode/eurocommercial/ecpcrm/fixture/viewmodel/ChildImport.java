package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCareRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

import lombok.Getter;
import lombok.Setter;

public class ChildImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String name;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String gender;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String birthdate;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String parentReference;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String notes;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String startTime;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String endTime;

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
        User user = userRepository.findByReference(parentReference);
        if(user == null)
            return null;

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate birthdate = dtf.parseLocalDate(getBirthdate().replace("T", " ").replace(".000", ""));
        LocalDateTime checkIn = getStartTime() == null ? null : dtf.parseLocalDateTime(getStartTime().replace("T", " ").replace(".000", ""));
        LocalDateTime checkOut = getEndTime() == null ? null : dtf.parseLocalDateTime(getEndTime().replace("T", " ").replace(".000", ""));
        Gender gender = Gender.valueOf(getGender());

        Child child = childRepository.findOrCreate(getName(), gender, birthdate, user);
        if(checkIn != null) {
            child.checkIn();
            ChildCare childCare = childCareRepository.findActiveChildCareByChild(child);
            childCare.setCheckIn(checkIn);
            childCare.setCheckOut(checkOut);
        }

        return null;
    }

    @Inject private ChildRepository childRepository;
    @Inject private ChildCareRepository childCareRepository;
    @Inject private UserRepository userRepository;
}
