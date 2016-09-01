package domainapp.fixture.dom.quick;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import domainapp.dom.quick.QuickObject;
import domainapp.dom.quick.QuickObjectRepository;
import lombok.Getter;
import lombok.Setter;

public class QuickObjectRowHandler implements ExcelFixtureRowHandler {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer integer;

    @Getter @Setter
    private LocalDate localDate;

    @Getter @Setter
    private Boolean flag;

    @Override
    public List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final ExcelFixture excelFixture,
            final Object previousRow) {

        final QuickObject quickObject = repository.findOrCreate(name);

        quickObject.setFlag(getFlag());
        quickObject.setInteger(getInteger());
        quickObject.setLocalDate(getLocalDate());

        executionContext.addResult(excelFixture, quickObject);
        return Collections.singletonList(quickObject);
    }
    private static double random(final double from, final double to) {
        return Math.random() * (to-from) + from;
    }


    @Inject
    QuickObjectRepository repository;

}
