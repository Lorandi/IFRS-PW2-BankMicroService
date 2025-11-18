# 🏦 IFRS Bank — Sistema de Autenticação e Contas (Quarkus)

Este projeto implementa um sistema bancário completo, utilizando arquitetura de microserviços com Quarkus e um front-end Angular.
Inclui autenticação JWT, autorização baseada em roles, auditoria de operações, orquestração de serviços e interface web moderna.
---

## 📁 Estrutura do Projeto

```
/auth-service         → Serviço de autenticação (login, criação de usuário, troca de senha)
/account-service      → Serviço de contas bancárias, transações e auditoria
/orch-service         → Orquestrador, expõe endpoints unificados ao front, chama outros serviços
/front                → Interface Angular que consome apenas o orch-service
```

* ✔ Cada microserviço é isolado
* ✔ Comunicação síncrona via REST
* ✔ JWT assinado no auth-service e validado pelos demais
* ✔ Account deposita sem token (whitelist)
* ✔ Auditoria registrada  exposta ao orchestrator 


---

## 🧩 auth-service

Serviço responsável por **usuários e autenticação**, incluindo:

- Cadastro de usuários
- Login com geração de **JWT (RSA 2048)**
- Troca de senha autenticada
- Roles: `CUSTOMER`, `ADMIN`, `AUDITOR`
- Validação com Bean Validation
- Tratamento consistente de erros

Exemplo de token emitido:

```json
{
  "iss": "users-issuer",
  "userId": 2001,
  "username": "rodrigo",
  "groups": ["CUSTOMER"]
}
```

### Endpoints principais

POST /api/v1/users/login

POST /api/v1/users

PATCH /api/v1/users/change-password

---

## 🏦 account-service

Gerencia toda a lógica de conta bancária, incluindo:

- Criação de conta

- Consultar saldo

- Saques

- Transferências

- Depósitos (sem autenticação — whitelisted)

- Auditoria de operações

### Endpoints 
POST /api/v1/accounts

GET /api/v1/accounts/owner

PATCH /api/v1/accounts/{accountId}/deposit

PATCH /api/v1/accounts/{accountId}/withdraw

PATCH /api/v1/accounts/{accountId}/transfer

---

## 🔗 orch-service (Orquestrador / Gateway)

É o único backend acessado pelo front.

#### Responsabilidades:

- Encaminha requisições para auth/account
- Injeta automaticamente o JWT nas chamadas
- Centraliza erros e padroniza respostas
- Aplica regras de negócio globais
- Faz retry quando serviços estão offline
- Não possui banco de dados próprio

### Endpoints  expostos ao front
### Usuário

POST /orch/users/login

POST /orch/users

PATCH /orch/users/change-password

Conta (cliente)

POST /orch/accounts/customer

GET /orch/accounts/customer/owner

PATCH /orch/accounts/customer/{id}/withdraw

PATCH /orch/accounts/customer/{id}/transfer

Conta (admin)

GET /orch/accounts/admin

PATCH /orch/accounts/admin/{id}/account-status-toggle

### Auditoria

GET /orch/accounts/audit

GET /orch/accounts/audit/recent

GET /orch/accounts/audit/owner/{id}

### Depósitos

PATCH /orch/accounts/deposit/{id}/deposit?amount=100

### Padronização de Erros

O ErrorHandler converte erros em:
````
{
"status": 400,
"message": "CPF já cadastrado"
}
````

---

## 🌐 front (Angular)

### Aplicação Angular moderna com:

- Login e cadastro
- Minha conta
- Depósito sem login
- Saque
- Transferência
- Alteração de senha
- Tela administrativa
- Tela de auditoria
- Dashboard
- Loaders nos botões
- JWT armazenado no localStorage
- Interceptor para incluir token automaticamente

### Principais rotas

/login

/dashboard

/deposit

/admin

/audit

/password

## ⚙️ Tecnologias

### Backend

* **Java 21+**
* **Quarkus 3.x**
* **SmallRye JWT** (para autenticação)
* **Hibernate ORM + Panache**
* **Banco H2 (em memória)**
* **BCrypt** (para hashing de senhas)
* **Jakarta REST (JAX-RS)**
* **Log e Auditoria via Quarkus Logging**

### Frontend
* **Angular 17**
* **Angular Router**
* **CSS moderno** 
* **Componentização por features**

---

## 🔐 Autenticação JWT

O serviço `auth-service` é responsável por gerar o **token JWT** assinado com uma chave privada.
Os demais serviços (ex: `account-service`) validam o token com a **chave pública** correspondente.

### Geração das chaves
No auth-service:

```
openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in privateKey.pem -out publicKey.pem
```


Copiar publicKey.pem para:

account-service

orch-service

---

## ▶️ Como Executar

#### 1. Iniciar auth-service
```
   cd auth-service
   mvn quarkus:dev
```

#### 2. Iniciar account-service
```
   cd account-service
   mvn quarkus:dev
```

#### 3. Iniciar orch-service
```
   cd orch
   mvn quarkus:dev
```

#### 4. Iniciar front (Angular)
```
   cd front_end/banksim-frontend
   npm install
   ng serve 
```

### 📌 URLs Importantes
#### Swagger

* Auth: http://localhost:8081/q/swagger-ui

* Accounts: http://localhost:8082/q/swagger-ui

* Orch: http://localhost:8080/q/swagger-ui


### 3️⃣ Acessar via navegador

* [http://localhost:8081/q/swagger-ui](http://localhost:8081/q/swagger-ui) → auth-service
* [http://localhost:8082/q/swagger-ui](http://localhost:8082/q/swagger-ui) → account-service
* [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui) → orch
* [http://localhost:4200](http://localhost:4200) → front



---

## 🦵 Auditoria

Cada operação sensível (login, troca de senha, transação, etc.) gera logs como:

```
AUDIT | LOGIN | userId=1 | result=SUCCESS
AUDIT | CHANGE_PASSWORD | userId=1 | result=FAILURE | msg=Senha antiga incorreta
```


---

## 🚀 Debug

Para rodar o Quarkus em modo **debug**, use:

```bash
mvn quarkus:dev -Ddebug
```

E conecte no IntelliJ com o host `localhost:5005`.

---


## 👨‍💻 Autor

**Rodrigo Lorandi**

**Backend Developer** 

> Projeto desenvolvido como parte da disciplina de Programção Web II (PW2) em 11/2025
