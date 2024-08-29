# Настройка и Запуск

Это **краткий мануал** для настройки и запуска проекта.

## Что нужно

Уметь пользоваться такими технологиями\инструментами:

+ [Git](https://git-scm.com/downloads) — распределённая система управления версиями.
+ [Java](https://jdk.java.net/archive/) — строго типизированный объектно-ориентированный язык программирования общего
  назначения.
+ [IDE](https://code.visualstudio.com/) — Интегрированная среда разработки. Советую
  использовать [`IntelliJ IDEA`](https://www.jetbrains.com/ru-ru/idea/), но вы можете использовать свой любимый.
+ [Postgres](https://www.postgresql.org/download/) — свободная объектно-реляционная система управления базами данных.
+ Иметь знания по
  использованию [терминала](https://ru.wikipedia.org/wiki/%D0%A2%D0%B5%D1%80%D0%BC%D0%B8%D0%BD%D0%B0%D0%BB)

## Установка и настройка

### База данных

Для начала нужно создать и настроить базу данных для запуска проекта.

Создание **базы данных**

```sql
CREATE DATABASE goverp;
```

Далее создаём **пользователя**

```sql
CREATE USER goverp_user WITH ENCRYPTED PASSWORD 'goverp_password';
```

Теперь наделяем **всеми привилегиями** созданного пользователя

```sql
GRANT ALL PRIVILEGES ON DATABASE goverp TO goverp_user;
```

### Склонируйте код

Откройте терминал и с клонируйте проект из [репозитория](https://github.com/sanaripdolbor/GovERP) с модулями

```bash
git clone --recurse-submodules https://github.com/sanaripdolbor/GovERP.git goverp
```

После, перейдите в каталог проекта

```bash
cd goverp
```

Структура проекта выглядит так.

```text
goverp
└── src
│   └── main
│       ├── java
│       └── resources
│           └── META-INF
│               ├── axelor-config.properties 
│               └── persistence.xml 
├── gradle 
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── modules 
├── gradlew 
├── gradlew.bat 
├── settings.gradle 
└── build.gradle 
```

Далее вам нужно будет поменять `учетные данные авторизации` для базы данных. Откройте файл `axelor-config.properties` и
там вы должны найти такие параметры.

```properties
# Database settings
# ~~~~~
db.default.driver=org.postgresql.Driver
db.default.ddl=update
db.default.url=jdbc:postgresql://localhost:5432/postgres
db.default.user=postgres
db.default.password=postgres
```

Теперь замените ранее созданные `учетные данные` на свои.

```properties
# Database settings
# ~~~~~
db.default.driver=org.postgresql.Driver
db.default.ddl=update
db.default.url=jdbc:postgresql://localhost:5432/goverp
db.default.user=goverp_user
db.default.password=goverp_password
```

### Запуск

Для запуска нужно ввести команду в терминале

```bash
./gradlew --no-daemon run
```