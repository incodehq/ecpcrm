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
package org.incode.eurocommercial.ecpcrm.integtests.tests.request;

import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRequestRepositoryIntegTest extends EcpCrmIntegTest{
    @Inject private FixtureScripts fixtureScripts;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject ClockService clockService;

    IntegTestFixture fs;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);
    }

    public static class ListAll extends CardRequestRepositoryIntegTest {
        @Test
        public void all_card_requests_should_be_listed() {
            // when
            List<CardRequest> cardRequestList = cardRequestRepository.listAll();

            // then
            assertThat(new TreeSet<>(cardRequestList)).isEqualTo(new TreeSet<>(fs.getCardRequests()));
        }
    }

    public static class ListOpenRequests extends CardRequestRepositoryIntegTest {
        @Test
        public void only_open_requests_should_be_returned() {
            // when
            List<CardRequest> openRequests = cardRequestRepository.listOpenRequests();

            // then
            for (CardRequest r : openRequests) {
                assertThat(r.getApproved()).isNull();
            }
        }

        @Test
        public void all_open_requests_should_be_returned() {
            // given
            List<CardRequest> cardRequestList = fs.getCardRequests();

            // when
            List<CardRequest> openRequests = cardRequestRepository.listOpenRequests();

            // then
            cardRequestList.removeAll(openRequests);

            for (CardRequest r : cardRequestList) {
                assertThat(r.getApproved()).isNotNull();
            }
        }
    }

    public static class ListRecentlyIssuedRequests extends CardRequestRepositoryIntegTest {
        @Test
        public void only_recent_requests_should_be_returned() {
            // given
            CardRequest oldRequest = cardRequestRepository.listAll().get(0);
            oldRequest.setIssueDate(clockService.nowAsLocalDateTime().minusDays(8));

            // when
            List<CardRequest> recentRequests = cardRequestRepository.listRecentlyIssuedRequests();

            // then
            for (CardRequest r : recentRequests) {
                assertThat(r.getIssueDate()).isGreaterThanOrEqualTo(clockService.nowAsLocalDateTime().minusDays(7));
            }
            assertThat(recentRequests).doesNotContain(oldRequest);
        }

        @Test
        public void all_recent_requests_should_be_returned() {
            // given
            List<CardRequest> allRequests = cardRequestRepository.listAll();
            CardRequest oldRequest = allRequests.get(0);
            oldRequest.setIssueDate(clockService.nowAsLocalDateTime().minusDays(8));

            // when
            List<CardRequest> recentRequests = cardRequestRepository.listRecentlyIssuedRequests();

            // then
            allRequests.removeAll(recentRequests);
            for (CardRequest r : allRequests) {
                assertThat(r.getIssueDate()).isLessThan(clockService.nowAsLocalDateTime().minusDays(7));
            }
            assertThat(allRequests).contains(oldRequest);
        }
    }

    public static class ListRecentlyHandledRequests extends CardRequestRepositoryIntegTest {
        @Test
        public void only_recent_requests_should_be_returned() {
            // given
            CardRequest oldRequest = cardRequestRepository.listAll().get(0);
            oldRequest.setHandleDate(clockService.nowAsLocalDateTime().minusDays(8));

            // when
            List<CardRequest> recentRequests = cardRequestRepository.listRecentlyHandledRequests();

            // then
            for (CardRequest r : recentRequests) {
                assertThat(r.getHandleDate()).isGreaterThanOrEqualTo(clockService.nowAsLocalDateTime().minusDays(7));
            }
            assertThat(recentRequests).doesNotContain(oldRequest);
        }

        @Test
        public void all_recent_requests_should_be_returned() {
            // given
            List<CardRequest> allRequests = cardRequestRepository.listAll();
            CardRequest oldRequest = allRequests.get(0);
            oldRequest.setHandleDate(clockService.nowAsLocalDateTime().minusDays(8));

            // when
            List<CardRequest> recentRequests = cardRequestRepository.listRecentlyHandledRequests();

            // then
            allRequests.removeAll(recentRequests);
            for (CardRequest request : allRequests) {
                if (request.getHandleDate() != null) {
                    assertThat(request.getHandleDate())
                            .isLessThan(clockService.nowAsLocalDateTime().minusDays(7));
                }
            }
            assertThat(allRequests).contains(oldRequest);
        }
    }
}
