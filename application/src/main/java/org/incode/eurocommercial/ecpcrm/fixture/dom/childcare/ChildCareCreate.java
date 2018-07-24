package org.incode.eurocommercial.ecpcrm.fixture.dom.childcare;

import java.util.Date;
import java.util.Random;

import javax.inject.Inject;

import com.github.javafaker.Faker;

import org.joda.time.LocalDateTime;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCareRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ChildCareCreate extends FixtureScript {

    public static final int MAX_DURATION = 80;
    public static final int OPEN_HOUR = 8;
    public static final int CLOSE_HOUR = 18;
    public static final int MONTH_RANGE = 3;

    @Getter @Setter
    private Child child;

    @Getter @Setter
    private LocalDateTime checkIn;

    @Getter @Setter
    private LocalDateTime checkOut;

    @Getter
    private ChildCare childCare;

    @Override
    protected void execute(final ExecutionContext ec) {
        Faker faker = new Faker();

        if (child == null)
            child = childRepository.listAll().get(new Random().nextInt(childRepository.listAll().size()));

        if (checkIn == null) {
            Date date = faker.date().between(
                    clockService.now().minusMonths(MONTH_RANGE).toDate(),
                    clockService.now().minusDays(1).toDate());
            checkIn = LocalDateTime.fromDateFields(date)
                    .plusHours(OPEN_HOUR)
                    .plusHours(new Random().nextInt(CLOSE_HOUR - OPEN_HOUR))
                    .plusMinutes(new Random().nextInt(60));
        }

        if(checkIn.plusMinutes(MAX_DURATION).isAfter(clockService.nowAsLocalDateTime()))
            checkOut = null;
        else
            checkOut = checkIn.plusMinutes(new Random().nextInt(MAX_DURATION));

        child.checkIn();
        childCare = childCareRepository.findActiveChildCareByChild(child);
        childCare.setCheckIn(checkIn);
        childCare.setCheckOut(checkOut);

        ec.addResult(this, childCare());
    }

    @Inject private ChildRepository childRepository;
    @Inject private ChildCareRepository childCareRepository;
    @Inject private ClockService clockService;
}
