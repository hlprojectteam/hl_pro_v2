/*
项目：流程设计器
官网：http://flowdesign.leipi.org
基本协议：apache2.0
由Dic二次开发  
*/
var defaults = {
		processData:{},//步骤节点数据
		fnRepeat:function(){
//			alert("步骤连接重复");
		},
		fnClick:function(){
//			alert("单击");
		},
		fnDbClick:function(){
//			alert("双击");
		},
		canvasMenus : {
			"one": function(t) {alert('画面右键')}
		},
		processMenus: {
			"one": function(t) {alert('步骤右键')}
		},
		convertMenus: {
			"one": function(t) {alert('流向右键')}
		},
		/*右键菜单样式*/
		menuStyle: {
			border: '1px solid #5a6377',
			minWidth:'150px',
			padding:'5px 0'
		},
		itemStyle: {
			fontFamily : 'verdana',
			color: '#333',
			border: '0',
			padding:'5px 40px 5px 20px'
		},
		itemHoverStyle: {
			border: '0',
			color: '#fff',//鼠标右键背景颜色
			backgroundColor: '#20B2AA'//鼠标右键选中颜色  
		},
		mtAfterDrop:function(params){
			//alert("连接："+params.sourceId +" -> "+ params.targetId);
		},
		//这是连接线路的绘画样式
		connectorPaintStyle : {
			lineWidth: 4,
            strokeStyle: "#61B7CF",//连接线颜色
            joinstyle: "round",
            outlineColor: "white",//连接线边缘颜色
            outlineWidth: 2//连接线边缘宽度
		},
		//鼠标经过样式
		connectorHoverStyle : {
			lineWidth:4,
			strokeStyle:"#da4f49",
	        outlineColor: "white",//连接线边缘颜色	
	        outlineWidth: 2,//连接线边缘宽度
		}
};/*defaults end*/

var initEndPoints;

(function($) {
	//连接线 样式
   initEndPoints = function(){
      $(".process-flag").each(function(i,e) {
          var p = $(e).parent();
          jsPlumb.makeSource($(e), {//只有1.3.x保留这方法
              parent:p,
              anchor:"Continuous",
              endpoint:[ "Dot", { radius:1 } ],
              //cornerRadius 连接线拐弯圆角 gap:偏移 
              connector:[ "Flowchart", { stub:[5, 5],gap:1,cornerRadius: 10,alwaysRespectStubs: true } ],
              connectorStyle:defaults.connectorPaintStyle,
              hoverPaintStyle:defaults.connectorHoverStyle,
              dragOptions:{},
              maxConnections:-1
          });
      });
  }
 
  /*设置隐藏域保存关系信息*/
  var aConnections = [];
  var setConnections = function(conn, remove) {
      if (!remove) aConnections.push(conn);
      else {
          var idx = -1;
          for (var i = 0; i < aConnections.length; i++) {
              if (aConnections[i] == conn) {
                  idx = i; break;
              }
          }
          if (idx != -1) aConnections.splice(idx, 1);
      }
      if (aConnections.length > 0) {
          var s = "";
          for ( var j = 0; j < aConnections.length; j++ ) {
              var from = $('#'+aConnections[j].sourceId).attr('process_id');
              var target = $('#'+aConnections[j].targetId).attr('process_id');
              s = s + "<input type='hidden' value=\"" + from + "," + target + "\">";
          }
          $('#process_info').html(s);
      } else {
          $('#process_info').html('');
      }
      jsPlumb.repaintEverything();//重画
  };

   /*Flowdesign 命名纯粹为了美观，而不是 formDesign */
   $.fn.Flowdesign = function(options){
        var _canvas = $(this);
        //右键步骤的步骤号  当前连接线
        _canvas.append('<input type="hidden" id="leipi_active_id" value="0"/><input type="hidden" id="leipi_convert_id" value="0"/><input type="hidden" id="leipi_copy_id" value="0"/>');
        _canvas.append('<div id="process_info"></div>');
        /*配置*/
        $.each(options, function(i, val) {
          if (typeof val == 'object' && defaults[i])
            $.extend(defaults[i], val);
          else 
            defaults[i] = val;
        });
        /*画布右键绑定*/
        var contextmenu = {
          bindings: defaults.canvasMenus,
          menuStyle : defaults.menuStyle,
          itemStyle : defaults.itemStyle,
          itemHoverStyle : defaults.itemHoverStyle
        }
        $(this).contextMenu('canvasMenu',contextmenu);//页面右键

        jsPlumb.importDefaults({
            DragOptions : { cursor: 'pointer'},
            EndpointStyle : { fillStyle:'#225588' },
            Endpoint : [ "Dot", {radius:1} ],
            ConnectionOverlays : [
                [ "Arrow", { location:1 } ],
                [ "Label", {
                        location:0.1,
                        id:"label",
                        cssClass:"aLabel"
                    }]
            ],
            Anchor : 'Continuous',
 //         HoverPaintStyle:defaults.connectorHoverStyle,//流程图鼠标经过连接线时颜色改变
            ConnectorZIndex:5
        });
//        if( $.browser.msie && $.browser.version < '9.0' ){ //ie9以下，用VML画图
//            jsPlumb.setRenderMode(jsPlumb.VML);
//        } else { //其他浏览器用SVG
            jsPlumb.setRenderMode(jsPlumb.SVG);
//        }

    //初始化原步骤
    var lastProcessId=0;
    var processData = defaults.processData;
    if(processData.process){
        $.each(processData.process, function(i,row){
            var nodeDiv = document.createElement('div');
            var nodeId = "window" + row.id, badge = 'badge-inverse',icon = 'icon-star';
            if(lastProcessId==0){//第一步
              badge = 'badge-info';
              icon = 'icon-play';
            }
            if(row.icon){
              icon = row.icon;
            }
            $(nodeDiv).attr("id",nodeId)
            .attr("style",row.style)
            .attr("process_to",row.process_to)
            .attr("process_id",row.id)
            .attr("process_type",row.process_type)//步骤类型
            .attr("process_code",row.process_code)//步骤编码
            .attr("countersign_type",row.countersign_type)//会签类型
            .attr("child_process_type",row.child_process_type)//子流程类型
            .attr("split_type",row.split_type)//会签类型
            .attr("join_type",row.join_type)//会签类型
            .attr("actor_ids",row.actor_ids)//参与者id
            .attr("actor_names",row.actor_names)//参与者名称
            .attr("role_ids",row.role_ids)//角色id
            .attr("role_names",row.role_names)//角色名称
            .attr("orgframe_ids",row.orgframe_ids)//组织架构id
            .attr("orgframe_names",row.orgframe_names)//组织架构名称
            .addClass("process-step")
            .html('<span class="process-flag badge '+badge+'"><i class="'+icon+' icon-white"></i></span>&nbsp;' + row.process_name )
            .mousedown(function(e){
              if( e.which == 3 ) { //右键绑定
                  _canvas.find('#leipi_active_id').val(row.id);
                  contextmenu.bindings = defaults.processMenus;
                  $(this).contextMenu('processMenu', contextmenu);
              }
            });
            _canvas.append(nodeDiv);
            //索引变量
            lastProcessId = row.id;
        });//each
    }
    var convertMap = new Map();
    if(processData.convert){
    	$.each(processData.convert, function(i,row){
    		convertMap.put(row.id,row.convert_html);
    	})
    }
    
    var timeout = null;
    //点击或双击事件,这里进行了一个单击事件延迟，因为同时绑定了双击事件
    $(document).on('click',".process-step",function(){//jquery1.9后使用on代替live
        //激活
        _canvas.find('#leipi_active_id').val($(this).attr("process_id")),
        clearTimeout(timeout);
        var obj = this;
        timeout = setTimeout(defaults.fnClick,300);
    }).on('dblclick',".process-step",function(){//jquery1.9后使用on代替live
        clearTimeout(timeout);
//        defaults.fnDbClick();
    });

    //使之可拖动
    if(isMove)
    	jsPlumb.draggable(jsPlumb.getSelector(".process-step"));
    initEndPoints();

    //绑定添加连接操作。画线-input text值  拒绝重复连接
    jsPlumb.bind("jsPlumbConnection", function(info) {
        setConnections(info.connection)
    });
    //绑定删除connection事件
    jsPlumb.bind("jsPlumbConnectionDetached", function(info) {
        setConnections(info.connection, true);
    });
    var jsPlumbObj;
    jsPlumb.bind("dblclick", function(conn, originalEvent) {//双击属性
    	jsPlumbObj = conn;
    });
    jsPlumb.bind("click", function(conn, originalEvent) {//单击获得对象
    	jsPlumbObj = conn;
    });
    //连接成功回调函数
    function mtAfterDrop(params){
        //console.log(params)
        defaults.mtAfterDrop({sourceId:$("#"+params.sourceId).attr('process_id'),targetId:$("#"+params.targetId).attr('process_id')});
    }
    
    jsPlumb.makeTarget(jsPlumb.getSelector(".process-step"), {
        dropOptions:{ hoverClass:"hover", activeClass:"active" },
        anchor:"Continuous",
        maxConnections:-1,
        endpoint:[ "Dot", { radius:1 } ],
        paintStyle:{ fillStyle:"#ec912a",radius:1 },
        hoverPaintStyle:this.connectorHoverStyle,
        beforeDrop:function(params){
            if(params.sourceId == params.targetId) return false;/*不能链接自己*/
            var j = 0;
            $('#process_info').find('input').each(function(i){
                var str = $('#' + params.sourceId).attr('process_id') + ',' + $('#' + params.targetId).attr('process_id');
                if(str == $(this).val()){
                    j++;
                    return;
                }
            })
            if( j > 0 ){
                defaults.fnRepeat();
                return false;
            } else {
                mtAfterDrop(params);
                return true;
            }
        }
    });
    //reset  start
    var _canvas_design = function(){
        //连接关联的步骤
        $('.process-step').each(function(i){
            var sourceId = $(this).attr('process_id');
            var prcsto = $(this).attr('process_to');
            var toArr = prcsto.split(",");
            var processData = defaults.processData;
            $.each(toArr,function(j,targetId){
                if(targetId!='' && targetId!=0){
                    //检查 source 和 target是否存在
                    var is_source = false,is_target = false;
                    $.each(processData.process, function(i,row) {
                        if(row.id == sourceId){
                            is_source = true;
                        }else if(row.id == targetId){
                            is_target = true;
                        }
                        if(is_source && is_target)
                            return true;
                    });
                    if(is_source && is_target){
                    	var convert_html = convertMap.get(sourceId+"_"+targetId);
                    	var cc = jsPlumb.connect({
                        	label:convert_html,//连接线名称
                            source:"window"+sourceId, 
                            target:"window"+targetId
                           /* ,labelStyle : { cssClass:"component label" }
                            ,label : id +" - "+ n*/
                        });
                    	cc.bind("contextmenu", function(connection,originalEvent) {//右键
                    		$("#leipi_convert_id").val(sourceId+"_"+targetId);
                    		contextmenu.bindings = defaults.convertMenus;
                    		$("#"+sourceId+"_"+targetId).contextMenu('convertMenu', contextmenu);
                    	});
                        return ;
                    }
                }
            })
        });
    }//_canvas_design end reset 
    _canvas_design();

    //-----外部调用---------添加步骤-------------
    var Flowdesign = {
        addProcess:function(row){
                if(row.id<=0){
                    return false;
                }
                var nodeDiv = document.createElement('div');
                var nodeId = "window" + row.id, badge = 'badge-inverse',icon = 'icon-star';
                if(row.icon){
                    icon = row.icon;
                }
                $(nodeDiv).attr("id",nodeId)
                .attr("style",row.style)
                .attr("process_to",row.process_to)
                .attr("process_id",row.id)
                .attr("process_type",row.process_type)//步骤类型
                .attr("process_code",row.process_code)//步骤编码
                .attr("countersign_type",row.countersign_type)//会签类型
                .attr("child_process_type",row.child_process_type)//子流程类型
                .attr("split_type",row.split_type)//分支类型
                .attr("join_type",row.join_type)//聚合类型
                .attr("actor_ids",row.actor_ids)//参与者id
                .attr("actor_names",row.actor_names)//参与者名称
                .attr("role_ids",row.role_ids)//角色id
                .attr("role_names",row.role_names)//角色名称
                .attr("orgframe_ids",row.orgframe_ids)//组织架构id
                .attr("orgframe_names",row.orgframe_names)//组织架构名称
                .addClass("process-step")
                .html('<span class="process-flag badge '+badge+'"><i class="'+icon+' icon-white"></i></span>&nbsp;' + row.process_name )
                .mousedown(function(e){
                  if( e.which == 3 ) { //右键绑定
                      _canvas.find('#leipi_active_id').val(row.id);
                      contextmenu.bindings = defaults.processMenus
                      $(this).contextMenu('processMenu', contextmenu);
                  }
                });
                
                _canvas.append(nodeDiv);
                //使之可拖动 和 连线
                if(isMove)
                	jsPlumb.draggable(jsPlumb.getSelector(".process-step"));
                initEndPoints();
                //使可以连接线
                jsPlumb.makeTarget(jsPlumb.getSelector(".process-step"), {
                    dropOptions:{ hoverClass:"hover", activeClass:"active" },
                    anchor:"Continuous",
                    maxConnections:-1,
                    endpoint:[ "Dot", { radius:1 } ],
                    paintStyle:{ fillStyle:"#ec912a",radius:1 },
                    hoverPaintStyle:this.connectorHoverStyle,
                    beforeDrop:function(params){
                    	if(params.sourceId == params.targetId) return false;/*不能链接自己*/   
                    	if($("#"+params.targetId).attr("process_type")=="start"){
                    		autoMsg("流程步骤不能指向开始步骤节点！",2);                    		
                    		return false;
                    	}
                    	if($("#"+params.sourceId).attr("process_type")=="end"){
                    		autoMsg("结束步骤节点不能再流向其他步骤节点！",2);                    		
                    		return false;
                    	}
                    	if($("#"+params.sourceId).attr("process_type")=="start"&&$("#"+params.targetId).attr("process_type")=="end"){
                    		autoMsg("开始节点不能直接流向结束节点！",2);                    		
                    		return false;
                    	}
                        var j = 0;
                        $('#process_info').find('input').each(function(i){
                            var str = $('#' + params.sourceId).attr('process_id') + ',' + $('#' + params.targetId).attr('process_id');
                            if(str == $(this).val()){
                                j++;
                                return;
                            }
                        })
                        if( j > 0 ){
                            defaults.fnRepeat();
                            return false;
                        } else {
                        	mtAfterDrop(params);
                            return true;
                        }
                    }
                });
                return true;
                
        },
        delProcess:function(activeId){
            if(activeId<=0) return false;
            $("#window"+activeId).remove();
            return true;
        },
        getActiveId:function(){
          return _canvas.find("#leipi_active_id").val();
        },
        getConvertId:function(){//当前连接线id
          return _canvas.find("#leipi_convert_id").val();
        },
        copy:function(active_id){
        if(!active_id)
        	active_id = _canvas.find("#leipi_active_id").val();
        	_canvas.find("#leipi_copy_id").val(active_id);
        	return true;
        },
        paste:function(){
            return  _canvas.find("#leipi_copy_id").val();
        },
        delConvertObj:function(){
        	if(confirm("你确定取消连接吗?")){
        		var convert_id = _canvas.find("#leipi_convert_id").val();
        		$("#"+convert_id).parent().prev().prev().remove();
        		$("#"+convert_id).parent().prev().remove();
        		$("#"+convert_id).parent().remove();
        		var convertId = convert_id.replace('_',',');
        		 $('#process_info').find('input').each(function(i){
                     if(convertId == $(this).val()){
                    	 $(this).remove();
                     }
                 })
//              jsPlumb.detach(jsPlumbObj);//由于直接右键 删除获取不了jsPlumbObj故废弃
        	}
        },
        getProcessInfo:function(){
            try{
              /*连接关系*/
              var aProcessData = {};
              var aConvertData = {};
              var flowJson = {};
              $("#process_info input[type=hidden]").each(function(i){
                  var processVal = $(this).val().split(",");
                  if(processVal.length==2){
                    if(!aProcessData[processVal[0]]){
                        aProcessData[processVal[0]] = {"top":0,"left":0,"process_to":[]};
                    }
                    aProcessData[processVal[0]]["process_to"].push(processVal[1]);
                  }
              });
              /*位置*/
              _canvas.find("div.process-step").each(function(i){ //生成Json字符串，发送到服务器解析
                      if($(this).attr('id')){
                          var pId = $(this).attr('process_id');
                          var icon = $(this).children().children().attr("class");//样式
                          var pLeft = parseInt($(this).css('left'));
                          var pTop = parseInt($(this).css('top'));
                          if(!aProcessData[pId]){
                              aProcessData[pId] = {"top":0,"left":0,"process_to":[]};
                          }
                          aProcessData[pId]["left"] = pLeft;//左边距离
                          aProcessData[pId]["top"] = pTop;//顶端距离
                          aProcessData[pId]["style"] = $(this).attr('style');//样式
                          aProcessData[pId]["icon"] = icon;
                          aProcessData[pId]["process_type"] = $(this).attr('process_type');//步骤类型
                          aProcessData[pId]["process_name"] = $(this).text().replace(/(^\s*)|(\s*$)/g,'');//名称
                          aProcessData[pId]["process_code"] = $(this).attr('process_code');//编码
                          aProcessData[pId]["countersign_type"] = $(this).attr('countersign_type');//会签类型
                          aProcessData[pId]["split_type"] = $(this).attr('split_type');//分支类型
                          aProcessData[pId]["join_type"] = $(this).attr('join_type');//聚合类型
                          aProcessData[pId]["child_process_type"] = $(this).attr('child_process_type');//子流程类型
                          aProcessData[pId]["actor_ids"] = $(this).attr('actor_ids');//人员id
                          aProcessData[pId]["actor_names"] = $(this).attr('actor_names');//人员名称
                          aProcessData[pId]["role_ids"] = $(this).attr('role_ids');//角色id
                          aProcessData[pId]["role_names"] = $(this).attr('role_names');//角色名称
                          aProcessData[pId]["orgframe_ids"] = $(this).attr('orgframe_ids');//组织架构id
                          aProcessData[pId]["orgframe_names"] = $(this).attr('orgframe_names');//组织架构名称
                      }
                  });
                  _canvas.find("div._jsPlumb_overlay").each(function(i){ //生成Json字符串，发送到服务器解析  流向
                      if($(this).html().length>1){
                    	  aConvertData[$(this).children().attr("id")] = {"convert_id":"","convert_name":"","convert_code":"","from":"","to":"","actor_ids":"","actor_names":"","role_ids":"","role_names":"","orgframe_ids":"","orgframe_names":"","convert_html":"","order":""};
                    	  var convertName = $(this).children().text().replace("<span></span>","");
                    	  aConvertData[$(this).children().attr("id")]["convert_name"] = convertName;
                    	  var from_to = $(this).children().attr("id");
                    	  aConvertData[$(this).children().attr("id")]["from"] = from_to.split("_")[0];
                          aConvertData[$(this).children().attr("id")]["to"] = from_to.split("_")[1];
                          aConvertData[$(this).children().attr("id")]["convert_id"] = $(this).children().attr("convert_id");
                          aConvertData[$(this).children().attr("id")]["convert_code"] = $(this).children().attr("convert_code");
                          aConvertData[$(this).children().attr("id")]["actor_ids"] = $(this).children().attr("actor_ids");
                          aConvertData[$(this).children().attr("id")]["actor_names"] = $(this).children().attr("actor_names");
                          aConvertData[$(this).children().attr("id")]["role_ids"] = $(this).children().attr("role_ids");
                          aConvertData[$(this).children().attr("id")]["role_names"] = $(this).children().attr("role_names");
                          aConvertData[$(this).children().attr("id")]["orgframe_ids"] = $(this).children().attr("orgframe_ids");
                          aConvertData[$(this).children().attr("id")]["orgframe_names"] = $(this).children().attr("orgframe_names");
                          aConvertData[$(this).children().attr("id")]["order"] = $(this).children().attr("order");
                    	  aConvertData[$(this).children().attr("id")]["convert_html"] = $(this).html().replace(/\"/g,"'");
                      }
                  });
                  flowJson["process"] = JSON.stringify(aProcessData);
                  flowJson["convert"] = JSON.stringify(aConvertData);
             return JSON.stringify(flowJson);
          }catch(e){
              return '';
          }

        },
        clear:function(){
            try{
                jsPlumb.detachEveryConnection();
                jsPlumb.deleteEveryEndpoint();
                $('#process_info').html('');
                $('#flowdesign_canvas').html('');
                jsPlumb.repaintEverything();
                return true;
            }catch(e){
                return false;
            }
        },refresh:function(){
            try{
                //jsPlumb.reset();
                this.clear();
                _canvas_design();
                return true;
            }catch(e){
                return false;
            }
        }
    };
    return Flowdesign;


  }//$.fn
})(jQuery);