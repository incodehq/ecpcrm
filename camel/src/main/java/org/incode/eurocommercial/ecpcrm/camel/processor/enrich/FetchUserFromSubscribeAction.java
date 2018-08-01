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

import org.apache.camel.Message;

import org.apache.isis.schema.common.v1.OidDto;
import org.apache.isis.schema.ixn.v1.InteractionDto;

import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

public class FetchUserFromSubscribeAction extends EnrichViaRestfulObjectsAbstract {

    public FetchUserFromSubscribeAction() {
        super();
    }

    protected OidDto obtainDtoFrom(final Message inMessage) {
        final InteractionDto interactionDto = (InteractionDto) inMessage.getBody();
        return interactionDto.getExecution().getTarget();
    }

    @Override
    protected Class<?> dtoClass(final String objectType) {
        return UserDto.class;
    }
}
