package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate;

import lombok.Getter;
import lombok.Setter;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

public class WebsiteUserCreateResponseViewModel {
    @Getter @Setter
    String user_id;
    @Getter @Setter
    String number;

    public WebsiteUserCreateResponseViewModel(final User user) {
        Card card = user.getCards().first();
        String cardNumber = card != null ? card.getNumber() : "";

        setUser_id(user.getReference());
        setNumber(cardNumber);
    }
}
