**Ajuda Rápida**

- **Build (reprodutível):** use o wrapper do Maven em vez do `mvn` global: `./mvnw`.
- **JAVA_HOME:** aponte `JAVA_HOME` para um JDK 21 (ex.: `C:\Program Files\Java\jdk-21...`). O `./mvnw` respeita `JAVA_HOME`.
- **Compilar sem testes (rápido):** `./mvnw -DskipTests clean package`.
- **Executar testes unitários:** `./mvnw test` (usa H2 em memória).
- **Executar testes de integração:** `./mvnw -Pintegration-test verify` — requer o Postgres disponível (por `docker compose up -d`).
- **Docker Compose (execução rápida):**

  ```bash
  docker compose up -d --build
  docker compose ps
  docker compose down -v
  ```

- **Seeds / SQL:** usam-se arquivos por plataforma: `src/main/resources/data-h2.sql` (H2, testes unitários) e `src/main/resources/data-postgres.sql` (Postgres, integração). O DDL principal está em `src/main/resources/schema.sql`.
- **Scripts legacy:** a pasta `scripts/` contém conveniências antigas e pode ser removida se não for necessária.
- **Problemas comuns:**
  - Se o Docker não estiver rodando, abra o Docker Desktop antes de executar os testes de integração.
  - Para limpar dados antigos, rode: `docker compose down -v` e reconstrua com `docker compose build --no-cache`.
  - Se o Maven apresentar erros, limpe `target/` e recompile: `./mvnw -DskipTests clean package`.
- **Referências rápidas:**
  - `https://maven.apache.org/guides/index.html`
  - `https://docs.spring.io/spring-boot/3.5.7/maven-plugin`
  - `https://spring.io/guides/gs/rest-service/`

_Se preferir, eu posso remover o arquivo `HELP.md` após a consolidação._

# CursoSpring — Elas + Tech

**Projeto usado nas aulas de Spring Boot da Ada/Artemísia** com apoio da Caixa (programa **Elas + Tech**). Este repositório contém uma pequena API Spring Boot de exemplo (CRUD de usuários), com banco PostgreSQL e uma interface de administração (Adminer).

**Visão Geral**

- **App:** Spring Boot (Java 21 compatível)
- **Banco:** PostgreSQL (container)
- **Admin UI:** Adminer (container)
- **Build:** Maven (usando `./mvnw`) / imagem Docker via `Dockerfile`

**Arquivos importantes**

- `src/main/resources/schema.sql` — criação das tabelas
- `src/main/resources/data.sql` — seed inicial (users/posts)
- `docker-compose.yml` — orquestra `postgres`, `adminer` e `app`
- `Dockerfile` — imagem para rodar o JAR
- `pom.xml` — dependências e configuração do build

**Como rodar (recomendado: Docker Compose)**

1. Certifique-se que o Docker Desktop (ou daemon Docker) está rodando no seu computador.
2. Na raiz do projeto:

```bash
cd /localDoProjeto/CursoSpring
docker compose up -d --build
```

3. Verifique os containers e portas:

```bash
docker compose ps
```

4. Endpoints:

- API base: `http://localhost:8080`
- Listar usuários: `GET http://localhost:8080/usuarios`
- Adminer (UI do DB): `http://localhost:8081` (user: `adauser`, pass: `adapass`, database: `curso_spring`)

Se você alterou o mapeamento de portas, verifique `docker-compose.yml` para os valores atuais.

**Como rodar localmente (sem Docker)**

1. Tenha JDK 21 instalado e configurado (`JAVA_HOME` apontando para o JDK 21).
2. Build e run via Maven:

```bash
./mvnw -DskipTests clean package
java -jar -Dspring.profiles.active=postgres target/cursospring-0.0.1-SNAPSHOT.jar
```

Ou para desenvolvimento (hot reload limitado):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

**Credenciais do ambiente local (padrão `docker-compose.yml`)**

- DB Host: `db` (quando dentro do compose) ou `localhost` (quando acessar pelo host)
- DB Port: `5432`
- Username: `adauser`
- Password: `adapass`
- Database: `curso_spring`

**Perfis de banco**

- Para testes automáticos, o projeto usa H2 (memória).
- Para produção/dev, use o perfil `postgres`.

**Sobre a seed e idempotência**

O projeto usa arquivos de seed separados por plataforma (`data-h2.sql` para testes em memória e `data-postgres.sql` para Postgres). Para execução local com Docker Compose o `data-postgres.sql` é o utilizado.

**Testes / Postman**

- Ex.: `GET http://localhost:8080/usuarios`

**Dicas de troubleshooting que foram úteis pra mim**

- Se `docker` retornar erro de conexão no Windows, abra o Docker Desktop e aguarde até ele indicar que o daemon está rodando.
- Se você estiver rodando a app localmente e via Docker ao mesmo tempo, lembre-se que existem duas instâncias (pare a que não quer usar).

- Se estiver enfrentando problemas estranhos de banco, dados antigos ou erros de build, tente limpar o cache do Docker (volumes e imagens) e recompilar tudo:

  ```bash
  docker compose down -v
  docker compose build --no-cache
  docker compose up -d
  ```

  Isso remove dados antigos e garante que o ambiente está limpo e atualizado.

- Se o Maven estiver apresentando erros de build ou dependências, limpe o cache local do projeto (diretório `target`) antes de compilar:

  ```bash
  mvn clean
  ```

  Isso remove arquivos antigos de build e resolve muitos problemas de compilação.

**Créditos**

Este projeto foi parte das aulas dadas pela **Ada/Artemísia**, com apoio da **Caixa** no programa **Elas + Tech**. Um projeto criado para mulheres em STEM. ❤️

---

## Documentação e Testes da API

Para testar os endpoints da API, acesse o Swagger UI:

http://localhost:8080/docs

Lá você pode visualizar e testar todos os endpoints disponíveis do projeto, inclusive realizar requisições diretamente pela interface web.

### Executando testes automatizados

Use o wrapper do Maven para executar os testes; não é necessário usar scripts adicionais:

- Testes unitários (H2):

```bash
./mvnw test
```

- Testes de integração (Postgres):

```bash
./mvnw -Pintegration-test verify
```

Se precisar de relatório de cobertura, ative o profile `coverage`:

```bash
./mvnw -Pcoverage test
```
