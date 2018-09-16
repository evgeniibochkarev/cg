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
				callback(data);
			}
		});
	};
	
	return _item;
});
