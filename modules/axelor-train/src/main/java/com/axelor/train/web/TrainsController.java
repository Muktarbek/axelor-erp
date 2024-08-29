package com.axelor.train.web;

import com.axelor.db.JPA;
import com.axelor.train.db.repo.TrainsRepo;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.train.service.TrainsServiceImpl;
import com.axelor.train.db.Trains;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class TrainsController {

    @Inject
    private TrainsServiceImpl trainsService;
    @Inject
    private TrainsRepo trainsRepository;

    @CallMethod
    public void updateTrain(ActionRequest request, ActionResponse response) {
        if (request.getContext().get("id") == null) {
            JPA.edit(Trains.class, request.getContext());
        } else {
            Trains companies = request.getContext().asType(Trains.class);
            System.out.println(companies);
            companies.setId((Long) request.getContext().get("id"));

            try {
                trainsService.updateTrain(companies);
                response.setInfo("Данные успешно обновлены");
            } catch (RuntimeException e) {
//                    response.setError(e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }
}
