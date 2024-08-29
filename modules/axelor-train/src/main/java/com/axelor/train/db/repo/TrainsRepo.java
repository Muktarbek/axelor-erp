package com.axelor.train.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.meta.db.repo.MetaTranslationRepository;
import com.axelor.train.db.Trains;
import com.axelor.train.db.repo.TrainsRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;

public class TrainsRepo extends TrainsRepository {

    @Inject
    private EntityManager manager;

//    public void updateEmployeeCompany(Long newCompanyId, Long oldCompanyId) {
//        String jpql = "UPDATE Employee e SET e.train.id = :newCompanyId WHERE e.train.id = :oldCompanyId";
//        Query query = manager.createQuery(jpql);
//        query.setParameter("newCompanyId", newCompanyId);
//        query.setParameter("oldCompanyId", oldCompanyId);
//        int updatedCount = query.executeUpdate();
//        System.out.println("Количество обновленных записей: " + updatedCount);
//    }
}
