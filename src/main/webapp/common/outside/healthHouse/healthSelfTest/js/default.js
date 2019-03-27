/**
 * 云健康小屋外网通用js Yjk应用
 */

$(document).ready(function() {
//导航菜单选中状态开始
    var menu = ["Index","Healthyhut","Psychology","Information","Gauge","Company","Mall"];
    var pathString = window.location.pathname;
    if($("#modular").length>0){
        pathString=$("#modular").attr("href");

    }
    var activeMenu = "#mMenu>li>a[href='" + pathString + window.location.search + "']";

    //alert(activeMenu);
    if ($(activeMenu).length == 0) {
        for (var i = 0; i < menu.length; i++) {
            if (pathString.indexOf(menu[i]) == 1) {
                var mm = "#mMenu > li:eq(" + i + ")";
                $(mm).addClass('navactive');
                $("#phoneMenu>li:eq("+i+")").addClass('navactive');
                if($("#modular").length>0){
                    $("#modular").text($(mm+" a").text());
                }
                break;
            }
        }
    } else {
        $(activeMenu).parent().addClass('navactive');
        $("#phoneMenu>li>a[href='" + pathString + window.location.search + "']").parent().addClass('navactive');
    }
    //alert($(".navactive").length);
    if ($(".navactive").length === 0) {
        $("#mMenu>li:eq(0)").addClass('navactive');
        $("#phoneMenu>li:eq(0)").addClass('navactive');
    }
//导航菜单选中状态结束

//搜索框开始
    var _sType = (getQueryString("stype"));
    if (_sType != null) {
        $("#searchType li").removeClass("selected");
        var _tid = "#" + _sType;
        $(_tid).addClass("selected");
    }

    $("#searchType li").click(function () {
        $("#searchType li").removeClass("selected");
        $(this).addClass("selected");
    });

    if ($(".selected").length == 0) {
        $("#searchType>li:eq(0)").addClass('selected');
    }
//搜索框结束

//设置搜索条件开始
    var _iv= $("#iv").val();
    if(_iv !=""){
        $("#"+_iv).addClass("active");
    }else{
        $("#industry_all").addClass("active");
    }
    var _rv= $("#rv").val();
    if(_rv !=""){
        $("#r"+_rv).addClass("active");
    }else{
        $("#r1").addClass("active");
    }
    var psyType=$("#psyValue").val();
    if(psyType != ""){
        $("#"+psyType).addClass("active");
    }else{
        $("#psyAll").addClass("active");
    }

// 设置搜索条件结束
});

function search() {
    var sType = $("#searchType .selected").attr("id");
    var sear = $("#sear").val();
    var iv="",rv="";
    if(sear){
        sear="&sear="+sear;
    }
    if($("#iv").val()!=""&&$("#iv").length>0){
        iv="&iv="+$("#iv").val();
    }
    if($("#rv").val()!=""&&$("#rv").length>0){
        rv="&rv="+$("#rv").val();
    }

    switch (sType) {
        case "gauge":
            if($("#psyValue").val()!=""&&$("#psyValue").length>0){
                window.location.href = "/Psychology/psySearch.html?stype=gauge&psytype="+$("#psyValue").val()+sear;
            }else{
                window.location.href = "/Psychology/psySearch.html?stype=gauge"+sear;
            }

            break;
        case "com":
            window.location.href = "/Company/comSearch.html?stype=com"+sear+iv+rv;
            break;
        case "info":
            window.location.href = "/Information/infoSearch.html?stype=info"+sear;
            break;
        case "mall":
            window.location.href = "/Mall/mallSearch.html?stype=mall"+sear;
            break;
    }

}
function mallSearch() {
    var sear = $("#sear").val();
    if(sear){
        sear="?sear="+sear;
    }
    window.location.href = "/Yjk/Mall/mallSearch.html"+sear;

}
$("#condition>a").click(function () {
    var cid=this.id;
    if(cid.substr(0,2)=="ci"){
        $("#iv").val("");
        search();
    }
    if(cid.substr(0,2)=="cr"){
        var rtype=$("#rtype").val();
        var pid="";
        switch (rtype) {
            case "3":
                pid=$("#rpid").val();
                break;
            case "2":
                pid=$("#rpid").val();
                break;
        }
        $("#rv").val(pid);
        search();
    }
});
$("#industry>li").click(function () {
    $("#industry>li").removeClass("active");
    $(this).addClass("active");
    var iv=this.id;
    if(iv=="industry_all"){
        $("#iv").val("");
    }else{
        $("#iv").val(iv);
    }
    search();

});

$("#region>li").click(function(){
    $("#region>li").removeClass("active");
    $(this).addClass("active");
    var rv=this.id;
    $("#rv").val(rv.substr(1));
    search();
});
$("#psyType>li").click(function(){
    $("#psyType>li").removeClass("active");
    $(this).addClass("active");
    if(this.id=="psyAll"){
        $("#psyValue").val("");
    }else{
        $("#psyValue").val(this.id);
    }
    search();
});
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}


//
//
//

    var bb=0;
    var mycars=new Array();
    var result=new Array();
    var _key = new Array();
    var qcont="16";
    $(document).ready(function(){
        $('input[type="checkbox"], input[type="radio"]').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
        });
    });
    $('#radio_qid input[type="radio"]').on('ifChanged',function(event){
        var _qid = $('#_qid').val();
        if (_qid == 151) {
            self.location.href="/Gauge/gaugeDetails.html?qid=151&sex="+$(this).val();
        }
        
    });
    $('.question input').on('ifChecked', function(event){
        console.log(this);      
        var qid=this.id.substr(7)            
        var rid=isHasElementOne(mycars,qid);
        
        if(rid==-1){
            mycars.push(qid);
            result.push(this.id.substr(6));                
            bb+=1*1;
            v_height=Math.floor(bb/qcont*100)+'%';
            $(".progress-num").text(v_height);
            $(".progress-bar").height(v_height);
            var row='row'+qid;
            rowdiv=document.getElementById(row);
            rowdiv.style.backgroundColor="#FFF8DC";
            _key.push($(this).attr('data-key'));
        }else{
            result[rid]=this.id.substr(6);

        }    
        $('#_key').val(_key.join(','));
        $("#result").val(result.join(","));  
    });
    function isHasElementOne(arr,value){ 
        for(var i = 0,vlen = arr.length; i < vlen; i++){ 
            if(arr[i] == value){ 
                return i; 
            } 
        } 
        return -1; 
    }

    function save(){
        var _key = $('#_key').val();
        _key = _key.split(',');
        var rescon = new Array();
        if (_key[0]) {
            for(var rl = 0, reslen = _key.length; rl < reslen; rl++){
                rescon.push(Number(_key[rl])+1);
            }
        }
        var quesCount = [];
        var formUl = $('.matrix');
        for(u = 0,ulLen = formUl.length; u < ulLen; u++){
            quesCount.push(parseInt(formUl[u].id.substr(3))+1);
        }
        var notsubject = [];
        for (cou = 0, couLen = quesCount.length; cou < couLen; cou++) {
            if (rescon.indexOf(quesCount[cou]) < 0 && notsubject.indexOf(quesCount[cou]) < 0) {
                notsubject.push(quesCount[cou]);
            }
        }
        var notlen = notsubject.length;
         if(notlen > 0){
             var width = $("#div"+notsubject[0]).offset().top;
             width = width - 120;
             $("html, body").animate({
             scrollTop: width }, {duration: 500,easing: "swing"});
             if(notlen > 5){
                 l="您还有"+notlen+"题没做完("+notsubject.slice(0,5).toString()+"...)。";
             }else{
                 l="您还有"+notlen+"题没做完("+notsubject.toString()+")。";
             }
            $('#myModal .modal-body').html(
                "<div class='sweet-alert'>"+
                    "<div class='icon warning pulseWarning'>"+
                        "<span class='body pulseWarningIns'></span>"+
                        "<span class='dot pulseWarningIns'></span>"+
                    "</div>"+
                    "<p><a href='/Yjk/user/userRegister.html'>"+l+"</a></p>"+
                "</div>"
            );
            $('#myModal').modal();
             return false; 
        } else {
            $('#questionForm').submit(); 
        } 
              
    }
    
    
    function userRegister() {
        var cno = getQueryString("cno");
        if (cno != null) {
            self.location.href = "/user/userRegister.html?cno=" + cno;
        } else {
            self.location.href = "/user/userRegister.html";
        }
    }
    function getQueryString(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }


