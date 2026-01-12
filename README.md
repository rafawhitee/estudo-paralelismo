# Motivitae - Controle de Bens

## Requisitos
- Java 21
- Maven 3.9.x

## Tecnologias
- Spring Boot 3.5.3
- Bootstrap 5

## Icons do Bootstrap utilizados
https://icons.getbootstrap.com/

## Deploy em PROD
- Gerar o build local (.jar)
- Acessar o remoto da máquina
- Parar a aplicação
```bash
sudo systemctl stop controle-bens
```
- Gerar backup do banco de dados
```bash
./backup-database.sh
```
- Remover o .jar antigo dentro da pasta do remoto
- Copiar o novo .jar
- Subir novamente o servidor
```bash
sudo systemctl start controle-bens
```