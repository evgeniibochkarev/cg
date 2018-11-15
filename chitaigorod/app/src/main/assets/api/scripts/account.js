define([ 'jquery'], function () {

	var account = {};
	account.checkAuth = function(callback){
		$.ajax({
                type: 'POST',
                url: '/personal/ajax_basket.php',
                data: {action: 'checkAuth'},
                //headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                dataType: "json",
            	success: function (data, status) {
					callback(data.auth);
				}
				
		});
	}
	account.auth = function(param, callback){
		//var login = param.login;
		//var pass  = param.pass;
		param.AUTH_FORM = "Y";
		param.TYPE = "AUTH";
		param.USER_REMEMBER = true;
		//param.USER_LOGIN 
		//param.USER_PASSWORD
		console.log("ooo");
		$.ajax({
            type: "POST",
            url: 'https://www.chitai-gorod.ru/login/auth.php?login=yes',
            data:  param,//JSON.stringify(param),
            //contentType: 'application/json',
        	//dataType: 'json',
			success:function (data) {		
				console.log(JSON.stringify(data))
				
				var parser = new DOMParser()
				var doc = parser.parseFromString(data, "text/html");
				
				var output = {};
				var isAuth =  doc.getElementById("sessid") != null;						
				//output.author = (doc.getElementsByClassName("product__author")[0]).innerHTML
				output.result = isAuth;
				callback(output);
			},
			error:function(data){
				console.log(JSON.stringify(data))
			}
		});
	}
	
	
	account.reg = function(param, callback){
			/*		var ajaxData = {};
			ajaxData.showForm = "Y";
			$.ajax({
				type: "post",
				data : ajaxData,
				url: "/login/registration.php",
				success: function (data) {
					console.log(data);
					
				}
				});*/
		param.register_submit_button = "Регистрация"
		$.ajax({
					type: "post",
					data : param,
					url: "/login/order.php",
					success: function (data) {  					  
						var result = JSON.parse(data);                           
						
						console.log(JSON.stringify(data));
						
						callback(result);		
						
					}
				})
	}

	account.getOrderHtml = function(callback){
		$.ajax({
					type: "post",
					//data : param,
					url: "/personal/order.php",
					dataType: "html",
					success: function (data) {  					  
						callback(data);
					/*var parser = new DOMParser();  
						var doc = parser.parseFromString(data, "text/html");

						
						//console.log(data);
						//console.log("lll")
						var out = doc.getElementsByTagName("script");
						for(var i = 0; i< out.length; i++){
							console.log("DEB"+out[i].outerHTML);
						}*/
					}
				});
	}
	return account;
})
