/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.dom.numerator;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Programmatic;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@Queries({
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator "
                        + "WHERE name == :name")
})
@DomainObject(
        editing = Editing.DISABLED
)
public class Numerator {

    public String title() {
        return getName() + " - " + format(getLastIncrement());
    }

    @Column(allowsNull = "false")
    @Getter @Setter
    private String name;

    @Column(allowsNull = "false")
    @Getter @Setter
    private String format;

    private String format(final long n) {
        return String.format(getFormat(), n);
    }

    @Persistent
    @Column(allowsNull = "false")
    @Getter @Setter
    private long lastIncrement;

    // //////////////////////////////////////

    @Programmatic
    public String nextIncrementStr() {
        return format(incrementCounter());
    }

    @Programmatic
    public String lastIncrementStr(){
        return format(getLastIncrement());
    }

    private long incrementCounter() {
        long next = getLastIncrement() + 1;
        setLastIncrement(next);
        return next;
    }
}
