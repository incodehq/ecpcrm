package org.incode.eurocommercial.ecpcrm.fixture.dom.user;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
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
    private String address;

    @Getter @Setter
    private String zipcode;

    @Getter @Setter
    private String city;

    @Getter @Setter
    private String phoneNumber;

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
        title = defaultParam("title", ec,
                Title.values()[new Random().nextInt(Title.values().length)]);
        firstName = defaultParam("firstName", ec, faker.name().firstName());
        lastName = defaultParam("lastName", ec, faker.name().lastName());
        email = defaultParam("email", ec,
                faker.internet().emailAddress((firstName() + "." + lastName())).toLowerCase());
        address = defaultParam("address", ec, faker.address().streetAddress());
        zipcode = defaultParam("zipcode", ec, faker.address().zipCode());
        city = defaultParam("city", ec, faker.address().city());
        phoneNumber = defaultParam("phoneNumber", ec, faker.phoneNumber().cellPhone());
        center = defaultParam("center", ec,
                centerRepository.listAll().get(
                        new Random().nextInt(centerRepository.listAll().size())));

        cardNumber = null;
        /* Only cards without owners can be asssigned */
        List<Card> availableCards = cardRepository.findByCenter(center()).stream()
                .filter(c -> c.getOwner() == null)
                .collect(Collectors.toList());

        if(availableCards.size() > 0) {
            cardNumber = defaultParam("cardNumber", ec,
                    availableCards.get(
                            new Random().nextInt(availableCards.size())).getNumber());
        }

        promotionalEmails = defaultParam("promotionalEmails", ec, faker.bool().bool());

        this.user = wrap(menu).newUser(
                enabled(),
                title(),
                firstName(),
                lastName(),
                email(),
                address(),
                zipcode(),
                city(),
                phoneNumber(),
                center(),
                cardNumber(),
                promotionalEmails()
        );

        ec.addResult(this, user);
    }


    @Inject UserMenu menu;
    @Inject CenterRepository centerRepository;
    @Inject CardRepository cardRepository;
}
