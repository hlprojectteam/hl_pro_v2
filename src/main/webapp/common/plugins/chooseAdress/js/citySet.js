function SelCity(obj, e)
{
    var ths = obj;
    var dal = '<div class="_citys"><span title="关闭" id="cColse" >×</span><ul id="_citysheng" class="_citys0"><li class="citySel">省份</li><li>城市</li><li>区县</li><li>街道/镇</li></ul><div id="_citys0" class="_citys1"></div><div style="display:none" id="_citys1" class="_citys1"></div><div style="display:none" id="_citys2" class="_citys1"></div><div style="display:none" id="_citys3" class="_citys1"></div></div>';
    
    
    Iput.show({
        id : ths, event : e, content : dal, width : "470"
    });
    $("#cColse").click(function (){
        Iput.colse();
    });
    
    
    var tb_province = [];
    var b = province;
    for (var i = 0, len = b.length; i < len; i++){
        tb_province.push('<a data-level="0" data-id="' + b[i]['id'] + '" data-name="' + b[i]['name'] + '">' + b[i]['name'] + '</a>');
    }
    $("#_citys0").append(tb_province.join(""));
    
    
    //省级的点击
    $("#_citys0 a").click(function (){
        var citySS = getCity($(this));		//获取选择省份的所有城市
        $("#_citys1 a").remove();			//先清空上一次的内容,
        $("#_citys1").append(citySS);		//再添加此次内容
        $("._citys1").hide();				//将四个div全部隐藏,
        $("._citys1:eq(1)").show();			//再显示第二个div(市级div)
        
        $("#_citys0 a,#_citys1 a,#_citys2 a,#_citys3 a").removeClass("AreaS");	//先清空之前div的样式AreaS,
        $(this).addClass("AreaS");												//再为最新的div添加样式AreaS	
        
        ths.value = $(this).data("name");			//点击省级后输入框的值(省),
        $("#"+province_).val($(this).data("id"));	//省级编码code(隐藏框),
        $("#"+city_).val(null);					//再将省级下(市、区、街道)code清空
        $("#"+district_).val(null);
        $("#"+street_).val(null);
        
        var lev = $(this).data("name");
        if (document.getElementById("hProvince") == null){
            var hiddenIputs = $('<input>', {
                type : 'hidden', name : "hProvince", "data-id" : $(this).data("id"), id : "hProvince", val : lev
            });
            $(ths).after(hiddenIputs);
        }
        else {
            $("#hProvince").val(lev);
            $("#hProvince").attr("data-id", $(this).data("id"));
        }
        
        
        
        //市级的点击
        $("#_citys1 a").click(function (){
        	var districtSS = getDistrict($(this));	//获取选中城市的所有区县
            $("#_citys2 a").remove();				//先清空上一次的内容,
            $("#_citys2").append(districtSS);		//再添加此次内容
            $("._citys1").hide();					//将四个div全部隐藏,
            $("._citys1:eq(2)").show();				//再显示第三个div(区县div)
            
            $("#_citys1 a,#_citys2 a,#_citys3 a").removeClass("AreaS");			//先清空之前div的样式AreaS,
            $(this).addClass("AreaS");											//再为最新的div添加样式AreaS	
            
            var hP = $("#hProvince").val();
            ths.value = hP + "-" + $(this).data("name");	//点击市级后输入框的值(省+市),
            $("#"+city_).val($(this).data("id"));			//市级编码code(隐藏框)
            
            var lev = $(this).data("name");
            if (document.getElementById("hCity") == null)
            {
                var hiddenIputs = $('<input>', {
                    type : 'hidden', name : "hCity", "data-id" : $(this).data("id"), id : "hCity", val : lev
                });
                $(ths).after(hiddenIputs);
            }
            else {
            	$("#hCity").val(lev);
                $("#hCity").attr("data-id", $(this).data("id"));
            }
            
            
            
            //区县的点击
            $("#_citys2 a").click(function (){
            	var streetSS = getStreet($(this));	//获取选中区县的所有街道/镇
	            $("#_citys3 a").remove();			//先清空上一次的内容,
	            $("#_citys3").append(streetSS);		//再添加此次内容
	            $("._citys1").hide();				//将四个div全部隐藏,
	            $("._citys1:eq(3)").show();			//再显示第四个div(街道/镇div)
	            
                $("#_citys2 a,#_citys3 a").removeClass("AreaS");				//先清空之前div的样式AreaS,
                $(this).addClass("AreaS");										//再为最新的div添加样式AreaS	
                
                var hP = $("#hProvince").val();
                var hC = $("#hCity").val();
                ths.value = hP + "-" + hC + "-" + $(this).data("name");		//点击区县后输入框的值(省+市+区县)
                $("#"+district_).val($(this).data("id"));					//区级编码code(隐藏框)
                
                var lev = $(this).data("name");
                if (document.getElementById("hDistrict") == null)
                {
                    var hiddenIputs = $('<input>', {
                        type : 'hidden', name : "hDistrict", "data-id" : $(this).data("id"), id : "hDistrict", val : lev
                    });
                    $(ths).after(hiddenIputs);
                }
                else {
                    $("#hDistrict").val(lev);
                    $("#hDistrict").attr("data-id", $(this).data("id"));
                }
                
                
                
	            //街道/镇的点击
	            $("#_citys3 a").click(function ()
	            {
	                $("#_citys3 a").removeClass("AreaS");						//先清空之前div的样式AreaS,
	                $(this).addClass("AreaS");									//再为最新的div添加样式AreaS	
	                
	                var hP = $("#hProvince").val();
	                var hC = $("#hCity").val();
	                var hD = $("#hDistrict").val();
	                ths.value = hP + "-" + hC + "-" + hD + "-" + $(this).data("name");		//点击区县后输入框的值(省+市+区县+街道/镇)
	                $("#"+street_).val($(this).data("id"));								//镇级编码code(隐藏框)
	                
	                var lev = $(this).data("name");
	                if (document.getElementById("hStreet") == null)
	                {
	                    var hiddenIputs = $('<input>', {
	                        type : 'hidden', name : "hStreet", "data-id" : $(this).data("id"), id : "hStreet", val : lev
	                    });
	                    $(ths).after(hiddenIputs);
	                }
	                else {
	                    $("#hStreet").val(lev);
	                    $("#hStreet").attr("data-id", $(this).data("id"));
	                }
	                
                	Iput.colse();	
                });
            });
        });
    });
    $("#_citysheng li").click(function ()
    {
        $("#_citysheng li").removeClass("citySel");
        $(this).addClass("citySel");
        var s = $("#_citysheng li").index(this);
        $("._citys1").hide();
        $("._citys1:eq(" + s + ")").show();
    });
}


//获得某个省的所有城市
function getCity(obj)
{
    var c = obj.data('id');
    var e = city;
    var f = [];
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['pid'] == parseInt(c)) {
            f.push(e[i]);
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++)
    {
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + f[j]['name'] + '" title="' + f[j]['name'] + '">' + f[j]['name'] + '</a>'
    }
    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(1)").addClass("citySel");
    return g;
}

//获得某个市的所有区县
function getDistrict(obj)
{
    var c = obj.data('id');
    var e = district;
    var f = [];
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['pid'] == parseInt(c)) {
            f.push(e[i]);
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++)
    {
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + f[j]['name'] + '" title="' + f[j]['name'] + '">' + f[j]['name'] + '</a>'
    }
    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(2)").addClass("citySel");
    return g;
}

//获得某个区县的所有街道/镇
function getStreet(obj)
{
	var c = obj.data('id');
    var e = street;
    var f = [];
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['pid'] == parseInt(c)) {
            f.push(e[i]);
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++)
    {
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + f[j]['name'] + '" title="' + f[j]['name'] + '">' + f[j]['name'] + '</a>'
    }
    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(3)").addClass("citySel");
    return g;
}