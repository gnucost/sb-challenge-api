# Social Bank Challenge API

Usarei esse arquivo de README para anotar meu processo criativo. Ele mudará de acordo com o desenvolvimento.

## Usarei o [spring initializr](https://start.spring.io/) para criar o projeto. Lista de dependencias iniciais:

- Spring Web
  - estou assumindo que o JUnit virá automaticamente por causa do spring
  - precisarei tbm do springfox para o swagger, mas ele n está na lista do spring initializr
- Spring Data JPA
- H2 Database (in-memory database)

## Maven vs Gradle

Decisão: Gradle

Não quero perder muito tempo com isso porque ambos "resolvem". Achei o Gradle um pouco mais interessante, menos verboso. Não conheço _groovy_, mas parece ser suficientemente simples.

## Rascunho da API

### Requisições HTTP

Em um primeiro momento acredito que as seguintes requisições atenderão os requisitos do desafio.

- accounts
  - POST /accounts _Desc:_ add new account
  - PUT /accounts _Desc:_ update an existing account
  - GET /accounts/{id} _Desc:_ find account by id
  - DELETE /accounts/{id} _Desc:_ change account status to CANCELED
  - GET /accounts/{id}/balance _Desc:_ find current balance for account
  - GET /accounts/{id}/transactions _Desc:_ list transactions for account id

- transactions
  - POST /transactions/deposit _Desc:_ add a credit transaction to account
  - POST /transactions/transfer _Desc:_ transfer credit between accounts
  - POST /transactions/payment _Desc:_ add a payment transaction to account

### Entities

Algumas pequenas considerações antes de listar as entities:

- Para valores financeiros o tipo _BigDecimal_ parace ser o mais apropriado. Pode ser que vale a pena dar uma olhada em _Currency_.
- Datas precisam respeitar a ISO 8601 então usarei _java.time.Instant_.
- Enums funcionam diferente no Java, precisarei entender um pouco melhor. Possivelmente usarei para status da conta e tipo de transação (conceito inicial).
- String resolve para o __barcode__? (Acho que sim, mas fiquei com uma pulguinha)

## Docker

<mais pra frente>

## Discussão sobre Design Patterns e princípios SOLID

- _factory_ para a criação de contas?

<mais pra frente>

## Outros

- IDE de escolha será o Intellij IDEA.

## Ideias malucas

E SE eu usasse uma linked list para transações financeiras? A conta apontaria para a última transação e cada transação apontaria para a transação anterior. Uma função hash simples do próprio conteúdo da transação poderia ser usada como id da transação (seria único pq a transação teria pelo menos a data/hora diferentes). A transação poderia guardar informações de balanço atual no momento em que ela foi efetuada e o balanço atual da conta seria fácil de calcular pq basta olhar para a última transação. Transações seriam um simples json. Daria para implementar isso aproveitando REDIS ou MongoDB?

a vantagem (talvez) seria a possibilidade de uma implementação _distribuida_. O hash ajuda nesse sentido (igual como o git funciona, que foi de onde veio a ideia). Seria tipo uma blockchain super super simples.

A POC disso não parece ser difícil de ser implementada, mas não vou tentar fazer isso nesse desafio. Nem tenho certeza se a ideia é boa mesmo.
