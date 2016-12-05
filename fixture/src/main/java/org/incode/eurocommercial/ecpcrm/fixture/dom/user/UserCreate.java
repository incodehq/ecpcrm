package org.incode.eurocommercial.ecpcrm.fixture.dom.user;

import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.wicket.util.string.Strings;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserMenu;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class UserCreate extends FixtureScript {

    @Getter @Setter
    private boolean enabled;

    @Getter @Setter
    private Title title;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private Center center;

    @Getter @Setter
    private String cardNumber;

    @Getter @Setter
    private boolean promotionalEmails;

    @Getter
    private User user;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        enabled = defaultParam("enabled", ec, true);
        title = defaultParam("title", ec, Title.values()[ThreadLocalRandom.current().nextInt(0, Title.values().length)]);
        firstName = defaultParam("firstName", ec, faker.name().firstName());
        lastName = defaultParam("lastName", ec, faker.name().lastName());
        email = defaultParam("email", ec, faker.internet().emailAddress((firstName() + "." + lastName())).toLowerCase());
        center = defaultParam("center", ec, centerRepository.listAll().get(ThreadLocalRandom.current().nextInt(0, centerRepository.listAll().size())));
        cardNumber = defaultParam("cardNumber", ec, Strings.toString(faker.number().randomNumber(13, true)));
        promotionalEmails = defaultParam("promotionalEmails", ec, faker.bool().bool());

        this.user = wrap(menu).newUser(enabled(), title(), firstName(), lastName(), email(), center(), cardNumber(), promotionalEmails());
        ec.addResult(this, user);
    }


    @Inject
    UserMenu menu;

    @Inject
    CenterRepository centerRepository;
}
