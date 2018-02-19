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

    @Getter @Setter
    private Boolean hasCar;

    @Getter
    private User user;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        enabled = defaultParam("enabled", ec, true);
        if (title == null)
            title = Title.values()[new Random().nextInt(Title.values().length)];
        if (firstName == null)
            firstName = faker.name().firstName();
        if (lastName == null)
            lastName = faker.name().lastName();
        if (email == null)
            email = faker.internet().emailAddress((firstName() + "." + lastName())).toLowerCase();
        if (address == null)
            address = faker.address().streetAddress();
        if (zipcode == null)
            zipcode = faker.address().zipCode();
        if (city == null)
            city = faker.address().city();
        if (phoneNumber == null)
            phoneNumber = faker.phoneNumber().cellPhone();
        if(center == null)
            center = centerRepository.listAll().get(new Random().nextInt(centerRepository.listAll().size()));
        if (cardNumber == null) {
            List<Card> availableCards = cardRepository.findByCenter(center()).stream()
                    .filter(c -> c.getOwner() == null)
                    .collect(Collectors.toList());
            if (availableCards.size() > 0)
                cardNumber = availableCards.get(new Random().nextInt(availableCards.size())).getNumber();
        }
        promotionalEmails = defaultParam("promotionalEmails", ec, faker.bool().bool());
        if (hasCar == null)
            hasCar = faker.bool().bool();

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
                promotionalEmails(),
                hasCar()
        );

        ec.addResult(this, user);
    }


    @Inject private UserMenu menu;
    @Inject private CenterRepository centerRepository;
    @Inject private CardRepository cardRepository;
}
