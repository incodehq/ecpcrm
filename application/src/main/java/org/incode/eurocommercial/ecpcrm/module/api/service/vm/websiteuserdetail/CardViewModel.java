package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;

import lombok.Data;

@Data(staticConstructor = "create")
public class CardViewModel {
    private final String number;
    private final String status;
    private final String created_at;
    private final String given_at;
    private final String sent_at;

    public static CardViewModel fromCard(final Card card) {
        String createdAt = ApiService.asDateString(card.getCreatedAt());
        String givenAt = ApiService.asDateString(card.getGivenToUserAt());
        String sentAt = ApiService.asDateString(card.getSentToUserAt());
        return CardViewModel.create(
                card.getNumber(),
                card.getStatus().toString().toLowerCase(),
                createdAt,
                givenAt,
                sentAt
        );
    }
}
