package com.training.springcore.repository;

import com.training.springcore.model.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({"com.training.springcore.repository",
        "com.training.springcore.utils"})
public class CaptorDaoImplTest {

    @Autowired
    private CaptorDao captorDao;
    private Site site;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MeasureDao measureDao;
    @Autowired
    private SiteDao siteDao;

    @Before
    public void init() {
        site = new Site("name");
        site.setId("site1");
    }

    @Test
    public void findById() {
        Optional<Captor> captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Eolienne");

    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {

        Optional<Captor> captor = captorDao.findById("unknown");
        Assertions.assertThat(captor).isEmpty();
    }

    @Test
    public void findAll() {
        List<Captor> captors = captorDao.findAll();
        Assertions.assertThat(captors)
                .hasSize(2)
                .extracting("id", "name")
                .contains(Tuple.tuple("c1", "Eolienne"))
                .contains(Tuple.tuple("c2", "Laminoire à chaud"));
    }

    @Test
    public void create() {
        Assertions.assertThat(captorDao.findAll()).hasSize(2);
        Captor captor = new FixedCaptor("New captor", site);
        captor.setPowerSource(PowerSource.SIMULATED);

        captorDao.save(captor);

        Assertions.assertThat(captorDao.findAll())
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Eolienne", "Laminoire à chaud", "New captor");
    }

    @Test
    public void update() {
        Optional<Captor> captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Eolienne");
        captor.ifPresent(c -> {
            c.setName("Captor updated");
            captorDao.save(c);

        });

        captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Captor updated");

    }

    @Test
    public void deleteById() {
        Captor newcaptor = new FixedCaptor("New captor", site);
        captorDao.save(newcaptor);
        Assertions.assertThat(captorDao.findById(newcaptor.getId())).isNotEmpty();
        captorDao.delete(newcaptor);
        Assertions.assertThat(captorDao.findById(newcaptor.getId())).isEmpty();

    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {

        Captor captor = captorDao.getOne("c1");
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.delete(captor);
                    entityManager.flush();
                })
                .isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void findByExample() {
        Captor captor = new FixedCaptor("Eolienne", site);
        captor.setId("c1");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.ignoreCase().contains())
                .withIgnorePaths("id")
                .withIgnoreNullValues();

        List<Captor> captors = captorDao.findAll(Example.of(captor, matcher));
        Assertions.assertThat(captors)
                .hasSize(1)
                .extracting("id", "name")
                .containsExactly(Tuple.tuple("c1", "Eolienne"));
    }

    @Test
    public void preventConcurrentWrite() {
        Captor captor = captorDao.getOne("c1");
// A la base le numéro de version est à sa valeur initiale
        Assertions.assertThat(captor.getVersion()).isEqualTo(0);
// On detache cet objet du contexte de persistence
        entityManager.detach(captor);
        captor.setName("Captor updated");
// On force la mise à jour en base (via le flush) et on vérifie que l'objet retourné
// et attaché à la session a été mis à jour
        Captor attachedCaptor = captorDao.save(captor);
        entityManager.flush();
        Assertions.assertThat(attachedCaptor.getName()).isEqualTo("Captor updated");
        Assertions.assertThat(attachedCaptor.getVersion()).isEqualTo(1);
// Si maintenant je réessaie d'enregistrer captor, comme le numéro de version est
// à 0 je dois avoir une exception
        Assertions.assertThatThrownBy(() -> captorDao.save(captor))
                .isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    public void createSimulatedCaptorShouldThrowExceptionWhenMinMaxAreInvalid() {
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.save(new SimulatedCaptor("Mon site", site, 10, 5));
                    entityManager.flush();
                })
                .isExactlyInstanceOf(javax.validation.ConstraintViolationException.class)
                .hasMessageContaining("minPowerInWatt should be less than maxPowerInWatt");
    }

    @Test
    public void deleteBySiteId() {
        Assertions.assertThat(captorDao.findBySiteId("site1")).hasSize(2);
        measureDao.deleteAll();
        captorDao.deleteBySiteId("site1");
        siteDao.delete(site);
        Assertions.assertThat(captorDao.findBySiteId("site1")).isEmpty();
    }


}
