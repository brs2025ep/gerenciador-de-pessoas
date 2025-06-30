## Frontend
Framework: React

### Core Features:

- Área de cadastro com busca de CEP: Formulário com campos para nome, email, data de nascimento (dd/mm/AAAA), Endereço(CEP, rua, cidade, número(número da residência) e estado.
- Lista de pessoas cadastradas
- Área de Consulta de Pessoa por CPF

- Validação de Campos: 

[ NOME ]
Validação requer o campo Nome preenchido com nome composto.
Cada palavra no campo Nome deve ter primeira letra Maiúscula.

[ DATA NASCIMENTO]
Campo data de nascimento, se preenchido, não pode ter data futura.


[ CPF ]
Campo CPF, se preenchido deve ter máscara 000.000.000-00.
O valor do Campo CPF terá enviado apenas seus números.

[ EMAIL ]
Campo Email, se preenchido, deve ter formato email@email.com

[ ENDEREÇO]
Endereço. Se informado CEP, deve ter todos os campos de Endereço preenchidos.
CEP no formato 00000-000.
CEP deve aceitar apenas número.
Se o CEP não for encontrado(CEP formatado e inválido)


Ao salvar pessoa, enviar pré-requisição para verificar se já existem alguém cadastrado com CPF.

O botão Salvar, vai criar ou atualizar os dados da pessoa.


** Se a Pessoa cadastrada tem todos os campos preenchidos e está cadastrada em Backend, pode enviar a pessoa para integração.


-----
Listagem de pessoas

A tabela deve listar todas as pessoas cadastradas no banco de dados.

Coluna nascimento dd/mm/AAAA

Coluna CPF 000.000.000-00

Coluna Cidade : Cidade / Estado

Coluna Situação da Integração : Não enviado / Pendente / Sucesso / Erro

Botão de Ação: 
Editar, vai carregar os dados da pessoa no formulário

Integrar, vai enviar a pessoa para a integração.
** Integrar só vai aparecer no status Pendente ou Erro.

Remover, vai remover a pessoa da API, via requisição REST, e do Banco de Dados.
** Deve ter uma modal de confirmação.
** Se der erro na remoção, da API, não deve ser removido da base de dados, e ainda deve mostrar uma mensagem de erro.

-----

Integração da pessoa

A integração deve ocorrer ao salvar a pessoa no cadastro de pessoa se todos os campos forem preenchidos e o salvamento da pessoa ocorreu com sucesso.

A integração deve ser feita em uma fila JMS
 - Fila na aplicação Web(backend)
 - Fila deve enviar a pessoa para a API.
 - Fila deve atualizar a situação de integração (erro ou sucesso) e mensagem, ou objeto da pessoa retornado pela API.

A API vai validar se Pessoa tem todos os campos preenchidos.
 - A API vai validar o CEP, se o CEP não existe(CEP inválido e formatado), não salvar a Pessoa na API e retornar ERRO,



- Botão Enviar: Visually consistent submit button.
- Ícones: Appropriate icons for each input field.
- Busca de CEP: Função para Buscar CEP(Código de Endereçamento Postal do Brasil) usando uma API externa para preencher demais campos de endereço. This tool intelligently determines whether a given zip code is likely to be a real and valid one. It also reasons whether or not to prefill other address fields in the form.

// TODO: Melhorar readme
### Guia da Marca:

- Primary color: Rich navy blue (#1A237E) to evoke trust and professionalism.
- Background color: Light gray (#EEEEEE) for a clean, neutral backdrop.
- Accent color: Deep purple (#5E35B1) to draw attention to important interactive elements and provide visual contrast.
- Body and headline font: 'Inter', a grotesque-style sans-serif, will be used for a modern, neutral look suitable for headlines and body text.
- Use a set of simple, line-based icons from a consistent set (e.g. FontAwesome or Material Design Icons) for visual clarity and recognition.
- Employ a clean, single-column layout for the form to guide the user's eye naturally down the page.
- Use subtle transitions and animations to provide feedback on user interactions and maintain engagement without distraction.


// TODO: Adicionar FIGMA

## Backend

Banco de dados: PostgreSQL
Framework : Spring Boot com Hibernate, Spring Data JPA e Swagger.


### Estrutura ORM

`Pessoa`  
idPessoa (Integer) (PK)
nome (String)
nascimento (Date)
cpf (String)
email (String)
endereco (Endereco)
situacaoIntegracao (String)

`Endereco`  
idPessoa (Integer) (FPK)
cep (Integer)
rua (String)
numero (Integer)
cidade (String)
estado (String

### Endpoints

#### 1. Salvar pessoa 

**URL:** `/pessoa`  
**Método HTTP:** `POST`  
**Descrição:** Salvar uma nova pessoa.

**Modelo de Reequisição(`application/json`):**  

```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
 }
}
```

**Exemplo de resposta:(Sucesso - `201 Created`):**

```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
 }
}
```

**Exemplo de resposta:(Erro - `400 Bad Request`):**


#### 2. Atualizar pessoa pelo CPF
**URL**: `/pessoa/cpf/{cpf}`    
**Método HTTP:** `PUT`  
**Descrição:** Atualizar informações de uma pessoa pelo seu CPF.

**Parâmetros de Path**  
**`cpf`** (Integer) - O ID único da Pessoa a ser atualizada (Obrigatório).

**Corpo de requisição (`application/json`):**
```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
 }
}
```

**Exemplo de resposta:(Sucesso - `200 OK`):**
```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
    }
}
```

#### 3. Obter pessoa pelo CPF

**URL**: `/pessoa/cpf/{cpf}`    
**Método HTTP:** `GET`  
**Descrição:** Obter informações de uma pessoa pelo seu CPF.

**Parâmetros de Path**  
**`cpf`** (Integer) - O ID único da Pessoa a ser obtida (Obrigatório).

**Exemplo de resposta:(Sucesso - `200 OK`):**
```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
    }
}

```

#### 4. Remover pessoa pelo CPF

**URL**: `/pessoa/cpf/{cpf}`    
**Método HTTP:** `DELETE`  
**Descrição:** Remover uma pessoa pelo seu CPF.

**Parâmetros de Path**  
**`cpf`** (Integer) - O ID único da Pessoa a ser obtida (Obrigatório).

**Exemplo de resposta:(Sucesso - `204 No Content`):**


#### 5. Obter lista de pessoas cadastradas

GET /pessoa

Response: Lista de pessoas
Status code: 200


Response:
Status code: 204

## API

### ORM

`Pessoa`  
idPessoa (Integer) (PK)
criacaoRegistro (Date)
alteracaoRegistro (Date)
nome (String)
nascimento (Date)
cpf (String)
email (String)
endereco (Endereco)


`Endereco`  
idPessoa (Integer) (FPK)
cep (Integer)
rua (String)
numero (Integer)
cidade (String)
estado (String)

### Endpoints

#### 1. Salvar pessoa

**URL:** `/pessoa`  
**Método HTTP:** `POST`  
**Descrição:** Salvar uma nova pessoa.

**Modelo de Reequisição(`application/json`):**  

```JSON

{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
 }
}
```

**Exemplo de resposta:(Sucesso - `201 Created`):**

```JSON
{
    "idPessoa": "Integer",
    "mensagem": "Sucesso"
}
```

**Exemplo de resposta:(Erro - `400 Bad Request`):**

```JSON
{
    "idPessoa": "Integer",
    "mensagem": "Erro"
}
```

**Exemplo de resposta:(Erro - `422 Unprocessable Content`):**

```JSON
{
    "idPessoa": "Integer",
    "mensagem": "Erro"
}
```


#### 2. Atualizar Pessoa por CPF
**URL**: `/pessoa/cpf/{cpf}`    
**Método HTTP:** `PUT`  
**Descrição:** Atualizar informações de uma pessoa pelo seu CPF.

**Parâmetros de Path**  
**`cpf`** (Integer) - O ID único da Pessoa a ser atualizada (Obrigatório).

**Corpo de requisição (`application/json`):**

```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
    }
}
```

**Exemplo de resposta (Sucesso - `200 OK`):**
```JSON
{   
    "idPessoa": "Integer",
    "mensagem": "String"
}
```

#### 3. Obter pessoa por CPF
**URL**: `/pessoa/cpf/{cpf}`    
**Método HTTP:** `GET`  
**Descrição:** Obter informações de uma pessoa pelo seu CPF.

**Parâmetros de Path**  
**`cpf`** (Integer) - O ID único da Pessoa a ser obtida (Obrigatório).

**Exemplo de resposta (Sucesso - `200 OK`):**
```JSON
{
    "nome": "String",
    "dataNascimento": "String",
    "cpf": "String",
    "email": "String",
    "endereco": {
        "cep": "Integer",
        "rua": "String",
        "numero": "Integer",
        "cidade": "String",
        "estado": "String"
    },
    "dataHoraInclusaoRegistro" : "Timestamp",
    "dataHoraUltimaAlteracaoRegistro" : "Timestamp"
}
```

#### 4. Remover Pessoa por CPF

**URL**: `/pessoa/cpf/{cpf}`    
**Método HTTP:** `DELETE`  
**Descrição:** Obter informações de uma pessoa pelo seu CPF.


**Parâmetros de Path**  
**`cpf`** (Integer) - O ID único da Pessoa a ser obtida (Obrigatório).


**Exemplo de resposta (Sucesso - `200 OK`):**
```JSON
{   
    "mensagem":"String"   
}
```