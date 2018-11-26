var API = 
	{
		item:{},
		cart:{},
		search:{},
		city:{},
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

define(['item', 'search', 'city', 'cart', 'account', 'jquery'], 	function(item, search, city, cart, account) {


API.cart.getDataCart = 
	function( param){
		item.getDataCart(param, function(data){
			console.log("MAGIC{ method:'getDataCart', data:"+JSON.stringify(data)+"}");
		});
	};
	


API.item.getOtherData = 
	function( param){
			item.getOtherData(param, function(data){			
				console.log("MAGIC{ method:'getOtherData', data:"+JSON.stringify(data)+"}");
			});				
	};
API.search.getDataSearch = 
function(param){
			search.getDataSearch(param, function(data){
				API.search.getOtherData(data);
				console.log("MAGIC{ method:'getDataSearch', data:"+JSON.stringify(data)+"}");
			});				
	};
	
API.search.getOtherData = 
	function( param){
			search.getOtherData(param, function(data){			
				console.log("MAGIC{ method:'getOtherData', data:"+JSON.stringify(data)+"}");
			});				
	};
API.search.getDataAuthorList =
	function(param){
		search.getAuthorList(param, function(data){
			console.log("MAGIC{ method:'getDataAuthorList', data:"+JSON.stringify(data.aggregations)+"}");
		});
	};
	
API.search.getDataCategoryList =
	function(param){
		search.getDataCategoryList(param, function(data){
			console.log("MAGIC{ method:'getDataCategoryList', data:"+JSON.stringify(data.aggregations)+"}");
		});
	};
API.city.getDataSuggCity =
function(param){
		city.getSugg(param,	function(data){
			console.log("MAGIC{ method:'getDataSuggCity', data:"+JSON.stringify(data)+"}");
		});
	};
API.city.setCity = 
	function(param){
		city.setCity(param);
	};

API.item.ItemViewerFragment_getProduct = 
function(id){
$.ajax({
	url:"https://search.chitai-gorod.ru/catalog/catalog/"+id,
	
	success:function(ajaxOut){
		var param = ajaxOut._source;
		
		item.ItemViewerFragment_getProduct(param, function(data){
			
			console.log("MAGIC{ method:'ItemViewerFragment_getProduct', item:"+JSON.stringify(ajaxOut)+" ,data:"+JSON.stringify(data)+"}")
		});
		item.getOtherData(param, function(outer){
				//console.log(JSON.stringify(outer));
				console.log("MAGIC{ method:'ItemViewerFragment_getOtherData', item:"+JSON.stringify(ajaxOut)+" ,data:"+JSON.stringify(outer)+"}")
		});
		
	}
})
}
API.item.addToBasket = function(param){
	item.addToBasket(param, function(data){
		API.item.ItemViewerFragment_getProduct(param.id)
		API.cart.getDataCart(null);
	})
}

API.CartFragment={};
API.CartFragment.getBasket = function(param){
	param = {action: 'get_basket'}
	cart.getDataCart(param, function(data){
		console.log("MAGIC{ method:'CartFragment.getBasket', data:"+JSON.stringify(data)+"}")
	});
}

API.CartFragment.onGoOrder = function(param){
	account.checkAuth(function(isAuth){
		console.log(isAuth);
		if(isAuth == "true"){		
			
		$.ajax({
			method: 'POST',
            url: '/personal/ajax_basket.php',                 
			data: {action: 'delete', id: param.forDel},
			success: function(data){
				var ajaxData = {action: 'update', items: param};
               
				$.ajax({
                    method: 'POST',
                    url: '/personal/ajax_basket.php',
                    data: JSON.stringify(  ajaxData),
                   // contentType: 'application/json',
                    //dataType: "json",
                    success: function (data, status) {
                   // $scope.getData();
				   console.log("DEB"+JSON.stringify(data));
				   console.log("DEB"+JSON.stringify(param))
					console.log("MAGIC{ method:'CartFragment.showOrder', data:{} }");
					}
                })
  
			}
		});
		      
		}else{
			console.log("MAGIC{ method:'CartFragment.showAuth', data:{} }");
		}
	});
}
API.AuthDialog = {};

API.AuthDialog.auth = function(param){
	account.auth(param, function(data){
		//console.log("MAGIC{ method:'ItemViewerFragment_getProduct', data:"+JSON.stringify(data)+"}
		console.log("MAGIC{ method:'AuthDialog.auth', data:"+JSON.stringify(data)+"}");
	})
}

API.AuthDialog.reg = function(param){
	account.reg(param, function(data){
		//console.log("MAGIC{ method:'ItemViewerFragment_getProduct', data:"+JSON.stringify(data)+"}
		
		//API.AuthDialog.auth({"USER_LOGIN": param.REGISTER_LOGIN, "USER_PASSWORD": param.USER_PASSWORD});
		console.log("MAGIC{ method:'AuthDialog.reg', data:"+JSON.stringify(data)+"}");
	})
}
	
API.OrderFragment = {};
API.OrderFragment.getPage = function(param){
	account.getOrderHtml(function(data){
		console.log("MAGIC{ method:'OrderFragment.getOrderPage', data:"+JSON.stringify(data)+"}")
						
	});
}
API.MainPageFragment = {};

API.MainPageFragment.lastHtml = null;

API.MainPageFragment.getHtml = function(param){
if(API.MainPageFragment.lastHtml == null){
	$.ajax({
					type: "post",
					//data : param,
					url: "/index.php",
					dataType: "html",
					success: function (data) {  					  
						//callback(data);
					var parser = new DOMParser();  
						var doc = parser.parseFromString(data, "text/html");

						var title = doc.getElementsByClassName("slider__title");
						var body = doc.getElementsByClassName("slider__viewport");
						var arr = [];
						
						for(var i = 0; i< title.length; i++){
							var arrBook = [];
							var books = body[i].getElementsByClassName("product-card");
							
							for(var j = 0; j < books.length; j++){
								var product = {};
								product.id = books[j].getAttribute("data-product");
								
								product.name = books[j].getElementsByClassName("product-card__title")[0].innerHTML.replace (/(\r\n\t|\n|\t|\s\s|\r\t)/gm,"")    
								product.img = books[j].getElementsByTagName("img")[0].getAttribute("src")
								product.author = books[j].getElementsByClassName("product-card__author")[0].innerHTML.replace (/(\r\n\t|\n|\t|\s\s|\r\t)/gm,"")
								product.price = books[j].getElementsByClassName("product-card__price")[0].textContent.replace (/(\r\n\t|\n|\t|\s\s|\r\t)/gm,"")
													
								arrBook.push(product);
							}
							arr.push( {title:title[i].textContent,  body: arrBook});
						}
						API.MainPageFragment.lastHtml = arr;
						console.log("MAGIC{ method:'MainPageFragment.getHtml', data:"+JSON.stringify(arr)+"}");
					}
				});
}else{
	console.log("MAGIC{ method:'MainPageFragment.getHtml', data:"+JSON.stringify(API.MainPageFragment.lastHtml)+"}")
}
}

API.CabinetFragment = {}
API.CabinetFragment.getProfilePage = function(data){
	account.checkAuth(function(isAuth){
		if(isAuth == "true"){		
			console.log("MAGIC{ method:'CabinetFragment.hideAuthBtn', data:{} }")
			$.ajax({
			method: 'POST',
            url: 'https://www.chitai-gorod.ru/profile/orders/',                 
			//data: {action: 'delete', id: param.forDel},
			success: function(data){
				console.log("MAGIC{ method:'CabinetFragment.showAuthq', data:"+data+" }")
			}
			})
		}else{
			console.log("MAGIC{ method:'CabinetFragment.showAuth', data:{} }")
		}
	});
}

API.MainPageFragment.getHtml(null);
console.log("MAIN{method:'jsIsLoaded'}");
city.checkCity();
API.cart.getDataCart(null);




/*
var getCatalog = function(id){
			var itog = [];
			var mystr;
			//var itog = {'id' : id, 'text' : "", 'sub' : []};
			$.ajax({
					url: 'https://www.chitai-gorod.ru' + id,
					async: false,
					success: function (data) {
						var parser = new DOMParser()
						var doc = parser.parseFromString(data, "text/html");

						
		
			
						var container = doc.getElementsByClassName("container__leftside");
				
						// список <a
						var el = container[0].getElementsByClassName("navigation__link");
						
						
						console.log(el.length);
						if(el.length == 0) return [itog, ""];
						
						
						
						[].forEach.call(el, function(ele){
							var _text = ele.innerHTML;
							var _id  = ele.getAttribute("href");
							
							var _sub = getCatalog(_id)
							
								console.log("  "+_id + "   "+_text+" " + JSON.stringify(_sub[0]));
								itog.push({'id':_id,'text': _text, 'sub' : _sub[0]});
							
								mystr += ".addSubCategory(new EntryCategory("+ _id + ", "+ _text+")"+_sub[1]+")" 
							//console.log("DEB"+JSON.stringify(_text));
						
				
							
						});
						
							//mystr += ")"

											
						//$(".citySuggest li").remove();
						//$(".citySuggest").append(data);
					}
				})
				
			return [itog, mystr];

}
var tt = getCatalog('/catalog/books/9154')
	
	 console.log("DEB"+JSON.stringify(tt[1]));

*/

//API.item = Object.assign(item, search);
//API.item.getDataCart("ffg", {query: "иван"});
});

//API.search.get('вор', {page:0,});
