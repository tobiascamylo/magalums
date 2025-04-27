<h3 align="center">
  Desafio Backend da Magalu
</h3>

<p align="center">

  <img alt="License: MIT" src="https://img.shields.io/badge/license-MIT-%2304D361">
  <img alt="Language: Java" src="https://img.shields.io/badge/language-java-green">
  <img alt="Version: 1.0" src="https://img.shields.io/badge/version-1.0-yellowgreen">
    
</p>

# 📢 Magalums Notification API

[![Java 21](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/) [![Maven](https://img.shields.io/badge/Maven-3.x-red)](https://maven.apache.org/) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)

Uma API simples de agendamento e gerenciamento de notificações, construída com **Java 21**, **Spring Boot**, **Maven** e **MySQL** em contêiner Docker.

---

## 📖 Sumário

- [Visão Geral](#-visão-geral)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [1. Estrutura do Projeto](#1-estrutura-do-projeto)
- [2. Configuração do Banco de Dados (Docker)](#2-configuração-do-banco-de-dados-docker)
- [3. Configuração da Aplicação](#3-configuração-da-aplicação)
- [4. Build & Execução](#4-build--execução)
- [5. Endpoints da API](#5-endpoints-da-api)
    - [POST /notifications](#postnotifications)
    - [GET /notifications/{notificationId}](#getnotificationsnotificationid)
    - [DELETE /notifications/{notificationId}](#deletenotificationsnotificationid)
- [6. Testando a API](#6-testando-a-api)
- [7. Encerramento](#7-encerramento)
- [📄 Licença](#-licença)

---

## 🧐 Visão Geral

Este projeto expõe uma API REST para:
- **Agendar** notificações futuras.
- **Consultar** o status/detalhes de uma notificação por ID.
- **Cancelar** notificações agendadas.

A aplicação usa um banco **MySQL** rodando em Docker e **Spring Scheduling** para despacho das notificações.

---

## 🛠️ Tecnologias

| Camada        | Tecnologia            |
|---------------|-----------------------|
| Linguagem     | Java 21               |
| Framework     | Spring Boot 3.x       |
| Build         | Maven 3.x             |
| Banco de Dados| MySQL 8+ (Docker)     |
| Agendamento   | Spring Scheduling     |

---

## 🔧 Pré-requisitos

1. **Java 21** JDK instalado
2. **Maven** 3.x instalado
3. **Docker** & **Docker Compose**
4. Porta **3306** livre (MySQL)

---

## 1️⃣ Estrutura do Projeto

```text
magalums/
├── src/
│   ├── main/
│   │   ├── java/com/thp/magalums/
│   │   │   ├── MagalumsApplication.java    ← Classe principal
│   │   │   ├── controller/
│   │   │   │   └── NotificationController.java
│   │   │   ├── dto/
│   │   │   │   └── ScheduleNotificationDto.java
│   │   │   ├── entity/
│   │   │   │   └── Notification.java
│   │   │   └── service/
│   │   │       └── NotificationService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/…
├── docker-compose.yml                     ← Definição do MySQL
├── pom.xml
└── README.md
```

## 2️⃣ Configuração do Banco de Dados (Docker)

### 2.1 Crie o arquivo `docker-compose.yml` na raiz:

```yaml
version: '3.8'
services:
  db:
    image: mysql:8.0
    container_name: magalums-db
    ports:
      - "3306:3306"
    environment:
      MYSQL_DATABASE: magaludb
      MYSQL_ROOT_PASSWORD: 1234
    volumes:
      - db-data:/var/lib/mysql

volumes:
  db-data:
```
 ### 2.2 Suba o container
# Inicia o MySQL:
Bash 
``` docker compose up -d ```

# Para parar e remover o container
Bash 
``` docker compose down ```

## 3️⃣ Configuração da Aplicação
 - Em src/main/resources/application.properties, ajuste conforme:
spring.application.name=magalums
spring.jpa.hibernate.ddl-auto=update

# Conexão MySQL (container Docker)
spring.datasource.url=jdbc:mysql://localhost:3306/magaludb
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true

## 4️⃣ Build & Execução
1. Build do projeto:
Bash : ``` mvn clean install ```
2. Executar localmente (Maven):
   Bash : ``` mvn spring-boot:run ```
3. Ou via JAR gerado:
   Bash : ``` java -jar target/magalums-0.0.1-SNAPSHOT.jar ```
4. A aplicação estará disponível em http://localhost:8080.

## 5️⃣ Endpoints da API
Todos os endpoints começam em /notifications.
## POST /notifications
## Descrição: Agendar uma nova notificação.

## Request Body (ScheduleNotificationDto):
   
```json {
  "dateTime": "2024-04-12T15:06:00",
  "destination": "teste1234@gmail.tech",
  "message": "Seja bem vindo ao curso de desenvolvimento",
  "channel": "EMAIL"
}
```

## GET /notifications/{notificationId}
## Descrição: Buscar detalhes de uma notificação pelo ID.

- Parâmetro de Path:

- notificationId (long) — ID da notificação.

- Retorno:

   - 200 OK + JSON Notification

   - 404 Not Found se não existir

## DELETE /notifications/{notificationId}
## Descrição: Cancelar uma notificação agendada.

- Parâmetro de Path:

- notificationId (long) — ID da notificação.

- Retorno:

   - 204 No Content

## 7️⃣ Encerramento
- Para parar o app: CTRL+C na console ou parar o container Docker se estiver rodando nele.

- Para limpar volumes (MySQL):
     Bash : ``` docker compose down -v ```