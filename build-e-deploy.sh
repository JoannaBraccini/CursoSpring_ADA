#!/bin/bash

# Para rodar: #!/bin/bash

export JAVA_HOME="/c/Program Files/Java/Eclipse Adoptium/jdk-21.0.9.10-hotspot"
export PATH="$JAVA_HOME/bin:$PATH"

echo "Compilando com Maven (JDK 21)..."
mvn clean package -DskipTests

echo "Derrubando containers antigos..."
docker-compose down -v

echo "Subindo containers e reconstruindo app..."
docker-compose up -d --build

echo "Pronto! App e banco estão rodando."