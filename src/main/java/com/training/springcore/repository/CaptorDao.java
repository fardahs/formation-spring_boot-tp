package com.training.springcore.repository;

import com.training.springcore.model.Captor;

import java.util.List;

public interface CaptorDao extends CrudDao<Captor, String> {
    List<Captor> findByCaptorId(String siteId);
}
