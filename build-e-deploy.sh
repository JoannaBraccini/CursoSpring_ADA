#!/bin/bash


# Para rodar: bash build-e-deploy.sh
#
# A configuração do JAVA_HOME abaixo é necessária para garantir que o Maven utilize o JDK 21 correto
# quando o script é executado via terminal/bash, especialmente fora de IDEs.
# Caso você utilize uma IDE como o IntelliJ, normalmente não é necessário configurar o JAVA_HOME manualmente,
# pois a IDE já gerencia a versão do Java utilizada no build.
#
# Este export é útil principalmente para quem NÃO tem o JAVA_HOME já configurado para o JDK 21,
# como é o caso do meu ambiente (em 2025). Se o seu JAVA_HOME já aponta para o JDK 21, pode remover ou comentar estas linhas.
export JAVA_HOME="/c/Program Files/Java/Eclipse Adoptium/jdk-21.0.9.10-hotspot"
export PATH="$JAVA_HOME/bin:$PATH"

echo "Compilando com Maven (JDK 21)..."
mvn clean package -DskipTests

echo "Derrubando containers antigos..."
docker-compose down -v

echo "Subindo containers e reconstruindo app..."
docker-compose up -d --build

echo "Pronto! App e banco estão rodando."