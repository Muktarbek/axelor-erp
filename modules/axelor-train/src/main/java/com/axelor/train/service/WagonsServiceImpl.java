package com.axelor.train.service;

import com.axelor.train.db.repo.WagonsRepo;
import com.axelor.wagon.db.Wagons;

import javax.inject.Inject;

public class WagonsServiceImpl implements WagonsService {

    @Inject
    private WagonsRepo wagonsRepository;

//    @Override
//    public void updateWagon(Wagons wagons) {
//        wagonsRepository.save(wagons);
//    }
}
