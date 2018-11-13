define([ 'jquery'], function () {

	var _cart = {};
	
	_cart.getDataCart = function(param, callback){
		var ajaxData = {action: 'get_basket' };
        
		$.ajax({
            type: "POST",
            url: '/personal/ajax_basket.php',
            data:  JSON.stringify(param),
            contentType: 'application/json',
        	dataType: 'json',
			success:function (data) {		
				callback(data.results);
			}
		});
	};
	
	return _cart;
});
