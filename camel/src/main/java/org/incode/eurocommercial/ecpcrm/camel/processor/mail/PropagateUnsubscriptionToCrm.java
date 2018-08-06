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
package org.incode.eurocommercial.ecpcrm.camel.processor.mail;

import com.google.common.base.Strings;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;

import org.incode.eurocommercial.ecpcrm.camel.processor.ProcessorAbstract;
import org.incode.eurocommercial.ecpcrm.camel.processor.enrich.EnrichmentService;

public class PropagateUnsubscriptionToCrm extends ProcessorAbstract {
    @Override
    public void process(final Exchange exchange) throws Exception {
        String listId = exchange.getIn().getHeader("data[list_id]", String.class);
        String email = exchange.getIn().getHeader("data[email]", String.class);
        String type = exchange.getIn().getHeader("type", String.class);

        if (Strings.isNullOrEmpty(listId) || Strings.isNullOrEmpty(email)) {
            return;
        }

        if (!Strings.isNullOrEmpty(type) && !(type.equals("unsubscribed") || type.equals("cleaned"))) {
            return;
        }

        String service = "org.incode.eurocommercial.ecpcrm.module.loyaltycards.services.MailService";
        String action = "mailchimpWebhookCallback";
        String queryString = String.format("listId=%s&email=%s", listId, email);

        enrichmentService.invokeAction(service, action, queryString);
    }

    @BeanInject EnrichmentService enrichmentService;
}

