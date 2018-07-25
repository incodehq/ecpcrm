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
package org.incode.eurocommercial.ecpcrm.fixture.dom.request;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequest;

public class CardRequestTearDown extends FixtureScript {

    @Override
    protected void execute(FixtureScript.ExecutionContext executionContext) {
        isisJdoSupport.deleteAll(CardRequest.class);
    }

    @Inject private IsisJdoSupport isisJdoSupport;
}
