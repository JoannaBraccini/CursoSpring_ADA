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

Ou, para automatizar todo o processo (compilar, limpar volumes e subir containers), use o script:

```bash
bash build-e-deploy.sh
```

> **ATENÇÃO:** Antes de rodar o script `build-e-deploy.sh`, leia os comentários no início do arquivo! Ele configura o JAVA_HOME para o JDK 21 manualmente, o que é necessário caso seu ambiente não esteja apontando para essa versão. Se você já tem o JAVA_HOME correto ou usa uma IDE como o IntelliJ, pode adaptar ou remover essa configuração.

Esse script já define o JAVA_HOME para o JDK 21, compila o projeto, remove volumes antigos e sobe os containers prontos para uso.

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

O arquivo `data.sql` contém INSERTs usados para popular o banco em ambiente de desenvolvimento.

- O script faz o build das tabelas.
- Os INSERTs são idempotentes, usando `ON CONFLICT DO NOTHING` no `data.sql`.

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

Este projeto foi parte das aulas dadas pela **Ada/Artemísia**, com apoio da **Caixa** no programa **Elas + Tech**. Um projeto criado para mulheres em STEM. <3

---

## Documentação e Testes da API

Para testar os endpoints da API, acesse o Swagger UI:

http://localhost:8080/docs

Lá você pode visualizar e testar todos os endpoints disponíveis do projeto, inclusive realizar requisições diretamente pela interface web.

### Executando testes automatizados com ou sem cobertura

O runner `test-runner.sh` permite executar os testes automatizados com ou sem cobertura de código. Por padrão, os testes são executados **sem cobertura**. Para ativar a cobertura de código, utilize a variável de ambiente `COVERAGE=true`.

- **Executar testes sem cobertura (padrão):**

```bash
bash scripts/tests/test-runner.sh
```

- **Executar testes com cobertura:**

```bash
COVERAGE=true bash scripts/tests/test-runner.sh
```

Ao ativar a cobertura, um relatório será gerado no diretório `target/site/jacoco/index.html`. Para visualizar o relatório, abra o arquivo HTML no navegador ou use uma extensão como "Live Server" no VS Code.

### Perfis de Teste

Por padrão, os testes de unidade do projeto usam H2 (banco em memória). Para facilitar o fluxo existem dois wrappers principais:

- `scripts/tests/run-unit-tests.sh`: roda os testes unitários com o profile `h2` (rápido, isolado).
- `scripts/tests/run-integration-tests.sh`: roda os testes de integração via Maven (profile `integration-test`) — usa Failsafe e, por padrão, Testcontainers irá inicializar um Postgres ephemeral.

O runner principal `test-runner.sh` ainda existe como utilitário, mas os wrappers acima cobrem os usos mais comuns. Exemplos:

- Executar testes unitários (H2) — recomendado para desenvolvimento local rápido:

  ```bash
  bash scripts/tests/run-unit-tests.sh
  # executar um teste específico
  bash scripts/tests/run-unit-tests.sh tech.ada.java.cursospring.api.amizade.AmizadeServiceTest
  ```

- Executar testes de integração (requires Docker):

  ```bash
  bash scripts/tests/run-integration-tests.sh
  # ou diretamente com Maven
  ./mvnw -Pintegration-test verify
  ```
