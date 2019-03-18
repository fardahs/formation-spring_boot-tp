package com.training.springcore.service.measure;

import com.training.springcore.config.properties.BigCorpApplicationProperties;
import com.training.springcore.model.Captor;
import com.training.springcore.model.FixedCaptor;
import com.training.springcore.model.Measure;
import com.training.springcore.model.MeasureStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service("fixed")
public class FixedMeasureService implements MeasureService<FixedCaptor> {

    @Autowired
    private BigCorpApplicationProperties properties;

    public List<Measure> readMeasures(FixedCaptor captor, Instant start, Instant end, MeasureStep step) {

        checkReadMeasuresAgrs(captor, start, end, step);
        List<Measure> measures = new ArrayList<>();
        Instant current = start;

        while(current.isBefore(end)){
            measures.add(new Measure(current,
                    properties.getMeasure().getDefaultFixed(), captor));
            current = current.plusSeconds(step.getDurationInSecondes());
        }
        return measures;
    }


}
