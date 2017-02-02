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
package org.incode.eurocommercial.ecpcrm.dom.game;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = CardGame.class
)
public class CardGameRepository {

    @Programmatic
    public List<CardGame> listAll() {
        return repositoryService.allInstances(CardGame.class);
    }

    @Programmatic
    public CardGame findByCardAndDate(
            final Card card,
            final LocalDate date
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        CardGame.class,
                        "findByCardAndDate",
                        "card", card,
                        "date", date));
    }

    @Programmatic
    public CardGame newCardGame(
            final Card card,
            final LocalDate date,
            final boolean outcome
    ) {
        final CardGame cardGame = repositoryService.instantiate(CardGame.class);

        cardGame.setCard(card);
        cardGame.setDate(date);
        cardGame.setOutcome(outcome);

        card.getCardGames().add(cardGame);
        repositoryService.persist(cardGame);
        return cardGame;
    }

    @Programmatic
    public CardGame findOrCreate(
            final Card card,
            final LocalDate date,
            final boolean outcome
    ) {
        CardGame cardGame = findByCardAndDate(card, date);
        cardGame = cardGame != null ? cardGame : newCardGame(card, date, outcome);
        return cardGame;
    }

    @Inject RepositoryService repositoryService;
}
