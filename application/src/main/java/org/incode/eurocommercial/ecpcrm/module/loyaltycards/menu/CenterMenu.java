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
package org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu;

import java.util.List;

import javax.inject.Inject;

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

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Centers",
        menuOrder = "10"
)
public class CenterMenu {
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Center> listAll() {
        return centerRepository.listAll();
    }

    public static class CreateDomainEvent extends IsisApplibModule.ActionDomainEvent<CenterMenu> {}

    @Action(domainEvent = CreateDomainEvent.class, semantics = SemanticsOf.IDEMPOTENT)
    @MemberOrder(sequence = "2")
    public Center newCenter(
            final String code,
            final String name,
            final String id,
            final @Parameter(optionality = Optionality.OPTIONAL) String mailchimpListId,
            final @Parameter(optionality = Optionality.OPTIONAL) String contactEmail
    ) {
        return centerRepository.findOrCreate(code, name, id, mailchimpListId, contactEmail);
    }

    @Inject private CenterRepository centerRepository;
}
