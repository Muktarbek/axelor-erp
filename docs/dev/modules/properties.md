# Конфигурации

В этой главе мы увидим различные параметры конфигурации.

## Введение

Конфигурация приложения обеспечивается через различные файлы конфигурации. Это:

* `axelor-config.properties`- конфигурация приложения
* `persistence.xml`- конфигурация гибернации/jpa

Наиболее важным из них является `axelor-config.properties`.

## Конфигурация приложения

AOP считывает значения конфигурации из нескольких источников (в порядке возрастания):

+ Внутренний файл конфигурации в формате src/main/resources.
+ Внешний файл конфигурации, использующий AXELOR_CONFIG переменную среды или axelor.config системное свойство.
+ Переменные среды с префиксом AXELOR_CONFIG_
+ Системные свойства с префиксом axelor.config.

Каждый из этих источников переопределяет значения предыдущего. Окончательная конфигурация представляет собой
совокупность свойств, определенных всеми этими источниками. Например, свойство, настроенное с использованием свойства
среды, переопределяет значение, предоставленное с помощью файла axelor-config.properties.

## Внутренняя конфигурация

main axelor-config.propertiesпредоставляет различные значения конфигурации для приложения. Он находится в
src/main/resourcesкаталоге проекта приложения.

Обратите внимание, что этот внутренний файл конфигурации является необязательным.

Также поддерживается формат YAML. axelor-config.properties может быть в формате YAML (yml или yamlext). Он должен иметь
только один внутренний файл конфигурации (в свойствах или в формате YAML).

## Внешняя конфигурация

Внешний файл конфигурации подобен внутреннему файлу конфигурации. Это может быть файл свойств или формат файла YAML.

Чтобы использовать внешний файл конфигурации, добавьте либо AXELOR_CONFIG переменную среды, либо axelor.config системное
свойство. Обратите внимание, что системные свойства получают предпочтения по сравнению с переменной среды.

```bash
 export JAVA_OPTS="-Daxelor.config=/path/to/dev.properties"
```

## Настройки базы данных

Мы можем настроить параметры подключения к базе данных со следующими свойствами:

```properties
# Database settings
# ~~~~~
# See hibernate documentation for connection parameters
db.default.ddl=update 
db.default.url=jdbc:postgresql://localhost:5432/my-database 
db.default.user=username 
db.default.password=secret 
```

## Другие настройки

| Имя ключа                                       | Описание                                                                                                            | По умолчанию                                                                                                                                     |
|-------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| application.name                                | Имя приложения                                                                                                      | Мое приложение                                                                                                                                   |
| application.description                         | описание приложения                                                                                                 |                                                                                                                                                  |
| application.version                             | версия приложения                                                                                                   |                                                                                                                                                  |
| application.author                              | автор приложения                                                                                                    |                                                                                                                                                  |
| application.copyright                           | авторское право приложения                                                                                          |                                                                                                                                                  |
| application.logo                                | логотип заголовка. Должен быть 40px в высоту с прозрачным фоном                                                     | img/axelor.png                                                                                                                                   |
| application.home                                | домашний сайт. Ссылка для использования с логотипом в шапке                                                         |                                                                                                                                                  |
| application.help                                | онлайн помощь. Ссылка для использования на странице «О нас»                                                         |                                                                                                                                                  |
| application.mode                                | режим развертывания приложения. Может быть prodилиdev                                                               | разработчик                                                                                                                                      |
| application.theme                               | CSS-тема                                                                                                            | тема по умолчанию                                                                                                                                |
| application.locale                              | локаль по умолчанию                                                                                                 | системные установки по умолчанию                                                                                                                 |
| application.base-url                            | базовый адрес приложения                                                                                            |                                                                                                                                                  |
| application.multi-tenancy                       | включить мультиарендность                                                                                           | ЛОЖЬ                                                                                                                                             |
| application.config-provider                     | if-featureпользовательский класс                                                                                    |                                                                                                                                                  |
| application.domain-blocklist-pattern            | шаблон для проверки выражений предметной области                                                                    |                                                                                                                                                  |
| application.script.cache.size                   | размер кеша groovy-скриптов                                                                                         | 500                                                                                                                                              |
| application.script.cache.expire-time            | время истечения срока действия записи кэша groovy scripts (в минутах)                                               | 10                                                                                                                                               |
| application.permission.disable-action           | не проверять ли разрешения на действия                                                                              | ЛОЖЬ                                                                                                                                             |
| application.permission.disable-relational-field | не проверять ли разрешения реляционных полей                                                                        | ЛОЖЬ                                                                                                                                             |
| view.single-tab                                 | использовать ли макет с одной вкладкой                                                                              | ЛОЖЬ                                                                                                                                             |
| view.max-tabs                                   | определить максимально допустимое количество открытых вкладок                                                       |                                                                                                                                                  |
| view.menubar.location                           | установить стиль меню. Может быть left, topилиboth                                                                  | оба                                                                                                                                              |
| view.toolbar.show-titles                        | показывать ли названия кнопок на панели инструментов                                                                | ЛОЖЬ                                                                                                                                             |
| view.confirm-yes-no                             | показывать ли диалоговое окно подтверждения с кнопками «да/нет» (иначе «Отмена/ОК»)                                 | ЛОЖЬ                                                                                                                                             |
| view.grid.selection                             | если установлено значение checkbox, у виджетов сетки будет включен выбор флажка                                     |                                                                                                                                                  |
| view.grid.editor-buttons                        | отображать ли кнопки подтверждения/отмены в редакторе строк сетки                                                   | истинный                                                                                                                                         |
| view.allow-customization                        | отключить ли настройку представлений                                                                                | истинный                                                                                                                                         |
| view.adv-search.share                           | отключать ли совместное использование расширенного поиска                                                           | истинный                                                                                                                                         |
| view.adv-search.export-full                     | отключить ли полный экспорт в предварительном поиске                                                                | истинный                                                                                                                                         |
| view.collaboration.enabled                      | включить ли совместную работу с просмотром                                                                          | истинный                                                                                                                                         |
| view.form.check-version                         | проверять ли значение версии для одновременных обновлений при переключении вкладок                                  | ЛОЖЬ                                                                                                                                             |
| user.password.pattern                           | шаблон для проверки пароля пользователя                                                                             | .{4,}                                                                                                                                            |
| user.password.pattern-title                     | заголовок, отображаемый для шаблона пароля                                                                          | "Пожалуйста, используйте не менее 4 символов."                                                                                                   |
| api.pagination.max-per-page                     | определить максимальное количество элементов на странице, -1 означает неограниченное                                | 500                                                                                                                                              |
| api.pagination.default-per-page                 | определить количество элементов по умолчанию на странице                                                            | 40                                                                                                                                               |
| session.timeout                                 | время ожидания сеанса (в минутах)                                                                                   | 60                                                                                                                                               |
| session.cookie.secure                           | определить cookie сеанса как безопасный                                                                             |                                                                                                                                                  |
| encryption.password                             | пароль шифрования                                                                                                   |                                                                                                                                                  |
| encryption.algorithm                            | алгоритм шифрования (CBC или GCM)                                                                                   |                                                                                                                                                  |
| encryption.old-password                         | старый пароль шифрования                                                                                            |                                                                                                                                                  |
| encryption.old-algorithm                        | старый алгоритм шифрования (CBC или GCM)                                                                            |                                                                                                                                                  |
| reports.design-dir                              | внешний каталог для дизайнов отчетов о рождении                                                                     | \{user.home}/.axelor/отчеты                                                                                                                      |
| reports.fonts-config                            | Путь конфигурации настраиваемых шрифтов для дизайна отчета о бирте                                                  |                                                                                                                                                  |
| data.upload.dir                                 | путь хранения для загружаемых файлов                                                                                | \{user.home}/.axelor/вложения                                                                                                                    |
| data.upload.max-size                            | максимальный размер загрузки (в МБ)                                                                                 | 5                                                                                                                                                |
| data.upload.filename-pattern                    | загрузить шаблон имени файла                                                                                        |                                                                                                                                                  |
| data.upload.allowlist.pattern                   | шаблон имени файла списка разрешенных, будут разрешены только соответствующие файлы                                 |                                                                                                                                                  |
| data.upload.blocklist.pattern                   | шаблон имени файла черного списка, соответствующие файлы будут отклонены                                            |                                                                                                                                                  |
| data.upload.allowlist.types                     | Тип содержимого списка разрешений может использоваться для разрешения загрузки файлов с соответствующим содержимым. |                                                                                                                                                  |
| data.upload.blocklist.types                     | Тип содержимого списка блокировки может использоваться для блокировки загрузки файлов с соответствующим содержимым. |                                                                                                                                                  |
| data.export.encoding                            | кодировка экспорта данных                                                                                           | UTF-8                                                                                                                                            |
| data.export.dir                                 | путь хранения для действия экспорта                                                                                 | \{user.home}/.axelor/экспорт данных                                                                                                              |
| data.export.max-size                            | максимальное количество записей для экспорта, -1 означает неограниченное                                            | -1                                                                                                                                               |
| data.export.fetch-size                          | экспортировать размер выборки                                                                                       | 500                                                                                                                                              |
| data.export.separator                           | разделитель экспорта по умолчанию                                                                                   | ';'                                                                                                                                              |
| data.export.locale                              | определить фиксированную локаль для всего экспорта                                                                  |                                                                                                                                                  |
| data.import.demo-data                           | импортировать ли демонстрационные данные для приложения                                                             | истинный                                                                                                                                         |
| template.search-dir                             | путь хранения шаблона для шаблона groovy                                                                            | \{user.home}/.axelor/templates                                                                                                                   |
| cors.allow-origin                               | разделенный запятыми список источников, чтобы разрешить                                                             | '*'                                                                                                                                              |
| cors.allow-credentials                          | поддерживаются ли учетные данные                                                                                    | истинный                                                                                                                                         |
| cors.allow-methods                              | разделенный запятыми список разрешенных методов                                                                     | ПОЛУЧИТЬ, ПОСТАВИТЬ, ПОСТАВИТЬ, УДАЛИТЬ, ГОЛОВУ, ВАРИАНТЫ                                                                                        |
| cors.allow-headers                              | разделенный запятыми список заголовков, разрешенных в запросе                                                       | Происхождение, Принять, Авторизация, X-Requested-With, X-CSRF-Token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers |
| cors.expose-headers                             | разделенный запятыми список заголовков для включения в ответ                                                        |                                                                                                                                                  |
| cors.max-age                                    | как долго ответ на предварительный запрос может кэшироваться клиентами (в секундах)                                 | 1728000                                                                                                                                          |
| cors.expose-headers                             | разделенный запятыми список заголовков для включения в ответ                                                        |                                                                                                                                                  |
| quartz.enable                                   | включить ли кварцевый планировщик                                                                                   | ЛОЖЬ                                                                                                                                             |
| quartz.thread-count                             | общее количество потоков в кварцевом пуле потоков                                                                   | 3                                                                                                                                                |
| mail.smtp.host                                  | хост smtp-сервера                                                                                                   |                                                                                                                                                  |
| mail.smtp.port                                  | порт smtp-сервера                                                                                                   |                                                                                                                                                  |
| mail.smtp.user                                  | Имя пользователя для входа в smtp                                                                                   |                                                                                                                                                  |
| mail.smtp.password                              | smtp-пароль                                                                                                         |                                                                                                                                                  |
| mail.smtp.channel                               | канал шифрования smtp (starttls или ssl)                                                                            |                                                                                                                                                  |
| mail.smtp.timeout                               | Тайм-аут чтения сокета smtp                                                                                         | 60000                                                                                                                                            |
| mail.smtp.connection-timeout                    | тайм-аут соединения сокета smtp                                                                                     | 60000                                                                                                                                            |
| mail.smtp.from                                  | по умолчанию из атрибута                                                                                            |                                                                                                                                                  |
| mail.imap.host                                  | Хост imap-сервера                                                                                                   |                                                                                                                                                  |
| mail.imap.port                                  | порт imap-сервера                                                                                                   |                                                                                                                                                  |
| mail.imap.user                                  | имя пользователя для входа в imap                                                                                   |                                                                                                                                                  |
| mail.imap.password                              | имап пароль                                                                                                         |                                                                                                                                                  |
| mail.imap.channel                               | канал шифрования imap (starttls или ssl)                                                                            |                                                                                                                                                  |
| mail.imap.timeout                               | тайм-аут чтения сокета imap                                                                                         | 60000                                                                                                                                            |
| mail.imap.connection-timeout                    | тайм-аут соединения сокета imap                                                                                     | 60000                                                                                                                                            |

Все указанные пути могут использовать специальные переменные:

+ {user.home}: ссылка на домашний каталог, System.getProperty("user.home")
+ {java.io.tmpdir}: ссылка на каталог tmp,System.getProperty("java.io.tmpdir")
+ {year}: текущий год, YYYYформат
+ {month}: текущий месяц, с 1 по 12
+ {day}: текущий день, с 1 по 31

Различия между режимом prodи dev:

+ используйте файл minify js/css.
+ использовать кеш браузера.
+ не отображать техническое всплывающее окно.

## Конфигурация JPA/спящего режима

Расположенный persistence.xml в разделе src/main/resources/META-INF предоставляет конфигурацию JPA/Hibernate.

Для подтверждения требований JPA требуется минимальный XML-файл сохраняемости:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<persistence version="2.1"
  xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
  <persistence-unit name="persistenceUnit" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <shared-cache-mode>ENABLE_SELECTIVE</shared-cache-mode>
  </persistence-unit>
</persistence>
```

Некоторые конфигурации JPA/Hibernate также могут быть установлены в axelor-config.properties файле:

| hibernate.hikari.minimumIdle     | минимальное количество простаивающих подключений для поддержания в пуле                    | 5      |
|----------------------------------|--------------------------------------------------------------------------------------------|--------|
| hibernate.hikari.maximumPoolSize | максимальный размер, которого может достичь пул                                            | 20     |
| hibernate.hikari.idleTimeout     | максимальное количество времени, в течение которого соединение может бездействовать в пуле | 300000 |
| hibernate.jdbc.batch_size        | максимальное количество пакетов операторов, прежде чем попросить драйвер выполнить пакет   | 20     |
| hibernate.jdbc.fetch_size        | Размер выборки JDBC                                                                        | 20     |

Все остальные hibernate.*свойства также передаются в Hibernate.

[Прев.](../dev-menu.md)