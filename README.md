# ClothIn

Sistema para cadastro e gerenciamento de roupas destinadas à doação e reutilização, desenvolvido como parte da Atividade de Estudo Programado (AEP).

## Problema

O descarte de roupas que ainda estão em condições de uso contribui para o desperdício de recursos e reduz as possibilidades de reutilização dessas peças.

Além disso, a falta de uma forma organizada para registrar e gerenciar roupas destinadas à doação pode dificultar o controle desses itens.

O ClothIn surge como uma proposta inicial para auxiliar nesse gerenciamento, permitindo cadastrar, consultar, atualizar e excluir informações de roupas que podem ser destinadas à doação e reutilização.

Nesta primeira entrega, foi desenvolvida uma primeira versão funcional da solução, com foco no CRUD básico de roupas e na persistência dos dados utilizando um banco de dados NoSQL.

## ODS

O projeto está relacionado ao **ODS 12 – Consumo e Produção Responsáveis**.

O ODS 12 busca promover padrões de consumo e produção mais sustentáveis, incentivando o uso consciente dos recursos e a redução de desperdícios.

O ClothIn contribui com esse objetivo ao incentivar a reutilização de roupas e possibilitar o gerenciamento das peças destinadas à doação, ajudando a evitar que roupas em condições de uso sejam descartadas desnecessariamente.

## Objetivo

O objetivo desta primeira versão do ClothIn é disponibilizar uma aplicação funcional capaz de realizar o cadastro e o gerenciamento básico de roupas destinadas à doação.

A aplicação permite:

- Cadastrar roupas;
- Listar roupas cadastradas;
- Buscar uma roupa pelo ID;
- Atualizar uma roupa;
- Excluir uma roupa;
- Armazenar os dados utilizando MongoDB;
- Realizar as operações através de um menu no terminal;
- Disponibilizar uma API REST para as operações do CRUD.

## Funcionalidades

### Cadastro de roupas

Permite cadastrar uma nova roupa informando:

- Tipo;
- Tamanho;
- Cor;
- Estado de conservação.

### Listagem de roupas

Permite visualizar todas as roupas cadastradas no sistema.

### Busca por ID

Permite localizar uma roupa específica utilizando seu identificador.

### Atualização

Permite alterar os dados de uma roupa já cadastrada.

### Exclusão

Permite remover uma roupa cadastrada, solicitando confirmação antes da exclusão.

## Dados de uma roupa

Cada roupa possui os seguintes campos:

| Campo | Descrição |
|---|---|
| `id` | Identificador da roupa |
| `tipo` | Tipo da peça |
| `tamanho` | Tamanho da peça |
| `cor` | Cor da peça |
| `estado` | Estado de conservação |

Exemplo de documento armazenado no MongoDB:

```json
{
  "_id": "id-gerado-pelo-mongodb",
  "tipo": "Camiseta",
  "tamanho": "M",
  "cor": "Azul",
  "estado": "Bom"
}
```

## Tecnologias

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data MongoDB**
- **MongoDB**
- **Maven**
- **JUnit 5**
- **Mockito**
- **IntelliJ IDEA**
- **Git e GitHub**

## Arquitetura

O projeto utiliza uma organização em camadas, separando as responsabilidades de cada parte da aplicação.

O fluxo principal é:

```text
Menu / Requisição
       ↓
   Controller
       ↓
     Service
       ↓
   Repository
       ↓
    MongoDB
```

### Model

A classe `Roupa` representa os dados de uma roupa que será armazenada no MongoDB.

### Controller

A camada `Controller` recebe as requisições da API e direciona as operações para a camada de serviço.

### Service

A camada `Service` concentra as operações realizadas sobre as roupas, como criar, listar, buscar, atualizar e deletar.

### Repository

A camada `Repository` realiza a comunicação entre a aplicação e o MongoDB utilizando o `MongoRepository` do Spring Data MongoDB.

### Console

A aplicação possui um menu de interação pelo terminal que permite executar as operações do CRUD.

# Instruções básicas de execução

## 1. IntelliJ IDEA

### Requisitos

Para executar o projeto, é necessário utilizar:

- **Java 21**
- **Maven**
- **IntelliJ IDEA**
- **MongoDB**

A versão utilizada no projeto é o **Java 21**. Certifique-se de que o IntelliJ IDEA está utilizando essa versão para executar a aplicação.

Para verificar a versão do Java instalada no computador, execute no terminal:

```bash
java -version
```

O resultado deve indicar uma versão 21 do Java.

### Clonar o projeto

O código-fonte do ClothIn está disponível no GitHub.

Clone o repositório utilizando:

```bash
git clone https://github.com/SeitiHsm/AEP1_esoft6C.git
```

Depois de clonar, entre na pasta do projeto:

```bash
cd AEP1_esoft6C
```

A aplicação está localizada dentro da pasta:

```text
ClothIn
```

### Abrir no IntelliJ IDEA

Abra a pasta `ClothIn` utilizando o IntelliJ IDEA.

O projeto possui um arquivo `pom.xml`, portanto o IntelliJ deverá reconhecer automaticamente o projeto como um projeto Maven.

Após abrir o projeto:

1. Aguarde o IntelliJ carregar as dependências do Maven;
2. Verifique se o projeto está utilizando o **Java 21**;
3. Certifique-se de que o MongoDB está em execução;
4. Localize a classe principal:

```text
src/main/java/AEP/ClothIn/ClothInApplication.java
```

5. Abra a classe `ClothInApplication`;
6. Clique no botão de execução ▶ ao lado do método `main`;
7. Selecione **Run 'ClothInApplication'**.

Após a inicialização do Spring Boot, o menu do ClothIn será apresentado no terminal do IntelliJ.

O menu será semelhante a:

```text
================================
            CLOTHIN
   DOE ROUPAS PARA UM PROXIMO!
================================
1 - Criar roupa
2 - Listar roupas
3 - Buscar roupa por ID
4 - Atualizar roupa
5 - Deletar roupa
0 - Sair
================================
Escolha uma opção:
```

Também é possível executar a aplicação utilizando o Maven. Dentro da pasta `ClothIn`, execute:

```bash
mvn spring-boot:run
```

## 2. MongoDB

O ClothIn utiliza o **MongoDB** como banco de dados NoSQL.

Nesta primeira entrega, o projeto utiliza uma única coleção:

```text
roupas
```

Os documentos possuem uma estrutura simples e homogênea.

### Configuração utilizada pela aplicação

A configuração da conexão com o MongoDB está localizada no arquivo:

```text
src/main/resources/application.properties
```

A aplicação deve utilizar a instância do MongoDB configurada nesse arquivo.

### Preparando o MongoDB para executar o projeto

Caso o avaliador esteja executando o projeto em outro computador, será necessário possuir o MongoDB instalado e em execução.

Após iniciar o MongoDB, abra o MongoDB Compass e conecte-se à mesma instância configurada no `application.properties`.

A coleção utilizada pelo projeto é:

```text
roupas
```

O banco de dados utilizado pela aplicação será criado pelo MongoDB conforme os primeiros dados forem persistidos.

Não é necessário cadastrar os documentos manualmente antes de executar o projeto.

### Realizando o primeiro cadastro

Depois de iniciar o ClothIn pelo IntelliJ IDEA, selecione:

```text
1 - Criar roupa
```

Informe os dados solicitados.

Exemplo:

```text
Tipo: Camiseta
Tamanho: M
Cor: Azul
Estado: Bom
```

Após o cadastro, o sistema apresentará:

```text
Roupa cadastrada com sucesso!
ID gerado: ID_DA_ROUPA
```

O ID será gerado automaticamente pelo MongoDB.

### Conferindo o cadastro no MongoDB Compass

Depois de realizar o cadastro, abra o MongoDB Compass.

Acesse o banco utilizado pelo projeto e localize a coleção:

```text
roupas
```

Dentro da coleção, o documento cadastrado deverá aparecer.

Um documento poderá ser semelhante a:

```json
{
  "_id": "68xxxxxxxxxxxxxxxxxxxxxx",
  "tipo": "Camiseta",
  "tamanho": "M",
  "cor": "Azul",
  "estado": "Bom"
}
```

Os valores de `tipo`, `tamanho`, `cor` e `estado` deverão ser os mesmos informados durante o cadastro.

O campo `_id` será gerado automaticamente pelo MongoDB e deverá corresponder ao ID apresentado no terminal após o cadastro.

Dessa forma, o avaliador pode verificar diretamente no MongoDB se a aplicação realmente realizou a persistência do dado.

### Verificando as outras operações

Depois de realizar o primeiro cadastro, também é possível verificar as demais operações do CRUD.

Para listar os documentos cadastrados, selecione:

```text
2 - Listar roupas
```

Para buscar uma roupa específica:

```text
3 - Buscar roupa por ID
```

Para atualizar uma roupa:

```text
4 - Atualizar roupa
```

Para excluir uma roupa:

```text
5 - Deletar roupa
```

Após realizar uma atualização ou exclusão, o MongoDB Compass pode ser atualizado para verificar se a alteração foi refletida na coleção `roupas`.

## 3. Testes automatizados

O projeto possui testes automatizados utilizando **JUnit 5**, **Mockito** e recursos de teste do Spring Boot.

Os testes estão localizados em:

```text
src/test/java/AEP/ClothIn
```

Entre as classes de teste estão:

```text
RoupaControllerTest
RoupaServiceTest
```

Os testes verificam o comportamento das principais funcionalidades implementadas nas camadas de Controller e Service.

### Executando os testes pelo IntelliJ IDEA

Para executar os testes pelo IntelliJ IDEA:

1. Abra a pasta:

```text
src/test/java/AEP/ClothIn
```

2. Abra uma das classes de teste, por exemplo:

```text
RoupaServiceTest
```

3. Clique no botão ▶ ao lado da classe;
4. Selecione **Run 'RoupaServiceTest'**.

Também é possível executar um método de teste individual utilizando o botão ▶ ao lado do método.

Para executar todos os testes, é possível utilizar a opção de execução disponível na pasta de testes.

### Executando os testes pelo Maven

Outra forma de executar os testes é através do Maven.

Abra o terminal dentro da pasta `ClothIn` e execute:

```bash
mvn test
```

O Maven irá compilar o projeto e executar os testes automatizados.

Ao final da execução, o terminal deverá apresentar o resultado dos testes.

Quando todos os testes forem executados sem falhas, o Maven apresentará:

```text
BUILD SUCCESS
```

Caso algum teste apresente erro ou falha, o Maven indicará quais testes não foram aprovados.

### Exemplo de teste

Um dos testes pode verificar, por exemplo, se o cadastro de uma roupa chama corretamente o repositório para salvar o objeto.

O fluxo do teste é:

```text
Criar uma roupa
       ↓
Executar o método do Service
       ↓
Verificar a chamada do Repository
       ↓
Confirmar o resultado esperado
```

Os testes automatizados permitem verificar o comportamento das funcionalidades sem precisar realizar manualmente cada operação pelo menu.

## API REST

Além do menu de terminal, o projeto possui uma API REST para realizar as operações do CRUD.

Os endpoints disponíveis são:

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/roupas` | Cadastrar uma roupa |
| `GET` | `/roupas` | Listar todas as roupas |
| `GET` | `/roupas/{id}` | Buscar uma roupa pelo ID |
| `PUT` | `/roupas/{id}` | Atualizar uma roupa |
| `DELETE` | `/roupas/{id}` | Deletar uma roupa |

As requisições podem ser realizadas utilizando o Postman.

### Exemplo de CREATE pela API

```http
POST http://localhost:8080/roupas
Content-Type: application/json
```

Body:

```json
{
  "tipo": "Camiseta",
  "tamanho": "M",
  "cor": "Azul",
  "estado": "Bom"
}
```

Após realizar o `POST`, o documento poderá ser verificado no MongoDB Compass na coleção `roupas`.

## Estrutura do projeto

```text
ClothIn
├── src
│   ├── main
│   │   ├── java
│   │   │   └── AEP
│   │   │       └── ClothIn
│   │   │           ├── console
│   │   │           ├── controllers
│   │   │           ├── models
│   │   │           ├── repositories
│   │   │           └── services
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│       └── java
│           └── AEP
│               └── ClothIn
│                   ├── RoupaControllerTest
│                   └── RoupaServiceTest
│
├── pom.xml
└── README.md
```

## Escopo da primeira entrega

Esta versão representa a **primeira versão funcional do ClothIn**.

O foco desta entrega é apresentar:

- O problema escolhido;
- A relação do projeto com o ODS 12;
- Uma primeira versão funcional da solução;
- Utilização de Java e Spring Boot;
- Utilização efetiva de um banco de dados NoSQL;
- Aplicação de conceitos de programação orientada a objetos;
- Organização do código em camadas;
- CRUD de uma única coleção MongoDB;
- Persistência dos dados;
- Testes automatizados;
- Versionamento utilizando Git e GitHub.

Nesta primeira etapa, o projeto utiliza uma única coleção com documentos de estrutura simples e homogênea.

Funcionalidades mais avançadas poderão ser desenvolvidas nas próximas etapas do projeto.

## Versionamento

O projeto utiliza **Git** para controle de versão e **GitHub** para armazenamento do código-fonte.

As alterações realizadas durante o desenvolvimento são registradas por meio de commits, permitindo acompanhar a evolução do ClothIn.

## Situação atual

**Primeira versão funcional do ClothIn.**

O sistema possui o CRUD de roupas integrado ao MongoDB, menu de interação pelo terminal, API REST, organização inicial em camadas e testes automatizados.
