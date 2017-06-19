package org.incode.eurocommercial.ecpcrm.dom.center;

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
    public String nextValidCardNumberOld() {

        long largestCardNumberSoFar = numerator.getLastIncrement();

        //split off last digit / checksum
        long withoutChecksum = largestCardNumberSoFar / 10;

        //increment card-number, and stringify
        String cardNumber = (withoutChecksum + 1) + "";

        //calculate checksum
        int[] digits = cardNumber.chars()
                .map(Character::getNumericValue)
                .toArray();
        int[] multipliers = {1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3};

        int sum = 0;

        for(int i = 0; i < digits.length; i++) {
            sum += digits[i] * multipliers[i];
        }

        int key = (10 - sum % 10) % 10;

        //attach checksum for new card-number
        return cardNumber + key;
    }

    @Programmatic
    public String nextValidCardNumber() {

        long largestCardNumberSoFar = numerator.getLastIncrement();

        //split off last digit i.e. remove checksum; then increment
        long withoutChecksum, temp;
        withoutChecksum = temp = (largestCardNumberSoFar / 10) + 1;

        //Calculate the weighted sum of the first 12 digits of the card-number
        int sum = 0;
        //reversed order (%10 -> /10 method produces array of digits in reversed order)
        int[] multipliers = {3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1};

        for(int i = 0; i < 12; ++i) {
            sum += multipliers[i] * (int)(temp % 10);
            temp /= 10;
        }

        int checksum = (10 - sum % 10) % 10;

        //attach checksum
        return withoutChecksum + "" + checksum;
    }

    @Inject CardRepository cardRepository;

}
