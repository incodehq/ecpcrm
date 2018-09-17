package org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.menu;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.LoyaltyCardModuleIntegTestAbstract;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.UserMenu;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMenuIntegTest extends LoyaltyCardModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;
    @Inject UserMenu userMenu;
    @Inject CardRepository cardRepository;

    LoyaltyCardsIntegTestFixture fs;
    User user;
    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new LoyaltyCardsIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(0);
        center = user.getCenter();
        assertThat(user).isNotNull();
    }

    public static class NewUserValidation extends UserMenuIntegTest {
        @Test
        public void cant_assign_already_owned_card_to_user() throws Exception {
            // given
            String cardToGive = center.nextValidCardNumber();
            user.newCard(cardToGive);
            Card card = cardRepository.findByExactNumber(cardToGive);

            // then
            expectedExceptions.expectMessage("Card with number " + cardToGive + " is already assigned to " + card.getOwner().getEmail());

            // when
            wrap(userMenu).newUser(
                    true,
                    Title.MR,
                    "fname",
                    "lname",
                    "email@domain.com",
                    null,
                    null,
                    null,
                    null,
                    null,
                    center,
                    cardToGive,
                    false,
                    null
            );
        }

        @Test
        public void cant_create_duplicate_user() throws Exception {
            //given
            String email = user.getEmail();

            //then
            expectedExceptions.expectMessage("User with email " + email + " already exists");

            // when
            wrap(userMenu).newUser(
                    true,
                    Title.MR,
                    "fname",
                    "lname",
                    email,
                    null,
                    null,
                    null,
                    null,
                    null,
                    center,
                    null,
                    false,
                    null
            );
        }

        @Test
        public void cant_assign_invalid_card_to_user() throws Exception {
            //given
            String cardNumber = "1";

            //then
            expectedExceptions.expectMessage("Card number " + cardNumber + " is invalid");

            // when
            wrap(userMenu).newUser(
                    true,
                    Title.MR,
                    "fname",
                    "lname",
                    "email@domain.com",
                    null,
                    null,
                    null,
                    null,
                    null,
                    center,
                    cardNumber,
                    false,
                    null
            );
        }

        @Test
        public void user_with_white_space_in_email() throws Exception {
            // given
            String badEmail = "email _ withWhiteSpace@domain.com";

            // then
            expectedExceptions.expectMessage("This email address is invalid");

            // when
            wrap(userMenu).newUser(
                    true,
                    Title.MR,
                    "fname",
                    "lname",
                    badEmail,
                    null,
                    null,
                    null,
                    null,
                    null,
                    center,
                    null,
                    false,
                    null
            );
        }

        @Test
        public void user_with_no_top_level_domain_in_email() throws Exception {
            // given
            String badEmail = "email@domain";

            // then
            expectedExceptions.expectMessage("This email address is invalid");

            // when
            wrap(userMenu).newUser(
                    true,
                    Title.MR,
                    "fname",
                    "lname",
                    badEmail,
                    null,
                    null,
                    null,
                    null,
                    null,
                    center,
                    null,
                    false,
                    null
            );
        }

        @Test
        public void user_with_no_at_in_email() throws Exception {
            // given
            String badEmail = "emailatdomain.com";

            // then
            expectedExceptions.expectMessage("This email address is invalid");

            // when
            wrap(userMenu).newUser(
                    true,
                    Title.MR,
                    "fname",
                    "lname",
                    badEmail,
                    null,
                    null,
                    null,
                    null,
                    null,
                    center,
                    null,
                    false,
                    null
            );
        }
    }
}
