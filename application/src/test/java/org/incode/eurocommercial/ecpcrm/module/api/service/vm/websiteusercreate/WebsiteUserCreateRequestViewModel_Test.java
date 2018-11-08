package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteUserCreateRequestViewModel_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    UserRepository mockUserRepository;

    @Test
    public void isValid_happyCase() {
        // given
        WebsiteUserCreateRequestViewModel viewModel = new WebsiteUserCreateRequestViewModel(
                "fooUserId",
                "fooNumber",
                "fooCenterId",
                "f124564196312c55b4f001f457f3c1f3", // printed, needs to be correct
                "mr", // throws exception if not mr/mrs/etc
                "John",
                "Doe",
                "johndoe@crm.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "true",
                null
        );

        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);
        User user = new User();

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter(viewModel.getEmail(), device.getCenter());
            will(returnValue(null));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);
    }

    @Test
    public void isValid_302_cast_title_cast_invalid_parameter_sadCase(){
        // given
        WebsiteUserCreateRequestViewModel viewModel = new WebsiteUserCreateRequestViewModel(
                "fooUserId",
                "fooNumber",
                "fooCenterId",
                "f124564196312c55b4f001f457f3c1f3",
                "fooWrongTitle", // returns null which will 302
                "John",
                "Doe",
                "johndoe@crm.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "true",
                null
        );
        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);
        User user = new User();

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_PARAMETER);

    }

    @Test
    public void isValid_402_wrong_check_code_sadCase(){
        // given
        WebsiteUserCreateRequestViewModel viewModel = new WebsiteUserCreateRequestViewModel(
                "fooUserId",
                "fooNumber",
                "fooCenterId",
                "fooWrongCheckCode",
                "mr", // returns null which will 302
                "John",
                "Doe",
                "johndoe@crm.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "true",
                null
        );
        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);
        User user = new User();

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INCORRECT_CHECK_CODE);

    }

    @Test
    public void isValid_305_user_exists_sadCase() {
        // given
        WebsiteUserCreateRequestViewModel viewModel = new WebsiteUserCreateRequestViewModel(
                "fooUserId",
                "fooNumber",
                "fooCenterId",
                "f124564196312c55b4f001f457f3c1f3", // printed, needs to be correct
                "mr", // throws exception if not mr/mrs/etc
                "John",
                "Doe",
                "johndoe@crm.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "true",
                null
        );

        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);
        User user = new User();

        // expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter(viewModel.getEmail(), device.getCenter());
            will(returnValue(new User()));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_EMAIL_ALREADY_EXISTS);
    }
}
