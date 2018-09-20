package org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.wrapper.WrapperFactory;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.incode.eurocommercial.ecpcrm.module.application.fixture.jdbc.Importable;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.UserMenu;

import lombok.Getter;
import lombok.Setter;

public class UserImport implements ExcelFixtureRowHandler, Importable {

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String reference;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String enabled;

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
    private String birthDate;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String address;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String zipcode;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String city;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String phoneNumber;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String centerCode;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String cardNumber;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String promotionalEmails;

    @Getter @Setter
    @Property(optionality = Optionality.MANDATORY)
    private String hasCar;

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
        Title title = Title.valueOf(getTitle());
        Center center = centerRepository.findByCode(getCenterCode());

        User user = userRepository.findByExactEmailAndCenter(getEmail(), center);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate birthDate = getBirthDate() == null ? null : dtf.parseLocalDate(getBirthDate().replace("T", " ").replace(".000", ""));

        if (user == null) {
            try {
                user = wrapperFactory.wrap(userMenu).newUser(
                        asBoolean(getEnabled()),
                        title,
                        StringUtils.trim(getFirstName()),
                        StringUtils.trim(getLastName()),
                        StringUtils.trim(getEmail()),
                        birthDate,
                        StringUtils.trim(getAddress()),
                        StringUtils.trim(getZipcode()),
                        StringUtils.trim(getCity()),
                        StringUtils.trim(getPhoneNumber()),
                        center,
                        null,
                        asBoolean(getPromotionalEmails()),
                        asBoolean(getHasCar())
                );

                if (user.validateNewCard(StringUtils.trim(getCardNumber())) == null) {
                    user.newCard(StringUtils.trim(getCardNumber()));
                }

                if (getReference() != null) {
                    user.setReference(getReference());
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            if ((user.getTitle() == null || user.getTitle() == Title.UNKNOWN_IMPORT) && title != Title.UNKNOWN_IMPORT)
                user.setTitle(title);
            if ((Strings.isNullOrEmpty(user.getFirstName()) || user.getFirstName().equals("UNKNOWN_IMPORT")) && !Strings.isNullOrEmpty(getFirstName()))
                user.setFirstName(getFirstName());
            if ((Strings.isNullOrEmpty(user.getLastName()) || user.getLastName().equals("UNKNOWN_IMPORT")) && !Strings.isNullOrEmpty(getLastName()))
                user.setLastName(getLastName());
            if (user.getBirthDate() == null && birthDate != null)
                user.setBirthDate(birthDate);
            if ((Strings.isNullOrEmpty(user.getAddress()) || user.getAddress().equals("UNKNOWN_IMPORT")) && !Strings.isNullOrEmpty(getAddress()))
                user.setAddress(getAddress());
            if ((Strings.isNullOrEmpty(user.getZipcode()) || user.getZipcode().equals("UNKNOWN_IMPORT")) && !Strings.isNullOrEmpty(getZipcode()))
                user.setZipcode(getZipcode());
            if ((Strings.isNullOrEmpty(user.getCity()) || user.getCity().equals("UNKNOWN_IMPORT")) && !Strings.isNullOrEmpty(getCity()))
                user.setCity(getCity());
            if ((Strings.isNullOrEmpty(user.getPhoneNumber()) || user.getPhoneNumber().equals("UNKNOWN_IMPORT")) && !Strings.isNullOrEmpty(getPhoneNumber()))
                user.setPhoneNumber(getPhoneNumber());
        }

        return null;
    }

    private boolean asBoolean(final String booleanString) {
        return booleanString != null && Integer.parseInt(booleanString) != 0;
    }

    @Inject private UserMenu userMenu;
    @Inject private UserRepository userRepository;
    @Inject private WrapperFactory wrapperFactory;
    @Inject private CenterRepository centerRepository;
}
