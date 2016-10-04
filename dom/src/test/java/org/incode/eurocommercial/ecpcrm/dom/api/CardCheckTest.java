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

import org.junit.Ignore;
import org.junit.Test;

public class CardCheckTest extends EcpCrmTest {
    @Test
    @Ignore
    /* When the device type is not app and the card number contains 3922 */
    public void when_card_does_not_exist_and_is_outdated_we_expect_319_error() throws Exception {
    }

    @Test
    @Ignore
    /* When the card number does not match the required pattern */
    public void when_card_does_not_exist_and_card_number_is_invalid_we_expect_312_error() throws Exception {
    }

    @Test
    @Ignore
    /* When the card status is "tochange" */
    public void when_card_exists_but_is_outdated_we_expect_319_error() throws Exception {
    }

    @Test
    @Ignore
    /* When the card status is not "enabled" */
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_does_not_exist_and_a_new_user_cant_be_created_for_it_we_expect_313_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_does_not_exist_and_a_new_user_is_created_for_it_but_they_cant_be_linked_we_expect_314_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_card_user_is_invalid_we_expect_304_error() throws Exception {
    }

    /* These two give regular responses */

    @Test
    @Ignore
    public void when_card_exists_but_cant_play_game_we_expect_sad_response() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
    }


}