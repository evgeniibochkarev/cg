var API = 
	{
		item:{}
	};
requirejs.config({
  paths: {
	'jquery': 'https://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min',
    'elasticsearch': 'elasticsearch.min',
  	'elastic':'https://www.chitai-gorod.ru/search/elasticsearch-js/elastic',
	'bodybuilder':'bodybuilder.min',
},
  shim:{
	 bodybuilder:{
		 exports: 'elastic'
	 },
	 elastic: {
		 deps: ['elasticsearch'],
		 exports: 'elastic'
	 },
	 jquery: {
		 exports: '$'
	 }
  }
});
 /*
API.search =
{
	get:
		function(query, param){
			define(['search'], 	function(search) {
					search.get(query, param);
			}
		);}
}*/

define(['item', 'search'], 	function(item, search) {


API.item.getDataCart = 
	function(ctxId, param){
		item.getDataCart(param, function(data){
			console.log("MAGIC{ctxId:\'"+ctxId+"\', \"data\":"+JSON.stringify(data)+"}");
		});
	};
	
API.item.getDataSearchSugg = 
	function(ctxId, param){
			search.getDataSearchSugg(param, function(data){
				console.log("MAGIC{ctxId:'"+ctxId+"', data:"+JSON.stringify(data)+"}");
			});				
	};

API.item.getDataSearch = 
	function(ctxId, param){
			search.getDataSearch(param, function(data){
				console.log("MAGIC{ctxId:'"+ctxId+"', method:'getDataSearch', data:"+JSON.stringify(data)+"}");
			});				
	};

//API.item = Object.assign(item, search);
API.item.getDataCart("ffg", {query: "иван"});
});

//API.search.get('вор', {page:0,});
