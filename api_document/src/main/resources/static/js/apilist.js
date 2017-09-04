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
						console.log(data)
						if(data){
							for(var i in data){
								$("#paramstbody").append($("#example tr")[0].outerHTML);
								for(var j in data[i]){
									$("#paramstbody:eq("+i+") [name='"+j+"']").val(data[i][j]);
								}
							}
							
						}
					}
					
				})
//				var formData = $("#api").DataTable().row(self.parents('tr')).data();
				
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
	var saveDate = getFormJson("#paramsForm");
	var paramsName = JSON.stringify(saveDate.paramsName);
	var paramsType = JSON.stringify(saveDate.paramsType);
	var exampleParams = JSON.stringify(saveDate.exampleParams);
	saveDate.paramsName = paramsName.substring(1,paramsName.length - 1 );
	saveDate.paramsType = paramsType.substring(1,paramsType.length - 1 );
	saveDate.exampleParams = exampleParams.substring(1,exampleParams.length - 1 );
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
	$("#paramstbody").html("")
}
function list(){
	$("#api").DataTable().ajax.reload(null,false);
	$(".J-grid").removeClass("hidden");
	$(".J-form").addClass("hidden");
	$("#addButton").removeClass("hidden");
}
