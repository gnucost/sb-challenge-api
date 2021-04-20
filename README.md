# Social Bank Challenge API

## Como subir uma imagem docker dessa aplicação

A partir da pasta da aplicação, você pode (escolha uma opção):

1. Usar o comando: <br>`./gradlew build` para gerar um .jar e depois:
<br>`sudo docker build --build-arg JAR_FILE=build/libs/\*.jar -t springio/sb-challenge-api .`<br> para gerar uma imagem docker;

2. Usar o comando simplificado:<br>
`sudo ./gradlew bootBuildImage --imageName=springio/sb-challenge-api`<br>para gerar uma imagem docker;

Para executar a imagem docker basta usar o comando:<br>
`sudo docker run -p 8080:8080 springio/sb-challenge-api`

## Como testar esta aplicação usando SwaggerUI

Com uma imagem docker rodando (ou simplesmente a aplicação rodando localmente), basta acessar:<br>
<http://localhost:8080/swagger-ui.html>