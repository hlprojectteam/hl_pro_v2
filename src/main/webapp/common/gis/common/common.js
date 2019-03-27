/**
 * 地图后台公共方法 
 */
var mapshowFrm=parent.frames["mapshow"];
var mapType = 3;
var multiAngle=false;
var mapTypes=[3];

function editPointPolyline(xs,ys){
	var arrX=xs.split(',');
	var arrY=ys.split(',');
	var points=[];
	for(var i=0;i<arrX.length;i++){
		points.push({x:parseInt(arrX[i]),y:parseInt(arrY[i])});
	}
	mapshowFrm.popomap.editPointPolyline('pointPolyline',points);
}

function editArea(entity){
	if(mapshowFrm==null) return;
	mapshowFrm.omapApi.getMap().mapApi.editArea(entity);
}

function viewArea(_id,_name,_xs,_ys){
	if(mapshowFrm==null) return;
	var arrX=_xs.split(',');
	var arrY=_ys.split(',');
	/*for(var i=0;i<arrX.length;i++){
		arrX[i]*=2;
		arrY[i]*=2;
	}
	_xs=arrX.join(',');
	_ys=arrY.join(',');*/
	var obj={_id:_id,_name:_name,_xs:_xs,_ys:_ys};
	mapshowFrm.drawHsArea(obj,arrX[0],arrY[0]);
}
function refreshStation(station,oldPoint){
	if(!mapshowFrm) return;
	if(oldPoint){
		if(oldPoint)mapshowFrm.refreshStation(station,oldPoint);
	}
}
function refreshSign(sign,oldPoint){
	if(!mapshowFrm) return;
	//if(sign.x==oldPoint.x&&sign.y==oldPoint.y) oldPoint=null;
	if(oldPoint){
		mapshowFrm.refreshSign(sign,oldPoint);
	}
}
function previewAd(src,w,h,x,y){
	if(!x||!y) return;
	go2xy(x,y);
	mapshowFrm.previewAd(src,w,h,x,y);
}
function showErr(id,x,y){
  //alert(x+','+y);
  if(!mapshowFrm) return;
  mapshowFrm.popomap.go2xy(x+300,y);
  removePop_showErr();
  var htmlStr='<div style="width:398px;"><table width="100%" border="0" cellspacing="0" cellpadding="0">    <tr>      <td width="69" background="images/bbs_69x1.gif"  valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">        <tr>          <td height="110px">&nbsp;</td>        </tr>        <tr>          <td><img src="images/bbs_69x35.gif" width="69px" height="35px" /></td>        </tr>      </table></td>      <td style="border-top-width: 3px;border-right-width: 3px;border-bottom-width: 3px;border-top-style: solid;border-right-style: solid;border-bottom-style: solid;border-top-color: #CCCCCC;border-right-color: #CCCCCC;border-bottom-color: #CCCCCC;background-color: #FFFFFF;"><table width="100%" border="0" cellpadding="0" cellspacing="0">        <tr>          <td bgcolor="#F6F6F6" width="315px"><img src="images/jc.gif" width="86" height="26"></td>          <td width="20px" bgcolor="#F6F6F6"><img src="images/bbs_close.gif" style="cursor:pointer;" onclick="mapApi.removePop(\'errPop\')"></td>        </tr>        <tr>          <td colspan="2"><iframe id="ifAddErr" style="WIDTH:330px;HEIGHT:160px" scrolling="yes" frameborder="no" src="/map/mistake/message.shtml?act=view&id={$ID}&x={$X}&y={$Y}"></iframe></td>        </tr>      </table></td>    </tr>  </table></div>';
  htmlStr=htmlStr.replace('{$ID}',id);
  mapshowFrm.popomap.createPop('errPop',htmlStr,x,y,0,145,5);
}

function removePop_showErr(){
	if(!mapshowFrm) return;
	mapshowFrm.popomap.removePop('errPop');
}

function string2intArray(str){
	var arr=str.split(',');
	for(var i=0;i<arr.length;i++){
		arr[i]=parseInt(arr[i]);
	}
	return arr;
}

function validateXY(xs,ys,min,max)
{
var arrayX = xs.split(",");
var arrayY = ys.split(",");
var xlen = arrayX.length;

if (xs==""||ys==""){
  return false;
}
if (xlen != arrayY.length)
{
return false;
}


if (xlen < min)
{
return false;
}

if ((max > -1)&&(xlen > max ))
{
return false;
}

return true;
}

//entity start
var fields={
		0:{centerX:'x',centerY:'y',x:'xs',y:'ys'},
		3:{centerX:'x',centerY:'y',x:'xs',y:'ys'},
		7:{centerX:'x7',centerY:'y7',x:'xs7',y:'ys7'},
		8:{centerX:'x8',centerY:'y8',x:'xs8',y:'ys8'},
		9:{centerX:'x9',centerY:'y9',x:'xs9',y:'ys9'},
		10:{centerX:'x10',centerY:'y10',x:'xs10',y:'ys10'},
		11:{centerX:'x11',centerY:'y11',x:'xs11',y:'ys11'},
		12:{centerX:'x12',centerY:'y12',x:'xs12',y:'ys12'},
		13:{centerX:'x13',centerY:'y13',x:'xs13',y:'ys13'},
		14:{centerX:'x14',centerY:'y14',x:'xs14',y:'ys14'},
		15:{centerX:'x15',centerY:'y15',x:'xs15',y:'ys15'}
	};
function setCoordData(form,d){
	var field=fields[mapType];
	if (d.x && d.y){
		form.elements.x.value=d.x;
		form.elements.y.value=d.y;
	}
	var xsObj=form.elements.xs;
	var ysObj=form.elements.ys;
	if(xsObj&&ysObj){
		xsObj.value=d.xs||'';
		ysObj.value=d.ys||'';
	}
}
function validateEntity(form)
{
	
	 if(form.name.value==''){
		 alert('名称不能为空！');
		 return false ;
	 }
	 if(form.address.value==''){
		 alert('地址不能为空！');
		 return false ;
	 }    
	
	if(mapshowFrm){
		setEntityValue(form);
		var field=fields[mapType];
	   if (!validateXY(form.elements(field.x).value,form.elements(field.y).value,4,-1)){		
			alert("您的坐标不规范！");
			return false;
	  }
	   form['mapType'].value=mapType;
	}
  if(!validateEntityForm(form)) return false;
	return true;
}
function setEntityValue(form){
	if(!parent||!parent.mapshow) return;
	var d=parent.mapshow.getD('area');
	if(!d) return;
	var xs=d.xs.split(",");
	var ys=d.ys.split(",");
	d.x=xs[0];
	d.y=ys[0];
	d.xs=xs;
	d.ys=ys;
	setCoordData(form,d);
}
//entity end

var mapshowFrm=parent.frames["mapshow"];

//road start
function editRoadLine(id,x,y,xs,ys){
	if(!mapshowFrm) return;
	if(id!=''){
		var arrX=xs.split(',');
		var arrY=ys.split(',');
		var points=[];
		for(var i=0;i<arrX.length;i++){
			points.push({xs:parseInt(arrX[i]),ys:parseInt(arrY[i])});
		};
		mapshowFrm.editRoadLine(points);
	}
}
function validateRoad(form){
	setRoadValue(form);
	if(form.xs.value==''||form.ys.value==''){
		alert('不能为空');
		return false;
	}
	return validateSimpleForm(form);
}
function setRoadValue(form){
	if(!mapshowFrm) return;
	var d=mapshowFrm.getLine();
	if(!d) return;
	form.x.value=d.xs.split(',')[0];
	form.y.value=d.ys.split(',')[0];
	form.xs.value=d.xs;
	form.ys.value=d.ys;
}
function viewRoad(){
	if(!mapshowFrm) return;
	mapshowFrm.clear();
}
//road end
function go2xy(x,y){
	if(!mapshowFrm) return;
	if(x&&y){
		mapshowFrm.go2xy(x,y);
	}
}
function showEU(x,y,id,type){
	if(!mapshowFrm) return;
	if(type==1){
		mapshowFrm.popomap.showOCInfo(x,y,id,0,false,true);
	}
	else{
		mapshowFrm.popomap.showOCInfo(x,y,0,id,true,true);
	}
}
function showFlag(x,y){
	mapshowFrm.popomap.removePop('popAnchor');
	mapshowFrm.popomap.createPopT('popAnchor','<img src="/images/map/flag.gif">',x,y,12,12,50);
}