# API RESTful para Consulta de Filmes

## Tecnologias utilizadas
A API foi desenvolvida utilizando Sprint Boot 3.2.5, Java 17 e Maven 3.9.9.
A IDE utilizada para criação do projeto foi o Sprint Tool Suite 4.


## Ambiente de execução alvo
Originalmente a API foi desenvolvida para execução em ambiente Linux, onde deverá estar instalada a versão 17 do Java.
Para compilação, build ou execução de testes automatizados o ambiente deverá contar também com Maven 3.9.9.


## Objetivo do sistema
O sistema deverá ler um arquivo CSV com dados de filmes que serão utilizados para modelagem de dados e implementação das regras de negócio.
Como principal função o sistema deve ser capaz de fornecer o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido, seguindo as especificações de resposta da página 2 do PDF de enunciado.


## Configurações do ambiente de execução
Tendo sido fornecido um arquivo CSV com dados de filmes (Movielist.csv), este arquivo foi colocado no módulo de resources da aplicação.
A forma atual do arquivo fornecido foi adotada como padrão para recuperação dos dados contidos, a saber:
- Caractere separador de campos: ';'
- Expressão regular para separação dos nomes dos produtores de filmes: ",|\\\sand\\\s" 

Caso seja necessário, é possível alterar a expressão regular usada para separação de nomes de produtores através da chave *outsera.challenge.csv.producer.names.splitter.regex* no arquivo *application.properties*, no projeto.

Internamente está sendo usada uma instância de banco de dados H2 para armazenamento dos dados lidos do arquivo CSV fornecido.
O console do banco H2 estará disponível após a aplicação ser iniciada. URL do H2: *http://localhost:8080/h2*. É necessário informar apenas usuário e senha (admin/admin) para ter acesso ao console do H2, demais campos têm valores default.
Uma vez no console, a tabela criada para armazenar os dados dos filmes é a *MOVIE*.


## Compilação/build em linha de comando
Para compilação e build do projeto via terminal, deve ser aberta uma sessão de Terminal na raiz do projeto e executar o comando *mvn clean install*. Com isso, o projeto será compilado, os testes automatizados serão executados e a aplicação será empacotada em um arquivo JAR.


## Consumo da aplicação
Após o build da aplicação, na pasta raiz do projeto será criada uma pasta target onde estará o executável da aplicação e demais arquivos.
Para executar a aplicação via linha de comando: abrir uma sessão de terminal na pasta target e executar o comando: *java -jar movies-0.0.1-SNAPSHOT.jar*

Foi desenvolvido um endpoint */movies/producers* para busca dos dados dos produtores conforme indicado no item 3 do PDF de enunciado.
CURL para request via linha de comando: *curl --location 'http://localhost:8080/movies/producers'*
