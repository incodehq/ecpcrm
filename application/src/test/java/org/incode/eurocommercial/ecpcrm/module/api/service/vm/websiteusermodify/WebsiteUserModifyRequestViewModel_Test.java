package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusermodify;

import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteUserModifyRequestViewModel_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    WrapperFactory mockWrapperFactory;

    @Test
    public void isValid_happyCase(){
        //given
        WebsiteUserModifyRequestViewModel viewModel = new WebsiteUserModifyRequestViewModel(
                "07fb5fcd2ed6e8d3d9656dbce491fc76",
                "fooCardNumber",
                "foo@bar.com",
                "mr",
                "fooLastName",
                "fooFirstName",
                "fooBirthdate",
                null,
                null,
                null,
                "fooAddress",
                null,
                null,
                "fooPhonenumber",
                "true",
                null
        );

        viewModel.wrapperFactory = mockWrapperFactory;

        final User user = new User();
        user.setAddress("foo@bar.com");
        user.setPromotionalEmails(false);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        context.checking(new Expectations() {{
            oneOf(mockWrapperFactory).wrap(user); //different promotional email params make sure that we'll call the wrapper factory
            will(returnValue(user));
        }});

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);

    }


    @Test
    public void isValid_302_invalidParameter_sadCase(){
        //given
        WebsiteUserModifyRequestViewModel viewModel = new WebsiteUserModifyRequestViewModel(
                null, //wrong check code
                "fooCardNumber",
                "foo@bar.com",
                "mr",
                "fooLastName",
                "fooFirstName",
                "fooBirthdate",
                null,
                null,
                null,
                "fooAddress",
                null,
                null,
                "fooPhonenumber",
                "true",
                null
        );

        viewModel.wrapperFactory = mockWrapperFactory;

        final User user = new User();

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_PARAMETER);

    }

    @Test
    public void isValid_304_invalid_user_sadCase(){
        //given
        WebsiteUserModifyRequestViewModel viewModel = new WebsiteUserModifyRequestViewModel(
                "07fb5fcd2ed6e8d3d9656dbce491fc76",
                "fooCardNumber",
                "foo@bar.com",
                "mr",
                "fooLastName",
                "fooFirstName",
                "fooBirthdate",
                null,
                null,
                null,
                "fooAddress",
                null,
                null,
                "fooPhonenumber",
                "true",
                null
        );

        viewModel.wrapperFactory = mockWrapperFactory;

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        //when
        final Result result = viewModel.isValid(device, null);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_USER);

    }

}
