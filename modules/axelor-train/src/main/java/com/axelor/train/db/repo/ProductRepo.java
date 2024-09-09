package com.axelor.train.db.repo;

import com.axelor.product.db.repo.ProductsRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class ProductRepo extends ProductsRepository {
    @Inject
    private EntityManager manager;
}
