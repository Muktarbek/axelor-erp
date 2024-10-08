/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2024 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.axelor.apps.businessproject.service.projectgenerator.factory;

import com.axelor.apps.base.AxelorException;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.apps.base.db.repo.TraceBackRepository;
import com.axelor.apps.base.service.ProductCompanyService;
import com.axelor.apps.businessproject.exception.BusinessProjectExceptionMessage;
import com.axelor.apps.businessproject.service.ProductTaskTemplateService;
import com.axelor.apps.businessproject.service.ProjectBusinessService;
import com.axelor.apps.businessproject.service.ProjectTaskBusinessProjectService;
import com.axelor.apps.businessproject.service.projectgenerator.ProjectGeneratorFactory;
import com.axelor.apps.project.db.Project;
import com.axelor.apps.project.db.ProjectTask;
import com.axelor.apps.project.db.repo.ProjectRepository;
import com.axelor.apps.project.db.repo.ProjectTaskRepository;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.SaleOrderLine;
import com.axelor.apps.sale.db.repo.SaleOrderLineRepository;
import com.axelor.i18n.I18n;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.actions.ActionView.ActionViewBuilder;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class ProjectGeneratorFactoryTaskTemplate implements ProjectGeneratorFactory {

  private ProjectBusinessService projectBusinessService;
  private ProjectRepository projectRepository;
  private ProjectTaskBusinessProjectService projectTaskBusinessProjectService;
  private ProjectTaskRepository projectTaskRepo;
  private ProductTaskTemplateService productTaskTemplateService;
  private ProductCompanyService productCompanyService;

  @Inject
  public ProjectGeneratorFactoryTaskTemplate(
      ProjectBusinessService projectBusinessService,
      ProjectRepository projectRepository,
      ProjectTaskBusinessProjectService projectTaskBusinessProjectService,
      ProjectTaskRepository projectTaskRepo,
      ProductTaskTemplateService productTaskTemplateService,
      ProductCompanyService productCompanyService) {
    this.projectBusinessService = projectBusinessService;
    this.projectRepository = projectRepository;
    this.projectTaskBusinessProjectService = projectTaskBusinessProjectService;
    this.projectTaskRepo = projectTaskRepo;
    this.productTaskTemplateService = productTaskTemplateService;
    this.productCompanyService = productCompanyService;
  }

  @Override
  public Project create(SaleOrder saleOrder) {
    Project project = projectBusinessService.generateProject(saleOrder);
    project.setIsBusinessProject(true);
    return project;
  }

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public ActionViewBuilder fill(Project project, SaleOrder saleOrder, LocalDateTime startDate)
      throws AxelorException {
    List<ProjectTask> tasks = new ArrayList<>();
    ProjectTask root;

    root =
        projectTaskRepo
            .all()
            .filter(
                "self.project = ? AND self.assignedTo = ? AND self.name = ?",
                project,
                project.getAssignedTo(),
                saleOrder.getFullName())
            .fetchOne();

    projectRepository.save(project);

    for (SaleOrderLine orderLine : saleOrder.getSaleOrderLineList()) {
      Product product = orderLine.getProduct();
      if (product != null
          && !((ProductRepository.PROCUREMENT_METHOD_PRODUCE.equals(
                      (String)
                          productCompanyService.get(
                              product, "procurementMethodSelect", saleOrder.getCompany()))
                  || orderLine.getSaleSupplySelect() == SaleOrderLineRepository.SALE_SUPPLY_PRODUCE)
              && ProductRepository.PRODUCT_TYPE_SERVICE.equals(product.getProductTypeSelect()))) {
        continue;
      }
      boolean isTaskGenerated =
          projectTaskRepo
                  .all()
                  .filter("self.saleOrderLine = ? AND self.project = ?", orderLine, project)
                  .fetch()
                  .size()
              > 0;
      if (root == null) {
        root =
            projectTaskBusinessProjectService.create(
                saleOrder.getFullName(), project, project.getAssignedTo());
        root.setTaskDate(startDate.toLocalDate());
        tasks.add(projectTaskRepo.save(root));
      }
      if (product != null && !isTaskGenerated) {
        if (!CollectionUtils.isEmpty(product.getTaskTemplateSet())) {
          List<ProjectTask> convertedTasks =
              productTaskTemplateService.convert(
                  product.getTaskTemplateSet().stream()
                      .filter(template -> Objects.isNull(template.getParentTaskTemplate()))
                      .collect(Collectors.toList()),
                  project,
                  root,
                  startDate,
                  orderLine.getQty(),
                  orderLine);
          convertedTasks.stream().forEach(task -> task.setSaleOrderLine(orderLine));
          tasks.addAll(convertedTasks);
        } else {
          ProjectTask childTask =
              projectTaskBusinessProjectService.create(
                  orderLine.getFullName(), project, project.getAssignedTo());
          this.updateTask(root, childTask, orderLine);

          tasks.add(projectTaskRepo.save(childTask));
        }
      }
    }
    if (root == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_NO_VALUE,
          I18n.get(BusinessProjectExceptionMessage.SALE_ORDER_GENERATE_FILL_PROJECT_ERROR_2));
    }
    return ActionView.define(I18n.get("Tasks"))
        .model(ProjectTask.class.getName())
        .add("grid", "project-task-grid")
        .add("form", "project-task-form")
        .param("search-filters", "project-task-filters")
        .domain("self.parentTask = " + root.getId());
  }

  protected void updateTask(ProjectTask root, ProjectTask childTask, SaleOrderLine orderLine)
      throws AxelorException {
    childTask.setParentTask(root);
    childTask.setQuantity(orderLine.getQty());
    Product product = orderLine.getProduct();
    childTask.setProduct(product);
    childTask.setExTaxTotal(orderLine.getExTaxTotal());
    Company company =
        orderLine.getSaleOrder() != null ? orderLine.getSaleOrder().getCompany() : null;
    childTask.setUnitPrice(
        product != null
            ? (BigDecimal) productCompanyService.get(product, "salePrice", company)
            : null);
    childTask.setUnit(
        product != null ? (Unit) productCompanyService.get(product, "unit", company) : null);
    childTask.setSaleOrderLine(orderLine);
    if (orderLine.getSaleOrder().getToInvoiceViaTask()) {
      childTask.setToInvoice(true);
      childTask.setInvoicingType(ProjectTaskRepository.INVOICING_TYPE_PACKAGE);
    }
  }
}
