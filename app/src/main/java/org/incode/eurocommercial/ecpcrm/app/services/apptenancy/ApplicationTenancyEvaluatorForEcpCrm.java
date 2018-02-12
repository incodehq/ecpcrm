/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.incode.eurocommercial.ecpcrm.app.services.apptenancy;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.i18n.TranslatableString;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyEvaluator;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.isisaddons.module.security.dom.user.ApplicationUser;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class ApplicationTenancyEvaluatorForEcpCrm implements ApplicationTenancyEvaluator {

    @Override public boolean handles(final Class<?> domainClass) {
        return HasAtPath.class.isAssignableFrom(domainClass);
    }

    @Override public String hides(final Object domainObject, final ApplicationUser applicationUser) {
        return doHides((HasAtPath) domainObject, applicationUser);
    }

    private String doHides(final HasAtPath domainObject, final ApplicationUser applicationUser) {
        HasAtPath hasAtPath = domainObject;
        final String userAtPath = applicationUser.getAtPath();

        final String objAtPath = hasAtPath.getAtPath();
        if(objAtPath == null) {
            return null;
        }
        if(userAtPath == null) {
            return TranslatableString.tr("hidden").toString();
        }

        return !(userAtPath.startsWith(objAtPath) || objAtPath.startsWith(userAtPath)) ? TranslatableString.tr("hidden").toString() : null;
    }

    @Override public String disables(final Object domainObject, final ApplicationUser applicationUser) {
        return doDisables((HasAtPath) domainObject, applicationUser);
    }

    private String doDisables(final HasAtPath domainObject, final ApplicationUser applicationUser) {
        HasAtPath hasAtPath = domainObject;
        final String userAtPath = applicationUser.getAtPath();

        final String objAtPath = hasAtPath.getAtPath();
        if(objAtPath == null) {
            return null;
        }
        if(userAtPath == null) {
            return TranslatableString.tr("Cannot edit the object with atPath of '{objAtPath}'", "objAtPath", objAtPath).toString();
        }

        return !objAtPath.startsWith(userAtPath) ? TranslatableString.tr(
                "User with atPath of '{userAtPath}' cannot edit the object with atPath of '{objAtPath}'",
                "userAtPath", userAtPath, "objAtPath", objAtPath).toString() : null;
    }
}

