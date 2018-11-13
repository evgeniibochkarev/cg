define([ 'jquery'], function () {

	var _item = {};
	
	_item.getDataCart = function(param, callback){
		var ajaxData = {action: 'get_basket' };
        
		$.ajax({
            type: "POST",
            url: '/personal/ajax_basket.php',
            data:  JSON.stringify(ajaxData),
            contentType: 'application/json',
        	dataType: 'json',
			success:function (data) {		
				callback(data.results);
			}
		});
	};
	
	_item.addToBasket = function (param, callback){
    var product_id = param.id;
	console.log(param.id);
    if (product_id > 0) {
        var url = '/bitrix/templates/t1/components/bitrix/catalog.section/books/add2basket.php';
        
		$.ajax({
			url: url,
		    method: "POST",
			data: {bookId: product_id},
			success: function (data) {
           		console.log(JSON.stringify(data));	   
				callback(null);
			}
		});
    }
	}
	
	_item.ItemViewerFragment_getProduct = function(param, callback){
		var id = param.id
		
		$.ajax({
            url: '/catalog/book/' + id,    
			success:function (data) {		
				var parser = new DOMParser()
				var doc = parser.parseFromString(data, "text/html");
				
				var output = {};
				output.name  = (doc.getElementsByClassName("product__title")[0]).innerHTML;						
				output.author = (doc.getElementsByClassName("product__author")[0]).innerHTML
				
				output.description = (doc.getElementsByClassName("product__description")[0]).getElementsByTagName("div")[1].innerHTML
				
				output.photos = [];
				var imgOut = doc.getElementsByClassName("gallery__slides-group")[0].getElementsByTagName("img");
				[].forEach.call(imgOut, function(imgHTML){
					output.photos.push(imgHTML.getAttribute("src"));
				})
				
				output.prop = []
				var propOut = doc.getElementsByClassName("product-prop");
				[].forEach.call(propOut, function(propHTML){
					output.prop.push({
						title: propHTML.getElementsByTagName("div")[0].textContent.replace(/(\r\n\t|\n|\t|\s\s|\r\t)/gm,""),
						value: propHTML.getElementsByTagName("div")[1].textContent.replace(/(\r\n\t|\n|\t|\s\s|\r\t)/gm,"")
					});
				});
				
				
				/*
				
				output.author = (doc.getElementsByClassName("product__author")[0]).innerHTML
				output.author = (doc.getElementsByClassName("product__author")[0]).innerHTML
*/
				
				callback(output);
			}
		});
		
		//callback( null);
	}

	_item.getOtherData = function(param, callback){
		var info = [];
		
			info.push({id: param.id, bookId: param.bid, section_id: param.ibl_sec_id});
		
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
	
	return _item;
});
