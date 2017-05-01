/**
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.eurocommercial.ecpcrm.dom.child;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCareRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@Queries({
        @Query(
                name = "findByParent", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.child.Child "
                        + "WHERE parent == :parent "),
        @Query(
                name = "findByParentAndName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.child.Child "
                        + "WHERE parent == :parent "
                        + "&& name == :name")
})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        paged = 1000
)
public class Child implements Comparable<Child>, HasAtPath {

    @Override
    public int compareTo(final Child other) {
        return ObjectContracts.compare(this, other, "parent", "name", "birthdate");
    }

    public String title() {
        return getName();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String name;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private Gender gender;

    @Column(allowsNull = "true")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDate birthdate;

    @Column(allowsNull = "false")
    @Property
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private User parent;

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(multiLine = 6)
    @Getter @Setter
    private String notes;

    // region > childCares

    @Persistent(mappedBy = "child", dependentElement = "true")
    @Collection
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<ChildCare> childCares = new TreeSet<>();

    @Action
    public Child checkIn() {
        ChildCare childCare = childCareRepository.findOrCreate(this);
        if(childCare != null) {
            getChildCares().add(childCare);
        }
        return this;
    }

    @Action
    public Child checkOut() {
        ChildCare childCare = childCareRepository.findActiveChildCareByChild(this);
        if(childCare != null) {
            childCare.doCheckOut();
        }
        return this;
    }

    public boolean hideCheckIn() {
        return childCareRepository.findActiveChildCareByChild(this) != null;
    }

    public boolean hideCheckOut() {
        return childCareRepository.findActiveChildCareByChild(this) == null;
    }

    // endregion > childCares

    @Override public String getAtPath() {
        return getParent().getAtPath();
    }

    @Inject ChildCareRepository childCareRepository;
}
