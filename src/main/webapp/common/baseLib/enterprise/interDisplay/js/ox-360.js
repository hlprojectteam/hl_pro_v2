(function(){
	var G=KISSY,Q=G.DOM,U=G.Event;
	var B=0;
	var J=B;
	var O=0;
	var T="";
	var I=0;
	var L=false;
	var R=false;
	var K=false;
	var E=0;
	var F=0;
	var P=0;
	var N=[0,0,0,0];
	//var M={2:"/common/baseLib/enterprise/interDisplay/images/banner01.jpg",3:"/common/baseLib/enterprise/interDisplay/images/banner02.jpg",4:"/common/baseLib/enterprise/interDisplay/images/banner03.jpg",5:"/common/baseLib/enterprise/interDisplay/images/banner04.jpg",1:"/common/baseLib/enterprise/interDisplay/images/banner05.jpg"};
	//var M={1:"/common/baseLib/enterprise/interDisplay/images/banner01.jpg"};
	var M =getAttachImg();
	var A=1;
	var H=function(){Q.css("#J_View360","backgroundPosition",J+"px "+I+"px")};
	Q.css("#J_View360","backgroundImage","url("+M+")");
	/*var D=function(W){
		var V=Q.width(W);
		var X=Q.offset(W).left-Q.offset(W.parentNode).left;
		G.Anim(".views-block",{left:X+11+"px",width:V+"px"},0.3,G.Easing.easeNone).run();
		alert("当前calss的名字："+W.className);
		var S=W.className.substr(W.className.length-1,1);
		alert("当前："+S);
		Q.css("#J_View360","backgroundImage","url("+M[S]+")");O=-10;
		J=B+50;R=true
	};*/
	var C=Q.query("a",Q.get(".views-trigger"));
	//D(C[A]);
	U.on(C,"click",function(S){
		//D(this)
	});
	O=-10;
	J=B+50;
	R=true;
	H();
	setInterval(function(){
		if(R){
			if(Math.abs(J-B)<5){
				R=false;
				return
			}
			O=(B-J)*0.0485;
			J+=O;
			H();
			return
		}if(K){
			P=F-E;
			N.push(P);
			N.shift();
			P=0;
			for(var S=0;S<N.length;S++){
				P+=N[S]
			}
			P=P/N.length;
			O=P*0.485;
			J+=O;
			H();
			E=F;
			return
		}if(L){
			if(T=="right"){
				O-=0.05
			}else{
				if(T=="left"){
					O+=0.05
				}
			}
		}if(!L){
			O=O*0.97
		}
		J+=O;H()
	},10);
	
	document.body.onkeydown=function(S){
		var S=window.event||S;
		L=true;
		if(S.keyCode==37){
			T="left"
		}else{
			if(S.keyCode==39){
				T="right"
			}
		}
	};
	
	document.body.onkeyup=function(){
		L=false
	};
	
	U.on("#J_Left","mousedown",function(){
		T="left";
		L=true
	});
	
	U.on("#J_Right","mousedown",function(){
		T="right";
		L=true
	});
	
	U.on(["#J_Left","#J_Right"],"mouseup",function(){
		L=false
	});
	
	U.on("#J_Back","mousedown",function(){
		R=true
	});
	
	U.on("#J_View360","mousedown",function(S){
		O=0;
		E=F;
		K=true
	});
	
	U.on(document,"mouseup",function(){
		K=false
	});
	
	U.on(document,"mousemove",function(S){
		var S=window.event||S;
		F=document.all?S.clientX:S.pageX
	});
	
	U.on(document,"dragstart",function(S){
		return false
	}
)})();

function getAttachImg(){
	var attach = $("#Attach").val();
	return attach;
}