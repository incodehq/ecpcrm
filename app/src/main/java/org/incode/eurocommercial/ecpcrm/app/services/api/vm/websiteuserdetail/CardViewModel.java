package org.incode.eurocommercial.ecpcrm.app.services.api.vm.websiteuserdetail;

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
        String givenAt = card.getGivenToUserAt() == null ? "null" : card.getGivenToUserAt().toString("yyyy-MM-dd HH:mm:ss");
        String sentAt = card.getSentToUserAt() == null ? "null" : card.getSentToUserAt().toString("yyyy-MM-dd HH:mm:ss");
        return CardViewModel.create(
                card.getNumber(),
                card.getStatus().toString().toLowerCase(),
                card.getCreatedAt().toString("yyyy-MM-dd HH:mm:ss"),
                givenAt,
                sentAt
        );
    }
}
