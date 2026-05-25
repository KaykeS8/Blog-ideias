# 📝 Blog API — Spring Boot

Projeto de aprendizado desenvolvido para praticar os fundamentos do Spring Boot com uma API REST completa de blog. O foco foi construir uma base sólida com boas práticas desde o início.

## 🎯 Objetivo

Projeto pessoal de estudo com o intuito de consolidar conhecimentos em:
- Arquitetura em camadas com Spring Boot
- Persistência de dados com Spring Data JPA
- Boas práticas de API REST
- Testes unitários com JUnit 5 e Mockito

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA** + Hibernate
- **PostgreSQL**
- **Lombok**
- **JUnit 5** + **Mockito** (testes)
- **Maven**

---

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas com separação clara de responsabilidades:

```
src/
└── main/java/com/simao/blog/
    ├── controller/     # Recebe e responde requisições HTTP
    ├── service/        # Lógica de negócio
    ├── repository/     # Acesso ao banco de dados
    ├── model/          # Entidades JPA
    ├── dto/            # Objetos de transferência de dados
    │   ├── PostRequestDto
    │   ├── PostResponseDto
    │   └── PostPaginationDto
    ├── mapper/         # Conversão entre entidades e DTOs
    └── exceptions/     # Exceções customizadas e handler global
```

---

## 📌 Endpoints

### Posts

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/posts` | Lista todos os posts (paginado) |
| `GET` | `/posts?title=spring` | Filtra posts pelo título |
| `GET` | `/posts/{id}` | Busca post por ID |
| `POST` | `/posts` | Cria um novo post |
| `PUT` | `/posts/{id}` | Atualiza um post existente |
| `DELETE` | `/posts/{id}` | Remove um post |

### Paginação e ordenação

```
GET /posts?page=0&size=10&sort=createdAt,desc
```

---

## 📦 Como rodar

**Pré-requisitos:** Java 17+, Maven e PostgreSQL

Configure as variáveis de ambiente ou edite o `application.properties` com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/blog-api.git
cd blog-api

# Rodar a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 🧪 Testes

Foram implementados testes unitários para a camada de **Service** com JUnit 5 e Mockito, cobrindo os cenários de sucesso e de erro de cada operação.

```bash
# Rodar todos os testes
./mvnw test
```

**Cobertura dos testes:**
- `createPost` — criação e persistência correta
- `getAll` — paginação com e sem dados
- `getPostById` — sucesso e lançamento de exceção quando não encontrado
- `findPostByTitle` — busca parcial case-insensitive (`ContainingIgnoreCase`), sucesso e exceção quando vazio
- `updatePost` — atualização total, parcial (campos nulos) e ID inexistente
- `deletePost` — deleção com sucesso e ID inexistente

---

## 💡 O que aprendi

- Como estruturar uma API REST em camadas com Spring Boot
- Uso de `record` do Java para DTOs imutáveis
- Paginação com `Pageable` e resposta customizada com `PostPaginationDto<T>`
- Tratamento centralizado de erros com `@RestControllerAdvice`
- Diferença entre `@Mock` e `@MockBean`, e quando usar cada um
- Padrão AAA (Arrange, Act, Assert) para escrever testes legíveis
- Como usar `assertThatThrownBy` para testar exceções com AssertJ
- Uso de `verify()` e `never()` para garantir que o código interage corretamente com suas dependências

---

## 📈 Próximos passos

- [ ] Testes da camada Controller com MockMvc
- [ ] Autenticação com Spring Security + JWT
- [ ] Documentação com Swagger/OpenAPI

---

## 📬 Exemplo de requisição

**Criar um post:**
```http
POST /posts
Content-Type: application/json

{
  "title": "Meu primeiro post",
  "content": "Conteúdo do post aqui."
}
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Meu primeiro post",
  "content": "Conteúdo do post aqui.",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```
