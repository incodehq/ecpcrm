package org.incode.eurocommercial.ecpcrm.app.services.api.vm.cardcheck;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.user.User;

import lombok.Data;
import static org.incode.eurocommercial.ecpcrm.app.services.api.ApiService.asString;

@Data(staticConstructor = "create")
public class CardCheckResponseViewModel {
    private final String id;
    private final String name;
    private final String email;
    private final String title;
    private final String first_name;
    private final String last_name;
    private final String birthdate;
    private final String optin;
    private final boolean game;
    private final String card_image;

    public static CardCheckResponseViewModel fromCard(final Card card) {
        User user = card.getOwner();
        if(user == null) {
            return null;
        }

        return CardCheckResponseViewModel.create(
                user.getReference(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getTitle().toString().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                asString(user.getBirthDate()),
                asString(user.isPromotionalEmails()),
                card.canPlay(),
                ""
        );
    }
}
