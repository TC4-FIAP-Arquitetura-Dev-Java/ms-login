
# 🛡️ MS-Login - Microserviço de Autenticação com JWT

O **MS-Login** é responsável por autenticação, geração e renovação de tokens JWT. Ele permite registrar usuários, realizar login, refresh de tokens e logout.
Este microserviço **depende diretamente do MS-Usuário**, pois não possui banco interno para armazenar usuários.

---

## 📌 Requisitos para funcionamento

✔ **Java 21+**  
✔ **Spring Boot 3+**  
✔ **MS-Usuario em execução obrigatoriamente**  
✔ Comunicação via **Feign Client** para consultar e registrar usuários  

> ⚠ O MS-Login **não funciona sozinho**.  
> Todas as operações que envolvem usuário dependem do **MS-Usuario rodando simultaneamente**.

Para que o MS-Login possa validar credenciais e criar usuários, é necessário configurar a URL do serviço de usuários no arquivo `application.properties`.

Adicione a seguinte propriedade:

```
usuario.service.url=http://localhost:9083/ms-usuario/
```

Essa configuração define o endereço base usado internamente para comunicação entre os microsserviços. 
O MS-Login utiliza Feign Client para consumir endpoints do MS-Usuário de maneira automática e transparente, 
permitindo operações como:

- Registro de novos usuários
- Consulta de credenciais no momento do login
- Suporte ao fluxo de autenticação e emissão de tokens

Não é necessário implementar chamadas HTTP manualmente — o Feign gerencia todo o processo com base na URL configurada.

> **Observação:** Certifique-se de que o MS-Usuário esteja em execução antes de iniciar o MS-Login.

---

## 🚀 Endpoints Principais

| Método | Endpoint      | Função |
|-------|---------------|--------|
| POST  | `/register`   | Registrar novo usuário |
| POST  | `/login`      | Autenticar e gerar tokens JWT |
| POST  | `/refresh`    | Renovar tokens usando refreshToken |
| POST  | `/logout`     | Invalidar o token atual |

---

## 1️⃣ Registro de Usuário  
### `POST /register`

Cria um usuário no **MS-Usuário** via Feign.

**Request Body:**
```json
{
  "username": "joao.silva",
  "password": "$enh@2025",
  "role": "USER"
}
```

**Response 201:**
```json
{
  "message": "Usuário registrado com sucesso.",
  "userId": "652ff3a9b1c2d40012ab45de",
  "username": "joao.silva",
  "createdAt": "2025-10-22T20:30:00Z"
}
```

---

## 2️⃣ Login  
### `POST /login`

Valida credenciais e retorna **accessToken** + **refreshToken**.

**Request Body:**
```json
{
  "username": "joao.silva",
  "password": "$enh@2025"
}
```

**Response 200:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1Ni...",
  "refreshToken": "eyJhbGciOiJIUzI1Ni...",
  "username": "joao.silva",
  "expiresAt": "2025-10-22T23:59:59Z",
  "userId": "652ff3a9b1c2d40012ab45de"
}
```

---

## 3️⃣ Refresh Token  
### `POST /refresh`

Gera novos tokens a partir de um **refreshToken válido**.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refreshToken..."
}
```

**Response 200:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1Ni...",
  "refreshToken": "eyJhbGciOiJIUzI1Ni...",
  "expiresAt": "2025-10-22T23:59:59Z"
}
```

---

## 4️⃣ Logout  
### `POST /logout`

Invalida o token atual.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.accessToken..."
}
```

**Response 200:**
```json
{
  "message": "Token invalidado com sucesso.",
  "timestamp": "2025-10-22T20:30:00Z"
}
```

---

## ❌ Tratamento de Erros

| Código | Motivo |
|-------|--------|
| 400   | Requisição inválida |
| 401   | Usuário ou token inválido |
| 403   | Sem permissão |
| 409   | Usuário já existente |
| 500   | Falha no MS-Usuário ou servidor |

Exemplo de erro:
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciais inválidas.",
  "path": "/login",
  "timestamp": "2025-10-11T10:30:00Z"
}
```

---

## 🔥 Como rodar o projeto

1. Start **MS-Usuario** → Porta recomendada: `9082`
2. Configure no MS-Login a URL Feign
3. Start **MS-Login**
4. Teste via Swagger em:  
👉 `/swagger-ui.html`
