# ClothIn

Sistema para cadastro e gerenciamento de roupas destinadas à doação e reutilização, desenvolvido como parte da Atividade de Estudo Programado (AEP) de Engenharia de Software.

O **ClothIn** tem como objetivo facilitar o gerenciamento de doações de roupas, organizando os usuários envolvidos, as peças cadastradas e o acompanhamento das doações.

## 1. Problema

O descarte de roupas que ainda estão em condições de uso contribui para o desperdício de recursos e dificulta a reutilização desses itens.

Além disso, a falta de organização no processo de doação pode dificultar o controle das peças e o acompanhamento das pessoas envolvidas.

O ClothIn propõe uma solução para organizar esse processo por meio de uma aplicação que permite cadastrar usuários, registrar doações, incluir várias peças em uma mesma doação e acompanhar seus respectivos status.

## 2. ODS relacionado

O projeto está relacionado ao **ODS 12 – Consumo e Produção Responsáveis**.

Esse objetivo busca promover padrões de consumo e produção mais sustentáveis, incentivando o uso consciente dos recursos e a redução do desperdício.

O ClothIn contribui com essa proposta ao facilitar a organização e a reutilização de roupas destinadas à doação.

## 3. Objetivo do projeto

Desenvolver uma aplicação para auxiliar no gerenciamento de doações de roupas, permitindo:

- Cadastrar, consultar, atualizar e excluir usuários;
- Identificar usuários como doadores ou responsáveis;
- Registrar e gerenciar doações;
- Associar doadores e responsáveis às doações;
- Adicionar várias peças de roupa em uma mesma doação;
- Atualizar e consultar o status das doações;
- Filtrar doações por doador, responsável e status;
- Armazenar os dados utilizando MongoDB;
- Disponibilizar uma interface web para utilização do sistema.

## 4. Evolução da segunda entrega

Na primeira entrega, o foco do projeto foi desenvolver uma estrutura inicial para cadastro e gerenciamento de roupas, utilizando Java, Spring Boot e MongoDB.

Na segunda entrega, o ClothIn foi ampliado para oferecer um gerenciamento mais completo das doações.

As principais evoluções foram:

- Implementação do gerenciamento de usuários;
- Implementação do gerenciamento de doações;
- Relacionamento entre usuários e doações;
- Armazenamento de várias peças em uma mesma doação por meio de documentos incorporados;
- Ampliação da estrutura do banco de dados NoSQL;
- Desenvolvimento de uma interface web;
- Implementação de testes automatizados;
- Documentação técnica e instruções de execução.

## 5. Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data MongoDB
- MongoDB
- HTML
- CSS
- JavaScript
- Maven
- JUnit 5
- Mockito
- JaCoCo
- IntelliJ IDEA
- Git e GitHub

## 6. Arquitetura do projeto

O projeto utiliza uma arquitetura organizada em camadas, separando as responsabilidades da aplicação.

```text
Interface Web / Requisição HTTP
              |
              v
          Controller
              |
              v
           Service
              |
              v
         Repository
              |
              v
            MongoDB
```

### Model

Representa as entidades e estruturas de dados utilizadas pela aplicação.

Principais modelos:

- `Usuario`: representa os usuários cadastrados;
- `Doacao`: representa uma doação;
- `RoupaDoacao`: representa uma peça de roupa incorporada dentro de uma doação.

### Controller

Recebe as requisições HTTP e direciona as operações para a camada de serviço.

### Service

Concentra as regras de negócio, validações e operações da aplicação.

### Repository

Realiza a comunicação com o MongoDB por meio do Spring Data MongoDB.

### Interface Web

Permite utilizar as funcionalidades do sistema através de páginas HTML, CSS e JavaScript.

## 7. Banco de dados MongoDB

O ClothIn utiliza o MongoDB como banco de dados NoSQL.

**Nome do banco de dados:**

```text
ClothIn
```

**Endereço local padrão:**

```text
mongodb://localhost:27017/ClothIn
```

O projeto utiliza duas coleções principais:

```text
ClothIn
├── usuarios
└── doacoes
```

### 7.1. Coleção `usuarios`

Armazena os usuários cadastrados no sistema.

| Campo | Tipo | Descrição |
|---|---|---|
| `_id` | ObjectId | Identificador do usuário |
| `nome` | String | Nome do usuário |
| `email` | String | E-mail |
| `telefone` | String | Telefone |
| `tipoUsuario` | String | Tipo do usuário |

Tipos disponíveis:

- `DOADOR`: usuário que realiza doações;
- `RESPONSAVEL`: usuário responsável pelo acompanhamento das doações.

Exemplo de documento:

```javascript
{
  _id: ObjectId("6b1000000000000000000001"),
  nome: "Ana Martins",
  email: "ana.martins@example.com",
  telefone: "44990001001",
  tipoUsuario: "DOADOR",
  _class: "AEP.ClothIn.models.Usuario"
}
```

### 7.2. Coleção `doacoes`

Armazena as doações registradas no sistema.

| Campo | Tipo | Descrição |
|---|---|---|
| `_id` | ObjectId | Identificador da doação |
| `doadorId` | String | Identificador do usuário doador |
| `responsavelId` | String | Identificador do responsável |
| `data` | Date | Data da doação |
| `status` | String | Situação atual da doação |
| `roupas` | Array | Lista de peças incorporadas |

Status disponíveis:

- `PENDENTE`
- `RECEBIDA`
- `EM_TRIAGEM`
- `DISPONIVEL`
- `CONCLUIDA`

Cada objeto dentro de `roupas` possui:

| Campo | Tipo |
|---|---|
| `tipo` | String |
| `tamanho` | String |
| `cor` | String |
| `estado` | String |

Exemplo de documento:

```javascript
{
  _id: ObjectId("6b2000000000000000000001"),
  doadorId: "6b1000000000000000000001",
  responsavelId: "6b1000000000000000000002",
  data: ISODate("2026-11-01T12:00:00.000Z"),
  status: "RECEBIDA",
  roupas: [
    {
      tipo: "Camiseta",
      tamanho: "M",
      cor: "Azul",
      estado: "Bom"
    },
    {
      tipo: "Calça",
      tamanho: "40",
      cor: "Preta",
      estado: "Ótimo"
    }
  ],
  _class: "AEP.ClothIn.models.Doacao"
}
```

### 7.3. Relacionamento entre as coleções

O relacionamento entre usuários e doações é realizado por meio dos identificadores:

- Um usuário do tipo `DOADOR` pode estar associado a várias doações;
- Cada doação possui um doador identificado por `doadorId`;
- Uma doação pode possuir um responsável identificado por `responsavelId`;
- Uma doação pode conter várias peças de roupa.

As roupas são armazenadas diretamente dentro do documento da doação, utilizando documentos incorporados (*embedded documents*).

Assim, não é necessária uma terceira coleção para armazenar individualmente cada peça.

### 7.4. Preparação do MongoDB

Para executar a aplicação, é necessário possuir o MongoDB instalado e em execução localmente.

O banco `ClothIn` e suas coleções podem ser criados automaticamente durante a primeira inserção de documentos.

O avaliador pode utilizar o MongoDB Compass ou o `mongosh` para inserir os dados de demonstração.

### 7.5. Dados fictícios para demonstração

Os comandos abaixo criam um conjunto de dados fictícios para facilitar a avaliação do sistema.

Eles inserem 10 usuários e 10 doações, com diferentes status e quantidades de roupas.

Abra o `mongosh` e selecione o banco:

```javascript
use ClothIn
```

**Inserir usuários:**

```javascript
db.usuarios.insertMany([
  {
    _id: ObjectId("6b1000000000000000000001"),
    nome: "Ana Martins",
    email: "ana.martins@example.com",
    telefone: "44990001001",
    tipoUsuario: "DOADOR",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000002"),
    nome: "Bruno Costa",
    email: "bruno.costa@example.com",
    telefone: "44990001002",
    tipoUsuario: "RESPONSAVEL",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000003"),
    nome: "Carla Souza",
    email: "carla.souza@example.com",
    telefone: "44990001003",
    tipoUsuario: "DOADOR",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000004"),
    nome: "Diego Almeida",
    email: "diego.almeida@example.com",
    telefone: "44990001004",
    tipoUsuario: "DOADOR",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000005"),
    nome: "Elisa Ferreira",
    email: "elisa.ferreira@example.com",
    telefone: "44990001005",
    tipoUsuario: "RESPONSAVEL",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000006"),
    nome: "Felipe Rocha",
    email: "felipe.rocha@example.com",
    telefone: "44990001006",
    tipoUsuario: "DOADOR",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000007"),
    nome: "Gabriela Lima",
    email: "gabriela.lima@example.com",
    telefone: "44990001007",
    tipoUsuario: "DOADOR",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000008"),
    nome: "Heitor Nunes",
    email: "heitor.nunes@example.com",
    telefone: "44990001008",
    tipoUsuario: "RESPONSAVEL",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000009"),
    nome: "Isabela Mendes",
    email: "isabela.mendes@example.com",
    telefone: "44990001009",
    tipoUsuario: "DOADOR",
    _class: "AEP.ClothIn.models.Usuario"
  },
  {
    _id: ObjectId("6b1000000000000000000010"),
    nome: "João Pedro Alves",
    email: "joao.alves@example.com",
    telefone: "44990001010",
    tipoUsuario: "RESPONSAVEL",
    _class: "AEP.ClothIn.models.Usuario"
  }
]);
```

**Inserir doações:**

```javascript
db.doacoes.insertMany([
  {
    _id: ObjectId("6b2000000000000000000001"),
    doadorId: "6b1000000000000000000001",
    responsavelId: "6b1000000000000000000002",
    data: ISODate("2026-11-01T12:00:00.000Z"),
    status: "RECEBIDA",
    roupas: [
      { tipo: "Camiseta", tamanho: "M", cor: "Azul", estado: "Bom" },
      { tipo: "Calça", tamanho: "40", cor: "Preta", estado: "Ótimo" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000002"),
    doadorId: "6b1000000000000000000003",
    responsavelId: "6b1000000000000000000005",
    data: ISODate("2026-11-02T12:00:00.000Z"),
    status: "EM_TRIAGEM",
    roupas: [
      { tipo: "Jaqueta", tamanho: "G", cor: "Cinza", estado: "Bom" },
      { tipo: "Blusa", tamanho: "P", cor: "Rosa", estado: "Bom" },
      { tipo: "Cachecol", tamanho: "Único", cor: "Vermelho", estado: "Ótimo" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000003"),
    doadorId: "6b1000000000000000000004",
    responsavelId: "6b1000000000000000000002",
    data: ISODate("2026-11-03T12:00:00.000Z"),
    status: "PENDENTE",
    roupas: [
      { tipo: "Tênis", tamanho: "39", cor: "Branco", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000004"),
    doadorId: "6b1000000000000000000006",
    responsavelId: "6b1000000000000000000008",
    data: ISODate("2026-11-04T12:00:00.000Z"),
    status: "DISPONIVEL",
    roupas: [
      { tipo: "Moletom", tamanho: "G", cor: "Verde", estado: "Ótimo" },
      { tipo: "Bermuda", tamanho: "42", cor: "Bege", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000005"),
    doadorId: "6b1000000000000000000007",
    responsavelId: "6b1000000000000000000005",
    data: ISODate("2026-11-05T12:00:00.000Z"),
    status: "CONCLUIDA",
    roupas: [
      { tipo: "Vestido", tamanho: "M", cor: "Estampado", estado: "Bom" },
      { tipo: "Saia", tamanho: "38", cor: "Preta", estado: "Ótimo" },
      { tipo: "Camiseta", tamanho: "M", cor: "Branca", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000006"),
    doadorId: "6b1000000000000000000009",
    responsavelId: "6b1000000000000000000010",
    data: ISODate("2026-11-06T12:00:00.000Z"),
    status: "RECEBIDA",
    roupas: [
      { tipo: "Camisa", tamanho: "G", cor: "Azul-marinho", estado: "Ótimo" },
      { tipo: "Calça", tamanho: "42", cor: "Jeans", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000007"),
    doadorId: "6b1000000000000000000001",
    responsavelId: "6b1000000000000000000008",
    data: ISODate("2026-11-07T12:00:00.000Z"),
    status: "EM_TRIAGEM",
    roupas: [
      { tipo: "Casaco", tamanho: "M", cor: "Marrom", estado: "Bom" },
      { tipo: "Meias", tamanho: "39", cor: "Brancas", estado: "Novo" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000008"),
    doadorId: "6b1000000000000000000003",
    responsavelId: "6b1000000000000000000002",
    data: ISODate("2026-11-08T12:00:00.000Z"),
    status: "PENDENTE",
    roupas: [
      { tipo: "Regata", tamanho: "P", cor: "Amarela", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000009"),
    doadorId: "6b1000000000000000000004",
    responsavelId: "6b1000000000000000000005",
    data: ISODate("2026-11-09T12:00:00.000Z"),
    status: "DISPONIVEL",
    roupas: [
      { tipo: "Calça", tamanho: "44", cor: "Jeans", estado: "Bom" },
      { tipo: "Camiseta", tamanho: "GG", cor: "Preta", estado: "Ótimo" },
      { tipo: "Boné", tamanho: "Único", cor: "Preto", estado: "Bom" },
      { tipo: "Tênis", tamanho: "41", cor: "Preto", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  },
  {
    _id: ObjectId("6b2000000000000000000010"),
    doadorId: "6b1000000000000000000006",
    responsavelId: "6b1000000000000000000010",
    data: ISODate("2026-11-10T12:00:00.000Z"),
    status: "CONCLUIDA",
    roupas: [
      { tipo: "Cardigã", tamanho: "M", cor: "Vinho", estado: "Ótimo" },
      { tipo: "Shorts", tamanho: "40", cor: "Azul", estado: "Bom" }
    ],
    _class: "AEP.ClothIn.models.Doacao"
  }
]);
```

**Conferir os registros inseridos:**

```javascript
db.usuarios.countDocuments();
db.doacoes.countDocuments();

db.usuarios.find();
db.doacoes.find();
```

Os comandos acima inserem os dados de demonstração. Caso sejam executados novamente, os mesmos identificadores já existirão e o MongoDB poderá retornar erro de chave duplicada. Para recomeçar a demonstração, remova os registros de demonstração antes de executar novamente.

## 8. Como executar o projeto

### 8.1. Requisitos

Para executar o projeto, é necessário possuir:

- Java 21;
- Maven;
- MongoDB;
- IntelliJ IDEA.

Verifique a instalação do Java:

```bash
java -version
```

### 8.2. Clonar o repositório

```bash
git clone https://github.com/SeitiHsm/AEP1_esoft6C.git
```

Entre na pasta do projeto:

```bash
cd AEP1_esoft6C/ClothIn
```

Abra a pasta `ClothIn` no IntelliJ IDEA e aguarde o carregamento das dependências Maven.

### 8.3. Configurar o MongoDB

Verifique o arquivo:

```text
src/main/resources/application.properties
```

Confirme que a conexão está apontando para o banco `ClothIn` e que o MongoDB está em execução.

Após inserir os dados de demonstração, as coleções estarão disponíveis para a aplicação.

### 8.4. Iniciar a aplicação

No IntelliJ, localize:

```text
src/main/java/AEP/ClothIn/ClothInApplication.java
```

Execute a classe principal pelo botão Run.

Também é possível iniciar pelo Maven:

```bash
mvn spring-boot:run
```

Após a inicialização, acesse:

```text
http://localhost:8080/
```

A interface web estará disponível no navegador.

**Observação:** caso a porta 8080 esteja ocupada, encerre a aplicação que já está utilizando essa porta antes de iniciar outra instância.

## 9. Funcionalidades

### Gerenciamento de usuários

- Cadastro de usuários;
- Consulta de usuários;
- Atualização de usuários;
- Exclusão de usuários;
- Identificação de doadores e responsáveis.

### Gerenciamento de doações

- Cadastro de doações;
- Consulta de doações;
- Atualização de doações;
- Exclusão de doações;
- Associação de doadores e responsáveis;
- Inclusão de várias roupas em uma mesma doação;
- Atualização do status;
- Filtros por doador, responsável e status.

### Painel principal

Apresenta informações gerais sobre as doações e permite acompanhar os registros cadastrados.

## 10. API REST

O projeto disponibiliza uma API REST para operações de usuários e doações.

### Endpoints de usuários

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastrar usuário |
| GET | `/usuarios` | Listar usuários |
| GET | `/usuarios/{id}` | Buscar usuário por ID |
| PUT | `/usuarios/{id}` | Atualizar usuário |
| DELETE | `/usuarios/{id}` | Excluir usuário |

### Endpoints de doações

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/doacoes` | Cadastrar doação |
| GET | `/doacoes` | Listar doações |
| GET | `/doacoes/{id}` | Buscar doação por ID |
| GET | `/doacoes/doador/{doadorId}` | Listar doações de um doador |
| GET | `/doacoes/responsavel/{responsavelId}` | Listar doações de um responsável |
| GET | `/doacoes/status/{status}` | Filtrar por status |
| PUT | `/doacoes/{id}` | Atualizar doação |
| DELETE | `/doacoes/{id}` | Excluir doação |

As requisições podem ser verificadas utilizando ferramentas como Postman.

## 11. Testes automatizados

O projeto possui testes automatizados utilizando JUnit 5, Mockito e recursos de teste do Spring Boot.

As classes de teste documentadas são:

```text
src/test/java/AEP/ClothIn/
├── DoacaoControllerTest
├── DoacaoServiceTest
├── RoupaControllerTest
├── RoupaServiceTest
├── UsuarioControllerTest
└── UsuarioServiceTest
```

Os testes verificam comportamentos das camadas Controller e Service, incluindo operações e regras de negócio.

### 11.1. Executar os testes

No terminal, dentro da pasta `ClothIn`, execute:

No Windows:

```cmd
mvnw.cmd clean test
```

Ou, caso o Maven esteja instalado globalmente:

```bash
mvn clean test
```

A execução deverá apresentar `BUILD SUCCESS` quando os testes forem concluídos sem falhas.

### 11.2. Cobertura de testes com JaCoCo

O projeto utiliza JaCoCo para gerar o relatório de cobertura dos testes automatizados.

Após executar os testes, verifique se o relatório foi gerado em:

```text
target/site/jacoco/index.html
```

Abra esse arquivo no navegador para consultar a cobertura de instruções, linhas, métodos, classes e ramificações.

A AEP exige cobertura mínima de **70%** sobre o código da PoC apresentado na entrega.

**Resultado obtido:** consultar e informar o percentual real apresentado pelo relatório JaCoCo.

## 12. Estrutura do projeto

```text
ClothIn
├── src
│   ├── main
│   │   ├── java
│   │   │   └── AEP
│   │   │       └── ClothIn
│   │   │           ├── enums
│   │   │           ├── models
│   │   │           ├── repositories
│   │   │           ├── services
│   │   │           ├── controllers
│   │   │           └── ClothInApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │           ├── index.html
│   │           ├── style.css
│   │           └── script.js
│   └── test
│       └── java
│           └── AEP
│               └── ClothIn
│                   ├── DoacaoControllerTest
│                   ├── DoacaoServiceTest
│                   ├── RoupaControllerTest
│                   ├── RoupaServiceTest
│                   ├── UsuarioControllerTest
│                   └── UsuarioServiceTest
├── docs
├── pom.xml
└── README.md
```

## 13. Documentação técnica

A documentação complementar do projeto está organizada na pasta `docs/`.

O README apresenta as instruções de instalação, execução, testes e estrutura do banco de dados para facilitar a avaliação da PoC.

## 14. Versionamento

O projeto utiliza Git para controle de versão e GitHub para armazenamento do código-fonte.

As alterações são registradas por meio de commits, permitindo acompanhar a evolução do desenvolvimento.

**Versão da segunda entrega:** identificar por meio da tag, release ou commit correspondente à versão final da AEP 2.

## 15. Situação do projeto

**ClothIn — Segunda entrega da AEP.**

A aplicação evoluiu da estrutura inicial de cadastro de roupas para um sistema de gerenciamento de usuários e doações, utilizando MongoDB com múltiplas coleções, relacionamento por identificadores e documentos incorporados.

O projeto contempla interface web, API REST, persistência de dados, testes automatizados e documentação para execução e avaliação.
