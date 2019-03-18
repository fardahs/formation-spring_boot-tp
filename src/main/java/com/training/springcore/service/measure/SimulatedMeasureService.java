package com.training.springcore.service.measure;

import com.training.springcore.config.properties.BigCorpApplicationProperties;
import com.training.springcore.model.Captor;
import com.training.springcore.model.Measure;
import com.training.springcore.model.MeasureStep;
import com.training.springcore.model.SimulatedCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("simulated")
public class SimulatedMeasureService implements MeasureService<SimulatedCaptor> {

    private BigCorpApplicationProperties properties;

    private RestTemplate restTemplate;

    public SimulatedMeasureService(BigCorpApplicationProperties properties, RestTemplateBuilder builder) {
        this.properties = properties;
        this.restTemplate = builder.setConnectTimeout(1000).build();

    }

    @Override
    public List<Measure> readMeasures(SimulatedCaptor captor, Instant start, Instant end, MeasureStep step) {

        checkReadMeasuresAgrs(captor,start,end, step);

        Instant current = start;


        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8090/measures")
                .path("")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("min", captor.getMinPowerInWatt())
                .queryParam("max", captor.getMaxPowerInWatt())
                .queryParam("step", step.getDurationInSecondes());

        Measure[] measures = this.restTemplate.getForObject(builder.toUriString(), Measure[].class);

        return Arrays.asList(measures);
    }


}
