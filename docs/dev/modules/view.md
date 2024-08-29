# Представления объектов

В этой главе мы увидим представления объектов. Представления объектов представляют собой XML-определения
пользовательского интерфейса для представления объектных моделей конечным пользователям. Открытая платформа Goverp
поддерживает несколько видов представления объекта. Это включает в себя:

* Сетка - показывает список данных в виде столбцов
* Форма — показывает одну запись в макете формы.
* Дерево — показывает данные в иерархическом порядке
* Диаграмма — отображает данные в виде 2D-графиков.
* Календарь — показывает данные в виде графика
* Канбан — показывает данные в виде гибкой канбан-доски.
* Карты - показывает список данных в виде карт
* Пользовательский — показывает данные с использованием пользовательских шаблонов.
* Ганта — показывает данные с помощью диаграммы Ганта.

Представления сетки и формы являются основными представлениями . Если эти представления не предусмотрены для модели,
набор представлений по умолчанию генерируется автоматически. Однако они могут не создавать красивого интерфейса, всегда
рекомендуется предоставлять представления для моделей предметной области.

Как и объектные модели, представления также определяются с использованием формата xml.

Каждый файл должен иметь правильное объявление:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<object-views xmlns="http://axelor.com/xml/ns/object-views"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://axelor.com/xml/ns/object-views
  https://axelor.com/xml/ns/object-views/object-views_6.0.xsd">

    <!-- views definitions here -->

</object-views>
```

## Вид сетки

Представление сетки показывает данные в виде списка с несколькими столбцами.

Поскольку представления сетки извлекают множество записей для отображения в виде списка, важно отображать только важную
информацию.

Представление сетки можно определить следующим образом:

```xml

<grid name="contact-grid" title="Contacts"
      model="com.axelor.contact.db.Contact">
    <field name="fullName"/>
    <field name="email"/>
    <field name="phone"/>
    <field name="dateOfBirth"/>
</grid>
```

Теги `<field>` можно использовать для определения столбцов, которые связываются с полями модели.

| Атрибут      | Описание                                                                                                     |
|--------------|--------------------------------------------------------------------------------------------------------------|
| name         | имя представления, повторяющееся имя считается переопределяющим                                              |
| model        | полное имя модели предметной области, к которой принадлежит это представление                                |
| title        | заголовок сетки                                                                                              |
| id           | если вы переопределяете какой-то существующий, укажите уникальный идентификатор, чтобы идентифицировать этот |
| editable     | является ли сетка встроенной редактируемой                                                                   |
| orderBy      | разделенный запятыми список имен полей для сортировки записей                                                |
| groupBy      | разделенный запятыми список имен полей для группировки записей                                               |
| edit-icon    | показывать ли значок редактирования в первом столбце (по умолчанию true)                                     |
| customSearch | включен ли расширенный пользовательский поиск                                                                |
| freeSearch   | укажите режим свободного поиска: «все» (по умолчанию), «нет» или список имен полей, разделенных запятыми.    |
| canNew       | разрешить ли создание новых записей                                                                          |
| canEdit      | разрешить ли редактирование записей                                                                          |
| canDelete    | разрешить ли удаление записей                                                                                |
| canSave      | разрешить ли сохранение записей                                                                              |
| canMove      | разрешить ли переупорядочивание строк с помощью перетаскивания                                               |
| canArchive   | разрешить ли архивирование/разархивирование записей                                                          |
| x-no-fetch   | следует ли извлекать начальные записи                                                                        |
| x-selector   | указать элемент управления выбором строки ( checkboxчтобы показать выбор флажка)                             |

## Вид Формы

Представление формы показывает одну запись в макете формы. Это основной вид для просмотра записи с подробностями.

Представление формы имеет два режима:

* `readonly`- режим показывает значения полей в виде html текста
* `editable`- режим показывает редакторы полей со значениями

Представление формы определяется следующим образом:

```xml

<form name="contact-form"
      title="Contact"
      model="com.axelor.contact.db.Contact">
    <panel name="overviewPanel" title="Overview">
        <field name="fullName" readonly="false">
            <editor>
                <field name="title" colSpan="3"/>
                <field name="firstName" colSpan="4"/>
                <field name="lastName" colSpan="5"/>
            </editor>
        </field>
        <field name="dateOfBirth"/>
        <field name="email">
            <viewer><![CDATA[ 
      <a href="mailto:{{record.email}}">{{record.email}}</a>
      ]]></viewer>
        </field>
        <field name="phone">
            <viewer><![CDATA[
      <a href="tel:{{record.phone}}">{{record.phone}}</a>
      ]]></viewer>
        </field>
    </panel>
    <panel name="aboutMePanel" title="About me">
        <field name="notes" showTitle="false" colSpan="12"/>
    </panel>
    <panel-related name="addressesPanel" field="addresses">
        <field name="street"/>
        <field name="area"/>
        <field name="city"/>
        <field name="state"/>
        <field name="zip"/>
        <field name="country"/>
    </panel-related>
    <panel name="sidebarPanel" sidebar="true">
        <field name="createdOn"/>
        <field name="createdBy"/>
        <field name="updatedOn"/>
        <field name="updatedBy"/>
    </panel-side>
</form>
```

Представление формы может иметь следующие атрибуты:

| Атрибут    | Описание                                                                                                 |
|------------|----------------------------------------------------------------------------------------------------------|
| name       | имя представления, дубликаты считаются переопределяющими                                                 |
| model      | полное имя модели предметной области                                                                     |
| title      | заголовок вида формы                                                                                     |
| id         | Если вы переопределяете какой-то существующий, укажите уникальный идентификатор для идентификации этого. |
| editable   | доступна ли форма для редактирования                                                                     |
| readonlyIf | логическое выражение angular.js для создания формы только для чтения                                     |
| onNew      | действие, вызываемое при создании новой записи                                                           |
| onLoad     | действие, которое будет вызываться при загрузке записи                                                   |
| onSave     | действие, которое будет вызвано при сохранении этой формы                                                |
| canNew     | логическое выражение angular.js для кнопки «Создать»                                                     |
| canEdit    | логическое выражение angular.js для кнопки «Изменить»                                                    |
| canDelete  | логическое выражение angular.js для кнопки «Удалить»                                                     |
| canCopy    | логическое выражение angular.js для кнопки «Копировать»                                                  |
| canSave    | логическое выражение angular.js для кнопки «Сохранить»                                                   |
| canAttach  | логическое выражение angular.js для кнопки «Прикрепить»                                                  |
| width      | Предпочтительный стиль ширины представления: mini, midили large.                                         |
| x-no-fetch | следует ли извлекать начальные записи                                                                    |
| x-selector | указать элемент управления выбором строки ( checkboxчтобы показать выбор флажка)                         |

#### Панель

Рассмотрим каждый тип панелей.

- `panel`- панель с 12 столбцами, обычно используемая для размещения простых полей
- `panel-tabs`- содержит другие панели, которые отображаются как вкладки блокнота
- `panel-stack`- содержит другие панели, которые прикреплены
- `panel-related`- панель для размещения полей o2m/m2m
- `panel-include`- включить другую форму панели
- `panel-dashlet`- панель дашлет можно использовать для встраивания других представлений

A panel может иметь следующие атрибуты:

| Атрибут     | Описание                                                                                                  |
|-------------|-----------------------------------------------------------------------------------------------------------|
| title       | название панели                                                                                           |
| name        | название панели                                                                                           |
| colSpan     | количество столбцов, занимаемых виджетом                                                                  |
| itemSpan    | диапазон по умолчанию для дочерних элементов                                                              |
| hidden      | скрыть ли виджет                                                                                          |
| hideIf      | логическое выражение angular.js , чтобы скрыть панель                                                     |
| readonly    | следует ли считать виджет доступным только для чтения                                                     |
| readonlyIf  | логическое выражение angular.js , чтобы пометить панель только для чтения                                 |
| showIf      | логическое выражение angular.js для отображения панели                                                    |
| onTabSelect | действие, выполняемое при выборе вкладки панели (если она находится на верхнем уровне на вкладках панели) |
| showFrame   | показывать ли рамку вокруг панели                                                                         |
| showTitle   | показывать ли заголовок панели                                                                            |
| sidebar     | показывать ли эту панель на боковой панели                                                                |
| attached    | присоединять ли панель к предыдущей                                                                       |
| stacked     | являются ли элементы панели стека                                                                         |
| if-module   | использовать виджет, если данный модуль установлен                                                        |
| canCollapse | указать, является ли панель складной                                                                      |
| collapseIf  | укажите логическое выражение, чтобы свернуть/раскрыть эту панель                                          |
| help        | текст справки, отображаемый при наведении курсора мыши — новое в версии 5.4                               |

## В виде дерева

Представление в виде дерева показывает данные в виде иерархического древовидного списка с несколькими столбцами.

Древовидное представление можно определить следующим образом:

```xml

<tree name="project-task-tree" title="Project Tasks">

    <!-- define tree columns -->
    <column name="title" type="string"/>
    <column name="progress" type="integer"/>

    <!-- define 1st level node -->
    <node model="com.axelor.project.db.Project">
        <field name="name" as="title"/>
    </node>

    <!-- define 2nd level node -->
    <node model="com.axelor.project.db.ProjectTask" parent="project"
          draggable="true" onClick="project.task.add">
        <field name="name" as="title"/>
        <field name="progress" as="progress"/>
    </node>

</tree>
```

#### Столбец

Элементы `<column>` используются для определения столбцов отображения для древовидного представления.

| Атрибут     | Описание                                                                                                  |
|-------------|-----------------------------------------------------------------------------------------------------------|
| name        | имя узла                                                                                                  |
| type        | тип данных столбца                                                                                        |
| title       | заголовок столбца                                                                                         |

Столбцы привязаны к полям узла, описанным ниже.

#### Узел

Представление в виде дерева требует, <node>чтобы элементы определяли источники данных для узлов дерева.

Элемент `<node>` может иметь следующие атрибуты:

| Атрибут   | Описание                                                            |
|-----------|---------------------------------------------------------------------|
| model     | имя модели источника данных                                         |
| parent    | имя родительского поля                                              |
| domain    | фильтр домена для ограничения данных узла                           |
| orderBy   | разделенный запятыми список имен полей для упорядочения данных узла |
| draggable | можно ли перетаскивать узел                                         |
| onClick   | действие, выполняемое при двойном щелчке по узлу                    |

Первый элемент узла должен определять источник данных того типа, на который ссылается `parent` атрибут следующего
элемента узла. Последующие узлы должны следовать тому же правилу.

Если узел есть, `draggable` его можно перетащить на любой родительский узел, чтобы изменить родителя перетаскиваемого
узла.

## Просмотр диаграммы

Представление диаграммы показывает данные в виде 2D-графиков и работает на
базе [NVD3](https://nvd3-community.github.io/nvd3/).

```xml

<chart name="chart.sales.per.month" title="Sales per month">
    <search-fields>
        <field type="datetime" name="fromDateTime" title="From Date"/>
        <field type="datetime" name="toDateTime" title="To Date"/>
    </search-fields>
    <dataset type="jpql">
        <![CDATA[
  SELECT
      SUM(self.totalAmount) AS amount,
      MONTH(self.orderDate) AS month,
      _customer.fullName AS customer
  FROM
      Order self
  LEFT JOIN
      self.customer AS _customer
  WHERE
      YEAR(self.orderDate) = YEAR(current_date)
      AND self.orderDate > :fromDateTime
      AND self.orderDate < :toDateTime
  GROUP BY
      _customer,
      MONTH(self.orderDate)
  ORDER BY
      month
  ]]>
    </dataset>
    <category key="month" type="month"/>
    <series key="amount" groupBy="customer" type="bar"/>
</chart>
```

Представление диаграммы не привязано к какому-либо объекту, но зависит от набора данных, полученного с помощью запросов
JPQL/SQL, или заданного rpc (вызова метода).

Необязательный параметр `<search-fields>` можно использовать для определения полей ввода для предоставления значений
параметров запроса или контекста для вызовов rpc.

## Просмотр календаря

Представление календаря показывает данные с интерфейсом, похожим на расписание / повестку дня, с поддержкой
перетаскивания.

```xml

<calendar name="project-task-calendar-my"
          title="My Tasks"
          model="com.axelor.project.db.ProjectTask"
          colorBy="project"
          eventStart="startDate"
          eventStop="endDate"
          eventLength="1">
    <field name="name"/>
</calendar>
```

Атрибуты представления календаря:

| Атрибут     | Описание                                                                                                                                                      |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name        | название представления календаря                                                                                                                              |
| model       | полное имя модели предметной области                                                                                                                          |
| eventStart  | имя поля типа дата/дата/время, которое будет использоваться в качестве времени начала события                                                                 |
| eventStop   | имя поля типа дата/дата/время, которое будет использоваться в качестве времени остановки события                                                              |
| eventLength | если eventStop не указан, длина события в часе (по умолчанию 1)                                                                                               |
| dayLength   | рабочих часов в день (по умолчанию 8)                                                                                                                         |
| colorBy     | имя поля, которое будет использоваться для раскрашивания событий (цвета используются последовательно в соответствии с полем, максимально 30 различных цветов) |
| mode        | режим просмотра по умолчанию (месяц, неделя или день)                                                                                                         |
| onChange    | действие, вызываемое при изменении события (с перетаскиванием или изменением размера)                                                                         |

Данные календаря показывают записи как события. Отображаемое значение, если событие настроено с `<field>` элементами.

## Канбан вид

Канбан-представление — это гибкая информационная панель, похожая на представление.

```xml

<kanban name="project-task-kanban" title="Project Tasks" model="com.axelor.project.db.ProjectTask"
        columnBy="state" sequenceBy="priority" onNew="project.task.kanban.on.new" limit="10">
    <field name="name"/>
    <field name="notes"/>
    <field name="progress"/>
    <field name="startDate"/>
    <field name="endDate"/>
    <field name="user"/>
    <hilite color="danger" if="progress == 0"/>
    <hilite color="success" if="progress == 100"/>
    <hilite color="info" if="progress &gt;= 50"/>
    <hilite color="warning" if="progress &gt; 0"/>
    <template><![CDATA[
  <h4>{{name}}</h4>
  <img ng-if="user" src="{{$image('user', 'image')}}">
  <div class="card-body">{{notes}}</div>
  <div class="card-footer">
    <i class='fa fa-clock-o'></i> <span>{{startDate|date:'yyyy-MM-dd HH:mm:ss'}}</span>
  </div>
  ]]></template>
</kanban>
```

Атрибуты представления канбан:

| Атрибут    | Описание                                                                              |
|------------|---------------------------------------------------------------------------------------|
| name       | имя представления                                                                     |
| model      | полное имя модели предметной области                                                  |
| columnBy   | поле выбора для создания столбцов                                                     |
| sequenceBy | поле, используемое для изменения порядка карточек (только числовые поля)              |
| onNew      | поле для использования при создании записи на лету                                    |
| onMove     | действие для вызова при перемещении канбан-карты                                      |
| limit      | лимит пагинации на столбец                                                            |

## Просмотр карточки

Представление карточек можно использовать для отображения связанных данных, таких как фото, текст и ссылка об одном
предмете, в виде карточек.

```xml

<cards name="contact-cards" title="Contacts" model="com.axelor.contact.db.Contact" orderBy="fullName">
    <field name="fullName"/>
    <field name="phone"/>
    <field name="email"/>
    <field name="address"/>
    <field name="hasImage"/>
    <template><![CDATA[
  <div class="span4 card-image">
    <img ng-if="hasImage" ng-src="{{$image(null, 'image')}}">
    <img ng-if="!hasImage" src="img/user.png">
    <strong>{{fullName}}</strong>
  </div>
  <div class="span8">
    <address>
      <strong>{{address.street}} {{address.area}}</strong><br>
      {{address.city}}<span ng-if="address.state">, {{address.state}}</span><span ng-if="address.zip"> - {{address.zip}}</span><br>
      {{address.country.name}}<br>
      <abbr ng-if="phone" title="Phone">P:</abbr> {{phone}}<br>
      <abbr ng-if="email" title="Email">E:</abbr> {{email}}<br>
    </address>
  </div>
  ]]></template>
</cards>
```

Атрибуты просмотра карточек:

| Атрибут   | Описание                                   |
|-----------|--------------------------------------------|
| name      | имя представления                          |
| model     | полное имя модели предметной области       |
| orderBy   | поле для заказа карт                       |
| cardWidth | указать виджет карты (по умолчанию 33,33%) |

Вы можете использовать `ui-action-click` директиву в шаблоне для выполнения любого действия по событию клика.

Например:

```xml

<template>
    <![CDATA[
        <button type="button" class="btn" ui-action-click="some.action" />
    ]]>
</template>
```

## Пользовательский вид

Пользовательский вид позволяет отображать произвольные данные с использованием шаблонов. Это представление обычно полезно для создания отчетов только для чтения.

Определение пользовательского представления:

```xml
<custom name="view-name" title="View Title">

  <!-- dataset fields (optional) -->
  <field name="some" type="integer" />
  <field name="total" type="decimal" scale="4"/>

  <!-- dataset is required -->
  <dataset type="jpql|sql|rpc">
  <![CDATA[
  // jpql or sql or method call
  ]]>
  </dataset>

  <!-- template is require -->
  <template>
  <![CDATA[
  // angular.js template, data can be accessed using `data`, and first data item
  // is accessible as `first`.
  ]]>
  </template>
</custom>
```

Пример использования с пользовательским шаблоном:

```xml
<!-- Dashboard box with custom template -->
<custom name="report.total.sale" title="Total sale" css="report-box">
  <dataset type="jpql">
  <![CDATA[
  select sum(self.totalAmount) as total from Order self
  ]]>
  </dataset>
  <template>
  <![CDATA[
  <div class="report-data">
    <h1>{{first.total}}</h1>
    <small>Total sale</small>
    <div class="report-percent font-bold text-info pull-right">20% <i class="fa fa-level-up"></i></div>
    <div class="report-tags"><span class="label label-important">Monthly</span></div>
  </div>
  ]]>
  </template>
</custom>
```

| Атрибут   | Описание                                   |
|-----------|--------------------------------------------|
| name      | имя представления                          |
| title     | отображать заголовок представления         |

## Просмотр Ганта
Представление Ганта показывает данные в виде диаграммы Ганта:

```xml
<gantt name="project-task-gantt" title="Task Planning" model="com.axelor.project.db.ProjectTask"
  mode="year"
  taskStart="plannedStartDate"
  taskDuration="plannedDuration"
  taskParent="parentTask"
  taskSequence="sequence"
  taskProgress="plannedProgress"
  x-finish-to-start="finishToStartTaskSet"
  x-start-to-start="startToStartTaskSet"
  x-finish-to-finish="finishToFinishTaskSet"
  x-start-to-finish="startToFinishaskSet">
  <field name="name" />
  <field name="project" />
  <field name="user" />
</gantt>
```

Атрибуты представления Ганта:

| Атрибут            | Описание                                                                                     |
|--------------------|----------------------------------------------------------------------------------------------|
| name               | имя представления Ганта                                                                      |
| title              | название представления Ганта                                                                 |
| model              | полное имя модели предметной области                                                         |
| mode               | режим просмотра, между year, month, weekили day(по умолчанию month)                          |
| taskStart          | имя поля типа дата/дата/время, которое будет использоваться в качестве времени начала        |
| taskDuration       | имя поля продолжительности                                                                   |
| taskEnd            | имя поля типа дата/дата/время, которое будет использоваться как время окончания              |
| taskParent         | имя родительского поля                                                                       |
| taskProgress       | название поля прогресса                                                                      |
| taskSequence       | имя поля для упорядочения задач по порядку                                                   |
| taskUser           | имя пользовательского поля, связанного с задачей                                             |
| x-finish-to-start  | имя поля M2M, содержащего задачи, которые необходимо завершить перед запуском текущей задачи |
| x-start-to-start   | имя поля M2M, содержащего задачи, которые нужно запустить перед запуском текущей задачи      |
| x-finish-to-finish | имя поля M2M, содержащего задачи, которые необходимо завершить до завершения текущей задачи  |
| x-start-to-finish  | имя поля M2M, содержащего задачи, которые нужно запустить до завершения текущей задачи       |

[Прев.](../dev-menu.md)