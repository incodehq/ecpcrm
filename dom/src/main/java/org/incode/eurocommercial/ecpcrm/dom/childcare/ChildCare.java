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
package org.incode.eurocommercial.ecpcrm.dom.childcare;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@Queries({
        @Query(
                name = "findByChild", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare "
                        + "WHERE child == :child "),
        @Query(
                name = "findByDateRange", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare "
                        + "WHERE child.parent.center == :center "
                        + "&& checkIn >= :start "
                        + "&& checkIn <= :end"),
        @Query(
                name = "findByChildAndDateRange", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare "
                        + "WHERE child == :child "
                        + "&& checkIn >= :start "
                        + "&& checkIn <= :end"),
        @Query(
                name = "findActiveChildCareByChild", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare "
                        + "WHERE child == :child "
                        + "&& checkOut == null"),
        @Query(
                name = "findActiveChildCares", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare "
                        + "WHERE child.parent.center == :center "
                        + "&& checkOut == null")
})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        paged = 1000
)
public class ChildCare implements Comparable<ChildCare>, HasAtPath {
    @Override
    public int compareTo(final ChildCare other) {
        return ObjectContracts.compare(this, other, "child", "checkIn");
    }

    public String title() {
        return getChild().getName() + " - " + getCheckIn().toLocalDate().toString();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private Child child;

    @Column(allowsNull = "false")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime checkIn;

    @Column(allowsNull = "true")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime checkOut;

    @Action
    @ActionLayout(named = "Check Out")
    public ChildCare doCheckOut() {
        if(getCheckOut() == null)
            setCheckOut(clockService.nowAsLocalDateTime());

        return this;
    }

    public boolean hideCheckOut() {
        return getCheckOut() == null;
    }

    public boolean hideDoCheckOut() {
        return getCheckOut() != null;
    }

    @Override public String getAtPath() {
        return getChild().getAtPath();
    }

    @Inject ClockService clockService;
}
