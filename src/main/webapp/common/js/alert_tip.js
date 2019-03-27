/*
 * 全局配置皮肤 
 */
layui.use('layer', function(){
	var layer = layui.layer;
//	parent.layer.config({
//	    skin: 'layui-layer-lan' //一旦设定，所有弹层风格都采用此主题。  layui-layer-molv绿色
//	});
});

/*
 * 提示框 使用layer控件
 */
function autoAlert(msg,icon){
	var index = parent.parent.layer.alert(msg, {
		icon: icon
	},function(){
		parent.layer.close(index); //关闭当前弹出框
	});
}


/*
 * 简单提示
 */
function autoMsg(msg,icon){
	parent.layer.msg(msg, {icon: icon});
}
