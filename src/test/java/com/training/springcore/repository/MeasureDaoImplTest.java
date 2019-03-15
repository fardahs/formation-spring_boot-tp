package com.training.springcore.repository;

import com.training.springcore.model.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
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

    @Autowired
    private EntityManager entityManager;

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
        Captor captor = new RealCaptor("Eolienne", new Site("site"));
        captor.setId("c1");
        captor.setPowerSource(PowerSource.REAL);
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

    @Test
    public void preventConcurrentWrite() {
        Measure measure = measureDao.getOne(-1L);
// A la base le numéro de version est à sa valeur initiale
        Assertions.assertThat(measure.getVersion()).isEqualTo(0);
// On detache cet objet du contexte de persistence
        entityManager.detach(measure);
        measure.setValueInWatt(100001);

// On force la mise à jour en base (via le flush) et on vérifie que l'objet retourné
// et attaché à la session a été mis à jour
        Measure attachedMeasure = measureDao.save(measure);
        entityManager.flush();
        Assertions.assertThat(attachedMeasure.getValueInWatt()).isEqualTo(100001);
        Assertions.assertThat(attachedMeasure.getVersion()).isEqualTo(1);
// Si maintenant je réessaie d'enregistrer captor, comme le numéro de version est
// à 0 je dois avoir une exception
        Assertions.assertThatThrownBy(() -> measureDao.save(measure))
                .isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    public void deleteByCaptorId() {
        Assertions.assertThat(measureDao.findAll().stream().filter(m ->
                m.getCaptor().getId().equals("c1"))).hasSize(5);
        measureDao.deleteByCaptorId("c1");
        Assertions.assertThat(measureDao.findAll().stream().filter(m ->
                m.getCaptor().getId().equals("c1"))).isEmpty();
    }

}
