package com.training.springcore.service;

import com.training.springcore.config.Monitored;
import com.training.springcore.model.Captor;
import com.training.springcore.model.PowerSource;
import com.training.springcore.repository.CaptorDao;
import com.training.springcore.service.measure.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CaptorServiceImpl implements CaptorService{

    private MeasureService fixed;
    private MeasureService real;
    private MeasureService simulated;
    private CaptorDao captorDao;

    public CaptorServiceImpl() {
    }

    @Autowired
    public CaptorServiceImpl(MeasureService fixed, MeasureService real, MeasureService simulated, CaptorDao captorDao) {
        this.fixed = fixed;
        this.real = real;
        this.simulated = simulated;
        this.captorDao = captorDao;
    }

    @Override
    @Monitored
    public Set<Captor> findBySite(String siteId) {

        return captorDao.findBySiteId(siteId).stream().collect(Collectors.toSet());
    }
}
