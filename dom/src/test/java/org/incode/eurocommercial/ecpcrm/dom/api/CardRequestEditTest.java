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

public class CardRequestEditTest extends EcpCrmTest {
    /* These are from CardRequestDetail */
    @Test
    @Ignore
    public void when_request_id_is_invalid_we_expect_311_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_status_is_not_new_we_expect_311_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_request_type_is_not_card_request_we_expect_311_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_request_center_id_is_not_current_user_center_id_we_expect_311_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_user_is_invalid_we_expect_304_error() throws Exception {
    }
    /* //////////////////////////////// */

    /* These are from CardCheckUnbound */
    @Test
    @Ignore
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_is_already_bound_to_user_we_expect_308_error() throws Exception {
    }
    /* //////////////////////////////// */

    @Test
    @Ignore
    public void when_request_cant_be_handled_we_expect_311_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_request_is_valid_and_user_is_valid_and_card_can_be_handled_we_expect_happy_response() throws Exception {
    }

}