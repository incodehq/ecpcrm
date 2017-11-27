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

import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.isis.applib.IsisApplibModule;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Authentication Devices",
        menuOrder = "15"
)
public class AuthenticationDeviceMenu {
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<AuthenticationDevice> listAll() {
        return authenticationDeviceRepository.listAll();
    }

    public static class CreateDomainEvent extends IsisApplibModule.ActionDomainEvent<AuthenticationDeviceMenu> {}

    @Action(domainEvent = AuthenticationDeviceMenu.CreateDomainEvent.class, semantics = SemanticsOf.NON_IDEMPOTENT)
    @MemberOrder(sequence = "2")
    public AuthenticationDevice newAuthenticationDevice(
            final Center center,
            final AuthenticationDeviceType type,
            final String name,
            final @Parameter(optionality = Optionality.OPTIONAL) String secret,
            final boolean active
    ) {
        if(Strings.isNullOrEmpty(secret)) {
            return authenticationDeviceRepository.findOrCreate(center, type, name, active);
        } else {
            return authenticationDeviceRepository.findOrCreate(center, type, name, secret, active);
        }
    }

    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;
}
