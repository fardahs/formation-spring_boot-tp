package com.training.springcore.repository;

import com.training.springcore.model.Captor;
import com.training.springcore.model.Measure;
import com.training.springcore.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({"com.training.springcore.repository",
        "com.training.springcore.utils"})
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    @Test
    public void findById() {
        Optional<Measure> measure = measureDao.findById(-1L);

        Assertions.assertThat(measure)
                .get()
                .extracting(Measure::getId, Measure::getInstant, Measure::getValueInWatt
                , m -> m.getCaptor().getName(), m -> m.getCaptor().getSite().getName()
        )
                .containsOnly(-1L, Instant.parse("2018-08-09T11:00:00.000Z"), 1_000_000, "Eolienne", "Bigcorp Lyon");


    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Measure> measure = measureDao.findById(20L);
        Assertions.assertThat(measure).isEmpty();
    }

    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
        Assertions.assertThat(measures).hasSize(10);
    }

    @Test
    public void create() {
        Captor captor = new Captor("Eolienne", new Site("site"));
        captor.setId("c1");
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.save(new Measure(Instant.now(), 2_333_666, captor));
        Assertions.assertThat(measureDao.findAll()).hasSize(11);
    }

    @Test
    public void update() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(1_000_000);
        measure.ifPresent(m -> {
            m.setValueInWatt(2_333_666);
            measureDao.save(m);
        });


        measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(2_333_666);

    }

    @Test
    public void deleteById() {
        Measure measure = measureDao.getOne(-1L);
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.delete(measure);
        Assertions.assertThat(measureDao.findAll()).hasSize(9);
    }

}
