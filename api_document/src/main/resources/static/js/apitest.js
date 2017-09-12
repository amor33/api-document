$(function(){
    var datatable = $('#apitest').DataTable({
        "ajax": {
        		"url":"apitestlist",
        },
        "paging": false,
        "columns": [
        		{
        			"data": "name" 
        		},
        		{
        			"data": "code" 
        		},
        		{
        			"data": "url" 
        		},
        		{
        			"data": "type" 
        		},
        		{
        			"data": "count" 
        		},
            {
        			"data": "id",
        			render:function(data, type, row){
        				var btnCon = $("<div class='btn-group'>");
                    var btnEdit = $("<a class=' btn btn-sm btn-primary API-update' data-id="+data+"><i class='fa fa-edit'></i> 明细</a>");
                    var btRemove = $("<a class='btn btn-sm btn-default API-delete' data-id="+data+"><i class='fa fa-trash-o'></i>删除</a>");
                    	 btnCon.append(btnEdit);
                    	 btnCon.append(btRemove);
                    	return btnCon[0].outerHTML;
        			}
            }
        ],
        drawCallback: function() {
			$(".API-update").click(function(e){
				var id = $(e.target).attr("data-id");
				$("#mainId").val(id);
				$("#apitestdetail").DataTable().ajax.reload(null,false);
				detail();
			});
			$(".API-delete").click(function(e){
				var id = $(e.target).attr("data-id");
				$.ajax({
					url:"apitestdelete",
					method:"POST",
					dataType:"json",
					data:{
						id:id
					},
					success:function(data){
						datatable.ajax.reload(null,false);
						alert("成功删除"+data+"条数据.");
					}
					
				})
			});
		}
    });
    var datatableDetail = $('#apitestdetail').DataTable({
        "ajax": {
        		url:"apitestdetail",
        		data:function(){
        			return {
        				"mainId":$("#mainId").val()
        			}
        		}
        },
        "paging": false,
        "columns": [
        		{
        			"data": "start" ,
        			render: function(data, type, row) {
        				return moment(data).format("YYYY-MM-DD HH:mm:ss");
        			}
        		},
        		{
        			"data": "end" ,
        			render: function(data, type, row) {
        				return moment(data).format("YYYY-MM-DD HH:mm:ss");
        			}
        		},
        		{
        			"data": "time"
        		},
        		{
        			"data": "status" 
        		},
        		{
        			"data": "result" 
        		}
        ],
    });
});
function removeParams(){
	var dom = $(event.target);
	dom.parents("tr").remove();
}
function addParams(){
	$("#paramstbody").append($("#example tr")[0].outerHTML)
}
function save(){
	
	var saveTexts = $("#paramsForm input.required");
	for(var i = 0 ;i<saveTexts.length ; i++){
		if($(saveTexts[i]).val() == ""){
			alert($(saveTexts[i]).attr("placeholder")+"不能为空!");
			return false;
		}
	}
	$.ajax({
        url: "checkcode",
        type: "post",
        data: {
        		apiId : $("#id").val(),
        		code : $("#code").val()
        },
        success: function(data){
        		if(data){
        			alert("code重复，请修改重试");
        			return false;
        		}
	        	var saveDate = getFormJson("#paramsForm");
	        	if(Object.prototype.toString.call(saveDate.key)=='[object Array]'){
	        		saveDate.key = JSON.stringify(saveDate.key);
	        		saveDate.value = JSON.stringify(saveDate.value);
	        	}
	        	$.ajax({
	                url: "apitestsave",
	                type: "post",
	                data: saveDate,
	                success: function(data){
	                		list();
	                		if(data && data=="OK"){
	                			alert("操作成功，请稍后查看结果")
	                		}
	                }
	        	});
        }
	});
	
}
function getFormJson(frm) {
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}
function add(){
	cleanForm();
	
	$(".J-grid").addClass("hidden");
	$(".J-form").removeClass("hidden");
	$("#addButton").addClass("hidden");
	$("#returnButton").removeClass("hidden");
}
function detail(){
	$(".J-grid").addClass("hidden");
	$("#addButton").addClass("hidden");
	$(".J-gridDetail").removeClass("hidden");
	$("#returnButton").removeClass("hidden");
	$("#excelButton").removeClass("hidden");
}
function cleanForm(){
	var saveTexts = $("#paramsForm input");
	for(var i = 0 ;i<saveTexts.length ; i++){
		$(saveTexts[i]).val("");
	}
	$("#result").val("");
	$("#paramstbody").html("");
}
function excel(){
	var url = "excel?mainId="+$("#mainId").val();
	window.open(url);
}
function list(){
	$("#apitest").DataTable().ajax.reload(null,false);
	$(".J-grid").removeClass("hidden");
	$(".J-form").addClass("hidden");
	$(".J-gridDetail").addClass("hidden");
	$("#addButton").removeClass("hidden");
	$("#returnButton").addClass("hidden");
	$("#excelButton").addClass("hidden");
}
