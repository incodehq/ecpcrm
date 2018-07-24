/**
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.incode.eurocommercial.ecpcrm.app.services.homepage;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.services.i18n.TranslatableString;

import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

@ViewModel
public class HomePageViewModel {

    public String title() {
        return "Customers";
    }


    @Collection(editing = Editing.DISABLED)
    @CollectionLayout(paged=200)
    @org.apache.isis.applib.annotation.HomePage
    public List<User> getCustomers() {
        return userRepository.listAll();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Delete")
    @MemberOrder(name = "customers", sequence = "2")
    public HomePageViewModel deleteCustomer(
            final User user,
            final boolean delete) {
        if (delete) {
            userRepository.delete(user);
        }
        return this;
    }

    public TranslatableString validateDeleteCustomer(final User user, final boolean delete) {
        return delete ? null : TranslatableString.tr("You have to agree");
    }

    public List<User> choices0DeleteCustomer() {
        return userRepository.listAll();
    }
    public User default0DeleteCustomer() {
        final List<User> choices = choices0DeleteCustomer();
        return choices.isEmpty() ? null: choices.get(0);
    }

    public TranslatableString disableDeleteCustomer() {
        return choices0DeleteCustomer().isEmpty() ? TranslatableString.tr("No customers"): null;
    }

    @javax.inject.Inject
    UserRepository userRepository;


}
