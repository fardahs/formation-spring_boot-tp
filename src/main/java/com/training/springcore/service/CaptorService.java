package com.training.springcore.service;

import com.training.springcore.model.Captor;

import java.util.Set;

public interface CaptorService {
    Set<Captor> findBySite(String siteId);
}
