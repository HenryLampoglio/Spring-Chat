# 💬API de Chat em Tempo Real

Um sistema de comunicação em tempo real (1-to-1) construído com **Java 17**, **Spring Boot** e **WebSockets**. O projeto foi desenhado focando em escalabilidade e separação de responsabilidades, utilizando os princípios da **Clean Architecture** e infraestrutura conteinerizada.

> ⚠️ **Nota:** O front-end desta aplicação foi construído em Angular e está localizado em um [repositório separado](#).

## 🚀 Status e Roadmap

O projeto está em desenvolvimento ativo. Atualmente, o core de comunicação bidirecional está funcional.

- [x] Conexão via WebSockets.
- [x] Chat 1-to-1 em tempo real.
- [x] Autenticação e adição de contatos.
- [ ] Histórico de conversas (Integração com DynamoDB). *[Em andamento]*
- [ ] Mensageria assíncrona/Roteamento (Integração com RabbitMQ). *[Em andamento]*
- [ ] Grupos e Salas Públicas.

## 🏗️ Arquitetura (Clean Architecture)

A aplicação foi estruturada em 3 camadas principais para garantir o desacoplamento total entre as regras de negócio e os frameworks (Inversão de Dependência):

1. **Core (Domain):** O coração da aplicação. Contém as entidades de negócio puras (agrupadas por domínio, ex: `user`, `connection`), exceções de domínio, enums e classes abstratas base (ID, timestamps). Nenhuma dependência externa entra aqui.
2. **Application:** Contém as regras de negócio orquestradas pelos **Use Cases**. Aqui residem os `Commands` (dados formatados de entrada), as `Ports` (interfaces que recebem requisições da camada exterior), os `Gateways` (interfaces de saída) e os `ResponseDTOs`.
3. **Infrastructure:** A camada mais externa. Contém tudo o que é detalhe de implementação e framework: Controllers (REST/WebSocket), Mappers, Entidades de Banco de Dados, Repositories (Spring Data), Configurações, Adapters dos Gateways e testes unitários.

## 🛠️ Tecnologias e Infraestrutura

A infraestrutura local é totalmente gerenciada via **Docker Compose**, subindo os seguintes serviços:

* **Java 17 + Spring Boot 3** (Backend Core)
* **PostgreSQL 15** (Banco relacional gerido via Flyway para autenticação e relacionamentos)
* **DynamoDB Local** (Banco NoSQL focado em alta disponibilidade para histórico de mensagens)
* **Redis 7** (Cache e gerenciamento de sessões)
* **RabbitMQ 3** (Broker de mensageria para processamento assíncrono de eventos)
* **Docker & Docker Compose**

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
* [Docker](https://www.docker.com/) e Docker Compose instalados.
* [Java 17](https://adoptium.net/) e [Maven](https://maven.apache.org/) instalados.

### Passo 1: Subir a Infraestrutura
Na raiz do projeto, execute o comando abaixo para baixar as imagens e subir os bancos de dados, o Redis e o RabbitMQ:

```bash
docker-compose up -d --build
```

### Passo 2: Configurar Variáveis de Ambiente
O projeto utiliza o arquivo `application.properties`. As migrações do banco de dados (tabelas e schemas) serão executadas automaticamente pelo **Flyway** ao iniciar a aplicação. Exemplo da configuração base:

```properties
spring.application.name=SpringChat

# Security
api.security.token.secret=${JWT_SECRET:my-secret-key}

# Banco de Dados (Postgres)
spring.datasource.url=jdbc:postgresql://localhost:5432/chat_auth
spring.datasource.username=chatuser
spring.datasource.password=chatpass
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
spring.jpa.properties.hibernate.default_schema=chat_auth

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.schemas=chat_auth

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.enabled=true
springdoc.swagger-ui.tags-sorter=alpha
springdoc.info.title=Chat API
springdoc.info.description=This is the documentation of the chat api.
springdoc.info.version=1.0.0
```

### Passo 3: Compilar e Rodar
Execute os comandos do Maven para instalar as dependências e iniciar o servidor Spring Boot:

```bash
mvn clean install
mvn spring-boot:run
```

## 📚 Documentação da API

A API está documentada utilizando o Springdoc/Swagger. Com a aplicação rodando, acesse seu navegador para visualizar e testar os endpoints REST:

* **Swagger UI:** `http://localhost:8080/swagger-ui.html`

---
*Desenvolvido por Henry Lampoglio.*