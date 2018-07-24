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
package org.incode.eurocommercial.ecpcrm.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import org.apache.isis.applib.AppManifest;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.metamodel.paraname8.NamedFacetOnParameterParaname8Factory;
import org.isisaddons.module.security.facets.TenantedAuthorizationFacetFactory;

import org.incode.eurocommercial.ecpcrm.dom.EcpCrmDomainModule;
import org.incode.eurocommercial.ecpcrm.fixture.EcpCrmFixtureModule;

public class EcpCrmAppManifest implements AppManifest {

    @Override
    public List<Class<?>> getModules() {
        return Arrays.asList(

                EcpCrmDomainModule.class     // entities and domain services
                ,EcpCrmFixtureModule.class   // fixture scripts and FixtureScriptsSpecificationProvider
                ,EcpCrmAppModule.class     // EcpCrmRolesAndPermissionsSeedService (requires security module)

                ,org.isisaddons.module.excel.ExcelModule.class // to run fixtures
                ,org.isisaddons.module.settings.SettingsModule.class // used by DomainAppUserSettingsThemeProvider

//                ,org.isisaddons.module.audit.AuditModule.class
//                ,org.isisaddons.module.command.CommandModule.class
//                ,org.isisaddons.module.devutils.DevUtilsModule.class
//                ,org.isisaddons.module.docx.DocxModule.class
//                ,org.isisaddons.module.fakedata.FakeDataModule.class
//                ,org.isisaddons.module.publishing.PublishingModule.class
                ,org.isisaddons.module.security.SecurityModule.class
//                ,org.isisaddons.module.sessionlogger.SessionLoggerModule.class
//                ,org.incode.module.note.dom.NoteModule.class
//                ,org.incode.module.commchannel.dom.CommChannelModule.class
                );
    }

    @Override
    public List<Class<?>> getAdditionalServices() {
        return Arrays.asList(
                org.isisaddons.module.security.dom.password.PasswordEncryptionServiceUsingJBcrypt.class
        );
    }



    /**
     * Use shiro for authentication.
     *
     * <p>
     *     NB: this is ignored for integration tests, which always use "bypass".
     * </p>
     */
    @Override
    public String getAuthenticationMechanism() {
        return "shiro";
    }

    /**
     * Use shiro for authorization.
     *
     * <p>
     *     NB: this is ignored for integration tests, which always use "bypass".
     * </p>
     */
    @Override
    public String getAuthorizationMechanism() {
        return "shiro";
    }

    /**
     * No fixtures.
     */
    @Override
    public List<Class<? extends FixtureScript>> getFixtures() {
        return Collections.emptyList();
    }

    /**
     * configure metamodel facets (paraname8 and tenanted authorization)
     */
    @Override
    public final Map<String, String> getConfigurationProperties() {
        Map<String,String> props = Maps.newHashMap();

        props.put(
                "isis.reflector.facets.include",
                Joiner.on(',').join(
                        NamedFacetOnParameterParaname8Factory.class.getName()
                        , TenantedAuthorizationFacetFactory.class.getName()
                ));

        appendConfigurationProperties(props);
        return props.isEmpty()? null: props;

    }

    /**
     * Optional hook for subclasses
     */
    protected void appendConfigurationProperties(final Map<String, String> props) {
    }

}
