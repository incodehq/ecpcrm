package org.incode.eurocommercial.ecpcrm.module.api.integtests.resource;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.incode.eurocommercial.ecpcrm.module.api.EcpCrmResource;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteUserCardDisableIntegTest extends ApiModuleIntegTestAbstract {
    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    CardRepository cardRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    AuthenticationDeviceRepository authenticationDeviceRepository;

    private EcpCrmResource resource;

    private ApiIntegTestFixture fs;
    private Card card;
    private Center center;
    private User user;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;
    private List<User> userList;
    private List<Card> cardList;

    @Before
    public void setUp() throws Exception {
        // given
        resource = new EcpCrmResourceForTesting();
        serviceRegistry.injectServicesInto(resource);

        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        cardList = cardRepository.findByCenter(center);
        userList = userRepository.findByCenter(center);
        deviceList = authenticationDeviceRepository.findByCenter(center);
        card = cardList.get(new Random().nextInt(cardList.size()));
        user = userList.get(new Random().nextInt(userList.size()));
        device = deviceList.get(new Random().nextInt(deviceList.size()));

        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
        assertThat(device).isNotNull();
        assertThat(card).isNotNull();
    }

    @Test
    public void invalid_device_secret_sadCase(){
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "INVALID_DEV_SECRET";
        String email = user.getEmail();
        String checkCode = ApiService.computeCheckCode(user.getEmail()); //missing in json

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        //when
        Response response = resource.websiteUserCardDisable(deviceName, deviceSecret, requestJson);

        //then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void invalid_parameter_sadCase(){
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = user.getEmail();
        String checkCode = ApiService.computeCheckCode(user.getEmail()); //missing in json

        String requestJson = String.format("{\"email\": \"%s\"}", email);

        //when
        Response response = resource.websiteUserCardDisable(deviceName, deviceSecret, requestJson);

        //then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void invalid_user_sadCase(){
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = user.getEmail() + "NOT_A_REAL_EMAIL_THUS_NO_EXISTING_USER";
        String checkCode = ApiService.computeCheckCode(user.getEmail());
        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        //when
        Response response = resource.websiteUserCardDisable(deviceName, deviceSecret, requestJson);

        //then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_USER, "Invalid user").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void incorrect_checkCode_sadCase(){
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = user.getEmail();
        String checkCode = ApiService.computeCheckCode(user.getEmail()) + "RANDOM_CHECKCODE_NOISE";

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        //when
        Response response = resource.websiteUserCardDisable(deviceName, deviceSecret, requestJson);

        //then
        Response expectedResponse = Result.error(Result.STATUS_INCORRECT_CHECK_CODE, "Incorrect check code").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }


    @Test
    public void if_user_has_card_we_expect_happyCase(){
        // given
        Card cardWithOwner = cardList.stream()
                .filter(c -> c.getOwner() != null)
                .findAny().get();
        cardWithOwner.setStatus(CardStatus.ENABLED);
        User userWithCard = cardWithOwner.getOwner();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = userWithCard.getEmail();
        String checkCode = ApiService.computeCheckCode(userWithCard.getEmail());

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        //when
        Response response = resource.websiteUserCardDisable(deviceName, deviceSecret, requestJson);

        //then
        Response expectedResponse = Result.ok().asResponse();
        for (Card card : user.getCards()){
            System.out.println("CS for nr:" + card.getNumber() + " " + card.getStatus().toString());
            assertThat(card.getStatus()).isNotEqualByComparingTo(CardStatus.ENABLED);
        }
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }
}
