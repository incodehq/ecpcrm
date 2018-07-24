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
package org.incode.eurocommercial.ecpcrm.dom.authentication;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
        @Query(
                name = "findByNameAndSecret",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDevice "
                        + "WHERE name == :name "
                        + "&& secret == :secret"
        ),
        @Query(
                name = "findByCenter",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDevice "
                        + "WHERE center == :center"
        )
})
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
public class AuthenticationDevice implements HasAtPath {

    public String title() {
        return getName() + " - " + getCenter().getName();
    }

    @Getter @Setter
    @Column(allowsNull = "false")
    private Center center;

    @Getter @Setter
    @Column(allowsNull = "false")
    private AuthenticationDeviceType type;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String name;

    @Getter @Setter
    @Column(allowsNull = "false")
    private String secret;

    @Getter @Setter
    @Column(allowsNull = "false")
    private boolean active;

    @Override public String getAtPath() {
        return getCenter().getAtPath();
    }
}
