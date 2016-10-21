package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import lombok.Getter;
import lombok.Setter;

public class CountryImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String code;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String alpha2Code;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String name;

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
//
//        Country country = countryRepository.findCountry(code);
//        if (country != null) {
//            return Lists.newArrayList(country);
//        }
//        return Lists.newArrayList(countryRepository.createCountry(code, alpha2Code, name));
        return Lists.newArrayList();
    }

//    @Inject
//    private CountryRepository countryRepository;

}