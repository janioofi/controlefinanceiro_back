# Back-end do sistema Controle Financeiro 💻

## O que é este projeto
Este é um projeto API Rest de um sistema para controle financeiro, onde o usuário poderá criar todos os seus pagamentos, definir o status, método e categoria. Esse sistema possui um sistema de validação, onde todos os usuários terão que criar uma conta para conseguir acessar o sistema. Os pagamentos só poderão ser visualizados pelo próprio usuário; nenhum outro poderá verificar essas informações.

## Tecnologias Utilizadas
- [Front-end](https://github.com/janioofi/controlefinanceiro)
  - JavaScript
  - TypeScript
  - Angular
  - HTML
  - CSS
  - Bootstrap
- Back-end
  - Java 17
  - Spring Boot
  - Maven
  - Spring Data
  - Spring Security
  - PostgreSQL
  - JWT
  - Flyway
- Deploy
  - Railway
  - Vercel

## Link da Documentação

```
https://api-cotrolefinanceiro.up.railway.app/swagger-ui/index.html
```
![documentacao](https://github.com/user-attachments/assets/f2e2750d-4b46-4882-b68c-0833762257f9)


## Funcionalidades
- CRUD para usuários.
- CRUD para pagamentos.
- Autenticação JWT.

## Request e Response
- **Register Request**: O JSON enviado para criar um usuário é:
```json
{
  "username": "string",
  "password": "string",
  "confirmPassword": "string"
}
```
- **Register Response**: Recebe como retorno uma string de confirmação ou um erro caso não seja concluído.

- **Login Request**: O JSON enviado para criar um usuário é:
```json
{
  "username": "string",
  "password": "string"
}
```

- **Payment Request**: O JSON enviado para criar um pagamento é:
```json
{
  "description": "string",
  "paymentDate": "yyyy-MM-dd",
  "value": 0,
  "category": "LEISURE",
  "status": "PENDING",
  "paymentMethod": "MONEY"
}
```

 - Valores dos Campos Enums:
   -  statuses = ['PENDING', 'APPROVED', 'CANCELED','Cancelado]
   - categories = ['LEISURE', 'Lazer', 'STUDIES', 'FOOD', 'HOUSING', 'TRANSPORT', 'TRIPS', 'HEALTH']
   - paymentMethods = ['MONEY', 'PIX', 'DEBIT_CARD', 'CRED_CARD']
   
- **Payment Response**: Caso concluído, o retorno será o pagamento no corpo da resposta; caso contrário, será enviado um erro:

## Regras do sistema
- Todo pagamento tem vínculo com o usuário que o criou.
- O usuário só consegue verificar e realizar alterações nos seus próprios pagamentos, não podendo interferir nos pagamentos de outros usuários.
- Apenas o usuário consegue verificar e realizar alterações no seu cadastro, não podendo interferir no cadastro de outros usuários.

## License
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

## Autor
|                                                                   Janio Filho                                                                    |
|:------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                              <img src="./github/images/perfil.png" width="150"/>                                                  |
| [![Techs](./github/icons/linkedin.png)](https://www.linkedin.com/in/janioofi) [![Techs](./github/icons/github.png)](https://github.com/janioofi) |
