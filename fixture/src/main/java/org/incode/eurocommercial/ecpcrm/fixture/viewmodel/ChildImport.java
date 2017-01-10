package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
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
    private String parentReference;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String notes;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String checkIn;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String checkOut;

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

        Child child = childRepository.findOrCreate(getName(), user);

        return null;
    }

    @Inject private ChildRepository childRepository;
    @Inject private UserRepository userRepository;
}