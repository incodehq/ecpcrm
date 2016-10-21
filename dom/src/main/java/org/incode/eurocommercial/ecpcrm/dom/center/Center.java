package org.incode.eurocommercial.ecpcrm.dom.center;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
})
public class Center {

    @Getter @Setter
    @Column(allowsNull = "false")
    private String reference;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String name;

}
