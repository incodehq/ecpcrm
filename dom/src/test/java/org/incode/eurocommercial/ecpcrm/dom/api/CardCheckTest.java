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
package org.incode.eurocommercial.ecpcrm.dom.api;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CardCheckTest {
    @Before
    public void setUp() throws Exception {
    }


    @Test
    @Ignore
    public void when() throws Exception {
    }

    @Test
    @Ignore
    /* When the device type is not app and the card number contains 3922 */
    public void whenCardDoesNotExistAndIsOutdatedWeExpect319Error() throws Exception {
    }

    @Test
    @Ignore
    /* When the card number does not match the required pattern */
    public void whenCardDoesNotExistAndCardNumberIsInvalidWeExpect312Error() throws Exception {
    }

    @Test
    @Ignore
    /* When the card status is "tochange" */
    public void whenCardExistsButIsOutdatedWeExpect319Error() throws Exception {
    }

    @Test
    @Ignore
    /* When the card status is not "enabled" */
    public void whenCardExistsButIsNotEnabledWeExpect303Error() throws Exception {
    }

    @Test
    @Ignore
    public void whenCardExistsButIsNotTheSameCenterAsDeviceWeExpect317Error() throws Exception {
    }

    @Test
    @Ignore
    public void whenCardDoesNotExistAndANewUserCantBeCreatedForItWeExpect313Error() throws Exception {
    }

    @Test
    @Ignore
    public void whenCardDoesNotExistAndANewUserIsCreatedForItButTheyCantBeLinkedWeExpect314Error() throws Exception {
    }

    @Test
    @Ignore
    public void whenCardExistsButCardUserIsInvalidWeExpect304Error() throws Exception {
    }

    /* These two give regular responses */

    @Test
    @Ignore
    public void whenCardExistsButCantPlayGameWeExpectSadResponse() throws Exception {
    }

    @Test
    @Ignore
    public void whenCardExistsAndCanPlayGameWeExpectHappyResponse() throws Exception {
    }


}