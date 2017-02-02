package org.incode.eurocommercial.ecpcrm.restapi;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;

import lombok.Data;

@Data(staticConstructor = "create")
public class CardViewModel {
    private final String number;
    private final String status;
    private final String created_at;
    private final String given_at;
    private final String sent_at;

    public static CardViewModel fromCard(final Card card) {
        return CardViewModel.create(
                card.getNumber(),
                card.getStatus().toString().toLowerCase(),
                "unknown",
                "unknown",
                "unknown"
        );
    }
}
