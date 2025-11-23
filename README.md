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

**Executando testes automatizados**

Existe um script conveniente na raiz `run-tests.sh` que garante o uso do JDK 21 (compatível com o projeto) e executa os testes via Maven Wrapper.

- Executar todos os testes:

```bash
bash run-tests.sh
```

- Executar apenas uma classe de teste (ex.: `AmizadeServiceTest`):

```bash
bash run-tests.sh tech.ada.java.cursospring.api.amizade.AmizadeServiceTest
```

O script força por padrão `JAVA_HOME` para o JDK 21 que este projeto espera. Se você quiser usar o Java do sistema (não recomendado aqui), execute com a variável de ambiente `USE_SYSTEM_JAVA=true`:

```bash
USE_SYSTEM_JAVA=true bash run-tests.sh
```

### Executando testes automatizados com ou sem cobertura

O script `run-tests.sh` permite executar os testes automatizados com ou sem cobertura de código. Por padrão, os testes são executados **sem cobertura**. Para ativar a cobertura de código, utilize a variável de ambiente `COVERAGE=true`.

- **Executar testes sem cobertura (padrão):**

```bash
bash run-tests.sh
```

- **Executar testes com cobertura:**

```bash
COVERAGE=true bash run-tests.sh
```

Ao ativar a cobertura, um relatório será gerado no diretório `target/site/jacoco/index.html`. Para visualizar o relatório, abra o arquivo HTML no navegador ou use uma extensão como "Live Server" no VS Code.

**VS Code (diferenças em relação ao IntelliJ)**

- **Resumo:** O IntelliJ possui um runner de cobertura integrado que facilita ver coverage localmente. No VS Code eu acrescentei o plugin JaCoCo ao `pom.xml` e um script `run-tests.sh` para garantir execução consistente com Java 21 e gerar um relatório HTML. Essas alterações facilitam gerar coverage fora do IntelliJ.
- **Passos principais que fiz aqui (VS Code):**
  - Atualizei/adicionei `org.jacoco:jacoco-maven-plugin` (versão compatível com Java 21).
  - Criei/uso o script `run-tests.sh` que força `JAVA_HOME` para JDK 21 e executa `./mvnw test` + gera o relatório JaCoCo.
  - Abra manualmente o relatório HTML em `target/site/jacoco/index.html` (no VS Code use a extensão "Live Server" ou "Open in Browser").
- **Comandos úteis (VS Code):**

```bash
# rodar todos os testes + gerar relatório JaCoCo
bash run-tests.sh

# alternativa direta via maven
./mvnw clean test jacoco:report
```

- **Observações:**
  - JaCoCo precisa de versão compatível com Java 21 (usei 0.8.11). Se você abrir o projeto no IntelliJ, o runner de coverage do IntelliJ dispensa essa configuração local, mas manter JaCoCo é útil para CI e para quem usa VS Code.
  - Pode aparecer o aviso do Mockito sobre "inline-mock-maker" — investiguei e preferi não adicionar `mockito-inline` automaticamente porque em alguns ambientes a dependência não foi resolvida; se quiser, posso documentar a alternativa de usar `mockito-inline` ou configurar o agente para CI.

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

---
