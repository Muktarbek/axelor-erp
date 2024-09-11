package com.axelor.train.web;

import com.axelor.cargoInfo.db.CargoInfo;
import com.axelor.db.JPA;
import com.axelor.i18n.I18n;
import com.axelor.product.db.Products;
import com.axelor.rpc.Response;
import com.axelor.train.db.repo.ProductRepo;
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
    @Inject
    private ProductRepo productRepository;

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

        Trains train = request.getContext().asType(Trains.class);
        int heaviestWagon =  train.getWagons().stream()
                .map(Wagons::getWeight)
                .max(Comparator.naturalOrder())
                .orElse(0);

        response.setValue("$Weight", heaviestWagon);
    }
    public void reportMonthly(ActionRequest request, ActionResponse response) {
        System.out.println("Hello");
        // ...
        Map<String, Object> data = new HashMap<>();
        data.put("total", 565);
        data.put("percent", 787);
        data.put("up",15);
        data.put("tag", I18n.get("Monthly"));
        data.put("tagCss", "label-success");

        // This data will be put into dataset.
        // For report-box, we send a list with a single item accessible as `first`.
        response.setData(List.of(data));
    }

    @POST
    @Path("/processInputJson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processInputJson(JsonNode requestBody) {
        Response response = new Response();
        String inputJson = requestBody.toString();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            Trains train = objectMapper.readValue(inputJson, Trains.class);
            trainsService.save(train);
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
