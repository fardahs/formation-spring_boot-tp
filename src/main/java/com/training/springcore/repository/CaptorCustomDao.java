package com.training.springcore.repository;

import com.training.springcore.model.Captor;

import java.util.List;

public interface CaptorCustomDao {
    List<Captor> findBySiteId(String siteId);
}
