# CursoSpring — Elas + Tech

**Projeto de exemplo usado na super aula da Ada/Artemísia** com apoio da Caixa (programa **Elas + Tech**). Este repositório contém uma pequena API Spring Boot de exemplo (CRUD de usuários), com banco PostgreSQL e uma interface de administração (Adminer).

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

1. Tenha JDK 21 instalado e configurado (`JAVA_HOME`).
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

O arquivo `data.sql` contém INSERTs usados para popular o banco em ambiente de desenvolvimento. Se você reiniciar a aplicação sem limpar as tabelas, pode ocorrer erro de conflito de chave única (UUID). Opções:

- Para desenvolvimento rápido, truncar tabelas:

```bash
docker exec -it postgres_container psql -U adauser -d curso_spring -c "TRUNCATE TABLE postagens, usuarios RESTART IDENTITY CASCADE;"
```

- Alternativamente, tornar os INSERTs idempotentes usando `ON CONFLICT DO NOTHING` no `data.sql`.

**Testes / Postman**

- Ex.: `GET http://localhost:8080/usuarios`

**Dicas de troubleshooting**

- Se `docker` retornar erro de conexão no Windows, abra o Docker Desktop e aguarde até ele indicar que o daemon está rodando.
- Se você estiver rodando a app localmente e via Docker ao mesmo tempo, lembre-se que existem duas instâncias (pare a que não quer usar).

**Créditos**

Este projeto foi preparado para a super aula da **Ada/Artemísia**, com apoio da **Caixa** — programa **Elas + Tech**. Obrigada por participar — é um espaço criado por e para mulheres aprendendo tecnologia. <3

---

Se quiser que eu adicione um `Makefile`, um `docker-compose.override.yml` para dev, ou gere o Postman collection, me avisa que eu implemento.
