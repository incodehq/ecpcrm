/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.incode.eurocommercial.ecpcrm.camel.webapp;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerUsingSpringOpts extends PropertyPlaceholderConfigurer {

    public static final String OPT_ENV = "SPRING_OPTS";
    public static final String SEPARATOR_ENV = "SPRING_OPTS_SEPARATOR";
    public static final String SEPARATOR_DEFAULT = "||";

    private Map<String,String> propertyValueByPlaceHolder;


    private boolean testMode;

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(final boolean testMode) {
        this.testMode = testMode;
    }


    @Override
    protected String resolvePlaceholder(
            String placeholder, Properties props, int systemPropertiesMode) {
        final String value = resolve(placeholder);
        if(value != null) {
            return value;
        }
        return super.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }

    private String resolve(final String placeholder) {
        if (propertyValueByPlaceHolder == null) {
            this.propertyValueByPlaceHolder = parseEnvironment();
        }
        return propertyValueByPlaceHolder.get(placeholder);
    }

    private Map<String, String> parseEnvironment() {
        Map<String, String> properties = Maps.newHashMap();
        final String separator = determineSeparator();
        final String env = System.getenv(OPT_ENV);
        for (Map.Entry<String, String> entry : fromEnv(env, separator).entrySet()) {
            final String envVarName = entry.getKey();
            final String envVarValue = entry.getValue();
            properties.put(envVarName, envVarValue);
        }
        return properties;
    }

    private static String determineSeparator() {
        final String separator = System.getenv(SEPARATOR_ENV);
        if (separator != null) {
            return separator;
        }
        return SEPARATOR_DEFAULT;
    }

    private static Map<String, String> fromEnv(final String env, final String separator) {
        final LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
        if (env != null) {
            final List<String> keyAndValues = Splitter.on(separator).splitToList(env);
            for (String keyAndValue : keyAndValues) {
                final List<String> parts = Splitter.on("=").splitToList(keyAndValue);
                if (parts.size() == 2) {
                    map.put(parts.get(0), parts.get(1));
                }
            }
        }
        return map;
    }

}
