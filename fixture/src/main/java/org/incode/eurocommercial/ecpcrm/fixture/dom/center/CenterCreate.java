package org.incode.eurocommercial.ecpcrm.fixture.dom.center;

import javax.inject.Inject;

import org.apache.wicket.util.string.Strings;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterMenu;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class CenterCreate extends FixtureScript {

    private FakeDataService faker;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String reference;

    @Getter
    private Center center;

    public CenterCreate() {
        faker = new FakeDataService();
        faker.init();

        name = faker.lorem().sentence(2);
        reference = Strings.toString(faker.ints().any());
    }

    @Override
    protected void execute(final ExecutionContext ec) {
        this.center = wrap(menu).newCenter(reference, name);
        ec.addResult(this, center);
    }

    @Inject
    CenterMenu menu;
}
