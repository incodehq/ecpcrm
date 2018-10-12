package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websitecardrequest;

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

public class WebsiteCardRequestRequestViewModel_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    UserRepository mockUserRepository;

    @Test
    public void isValid_happyCase(){
        WebsiteCardRequestRequestViewModel viewModel = new WebsiteCardRequestRequestViewModel(
                "fooOrigin",
                "fooCenterId",
                "mr", // throws exception if not mr/mrs/etc
                "John",
                "Doe",
                "johndoe@crm.com",
                "fooPassword",
                "fooBirthday",
                null,
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                null,
                "f124564196312c55b4f001f457f3c1f3", // printed, needs to be correct
                null,
                null
        );

        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        //expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter(viewModel.getEmail(), device.getCenter());
            will(returnValue(new User()));
        }});


        // when
        final Result result = viewModel.isValid(device, null);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);
    }

    @Test
    public void isValid_302_invalid_parameter_sadCase(){
        WebsiteCardRequestRequestViewModel viewModel = new WebsiteCardRequestRequestViewModel(
                "fooOrigin",
                "fooCenterId",
                "oogieBoogie", // invalid
                "John",
                "Doe",
                "johndoe@crm.com",
                "fooPassword",
                "fooBirthday",
                null,
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                null,
                "f124564196312c55b4f001f457f3c1f3", // printed, needs to be correct
                null,
                null
        );

        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        // when
        final Result result = viewModel.isValid(device, null);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_PARAMETER);
    }

    @Test
    public void isValid_invalid_user_sadCase(){
        WebsiteCardRequestRequestViewModel viewModel = new WebsiteCardRequestRequestViewModel(
                "fooOrigin",
                "fooCenterId",
                "mr", // throws exception if not mr/mrs/etc
                "John",
                "Doe",
                "johndoe@crm.com",
                "fooPassword",
                "fooBirthday",
                null,
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                null,
                "f124564196312c55b4f001f457f3c1f3", // printed, needs to be correct
                null,
                null
        );

        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        //expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter(viewModel.getEmail(), device.getCenter());
            will(returnValue(null)); //invalid user
        }});

        // when
        final Result result = viewModel.isValid(device, null);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_USER);
    }

    @Test
    public void isValid_incorrect_checkCode_sadCase(){
        WebsiteCardRequestRequestViewModel viewModel = new WebsiteCardRequestRequestViewModel(
                "fooOrigin",
                "fooCenterId",
                "mr", // throws exception if not mr/mrs/etc
                "John",
                "Doe",
                "johndoe@crm.com",
                "fooPassword",
                "fooBirthday",
                null,
                null,
                null,
                "fooAddress",
                "fooZipcode",
                "fooCity",
                null,
                null,
                "fooCheckCode", // printed, needs to be correct
                null,
                null
        );

        viewModel.userRepository = mockUserRepository;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        //expected
        context.checking(new Expectations() {{
            oneOf(mockUserRepository).findByExactEmailAndCenter(viewModel.getEmail(), device.getCenter());
            will(returnValue(new User())); //invalid user
        }});

        // when
        final Result result = viewModel.isValid(device, null);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INCORRECT_CHECK_CODE);
    }

}
