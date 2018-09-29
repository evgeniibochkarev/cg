var API = 
	{
		item:{},
		cart:{},
		search:{},
		city:{}
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

define(['item', 'search', 'city'], 	function(item, search, city) {


API.cart.getDataCart = 
	function( param){
		item.getDataCart(param, function(data){
			console.log("MAGIC{ method:'getDataCart', data:"+JSON.stringify(data)+"}");
		});
	};
	


API.search.getDataSearch = 
function(param){
			search.getDataSearch(param, function(data){
				API.search.getOtherData(data);
				console.log("MAGIC{ method:'getDataSearch', data:"+JSON.stringify(data)+"}");
			});				
	}
	
API.search.getOtherData = 
	function( param){
			search.getOtherData(param, function(data){
				
				console.log("MAGIC{ method:'getOtherData', data:"+JSON.stringify(data)+"}");
			});				
	};
	
API.city.getDataSuggCity =
function(param){
		city.getSugg(param,	function(data){
			console.log("MAGIC{ method:'getDataSuggCity', data:"+JSON.stringify(data)+"}");
		});
	}	
API.city.setCity = 
	function(param){
		city.setCity(param);
	}
	
console.log("MAIN{method:'jsIsLoaded'}");
city.checkCity();
//API.item = Object.assign(item, search);
//API.item.getDataCart("ffg", {query: "иван"});
});

//API.search.get('вор', {page:0,});
