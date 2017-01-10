package org.incode.eurocommercial.ecpcrm.fixture.dom.child;

import java.util.Random;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ChildCreate extends FixtureScript {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private User parent;

    @Getter
    private Child child;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        name = defaultParam("name", ec, faker.name().firstName());
        parent = defaultParam("parent", ec, userRepository.listAll().get(new Random().nextInt(userRepository.listAll().size())));
        this.child = childRepository.findOrCreate(name(), parent());

        ec.addResult(this, child());
    }

    @Inject ChildRepository childRepository;
    @Inject UserRepository userRepository;
}
