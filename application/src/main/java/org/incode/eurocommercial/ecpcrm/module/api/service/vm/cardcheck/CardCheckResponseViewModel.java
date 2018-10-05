package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck;

import lombok.Getter;
import lombok.Setter;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractBaseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

public class CardCheckResponseViewModel extends AbstractBaseViewModel {
    @Getter @Setter
    String id;
    @Getter @Setter
    String name;
    @Getter @Setter
    String email;
    @Getter @Setter
    String title;
    @Getter @Setter
    String first_name;
    @Getter @Setter
    String last_name;
    @Getter @Setter
    String birthdate;
    @Getter @Setter
    String optin;
    @Getter @Setter
    boolean game;
    @Getter @Setter
    String card_image;

    public CardCheckResponseViewModel(final Card card) {
        User user = card.getOwner();
        if(user == null) {
            return;
        }

        setId(user.getReference());
        setName(user.getFirstName() + " " + user.getLastName());
        setEmail(user.getEmail());
        setTitle(user.getTitle().toString().toLowerCase());
        setFirst_name(user.getFirstName());
        setLast_name(user.getLastName());
        setBirthdate(asString(user.getBirthDate()));
        setOptin(asString(user.isPromotionalEmails()));
        setGame(card.canPlay());
        setCard_image("");
    }
}
