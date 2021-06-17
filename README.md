# Api Pesquisa

Api de pesquisa para dados do ElasticSearch. Toda pesquisa de dados do Elasticsearch passa por esta api. Onde possui vários endpoint especifico para cada índice.

## Índices ElasticSearch de pesquisa

Cada **índice** possui um endpoint com suas respectivas característica de busca. Tendo também seu domínio, repositório e demais para implementação.

## Rodar o projeto

### Ambiente local, seguir os seguintes passos:

1. **Profile local** Rodar direto da IDE a classe `ApiPesquisaApplication`, lembrando de informar o profile para a jvm com `-Dspring.profiles.active=dev`

2. (Alternativa) `mvn spring-boot:run -Drun.profiles=dev`

3. **ElasticSearch** `docker-compose -up`

4. Acessar [http://localhost:8082/docs/](http://localhost:8082/docs/)

## Implementação

Para cada índice de pesquisa, basicamente é desenvolvido em 3 pilares, um **controlador**, **repositório rest** e **conversor**. Esses 3 itens a serem implementado estão descritos abaixo.
Obs: Esses 3 itens é apenas uma abstração, pois cada índice possui seu domínio, interface etc.

### 1. Controlador
Para o novo controlador basta apenas seguir os padrões de desenvolvimento rest, disponibilizando um GET  por exemplo para consultar o índice com seus respectivos parâmetros se necessário. Nesse método não deve possuir regras para pesquisa. O controlador deve receber apenas parâmetros e repassar para camada responsável. Toda e qualquer regra peculiar que seja do elasticSearch ou não, devem esta em suas respectivas camada.

### 2. Repositório rest
Para cada índice a ser pesquisado é necessário criar um repositório. Esse repositórios vai fazer a pesquisa no índice usando o repositório [elasticSearch](src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/elasticSearch) com seus métodos próprios de pesquisa. 
Não deve ser usando nesse repositório bibliotecas ou implementações para fazer consultas no elasticSearch, para isso deve ser usado o repositório citado acima. Nele já possui métodos e formas padrões para fazer essas consultas, tirando complexidades e códigos duplicados para consulta com ElasticSearch.

#### Instruções para criação do método de pesquisa para repositório
1. ***Criando parâmetros para template:***
	Instancie a class [ParametrosParaTemplateElasticSearch](src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/elasticSearch/parametros/ParametrosParaTemplateElasticSearch.java), passando os build que você precisa para realizar a consulta. Esta class esta preparada para receber diversos parâmetros utilizado no template como quantidade, pagina, ordenação, tipo ordenação, campos que devem existir ou excluir, entre outros. E possui também métodos para outros tipo de pesquisa, como filter, query_string e agregação do resultado. Abaixo segue.
	
	1.1 ***Parâmetros de pesquisa*** exemplo:
	```
	ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch
					.builder()
					.pagina(1)
					.quantidade(20)
					.ordenacao(Arrays.asList(new Ordenacao("nome")))
					.build();
	```
	1.2 ***Métodos para pesquisa*** o métodos criarFiltroContains() pesquisa o valor que contenha no campo informado. Já o método criarFiltro() faz um filtro com o valor e campo informando. Para mais informação sobre a pesquisa que é realizada no método consulte: [Query_string](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html)  e [filter](https://www.elastic.co/guide/en/elasticsearch/reference/7.5/query-filter-context.html).
	```
	    // Pode ser adicinado quantos desejar, usando método AND ou OR
	    parametrosES.criarFiltroContains("temaPortal", "Agropecuária")
	        .and("assunto", "Eleição direta");

	    // Pode ser adicionados quantos desejar usando o método add
	    parametrosES.criarFiltro("colecao", "Comissões")
	        .add("veiculo", "agencia");
	```
	1.3 ***Ordenação da pesquisa*** é possível adicionar uma lista de objeto do tipo Ordenacao.class, podendo passar vários campos a serem ordenados juntamente com tipo de ordenação para cada campo. É recomendado que se crie um enum para receber os campos permitidos para ordenação, para evitar futuros erros, pois nem todos campos são ordenados. Segue abaixo exemplo de chamada Get com campos e uso para ordenação.
	```
	Get - [http://localhost:8082/api/servidores?ordenacao=nome:DESC,situacao]
	ou
	Get - [http://localhost:8082/api/servidores?ordenacao=nome:DESC&ordenacao=situacao]
	
	OBS: Caso não seja informado o tipo de ordenação para o campo, por padrão é atribuido ASC como tipo.
	```
	1.4 ***Range data da pesquisa*** é possível fazer um range por data para adicionar ao filtro. Para utilizar basta apenas passar, data inicio, data fim e o nome do campo para fazer o filtro. Esses dados devem ser passados através de um modelo que recebe esses valores.
	Caso queira fazer um filtro utilizando uma data de inicio e data atual basta passar `null` para o campo data fim, que ele considera a data presente para filtro data fim. Exemplo abaixo:
	``` 
	Date dataInicio = new SimpleDateFormat("dd-MM-yyyy").parse("24-01-2020");
	
	// Criando objeto rage para filtro data
	RangeData range = new RangeData(
		dataInicio ,
		null, 
		"dataPublicacao");

	// Add objeto para o método de filtro rangeData
	parametrosES.criarFiltro("situacao", "Julgada")
			.addRangeData(range());
	```

	1.5 ***Agregação para pesquisa*** é possível adicionar uma agregação simples utilizando o método criarAgregacao(). Caso o método não atenda ao requisito, é possível passar seu código de agregação especifico para consulta.
	```
	// Método simples agregação
	parametrosES.criarAgregacao("tema", "temaNoticia.keyboard", 100, TipoOrdenacao.DESC);

	// Passando agregação específica
	parametrosES.criarAgregacao("" + 
				"    	\"retranca\":{  \n" + 
				"		     \"terms\":{  \n" + 
				"		        \"field\":\"retranca.keyword\",\n" + 
				"		        \"size\":100,\n" + 
				"		        \"order\":{  \n" + 
				"		           	\"_count\":\"desc\"\n" + 
				"		        }\n" + 
				"    		}\n" + 
				"		}\n");
	```

2. ***Realizando pesquisa no ElasticSearch***
A classe [PesquisaElasticSearchImpl](src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/elasticSearch/PesquisaElasticSearchImpl.java), é possivel utlizar 2 métodos para realizar pesquisa. Podendo passar até 3 parâmetros, parâmetros elasticSearch, contexto (o contexto é nome do índice, lembrando que o template **json** deve esta com o mesmo nome do contexto pois o contexto é utilizado para puxar o arquivo) e também uma query personalizada. que ira substituir partes dos parâmetros que é passado na classe de parâmetros elasticSearch:

	2.1 ***Pesquisa com 2 parametro*** exemplo utilizando 2 parâmetros.

	```
	String contexto = "enquetes";

	PesquisaElasticSearch pesquisa = repositorio
			.pesquisar(parametrosES, contexto);
	```

	2.2 ***Pesquisa com parâmetro personalizado*** exemplo de pesquisando passando os 3 parâmetros, podendo agora personalizar. Basicamente quando é passado esse último parâmetro a classe template substitui o ***must*** e ***filter***, por esse parâmetro personalizado.

	```
	String contexto = "enquetes";
	String parametrosQueryPersonalizados = "\"should\": [" + 
							"{" + 
								"\"range\" : { " + 
									"\"documentos.dataFimExercicio\": { \"gte\" : \"now\" } " + 
								"}" + 
							"}," + 
							"{" + 
								"\"bool\": {" + 
									"\"must_not\": {" + 
										"\"exists\": {" + 
											"\"field\": \"documentos.dataFimExercicio\"" + 
										"}" + 
									"}" + 
								"}" + 
							"}" + 
						"]";

	PesquisaElasticSearch pesquisa = repositorio
			.pesquisar(parametrosES, contexto, parametrosQueryPersonalizados);
	```

3. ***Realizando conversão dos dados***
Após fazer a pesquisa no ElasticSearch, o resultado pode ser passado para um lista ou objeto dto puxando **buscarConteudos** ou **buscarAgregacoes** dependendo da sua necessidade, após isso sim deve chamar o conversos. 

	**buscarConteudos:** exemplo de uso [EnqueteRest](src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/EnqueteRest.java)
	```
	ConversorEnquete conversor;

	List<EnqueteDTO> buscarConteudos = pesquisa
			.buscarConteudos(EnqueteDTO.class);
			
	List<Enquete> conversor.convert(buscarConteudos);
	```
	**buscarAgregacoes:** exemplo de uso [VotacaoRest](src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/VotacaoRest.java)
		```
		ConversorProposicoesMaisVotadas conversor;

		List<BucketsDTO> buscarAgregacoes = pesquisa
				.buscarAgregacoes("descricaoProposicao");
				
		ProposicoesVotadas conversor.convert(buscarAgregacoes);
		```

### 3. Conversor
Para cada índice é necessário criar uma class conversor que implementa o [Converter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/Converter.html) do spring. Nela deve ter o método que converte e retorna o resultado em um domínio. Exemplo de uso do [ConversorDiscurso](src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/conversores/ConversorDiscurso.java).

### 4. Template
É necessário criar um template, exemplo: **(nome do índice).json**, seu nome deve ser igual ao nome do índice, pois na implementação do repositório é criado uma variável **contexto** que é utilizado para puxar esse arquivo. Esse json vai ter uma estrutura de pesquisa que é padrão para maioria dos casos. Esse template tem seu inicio igual ao mostrado abaixo. Demais critérios podem ser adicionados como por exemplo aggregations, pois ele é estático e não é necessário passar nenhum valor dinâmico.

```
{  
    "from" : %d, "size" : %d,
    "sort" : %s,
    "_source" :%s,
    "query" : {
        "bool" : {
            "must": %s,
            "filter": %s
         }
    },
    "aggs": {
    	%s
    }
}
```

Essa seria o top obrigatório para cada template mesmo que não sejam utilizados os atributos. Os % serão substituídos automaticamente pela class [Template](/src/main/java/br/leg/camara/apipesquisa/adaptadores/repositorio/rest/elasticSearch/Template.java), caso não sejam setados os parâmetros, o template substitui por valores padrões que não interferem no resultado da pesquisa. Exemplo de uso [enquetes.json](src/main/resources/elasticsearch/enquetes.json).

## Observação

Caso você necessite de uma pesquisa mais especifica que o repositório elasticSearch não atenda. É possível e recomendado que faça implementação no próprio repositório do elasticSearch, contribuindo para novas forma de pesquisa e evolução da api.
