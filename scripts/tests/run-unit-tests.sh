#!/usr/bin/env bash
set -euo pipefail

# Wrapper para executar a suíte de testes unitários com H2 (memória).
# Força explicitamente o profile `h2` ao invocar `test-runner.sh`.
# Uso: ./scripts/tests/run-unit-tests.sh [<FQCN>]

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [ "$#" -gt 0 ]; then
  SPRING_TEST_PROFILE=h2 bash "$SCRIPT_DIR/test-runner.sh" "$@"
else
  SPRING_TEST_PROFILE=h2 bash "$SCRIPT_DIR/test-runner.sh"
fi

exit 0
