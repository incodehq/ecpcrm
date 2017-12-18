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
import java.util.Random;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = AuthenticationDevice.class
)
public class AuthenticationDeviceRepository {
    @Programmatic
    public List<AuthenticationDevice> listAll() {
        return repositoryService.allInstances(AuthenticationDevice.class);
    }

    @Programmatic
    public AuthenticationDevice findByNameAndSecret(
            final String name,
            final String secret
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        AuthenticationDevice.class,
                        "findByNameAndSecret",
                        "name", name,
                        "secret", secret
                ));
    }

    @Programmatic
    public List<AuthenticationDevice> findByCenter(
            final Center center
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        AuthenticationDevice.class,
                        "findByCenter",
                        "center", center
                ));
    }

    @Programmatic
    public AuthenticationDevice newAuthenticationDevice(
            Center center,
            AuthenticationDeviceType type,
            String name,
            String secret,
            boolean active
    ) {
        final AuthenticationDevice device = repositoryService.instantiate(AuthenticationDevice.class);

        device.setCenter(center);
        device.setType(type);
        device.setName(name);
        device.setSecret(secret);
        device.setActive(active);

        repositoryService.persist(device);

        return device;
    }

    @Programmatic
    public AuthenticationDevice findOrCreate(
            Center center,
            AuthenticationDeviceType type,
            String name,
            String secret,
            boolean active
    ) {
        AuthenticationDevice device = findByNameAndSecret(name, secret);
        if(device == null) {
            device = newAuthenticationDevice(center, type, name, secret, active);
        }

        return device;
    }

    @Programmatic
    public AuthenticationDevice findOrCreate(
            Center center,
            AuthenticationDeviceType type,
            String name,
            boolean active
    ) {
        String secret = generateSecret();
        return findOrCreate(center, type, name, secret, active);
    }

    @Programmatic
    private String generateSecret() {
        return DigestUtils.sha1Hex(new Random().nextInt() + "");
    }

    @Inject RepositoryService repositoryService;
}
