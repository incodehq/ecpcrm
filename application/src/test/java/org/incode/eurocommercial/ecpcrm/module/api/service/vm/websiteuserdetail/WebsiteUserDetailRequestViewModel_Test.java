package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteUserDetailRequestViewModel_Test {
    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Test
    public void isValid_happyCase(){
        //given
        final User user = new User();
        user.setAddress("foo@bar.com");

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        WebsiteUserDetailRequestViewModel viewModel = new WebsiteUserDetailRequestViewModel(
                "foo@bar.com",
                "07fb5fcd2ed6e8d3d9656dbce491fc76"
        );

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);

    }

    @Test
    public void isValid_302_sadCase (){
        //given
        final User user = new User();
        user.setAddress("foo@bar.com");

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        WebsiteUserDetailRequestViewModel viewModel = new WebsiteUserDetailRequestViewModel(
                null, //email null, which causes invalid parameter
                "07fb5fcd2ed6e8d3d9656dbce491fc76"
        );

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_PARAMETER);

    }

    @Test
    public void isValid_304_sadCase (){
        //given
        final User user = null; //null user will case test to invalid user

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        WebsiteUserDetailRequestViewModel viewModel = new WebsiteUserDetailRequestViewModel(
                "foo@bar.com",
                "07fb5fcd2ed6e8d3d9656dbce491fc76"
        );

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_USER);
    }

    @Test
    public void isValid_402_sadCase (){
        //given
        final User user = new User();
        user.setAddress("foo@bar.com");

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        WebsiteUserDetailRequestViewModel viewModel = new WebsiteUserDetailRequestViewModel(
                "foo@bar.com",
                "fooCheckCode" //false checkcode will case fail
        );

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INCORRECT_CHECK_CODE);
    }

}
