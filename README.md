# 🏦 IFRS Bank — Sistema de Autenticação e Contas (Quarkus)

Este projeto implementa uma arquitetura de **microserviços Java com Quarkus** para simular o funcionamento de um banco digital.
Inclui autenticação JWT, controle de acesso baseado em papéis (roles), auditoria de ações e endpoints RESTful para manipulação de usuários e contas.

---

## 📁 Estrutura do Projeto

```
/auth-service       → Serviço de autenticação e emissão de tokens JWT
/account-service    → Serviço de contas bancárias (consome e valida o JWT)
```

Cada serviço é independente, com seu próprio `pom.xml`, banco de dados H2 em memória e chaves de assinatura JWT.

---

## ⚙️ Tecnologias

* **Java 21+**
* **Quarkus 3.x**
* **SmallRye JWT** (para autenticação)
* **Hibernate ORM + Panache**
* **Banco H2 (em memória)**
* **BCrypt** (para hashing de senhas)
* **Jakarta REST (JAX-RS)**
* **Log e Auditoria via Quarkus Logging**

---

## 🔐 Autenticação JWT

O serviço `auth-service` é responsável por gerar o **token JWT** assinado com uma chave privada.
Os demais serviços (ex: `account-service`) validam o token com a **chave pública** correspondente.

### Geração das chaves

Execute os comandos abaixo no diretório `src/main/resources` do `auth-service`:

```bash
# Gera a chave privada
openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:2048

# Gera a chave pública correspondente
openssl rsa -pubout -in privateKey.pem -out publicKey.pem
```

Depois, copie o arquivo `publicKey.pem` para o diretório `src/main/resources` de **todos os outros serviços**.

---

## 🧩 auth-service

### Função

Gerencia usuários e autenticação.
Ao fazer login com `userId` e `password`, o serviço gera um **JWT** assinado contendo:

```json
{
  "iss": "ifrs-bank",
  "upn": "admin",
  "groups": ["ADMIN"],
  "userId": 1
}
```

### Exemplo de endpoint

#### 🔑 Login

```bash
curl -X POST http://localhost:8081/api/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "password": "a"}'
```

**Resposta:**

```json
{
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9...",
  "role": "ADMIN",
  "username": "admin"
}
```

#### 🔒 Troca de senha autenticada

```bash
curl -X PATCH http://localhost:8081/api/v1/users/change-password \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "oldPassword": "a",
    "newPassword": "b"
  }'
```

### Configuração (`application.properties`)

```properties
quarkus.http.port=8081

# Banco
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1
quarkus.datasource.username=sa
quarkus.datasource.password=sa
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.sql-load-script=import.sql

# JWT
smallrye.jwt.sign.key-location=privateKey.pem
smallrye.jwt.new-token.issuer=ifrs-bank
smallrye.jwt.new-token.lifespan=600
mp.jwt.verify.publickey.location=publicKey.pem
mp.jwt.verify.issuer=ifrs-bank
quarkus.smallrye-jwt.enabled=true
```

---

## 🗾 account-service

### Função

Serviço de contas bancárias e auditoria de transações.
Valida o token JWT gerado pelo `auth-service` e permite acesso a endpoints conforme a role do usuário.

### Exemplo de endpoint

#### 👤 Identificação do usuário logado

```bash
curl -X GET http://localhost:8082/api/v1/accounts/whoami \
  -H "Authorization: Bearer <TOKEN>"
```

**Resposta:**

```json
{
  "userId": 1,
  "username": "admin",
  "groups": ["ADMIN"]
}
```

### Configuração (`application.properties`)

```properties
quarkus.http.port=8082

# Banco
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:accountdb;DB_CLOSE_DELAY=-1
quarkus.datasource.username=sa
quarkus.datasource.password=sa
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.sql-load-script=import.sql

# JWT Validation
mp.jwt.verify.publickey.location=publicKey.pem
mp.jwt.verify.issuer=ifrs-bank
quarkus.smallrye-jwt.enabled=true
```

---

## 🧮 Como Rodar

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/Lorandi/IFRS-PW2-BankMicroService.git
```

### 2️⃣ Iniciar cada serviço

**Auth Service:**

```bash
cd auth-service
mvn quarkus:dev
```

**Account Service:**

```bash
cd account-service
mvn quarkus:dev
```

### 3️⃣ Acessar via navegador

* [http://localhost:8081/q/swagger-ui](http://localhost:8081/q/swagger-ui) → auth-service
* [http://localhost:8082/q/swagger-ui](http://localhost:8082/q/swagger-ui) → account-service

---

## 🥪 Testando o Fluxo Completo

1. **Fazer login no `auth-service`** e copiar o token JWT.
2. **Usar o token** no `account-service` para acessar endpoints protegidos.
3. **Roles diferentes** (`CUSTOMER`, `ADMIN`) controlam o acesso via `@RolesAllowed`.

---

## 🦵 Auditoria

Cada operação sensível (login, troca de senha, transação, etc.) gera logs como:

```
AUDIT | LOGIN | userId=1 | result=SUCCESS
AUDIT | CHANGE_PASSWORD | userId=1 | result=FAILURE | msg=Senha antiga incorreta
```

Os logs são armazenados no console e podem ser integrados com observabilidade (ex: Loki, ELK, etc.).

---

## 🚀 Debug

Para rodar o Quarkus em modo **debug**, use:

```bash
mvn quarkus:dev -Ddebug
```

E conecte no IntelliJ com o host `localhost:5005`.

---

## 🧱 Extensões Quarkus utilizadas

* `quarkus-resteasy-reactive`
* `quarkus-hibernate-orm-panache`
* `quarkus-smallrye-jwt`
* `quarkus-smallrye-jwt-build`
* `quarkus-smallrye-openapi`
* `quarkus-swagger-ui`
* `quarkus-arc`
* `quarkus-logging-json`

---

## 👨‍💻 Autor

**Rodrigo Lorandi**
Backend Developer & Researcher @ IFRS

> Projeto desenvolvido como parte da disciplina de Programção Web II (PW2)
