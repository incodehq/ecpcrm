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
package org.incode.eurocommercial.ecpcrm.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;

import org.apache.isis.viewer.wicket.viewer.IsisWicketApplication;
import org.apache.isis.viewer.wicket.viewer.integration.wicket.AuthenticatedWebSessionForIsis;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;

/**
 * As specified in <tt>web.xml</tt>.
 * 
 * <p>
 * See:
 * <pre>
 * &lt;filter>
 *   &lt;filter-name>wicket&lt;/filter-name>
 *    &lt;filter-class>org.apache.wicket.protocol.http.WicketFilter&lt;/filter-class>
 *    &lt;init-param>
 *      &lt;param-name>applicationClassName&lt;/param-name>
 *      &lt;param-value>EcpCrmApplication&lt;/param-value>
 *    &lt;/init-param>
 * &lt;/filter>
 * </pre>
 * 
 */
public class EcpCrmApplication extends IsisWicketApplication {

    private static final long serialVersionUID = 1L;

    /**
     * set to true for a (slightly hacky) way of allowing logins using query args, eg:
     * 
     * <tt>?user=sven&pass=pass</tt>
     * 
     * <p>
     * for demos only, obviously.
     */
    private final static boolean DEMO_MODE_USING_CREDENTIALS_AS_QUERYARGS = false;

    @Override
    protected void init() {
        super.init();

        final IBootstrapSettings settings = Bootstrap.getSettings();
        settings.setThemeProvider(new BootswatchThemeProvider(BootswatchTheme.Flatly));

        settings.setActiveThemeProvider(new EcpCrmUserSettingsThemeProvider(settings));
    }


    @Override
    public Session newSession(final Request request, final Response response) {
        if(!DEMO_MODE_USING_CREDENTIALS_AS_QUERYARGS) {
            return super.newSession(request, response);
        } 
        
        // else demo mode
        final AuthenticatedWebSessionForIsis s = (AuthenticatedWebSessionForIsis) super.newSession(request, response);
        final org.apache.wicket.util.string.StringValue user = request.getRequestParameters().getParameterValue("user");
        final org.apache.wicket.util.string.StringValue password = request.getRequestParameters().getParameterValue("pass");
        s.signIn(user.toString(), password.toString());
        return s;
    }

    @Override
    public WebRequest newWebRequest(final HttpServletRequest servletRequest, final String filterPath) {
        if(!DEMO_MODE_USING_CREDENTIALS_AS_QUERYARGS) {
            return super.newWebRequest(servletRequest, filterPath);
        } 

        // else demo mode
        try {
            final String uname = servletRequest.getParameter("user");
            if (uname != null) {
                servletRequest.getSession().invalidate();
            }
        } catch (Exception ignored) {
        }

        return super.newWebRequest(servletRequest, filterPath);
    }
    
    @Override
    protected Module newIsisWicketModule() {
        final Module isisDefaults = super.newIsisWicketModule();
        
        final Module overrides = new AbstractModule() {
            @Override
            protected void configure() {

                bind(String.class).annotatedWith(Names.named("applicationName")).toInstance("ECP CRM");
                bind(String.class).annotatedWith(Names.named("applicationCss")).toInstance("css/application.css");
                bind(String.class).annotatedWith(Names.named("applicationJs")).toInstance("scripts/application.js");
//                bind(String.class).annotatedWith(Names.named("brandLogoHeader")).toInstance("/images/domainapp-logo-header.png");
//                bind(String.class).annotatedWith(Names.named("brandLogoSignin")).toInstance("/images/domainapp-logo-signin.png");
                bind(String.class).annotatedWith(Names.named("welcomeMessage")).toInstance(readLines(getClass(), "welcome.html"));
                bind(String.class).annotatedWith(Names.named("aboutMessage")).toInstance("ECP CRM");
                bind(InputStream.class).annotatedWith(Names.named("metaInfManifest")).toProvider(Providers.of(getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF")));
            }
        };

        return Modules.override(isisDefaults).with(overrides);
    }

    private static String readLines(final Class<?> contextClass, final String resourceName) {
        try {
            final List<String> readLines = Resources.readLines(
                    Resources.getResource(contextClass, resourceName), Charset.defaultCharset());
            final String aboutText = Joiner.on("\n").join(readLines);
            return aboutText;
        } catch (final IOException e) {
            return "This is an Apache Isis Quickstart app";
        }
    }

}
