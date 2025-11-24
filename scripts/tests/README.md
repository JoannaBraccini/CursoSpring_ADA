# Test scripts

Scripts para executar a suíte de testes de forma previsível. Para detalhes de implementação (variáveis, lógica e comentários) veja `scripts/tests/test-runner.sh`.

Principais arquivos

- `test-runner.sh` — runner central (aceita classe de teste, suporta `COVERAGE=true` e `SPRING_TEST_PROFILE`).
- `run-unit-tests.sh` — wrapper rápido que força `h2` (uso diário).
- `run-integration-tests.sh` — wrapper para integração (usa Testcontainers; requer Docker).

Uso comum (copiar/colar)

Rodar todos os testes (usa H2 por padrão):

```bash
bash scripts/tests/test-runner.sh
```

Rodar uma classe específica:

```bash
bash scripts/tests/test-runner.sh tech.ada.java.cursospring.api.amizade.AmizadeServiceTest
```

Ativar cobertura:

```bash
COVERAGE=true bash scripts/tests/test-runner.sh
```

Testes de integração (Docker necessário):

```bash
bash scripts/tests/run-integration-tests.sh
# ou
./mvnw -Pintegration-test verify
```

Observações rápidas

- Prefira `run-unit-tests.sh` para ciclos rápidos de desenvolvimento.
- Consulte `test-runner.sh` se precisar ajustar `JAVA_HOME`, profiles ou comportamento avançado.

Posso adicionar um exemplo de workflow (GitHub Actions) que execute os testes de integração em um runner com Docker, se quiser.
