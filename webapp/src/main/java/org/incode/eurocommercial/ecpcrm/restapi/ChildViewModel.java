package org.incode.eurocommercial.ecpcrm.restapi;

import lombok.Data;

@Data(staticConstructor = "create")
public class ChildViewModel {
    private final String age;
    private final String birthdate;
    private final String genre;
}
