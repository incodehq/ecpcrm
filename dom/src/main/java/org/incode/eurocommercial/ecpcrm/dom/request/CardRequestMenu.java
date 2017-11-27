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
package org.incode.eurocommercial.ecpcrm.dom.request;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Card Requests",
        menuOrder = "13"
)

public class CardRequestMenu {
    @Action(semantics = SemanticsOf.SAFE)
    public List<CardRequest> listAll() {
        return cardRequestRepository.listAll()  ;
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<CardRequest> allOpenRequests() {
        return cardRequestRepository.listOpenRequests();
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<CardRequest> allRecentlyHandledRequests() {
        return cardRequestRepository.listRecentlyHandledRequests();
    }

    @Inject CardRequestRepository cardRequestRepository;
}
