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
package org.incode.eurocommercial.ecpcrm.webapp.card_request_detail;

import org.junit.Ignore;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.webapp.ecp_crm_test.EcpCrmTest;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

@Ignore
public class CardRequestDetailTest extends EcpCrmTest {

    private String endpoint = "card-request-detail";

    private String sendRequest(String id) throws Exception {
        String request = "{\"id\": \"" + id + "\"}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        String id = "";
        assertThatJson(sendRequest(id))
            .node("status").isEqualTo(302);
    }

    @Test
    public void when_request_id_is_invalid_we_expect_311_error() throws Exception {
        String id = "-1";
        assertThatJson(sendRequest(id))
            .node("status").isEqualTo(311);
    }

    @Test
    public void when_status_is_not_new_we_expect_311_error() throws Exception {
        String id = "4";
        assertThatJson(sendRequest(id))
            .node("status").isEqualTo(311);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_request_center_id_is_not_current_user_center_id_we_expect_311_error() throws Exception {
        String id = "";
        assertThatJson(sendRequest(id))
            .node("status").isEqualTo(311);
    }

    @Test
    public void when_user_is_invalid_we_expect_304_error() throws Exception {
        String id = "778";
        assertThatJson(sendRequest(id))
            .node("status").isEqualTo(304);
    }

    @Test
    public void when_request_is_valid_and_user_is_valid_we_expect_happy_response() throws Exception {
        String id = "3181";
        assertThatJson(sendRequest(id))
            .node("status").isEqualTo(200);
    }

}