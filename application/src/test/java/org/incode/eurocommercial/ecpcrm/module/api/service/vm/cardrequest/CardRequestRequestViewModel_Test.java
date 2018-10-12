package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardrequest;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRequestRequestViewModel_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    CardRequestRepository mockCardRequestRepository;

    @Mock
    UserRepository mockUserRepository;

    @Test
    public void isValid_happyCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                "fooOrigin",
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                "fooCheckItem",
                "false",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(null));
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);

    }

    @Test
    public void isValid_302_invalid_parameter_sadCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                null, //invalid parameter
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                "fooCheckItem",
                "false",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        User user = new User();


        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(user));
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_PARAMETER);

    }

    @Test
    public void isValid_307_duplicate_request_sadCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                "fooOrigin",
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                "fooCheckItem",
                "true",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        User user = new User();

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(user));
            oneOf(mockCardRequestRepository).findByUser(user);
            will(returnValue(Arrays.asList(new CardRequest()))); //already finds lost card request
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_DUPLICATE_REQUEST_FOR_REPLACEMENT_LOST_CARD);

    }

    @Test
    public void isValid_318_email_exists_valid_check_ask_lost_sadCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                "fooOrigin",
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                null,
                "false",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        User user = new User();
        user.setFirstName("fooFirstName"); //same firstname
        user.setLastName("fooLastName"); //same lastname

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(user));
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_EMAIL_ALREADY_EXISTS_VALID_CHECK_ASK_IF_LOST);

    }


    @Test
    public void isValid_305_email_exists_non_valid_ask_lost_sadCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                "fooOrigin",
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                null,
                "false",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        User user = new User();
        user.setFirstName("barFirstName"); //different firstname
        user.setLastName("barLastName"); //different lastname

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(user));
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_EMAIL_ALREADY_EXISTS);

    }

    @Test
    public void isValid_318_email_exists_same_checkItem_valid_ask_lost_sadCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                "fooOrigin",
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                "fooFirstName fooLastName", //checkitem same as name
                "false",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        User user = new User();
        user.setFirstName("fooFirstName"); //same
        user.setLastName("fooLastName"); //same

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(user));
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_EMAIL_ALREADY_EXISTS_VALID_CHECK_ASK_IF_LOST);

    }

    @Test
    public void isValid_305_email_exists_same_checkItem_valid_ask_lost_sadCase() {
        CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel(
                "fooOrigin",
                "fooHostess",
                "mr",
                "fooFirstName",
                "fooLastName",
                "johndoe@crm.com",
                null,
                "",
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                "true",
                "fooCheckIetem", //checkitem makes non valid check
                "false",
                null,
                null
        );

        viewModel.cardRequestRepository = mockCardRequestRepository;
        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        User user = new User();
        user.setFirstName("fooFirstName");
        user.setLastName("fooLastName");

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter("johndoe@crm.com", device.getCenter());
            will(returnValue(user));
        }});

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_EMAIL_ALREADY_EXISTS_INVALID_CHECK);

    }
}
