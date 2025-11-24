#!/usr/bin/env bash
set -euo pipefail

# Wrapper para executar os testes de integração (Failsafe + Testcontainers).
# Requer Docker ativo na máquina para que Testcontainers possa iniciar containers.
# Uso: ./scripts/tests/run-integration-tests.sh

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo "Executando testes de integração (profile: integration-test)"

# Verifica se o Docker está acessível (Testcontainers precisa do daemon)
if ! docker info >/dev/null 2>&1; then
  echo "AVISO: Docker não parece estar acessível. Testcontainers pode falhar." >&2
  echo "Tente 'docker desktop' ou garantir que o daemon Docker esteja rodando." >&2
fi

# Executa o Maven com o profile integration-test (Failsafe irá executar *IT.java)
exec "$PROJECT_ROOT/mvnw" -Pintegration-test verify "$@"
