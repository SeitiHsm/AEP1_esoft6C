# ClothIn

Sistema para cadastro e gerenciamento de roupas destinadas à doação e reutilização, desenvolvido como parte da Atividade de Estudo Programado (AEP).

O **ClothIn** busca facilitar o gerenciamento de doações de roupas, permitindo organizar os usuários envolvidos, registrar peças e acompanhar o andamento das doações.

## Problema

O descarte de roupas que ainda estão em condições de uso contribui para o desperdício de recursos e reduz as possibilidades de reutilização dessas peças.

Além disso, a falta de uma forma organizada para registrar e gerenciar roupas destinadas à doação pode dificultar o controle desses itens.

O ClothIn surge como uma solução para auxiliar nesse gerenciamento, permitindo cadastrar usuários, registrar doações, incluir várias peças em uma mesma doação e acompanhar seus respectivos status.

## ODS

O projeto está relacionado ao **ODS 12 – Consumo e Produção Responsáveis**.

O ODS 12 busca promover padrões de consumo e produção mais sustentáveis, incentivando o uso consciente dos recursos e a redução de desperdícios.

O ClothIn contribui com esse objetivo ao incentivar a reutilização de roupas e facilitar o gerenciamento das peças destinadas à doação.

## Objetivo

O objetivo do ClothIn é disponibilizar uma aplicação para organizar o processo de doação de roupas, desde o cadastro dos usuários até o acompanhamento das doações.

A aplicação permite:

- Cadastrar, consultar, atualizar e excluir usuários;
- Identificar usuários como doadores ou responsáveis;
- Cadastrar e gerenciar doações;
- Associar um doador e, opcionalmente, um responsável a cada doação;
- Adicionar várias peças de roupa em uma mesma doação;
- Atualizar e consultar o status das doações;
- Filtrar doações por doador, responsável e status;
- Armazenar os dados utilizando MongoDB;
- Disponibilizar uma interface web para utilização do sistema;
- Disponibilizar uma API REST para as operações.

## Funcionalidades

### Gerenciamento de usuários

O sistema permite cadastrar e gerenciar os usuários envolvidos nas doações.

Cada usuário possui:

- Nome;
- E-mail;
- Telefone;
- Tipo de usuário.

Os tipos disponíveis são:

- `DOADOR`: usuário responsável por realizar a doação;
- `RESPONSAVEL`: usuário responsável por acompanhar ou gerenciar a doação.

### Gerenciamento de doações

O sistema permite registrar e gerenciar doações, associando cada uma a um doador e, opcionalmente, a um responsável.

Uma doação pode conter várias peças de roupa, permitindo registrar diferentes itens em uma única operação.

### Acompanhamento de status

As doações possuem os seguintes status:

| Status | Descrição |
|---|---|
| `PENDENTE` | Doação registrada e aguardando recebimento |
| `RECEBIDA` | Doação recebida |
| `EM_TRIAGEM` | Doação em processo de avaliação |
| `DISPONIVEL` | Peças disponíveis para reutilização |
| `CONCLUIDA` | Processo da doação concluído |

### Painel principal

A interface apresenta informações gerais sobre as doações, permitindo visualizar indicadores e acompanhar a situação dos registros.

### Interface web

O ClothIn possui uma interface web para facilitar a utilização das funcionalidades de usuários e doações, sem depender de operações diretamente pelo terminal.

## Tecnologias

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data MongoDB**
- **MongoDB**
- **HTML**
- **CSS**
- **JavaScript**
- **Maven**
- **JUnit 5**
- **Mockito**
- **JaCoCo**
- **IntelliJ IDEA**
- **Git e GitHub**

## Arquitetura

O projeto utiliza uma organização em camadas, separando as responsabilidades da aplicação.

O fluxo principal é:

```text
Interface Web / Requisição HTTP
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

Representa as entidades e estruturas de dados utilizadas pela aplicação.

Entre os principais modelos estão:

- `Usuario`: representa os usuários cadastrados;
- `Doacao`: representa uma doação;
- `RoupaDoacao`: representa uma peça de roupa incorporada dentro de uma doação.

### Controller

Recebe as requisições HTTP da API REST e direciona as operações para a camada de serviço.

### Service

Concentra as regras de negócio, validações e operações realizadas sobre usuários e doações.

### Repository

Realiza a comunicação com o MongoDB utilizando o Spring Data MongoDB.

### Interface Web

A interface permite utilizar as funcionalidades do sistema por meio de páginas HTML, estilizadas com CSS e integradas ao backend através de JavaScript.

## Estrutura do banco de dados MongoDB

O ClothIn utiliza o **MongoDB** como banco de dados NoSQL.

Nesta segunda entrega, a estrutura foi ampliada para trabalhar com duas coleções principais:

```text
ClothIn
├── usuarios
└── doacoes
```

### Coleção `usuarios`

Armazena os dados dos usuários cadastrados no sistema.

| Campo | Descrição |
|---|---|
| `_id` | Identificador do usuário |
| `nome` | Nome do usuário |
| `email` | E-mail do usuário |
| `telefone` | Telefone do usuário |
| `tipoUsuario` | Tipo do usuário: `DOADOR` ou `RESPONSAVEL` |

Exemplo de documento:

```json
{
  "_id": "ID_DO_USUARIO",
  "nome": "Maria",
  "email": "maria@email.com",
  "telefone": "44999999999",
  "tipoUsuario": "DOADOR"
}
```

### Coleção `doacoes`

Armazena as doações registradas no sistema.

| Campo | Descrição |
|---|---|
| `_id` | Identificador da doação |
| `doadorId` | Identificador do usuário doador |
| `responsavelId` | Identificador do responsável, quando associado |
| `data` | Data da doação |
| `status` | Situação atual da doação |
| `roupas` | Lista de peças incorporadas à doação |

Exemplo de documento:

```json
{
  "_id": "ID_DA_DOACAO",
  "doadorId": "ID_DO_USUARIO",
  "responsavelId": "ID_DO_RESPONSAVEL",
  "data": "2026-11-09",
  "status": "RECEBIDA",
  "roupas": [
    {
      "tipo": "Camiseta",
      "tamanho": "G",
      "cor": "Azul",
      "estado": "Bom"
    },
    {
      "tipo": "Calça",
      "tamanho": "G",
      "cor": "Preta",
      "estado": "Ótimo"
    }
  ]
}
```

### Relacionamento entre as coleções

O relacionamento entre usuários e doações é realizado por meio dos identificadores dos usuários.

- Um usuário do tipo `DOADOR` pode estar associado a várias doações.
- Cada doação possui um doador identificado por `doadorId`.
- Uma doação pode possuir um responsável identificado por `responsavelId`.
- Uma doação pode conter várias peças de roupa dentro do campo `roupas`.

As peças são armazenadas como documentos incorporados (*embedded documents*) dentro da própria coleção `doacoes`, não sendo necessário criar uma coleção separada para cada peça.

Esse modelo permite manter os dados das roupas agrupados com a doação à qual pertencem.

## Instruções básicas de execução

### 1. Requisitos

Para executar o projeto, é necessário utilizar:

- Java 21;
- Maven;
- IntelliJ IDEA;
- MongoDB.

Verifique a versão do Java instalada:

```bash
java -version
```

O resultado deve indicar uma versão 21 do Java.

### 2. Clonar o projeto

Clone o repositório:

```bash
git clone https://github.com/SeitiHsm/AEP1_esoft6C.git
```

Entre na pasta do projeto:

```bash
cd AEP1_esoft6C
```

A aplicação está localizada dentro da pasta:

```text
ClothIn
```

Abra essa pasta utilizando o IntelliJ IDEA.

Aguarde o carregamento das dependências do Maven e verifique se o projeto está utilizando Java 21.

### 3. Preparar o MongoDB

O projeto utiliza uma instância local do MongoDB, configurada no arquivo:

```text
src/main/resources/application.properties
```

O banco de dados utilizado é:

```text
ClothIn
```

As coleções principais são:

```text
usuarios
doacoes
```

#### Importando os dados de demonstração

Para facilitar a execução e a avaliação em outro computador, o projeto disponibiliza um pacote com dados de demonstração do MongoDB.

O arquivo contém os dados necessários para iniciar a demonstração das funcionalidades, incluindo usuários e doações.

**Download do pacote:**

[📥 Baixar ClothIn_Dados.zip](database/ClothIn_Dados.zip)

Após baixar o arquivo:

1. Extraia o conteúdo do ZIP;
2. Inicie o MongoDB local;
3. Abra o terminal na pasta onde está o arquivo `stream.js`;
4. Execute:

```bash
mongosh "mongodb://localhost:27017/ClothIn" stream.js
```

O script irá inserir ou atualizar os documentos de demonstração nas coleções `usuarios` e `doacoes`.

Os dados de demonstração utilizam identificadores que preservam os relacionamentos entre usuários e doações.

### 4. Executar a aplicação

No IntelliJ IDEA, localize a classe principal:

```text
src/main/java/AEP/ClothIn/ClothInApplication.java
```

Abra a classe e clique no botão de execução ao lado do método `main`.

Também é possível iniciar a aplicação pelo Maven, dentro da pasta `ClothIn`:

```bash
mvn spring-boot:run
```

Após a inicialização do Spring Boot, a aplicação estará disponível em:

```text
http://localhost:8080/
```

A interface web poderá ser acessada pelo navegador.

**Observação:** o MongoDB deve estar em execução antes de iniciar a aplicação. Caso a porta 8080 já esteja sendo utilizada por outro processo, será necessário encerrá-lo antes de executar o ClothIn.

## Testes automatizados

O projeto possui testes automatizados utilizando **JUnit 5**, **Mockito** e recursos de teste do Spring Boot, incluindo `@WebMvcTest`.

Os testes estão localizados em:

```text
src/test/java/AEP/ClothIn
```

As classes de teste implementadas são:

```text
DoacaoControllerTest
DoacaoServiceTest
RoupaControllerTest
RoupaServiceTest
UsuarioControllerTest
UsuarioServiceTest
```

Os testes verificam o comportamento das regras de negócio e das rotas REST implementadas nas camadas de `Controller` e `Service` para usuários, roupas e doações.

### Executando os testes pelo IntelliJ IDEA

Para executar os testes:

1. Abra a pasta `src/test/java/AEP/ClothIn`;
2. Clique com o botão direito sobre a pasta;
3. Selecione a opção de execução de todos os testes.

Também é possível executar uma classe individual ou utilizar a opção **Run All Tests with Coverage** para visualizar a cobertura diretamente na IDE.

### Executando os testes pelo Maven

Dentro da pasta `ClothIn`, execute:

```bash
./mvnw clean test
```

No Windows, também é possível utilizar:

```cmd
mvnw.cmd clean test
```

O Maven irá compilar o projeto e executar os testes automatizados, gerando o relatório de cobertura por meio do **JaCoCo**.

Quando todos os testes forem executados sem falhas, o terminal deverá apresentar:

```text
BUILD SUCCESS
```

### Relatório de cobertura JaCoCo

Após a execução dos testes, o relatório HTML de cobertura será gerado em:

```text
target/site/jacoco/index.html
```

Abra o arquivo `index.html` no navegador para consultar os percentuais de cobertura de:

- Instruções;
- Ramificações;
- Linhas;
- Métodos;
- Classes.

O relatório permite verificar quais partes do código foram executadas durante os testes automatizados.

A meta estabelecida para a entrega é de **no mínimo 70% de cobertura**, conforme os requisitos da AEP.

**Resultado da cobertura:** inserir aqui o percentual real obtido no relatório JaCoCo antes da entrega.

### Fluxo dos testes

```text
Preparar dados e mocks
         ↓
Executar método do Service
ou requisição MockMvc
         ↓
Verificar chamadas ao Repository / Service
         ↓
Confirmar retorno, status HTTP
ou exceção esperada
```

Os testes isolam dependências utilizando mocks para validar fluxos de sucesso, exceções e regras de negócio.

## API REST

Além da interface web, o projeto disponibiliza uma API REST para realizar as operações de usuários e doações.

### Endpoints de usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastrar um usuário |
| `GET` | `/usuarios` | Listar usuários |
| `GET` | `/usuarios/{id}` | Buscar usuário pelo ID |
| `PUT` | `/usuarios/{id}` | Atualizar um usuário |
| `DELETE` | `/usuarios/{id}` | Excluir um usuário |

### Endpoints de doações

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/doacoes` | Cadastrar uma doação |
| `GET` | `/doacoes` | Listar doações |
| `GET` | `/doacoes/{id}` | Buscar doação pelo ID |
| `GET` | `/doacoes/doador/{doadorId}` | Listar doações de um doador |
| `GET` | `/doacoes/responsavel/{responsavelId}` | Listar doações de um responsável |
| `GET` | `/doacoes/status/{status}` | Filtrar doações pelo status |
| `PUT` | `/doacoes/{id}` | Atualizar uma doação |
| `DELETE` | `/doacoes/{id}` | Excluir uma doação |

As requisições podem ser verificadas utilizando ferramentas como Postman.

## Estrutura do projeto

```text
ClothIn
├── database
│   └── ClothIn_Dados.zip
├── docs
│   └── documentação técnica
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
├── pom.xml
└── README.md
```

## Documentação técnica

A documentação técnica complementar do projeto está disponível na pasta:

```text
docs/
```

Ela deve apresentar as informações necessárias para compreender a estrutura da aplicação, sua arquitetura, o funcionamento do banco de dados e as tecnologias utilizadas.

## Versionamento

O projeto utiliza **Git** para controle de versão e **GitHub** para armazenamento do código-fonte.

As alterações realizadas durante o desenvolvimento são registradas por meio de commits, permitindo acompanhar a evolução do ClothIn.

Cada entrega deve possuir uma identificação clara da versão correspondente, por meio de tag, release ou commit informado pela equipe.

**Versão da segunda entrega:** inserir a tag, release ou identificação do commit correspondente à versão final da AEP 2.

## Escopo da segunda entrega

A segunda entrega representa a evolução da primeira versão funcional do ClothIn.

Enquanto a primeira entrega tinha como foco o CRUD básico de roupas, esta etapa amplia a aplicação com:

- Gerenciamento de usuários;
- Gerenciamento de doações;
- Associação entre usuários e doações;
- Armazenamento de várias peças em uma mesma doação por meio de documentos incorporados;
- Ampliação da estrutura do banco de dados NoSQL;
- Interface web para utilização do sistema;
- API REST para usuários e doações;
- Testes automatizados utilizando JUnit 5 e Mockito;
- Relatório de cobertura de testes com JaCoCo;
- Documentação técnica e instruções de execução;
- Pacote de dados de demonstração do MongoDB.

## Situação atual

**Segunda versão do ClothIn — AEP 2.**

O sistema possui funcionalidades para gerenciamento de usuários e doações, persistência no MongoDB, interface web, API REST e testes automatizados.

A entrega também contempla documentação técnica, instruções para reprodução do ambiente e evidências de execução dos testes e da cobertura de código.
