# Social Bank Challenge API

## Como subir uma imagem docker dessa aplicação

A partir da pasta da aplicação, você pode:

1. Usar o comando `./gradlew build` para gerar um .jar e depois `sudo docker build --build-arg JAR_FILE=build/libs/\*.jar -t springio/sb-challenge-api .` para gerar uma imagem docker;

2. Usar o comando simplificado `sudo ./gradlew bootBuildImage --imageName=springio/sb-challenge-api` para gerar uma imagem docker;

Para executar a imagem docker basta usar o comando `sudo docker run -p 8080:8080 springio/sb-challenge-api`

## Como testar esta aplicação usando SwaggerUI

Basta acessar <http://localhost:8080/swagger-ui.html>