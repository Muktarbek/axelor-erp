package com.axelor.train.service;

import com.axelor.train.db.repo.TrainsRepo;
import com.axelor.train.db.Trains;
import com.google.inject.Inject;

import java.util.Objects;

public class TrainsServiceImpl implements TrainsService{
    @Inject
    private TrainsRepo trainsRepository;
    @Override
    public void updateTrain(Trains newTrainData) {
        Trains oldTrain = trainsRepository.find(newTrainData.getId());
        System.out.println(newTrainData);
        System.out.println(oldTrain);
        if (oldTrain == null) {
            throw new RuntimeException("Не найдено !!!");
        }
        if (hasChanges(oldTrain, newTrainData)) {
            Trains newCompany = createNewCompany(newTrainData);
            trainsRepository.save(newCompany);
            changeStatusOldCompany(oldTrain);
           // updateEmployeeCompany(newCompany.getId(), oldCompany.getId());
        } else {
            System.out.println("Нет изменений для сохранения");
            throw new RuntimeException("Нет изменений для сохранения");
        }
    }
    private boolean hasChanges(Trains oldCompany, Trains newCompanyData) {
        return !Objects.equals(oldCompany.getDepartureCountry(), newCompanyData.getDepartureCountry()) ||
                !Objects.equals(oldCompany.getDepartureStation(), newCompanyData.getDepartureStation()) ||
                !Objects.equals(oldCompany.getDestinationCustomsDepartment(), newCompanyData.getDestinationCustomsDepartment());
    }
    private Trains createNewCompany(Trains newCompanyData) {
        Trains newCompany = new Trains();
        newCompany.setDepartureCountry(newCompanyData.getDepartureCountry());
        newCompany.setDepartureStation(newCompanyData.getDepartureStation());
        newCompany.setDestinationCustomsDepartment(newCompanyData.getDestinationCustomsDepartment());
        newCompany.setStatus("ARRIVED");
     //   trainsRepository.save(newCompany);
        System.out.println("Создана новая компания: " + newCompany);
        return newCompany;
    }
    private void changeStatusOldCompany(Trains oldCompany) {
//      companiesRepository.updateCompanyStatus(oldCompany.getId(),Status.INACTIVE);
        oldCompany.setStatus("DEPARTED");
        trainsRepository.save(oldCompany);
    }
//    private void updateEmployeeCompany(Long newCompanyId, Long oldCompanyId) {
//        trainsRepository.updateEmployeeCompany(newCompanyId, oldCompanyId);
//        System.out.println("Обновлены компании сотрудников");
//    }
}
