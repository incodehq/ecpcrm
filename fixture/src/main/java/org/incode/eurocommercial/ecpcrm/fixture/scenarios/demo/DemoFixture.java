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

package org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo;

import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;

import org.incode.eurocommercial.ecpcrm.dom.quick.QuickObject;
import org.incode.eurocommercial.ecpcrm.fixture.dom.quick.QuickObjectsTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.quick.QuickObjectRowHandler;

import lombok.Getter;

public class DemoFixture extends FixtureScript {

    public DemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    /**
     * The quick objects created by this fixture (output).
     */
    @Getter
    private final List<QuickObject> quickObjects = Lists.newArrayList();


    @Override
    protected void execute(final ExecutionContext ec) {

        // zap everything
        ec.executeChild(this, new QuickObjectsTearDown());

        // load data from spreadsheet
        final URL spreadsheet = Resources.getResource(DemoFixture.class, "DemoFixture.xlsx");
        final ExcelFixture fs = new ExcelFixture(spreadsheet, getHandlers());
        ec.executeChild(this, fs);

        // make objects created by ExcelFixture available to our caller.
        final Map<Class, List<Object>> objectsByClass = fs.getObjectsByClass();

        getQuickObjects().addAll((List)objectsByClass.get(QuickObject.class));
        getQuickObjects().addAll((List)objectsByClass.get(QuickObjectRowHandler.class));
    }

    private Class[] getHandlers() {
        return new Class[]{
                QuickObject.class,
                QuickObjectRowHandler.class
        };
    }
}
