package com.training.springcore.service;

import com.training.springcore.model.Captor;
import com.training.springcore.model.PowerSource;
import com.training.springcore.model.Site;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SiteServiceImplTest.SiteServiceTestConfiguration.class})
public class SiteServiceImplTest {
    @Configuration
    @ComponentScan("com.training.springcore.service")
    static class SiteServiceTestConfiguration{ }

    @Autowired
    private SiteService siteService1;

    @Rule
    public OutputCapture output = new OutputCapture();

    @Test
    public void readFileFromUrl() throws IOException {
        siteService1.readFile("url:https://dev-mind.fr/lorem.txt");
        assertThat(output.toString()).contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit");

    }
    @Test
    public void readFileFromClasspath(){
       siteService1.readFile("classpath:lorem.txt");
        assertThat(output.toString()).contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
    }
    @Test
    public void readFileFromFileSystem(){
        siteService1.readFile("file:///c:/lorem.txt");
        assertThat(output.toString()).contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit");

    }


    @Mock
    private CaptorService captorService;

    @InjectMocks
    private SiteServiceImpl siteService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByIdShouldReturnNullWhenIdIsNull(){
        // Initialisation
        String siteId = null;

        // Appel du SUT
        Site site = siteService.findById(siteId);

        // Vérification
        Assertions.assertThat(site).isNull();
    }

    @Test
    public void findById(){
        // Initialisation
        String siteId = "siteId";
        Set<Captor> expectedCpators = Collections.singleton(new Captor("Capteur A", PowerSource.FIXED));
        Mockito.when(captorService.findBySite(siteId)).thenReturn(expectedCpators);

        // Appel du SUT
        Site site = siteService.findById(siteId);

        // Vérification
        Assertions.assertThat(site.getId()).isEqualTo(siteId);
        Assertions.assertThat(site.getName()).isEqualTo("Florange");
        Assertions.assertThat(site.getCaptors()).isEqualTo(expectedCpators);
    }

}