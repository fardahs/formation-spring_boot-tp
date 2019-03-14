package com.training.springcore.repository;

import com.training.springcore.model.Captor;
import com.training.springcore.model.PowerSource;
import com.training.springcore.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

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

    @Before
    public void init() {
        site = new Site("name");
        site.setId("site1");
    }

    @Test
    public void findById() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Eolienne");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Captor captor = captorDao.findById("unknown");
        Assertions.assertThat(captor).isNull();
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
        Captor captor = new Captor("New captor", site );
        captor.setPowerSource(PowerSource.SIMULATED);

        captorDao.persist(captor);

        Assertions.assertThat(captorDao.findAll())
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Eolienne", "Laminoire à chaud", "New captor");
    }

    @Test
    public void update() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Eolienne");
        captor.setName("Captor updated");
        captorDao.persist(captor);
        captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Captor updated");
    }

    @Test
    public void deleteById() {
        Captor newcaptor = new Captor("New captor", site);
        captorDao.persist(newcaptor);
        Assertions.assertThat(captorDao.findById(newcaptor.getId())).isNotNull();
        captorDao.delete(captorDao.findById(newcaptor.getId()));
        Assertions.assertThat(captorDao.findById(newcaptor.getId())).isNull();

    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {

        Captor captor = captorDao.findById("c1");
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.delete(captor);
                    entityManager.flush();
                })
                .isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

}
