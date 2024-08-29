# Веб-сервисы

Открытая платформа Axelor предоставляет REST-подобные веб-сервисы JSON.

Веб-сервисы являются доступными `/ws/` конечными точками.

Каждый веб-сервис возвращает данные JSON в определенном формате. Кроме того, для некоторых веб-служб требуются данные
JSON в качестве тела запроса.

## Запрос

```json
{
  "model": "",
  "offset": 0,
  "limit": 40,
  "sortBy": [],
  "data": {},
  "records": [],
  "fields": []
}
```

1. `model`- название модели ресурса
2. `offset`- смещение страницы
3. `limit`- лимит пагинации
4. `sortBy`- список полей для сортировки результата
5. `data`- карта json, зависит от веб-сервиса
6. `records`- список карт json или идентификаторов записей
7. `fields`- название полей

Эти атрибуты запроса зависят от службы, должны быть предоставлены только обязательные атрибуты.

## Ответ

```json
{
  "status": 0,
  "offset": 0,
  "total": 0,
  "errors": {},
  "data": {},
  "data": []
}
```

1. `status`- статус ответа
2. `offset`- текущее смещение страницы
3. `total`- общее количество совпавших записей
4. `errors`- ошибки проверки (ключ - имя поля, значение - сообщение об ошибке)
5. `data`- карта json или массив в зависимости от типа сервиса

Атрибуты ответа зависят от службы, служба может возвращать только определенные атрибуты.

Атрибут status может иметь следующие значения:

| Код | Причина         |
|-----|-----------------|
| 0   | успех           |
| -1  | отказ           |
| -4  | Ошибка проверки |

## Cors

Открытая платформа Axelor поддерживает CORS, который обычно требуется для вызова веб-сервисов из мобильных приложений.

Им можно управлять с помощью следующих настроек:

```properties
# CORS configuration
# ~~~~~
# CORS settings to allow cross origin requests
# regular expression to test allowed origin or * to allow all (not recommended)
#cors.allow-origin = *
#cors.allow-credentials = true
#cors.allow-methods = GET,PUT,POST,DELETE,HEAD,OPTIONS
#cors.allow-headers = Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers
#cors.expose-headers =
#cors.max-age = 1728000
```

Как правило, вы должны установить только cors.allow-origin список разрешенных доменов. Остальные варианты следует
оставить как есть.

Чтобы избежать предварительных запросов cors, не добавляйте заголовок и X-Requested-With предоставляйте Accept:
application/json заголовок для запросов и используйте заголовок для методов.

## CSRF-защита

На открытой платформе включена защита от CSRF с помощью pac4j. Если вы хотите вызывать веб-службы из клиента браузера,
вам нужно убедиться, что вы обрабатываете токен CSRF:

+ прочитать файл cookie с именем CSRF-TOKEN.
+ при выполнении вашего запроса передайте значение этого файла cookie в заголовке с именем X-CSRF-Token.

Пример:

```html

<ul id="product-list"></ul>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
  function getCookies() {
    return decodeURIComponent(document.cookie)
      .split('; ')
      .reduce((acc, cur) => { const [k, v] = cur.split('='); return {...acc, [k]: v}; }, {});
  }

  const cookies = getCookies();

  $.ajax({
    url: 'ws/rest/com.axelor.sale.db.Product/search',
    type: 'POST',
    headers: { 'X-CSRF-Token': cookies['CSRF-TOKEN'] },
    data: JSON.stringify({
      fields: ['name'],
      sortBy: ['name'],
      limit: 20
    }),
    contentType: 'application/json'
  }).done(response => {
    const productNames = response.data.map(e => e.name);
    const productList = $('#product-list');
    productNames.forEach(name => {
      $(`<li>${name}</li>`).appendTo(productList);
    });
  });









</script>
```

## Службы аутентификации

На данный момент веб-сервисы используют аутентификацию на основе сеанса. Таким образом, клиентское приложение должно
отслеживать идентификатор сеанса и файлы cookie между запросами.

Сеанс может быть установлен с помощью сервисов входа в систему:

### Авторизоваться

> POST /login.jsp HTTP/1.1
>
> Content-Type: application/json

```json
{
  "username": "admin",
  "password": "secret"
}
```

Данные аутентификации отправляются в теле запроса.

Возвращает ответ с HTTP-статусом, 200если вход выполнен успешно, в противном случае возвращает HTTP-статус 401.

Отображаются только соответствующие заголовки и тело запроса.

### Выйти

> GET /logout HTTP/1.1
>
> Content-Type: application/json

Ответ на вход в систему возвращает файлы cookie сеанса, которые должны отслеживаться клиентским приложением.

## Службы метаданных

Ниже приведены службы для получения метаданных моделей предметной области.

### Получить модели

Этот сервис возвращает список всех моделей доменов.

Request

```text
GET /ws/meta/models HTTP/1.1
Accept: application/json
```

Response

```json
{
  "status": 0,
  "total": 32,
  "data": [
    "com.axelor.contact.db.Contact",
    "..."
  ]
}
```

### Получить свойства модели

Этот сервис можно использовать для получения свойств модели предметной области.

Request

```text
GET /ws/meta/fields/com.axelor.contact.db.Contact HTTP/1.1
Accept: application/json
```

Response

```json
{
  "status": 0,
  "data": {
    "model": "com.axelor.contact.db.Contact",
    "fields": [
      {
        "name": "firstName",
        "type": "STRING",
        "required": true
      },
      {
        "name": "lastName",
        "type": "STRING"
      },
      "..."
    ]
  }
}
```

## REST-сервисы

REST-подобные сервисы для общих операций поиска, создания, чтения, обновления, удаления.

### Найти записи

Веб-сервис доступен по следующему шаблону URL:

```text
GET /ws/rest/:model
```

Request

```text
GET /ws/rest/com.axelor.contact.db.Contact?offset=0&limit=10 HTTP/1.1
Accept: application/json
```

Response

```json
{
  "status": 0,
  "offset": 0,
  "total": 120,
  "data": [
    {
      "id": 1,
      "fullName": "John Smith",
      "email": "j.smith@gmail.com",
      "version": 1
    },
    {
      "id": 9,
      "fullName": "Tom Boy",
      "email": "tom.boy@gmail.com",
      "version": 0
    },
    "..."
  ]
}
```

### Прочитать запись

Веб-сервис доступен по следующему шаблону URL:

```text
GET /ws/rest/:model/:id
```

Request

```text
GET /ws/rest/com.axelor.contact.db.Contact/1 HTTP/1.1
Accept: application/json
```

Response

```json
{
  "status": 0,
  "data": [
    {
      "id": 1,
      "version": 0,
      "fullName": "John Smith",
      "firstName": "John",
      "..."
    }
  ]
}
```

### Создать запись

Веб-сервис доступен по следующему шаблону URL:

```text
PUT /ws/rest/:model
```

Request

```text
PUT /ws/rest/com.axelor.contact.db.Contact HTTP/1.1
Accept: application/json
Content-Type: application/json
```

```json
{
  "data": {
    "firstName": "John",
    "lastName": "Smith",
    "email": "j.smith@gmail.com",
    "..."
  }
}
```

Response

```json
{
  "status": 0,
  "data": [
    {
      "id": 1,
      "version": 0,
      "fullName": "John Smith",
      "firstName": "John",
      "..."
    }
  ]
}
```

### Обновить запись

Веб-сервис доступен по следующему шаблону URL:

```text
POST /ws/rest/:model/:id
```

Request

```text
POST /ws/rest/com.axelor.contact.db.Contact/1 HTTP/1.1
Accept: application/json
Content-Type: application/json
```

```json
{
  "data": {
    "id": 1,
    "version": 1,
    "firstName": "John",
    "lastName": "SMITH"
    "..."
  }
}
```

Response

```json
{
  "status": 0,
  "data": [
    {
      "id": 1,
      "version": 2,
      "fullName": "John SMITH",
      "firstName": "John",
      "lastName": "SMITH",
      "..."
    }
  ]
}
```

> Номер версии используется для обеспечения не конфликтующих модификаций записи и поэтому должен быть указан.

### Удалить запись

Веб-сервис доступен по следующему шаблону URL:

```text
DELETE /ws/rest/:model/:id
```

Request

```text
DELETE /ws/rest/com.axelor.contact.db.Contact/1 HTTP/1.1
Accept: application/json
```

Response

```json
{
  "status": 0,
  "data": [
    1
  ]
}
```

Это `data` массив с идентификатором удаленной записи в качестве единственного элемента.

[Прев.](../dev-menu.md)