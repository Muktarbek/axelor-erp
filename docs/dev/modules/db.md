# Источник данных CSV

CSV (значения, разделенные запятыми) — наиболее широко используемый устаревший формат данных.

Для преобразования данных CSV требуются csv-configфайлы, которые представляют собой правила сопоставления данных на
основе XML.

## Отображение данных

Определения сопоставления данных CSV определяются с использованием синтаксиса XML. Типичный файл конфигурации выглядит
так:

```xml
<?xml version="1.0"?>
<csv-inputs xmlns="http://axelor.com/xml/ns/data-import"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://axelor.com/xml/ns/data-import
    https://axelor.com/xml/ns/data-import/data-import_6.0.xsd">

    <input file="titles.csv" type="com.axelor.contact.db.Title"/>
    <input file="circles.csv" type="com.axelor.contact.db.Circle"/>
    <input file="company.csv" type="com.axelor.contact.db.Company"/>
    <input file="contacts.csv" type="com.axelor.contact.db.Contact"/>

    <input file="titles.csv" type="com.axelor.contact.db.Title"
           search="self.code = :code" update="true">
        <bind to="code" column="code"></bind>
        <bind to="name" column="name" if-empty="true"></bind>
    </input>

    <input file="titles-no-header.csv" type="com.axelor.contact.db.Title"
           search="self.code = :code" update="true">
        <header>code,name</header>
        <bind to="code" column="code"></bind>
        <bind to="name" column="name" if-empty="true"></bind>
    </input>

    <input file="contacts-update.csv" type="com.axelor.contact.db.Contact"
           separator="\t"
           search="self.firstName = :firstName AND self.lastName = :lastName"
           update="true"/>

    <input file="taxes.csv" type="com.axelor.sale.db.Tax"/>

    <input file="sale-orders.csv" type="com.axelor.sale.db.Order"
           call="com.axelor.data.tests.Validators:validateSaleOrder">

        <!-- transform boolean value into 'true' or 'false' -->
        <bind column="confirmed" to="confirmed"
              eval="confirmed ==~ /^(T|Y|1)$/ ? 'true' : 'false'"/>

        <bind column="date" to="orderDate"/>
        <bind column="date" to="createDate"/>

        <!-- default value provided with eval expression -->
        <bind to="confirmDate" eval="java.time.LocalDate.now()"/>

        <bind to="customer" search="self.email = :email" if="!email.empty">
            <bind column="firstName" to="firstName"/>
            <bind column="lastName" to="lastName"/>
            <bind column="email" to="email"/>
        </bind>

        <bind to="customer" if="email.empty">
            <bind column="firstName" to="firstName"/>
            <bind column="lastName" to="lastName"/>
            <bind to="email" eval='"${firstName}.${lastName}@gmail.com".toLowerCase()'/>
        </bind>

        <bind to="items">
            <bind column="q1" to="quantity"/>
            <bind column="r1" to="price"/>
            <bind to="product" search="self.name = :p1">
                <bind column="p1" to="name"/>
                <bind column="p1" to="code"/>
            </bind>
            <bind column="t11" to="taxes" search="self.code = :t11"/>
            <bind column="t12" to="taxes" search="self.code = :t12"/>
        </bind>

        <bind to="items">
            <bind column="q2" to="quantity"/>
            <bind column="r2" to="price"/>
            <bind to="product" search="self.name = :p2">
                <bind column="p2" to="name"/>
                <bind column="p2" to="code"/>
            </bind>
            <bind column="t2" to="taxes" search="self.code in :t2"
                  eval="t2.split('\\|') as List"/>
        </bind>

    </input>

</csv-input>
```

Как видите, файл сопоставления сопоставляет входные файлы с целевыми объектами и связывает столбцы csv с полями целевого
объекта. Синтаксис не требует пояснений и прост для понимания.

Давайте посмотрим на привязку в деталях:

Тег `<input>` используется для сопоставления исходного CSV-файла с типом целевой модели.

| Атрибут            | Описание                                                                                                                                                                 |
|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| file               | имя исходного входного файла                                                                                                                                             |
| type               | название целевой модели                                                                                                                                                  |
| separator          | символ-разделитель (по умолчанию запятая ,)                                                                                                                              |
| search             | JPQL, где предложение ищет существующую запись                                                                                                                           |
| update             | trueразрешить только обновление (если существующая запись не найдена, она не будет создана)                                                                              |
| call               | вызвать метод преобразованного объекта перед его сохранением в базе данных                                                                                               |
| prepare-context    | вызвать метод для подготовки контекста перед преобразованием строки csv                                                                                                  |
| search-call        | вызвать метод для возврата bean-компонента. Используется для поиска бина в случае сложного поиска. searchВыражение JPQL будет игнорироваться в пользу search-callметода. |

Вот пример java-методов:

```java
public class DemoImport {

    /**
     * This method is called with `prepare-context` attribute.
     * It prepares the global context before transforming the csv row.
     */
    public void prepareData(Map context) {

        Order order = new Order();
        order.setCreateDate(new LocalDate());
        order.setOrderDate(new LocalDate());

        context.put("_saleOrder", order);
    }

    /**
     * This method is called with `search-call` attribute.
     *
     * This method is called  in favor of `search` JPQL expression.
     *
     * @param values the value map that represents the csv row being imported
     * @return the bean object to update
     */
    public Object searchData(Map values) {
        Object bean = searchForRecord(values);
        return bean;
    }

    /**
     * This method is called with `call` attribute.
     *
     * This method is called for each record being imported.
     *
     * @param bean the bean instance created from the imported record
     * @param values the value map that represents the imported data
     * @return the bean object to persist (in most cases the same bean object)
     */
    public Object importData(Object bean, Map values) {
        Order order = (Order) bean;
        // do something with order
        return order;
    }

}
```

Можно `<input>` использовать по-разному. Вы можете видеть, что первые четыре входа в примере очень просты. Это связано с
автоматическим импортом, поскольку имена столбцов заголовка csv совпадают с именами полей целевой модели. В противном
случае нам нужно указать привязку вручную с помощью `<bind>` тега.

Тег `<bind>` можно использовать для сопоставления столбцов CSV с полем целевого объекта.

| Атрибут  | Описание                                                                                   |
|----------|--------------------------------------------------------------------------------------------|
| column   | имя столбца CSV                                                                            |
| to       | имя поля целевой модели                                                                    |
| adapter  | адаптер типа, за которым следует необязательный строковый аргумент, разделенный \|символом |
| search   | jpql, где поиск существующей записи                                                        |
| update   | если поиск возвращает существующую запись, обновлять ли ее                                 |
| eval     | отличное выражение, чтобы преобразовать значение                                           |
| if       | логическое заводное выражение, связывание только в том случае, если условие выполнено      |
| if-empty | обновлять целевое значение только в том случае, если целевое поле пусто (или равно null)   |

Тег `<bind>` может снова иметь вложенные <bind>теги в случае связывания реляционных полей.

### Автоматический импорт

Если файлы данных CSV имеют идентичные столбцы с именами полей целевого класса модели предметной области, их можно
импортировать автоматически с минимальной настройкой.

Первая строка CSV-файла содержит информацию о полях.

Пунктирные поля `title.code` и можно использовать непосредственно для связанных записей group.code.company.code

Одной из основных задач импорта данных из внешнего источника является управление отношениями (интеграция данных).
Поскольку первичные ключи часто являются автоматически сгенерированными значениями, непросто сопоставить исходные ключи
с целевым ключом. Чтобы решить эту проблему, вместо ссылки на отношения с первичным ключом мы используем один или
несколько ключей связанного объекта, что приводит к уникальному ограничению для поиска записи, на которую ссылаются.

Здесь `title.code` сообщает механизму преобразования запрашивать `title` запись, на которую ссылаются, по `code` полю.
Мы также можем предоставить несколько полей поиска, например:

```csv
orderDate,confirmed,...,customer.firstName,customer.lastName,...
```

В этом случае запись будет получена с помощью выражения AND следующим образом:

```sql
SELECT self FROM Contact self WHERE self.firstName = :firstName AND self.lastName = :lastName
```

Точно так же many-to-manyполя могут быть импортированы с несколькими значениями, разделенными |следующим образом:

```csv
...,taxes.code
...,tax1.4|tax0.2|tax0.4
```

Поле «многие ко многим» будет установлено в результате следующего запроса:

```sql
SELECT self FROM Tax self WHERE self.code IN :code
```

Формат `csv-config.xmlдля автоматического импорта выглядит следующим образом:

```xml
<?xml version="1.0"?>
<csv-inputs xmlns="http://axelor.com/xml/ns/data-import"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://axelor.com/xml/ns/data-import
  https://axelor.com/xml/ns/data-import/data-import_6.0.xsd">

  <input file="titles.csv" type="com.axelor.contact.db.Title"/>
  <input file="company.csv" type="com.axelor.contact.db.Company"/>
  ...
  ...
  ...
</csv-inputs>
```

Если вам нужно переопределить автоматическую привязку, лучший способ обработки — привязать столбец к значению контекста, а затем использовать это значение контекста в привязке к полю.

Пример:

```xml
<input file="contacts.csv" type="com.axelor.contact.db.Contact">
    <bind to="_lastName" column="lastName"/>
    <bind to="lastName" eval="_lastName + ..." if="..."/>
</input>
```

[Прев.](../dev-menu.md)