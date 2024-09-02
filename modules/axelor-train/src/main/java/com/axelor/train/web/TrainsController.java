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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.oracle.truffle.regex.tregex.util.json.JsonObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

@Path("/trains")
public class  TrainsController {

    @Inject
    private TrainsServiceImpl trainsService;
    @Inject
    private TrainsRepo trainsRepository;

    @CallMethod
//    public void updateTrain(ActionRequest request, ActionResponse response) {
//        if (request.getContext().get("id") == null) {
//            JPA.edit(Trains.class, request.getContext());
//        } else {
//            Trains companies = request.getContext().asType(Trains.class);
//            System.out.println(companies);
//            companies.setId((Long) request.getContext().get("id"));
//
//            try {
//                trainsService.updateTrain(companies);
//                response.setInfo("Данные успешно обновлены");
//            } catch (RuntimeException e) {
////                    response.setError(e.getMessage());
//                System.out.println(e.getMessage());
//            }
//        }
//    }

    public void findMaxWeight(ActionRequest request, ActionResponse response) {
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

        response.setValue("$Weight", heaviestTrain);
    }

    @POST
    @Path("/processInputJson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processInputJson(JsonNode requestBody) {

      String inputJson = requestBody.toString();
    //   System.out.println(inputJson);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            Trains trains = objectMapper.readValue(inputJson, Trains.class);

            // Теперь вы можете использовать объект train
            System.out.println(trains.getTrainNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Логика обработки JSON
      //  response.setInfo("JSON успешно обработан!");
        System.out.println("Hello");
        return new Response();
    }
}
