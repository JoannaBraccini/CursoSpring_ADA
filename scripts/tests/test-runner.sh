#!/usr/bin/env bash
set -euo pipefail

## Runner central de testes (renomeado para `test-runner.sh`).
## Mantém compatibilidade com as opções originais, mas resolve o caminho para o `mvnw`

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

## Força o uso do JDK 21 a menos que o usuário peça explicitamente para usar o Java do sistema.
if [ "${USE_SYSTEM_JAVA:-false}" != "true" ]; then
  JAVA_HOME_DEFAULT="/c/Program Files/Java/Eclipse Adoptium/jdk-21.0.9.10-hotspot"
  JAVA_HOME="$JAVA_HOME_DEFAULT"
fi

export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"

TEST_CLASS="${1:-}"

# Perfil do Spring a ser usado durante os testes. Por padrão usamos o H2 (teste em memória),
# mas o desenvolvedor pode sobrescrever passando SPRING_TEST_PROFILE=postgres ./scripts/tests/test-runner.sh
SPRING_TEST_PROFILE="${SPRING_TEST_PROFILE:-h2}"

COVERAGE="${COVERAGE:-false}"

MAVEN_PROFILE_OPTS=""
if [ "$COVERAGE" = "true" ]; then
  MAVEN_PROFILE_OPTS="-Pcoverage"
fi

echo "Usando JAVA_HOME=$JAVA_HOME"

if [ -n "$TEST_CLASS" ]; then
  echo "Executando testes para: $TEST_CLASS"
  if [ "$SPRING_TEST_PROFILE" = "postgres" ]; then
    echo "Profile 'postgres' selecionado: forçando datasource para localhost:5432"
    export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/curso_spring?sslmode=disable"
    export SPRING_DATASOURCE_USERNAME="adauser"
    export SPRING_DATASOURCE_PASSWORD="adapass"
    "$PROJECT_ROOT/mvnw" $MAVEN_PROFILE_OPTS -Dtest="$TEST_CLASS" -Dspring.profiles.active="$SPRING_TEST_PROFILE" -Dspring.datasource.url="$SPRING_DATASOURCE_URL" -Dspring.datasource.username="$SPRING_DATASOURCE_USERNAME" -Dspring.datasource.password="$SPRING_DATASOURCE_PASSWORD" test
  else
    "$PROJECT_ROOT/mvnw" $MAVEN_PROFILE_OPTS -Dtest="$TEST_CLASS" -Dspring.profiles.active="$SPRING_TEST_PROFILE" test
  fi
else
  echo "Executando toda a suíte de testes"
  if [ "$SPRING_TEST_PROFILE" = "postgres" ]; then
    echo "Profile 'postgres' selecionado: forçando datasource para localhost:5432"
    export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/curso_spring?sslmode=disable"
    export SPRING_DATASOURCE_USERNAME="adauser"
    export SPRING_DATASOURCE_PASSWORD="adapass"
    "$PROJECT_ROOT/mvnw" $MAVEN_PROFILE_OPTS -Dspring.profiles.active="$SPRING_TEST_PROFILE" -Dspring.datasource.url="$SPRING_DATASOURCE_URL" -Dspring.datasource.username="$SPRING_DATASOURCE_USERNAME" -Dspring.datasource.password="$SPRING_DATASOURCE_PASSWORD" test
  else
    "$PROJECT_ROOT/mvnw" $MAVEN_PROFILE_OPTS -Dspring.profiles.active="$SPRING_TEST_PROFILE" test
  fi
fi

echo "Fim dos testes"
