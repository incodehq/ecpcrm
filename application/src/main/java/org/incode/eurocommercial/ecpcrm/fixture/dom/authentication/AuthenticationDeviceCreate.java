package org.incode.eurocommercial.ecpcrm.fixture.dom.authentication;

import java.util.Random;

import javax.inject.Inject;

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
        if (center == null)
            center = centerRepository.listAll().get(new Random().nextInt(centerRepository.listAll().size()));
        if (type == null)
            type = AuthenticationDeviceType.APP;
        if (name == null)
            name = center.getName();
        if (secret == null)
            secret = center.getCode() + center.getName() + center.getId();
        active = defaultParam("active", ec, true);

        device = wrap(menu).newAuthenticationDevice(center, type, name, secret, active);

        ec.addResult(this, device);
    }

    @Inject private AuthenticationDeviceMenu menu;
    @Inject private CenterRepository centerRepository;
}
