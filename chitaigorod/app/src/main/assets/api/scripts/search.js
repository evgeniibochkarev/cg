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
	getDataCategoryList(param, callback){
	var body = bodybuilder();
	var val = param.query;
	
	body.query(
    'multi_match',
    {
      query: val.toLowerCase(),
	  "type":       "phrase_prefix",
       fuzziness: "auto",
	   "operator": "and",
	   boost:2,
	   "slop":3,
      //prefix_length: 0,
      fields: ["name.lowercase_space", "author_t", "author_b"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  ).size(100);
  
  body.agg("terms", "ibl_sec_id");
  
  
this.client
		.search(
			{
				type: "catalog",
				catalog:'catalog',	
				body:body.build()
			})
		.then(function(data){
			//console.log("MAGIC"+JSON.stringify(data));
			callback(data);
		});

}	

getAuthorList(param, callback){
	var body = bodybuilder();
	var val = param.query;
	if(param.method == "query"){
	body.query(
    'multi_match',
    {
      query: val.toLowerCase(),
	  "type":       "phrase_prefix",
       fuzziness: "auto",
	   "operator": "and",
	   boost:2,
	   "slop":3,
      //prefix_length: 0,
      fields: ["name.lowercase_space", "author_t", "author_b"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  ).size(100);

 
	}else{
		body.orQuery(
    'multi_match',
    {
      query: val.toLowerCase(),
	  "type":       "phrase_prefix",

       fuzziness: "auto",
	   "operator": "or",
	   
	    "slop":3,
      //prefix_length: 0,
      fields: ["author_t", "author_b"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  );
		body.orQuery(
    'multi_match',
    {
      query: val.toLowerCase(),
	  "type":       "phrase_prefix",
       fuzziness: 0.7,
	   "operator": "or",
	   boost:2,
	   "slop":3,
      //prefix_length: 0,
      fields: ["author_t", "author_b"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  ).size(100);
	}
	
	body.agg("terms", "author_t.raw")
	body.agg("terms", "author.raw")
	body.agg("terms", "author_b")
  
  
  this.client
		.search(
			{
				type: "catalog",
				catalog:'catalog',	
				body:body.build()
			})
		.then(function(data){
			//console.log("MAGIC"+JSON.stringify(data));
			callback(data);
		});
	//var query 
	//console.log(param);
	//return callback({});
}
getOtherData(param, callback){
		var info = [];
		(param.hits.hits).forEach(function(value){
			info.push({id: value._source.id, bookId: value._source.bid, section_id: value._source.ibl_sec_id});
		});
		//console.log(JSON.stringify(info));

		
		$.ajax({
            type: "POST",
            url: 'search/check_stock.php',
            data:  JSON.stringify(info),
            contentType: 'application/json',
        	dataType: 'json',
			success:function (data) {		
				//console.log(data);
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
	var category = filter.category;
	
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
      fields: ["name","name.lowercase_space", "author_t", "author"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
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
      fields: ["name.lowercase_space", "author_t", "author"]// ['Field1^1000', 'Field2^3', 'Field3^2', 'Field4']
    }
  )
 //body.orQuery("match", "name", val);
 //body.query('multiMatch', [ 'name.lowercase_space', "author"], "дэн", "phrase_prefix")
/*
body.orQuery("bool", (f) => { 
	
   	f.orQuery("multiMatchQuery", 'name.lowercase_space', {"query": val.toLowerCase()})
   // f.orQuery("match", 'author', {"query": val.toLowerCase()})
    return f;
});*/
	//console.log("DEB"+JSON.stringify(body.build()));
	//console.log(Search.getLastReq());
	//author = "Федор Достоевский";
	if(author != 'not_set'){
		//body.orFilter('prefix', 'author', author);
		body.andFilter('term', 'author_t.raw', author);
		//body.orFilter('term', 'author_b', author);
	}
	if(category != null){
		body.andFilter('term','ibl_sec_id', category);
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
