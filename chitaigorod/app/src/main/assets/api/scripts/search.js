define([ 'elasticsearch', 'elastic', 'bodybuilder.min'], function () {
	var _lastReq =  null;
	class Search{
		constructor(){
			this.client = new elasticsearch.Client ({ 
  					host: ['https://search.chitai-gorod.ru'],	
				});
		}
		static getLastReq(){
			return _lastReq;
		}
		static setLastReq(v){
			_lastReq = v;
		}
		getDataSearchSugg(param, callback){
			Search.setLastReq( param.query);
			
			var val = param.query || "";
			this.client.msearch({
            body: [
            /**
             * get name
             */
                {index: 'catalog', type: 'catalog'},
                ejs.Request()
                    .query(
                        ejs.BoolQuery()
                            .must(
                                ejs.FilteredQuery(
                                    ejs.DisMaxQuery()
                                        .queries(ejs.MatchQuery('name', val)
                                            .boost(30)
                                            .type('phrase_prefix')
                                            .slop(3)
                                            .fuzziness(0.7)
                                        )
                                        .queries(ejs.MatchQuery('author', val)
                                            .boost(30)
                                            .type('phrase_prefix')
                                            .slop(3)
                                        )
                                        .queries(ejs.MatchQuery('author_b', val)
                                            .type('phrase_prefix')
                                            .slop(3)
                                        )
                                        .queries(ejs.MatchQuery('author_t', val)
                                            .type('phrase_prefix')
                                            .slop(3)
                                        )
                                        .queries(ejs.MatchQuery('name', val)
                                            .type('prefix')
                                            .boost(10)
                                        )
                                        .queries(ejs.MatchQuery('author', val).type('prefix').boost(30)
                                        )
                                        .queries(ejs.MatchQuery('author_b', val).type('prefix')
                                        )
                                        .queries(ejs.MatchQuery('author_t', val).type('prefix')
                                        )
                                        .queries(ejs.MatchQuery('tags', val).type('prefix')
                                        )
                                        .queries(ejs.MatchQuery('seria', val)
                                        )
                                        .queries(ejs.MatchQuery('bid', val)
                                        )

                                        .tieBreaker(1.0)
                                ))
                    ).size(20),

            /**
             * get author
             */
                {index: 'catalog', type: 'catalog'},
                ejs.Request()
                    .query(
                        ejs.BoolQuery()
                            .must(
                                ejs.FilteredQuery(
                                    ejs.DisMaxQuery()
                                        .queries(ejs.MatchQuery('author', val)
                                            .boost(30)
                                            .type('phrase_prefix')
                                            .slop(3)
                                        )
                                        .queries(ejs.MatchQuery('author_b', val)
                                            .type('phrase_prefix')
                                            .slop(3)
                                        )
                                        .queries(ejs.MatchQuery('author_t', val)
                                            .type('phrase_prefix')
                                            .slop(3)
                                        )
                                        .queries(ejs.MatchQuery('author', val).type('prefix').boost(30)
                                        )
                                        .queries(ejs.MatchQuery('author_b', val).type('prefix')
                                        )
                                        .queries(ejs.MatchQuery('author_t', val).type('prefix')
                                        )
                                        .queries(ejs.MatchQuery('tags', val).type('prefix')
                                        )

                                        .tieBreaker(1.0)
                                ))
                    ).size(500),

            /**
             * get seria
             */
                {index: 'catalog', type: 'catalog'},
                ejs.Request()
                    .query(
                        ejs.BoolQuery()
                            .must(
                                ejs.FilteredQuery(
                                    ejs.DisMaxQuery()
                                        .queries(ejs.MatchQuery('seria', val)
                                            .type('phrase_prefix')
                                            .slop(0)
                                            .boost(3)
                                            .minimumShouldMatch(90)
                                        )
                                        .queries(ejs.MatchQuery('seria', val)
                                            .type('phrase')
                                            .slop(0)
                                            .boost(3)
                                            .minimumShouldMatch(90)
                                        )
                                        .queries(ejs.MatchQuery('seria', val)
                                            .slop(0)
                                            .minimumShouldMatch(90)
                                        )

                                        .tieBreaker(1.0)
                                ))
                    ).size(20)
            ]
        }).then(function (response) {
			if(val == Search.getLastReq()){
  				var result = {name:[],author:[], seria:[]};
				var respItemAuthor = {};		
				response.responses[1].hits.hits.forEach(function(itm){		
					if(itm._source.author_p !== null){
						respItemAuthor[itm._source.author] = itm._source.author_p;
					}	
				});
				var authorKeys = Object.keys(respItemAuthor);
	
				authorKeys.forEach(function(a){
					result.author.push({author:a, author_p:respItemAuthor[a]} );
				});
			

				result.name = response.responses[0].hits.hits;
				
				return callback(result);
			}


		});

			}


getDataSearch(param, callback){
	var val = param.query;
	var page = param.page;
	
	var filter = param.filter;
	
	var author = filter.author;
	var seria = filter.seria;
	var year = filter.year;
	var category = filter.categiry;
	
	var body = bodybuilder();
	;
	body.query('match','name','книжный вор');
	
	if(author != 'not_set'){
		body.filter('term', 'author', author);
	}
	//body.filter('term', 'author', "Пушкин");
	//return callback(body.build());
	
	this.client.search(
		{
			catalog:'catalog',
			
			body:body.build()
		}).then(function(data){
		return callback(data);
	});
	//var query 
	//console.log(param);
	//return callback({});
	}
		}
	
	
	

	
return new Search();
});
