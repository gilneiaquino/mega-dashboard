# 📊 Mega Dashboard — Plataforma de Dashboards com Spring Boot + Kafka + MySQL

O **Mega Dashboard** é um projeto backend moderno desenvolvido com **Spring Boot**, integrando **Kafka**, **MySQL**, **Docker** e uma arquitetura limpa, preparado para futuras integrações com AWS (Aurora, MSK etc.) e aplicações frontend.

O objetivo do projeto é fornecer uma base sólida para criação de serviços que gerenciam, processam e distribuem dados para **dashboards analíticos**, com pipeline de eventos via Kafka e APIs REST performáticas.

---

## 🚀 Tecnologias Utilizadas

### **Backend**
- Java **17**
- Spring Boot **3.x**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Kafka
  - Spring Boot Actuator
  - Spring Validation
- Lombok

### **Mensageria**
- Apache Kafka (Confluent images)
- Zookeeper
- Kafka UI (Provectus)

### **Banco de Dados**
- MySQL 8
- Suporte futuro para **AWS Aurora MySQL**

### **Infraestrutura / DevOps**
- Docker
- Docker Compose
- Maven
- Kafka UI para monitoramento e testes

---

## 📁 Estrutura de Pastas

mega-dashboard
├── src
│ ├── main
│ │ ├── java/br/com/megadashboard
│ │ │ ├── MegaDashboardApplication.java
│ │ │ ├── config/ # Configurações (Kafka, Security, etc.)
│ │ │ ├── controller/ # Endpoints REST
│ │ │ ├── service/ # Regras de negócio
│ │ │ ├── repository/ # Repositórios JPA
│ │ │ ├── model/ # Entidades JPA
│ │ │ └── kafka/ # Producers / Consumers
│ │ └── resources
│ │ ├── application.properties
│ │ └── static/templates (opcional)
│ └── test/java/br/com/megadashboard
│
├── docker-compose.yml
├── Dockerfile
└── pom.xml


---

## 🐳 Ambiente com Docker

O ambiente completo sobe:

| Serviço      | Porta Local | Descrição |
|--------------|------------|-----------|
| MySQL        | `3306`     | Banco de dados principal |
| Kafka        | `9092`     | Acesso externo |
| Kafka (interno) | `29092` | Comunicação docker-interna |
| Zookeeper    | `2181`     | Coordenação do Kafka |
| Kafka UI     | `8081`     | UI para monitorar tópicos |
| API Spring   | `8080`     | Aplicação backend |

### ▶️ Subir o ambiente

```bash
docker-compose up -d --build

docker-compose down -v
