# cancanvas-mobile
## Plataforma para armazenamento de dados ##

> [[1]](#1) Bancos de dados [...] são conjuntos de arquivos relacionados entre si com registros sobre pessoas, lugares ou coisas. São coleções organizadas de dados que se relacionam de forma a criar algum sentido (Informação) e dar mais eficiência durante uma pesquisa ou estudo cientifico.

[[5]](#5) Alguns dos bancos de dados mais utilizados no mercado atualmente são: [**Oracle Express**](https://www.oracle.com/database/technologies/appdev/xe.html), [**MySQL**](https://www.mysql.com/), [**Microsoft SQL Server**](https://www.microsoft.com/pt-br/sql-server/), [**MongoDB**](https://www.mongodb.com/) e [**PostgreSQL**](https://www.postgresql.org/).

### Modelos de banco de dados ###

Bancos de dados se organizam em dois modelos principais: relacionais e não-relacionais.

#### [[6]](#6) Bancos de dados relacionais (*SQL*) ####

> *SQL* é a sigla para “*Structured Query Language*” que significa, traduzindo para o português, “Linguagem de Consulta Estruturada”. Trata-se de uma linguagem de consulta a banco de dados relacionais. Com o *SQL*, você pode executar vários comandos para criar, alterar, gerenciar, consultar, dentre outras informações no seu banco de dados. Costumamos dizer que bancos *SQL* seguem uma modelagem relacional, pois estes se baseiam no fato de que todos seus dados sejam guardados em tabelas.

#### [[6]](#6) Bancos de dados não-relacionais (*NoSQL*) ####

> *NoSQL* (*Not Only SQL*) é o termo utilizado para banco de dados não relacionais de alto desempenho, onde geralmente não é utilizado o *SQL* como linguagem de consulta. O NoSQL foi criado para ter uma performance melhor e uma escalabilidade mais horizontal para suprir necessidades onde os bancos relacionais não são eficazes.

### Teorema *CAP* (*Consistency*, *Avaliability* e *Partition Tolerance*) ###

> [[7]](#7) O Teorema *CAP* (*Consistency*, *Avaliability* e *Partition Tolerance*) é muito importante no mundo da ciência de dados, especialmente quando precisamos decidir entre quais pilares são essenciais para cada caso.

![Teorema CAP](https://miro.medium.com/max/671/1*7mDBUO-j0yws52wZlSxbAg.png)
*Imagem 1 - Teorema CAP*
&nbsp;

Os três pilares do Teorema *CAP* são:
 - Consistência: garante que todos os nós (instâncias) do banco de dados possuam os mesmos dados em todos os momentos. Um sistema é consistente se as transações começam com o sistema em um estado de consistência e terminam também em um estado de consistência. Porém, os nós precisam de tempo para se atualizarem, o que diminui a disponibilidade.

 - Disponibilidade: garante que todas as requisições feitas ao banco de dados receberão um resposta de sucesso ou fracasso. Alta disponibilidade exige que o sistema esteja operacional em todo o tempo. Todo cliente recebe uma resposta, independente do estado dos nós no sistema.

 - Tolerância de Partição: garante que o sistema continue funcionando, mesmo que haja mensagens atrasadas pela conexão de rede entre os nós. Um sistema que possui tolerância de partição pode suportar uma grande quantidade de falhas de rede, desde que as falhas não afetem todo o sistema.

Como o serviço proposto neste artigo é centrado no conteúdo enviado do servidor para a aplicação, é vital que o acesso dos usuários a este conteúdo não seja impedido em caso de falhas de rede e a consistência dos dados apresentados é de extrema importância para que os clientes do aplicativo possam fazer uso das informações recebidas. Portanto, o *MongoDB*, que é um banco de dados orientado a documentos, foi escolhido. O formato dos documentos armazenados no *MongoDB* é o *BSON* (*JSON* binário). A modelo de dados utilizado pelo *JSON* (JavaScript Object Notation) proporciona um fácil mapeamento dos dados para objetos utilizados nas diversas linguagens de programação. Como o esquema dos documentos é dinâmico, há uma maior flexibilidade para evoluir o modelo de dados.

## *Back-end* e *Front-end*

> [[2]](#2) *Back-end*, como o próprio nome sugere, vem da ideia do que há por trás de uma aplicação.

Numa aplicação que utiliza serviços *online* próprios, o *software* *back-end* é a parte dessa aplicação que é executada num servidor. É responsável por processar requisições de usuários, (troca de informações) que podem incluir autenticações, transações financeiras e outras informações confidenciais.

> [[2]](#2) Podemos classificar como a parte visual de uma aplicação aquilo que conseguimos interagir. Quem trabalha com *front-end* é responsável por desenvolver por meio de código uma interface gráfica, normalmente com tecnologias base da *Web* [...].

No caso do serviço proposto neste documento, a parte de *front-end* se estende, não para desenvolvimento de um *site*, mas sim para um aplicativo móvel, mencionado com detalhes posteriormente neste artigo.

## *API* (*Application Programming Interface*) ##

> [[9]](#9) Uma *API* (*Application Programming Interface*) é uma interface de computação que define interações entre múltiplos intermediários de software. Ela define os tipos de chamadas ou requisições que podem ser feitas, como fazê-las, o formato de dados que deve ser usado, etc.

No caso do serviço proposto neste documento, a *API* se dá entre a interação do *back-end* (software em execução no servidor) com o *front-end* (aplicativo móvel).

### [[10]](#10) Tipos de *API* ###

> Para estabelecer quais são as melhores tecnologias, algorítmos e técnicas para o desenvolvimento de uma *API*, deve-se considerar as características de 3 dos principais casos de uso com *API*s.

#### *Experience API*s ####

> (*Experience API*s) são *API*s para consumo por aplicativos *front-end* e dispositivos para experiências digitais.
>
> Suas principais características são:
> - Menor tempo de transferência de dados
> - Menor tempo de resposta
> - Uso em conjunto com padrão *BFF* (*back-end for front-end*)
> - Menos requests para renderização de uma tela
> - Maior nivel de monitoramento

#### *Open API*s ####

> (*Open API*s) são APIs para integrações com parceiros e inovação aberta.
>
> Suas principais características são:
> - Experiencia de uso mais fácil e seguindo padres mais conhecidos
> - Reuso de *API*s
> - Documentação detalhada
> - Maior nível de segurança
> - Maior nível de governança

#### *Internal API*s ####

> (*Internal API*s) são APIS para comunicação entre microsserviços e integrações internas.
>
> Suas principais características são:
> - Compatibilidade com a arquitetura de eventos ou arquitetura reativa
> - Melhor desempeno
> - Mais escalabilidade
> - Maior nível de controle e monitoramento

### Arquiteturas de transmissão de dados ###

> [[12]](#12) (Arquiteturas de transmissão de dados) providenciam padrões entre sistemas de computadores conectados, facilitando sistemas a se comunicarem uns com os outros.

#### [[11]](#11) *REST* ####

A arquitetura *REST* é uma das mais conhecidas, definida pela primeira vez em 2000, por Roy Fielding. Por natureza, a arquitetura *REST* é sem-estado, construída de uma maneira de modo que qualquer serviço que a implemente possa receber informações textuais. Essas operações de envio e recebimento de informações são chamadas de "requisições", e podem ser do tipo *GET*, *POST*, *PUT* e outros, que servem para diferenciar tratativas de operações no *back-end*.

##### Estudo de caso: *PayPal* #####

O serviço *PayPal* tem uma forte função de negócios central, que providencia sistemas integrados para processamento de pagamento.

Neste exemplo, retirado da documentação do *PayPal*, pode-se notar alguns dos componentes essenciais numa requisição *REST*:

`curl -v -X GET https://api.sandbox.paypal.com/v1/activities/activities?start_time=2012-01-01T00:00:01.000Z&amp;end_time=2014-10-01T23:59:59.999Z&amp;page_size=10 \
-H "Content-Type: application/json" \
-H "Authorization: Bearer Access-Token"`

O tipo da requisição (`GET`) indica que o usuário deseja **obter informações** do *back-end*. A rota neste caso, é definida como "`activities`", que é o que determina qual chamada (ou função) será executada no *back-end*. Outro componente de uma requisição *REST* são os *headers*, que podem ser interpretados como espécies de parâmetros passados para a *API*, tratados pela função executada. A linha `-H "Content-Type: application/json"` indica o formato no qual os outros *headers* serão representados, e a linha `-H "Authorization: Bearer Access-Token"` é propriamente uma informação de contexto, que passa uma *token* de acesso para o *PayPal*.

#### [[11]](#11) *gRPC* ####

A *gRPC* (*gRPC Remote Procedure Call*) é uma extensão do método *RPC* (*Remote Procedure Call*) &mdash; que visa a execução de funções (ou chamadas de procedimento), com tipos de parâmetros e retorno fixos, localmente ou através de uma rede de computadores &mdash; com suas próprias vantagens e desvantagens.

> A principal diferença entre a *RPC* e a *REST* está na maneira como a *RPC* lida com suas negociações de contrato. Enquanto a *REST* define suas interações através de termos padronizados em suas requisições, a *RPC* funciona a partir da ideia de contratos, na qual a negociação é definida [...] pela relação cliente-servidor ou invés de pela arquitetura por si só. A *RPC* dá mais poder e responsabilidade ao cliente, [...] fazendo com que muito da responsabilidade de computação seja transferida do servidor ao cliente.

A maior funcionalidade disponível na arquitetura *gRPC* é o conceito de *protobufs*, que é uma linguagem neutra em termos de plataforma e sistema utilizada para descrever os parâmetros dos contratos. Isso possibilita que as chamadas de procedimento sejam serializadas de uma maneira eficiente.

##### Estudo de caso: *Google Cloud* #####

Os serviços *Google Cloud PubSub API*, *Google Cloud Speech API* e *Google Cloud BigTable Client API* utilizam a arquitetura *gRPC* como intermediador e sistema de processamento para seus dados. Este é um caso apropriado, uma vez que o *design* da *gRPC* é relativamente ágil e é melhor utilizado em *stream*s (fila contínua) de informações.

#### [[11]](#11) *GraphQL* ####

Com *GraphQL*, é o cliente quem determina quais, quantos e o formato no qual a *API* deve enviar dados. Essas tratativas podem ser classificadas como uma forma reversa das tratativas clássicas do *REST* e *RPC*, nas quais o contrato é negociado pelo servidor e o cliente, mas é amplamente definido pelos recursos.

> Deve ser notado que um grande benefício da *GraphQL* é o fato de que, a requisição enviada pelo servidor é tipicamente a menor possível. A *REST* por outro lado, geralmente envia todos os dados por padrão &mdash; quanto mais completa a requisição, melhor. Por causa disso, a *GraphQL* pode ser mais útil em casos de uso específicos onde o tipo de dados é bem-definido e um baixo volume é preferido.

##### Estudo de caso: *GitHub API* #####

> Um exemplo de uso da arquitetura *GraphQL* é a *API GraphQL da API do GitHub*. Mesmo que a *API* *REST* inicial fosse poderosa e fazia o que era proposto, a equipe do *GitHub* achou que a *API* era inflexível. Falando sobre o problema, a equipe disse que as repostas da *API* "ao mesmo tempo tinham dados demais e não incluiam os dados que os consumidores necessitavam.

### [[11]](#11) Comparação de arquiteturas ###

As principais diferenças entre as arquiteturas mencionadas acima podem se resumir ao seguinte quadro:

![Las Arquitecturas y sus principales diferencias](https://3k64nh47gxyj39ud4k2tc04b-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/apis-graphql-rest-grpc-1.png)
*Comparativo de arquiteturas*
&nbsp;

Para *Experience API*s, a "matriz de elegibilidade" ou a "tabela de escores" das três arquiteturas fica da seguinte forma:

![*Experience API*s](https://3k64nh47gxyj39ud4k2tc04b-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/apis-graphql-rest-grpc-4.png)
Tabela das arquiteturas para *Experience API*s
&nbsp;

Já para as *Open API*s, temos a tabela a seguir:

![*Open API*s](https://3k64nh47gxyj39ud4k2tc04b-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/apis-graphql-rest-grpc-5.png)
Tabela das arquiteturas para *Open API*s
&nbsp;

Para as *Internal API*s, a arquitetura *REST* pode ser a mais eficiente:
![*Internal API*s](https://3k64nh47gxyj39ud4k2tc04b-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/apis-graphql-rest-grpc-6.png)
Tabela das arquiteturas para *Internal API*s

Portanto, a combinação de informações personalizadas para cada usuário, juntamente com o volume mínimo de informações possíveis, somada à alta performance da arquitetura ***GraphQL*** é ideal para o desenvolvimento do serviço proposto neste documento.

## Linguagem de programação para o back-end ##

O programa *back-end* deve ser capaz de garantir, não só a segurança e integridade dessas informações, como também a capacidade de lidar com múltiplas conexões simultâneas, e processamento volumoso. Um dos fatores que mais impactam, na estruturação e performance desse tipo de *software* é a linguagem na qual foi concebido. [[3]](#3) Atualmente, algumas das linguagens mais utilizadas para desenvolvimento de aplicações *back-end* são [***Node.js***](https://nodejs.org/), [***PHP***](https://www.php.net/), [***Java***](https://www.java.com/) e [***Go***](https://golang.org/).

### [[4]](#4) Chamadas de sistema ###

Chamadas de sistema ou *syscalls* são requisições do software para o *kernel* do sistema para efetuar uma operação de E/S (entrada e saída) no dispositivo (*hardware*). Chamadas de sistema são classificadas em bloqueantes e não-bloqueantes. Em chamadas não-bloqueantes, também conhecidas como chamadas assíncronas, o kernel armazena um request numa fila ou num *buffer* e responde a requisição imediatamente, sem esperar pela resposta da operação de E/S.

### [[4]](#4) *Scheduling* ###

Enquanto *threads* diferentes compartilham da mesma memória, processos diferentes &mdash; por outro lado &mdash; carregam um contexto de memória consigo, e quanto mais processos, mais *scheduling* (distribuição de tempo de processamento) é realizada pelo gerenciador de processos do sistema. Isso é equivalente a mais trocas de contexto, que neste caso, sofre cada vez mais redução de performance no processamento de chamadas síncronas, em contraste com o mesmo processamento implementado utilizando alocação de *threads*.

Já as chamadas não-bloqueantes requisitam que o *kernel* execute um *callback* (requisição do próprio *kernel* para a origem da chamada) quando as operações forem concluídas. Esse tipo de chamada foi feito especificamente para lidar com grandes volumes de operações de E/S de forma eficiente.

### Processamento de requisições ###

#### PHP ####

O modelo de processamento de requisições implementado em servidores *PHP* é relativamente simples. Uma requisição *HTTP* chega ao servidor, que **cria um processo para a requisição**, com algumas otimizações para reutilizar o processo para outras requisições.

O código-fonte é simplesmente inserido em sua página, e **operações de E/S são bloqueantes**.

É importante destacar que a aproximação utilizada em servidores *Ruby* é praticamente idêntica à aproximação implementada em servidores *PHP*.

#### Java ####

O modelo de tratamento de requisições implementado em servidores *Java* é bem diferente, já que a linguagem conta com *multithreading* nativo. A maior parte dos *back-end*s *Java* **iniciam uma nova *thread* para cada requisição *HTTP*** que chega ao servidor.

Como já descrito anteriormente, *threads* compartiham da mesma memória, possibilitando que requisições num servidor *Java* acessem a dados de outas requisições, mas o impacto em como *threads* interagem com o *scheduling* é praticamente o mesmo de um servidor *PHP*. Uma vez que, nas versões mais usadas do *Java*, **todas as operações de E/S são bloqueantes**, *thread*s de requisições ainda ficam em espera durante seu processamento até que a requisição seja completa. Portanto, milhares de conexões significam milhares de *threads* sendo executadas no mesmo processador, o que faz voltar o problema do *scheduling*.

#### Node.js ####

A plataforma de execução da linguagem *JavaScript*, *Node.js*, é conhecida por lidar com chamadas e E/S de forma não-bloqueante. Esse resultado é alcançado pelo uso das chamadas "funções de *callbacks*", que são executadas assim que o *kernel* começa e termina a execução da chamada.

Entretanto, **todas as operações de E/S e processamento de requisições são executados em uma única *thread***. Cada parte do código bloqueia a parte seguinte, por serem operações concorrentes.

#### Go ####

A linguagem *Go* tem o seu próprio *scheduler* nativo. Em vez de cada *thread* de execução corresponder a uma *thread* do sistema operacional, *Go* trabalha com o conceito de "*goroutine*"s. Durante o tempo de execução, a *Go* pode atribuir uma *goroutine* a uma *thread* do sistema, suspendê-la, ou até mesmo não associá-la a uma *thread* do sistema operacional, baseado no que a *goroutine* está fazendo. Cada request tem sua própria *goroutine*.

A *Go* também mapeia automaticamente as *goroutines* à todas as *threads* disponíveis no sistema, interagindo também diretamente com o *scheduler* do sistema operacional. No geral, a *Go* implementa o *multithreading* nativo do *Java* &mdash; com alocação dinâmica de *threads* &mdash; e as operações de E/S não-bloqueantes do *Node.js*, garantindo um ganho considerável de performance.

### [[4]](#4) *Benchmarking* (testes de performance com base em algoritmos em comum) ###

Como mencionado previamente, a performance do *back-end* é de extrema importância para um bom funcionamento da aplicação. Um servidor deve ser capaz de processar um grande quantidade de requisições e respondê-las no menor tempo possível.

Foram realizados testes, com as linguagens citadas anteriormente, para demonstrar as diferenças de performance entre os métodos de processamento utilizados por elas. Em cada uma das linguagens foi implementado um algoritmo para ler um arquivo com 64kb de tamanho, tendo como seu conteúdo *bytes* aleatórios, e calcular a *hash* *SHA-256* (resultado do algoritmo criptográfico *SHA-256* N vezes (onde N é enviado como parâmetro na *URL* da requisição, por exemplo: `.../test?N=10`). Dessa forma foi possível realizar os testes com uma E/S consistente e uma forma controlada de aumentar o processamento realizado (aumentando o N).

No primeiro teste foram executadas 2000 iterações, com 300 requisições simultâneas em cada iteração e apenas um cálculo de *hash* por requisição, ou seja N = 1.

![Imagem 2 - Teste 1: 2000 iterações, 300 requisições simultâneas, N = 1](https://uploads.toptal.io/blog/image/126909/toptal-blog-image-1534449533146-fbfd7f7f8fef1c1642379e78288ff833.png)
*Imagem 2 - Teste 1: 2000 iterações, 300 requisições simultâneas, N = 1*
&nbsp;

O gráfico acima mostra o tempo (em milissegundos) em que cada requisição foi respondida (em média) em cada um dos servidores testados, ou seja, quanto menor o tempo, melhor. Com o volume de conexões e processamento aplicados neste teste, não é possível obter um conclusão sobre qual linguagem é mais performática, os dados demonstram mais sobre a execução das linguagens do que sobre suas implementações de E/S.

![Imagem 3 - Teste 2: 2000 iterações, 300 requisições simultâneas, N = 1000](https://uploads.toptal.io/blog/image/126910/toptal-blog-image-1534449549951-9c5873ba6add8234d456199df802891e.png)
*Imagem 3 - Teste 2: 2000 iterações, 300 requisições simultâneas, N = 1000*
&nbsp;

No segundo teste, o número de iterações e requisições simultâneas foi mantido, porém, em cada requisição foram realizados 1000 cálculos de *hash*. O gráfico acima apresenta o tempo (em milissegundos), em média, em que cada requisição, foi respondida, então novamente, quanto menor for o tempo, melhor.

Por causa das operações de processamento intenso realizadas em cada requisição, a performance do *Node.js* é negativamente afetada.

![Imagem 4 - Teste 3: 2000 iterações, 5000 requisições simultâneas, N = 1](https://uploads.toptal.io/blog/image/126911/toptal-blog-image-1534449565382-9c3f283d73f19b6d1164372e9b2611ea.png)
*Imagem 4 - Teste 3: 2000 iterações, 5000 requisições simultâneas, N = 1*
&nbsp;

No terceiro teste, o número de iterações continuou o mesmo, porém o número de requisições simultâneas passou a ser 5000 e o N voltou a ser 1. O gráfico acima a quantidade de requisições que foram processadas por segundo, por cada servidor, dessa vez, quanto maior o número, melhor.

Devido ao grande volume de conexões, a performance do *PHP* foi negativamente afetada, pois é preciso criar um novo processo para cada uma das requisições. No teste final, a linguagem *Go* foi a mais performática, seguida do *Java*, *Node.js* e finalmente do *PHP*.

Portanto, o processamento das requisições utilizando *threads* e as operações de E/S não-bloqueantes, implementadas pela linguagem *Go*, são a combinação ideal para garantir uma melhor performance do servidor.


## Bibliografia ##

##### 1 
https://pt.wikipedia.org/wiki/Banco_de_dados

##### 2
https://www.alura.com.br/artigos/o-que-e-front-end-e-back-end

##### 3
https://www.improgrammer.net/most-popular-web-back-end-programming-language/

##### 4
https://www.toptal.com/back-end/server-side-io-performance-node-php-java-go

##### 5
http://aprendaplsql.com/oracle/os-5-bancos-de-dados-mais-utilizados-do-mercado/

##### 6
https://www.treinaweb.com.br/blog/sql-vs-nosql-qual-usar/

##### 7
https://towardsdatascience.com/cap-theorem-and-distributed-database-management-systems-5c2be977950e

##### 8
https://medium.com/@bikas.katwal10/mongodb-vs-cassandra-vs-rdbms-where-do-they-stand-in-the-cap-theorem-1bae779a7a15

##### 9
https://en.wikipedia.org/wiki/Application_programming_interface

##### 10
https://sensedia.com/api/apis-rest-graphql-ou-grpc/

##### 11
https://nordicapis.com/when-to-use-what-rest-graphql-webhooks-grpc/

##### 12
https://www.codecademy.com/articles/what-is-rest