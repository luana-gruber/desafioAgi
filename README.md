# Introdução do Projeto: API Customers e API Insurance
 - **API Customers** : Responsável por realizar consulta, criação, atualização e deleção de clientes no banco de dados;
 - **API Insurance** : Responsável pela simulação e contratação de seguros. Realiza chamada na API Customers para verificar se o cliente existe antes que realizar a contratação do seguro.

As duas API's foram desenvolvidas como parte de um desafio técnico do AgiBank com o objetivo de criar uma solução que realize operações de CRUD e chamada Back to Back entre as aplicações.

## Funcionalidades

1. **Criação de clientes**: A API Customer verifica se o CPF do cliente já possui cadastro, caso não, realiza operação de inserção no banco.
2. **Consulta de clientes**: A API Customer consulta no banco clientes cadastrados, tanto por id, quanto por CPF.
2. **Atualização cadastro clientes**: A API Customer realiza atualização do cadastro do cliente no banco, localizando-o por id.
3. **Deleção cadastro clientes**: A API Customer realiza deleção do cadastro do cliente no banco, localizando-o por id.
4. **Simulação de Seguro**: API Insurance retorna simulação de seguro de acordo com o plano informado.
5. **Contratação de Seguro**: API Insurance realiza chamada para API Customer para validar se cliente possui cadastro e realiza a contratação do seguro gravando a operação no banco.

# Como rodar o projeto
1. Preparar o ambiente:
    - Clonar o repositório do projeto do git para sua máquina local:

```bash
git clone https://github.com/luana-gruber/desafioAgi.git
cd desafioAgi
```
Escolha qual API abrir na sua IDE, e execute-as normalmente. 
**Observação**: Ambas devem ser abertas e executadas simultaneamente para realizar a chamada Back to Back.

# Tecnologias
A aplicação foi desenvolvida com as principais tecnologias:
1. Java  - Linguagem do projeto
2. SpringBoot - Framework para desenvolver aplicações web
   - Spring WebFlux
   - Spring Web
   - Spring Data Mongo Reactive
3. Resilience4j - Para mecanismos de resiliência, como CircuitBreaker e RateLimiter
4. Maven - Automação de compilação e dependências
5. Mongo - Banco de dados
6. JUnit - Testes automatizados
7. Mockito - Criação de mocks  para teste unitários
8. Swegger - Documentação da API


# Postman
Deixei a collection com todas as rotas disponibilizadas no postman.
