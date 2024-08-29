package com.axelor.train;

import com.axelor.app.AxelorModule;
import com.axelor.train.service.TrainsService;
import com.axelor.train.service.TrainsServiceImpl;
import com.axelor.train.service.WagonsService;
import com.axelor.train.service.WagonsServiceImpl;

public class TrainsModule extends AxelorModule {
    @Override
    protected void configure() {
        bind(TrainsService.class).to(TrainsServiceImpl.class);
        bind(WagonsService.class).to(WagonsServiceImpl.class);
//        bind(CompaniesRepository.class).to(ComRepository.class);
    }
}
