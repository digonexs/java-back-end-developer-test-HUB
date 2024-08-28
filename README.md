# Cupom System

![Cupom System](./diagrama-desafio.png)

## Descrição do Projeto:

O `Cupom System` é uma aplicação desenvolvida em Java 17 usando Spring Boot. O sistema processa cupons de compras, valida os dados dos produtos, grava as informações em um banco de dados PostgreSQL e utiliza RabbitMQ para integração e comunicação assíncrona com outros sistemas.

### Funcionalidades:

1. **Validação dos dados do cupom**:
    - Valida o código `code44` para garantir que ele possui 44 caracteres numéricos.
    - Valida o `companyDocument` para garantir que é um CNPJ válido.
    - Verifica se o `totalValue` corresponde à soma dos valores dos produtos.

2. **Validação dos produtos**:
    - Verifica se `ean` dos produtos são únicos.
    - Verifica se `unitaryPrice` dos produtos são positivos.
    - Verifica se `quantity` dos produtos não são negativos.

3. **Persistência dos dados**:
    - Salva os dados do cupom em um banco de dados PostgreSQL.

4. **Publicação de cupons válidos**:
    - Publica os cupons válidos em uma fila RabbitMQ.

5. **Listener para RabbitMQ**:
    - Recebe informações adicionais de compradores através do RabbitMQ e atualiza os cupons no banco de dados.

## Tecnologias Utilizadas:

- **Java 17**: Linguagem de programação principal.
- **Spring Boot**: Framework para construção de aplicações Java.
- **Maven**: Gerenciador de dependências e construção de projeto.
- **PostgreSQL**: Banco de dados relacional para armazenamento de dados de cupons.
- **RabbitMQ**: Sistema de mensageria para comunicação assíncrona.
- **Docker**: Containerização da aplicação e serviços.
- **Postman**: Ferramenta para testes de API.

## Pré-requisitos:

- Docker e Docker Compose instalados
- JDK 17 instalado
- Maven instalado
- Postman (para testes de API)

## Configuração e Execução:

### 1. Clonar o repositório

```bash
   git clone https://github.com/digonexs/java-back-end-developer-test-HUB.git
   
   cd java-back-end-developer-test-HUB
```
### 2. Configurar o Banco de Dados e RabbitMQ
- Certifique-se de que o Docker e Docker Compose estão instalados. A aplicação usa PostgreSQL e RabbitMQ, podem ser facilmente configurados usando o `docker-compose.yml` fornecido.

```bash
   docker-compose up -d
```
- Este comando iniciará o PostgreSQL na porta 5432 e o RabbitMQ nas portas 5672 e 15672 (interface de gerenciamento).

### 3. Construir e Executar a Aplicação
- Com os serviços de banco de dados e mensageria em execução, você pode construir e executar a aplicação.

- Você pode construir uma imagem Docker da aplicação e executá-la em um contêiner. O `Dockerfile` é utilizado para containerizar a aplicação:

- 3.1 Construa a imagem Docker: Navegue até a pasta raiz do projeto onde o `Dockerfile` está localizado e execute o seguinte comando para construir a imagem:
 
```bash
  docker build -t cupom-system .
```
- Este comando irá criar uma imagem Docker com o nome cupom-system usando o `Dockerfile` fornecido.
- 3.2 Execute o contêiner Docker: Após a construção da imagem, execute um contêiner a partir da imagem criada:

```bash
  docker run -p 8080:8080 cupom-system
```

- Este comando inicia um contêiner baseado na imagem cupom-system e mapeia a porta 8080 do contêiner para a porta 8080 do host, tornando a aplicação acessível em http://localhost:8080.
- 3.3 Verifique se a aplicação está rodando: Abra um navegador e vá para http://localhost:8080 ou use o Postman para interagir com a API da aplicação.

### 4. Testes
- Para executar os testes unitários, use o comando abaixo:

```bash
  ./mvnw test
```

### 5. Testes de API com Postman
- Uma coleção do Postman é fornecida para facilitar os testes da API. Importe a coleção no Postman e execute as requisições para testar a criação de cupons, consulta de cupons, criação de produtos, consulta de produtos e mais.

### Desenvolvido por: [Rodrigo](https://www.linkedin.com/in/rodrigocavalcantedebarros/)