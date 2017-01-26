package org.incode.eurocommercial.ecpcrm.restapi;

import lombok.Data;

@Data(staticConstructor = "create")
public class CardViewModel {
    private final String number;
    private final String status;
    private final String created_at;
    private final String given_at;
    private final String sent_at;
}
