package org.incode.eurocommercial.ecpcrm.fixture.dom.center;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterMenu;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class CenterCreate extends FixtureScript {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String code;

    @Getter @Setter
    private String id;

    @Getter
    private Center center;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        name = defaultParam("name", ec,
                faker.gameOfThrones().city());
        code = defaultParam("code", ec,
                "0" + faker.number().randomNumber(2, true));
        id = defaultParam("id", ec,
                "" + faker.number().randomNumber(2, true));

        center = wrap(menu).newCenter(code(), name(), id());

        ec.addResult(this, center);
    }

    @Inject CenterMenu menu;
}
