package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;
import lombok.Getter;
import lombok.Setter;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractBaseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;

public class CardViewModel extends AbstractBaseViewModel {
    @Getter @Setter
    String number;
    @Getter @Setter
    String status;
    @Getter @Setter
    String created_at;
    @Getter @Setter
    String given_at;
    @Getter @Setter
    String sent_at;

    public CardViewModel(final Card card) {
        setNumber(card.getNumber());
        setStatus(card.getStatus().toString().toLowerCase());
        setCreated_at(asDateString(card.getCreatedAt()));
        setGiven_at(asDateString(card.getGivenToUserAt()));
        setSent_at(asDateString(card.getSentToUserAt()));
    }
}
