package com.axelor.train.web;

import com.axelor.db.JPA;
import com.axelor.rpc.Response;
import com.axelor.train.db.repo.TrainsRepo;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.train.db.repo.TrainsRepository;
import com.axelor.train.service.TrainsServiceImpl;
import com.axelor.train.db.Trains;
import com.axelor.wagon.db.Wagons;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import java.util.*;
import java.util.stream.Collectors;

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

    public void findMaxWeight(ActionRequest request,ActionResponse response){
      List<Trains> trains = trainsRepository.all().fetch();

        Map<Trains, Double> totalWeightsByTrain = trains.stream()
                .collect(Collectors.toMap(
                        train -> train,
                        train -> train.getWagons().stream()
                                .mapToDouble(wagon -> wagon.getWeight() != null ? wagon.getWeight() : 0.0)
                                .sum()
                ));
   //     Нахождение поезда с максимальным весом
         Double heaviestTrain = totalWeightsByTrain.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null).getValue();

        response.setValue("$Weight",heaviestTrain);
    }
}
