define(['jquery', 'jquery.cookie'], function(){
var userCity = {
			param: {
				city: "Москва",
				cityId: "213",
				timer: 0,
				reloadPage: true
			},
			init: function () {
				userCity.geoPosition();
				
				/** click event on city*/
			},
			geoPosition: function () {
				/** Try get HTML5 geolocation */
				if (navigator.geolocation) {
					
					/** when the user has sent its position in Google */
					navigator.geolocation.getCurrentPosition(function (position) {
						var lat = position.coords.latitude;
						var lng = position.coords.longitude;
						$.ajax({
							tyle: "POST",
							url: "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=false&language=ru",
							success: function (data) {
								res = data.results[0].address_components;
								$.each(res, function (index, key) {
									if ($.inArray("locality", key.types) >= 0) {
										/********************************************
311										 *    send the name of the city,
312										 *    which received from Google and
										 *    return a list of cities which match
314										 ********************************************/
										userCity.showSelectCityBlock();
										userCity.showChangeCityBlock();
										//$('#citySearch').val(key.long_name);
										//$('#citySearch').keyup();
									}
								});
							}
						});
					});
	
									userCity.checkCity();
								}
				else {
					/** Browser doesn't support Geolocation. Set default value */
					userCity.agree();
				}
			},
			checkCity: function () {
			
				if (typeof $.cookie('cityName') == 'undefined') {
					/** if user first on site*/
					
					userCity.showSelectCityBlock();
					/** set timer for agree the default value */
					//userCity.timer = setTimeout(userCity.agree, 6000);
				}else {
					/** set user param from cookies*/
					//userCity.param.city = $.cookie('cityName');
					//userCity.param.cityId = $.cookie('cityId');
					//userCity.changeAllCityText();
				}
			},
			setCity: function (param) {
				$.cookie('cityName', param.cityName, {path: '/'});
				$.cookie('cityId', param.cityId, {path: '/'});
				//userCity.changeAllCityText();
	
								/** reload page if set city for not auth user */
					//location.reload();
				},
			saveCity: function () {

				/********************************************
356				 *    if user is auth, save to profile and
357				 *    the ID in the profile if it is
358				 ********************************************/
				if (parseInt(userCity.param.cityId) > 0) {
					$.ajax({
						type: "POST",
						url: "/api/kav/city.php",
						data: {city: userCity.param.cityId},
						success: function () {
							if (userCity.param.reloadPage) {
								location.reload();
							}
						}
					});
				}
			},
			agree: function () {
				/** when the user agrees to the shown city*/
				userCity.setCity();
				userCity.changeAllCityText();
				userCity.hideSelectCityBlock();
			},
			changeAllCityText: function () {
				/** first change on full city name*/
				/*$('.jsUserCity').each(function () {
					$(this).text(userCity.param.city);
				});
	
				/** then change on short city name if has class jsUserCityShort*/
				/*$('.jsUserCityShort').each(function () {
					$(this).text(userCity.param.city.substr(0, 20) + "...");
				});*/
			},
			autoloadCityListSelect: function (city_name, city_id) {
				/** click event on the city from the autoload list*/
				userCity.param.city = city_name;
				userCity.param.cityId = city_id;
	
				/*
				popCityBlock.hide();
				$("#citySearch").val("");
				$(".citySuggest li").remove();
	
				*/
				userCity.setCity();
			},
	        autoloadNewCityListSelect: function (city_name) {
	            /** click event on the new suggested city from the autoload list*/
	            $.ajax({
	                url: "/api/kav/city.php",
	                data: {city: city_name, check_city: true},
	                dataType: 'json',
	                success: function (data) {
	                    if (data.name) {
	                        userCity.param.city = data.name;
	                        userCity.param.cityId = data.id;
	
							/*
	                        popCityBlock.hide();
	                        $("#citySearch").val("");
	                        $(".citySuggest li").remove();
	
							*/
	                        userCity.setCity();
	                    } else console.log('select new city error',data);
	                }
	            });
	
	        },
			showSelectCityBlock: function () {
				console.log("MAIN{method:'showCityPickerDialog'}");
				/*
				userCity.param.reloadPage = true;
				
				$("#yourCity").text(userCity.param.city);
				$('.popup-city').find('.step2').removeClass('activeStep');
				$('.popup-city').find('.step1').addClass('activeStep');
				popCityBlock.show();*/
			},
			hideSelectCityBlock: function () {
				//popCityBlock.hide();
			},
			showChangeCityBlock: function () {
				$('.popup-city').find('.step1').removeClass('activeStep');
				$('.popup-city').find('.step2').addClass('activeStep');
				$('.citySuggest').slideDown(100);
				clearTimeout(userCity.timer);
			},
			
			getSugg: function (param, callback) {
				/** send ajax to search page and return html list of cities*/
				
				$.ajax({
					url: '/api/kav/city.php?city=' + decodeURIComponent(param.query),
					
					success: function (data) {
						var parser = new DOMParser()
						var doc = parser.parseFromString(data, "text/html");

//""userCity.autoloadCityListSelect('Тюмень', 55)"", source: https://www.chitai-gorod.ru/scripts/city.js (180)

						var el = doc.getElementsByTagName("li");
				
						var itog = [];
						for(i = 0; i < el.length; i++){
							var str = el[i].getAttribute("onclick")	
							str = str.slice( str.match(/\('/).index +2 );
							str = str.slice(0, -2);
							var out = str.split("', ");
							itog.push(out);
							//console.log(JSON.stringify(out));						
						}
							
						
						
						callback(itog);
						
						//$(".citySuggest li").remove();
						//$(".citySuggest").append(data);
					}
				});
			},
			
			
			
			findCity: function (city) {
				/** send ajax to search page and return html list of cities*/
				$.ajax({
					url: '/api/kav/city.php?city=' + decodeURIComponent(city),
					success: function (data) {
						//$(".citySuggest li").remove();
						//$(".citySuggest").append(data);
					}
				});
			}
		};
	
		function get_city_suggest(){
	        processOnTime(function () {
	            //userCity.findCity($('#citySearch').val());
	        },250);
	    }
	
	return userCity;
})
