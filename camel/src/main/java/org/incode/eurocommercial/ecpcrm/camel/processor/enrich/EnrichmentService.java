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
package org.incode.eurocommercial.ecpcrm.camel.processor.enrich;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.camel.Message;

import org.apache.isis.schema.common.v1.OidDto;

import org.isisaddons.module.publishmq.dom.statusclient.StatusMessage;
import org.isisaddons.module.publishmq.dom.statusclient.StatusMessageClient;

import org.incode.eurocommercial.ecpcrm.camel.processor.util.MessageUtil;

import lombok.Setter;

public class EnrichmentService {

    @Setter
    private StatusMessageClient statusMessageClient;

    private UriBuilder uriBuilder;

    @Setter
    private String base;

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    private JaxRsClient jaxRsClient;

    public void init() {
        jaxRsClient = new JaxRsClient.Default();
    }

    public void invokeAction(String service, String action, String queryString) throws Exception {
        uriBuilder = UriBuilder
                .fromUri(base + "services/{service}/actions/{action}/invoke/")
                .replaceQuery(queryString);
        final URI uri = uriBuilder.build(service, action);

        JaxRsResponse jaxRsResponse = jaxRsClient.get(uri, username, password);

        final int status = jaxRsResponse.getStatus();
        if(status != 200) {
            final String responseEntity = jaxRsResponse.readEntity(String.class);

            statusMessageClient.log(
                    StatusMessage.builder("", 0,"Failed to invoke " + service + "#" + action + "/?" + queryString)
                            .withUri(uri)
                            .withStatus(-1) // TODO: perhaps something a bit more nuanced here?
                            .withDetail(responseEntity)
            );

            throw new Exception(responseEntity);
        }
    }

    public <T> T retrieveDto(Message message, OidDto oidDto, Class<T> dtoClass) throws Exception {
        final String transactionId = MessageUtil.transactionIdFrom(message);
        final String objectType = oidDto.getType() != null? oidDto.getType() : oidDto.getObjectType();
        final String objectIdentifier = oidDto.getId() != null ? oidDto.getId(): oidDto.getObjectIdentifier();

        uriBuilder = UriBuilder.fromUri(base + "objects/{objectType}/{objectInstance}");
        final URI uri = uriBuilder.build(objectType, objectIdentifier);

        JaxRsResponse jaxRsResponse = jaxRsClient.invoke(uri, dtoClass, username, password);

        final int status = jaxRsResponse.getStatus();
        if(status != 200) {
            final String responseEntity = jaxRsResponse.readEntity(String.class);

            statusMessageClient.log(
                    StatusMessage.builder(transactionId, 0,"Failed to retrieve " + dtoClass.getName())
                            .withUri(uri)
                            .withOid(objectType, objectIdentifier)
                            .withStatus(-1) // TODO: perhaps something a bit more nuanced here?
                            .withDetail(responseEntity)
            );

            throw new Exception(responseEntity);
        }

        return jaxRsResponse.readEntity(dtoClass);
    }

    public <T> void enrichMessageWithDto(Message message, OidDto oidDto, Class<T> dtoClass) throws Exception {
        enrichMessageWithDto(message, oidDto, dtoClass, "default");
    }

    public <T> void enrichMessageWithDto(Message message, OidDto oidDto, Class<T> dtoClass, String role) throws Exception {
        T dto = retrieveDto(message, oidDto, dtoClass);
        MessageUtil.setHeader(message, dto, role);
    }

    public <T> void enrichMessageWithLocalDto(Message message, T dto) {
        enrichMessageWithLocalDto(message, dto, "default");
    }

    public <T> void enrichMessageWithLocalDto(Message message, T dto, String role) {
        MessageUtil.setHeader(message, dto, role);
    }
}
