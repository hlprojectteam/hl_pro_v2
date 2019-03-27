var index;
layui.use('layer', function(){
	index = parent.layer.getFrameIndex(window.name); //获取窗口索引
});
var row_Id_ = "";//表单自定义按钮替换使用
/**
 * 初始化页面
 */
$(document).ready(function(){
	$("div[name='searchDiv']").each(function(){  
		$(this).css("display","none");//搜索行初始化为隐藏
	}); 
	if($("#baseForm").length>0)
		$("#baseForm").validate();//初始化验证
});

//获得当前iframe对象
var iframeIndex;
for (var i = 0; i < parent.$(".J_iframe").length; i++) {
	if(parent.$(".J_iframe")[i].style.display!="none"){
		iframeIndex = parent.$(".J_iframe")[i].contentWindow;
	}
}


//更多查询条件
function on_more(obj){
	if($(obj).html().indexOf("left")>-1){
		$(obj).html('<i class="fa fa-angle-double-down"></i>&nbsp;隐藏');
		$("div[name='searchDiv']").each(function(){  
			$(this).css("display","block");	  
			$(this).css("padding-top","5px");	  
		});
	}else{
		$(obj).html('<i class="fa fa-angle-double-left"></i>&nbsp;更多');
		$("div[name='searchDiv']").each(function(){  
			$(this).css("display","none");	  
		});
	}
}

/*
 * 表单字段转换
 */
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
};

/**
 * 传递请求参数(列表分页查询)
 * @param params
 * @returns
 */
function queryParams(params) {
	if($("#baseForm").length>0){
		var form = document.getElementById('baseForm');
		var data = getFormJson(form);
		var json = eval('('+(JSON.stringify(params)+JSON.stringify(data)).replace(/}{/,',')+')');//参数组合		
		return json;
	}else{
		return params;
	}
}

/**
 * 删除列表数据
 */
function delete_tableData(table,url,refreshUrl){
	var rows = $("#"+table).bootstrapTable("getSelections");
	if(rows.length>0){
		parent.layer.confirm("确定删除所选数据？", {
			btn: ["确定","取消"] //按钮
		}, function(){
	  		var ids = [];
			$.each(rows, function(index, item){
				ids.push(item.id);
			});
			//装配url
			if(url.indexOf("?")>-1){
				var urlz = url.split("?");		
				url = urlz[0]+'?ids='+ids+"&"+urlz[1];
			}else{
				url = url+'?ids='+ids;
			}
			$.ajax({
				type:"post",
				async:false,
				dataType : "json",
				url: url,
				success : function(data){
					if(data.result){
						$("#"+table).bootstrapTable("refresh",{url:refreshUrl});//加载树下的列表
						autoMsg("删除成功！",1);
					}else{
						autoMsg("删除失败！",1);
					}
				},
				error : function(result){
					autoAlert("系统出错",5);
				}
			});
		});
	}else{
		autoAlert("请选择删除的数据",5);
	}
}

/**
 * 数据字典翻译
 * @param dict 字典名
 * @param key 
 * @returns {String}
 */
function changeDataDictByKey(dict,key){
	var state ="";
	$.ajax({
		type:"post",
		async:false,
		dataType:"text",
		url: '/urms/changeDataDictByKey?dict='+dict+'&key='+key,
		success : function(data){
			if(data!=""&&data!=null&&data!="null")
				state = data;
		}
	});
	return state;
}

/**
 * 获得字典值
 * @param dict 字典名
 * @returns {Array} 字典键值对数组
 */
function getDataDict(dict){
	var dataDict = new Array();
	$.ajax({
		type:"post",
		async:false,
		dataType:"json",
		url: '/urms/getDataDict?dict='+dict,
		success : function(data){
			dataDict[0] = data.keys;
			dataDict[1] = data.values;
		}
	});
	return dataDict;
}

//获得字典值
function getDict(dict){
	var dataDict = "";
	$.ajax({
		type:"post",
		async:false,
		dataType:"json",
		url: '/urms/getDict?dict='+dict,
		success : function(data){
			dataDict = data.dict;
		}
	});
	return dataDict;
}

/**
 * 子系统翻译
 * @param sysCode 子系统编码
 * @returns {String}
 */
function changeSubsystem(sysCode){
	var sysName ="";
	$.ajax({
		type:"post",
		async:false,
		dataType:"text",
		url: '/urms/changeSubsystem?sysCode='+sysCode,
		success : function(data){
			sysName = data;
		}
	});
	return sysName;
}

//---------------------二级联动begin--------------------------------------------------
/**
 * categoryCode:	一级目录名称
 * id:	select的id
 * name:	select的name
 * isChoose:	是否请选择
 * showId:	显示在哪个div上的id
 * secondId:	联动的select的id
 * secondName：	 联动的select的name
 * secondIsChoose:	是否请选择
 * secondShowId  显示在哪个联动的div上的id
 */
function linkage2(categoryCode,id,name,isChoose,showId,secondId,secondName,secondIsChoose,secondShowId){
	$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+categoryCode,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+id+'" name="'+name+'" onclick="on_secondLinkage(this,\''+secondId+'\',\''+secondName+'\','+secondIsChoose+',\''+secondShowId+'\')" class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(isChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}	
					html+='</select>';
					$("#"+showId).html(html);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
}

//点击一级后二级联动 改变select
function on_secondLinkage(selectObj,secondId,secondName,secondIsChoose,secondShowId){
	var key = $(selectObj).find("option:selected").val();
	if(key!=null&&key!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+key,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+secondId+'" name="'+secondName+'" onclick="" class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(secondIsChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}
					html+='</select>';
					$("#"+secondShowId).html(html);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}
//----------------------二级联动------------------------------------------------------------

//------------------------三级联动--------------------------------------------------------------
/**
 * categoryCode:	一级目录名称
 * id:	select的id
 * name:	select的name
 * isChoose:	是否请选择
 * showId:	显示在哪个div上的id
 * firstIsMust 是否必填
 * secondId:	联动的select的id
 * secondName：	 联动的select的name
 * secondIsChoose:	是否请选择
 * secondShowId  显示在哪个联动的div上的id
 * secondIsMust 是否必填
 * thirdId:	联动的select的id
 * thirdName：	 联动的select的name
 * thirdIsChoose:	是否请选择
 * thirdShowId  显示在哪个联动的div上的id
 * thirdIsMust 是否必填
 */
function linkage3(categoryCode,id,name,isChoose,showId,firstIsMust,secondId,secondName,secondIsChoose,secondShowId,secondIsMust,thirdId,thirdName,thirdIsChoose,thirdShowId,thirdIsMust){
	$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+categoryCode,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+id+'" name="'+name+'" onclick="on_secondLinkage3(this,\''+secondId+'\',\''+secondName+'\','+secondIsChoose+',\''+secondShowId+'\',\''+secondIsMust+'\',\''+thirdId+'\',\''+thirdName+'\','+thirdIsChoose+',\''+thirdShowId+'\',\''+thirdIsMust+'\')"'; 
					if(firstIsMust)
						html += ' data-rule-required="true" ';//是否必填
					html += ' class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(isChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}	
					html+='</select>';
					$("#"+showId).html(html);
				}else{
					$("#"+showId).html("");
					$("#"+secondShowId).html("");
					$("#"+thirdShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
}

//点击一级后二级联动 改变select
function on_secondLinkage3(selectObj,secondId,secondName,secondIsChoose,secondShowId,secondIsMust,thirdId,thirdName,thirdIsChoose,thirdShowId,thirdIsMust){
	var key = $(selectObj).find("option:selected").val();
	if(key!=null&&key!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+key,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+secondId+'" onclick="on_thirdLinkage3(this,\''+thirdId+'\',\''+thirdName+'\','+thirdIsChoose+',\''+thirdShowId+'\',\''+thirdIsMust+'\')" name="'+secondName+'" onclick="" ';
					if(secondIsMust)
						html += ' data-rule-required="true" ';//是否必填
					html += ' class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(secondIsChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}
					html+='</select>';
					$("#"+secondShowId).html(html);
				}else{
					$("#"+secondShowId).html("");
					$("#"+thirdShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}

//点击二级后三级联动 改变select
function on_thirdLinkage3(selectObj,thirdId,thirdName,thirdIsChoose,thirdShowId,thirdIsMust){
	var key = $(selectObj).find("option:selected").val();
	if(key!=null&&key!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+key,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+thirdId+'" name="'+thirdName+'" onclick="" ';
					if(thirdIsMust)
						html += ' data-rule-required="true" ';//是否必填
					html += ' class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(thirdIsChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}
					html+='</select>';
					$("#"+thirdShowId).html(html);
				}else{
					$("#"+thirdShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}
//--------------------------三级联动------------------------------------------------------------------


//------------------------四级联动--------------------------------------------------------------
/**
 * categoryCode:	一级目录名称
 * id:	select的id
 * name:	select的name
 * isChoose:	是否请选择
 * showId:	显示在哪个div上的id
 * secondId:	联动的select的id
 * secondName：	 联动的select的name
 * secondIsChoose:	是否请选择
 * secondShowId  显示在哪个联动的div上的id
 * thirdId:	联动的select的id
 * thirdName：	 联动的select的name
 * thirdIsChoose:	是否请选择
 * fourthShowId  显示在哪个联动的div上的id
 * fourthId:	联动的select的id
 * fourthName：	 联动的select的name
 * fourthIsChoose:	是否请选择
 * fourthShowId  显示在哪个联动的div上的id
 */
function linkage4(categoryCode,id,name,isChoose,showId,secondId,secondName,secondIsChoose,secondShowId,thirdId,thirdName,thirdIsChoose,thirdShowId,fourthId,fourthName,fourthIsChoose,fourthShowId){
	$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+categoryCode,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+id+'" name="'+name+'" onclick="on_secondLinkage4(this,\''+secondId+'\',\''+secondName+'\','+secondIsChoose+',\''+secondShowId+'\',\''+thirdId+'\',\''+thirdName+'\','+thirdIsChoose+',\''+thirdShowId+'\',\''+fourthId+'\',\''+fourthName+'\','+fourthIsChoose+',\''+fourthShowId+'\')" class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(isChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}	
					html+='</select>';
					$("#"+showId).html(html);
				}else{
					$("#"+showId).html("");
					$("#"+secondShowId).html("");
					$("#"+thirdShowId).html("");
					$("#"+fourthShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
}

//点击一级后二级联动 改变select
function on_secondLinkage4(selectObj,secondId,secondName,secondIsChoose,secondShowId,thirdId,thirdName,thirdIsChoose,thirdShowId,fourthId,fourthName,fourthIsChoose,fourthShowId){
	var key = $(selectObj).find("option:selected").val();
	if(key!=null&&key!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+key,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+secondId+'" onclick="on_thirdLinkage4(this,\''+thirdId+'\',\''+thirdName+'\','+thirdIsChoose+',\''+thirdShowId+'\',\''+fourthId+'\',\''+fourthName+'\','+fourthIsChoose+',\''+fourthShowId+'\')" name="'+secondName+'" onclick="" class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(secondIsChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}
					html+='</select>';
					$("#"+secondShowId).html(html);
				}else{
					$("#"+secondShowId).html("");
					$("#"+thirdShowId).html("");
					$("#"+fourthShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}

//点击二级后三级联动 改变select
function on_thirdLinkage4(selectObj,thirdId,thirdName,thirdIsChoose,thirdShowId,fourthId,fourthName,fourthIsChoose,fourthShowId){
	var key = $(selectObj).find("option:selected").val();
	if(key!=null&&key!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+key,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+thirdId+'" onclick="on_fourthLinkage4(this,\''+fourthId+'\',\''+fourthName+'\','+fourthIsChoose+',\''+fourthShowId+'\')" name="'+thirdName+'" onclick="" class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(thirdIsChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}
					html+='</select>';
					$("#"+thirdShowId).html(html);
				}else{
					$("#"+thirdShowId).html("");
					$("#"+fourthShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}

//点击三级后四级联动 改变select
function on_fourthLinkage4(selectObj,fourthId,fourthName,fourthIsChoose,fourthShowId){
	var key = $(selectObj).find("option:selected").val();
	if(key!=null&&key!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/queryCategoryByCode?categoryCode='+key,
			success : function(data){
				if(data!=null&&data!=""){
					var html = '<select id="'+fourthId+'" name="'+fourthName+'" onclick="" class="form-control">';
					for (var i = 0; i < data.length; i++) {
						var obj = data[i];
						if(i==0){
							if(fourthIsChoose)
								html+='<option value="">---请选择---</option>';
						}
						html+='<option value="'+obj.categoryCode+'">'+obj.categoryName+'</option>';
					}
					html+='</select>';
					$("#"+fourthShowId).html(html);
				}else{
					$("#"+fourthShowId).html("");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}
//--------------------------四级联动------------------------------------------------------------------

//通过组织树tree 选择组织 回调tree.id返回的input id tree.name返回的input id tree.code返回的input id 
function on_select(id,name,code){
	parent.parent.layer.open({
        type: 2,
        title: "选择组织",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["40%", "60%"],
        content: "/urms/orgFrame_tree?winName="+window.name+"&id="+id+"&name="+name+"&code="+code
    });
}
/**
 * 
 * 获得当前登录用户有关人员
 * @param type
 * @returns {String}
 */
function getDeptUser(type){
	var array = new Array();
	$.ajax({
		type:"post",
		async:false,
		dataType:"json",
		url: '/urms/user_getDeptUser?type='+type,
		success : function(data){
			array[0] = data.ids;
			array[1] = data.names;
		}
	});
	return array;
}

/**
 * @param id 回填id
 * @param name 回填name
 * @param selectNum 单选:single 多选:many
 */
function choose_user(id,name,selectNum){
	parent.layer.open({
        type: 2,
        title: "用户",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/user_chooseUser?winName="+window.name+"&id="+id+"&name="+name+"&selectNum="+selectNum
    });
}

/** 人、组织、角色 一起的页面
 * id:回填的id的id； name：回填name的id；type回填type的id，定义1：用户 2：组织 3：角色;selectNum 多选还是单选
 */
function on_sendOut(id,name,type,selectNum){
	parent.layer.open({
        type: 2,
        title: "选择发送人、组织、角色",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/user_chooseType?winName="+window.name+"&selectNum="+selectNum+"&id="+id+"&name="+name+"&type="+type
    });
}

//------------------------工作流人员选择start------------------------------------------------------------
/**
 * @param id 回填id
 * @param name 回填name
 * @param selectNum 单选还是多选
 * @param showUserName 回填显示名字div ID
 */
function next_actor(id,name,selectNum,showUserName){
	parent.layer.open({
        type: 2,
        title: "下一处理人",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/chooseNextUser?winName="+window.name+"&id="+id+"&name="+name+"&selectNum="+selectNum+"&showUserName="+showUserName
    });
}

/** 选择参与者 已配置某些参与者选择
 * @param id 回填id
 * @param name 回填name
 * @param selectNum 单选还是多选
 * @param actorIds
 * @param showUserName 显示名字div ID
 */
function actor(id,name,selectNum,actorIds,showUserName){
	parent.layer.open({
        type: 2,
        title: "下一处理人选择",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/user_chooseActor?winName="+window.name+"&id="+id+"&name="+name+"&selectNum="+selectNum+"&actorIds="+actorIds+"&showUserName="+showUserName
    });
}

/** 选择角色参与者
 * @param id
 * @param name
 * @param selectNum
 * @param roleIds
 */
function actor_role(id,name,selectNum,roleIds){
	parent.layer.open({
        type: 2,
        title: "下一处理人选择",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/role_roleActor?winName="+window.name+"&id="+id+"&name="+name+"&selectNum="+selectNum+"&roleIds="+roleIds
    });
}
/**
 * @param id 回填id
 * @param name 回填name
 * @param selectNum 单选还是多选
 */
function orgFrame_actor(id,name,selectNum,orgFrameIds){
	parent.layer.open({
        type: 2,
        title: "下一处理人选择",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/user_orgFrameActor?winName="+window.name+"&id="+id+"&name="+name+"&selectNum="+selectNum+"&orgFrameIds="+orgFrameIds
    });
}
//------------------------工作流人员选择end------------------------------------------------------------

//选择角色
function choose_role(id,name){
	parent.layer.open({
        type: 2,
        title: "角色",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/role_chooseRole?winName="+window.name+"&id="+id+"&name="+name
    });
}

//通过角色选择用户
function role_user(id,name){
	parent.layer.open({
        type: 2,
        title: "角色用户",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/role_roleUser?winName="+window.name+"&id="+id+"&name="+name
    });
}

//选择组织架构
function choose_orgFrame(id,name){
	parent.layer.open({
        type: 2,
        title: "组织架构",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/orgFrame_choose?winName="+window.name+"&id="+id+"&name="+name
    });
}

/** 选择流程组装
 * @param id 回填id
 * @param name 回填name
 * @param selectNum 单选:single 多选:many
 */
function choose_flowAssemble(id,name,selectNum){
	parent.layer.open({
        type: 2,
        title: "选择流程",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/wf/choose_flowAssemble?winName="+window.name+"&id="+id+"&name="+name+"&selectNum="+selectNum
    });
}

//---------------字典树选择 参考地图标记 图层-----------------
/**
 * @param categoryCode 字典编码
 * @param id 回填id
 * @param name 回填name
 * @param selectNum 单选还是多选
 */
function choose_layer(categoryCode,id,name,selectNum){
	parent.layer.open({
        type: 2,
        title: "图层类型选择",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["65%", "80%"],
        content: "/urms/category_choose?winName="+window.name+"&categoryCode="+categoryCode+"&id="+id+"&name="+name+"&selectNum="+selectNum
    });
}

//自动按比例缩小图片by高度 ReSizePic RePicHeight:修改为您想显示的高度值
function RePicByHeight(ThisPic,RePicHeight){
    //============以下代码请勿修改==================================
    var TrueWidth = ThisPic.width;    //图片实际宽度
    var TrueHeight = ThisPic.height;  //图片实际高度
    var Multiple = TrueHeight / RePicHeight;  //图片缩小(放大)的倍数
    $(ThisPic).width(TrueWidth / Multiple);//图片显示的可视宽度
    $(ThisPic).height(RePicHeight);//图片显示的可视高度
}
//自动按比例缩小图片by宽度  RePicWidth:修改为您想显示的宽度值
function RePicByWidth(ThisPic,RePicWidth){
    //============以下代码请勿修改==================================
    var TrueWidth = ThisPic.width;    //图片实际宽度
    var TrueHeight = ThisPic.height;  //图片实际高度
    var Multiple = TrueWidth / RePicWidth;  //图片缩小(放大)的倍数
    $(ThisPic).width(RePicWidth);//图片显示的可视宽度
    $(ThisPic).height(TrueHeight / Multiple);//图片显示的可视高度
}

//建立一個可存取到該file的url 选择图片后马上预览
function getObjectURL(file) {
	var url = null ; 
	if (window.createObjectURL!=undefined) { // basic
		url = window.createObjectURL(file) ;
	} else if (window.URL!=undefined) { // mozilla(firefox)
		url = window.URL.createObjectURL(file) ;
	} else if (window.webkitURL!=undefined) { // webkit or chrome
		url = window.webkitURL.createObjectURL(file) ;
	}
	return url ;
}

//当前所有弹出窗口
function on_close(){
	parent.layer.close(index);
}


//---------------------------------工具类js方法-------------------------------------------------------------------
//Generate four random hex digits.
function S4() {
 return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
};
//生成uuid
function uuid() {
 return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
};