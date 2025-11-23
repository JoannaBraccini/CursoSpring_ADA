#!/bin/bash
set -euo pipefail

# Uso:
#  ./run-tests.sh                -> executa todos os testes
#  ./run-tests.sh <FQCN>        -> executa apenas o teste especificado (ex.: tech.ada.java.cursospring.api.amizade.AmizadeServiceTest)
# Pode também sobrescrever JAVA_HOME por variável de ambiente antes de chamar o script:
#  JAVA_HOME="/c/Program Files/Java/..." ./run-tests.sh

## Força o uso do JDK 21 a menos que o usuário peça explicitamente para usar o Java do sistema.
# Para usar o Java do sistema, execute com: USE_SYSTEM_JAVA=true bash run-tests.sh
if [ "${USE_SYSTEM_JAVA:-false}" != "true" ]; then
  JAVA_HOME_DEFAULT="/c/Program Files/Java/Eclipse Adoptium/jdk-21.0.9.10-hotspot"
  JAVA_HOME="$JAVA_HOME_DEFAULT"
fi

export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"

TEST_CLASS="${1:-}"

# Perfil do Spring a ser usado durante os testes. Por padrão usamos o H2 (teste em memória),
# mas o desenvolvedor pode sobrescrever passando SPRING_TEST_PROFILE=postgres ./run-tests.sh
SPRING_TEST_PROFILE="${SPRING_TEST_PROFILE:-h2}"

# Ative cobertura definindo COVERAGE=true (ex.: COVERAGE=true bash run-tests.sh)
COVERAGE="${COVERAGE:-false}"

# Se COVERAGE=true, ativamos o profile maven 'coverage'
MAVEN_PROFILE_OPTS=""
if [ "$COVERAGE" = "true" ]; then
  MAVEN_PROFILE_OPTS="-Pcoverage"
fi

echo "Usando JAVA_HOME=$JAVA_HOME"

if [ -n "$TEST_CLASS" ]; then
  echo "Executando testes para: $TEST_CLASS"
  ./mvnw $MAVEN_PROFILE_OPTS -Dtest="$TEST_CLASS" -Dspring.profiles.active="$SPRING_TEST_PROFILE" test
else
  echo "Executando toda a suíte de testes"
  ./mvnw $MAVEN_PROFILE_OPTS -Dspring.profiles.active="$SPRING_TEST_PROFILE" test
fi

echo "Fim dos testes"
