define([ 'elasticsearch', 'elastic', 'bodybuilder.min', 'jquery'], function () {
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

getOtherData(param, callback){
		var info = [];
		(param.hits.hits).forEach(function(value){
			info.push({id: value._source.id, bookId: value._source.bid, section_id: value._source.ibl_sec_id});
		});
		console.log(JSON.stringify(info));

		
		$.ajax({
            type: "POST",
            url: 'search/check_stock.php',
            data:  JSON.stringify(info),
            contentType: 'application/json',
        	dataType: 'json',
			success:function (data) {		
				console.log(data);
				callback(data);
			}, error: function(err){
				console.log(JSON.stringify(err));
			}
		});
		
		//console.log("MAGIC{ctxId:'"+ctxId+"', method:'getDataSearch', data:"+JSON.stringify(data)+"}");
	}

getDataSearch(param, callback){
	var val = param.query;
	
	Search.setLastReq( param.query);

	var page = param.page;
	
	var filter = param.filter;
	
	var author = filter.author;
	var seria = filter.seria;
	var year = filter.year;
	var category = filter.categiry;
	
	var body = bodybuilder();
	
	body.from(page*20);
	body.size(20);
	
	/*
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
                                        )*/
   /*
	body.orQuery('match','name',{"query": val, boost:50, slop: 3,  fuzziness: 0.7,type: 'phrase_prefix'});
	body.orQuery('match','name.lowercase_space',{"query": val, boost:50, slop: 3,  fuzziness: 0.7,type: 'phrase_prefix'});
	body.orQuery('match','author',{"query":   val, boost:10, slop: 3,type: 'phrase_prefix'});
	//body.orQuery('match','author_b',{"query": val, boost:10, slop: 3,type: 'phrase_prefix'});
	//body.orQuery('match','author_t',{"query": val, boost:10, slop: 3,type: 'phrase_prefix'});
	//body.orQuery('match','name',{"query": val, boost:10,type: 'phrase_prefix'});
	/*body.orQuery('match','author',{"query": val, boost:30,type: 'phrase'});
	body.orQuery('match','author_b',{"query": val,type: 'phrase'});
	body.orQuery('match','author_t',{"query": val,type: 'phrase'});
	body.orQuery('match','tags',{"query": val,type: "phrase"});
	body.orQuery('match','seria',{"query": val});
	*//*
	body
  .orQuery('bool', f => {
    f.orQuery('match', 'author', [val])
    f.orQuery('match', 'name', [val])
    return f
  });*/
  /*
	body.orQuery('match','name',{"query": val, boost:50, slop: 3,  fuzziness: 0.7,type: 'phrase_prefix'});
	body.orQuery('match','name.lowercase_space',{"query": val, boost:50, slop: 3,  fuzziness: 0.7,type: 'phrase_prefix'});
	body.orQuery('match','author',{"query":   val, boost:50,fuzziness:0.7, slop: 3,type: 'phrase_prefix'});
*/
/*
body.orQuery("bool", {boost: 50},(f) => { 
   	f.query("match", 'name.lowercase_space', {query: val.toLowerCase(), fuzziness:0.7})
    f.query("match", 'author', {query: val.toLowerCase(),  fuzziness:0.7})
    return f;
});*/
body
  .orQuery(
    'multi_match',
    {
      query: val.toLowerCase(),
	 // "type":       "phrase_prefix",
       fuzziness: 0.7,
	   "operator": "and",
	   boost:100,
	  // "slop":3,
      //prefix_length: 0,
      fields: ["name.lowercase_space", "author"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  )
body
  .orQuery(
    'multi_match',
    {
      query: val.toLowerCase(),
	   "type":       "phrase_prefix",
       fuzziness: 0.7,
	   "slop":3,
       "operator": "or",
      fields: ["name.lowercase_space", "author"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  )
 body
  .orQuery(
    'multi_match',
    {
      query: val.toLowerCase(),
	 // "type":       "phrase_prefix",
       fuzziness: 0.7,
	   "slop":3,
      //prefix_length: 0,
      fields: ["name.lowercase_space", "author"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  )
 //body.query('multiMatch', [ 'name.lowercase_space', "author"], "дэн", "phrase_prefix")
/*
body.orQuery("bool", (f) => { 
	
   	f.orQuery("multiMatchQuery", 'name.lowercase_space', {"query": val.toLowerCase()})
   // f.orQuery("match", 'author', {"query": val.toLowerCase()})
    return f;
});*/
	//console.log("DEB"+JSON.stringify(body.build()));
	//console.log(Search.getLastReq());
	if(author != 'not_set'){
		body.filter('term', 'author', author);
		body.filter('term', 'author_t', author);
		body.filter('term', 'author_b', author);

	}
	//body.filter('term', 'author', "Пушкин");
	//return callback(body.build());
	
	this.client
		.search(
			{
				type: "catalog",
				catalog:'catalog',	
				body:body.build()
			})
		.then(function(data){
			if(val.toLowerCase() == Search.getLastReq().toLowerCase()){		
				//console.log(JSON.stringify(data));
				return callback(data);
			}
		
		});
	//var query 
	//console.log(param);
	//return callback({});
	}
}
	
	
	

	
return new Search();
});
