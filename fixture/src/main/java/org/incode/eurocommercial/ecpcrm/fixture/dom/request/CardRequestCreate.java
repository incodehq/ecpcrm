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

import java.util.Random;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestType;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class CardRequestCreate extends FixtureScript {

    @Getter @Setter
    private User user;

    @Getter @Setter
    private CardRequestType type;

    @Getter
    private CardRequest cardRequest;

    @Override
    protected void execute(final ExecutionContext ec) {
        if(user == null)
            user = userRepository.listAll().get(new Random().nextInt(userRepository.listAll().size()));
        if(type == null)
            type = CardRequestType.values()[new Random().nextInt(CardRequestType.values().length)];

        cardRequest = cardRequestRepository.findOrCreate(user, type);

        ec.addResult(this, cardRequest);
    }

    @Inject private CardRequestRepository cardRequestRepository;
    @Inject private UserRepository userRepository;
}
