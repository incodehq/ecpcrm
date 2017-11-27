package org.incode.eurocommercial.ecpcrm.fixture.dom.authentication.card;

import java.util.Random;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDeviceMenu;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class AuthenticationDeviceCreate extends FixtureScript {
    @Getter @Setter
    private Center center;

    @Getter @Setter
    private AuthenticationDeviceType type;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String secret;

    @Getter @Setter
    private boolean active;

    @Getter
    private AuthenticationDevice device;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        if(center == null) {
            center = defaultParam("center", ec,
                    centerRepository.listAll().get(
                            new Random().nextInt(centerRepository.listAll().size())));
        }
        type = defaultParam("number", ec, AuthenticationDeviceType.APP);
        name = defaultParam("name", ec, center.getName());
        secret = defaultParam("secret", ec, center.getCode() + center.getName() + center.getId());
        active = defaultParam("active", ec, true);

        device = wrap(menu).newAuthenticationDevice(center, type, name, secret, active);

        ec.addResult(this, device);
    }

    @Inject AuthenticationDeviceMenu menu;
    @Inject CenterRepository centerRepository;
}
