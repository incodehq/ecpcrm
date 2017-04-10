package org.incode.eurocommercial.ecpcrm.dom.center;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
        @Query(
                name = "findByExactName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.center.Center "
                        + "WHERE name == :name "),
        @Query(
                name = "findByNameContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.center.Center "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @Query(
                name = "findByReference", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.center.Center "
                        + "WHERE reference == :reference ")
})
@DomainObject(bounded = true)
public class Center implements Comparable<Center>, HasAtPath {

    @Override
    public int compareTo(final Center other) {
        return ObjectContracts.compare(this, other, "reference", "name");
    }

    public String title() {
        return getName();
    }

    @Getter @Setter
    @Column(allowsNull = "false")
    private String reference;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String name;

    @Getter @Setter
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.EVERYWHERE)
    private Numerator numerator;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String atPath;


    @Programmatic
    public String nextValidCardNumber() {
        /* Save last increment */
        BigInteger lastIncrement = numerator.getLastIncrement();

        /* Increment cardNumber, until a valid number has been found */
        String cardNumber;
        do {
            cardNumber = numerator.nextIncrementStr();
        } while(!cardRepository.cardNumberIsValid(cardNumber, reference));

        /* Reset back to last increment */
        numerator.setLastIncrement(lastIncrement);
        return cardNumber;
    }

    @Inject CardRepository cardRepository;

}
