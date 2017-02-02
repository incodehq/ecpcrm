package org.incode.eurocommercial.ecpcrm.fixture.dom.card;

import java.util.Random;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardMenu;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class CardCreate extends FixtureScript {

    @Getter @Setter
    private String number;

    @Getter @Setter
    private CardStatus status;

    @Getter @Setter
    private Center center;

    @Getter
    private Card card;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        number = defaultParam("number", ec,
                "" + faker.number().randomNumber(13, true));
        status = defaultParam("status", ec, CardStatus.ENABLED);
        if(center == null) {
            center = defaultParam("center", ec,
                    centerRepository.listAll().get(
                            new Random().nextInt(centerRepository.listAll().size())));
        }

        card = wrap(menu).newCard(number(), status(), center());

        ec.addResult(this, card);
    }

    @Inject CardMenu menu;
    @Inject CenterRepository centerRepository;
}
