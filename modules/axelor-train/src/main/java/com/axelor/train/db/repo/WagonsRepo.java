package com.axelor.train.db.repo;

import com.axelor.wagon.db.repo.WagonsRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class WagonsRepo extends WagonsRepository {

    @Inject
    private EntityManager manager;
}
