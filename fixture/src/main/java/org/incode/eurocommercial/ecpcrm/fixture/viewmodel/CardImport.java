package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;

import lombok.Getter;
import lombok.Setter;

public class CardImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String number;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String status;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String userId;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String clientId;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String centerReference;

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
        CardStatus status = CardStatus.valueOf(getStatus());
        Center center = centerRepository.findByReference(getCenterReference());

        cardRepository.newCard(
                getNumber(),
                status,
                getClientId(),
                center
        );

        return null;
    }

    @Inject
    private CardRepository cardRepository;

    @Inject
    private CenterRepository centerRepository;
}