package com.training.springcore.service;

import com.training.springcore.config.Monitored;
import com.training.springcore.model.Captor;
import com.training.springcore.model.PowerSource;
import com.training.springcore.service.measure.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CaptorServiceImpl implements CaptorService{

    private MeasureService fixed;
    private MeasureService real;
    private MeasureService simulated;

    public CaptorServiceImpl() {
    }

    @Autowired
    public CaptorServiceImpl(MeasureService fixed, MeasureService real, MeasureService simulated) {
        this.fixed = fixed;
        this.real = real;
        this.simulated = simulated;
    }

    @Override
    @Monitored
    public Set<Captor> findBySite(String siteId) {
        Set<Captor> captors = new HashSet<>();
        if (siteId == null) {
            return captors;
        }
        captors.add(new Captor("Capteur A", PowerSource.FIXED));
        return captors;
    }
}
