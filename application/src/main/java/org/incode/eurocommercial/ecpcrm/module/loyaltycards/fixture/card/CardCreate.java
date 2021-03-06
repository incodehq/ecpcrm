package org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.card;

import java.util.Random;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.CardMenu;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class CardCreate extends FixtureScript {

    @Getter @Setter
    private String number;

    @Getter @Setter
    private Center center;

    @Getter
    private Card card;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        if (number == null)
            number = "" + faker.number().randomNumber(13, true);
        if(center == null)
            center = centerRepository.listAll().get(new Random().nextInt(centerRepository.listAll().size()));

        card = wrap(menu).newCard(number(), center());

        ec.addResult(this, card);
    }

    @Inject private CardMenu menu;
    @Inject private CenterRepository centerRepository;
}
