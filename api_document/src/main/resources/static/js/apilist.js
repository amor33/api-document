$(function(){
    var datatable = $('#api').DataTable({
        "ajax": 'list',
        "paging": false,
        "columns": [
        		{
        			"data": "project" 
        		},
        		{
        			"data": "name" 
        		},
        		{
        			"data": "code" 
        		},
        		{
        			"data": "type" 
        		},
            {
        			"data": "id",
        			render:function(data, type, row){
        				var btnCon = $("<div class='btn-group'>");
                    var btnEdit = $("<a class=' btn btn-sm btn-primary API-update' data-id="+data+"><i class='fa fa-edit'></i> 编辑</a>");
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
				add();
				var formData = $("#api").DataTable().row($(e.target).parents('tr')).data();
				
				for(var i in formData){
					$("#"+i).val(formData[i]);
				}
				
				$.ajax({
					url:"paramslist",
					method:"POST",
					dataType:"json",
					data:{
						apiId:formData.id
					},
					success:function(data){
						if(data){
							for(var i in data){
								$("#paramstbody").append($("#example tr")[0].outerHTML);
								for(var j in data[i]){
									$("#paramstbody tr:eq("+i+") [name='"+j+"']").val(data[i][j]);
								}
							}
							
						}
					}
					
				})
				
			});
			$(".API-delete").click(function(e){
				var id = $(e.target).attr("data-id");
				$.ajax({
					url:"delete",
					method:"POST",
					dataType:"json",
					data:{
						apiId:id
					},
					success:function(data){
						datatable.ajax.reload(null,false);
						alert("成功删除"+data+"条数据.");
					}
					
				})
			});
		}
    });
    	
    var str = "<option  value='政府补贴平台'></option>"
	$.ajax({
        url: "project",
        type: "post",
        success: function(data){
        		if(data && data.length > 0){
        			var str = "";
        			for(var i = 0; i < data.length ; i++ ){
        				str = str + "<option  value='" + data[i] + "'>"+ data[i] + "</option>";
        			}
        			$("#project").append(str);
        		}
        }
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
	        	if(saveDate.paramsName){
	        		saveDate.paramsName = JSON.stringify(saveDate.paramsName);
	        		saveDate.paramsType = JSON.stringify(saveDate.paramsType);
	        		saveDate.paramsDiscription = JSON.stringify(saveDate.paramsDiscription);
	        		saveDate.exampleParams = JSON.stringify(saveDate.exampleParams);
	        		saveDate.isRequired = JSON.stringify(saveDate.isRequired);
	        	}
	        	$.ajax({
	                url: "save",
	                type: "post",
	                data: saveDate,
	                success: function(data){
	                		list();
	                		if(data && data.id){
	                			alert("操作成功")
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
	
}
function cleanForm(){
	var saveTexts = $("#paramsForm input");
	for(var i = 0 ;i<saveTexts.length ; i++){
		$(saveTexts[i]).val("");
	}
	$("#result").val("");
	$("#paramstbody").html("");
}
function list(){
	$("#api").DataTable().ajax.reload(null,false);
	$(".J-grid").removeClass("hidden");
	$(".J-form").addClass("hidden");
	$("#addButton").removeClass("hidden");
}
