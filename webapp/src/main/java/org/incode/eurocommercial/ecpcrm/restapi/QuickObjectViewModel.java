package org.incode.eurocommercial.ecpcrm.restapi;

import lombok.Data;

@Data(staticConstructor = "create")
public class QuickObjectViewModel {

    private final String name;
    private final Integer integer;
    private final String localDate;


}
