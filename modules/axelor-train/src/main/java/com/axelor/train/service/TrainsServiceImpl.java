package com.axelor.train.service;

import com.axelor.train.db.repo.TrainsRepo;
import com.axelor.train.db.Trains;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.util.Objects;

public class TrainsServiceImpl implements TrainsService{
    @Inject
    private TrainsRepo trainsRepository;

    @Override
    @Transactional
    public void save(Trains trains) {
        trainsRepository.save(trains);
    }
}
