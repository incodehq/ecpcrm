/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package domainapp.dom.quick;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "quick",
        table = "QuickObject"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.quick.QuickObject "),
        @javax.jdo.annotations.Query(
                name = "findByNameContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.quick.QuickObject "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.quick.QuickObject "
                        + "WHERE name == :name ")
})
@javax.jdo.annotations.Unique(name="QuickObject_name_UNQ", members = {"name"})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
        // ,cssClassFa = "fa-flag" // use the .png instead
)
public class QuickObject implements Comparable<QuickObject> {


    public TranslatableString title() {
        return TranslatableString.tr("Object: {name}", "name", getName());
    }

    @Override
    public int compareTo(final QuickObject other) {
        return ObjectContracts.compare(this, other, "name");
    }


    @javax.jdo.annotations.Column(allowsNull="false", length = 40)
    @Property
    @Getter @Setter
    private String name;

    @javax.jdo.annotations.Column(allowsNull="true")
    @Property
    @Getter @Setter
    private Integer integer;

    @javax.jdo.annotations.Column(allowsNull="true")
    @Property
    @Getter @Setter
    private LocalDate localDate;

    @javax.jdo.annotations.Column(allowsNull="true")
    @Property
    @Getter @Setter
    private Boolean flag;





    public static class UpdateNameDomainEvent extends ActionDomainEvent<QuickObject> { }
    @Action(domainEvent = UpdateNameDomainEvent.class)
    public QuickObject updateName(
            @Parameter(maxLength = 40)
            final String newName) {
        setName(newName);
        return this;
    }
    public String default0UpdateName() {
        return getName();
    }
    public TranslatableString validateUpdateName(final String name) {
        return name.contains("!")? TranslatableString.tr("Exclamation mark is not allowed"): null;
    }


    public static class UpdateIntegerDomainEvent extends ActionDomainEvent<QuickObject> { }
    @Action(domainEvent = UpdateIntegerDomainEvent.class)
    public QuickObject updateInteger(
            final Integer newInteger) {
        setInteger(newInteger);
        return this;
    }
    public Integer default0UpdateInteger() {
        return getInteger();
    }


    public static class UpdateLocalDateDomainEvent extends ActionDomainEvent<QuickObject> { }
    @Action(domainEvent = UpdateLocalDateDomainEvent.class)
    public QuickObject updateLocalDate(
            final LocalDate newLocaldate) {
        setLocalDate(newLocaldate);
        return this;
    }
    public LocalDate default0UpdateLocalDate() {
        return getLocalDate();
    }


    public static class UpdateBooleanDomainEvent extends ActionDomainEvent<QuickObject> { }
    @Action(domainEvent = UpdateBooleanDomainEvent.class)
    public QuickObject updateFlag(
            final Boolean newFlag) {
        setFlag(newFlag);
        return this;
    }
    public Boolean default0UpdateFlag() {
        return getFlag();
    }


}
