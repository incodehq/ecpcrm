package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardgame;

import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.game.CardGame;
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

public class CardGameRequestViewModel_Test {

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
        CardGameRequestViewModel viewModel = new CardGameRequestViewModel(
                "2050000000065",
                "fooGame",
                "fooDesc",
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

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_OK);
    }

    @Test
    public void isValid_302_invalid_parameter_sadCase(){
        //given
        CardGameRequestViewModel viewModel = new CardGameRequestViewModel(
                null, //cardNumber is null
                "fooGame",
                "fooDesc",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(true);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_PARAMETER);
    }

    @Test
    public void isValid_invalid_card_sadCase(){
        //given
        CardGameRequestViewModel viewModel = new CardGameRequestViewModel(
                "2050000000065",
                "fooGame",
                "fooDesc",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(true);
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(null)); //card not found
        }});

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_CARD);
    }

    @Test
    public void isValid_invalid_user_sadCase(){
        //given
        CardGameRequestViewModel viewModel = new CardGameRequestViewModel(
                "2050000000065",
                "fooGame",
                "fooDesc",
                null
        );

        viewModel.cardRepository = mockCardRepository;

        final User user = new User();
        user.setEnabled(false); //user not enabled
        user.setTitle(Title.MR);

        Center center = new Center();
        AuthenticationDevice device = new AuthenticationDevice();
        device.setCenter(center);

        Card card = new Card();
        card.setStatus(CardStatus.ENABLED);
        card.setCenter(center);
        card.setOwner(user);

        // expected
        context.checking(new Expectations() {{
            oneOf(mockCardRepository).findByExactNumber(viewModel.getCardNumber());
            will(returnValue(card));
        }});

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_INVALID_USER);
    }

    @Test
    public void isValid_card_has_been_played_sadCase(){
        //given
        CardGameRequestViewModel viewModel = new CardGameRequestViewModel(
                "2050000000065",
                "fooGame",
                "fooDesc",
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
            will(returnValue(new CardGame())); //return an already found cardgame for "today"
            oneOf(mockClockService).now();
            will(returnValue(searchDate));
        }});

        //when
        final Result result = viewModel.isValid(device, user);

        //then
        assertThat(result.getStatus()).isEqualTo(Result.STATUS_CARD_ALREADY_PLAYED);
    }

}
