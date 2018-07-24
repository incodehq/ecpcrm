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
package org.incode.eurocommercial.ecpcrm.dom.impersonation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.security.RoleMemento;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.user.UserService;

@DomainService(nature = NatureOfService.DOMAIN)
@DomainServiceLayout(menuBar = DomainServiceLayout.MenuBar.TERTIARY)
public class UserServiceWithImpersonation implements UserService {

    private UserMemento impersonatedUser;

    @Programmatic
    @Override
    public UserMemento getUser() {
        final Optional<UserMemento> impersonatedUserIfAny = getImpersonatedUserIfAny();
        return impersonatedUserIfAny.orElse(delegateUserService().getUser());
    }

    @Programmatic
    public void setUser(final String userName) {
        setImpersonatedUser(new UserMemento(userName));
    }

    @Programmatic
    public void setUser(final String userName, final String... roles) {
        setUser(userName, Arrays.asList(roles));
    }

    @Programmatic
    public void setUser(final String userName, final List<String> roles) {
        setImpersonatedUser(new UserMemento(userName, roles.stream().map(RoleMemento::new).collect(Collectors.toList())));
    }

    @Programmatic
    public void reset() {
        setImpersonatedUser(null);
    }

    @Programmatic
    public boolean isImpersonating() {
        return getImpersonatedUserIfAny().isPresent();
    }

    @Programmatic
    public boolean isAvailable() {
        return true;
    }

    private UserService delegateUserService;
    private UserService delegateUserService() {
        if (delegateUserService == null) {
            // there will always be at least one other user service, namely UserServiceDefault provided by the framework itself
            delegateUserService = userServiceList.stream().filter(x -> x != UserServiceWithImpersonation.this).findFirst().get();
        }
        return delegateUserService;
    }

    private Optional<UserMemento> getImpersonatedUserIfAny() {
        if(!isAvailable()) {
            return Optional.empty();
        }
        return impersonatedUser == null ? Optional.empty() : Optional.of(impersonatedUser);
    }

    private void setImpersonatedUser(UserMemento overrideUser) {
        if(!isAvailable()) {
            return;
        }
        impersonatedUser = overrideUser;
    }

    @Inject List<UserService> userServiceList;
}
