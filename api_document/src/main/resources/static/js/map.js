function getAddress(){
	var address = $("#address").val();
	var url = "https://api.map.baidu.com/geocoder?address="+address+"&output=json&qq-pf-to=pcqq.c2c";
	$.ajax({
        url: url,
        type: "get",
        dataType:"jsonp",
        jsonp:"callback",
        jsonpCallback:"success_jsonpCallback",
        success: function(data){
        		$("#info").text(JSON.stringify(data));
        },
		error:function(data,a,b,c){
			console.info();
		}
	});
}
function clear(){
	$("#address").val("");
	$("#info").text("");
}
function jsonpCallback(data){
	console.info(data);
}