package com.axelor.train.web;

import com.axelor.db.JPA;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.train.db.repo.WagonsRepo;
import com.axelor.train.service.WagonsServiceImpl;
import com.axelor.wagon.db.Wagons;
import com.google.inject.servlet.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class WagonController {

    @Inject
    private
    WagonsRepo wagonsRepository;

    @Inject
    private WagonsServiceImpl wagonsService;

//    @CallMethod
//    public void updateWagon(ActionRequest request, ActionResponse response) {
//        if (request.getContext().get("id") == null) {
//            JPA.edit(Wagons.class, request.getContext());
//        } else {
//            Wagons companies = request.getContext().asType(Wagons.class);
//            System.out.println(companies);
//            companies.setId((Long) request.getContext().get("id"));
//
//            try {

//                wagonsService.updateWagon(companies);
//                response.setInfo("Данные успешно обновлены");
//            } catch (RuntimeException e) {
////                    response.setError(e.getMessage());
//                System.out.println(e.getMessage());
//            }
//        }
//    }
}
