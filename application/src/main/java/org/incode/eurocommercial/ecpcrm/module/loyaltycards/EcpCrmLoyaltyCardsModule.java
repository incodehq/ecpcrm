/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
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
package org.incode.eurocommercial.ecpcrm.module.loyaltycards;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.ecwid.maleorang.MailchimpObject;
import com.google.common.collect.Sets;

import org.apache.isis.applib.Module;
import org.apache.isis.applib.ModuleAbstract;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.teardown.TeardownFixtureAbstract2;

import org.isisaddons.module.security.SecurityModule;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.game.CardGame;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.numerator.Numerator;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

@XmlRootElement(name = "module")
public final class EcpCrmLoyaltyCardsModule extends ModuleAbstract {

    @Override
    public Set<Module> getDependencies() {
        return Sets.newHashSet(
                new SecurityModule()
        );
    }

    @Override
    public Set<Class<?>> getAdditionalModules() {
        return Sets.newHashSet(
                MailchimpObject.class
        );
    }

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureAbstract2() {
            @Override protected void execute(final ExecutionContext executionContext) {
                deleteFrom(CardRequest.class);
                deleteFrom(CardGame.class);
                deleteFrom(Card.class);
                deleteFrom(ChildCare.class);
                deleteFrom(Child.class);
                deleteFrom(User.class);
                deleteFrom(Center.class);
                deleteFrom(Numerator.class);
            }
        };
    }

}
