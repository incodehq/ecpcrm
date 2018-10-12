package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck;

import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardCheckRequestViewModel_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    CardRepository mockCardRepository;

    @Mock
    CardGameRepository mockCardGameRepository;

    @Mock
    ClockService mockClockService;

    @Test
    public void isValid_happyCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "2050000000065",
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(true);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        Card card = new Card();
        card.setStatus(CardStatus.ENABLED);
        card.setCenter(center);
        card.setOwner(user);
        card.setCardGameRepository(mockCardGameRepository);
        card.setClockService(mockClockService);

        LocalDate searchDate = LocalDate.parse("2018-01-01");

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(card));
            oneOf(mockCardGameRepository).findByCardAndDate(card, searchDate);
            will(returnValue(null));
            oneOf(mockClockService).now();
            will(returnValue(searchDate));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);
    }


    @Test
    public void isValid_319_outdated_card_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "2050000000065",
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(true);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        Card card = new Card();
        card.setStatus(CardStatus.TOCHANGE);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(card));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OUTDATED_CARD);
    }

    @Test
    public void isValid_303_invalid_card_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "2050000000065",
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(true);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        Card card = new Card();
        card.setStatus(CardStatus.DISABLED);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(card));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_CARD);
    }

    @Test
    public void isValid_317_unequal_center_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "2050000000065",
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(true);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        Card card = new Card();
        card.setStatus(CardStatus.ENABLED);
        card.setCenter(new Center());

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(card));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_CARD_CENTER_NOT_EQUAL_TO_DEVICE_CENTER);
    }

    @Test
    public void isValid_304_invalid_user_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "2050000000065",
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(false);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        Card card = new Card();
        card.setStatus(CardStatus.ENABLED);
        card.setCenter(center);


        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(card));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_USER);
    }

    @Test
    public void isValid_319_outdated_startsWith_card_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "3922000000065",
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(false);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center); //device does not have type


        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(null));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OUTDATED_CARD);
    }

    @Test
    public void isValid_303_invalid_card_number_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "fooNumber", //invalid card number
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(false);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(null));
            oneOf(mockCardRepository).cardNumberIsValid("fooNumber");
            will(returnValue(false));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_CARD);
    }

    @Test
    public void isValid_314_unable_to_bind_card_to_user_sadCase(){
        //given
        CardCheckRequestViewModel  viewModel = new CardCheckRequestViewModel(
                "fooNumber", //invalid card number
                "fooOrigin",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(false);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(null));
            oneOf(mockCardRepository).cardNumberIsValid("fooNumber");
            will(returnValue(true));
        }});

        // when
        final Result result = viewModel.isValid(device, user);

        // then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_UNABLE_TO_BIND_USER);
    }
    
}
