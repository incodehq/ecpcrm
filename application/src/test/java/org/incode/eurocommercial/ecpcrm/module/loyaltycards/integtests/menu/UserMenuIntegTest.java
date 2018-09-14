package org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.menu;

import javax.inject.Inject;

import org.docx4j.wml.U;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.LoyaltyCardModuleIntegTestAbstract;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.user.UserRepositoryIntegTest;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.UserMenu;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMenuIntegTest extends LoyaltyCardModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;
    @Inject UserRepository userRepository;
    @Inject CenterRepository centerRepository;

    LoyaltyCardsIntegTestFixture fs;
    User user;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new LoyaltyCardsIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(0);
        assertThat(user).isNotNull();
    }

    public static class AssignUserCardValidation extends UserMenuIntegTest {
        @Test
        public void assign_card_to_user_which_is_already_in_use() throws Exception{
            //given
            int centerCode = 10;
            Center center = centerRepository.findOrCreate("0" + centerCode, "New Center", "" + centerCode, null, null);
            String cardToGive = center.nextValidCardNumber();

            //when
            User userWithCard = userRepository.listAll().get(0);
            userWithCard.newCard(cardToGive);

            //then
            expectedExceptions.expectMessage("Card with number " + cardToGive + " doesn't exist");
            UserMenu userMenu = new UserMenu();
            wrap(userMenu).newUser(true, Title.MR, "fname", "lname", "email", null, null, null, null, null, center, cardToGive, false, null);
        }
    }

}
