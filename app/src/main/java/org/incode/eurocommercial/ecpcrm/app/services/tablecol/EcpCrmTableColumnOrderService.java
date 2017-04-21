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
package org.incode.eurocommercial.ecpcrm.app.services.tablecol;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.tablecol.TableColumnOrderService;

import org.isisaddons.module.security.app.user.MeService;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import org.incode.eurocommercial.ecpcrm.dom.seed.roles.HostessRoleAndPermissions;

@DomainService(
        nature = NatureOfService.DOMAIN,
        menuOrder = "100"
)
public class EcpCrmTableColumnOrderService implements TableColumnOrderService {

    @Override public List<String> orderParented(
            Object parent,
            String collectionId,
            Class<?> collectionType,
            List<String> propertyIds) {
        if(isHostess(meService.me())) {
            propertyIds.remove("center");
        }

        return propertyIds;
    }

    @Override public List<String> orderStandalone(
            Class<?> collectionType, final List<String> propertyIds) {
        if(isHostess(meService.me())) {
            propertyIds.remove("center");
        }

        return propertyIds;
    }

    private boolean isHostess(ApplicationUser user) {
        System.out.println(user.getName());
        for(ApplicationRole role : user.getRoles()) {
            System.out.println(role.getName());
            if(role.getName().equals(HostessRoleAndPermissions.ROLE_NAME)) {
                return true;
            }
        }

        return false;
    }

    @Inject MeService meService;
}