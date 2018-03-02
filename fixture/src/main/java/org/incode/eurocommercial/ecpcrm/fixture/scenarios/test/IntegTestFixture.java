/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with DemoFixture.this work for additional information
 *  regarding copyright ownership.  The ASF licenses DemoFixture.this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use DemoFixture.this file except in compliance
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

package org.incode.eurocommercial.ecpcrm.fixture.scenarios.test;

import org.incode.eurocommercial.ecpcrm.fixture.scenarios.EcpCrmFixture;

public class IntegTestFixture extends EcpCrmFixture {
    public IntegTestFixture() {
        super();
        NUM_CENTERS = 2;
        CARDS_PER_CENTER = 5;
        USERS_WITH_CARDS_PER_CENTER = 3;
        USERS_WITHOUT_CARDS_PER_CENTER = 3;
        CARD_REQUESTS_PER_CENTER = 2;
        CHILDREN_PER_USER = 1;
        CLOSED_CHILDCARES_PER_CHILD = 2;
        OPEN_CHILDCARES_PER_CENTER = 2;
    }
}
