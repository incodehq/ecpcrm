package org.incode.eurocommercial.ecpcrm.fixture.dom.child;

import java.util.Random;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.joda.time.LocalDate;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
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
    private Gender gender;

    @Getter @Setter
    private LocalDate birthdate;

    @Getter @Setter
    private User parent;

    @Getter
    private Child child;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        if (name == null)
            name = faker.name().firstName();
        if (gender == null)
            gender = Gender.values()[new Random().nextInt(Gender.values().length)];
        if (birthdate == null)
            birthdate = LocalDate.fromDateFields(faker.date().between(
                    clockService.now().minusYears(30).toDate(),
                    clockService.now().toDate()));
        if (parent == null)
            parent = userRepository.listAll().get(new Random().nextInt(userRepository.listAll().size()));

        child = childRepository.findOrCreate(name(), gender(), birthdate(), parent());

        ec.addResult(this, child());
    }

    @Inject private ChildRepository childRepository;
    @Inject private UserRepository userRepository;
    @Inject private ClockService clockService;
}
