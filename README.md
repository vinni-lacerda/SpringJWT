# Spring Security + JWT Authentication API

API REST desenvolvida com **Java e Spring Boot** demonstrando autenticação e autorização utilizando **Spring Security** e **JSON Web Token (JWT)**.

O objetivo do projeto é demonstrar como implementar:

* Autenticação com **username e password**
* Geração de **JWT**
* Validação de token em requisições protegidas
* Controle de acesso por **roles**
* Persistência de usuários
* Proteção de endpoints com **Spring Security**

---

# Tecnologias Utilizadas

* Java 17+
* Spring Boot
* Spring Security
* JWT (JJWT)
* H2 Database
* Maven
* Postman (para testes de API)

---

# Arquitetura do Projeto

A estrutura do projeto segue uma separação comum em aplicações Spring Boot:

```
src/main/java
 └── study.spring_security_jwt
     ├── controller
     │     └── UserController
     │
     ├── service
     │     └── UserService
     │
     ├── repository
     │     └── UserRepository
     │
     ├── model
     │     └── User
     │
     └── security
           ├── SecurityConfig
           ├── JWTFilter
           ├── JWTCreator
           └── JWTObject
```

---

# Fluxo de Autenticação

O fluxo de autenticação da aplicação funciona da seguinte forma:

```
Cliente
   ↓
POST /login
   ↓
Spring Security autentica usuário
   ↓
JWT é gerado
   ↓
Token retornado no header Authorization
   ↓
Cliente envia token nas próximas requisições
   ↓
JWTFilter valida o token
   ↓
Usuário autenticado no SecurityContext
```

---

# Estrutura do Token JWT

O JWT gerado possui três partes:

```
header.payload.signature
```

Exemplo:

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2aW5pIn0.hK2kdf92jdf923...
```

Claims utilizadas:

| Claim       | Descrição          |
| ----------- | ------------------ |
| sub         | username           |
| iat         | data de criação    |
| exp         | expiração do token |
| authorities | roles do usuário   |

---

# Configuração de Segurança

A configuração principal é realizada na classe:

```
SecurityConfig
```

Responsável por:

* Definir política **stateless**
* Configurar endpoints públicos
* Registrar **JWTFilter**
* Gerenciar autenticação e autorização

Exemplo de configuração:

```java
.sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```

---

# JWT Filter

O filtro `JWTFilter` intercepta todas as requisições e verifica se existe um token JWT válido no header:

```
Authorization: Bearer <token>
```

Caso o token seja válido:

* As roles são extraídas
* O usuário é autenticado no `SecurityContext`

---

# Endpoints da API

## Criar Usuário

```
POST /users
```

Body:

```json
{
  "name": "Vini",
  "username": "vinn",
  "password": "jwt123",
  "roles": ["USERS","MANAGERS"]
}
```

---

## Login

```
POST /login
```

Resposta:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## Listar Usuários (protegido)

```
GET /users
```

Header obrigatório:

```
Authorization: Bearer <JWT_TOKEN>
```

Permissões:

```
ROLE_USERS
ROLE_MANAGERS
```

---

## Endpoint Managers

```
GET /managers
```

Permissão necessária:

```
ROLE_MANAGERS
```

---

# Testando com Postman

### 1. Criar usuário

```
POST http://localhost:8080/users
```

Body:

```json
{
 "name": "Vini",
 "username": "vinn",
 "password": "jwt123",
 "roles": ["USERS","MANAGERS"]
}
```

---

### 2. Fazer login

```
POST http://localhost:8080/login
```

Resposta:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

### 3. Usar token

```
GET http://localhost:8080/users
```

Header:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

# Banco de Dados

O projeto utiliza **H2 Database** para desenvolvimento.

Console disponível em:

```
http://localhost:8080/h2-console
```

Configuração padrão:

```
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (vazio)
```

---

# Propriedades de Segurança

Configuração no `application.properties`:

```
security.config.prefix=Bearer
security.config.key=SECRET_KEY
security.config.expiration=3600000
```

* prefix → prefixo do token
* key → chave secreta
* expiration → tempo de expiração do JWT

---

# Melhorias Futuras

Possíveis evoluções para o projeto:

* Refresh Token
* OAuth2 / OpenID Connect
* Persistência de tokens
* Rate limiting
* Integração com banco relacional (PostgreSQL)
* Documentação com Swagger / OpenAPI

---

# Objetivo Educacional

Este projeto foi desenvolvido com fins de estudo para compreender:

* Funcionamento do **Spring Security**
* Implementação de **JWT Authentication**
* Proteção de APIs REST
* Controle de acesso baseado em roles

---

# Autor

Vinicius Lacerda

Projeto desenvolvido para estudo de **Backend com Java e Spring Boot**.
