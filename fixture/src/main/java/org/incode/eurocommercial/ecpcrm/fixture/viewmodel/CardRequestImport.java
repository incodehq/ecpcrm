package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestType;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

import lombok.Getter;
import lombok.Setter;

public class CardRequestImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String id;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String centerCode;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String requestingUser;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String type;

    @Getter @Setter
    @Property
    private String issueDate;

    @Getter @Setter
    @Property
    private String handleDate;

    @Getter @Setter
    @Property
    private String approved;

    @Getter @Setter
    @Property
    private String assignedCard;

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
        Center center = centerRepository.findByCode(getCenterCode());
        User user = userRepository.findByReference(requestingUser);
        CardRequestType requestType = CardRequestType.valueOf(type);

        CardRequest cardRequest = cardRequestRepository.findOrCreate(user, requestType);

        if(cardRequest == null) {
            return null;
        }

        if(asBoolean(approved)) {
            cardRequest.approve(assignedCard);
//            if(cardRequest.getApproved() == null)
//                cardRequest.deny();
        }

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime issueDateLocalDate = issueDate == null ? cardRequest.getIssueDate() : dtf.parseLocalDateTime(issueDate.replace("T", " ").replace(".000", ""));
        LocalDateTime handleDateLocalDate = handleDate == null ? cardRequest.getHandleDate() : dtf.parseLocalDateTime(handleDate.replace("T", " ").replace(".000", ""));

        cardRequest.setIssueDate(issueDateLocalDate);
        cardRequest.setHandleDate(handleDateLocalDate);

        return null;
    }
    private boolean asBoolean(final String booleanString) {
        return booleanString != null && Integer.parseInt(booleanString) != 0;
    }

    @Inject private CardRepository cardRepository;
    @Inject private CenterRepository centerRepository;
    @Inject private UserRepository userRepository;
    @Inject private CardRequestRepository cardRequestRepository;
}
