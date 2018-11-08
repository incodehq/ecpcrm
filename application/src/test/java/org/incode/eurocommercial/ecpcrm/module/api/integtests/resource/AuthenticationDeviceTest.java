package org.incode.eurocommercial.ecpcrm.module.api.integtests.resource;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationDeviceTest extends ApiModuleIntegTestAbstract {

    @Inject FixtureScripts fixtureScripts;

    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    private ApiIntegTestFixture fs;
    private Card card;
    private Center center;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));

        center = card.getCenter();

        deviceList = authenticationDeviceRepository.findByCenter(center);
        device = deviceList.get(new Random().nextInt(deviceList.size()));

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
        assertThat(device).isNotNull();
    }

    @Before
    public void add_non_active_auth_device() throws Exception {
        authenticationDeviceRepository.findOrCreate(center, AuthenticationDeviceType.APP, "not_active", "secret", false);
    }


    @Test
    public void cannot_delete_device_if_active() throws Exception {
        //give auth device which is enabled
        //expect
        expectedExceptions.expectMessage("This authentication device cannot be deleted because it is active.");

        //when
        wrap(device).delete();

    }

    @Test
    public void pass_deleting_device_if_not_active() throws Exception {
        //given
        long size_before_deletion = authenticationDeviceRepository.listAll().stream().count();
        AuthenticationDevice not_active_device = authenticationDeviceRepository.findByNameAndSecret("not_active", "secret");

        //when
        wrap(not_active_device).delete();

        //then
        long size_after_deletion = authenticationDeviceRepository.listAll().stream().count();
        assertThat(size_before_deletion).isGreaterThan(size_after_deletion);
    }

}