/**
 * =============================== 工具函数目录 ===============================
 * Domain				本系统用到的所有域名
 * MD5.encode			对字符串进行dm5加密
 * 
 * Login.checkLogin		检查是否已登录
 * 
 * Sender.ajax			以post方法进行ajax提交数据，ajax的通用方法
 * Sender.submitAjax	通过ajax异步提交表单	
 * Sender.submitIframe	通过iframe异步提交表单，即使用iframe实现ajax
 * Sender.submitLoad	提交一个form表单，post方式，用返回的结果刷新指定div，适用于多个组合条件查找数据显示列表功能
 * Sender.load			在div中显示url请求返回的页面，以get方式提交请求（用于异步加载页面中某一div）
 * Sender.openDiv		打开div弹出窗口，并显示url请求的页面（用于在当前页面打开一个弹出层，在弹出层中加载url请求页面），会调用Sender.toLoad
 * Sender.toLoad		在div中显示url请求返回的页面（多用于弹出层div，被openDiv调用）
 * Sender.getJsonByUrl	获取url返回的json数据，data表示get方式提交的数据
 * Sender.getJsonByForm	将form表单中的内容封装成json（不包含file input）
 * Sender.dealResult	处理ajax提交返回的结果，被obj.onAjax、Sender.ajax等方法调用
 * Sender.security		安全性验证不通过后，前端信息提示方式，被Sender.dealResult、Sender.submitAjax等方法调用
 * Sender.isNotSecurity	对提交的内容在前端做安全验证
 * 
 * Tips.showSuccessMsg	弹出信息提示小窗口，无按钮，成功时使用，2秒钟后自动关闭
 * Tips.showErrorMsg	弹出信息提示小窗口，无按钮，出错时使用，手动关闭
 * Tips.showWarnWin		弹出信息提示窗口，有按钮，警告时使用，手动关闭
 * Tips.showErrorWin	弹出信息提示窗口，有按钮，出错时使用，手动关闭
 * Tips.showAlertWin	弹出信息提示窗口，只有一个按钮：确定，功能类似于js的alert()方法弹出的信息框
 * Tips.showConfirmWin	弹出确认框，包含两个按钮：确定和取消，功能类似于js的confirm()方法弹出的确认框
 * Tips.showConfirmError出错，弹出确认框，包含两个按钮：确定和取消，功能类似于js的confirm()方法弹出的确认框
 * Tips.loading			弹出正在载入或提交小窗口
 * Tips.loaded			关闭正在载入或提交小窗口
 * 
 * Box.showWin			显示自定义窗口，多用于显示页面中隐藏的div
 * Box.hideWin			隐藏自定义窗口，与Box.showWin搭配使用，隐藏Box.showWin打开的窗口
 * Box.removeWin		移除自定义窗口，与Box.showWin搭配使用，移除Box.showWin打开的窗口
 * Box.showDiv			显示Div弹出层窗口，多用于打开编辑窗口，被Sender.openDiv方法调用
 * Box.closeDiv			关闭Div弹出层窗口，与Box.showDiv搭配使用，关于Box.showDiv打开的弹出层
 * 
 * FieldMsg.addFieldError	把错误信息加到页面输入框上，支持多行编辑出错提示，支持有内容输入后自动去掉错误提示
 * FieldMsg.addFieldErrors	把错误信息加到指定的一系列字段上
 * FieldMsg.addFieldSuccess	把成功信息加到页面输入框上，支持多行编辑提示
 * FieldMsg.clearFieldError	清除addFieldError方法添加的错误信息
 * FieldMsg.addActionError		以弹出窗口方式显示actionError内容
 * FieldMsg.addActionMessage	以弹出窗口方式显示actionMessage内容
 * FieldMsg.drawMessages		对ajax返回的消息对象进行页面显示（返回的是ReplyDto对象的json格式）
 * 
 * Util.winGo			跳转到指定的页面
 * Util.winBack			页面后退
 * Util.winBackTo		页面内滚动条跳到指定锚点
 * Util.winReload		重新加载
 * Util.winClose		关闭页面
 * Util.getUrlParams	获取浏览器当前地址栏某一参数值
 * Util.setUrlParams	添加地址栏参数
 * Util.hasParams		判断url中是否有某个参数
 * Util.getCheckedValues	获取单选框或者复选框选中的值
 * Util.setCheckedAll		复选框全选
 * Util.hover				鼠标悬停/离开添加样式
 * Util.getElement			获取当前点击的元素
 * Util.getElementTagName	获取当前点击元素的tagName值
 * 
 * StringUtil.trim			去掉字符串前后空格
 * StringUtil.ltrim			去掉字符串左侧空格
 * StringUtil.rtrim			去掉字符串右侧空格
 * StringUtil.getWidth		获取字符宽度，一个中文算1个字符宽度，两个英文算1个字符宽度（用于截取多少个字符显示在页面上）
 * StringUtil.getLength		获取字符数，一个中文算1个字符，一个英文算1个字符（用于mysql库时）
 * StringUtil.getLength2	获取字节数，一个中文算2个字节，一个英文算1个字节（用于oracle库gbk编码时）
 * StringUtil.getLength3	获取字节数，一个中文算3个字节，一个英文算1个字节（用于oracle库utf-8编码时）
 * StringUtil.startWith		判断是否以指定字符串开头
 * StringUtil.endWith		判断是否以指定字符串结尾
 * StringUtil.encodeURI		中文编码
 * StringUtil.encodeForHTML		对HTML的<>符号转义
 * StringUtil.decodeForHTML		对HTML的<>符号转义还原
 * StringUtil.htmlFilter		对html的所有标签符号进行转义
 * StringUtil.getTimeStr		显示数据产生的时间，格式如：12分种前、54秒前、今天12:10
 * StringUtil.compareDate		比较日期的大小，字符串格式：2015-4-5
 * StringUtil.trimHeadZero		去掉字符串前面的0
 * StringUtil.getBirthdayIdcard	根据身份证获取生日，格式：YYYY-MM-DD（注：不会验证身份证的合法性，也不会验证获得的出生日期合法性）
 * StringUtil.getSexIdcard		根据身份证获取性别：1男，2女（注：不会验证身份证的合法性）
 * StringUtil.subStringChinese  截取字符串，包含中文处理  
 * 
 * Validator.isNotBlank		判断字符串非空（空格表示非空）
 * Validator.isNotEmpty		判断字符串非空（空格表示空）
 * Validator.isBlank		判断是否为空串或空格（""或" "返回true，null或有值返回false）
 * Validator.isEmpty		判断是否为null或空串或空格（即：没有确切值，null或""或" "返回true，有值返回false）
 * Validator.isNumber		是否为数字（正数或0）
 * Validator.isInteger		是否为整数（包括正数，负数和0）
 * Validator.isPlusInt		是否为大于0的整数
 * Validator.isFloat		是否为小数，dl表示几位小数
 * Validator.isNotMinusFloat	是否为大于等于0的小数，dl表示几位小数
 * Validator.isLetter			是否为字母（包括大小写）
 * Validator.isValidText		判断输入的文字是否为中文、英文或数字
 * Validator.isLetterOrNum		判断是否为数字或字母
 * Validator.isLetterOrNumOrUnLine	判断是否为数字、字母或下划线
 * Validator.isUserName			用户名校验，字母数字和下划线组成，4-25位
 * Validator.isImage			图片格式验证jpg|jpeg|gif|png
 * Validator.isDoc				文档格式验证doc|docx|xlsx|xls
 * Validator.isXls				文档格式验证xlsx|xls
 * Validator.hasIllegalChar		判断是否含非法字符[<>&~!@#$%|]
 * Validator.isPhone			是否为电话号码
 * Validator.isMobile			是否为手机号码
 * Validator.isEmail			是否为邮箱地址
 * Validator.isQQ				是否为QQ
 * Validator.isIdCard15			是否为15位身份证号
 * Validator.isIdCard18			是否为18位身份证号
 * Validator.isValidHttpIpPort	判断是否为http://IP:PORT地址格式
 * Validator.isValidUrl			判断是否为有效的URL
 * Validator.isPicTypeValue		判断输入的课程默认图片code和中文对应关系,格式是否如 xx:小学;cz:初中;gz:高中
 * Validator.isFile				验证字符串是否符合文件的正则表达式
 * Validator.test				验证字符串是否符合正则表达式
 * Validator.isDateFormat		校验日期格式：YYYY-MM-DD
 * 
 * Browser.IE		是否IE
 * Browser.IE6		是否IE6
 * Browser.IE7		是否IE7
 * Browser.Opera	是否Opera
 * Browser.WebKit	是否WebKit
 * Browser.Gecko	是否Gecko
 * Browser.Chrome	是否Chrome
 * Browser.copy		将string复制到剪贴板
 * 
 * object.onAjax		在提交元素上绑定该方法，防止重复提交（全部post方式）
 * object.onEnter		回车响应事件
 * object.getVal		获取输入框中含有占位符的内容，过滤掉占位符
 * object.setVal		设置元素值，并执行回调函数(兼容IE6)
 * object.minHeight		设置对象最小高度
 * object.counter			字符统计
 * object.clearCounter		字符统计清零
 * object.clearFile			清空文件对象
 * object.slideRemove		滑动删除
 * object.setCursorPosition	设置光标位置
 * object.setSelection		选中文字
 * object.focusEnd			设置光标停在文字最后
 * object.center			DIV居中
 * object.error				输入框错误闪烁（需要样式支持）
 * object.isVisible			元素是否可见
 * object.vshow				设置元素可见
 * object.vhide				设置元素不可见（但位置仍然占着）
 * object.setAllSelectCheckbox		容器中有checkbox全选
 * 
 * DateObject.format		将Date转化为指定格式的字符串
 */
(function(window, undefined){

	window.Domain = {
		domain_path: null,
		static_path: null
	};
	
	window.MD5 = {
		encode : function(str){
			return hex_md5(str);
		}
	};
	
	window.Login = {
		// 检测是否登录，如果未登录则需登录
		checkLogin: function(isLayer) {
			var url = "/open/isLogin.action";
			var isLogin = false;
			Sender.ajax({"url": url, "async":false, "fn": function(rs){
				if (rs == "1"){
					isLogin =  true;
				}else{
					if(isLayer){
						Tips.showLoginWin();
					}else{
						Tips.showAlertWin({"info":"请先登录！","callFn":function(){
							$("#username").focus();
						}});
					}
				}
			}});
			return isLogin;
		}
	};
	
	/**
	 * =============================== Sender类：向服务端提交请求或保存数据，包括ajax和iframe两种方式 ===============================
	 */
	window.Sender = {
		/**
		 * 以post方法进行ajax提交数据，ajax的通用方法，参数params组成如下：
		 * 
		 * @param url  		提交数据的地址，必须·
		 * @param data		提交的数据(json格式或&分隔的key=value串，如：unitId=1&userId)，必须
		 * @param fn 		提交数据成功时返回页面后的回调函数，可以为空
		 * @param dataType 	表示返回数据类型（text,json,jsonp，默认text），可以为空
		 * @param async		是否异步提交，默认同步，可以为空
		 * @param fne		提交数据失败时返回页面后的回调函数，可以为空
		 */
		ajax: function(params) {
			var url = params["url"];
			var data = params["data"];
			var fn = params["fn"];
			var async = params["async"];
			var dataType = params["dataType"];
			var fne = params["fne"];
			
			if (async != undefined && !async){
				async = false;
			}
			$.ajax({
				type: 'post',
				url: url, 
				data: data,
				async: async,
				dataType: dataType || 'text',
				success: function(result){
					Sender.dealResult(result, fn, fne);
				},
				error: function(obj, status){
					Sender.dealResult(obj.responseText, null, fne);
				}
			});
		},
		
		
		/**
		 * 通过ajax异步提交表单，参数params组成如下：
		 * 
		 * @param frm  		提交数据的form表单在jQuery的selector名字（如id=formId的，则传入#formId ）
		 * @param fn 		提交数据返回页面后的回调函数
		 * @param dataType 	表示返回数据类型（text,json,jsonp，默认text）
		 */
		submitAjax: function(params){
			var frm = params["frm"];
			var fn = params["fn"];
			var dataType = params["dataType"];
			
			var url = $(frm).attr("action");
			var serializedata = $(frm).serialize();
			var _dataType;
			if(Validator.isNotEmpty(dataType)){
				_dataType = dataType;
			}else{
				_dataType = "text";
			}
			
			$.post(url, serializedata, function(result){
			  if(!Sender.security(result)){
			    return;
			  }
				fn(result);
			}, dataType);
		},
		
		
		/**
		 * 通过iframe异步提交表单，类似于ajax，即使用iframe实现ajax，params参数如下：
		 * 
		 * @param frm  		提交数据的form表单在jQuery的selector名字（如id=formId的，则传入#formId ）
		 * @param fn 		提交数据返回页面后的回调函数
		 * @param dataType 	表示返回数据类型（text,json,jsonp）
		 */
		submitIframe: function(params){
			var frm = params["frm"];
			var fn = params["fn"];
			var dataType = params["dataType"];
			
			var target = "submit_iframe_" + new Date().getTime();
			$(".submit_iframe").remove();
			var iframe = $('<iframe src="about:blank" id="'+target+'" name="'+target+'" style="display:none" class="submit_iframe"></iframe>');
			$(frm).after(iframe);
			iframe.bind("load", function(){
				var doc = window.frames[target].document;
				var title = doc.getElementsByTagName('title')[0];
				if (title && title.innerHTML) {
					Tips.showErrorMsg("\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5\uff01");
					return;
				}
				var pre = doc.getElementsByTagName('pre')[0];
				var data = pre ? pre.innerHTML : doc.body.innerHTML;
				if (dataType == 'json') {
					data = $.toJSON(data);
				}
				fn(data);
			});
			$(frm).attr("target", target);
			$(frm).submit();
		},
		
		
		/**
		 * 提交一个form表单，post方式，用返回的结果刷新指定div，适用于多个组合条件查找数据显示列表功能，params参数如下：
		 * 
		 * @param frm		提交数据的form表单在jQuery的selector名字（如id=formId的，则传入#formId ）
		 * @param div		返回数据刷新的div，selector名字
		 * @param url		提交到哪个地址，可以为空，如果为空则取form表单中的action属性
		 */
		submitLoad: function(params){
			var frm = params["frm"];
			var div = params["div"];
			var url = params["url"];
			
			var obj = $(div);
			if(Validator.isEmpty(url)){
				url = $(frm).attr("action");
			}
			var serializedata = $(frm).serialize();
			obj.empty().addClass("loader");
			obj.empty().append("<div style='vertical-align:middle;text-align:center;'><img src='" + 
					Domain.static_path + "/home/static/images/loading.gif' /></div>");
			$.ajax({
				type:"post",
				url: url,
				data: serializedata,
				error:function(){
					obj.empty().remove("loader");
					var msg = "<div style='vertical-align:middle;text-align:center;'>加载失败，请<a href='javascript:void();' " +
							  "onclick=\"javascript:Sender.submitLoad({'frm':'"+frm+"','div':'"+div+"','url':'"+url+"'})\">" +
							  "重新刷新</a></div>";
					obj.empty().append(msg);
				},
				success:function(data){
					obj.empty().remove("loader");
					obj.empty().append(data);
				}
			});
		},
		
		/**
		 * 在div中显示url请求返回的页面，以get方式提交请求（多用于页面中某一div），params参数如下：
		 * 
		 * @param id  载入数据的div在jQuery的selector名字（如id=divId的，则传入#divId ），必须
		 * @param url 载入数据地址，必须
		 * @param fn  载入数据之后的回调函数  非必须
		 */
		load: function(params){
			var div = params["div"];
			var url = params["url"];
			var fn  = params["fn"];
			var obj = $(div);
			obj.empty().addClass("loader").attr("url", url);
			obj.empty().append("<div style='vertical-align:middle;text-align:center;'><img src='" + 
					Domain.static_path + "/home/static/images/loading.gif' /></div>");
			
			$.ajax({
				type:"GET",
				contentType:"application/x-www-form-urlencoded;charset=UTF-8",  
				url:url,
				
				error:function(){
					obj.empty().remove("loader");
					var msg = "<div style='vertical-align:middle;text-align:center;'>加载失败，" +
							  "请<a href='javascript:void();' onclick=\"javascript:Sender.load({'div':'"+div+"','url':'"+url+"'})\">" +
							  "重新刷新</a></div>";
					obj.empty().append(msg);
				},
				success:function(data){
					obj.empty().remove("loader");
					if (obj.attr("url") == url){
						obj.empty().append(data);	
					}
					if(fn!=undefined&&fn!=null){
						fn();
					}
				}
			});
		},
		
		
		/**
		 * 打开div弹出窗口，并显示url请求的页面（用于在当前页面打开一个弹出层，在弹出层中加载url请求页面），会调用Sender.toLoad，params参数如下：
		 * 
		 * @param div			显示的DIV在jQuery的selector名字，如id=divId的，则传入#divId，不能空
		 * @param closeObject	关闭的控件在jQuery的selector名字，如id=closeBut的，则传入#closeBut，可以空
		 * @param url			加载的url 可以空
		 * @param scroll		是否需要滚动
		 * @param className		内部的className 如果需要设置滚动 则必须填写
		 * @param height		内部的高度超过此高度则需要 否则不需要
		 * @param urlLoadedHandler	url加载完毕后的回调函数
		 * @param closeHandler		弹出窗口关闭后的回调函数
		 */
		openDiv: function(params) {
			var div = params["div"];
			var closeObject = params["closeObject"];
			var url = params["url"];
			var scroll = params["scroll"];
			var className = params["className"];
			var height = params["height"];
			var urlLoadedHandler = params["urlLoadedHandler"];
			var closeHandler = params["closeHandler"];
			
			if(url){
				var params1 ={
						"loadObject":div,
						"url":url,
						"endHandler":function() {
							var params2 ={
									"div":div,
									"closeObject":closeObject,
									"scroll":scroll,
									"className":className,
									"height":height,
									"closeHandler":closeHandler
							};
							Box.showDiv(params2);
							
							if (urlLoadedHandler) {
								if (urlLoadedHandler instanceof Function) {
									eval(urlLoadedHandler)();
								} else {
									eval(urlLoadedHandler);
								}
							}
						}
				};
				
				Sender.toLoad(params1);
			}else{
				var params2 ={
						"div":div,
						"closeObject":closeObject,
						"scroll":scroll,
						"className":className,
						"height":height,
						"closeHandler":closeHandler
				};
				Box.showDiv(params2);
			}
		},
		
		
		/**
		 * 在div中显示url请求返回的页面（多用于弹出层div，被openDiv调用），params参数如下：
		 * 
		 * @param loadObject	显示的DIV在jQuery的selector名字，如id=openDiv的，则传入 #openDiv 不能空
		 * @param url 			加载的url 可以空
		 * @param endHandler	加载后回调函数
		 * @param beforeHandle	加载前回调函数
		 * @param noLoadTip		是否显示加载数据中 否则显示
		 * @param data			提交的数据
		 * @return
		 */
		toLoad: function(params) {
			var loadObject = params["loadObject"];
			var url = params["url"];
			var endHandler = params["endHandler"];
			var beforeHandle = params["beforeHandle"];
			var noLoadTip = params["noLoadTip"];
			var data = params["data"];
			
			var length = url.length;
			var u = "";
			for (i = 0; i < length; i++) {
				var v = url.substring(i, i + 1);
				if (url.charCodeAt(i) > 255) {
					u += encodeURI(v);
				} else {
					u += v;
				}
			}
			url = u;
			if (beforeHandle && beforeHandle != "") {
				if (beforeHandle instanceof Function) {
					eval(beforeHandle)();
				} else {
					eval(beforeHandle);
				}
			} else {
				if (!noLoadTip) {
					noLoadTip = false;
				}
				if (!noLoadTip) {
					var padding = $(loadObject).height();
					$(loadObject)
							.html("<table height='" 
									+ padding
									+ "' width='100%'><tr><td width='50%' align='right'><img src='"
									+ Domain.static_path
									+ "/home/static/images/loading.gif' />" 
									+ "</td><td width='50%' align='left'><span>&nbsp;正在加载数据……</span></td></tr></table>");
				}
			}
			
			// 调用jquery中div的load扩展方法
			$(loadObject).load(url, data, function(response, status, xhr) {
				var sessionstatus = xhr.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
				if (sessionstatus == "timeout") { // 如果超时就处理 ，指定要跳转的页面
					top.location.href = Domain.domain_path
							+ "/login.action";
				}
				if (endHandler && endHandler != "") {
					if (endHandler instanceof Function) {
						eval(endHandler)();
					} else {
						eval(endHandler);
					}
				}
			});

		},
		
		/**
		 * 获取url返回的json数据，data表示get方式提交的数据，params参数如下：
		 * 
		 * @param url		请求的url
		 * @param data		提交的数据
		 * @param handler	请求后回调函数
		 */
		getJsonByUrl: function(params) {
			var url = params["url"];
			var data = params["data"];
			var handler = params["handler"];
			
			$.getJSON(url, data, function(data, textStatus, xhr) {
				var sessionstatus = xhr.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
				if (sessionstatus == "timeout") { // 如果超时就处理 ，指定要跳转的页面
					top.location.href = Domain.domain_path
							+ "/login.action";
				}
				if (handler && handler != "") {
					if (handler instanceof Function) {
						eval(handler)(data);
					} else {
						eval(handler);
					}
				}
			});
		},
		
		/**
		 * 将form表单中的内容封装成json（不包含file input）
		 * 
		 * @param obj
		 */
		getJsonByForm: function(obj) {
			var data = obj.serializeArray();
	        return JSON.stringify(data);
		},
		
		/**
		 * 处理ajax提交返回的结果，被obj.onAjax、Sender.ajax等方法调用
		 * 
		 * result：返回的结果数据
		 * fn：成功时执行的函数
		 * fne：失败时执行的函数
		 */
		dealResult: function(result, fn, fne) {
			switch (result) {
				case "TIME_OUT":
					if (fne) {
						fne();
					}
					break;
				case "PES_ERROR":
					if (fne) {
						fne();
					}
					Tips.showErrorMsg("\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5\uff01");
					break;
				case "SYS_ERROR":
					if (fne) {
						fne();
					}
					Tips.showErrorMsg("\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5\uff01");
					break;
				default:
					if(!Sender.security(result)){
						return;
					}
					if (fn) {
						fn(result);
					} 
				break;
			}
		},
		
		// 安全性验证不通过后，前端信息提示方式，被Sender.dealResult、Sender.submitAjax等方法调用
		security: function(result){
			if(typeof result === "string" && result.indexOf("NO_SECURITY") != -1){
				var temp = result.substring(11);
				var cont = StringUtil.encodeForHTML(temp);
				Tips.showAlertWin("你提交的内容中可能包含不安全内容：【" + cont + "】");
				return false;
			}
			return true;
		},
		
		// 对提交的内容在前端做安全验证
		isNotSecurity: function(val){
			var result = /[&;%\'\"<>]|[\s\S]*javascript\:[\s\S]*|[\s\S]*(window\.)?document\.[\s\S]*|[\s\S]*eval\([\s\S]*\)[\s\S]*/.test(val);
			if (result){
				Tips.showAlertWin("你提交的内容中可能包含不安全内容：【" + StringUtil.encodeForHTML(val) + "】！");
			}
			return result;
		}
		
	};
	
	
	/**
	 * =============================== Tips类：提示窗口 ===============================
	 */
	window.Tips = {
		/**
		 * 弹出信息提示小窗口，无按钮，成功时使用，2秒钟后自动关闭，params参数如下：
		 * 
		 * info：		提示信息 
		 * info2：		提示信息 
		 * overlayer: 	是否遮挡底部，true遮挡，false不遮挡，默认为true 
		 * callFn： 		回调函数
		 */
		showSuccessMsg : function(params){
			var info = params["info"];
			var info2 = !params["info2"]? "" : params["info2"];
			var overlayer = params["overlayer"];
			var callFn = params["callFn"];

			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:block;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>提示</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips ui-layer-win'>" 
						+ "<p class='tips-icon'><span class='tips-img' id='info'></span></p>"
						+ "<p class='tips-line' id='info2'></p>"
					+ "</div>" 
				+ "</div>";
			
			$("#popupInfoBox").html(htmlStr);
			$("#popupInfoBox #info").html(info);
			$("#popupInfoBox #info2").html(info2);
			
			autoLayer({
				"layer" : "#popupInfoBox",
				"mask" : (overlayer != undefined && !overlayer) ? 0 : 1,
				"cls"　: "a.close",
				"tt" : "提示",
				"fn" : callFn
			});
		},
		
		/**
		 * 弹出信息提示小窗口，无按钮，出错时使用，手动关闭，params的参数如下：
		 * 
		 * info：		提示信息 
		 * info2：		提示信息 
		 * overlayer: 	是否遮挡底部，true遮挡，false不遮挡，默认为true 
		 * callFn： 		回调函数
		 */
		showErrorMsg : function(params){
			var info = params["info"];
			var info2 = !params["info2"]? "" : params["info2"];
			var overlayer = params["overlayer"];
			var callFn = params["callFn"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:block;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>提示</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips ui-layer-lose'>" 
						+ "<p class='tips-icon'><span class='tips-img' id='info'></span></p>" 
						+ "<p class='tips-line' id='info2'></p>"
					+ "</div>" 
				+ "</div>";
			
			$("#popupInfoBox").html(htmlStr);
			$("#popupInfoBox #info").html(info);
			$("#popupInfoBox #info2").html(info2);
			
			// 显示div弹出框
			layer({
				"layer" : "#popupInfoBox",
				"mask" : 1,
				"cls"　: "a.close",
				"tt" : "提示"
			});
			
			if(callFn){
				$(".close").bind("click", callFn);
			}
			
			// 去掉遮罩层
			if (overlayer != undefined && !overlayer){
				$("#_______overlayer").remove();
			}
			
		},
		
		/**
		 * 弹出信息提示窗口，有按钮，警告时使用，手动关闭，params参数如下：
		 * 
		 * info：		提示信息 
		 * overlayer: 	是否遮挡底部，true遮挡，false不遮挡，默认为true 
		 * callFn： 		回调函数
		 */
		showWarnWin : function(params){
			var info = params["info"];
			var overlayer = params["overlayer"];
			var callFn = params["callFn"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:none;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>警告</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips ui-layer-warn'>" 
						+ "<p class='tips-icon pt-30'><span class='tips-img' id='info'></span></p>" 
					+ "</div>" 
				+ "</div>"
				+ "<p class='dd'>"
					+ "<a class='abtn abtn-blue submit' href='javascript:;'><span>确定</span></a>"
				+ "</p>";
			
			$("#popupInfoBox").html(htmlStr);
			$("#info").html(info);
			if(callFn){
				$("#popupInfoBox .close").bind("click", callFn);
				$("#popupInfoBox .submit").bind("click", callFn);
			}
			
			// 显示div弹出框
			$("#popupInfoBox").jWindowOpen({
				modal:true,
				center:true,
				//drag:".tt",//可移动层，不需要注释掉本行
				close:"#popupInfoBox .close,#popupInfoBox .submit" //最后一个不要加逗号，不然IE7无效
			});
			
			// 显示div弹出框
			layer({
				"layer" : "#popupInfoBox",
				"mask" : 1,
				"cls"　: "a.close, a.submit",
				"tt" : "提示"
			});
			
			// 去掉遮罩层
			if (overlayer != undefined && !overlayer){
				$("#_______overlayer").remove();
			}
		},
		
		/**
		 * 弹出信息提示窗口，有按钮，出错时使用，手动关闭，params参数如下：
		 * 
		 * info：		提示信息 
		 * callFn： 		回调函数
		 */
		showErrorWin : function(params){
			var info = params["info"];
			var info2 = !params["info2"]? "" : params["info2"];
			var callFn = params["callFn"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:none;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>错误</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips ui-layer-lose'>" 
						+ "<p class='tips-icon'><span class='tips-img' id='info'></span></p>" 
						+ "<p class='tips-line' id='info2'></p>" 
					+ "</div>" 
				+ "</div>"
				+ "<p class='dd'>"
					+ "<a class='abtn abtn-blue submit' href='javascript:;'><span>确定</span></a>"
					+ "<a class='abtn abtn-gray reset ml-20' href='javascript:;'><span>取消</span></a>"
				+ "</p>";
			
			
			$("#popupInfoBox").html(htmlStr);
			$("#info").html(info);
			$("#info2").html(info2);
			
			if(callFn){
				$("#popupInfoBox .submit").bind("click", callFn);
			}
			
			// 显示div弹出框
			layer({
				"layer" : "#popupInfoBox",
				"mask" : 1,
				"cls"　: "a.close,a.submit,a.reset",
				"tt" : "提示"
			});
		},
		
		/**
		 * 弹出信息提示窗口，只有一个按钮：确定，功能类似于js的alert()方法弹出的信息框，params参数如下：
		 * 
		 * info：		提示信息 
		 * callFn：		确定关闭时的回调函数
		 * btnText：		按钮名称
		 */
		showAlertWin : function(params){
			var info = params["info"];
			var callFn = params["callFn"];
			var btnText = params["btnText"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:none;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>提示</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips ui-layer-warn'>" 
						+ "<p class='tips-line' id='info'></p>" 
					+ "</div>" 
				+ "</div>"
				+ "<p class='dd'>"
					+ "<a class='abtn abtn-blue submit' href='javascript:;'><span>"+(btnText == null?"确定":btnText)+"</span></a>"
				+ "</p>";
			
			$("#popupInfoBox").html(htmlStr);
			$("#info").html(info);
			
			// 显示div弹出框
			layer({
				"layer" : "#popupInfoBox",
				"mask" : 1,
				"cls"　: "a.close,a.submit",
				"tt" : "提示"
			});
			
			if(callFn){
				$("#popupInfoBox .close").bind("click", callFn);
				$("#popupInfoBox .submit").bind("click", callFn);
			}
		},
		
		
		/**
		 * 弹出确认框，包含两个按钮：确定和取消，功能类似于js的confirm()方法弹出的确认框，params参数如下：
		 * 
		 * info：		主提示信息 
		 * title：		提示框名称，可为空
		 * callFn：		确定按钮点击后要执行的函数名
		 * btnText:		按钮名称；
		 */
		showConfirmWin : function(params){
			var info = params["info"];
			var title = params["title"];
			var callFn = params["callFn"];
			var btnText = params["btnText"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:none;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>"+(title == null?"提示":title)+"</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips'>" 
						+ "<p class='tips-line' id='info'></p>" 
					+ "</div>" 
				+ "</div>"
				+ "<p class='dd'>"
					+ "<a class='abtn abtn-blue submit' href='javascript:;'><span>"+(btnText == null?"确定":btnText)+"</span></a>"
					+ "<a class='abtn abtn-gray reset ml-20' href='javascript:;'><span>取消</span></a>"
				+ "</p>";
			
			
			$("#popupInfoBox").html(htmlStr);
			$("#info").html(info);

			
			// 显示div弹出框
			layer({
				"layer" : "#popupInfoBox",
				"mask" : 1,
				"cls"　: "a.close,a.submit,a.reset",
				"tt" : title
			});
			$("#popupInfoBox .submit").bind("click", callFn);
		},
		
		/**
		 * 出错，弹出确认框，包含两个按钮：确定和取消，功能类似于js的confirm()方法弹出的确认框，params参数如下：
		 * info：		主提示信息
		 * info2:       底部显示说明
		 * callFn：		确定按钮点击后要执行的函数名
		 * btnText:		按钮名称；默认:确定
		 */
		showConfirmError : function(params){
			var info = params["info"];
			var info2 = params["info2"];
			var btnText = params["btnText"];
			var callFn = params["callFn"];
			var btnText2 = params["btnText2"];
			var callFn2 = params["callFn2"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			if(!btnText){
				btnText = "确定";
			}
			if(!btnText2){
				btnText2 = "取消";
			}
			
			$(document.body).append("<div class='ui-layer' id='popupInfoBox' style='display:none;width:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='tt'><a href='javascript:;' class='fa fa-close fa-lg close' title='关闭'></a><span class='title'>提示</span></p>"           	
				+ "<div class='wrap'>" 
					+ "<div class='ui-layer-tips ui-layer-warn'>" 
						+ "<p class='tips-icon pt-30'><span class='tips-img' id='info'></span></p>" 
						+ "<p class='tips-icon pt-30'><span class='pt-10 t-center f14 c-666'>"+ (info2 ? "（"+info2+"）" : "")+"</span></p>" 
					+ "</div>" 
				+ "</div>"
				+ "<p class='dd'>"
					+ "<a class='abtn abtn-blue submit' href='javascript:;'><span>"+btnText+"</span></a>"
					+ "<a class='abtn abtn-gray reset ml-20' href='javascript:;'><span>"+btnText2+"</span></a>"
				+ "</p>";
			
			
			$("#popupInfoBox").html(htmlStr);
			$("#info").html(info);
			
			if(callFn){
				$("#popupInfoBox a.submit").bind("click", callFn);
			}
			if(callFn2){
				$("#popupInfoBox a.reset").bind("click", callFn2);
			}
			
			// 显示div弹出框
			layer({
				"layer" : "#popupInfoBox",
				"mask" : 1,
				"cls"　: "a.close,a.submit,a.reset",
				"tt" : "提示"
			});
		},
		
		/**
		 * 弹出正在载入或提交小窗口，params参数如下：
		 * 
		 * info：	提示信息
		 * modal:	是否显示遮罩层
		 */
		loading : function(params){
			var info = params["info"];
			var modal = params["modal"];
			
			// 页面中添加弹出框的div
			var obj = $("#popupInfoBox");
			if(obj){
				obj.remove();
			}
			
			$(document.body).append("<div class='popUp-layer-auto' id='popupInfoBox' style='position:absolute;display:none;top:430px;left:400px;'></div>");
			
			// 添加div中显示的东西
			var htmlStr = "<p class='txt'><img src='" + Domain.static_path + "/home/static/images/loading_little.gif'>&nbsp;&nbsp;<span id='info'></span></p>";
				
			$("#popupInfoBox").html(htmlStr);
			$("#info").html(info);
			
			autoLayer({
				"layer" : "#popupInfoBox"
			});
		},
		
		/**
		 * 关闭正在载入或提交小窗口
		 */
		loaded : function(){
			Box.hideWin("#popupInfoBox");
		},
		
		/**
		 * 弹出登陆窗口
		 */
		showLoginWin : function(){
			var obj = $("#loginLayer");
			if(obj){
				obj.remove();
			}
			Sender.ajax({"url":"/open/openLogin.action", "data":{url:window.location.href}, "fn":function(data){
				if(data == null || data == ''){
					window.location.href=window.location.href;
				}else{
					$(document.body).append('<div class="login-layer ui-layer" id="loginLayer" style="display:block;width:300px;"></div>');
					var htmlStr = '<p class="login-close"><a href="javascript:;" class="fa fa-close fa-lg close" title="关闭"></a></p>'
						+'<div class="login-wrap" id="ui-login-index">'+data+'</div>';
					$("#loginLayer").html(htmlStr);
					
					// 显示div弹出框
					layer({
						"layer" : "#loginLayer",
						"mask" : 1,
						"cls"　: "a.close",
						"tt" : ""
					});
				}
			}});
			
		}

//		/**
//		 * 自定义对话框(临时加上的，播放视频使用)
//		 */
//		showDialog : function(title, info, hideCallBack, overlayer, width, className){
//		    // 页面中添加弹出框的div
//		    var obj = $(".popUp-layer");
//		    if(obj){
//		      obj.remove();
//		    }
//		    // 构造窗口内容
//		    var id = "popupInfoBox_" + new Date().getTime();
//		    // 添加div中显示的东西
//		    var htmlStr = 
//		       "<p class='tt'><a href='javascript:;' class='close'>关闭</a></p>"
//		       + "<p class='tit'>" + title + "</p>"
//		       + "<table border='0' cellspacing='0' cellpadding='0' class='popUp-table'>"
//		       + "     <tr class='top'><td class='l'></td><td class='bg'></td><td class='r'></td></tr>"
//		       + "     <tr class='middle'><td class='l'></td><td class='bg' style='width:480px;'> "+ info
//		       + "         </td><td class='r'></td></tr>"
//		       + "     <tr class='bottom'><td class='l'></td><td class='bg'></td><td class='r'></td>"               
//		       + "</tr></table>";                   
//		    $(document.body).append("<div class='popUp-layer' id='"+id+"'>" + htmlStr + "</div>");
//		    // 宽度
//		    if(width != null && width != ''){
//		      $("#info").css("width", width + "px");
//		    }
//		    // 添加外层样式
//		    if(className != null && className != ''){
//		      $("#info").addClass(className);
//		    }
//		    // 关联回调函数
//		    if(hideCallBack){
//		      $(".close").bind("click", hideCallBack);
//		    }
//		    // 显示div弹出框
//		    $("#" + id).jWindowOpen({
//		      modal:true,
//		      center:true,
//		      close: ".close,.closePopupLayer",
//		      closeHoverClass:"hover",
//		      transfererClass:"transferer"
//		    });
//		    
//		    // 去掉遮罩层
//		    if (!overlayer){
//		      $("#_______overlayer").remove();
//		    }
//		}
			
	};
	
	/**
	 * =============================== Box类：自定义内容的弹出窗口 ===============================
	 */
	window.Box = {
		/**
		 * 显示自定义窗口，多用于显示页面中隐藏的div，params参数如下：
		 * @param target	层显示的DIV在jQuery的selector名字，如id=openDiv的，则传入 #openDiv 不能空
		 * @param modal		是否显示遮罩层
		 * @param isCenter	是否居中
		 */
		showWin: function(params){
			var target = params["target"];
			var modal = params["modal"];
			var isCenter = params["isCenter"];
			
			var obj = null;
			if (typeof(target) == "string") {
				obj = $(target);
			}else{
				obj = target.attr ? target : $(this);
			}
			if(isCenter == undefined){
				isCenter = true;
			}
			
			layer({
				"layer" : obj,
				"cls"　: ".close,.cancel",
				"tt" : "提示"
			});
			// 非居中显示时设置初始位置
			if(isCenter == false){
				var divHeight = $(target).height();
				var divWidth = $(target).width();
				var bodyHeight = $(window).height();
				var bodyWidth = $(window).width();
				if (bodyHeight > divHeight && bodyWidth > divWidth){
					$(target).css("margin", (bodyHeight - divHeight)/2+$(window).scrollTop()+"px");
					$(target).css("margin-left", (bodyWidth - divWidth)/2+$(window).scrollLeft()+"px");
				}else{
					$(target).css("marginTop", "0px");
					$(target).css("marginBottom", "0px");
				}
			}
		},
		
		/**
		 * 隐藏自定义窗口，与Box.showWin搭配使用，隐藏Box.showWin打开的窗口
		 */
		hideWin: function(target){
			if (target && typeof(target) == "string") {
				$(target).hide();
				$("#_______overlayer").remove();// 去掉遮罩层
			} else {
				$(".close").click();
			}
		},
		
		/**
		 * 移除自定义窗口，与Box.showWin搭配使用，移除Box.showWin打开的窗口
		 */
		removeWin: function(target){
			if (target && typeof(target) == "string") {
				$(target).remove();
				$("#_______overlayer").remove();// 去掉遮罩层
			} else {
				$(".close").click();
			}
		},
		
		/**
		 * 显示Div弹出层窗口，多用于打开编辑窗口，被Sender.openDiv方法调用，params参数如下：
		 * 
		 * @param div			显示的DIV在jQuery的selector名字，如id=divId的，则传入#divId，不能空
		 * @param closeObject	关闭的控件在jQuery的selector名字，如id=closeBu的，则传入#closeBut，可以空
		 * @param scroll		是否需要滚动
		 * @param className		内部的className 如果需要设置滚动 则必须填写
		 * @param height		内部的高度超过此高度则需要 否则不需要
		 * @param closeHandler	弹出窗口关闭后的回调函数（包括消息窗口）
		 */
		showDiv: function(params) {
			var div = params["div"];
			var closeObject = params["closeObject"];
			var scroll = params["scroll"];
			var className = params["className"];
			var height = params["height"];
			var closeHandler = params["closeHandler"];
			
			
			// 成功时关闭原弹出框，再弹出成功提示框
			var handler = closeHandler;
			if (div == '#panelWindow_success') {
				var popupObj = $('#container .popUp-layer:not(".popUp-layer-tips,.keep-div")');
				if (null != popupObj && popupObj.length != 0) {
					popupObj.hide();
				}
				var handler = function() {
					if (closeHandler != null) {
						closeHandler();
					}
				}
			}
			
			if (div == '#panelWindow_confirm') {
				if(closeHandler){
					$(".submit").unbind("click");
					$(".submit").bind("click", handler);
				}
				
				layer({
					"layer" : $(div),
					"cls"　: closeObject,
					"tt" : "提示"
				});
			}else{
				if(closeHandler){
					$(closeObject).unbind("click");
					$(".submit").bind("click", closeHandler);
				}
				
				layer({
					"layer" : $(div),
					"cls"　: closeObject,
					"tt" : "提示"
				});
			}
			
			$(div).css({
						"zIndex" : (101000 + 500)
					});
			if (scroll) {
				var myHeight = $(className).height();
				if (height) {
					if (myHeight > height) {
						$(className).css({
									'height' : height + 'px'
								});
						$(className).jscroll({
							W : "5px"// 设置滚动条宽度
							,
							Bar : {
								Pos : ""// 设置滚动条初始化位置在底部
								,
								Bd : {
									Out : "#999fa5",
									Hover : "#5b5c5d"
								}// 设置滚动滚轴边框颜色：鼠标离开(默认)，经过
								,
								Bg : {
									Out : "#999fa5",
									Hover : "#67686a",
									Focus : "#67686a"
								}
							}// 设置滚动条滚轴背景：鼠标离开(默认)，经过，点击
							,
							Btn : {
								btn : false
							}// 是否显示上下按钮 false为不显示
						});
					}
				} else {
					$(className).jscroll({
						W : "5px"// 设置滚动条宽度
						,
						Bar : {
							Pos : ""// 设置滚动条初始化位置在底部
							,
							Bd : {
								Out : "#999fa5",
								Hover : "#5b5c5d"
							}// 设置滚动滚轴边框颜色：鼠标离开(默认)，经过
							,
							Bg : {
								Out : "#999fa5",
								Hover : "#67686a",
								Focus : "#67686a"
							}
						}// 设置滚动条滚轴背景：鼠标离开(默认)，经过，点击
						,
						Btn : {
							btn : false
						}// 是否显示上下按钮 false为不显示
					});
				}
			}
		},
		
		/**
		 * 关闭Div弹出层窗口，与Box.showDiv搭配使用，关于Box.showDiv打开的弹出层
		 * 
		 * @param div		要关闭的DIV在jQuery的selector名字，如id=divId的，则传入#divId，不能空
		 * @param handler	回调方法
		 */
		closeDiv: function(div, handler) {
			if (!div || div == "") {
				return;
			}
			if ($(div).is(":visible") == true) {
				$(div).remove();
				$('.ui-layer-mask').remove();;// 去掉遮罩层
				$(".close").click();
				if(typeof($(div).defaults) != 'undefined' && $(div).defaults.closeHandler != ""){
					return;
				}
				if (handler instanceof Function) {
					eval(handler)();
				} else {
					eval(handler);
				}
			}
		}
		
	};
	
	
	/**
	 * =============================== FieldMsg类：自定义内容的弹出窗口 ===============================
	 */
	window.FieldMsg = {
		/**
		 * 把错误信息加到页面输入框上，支持多行编辑出错提示，支持有内容输入后自动去掉错误提示
		 * 
		 * @param fieldName	可以传入id、name、对象、或#id，比如fid、fname、$(fid)，非空
		 * @param msg		提示内容
		 * @param isBlur	是否在输入框中添加blur事件（用在base_verify.js中check方法）
		 * @param rowNum	只有列表形式的时候需要传入，一般的表单不需要。
		 */
		addFieldError: function (fieldName, msg, isBlur, rowNum) {
			msg=msg.toString();
			var obj;
			if (rowNum || rowNum == 0) {// 第一行按照从0开始计算
				obj = $("input[name:" + fieldName + "]")[rowNum];
			} else {
				if (typeof(fieldName) == "string") {
					if(StringUtil.startWith(fieldName, "#")){
						obj = $(fieldName);
					}else{
						obj = $("#" + fieldName);
					}
				} else {
					obj = fieldName;
				}
			}
			
			// 显示红色框
			if($(obj)[0].tagName=="SELECT"){
				$(obj).addClass('input-sel-error');
			}else{
				$(obj).addClass('input-txt-error');
			}

			// 找到当前元素的父元素
			var parent;
			if ($(obj).parent().hasClass('select-analog')) {
				parent = $(obj).parent();
			} else {
				parent = $(obj);
			}
			// 父元素后面显示出错提示文字
			if (parent.parent().find(".field_tip").length == 0) {
				if($(obj).parent()[0].tagName=="DIV"){
					parent.after("<span class='field_tip onError fn-left' style='line-height:26px'>" + msg + "</span>");
				}
				else if($(obj).hasClass('text-area')){
					parent.after("<span class='field_tip onError fn-left'>" + msg + "</span>");
				}
				else{
					parent.after("<span class='field_tip onError'>" + msg + "</span>");
				}
				
				if (isBlur) {
					// 取调用本函数的父函数
					var parentFun = FieldMsg.addFieldError.caller;
					// 取父函数的参数
					var arg = parentFun.arguments;
					// 组装参数
					var argLen = arg.length;
					var param = "";
					for(var i=0; i<argLen; i++){
						if(typeof(arg[i]) == "string"){
							param += "'" + arg[i] + "'";
						}else{
							param += arg[i];
						}
						if(i < argLen -1){
							param += ",";
						}
					}

					// 组装父函数为字可执行的字符串
					var fun = "("+parentFun.toString()+")("+param+");";
					
					// 当前对象绑定onblur事件
					$(obj).on("blur", $(obj), function() {
						// 再次执行父函数检验是否满足
						if (eval(fun)) {
							
							// 隐藏提示信息
							parent.parent().find(".field_tip").remove();
							
							//清除出错时的focus红框
							if($(obj)[0].tagName=="SELECT"){
								$(obj).removeClass('input-sel-error');
							}else{
								$(obj).removeClass('input-txt-error');
							}
						}
					});
				}
			} 
			else {
				parent.parent().find(".field_tip").removeClass("onCorrect").addClass("onError").html(msg).show();
			}
		},

		/**
		 * 把错误信息加到指定的一系列字段上
		 * 
		 * @param fields	对应的一组id值
		 * @param objId		id前缀
		 * @param msg		提示信息
		 */
		addFieldErrors: function (fields, objId, msg) {
			var objclear = document.all;
			if (objclear != null) {
				for (var i = 0; i < objclear.length; i++) {
					if (objclear[i].fielderror != '') {
						if (objclear[i].tagName == "SELECT") {
							objclear[i].style.color = "";
						} else {
							objclear[i].style.borderColor = "";
						}
					}
				}
			}
			for (var i = 0, len = fields.length; i < len; i++) {
				fieldName = objId + fields[i];
				obj = obj = $("#" + fieldName);
				obj.fielderror = msg;
				if (obj.tagName == "SELECT") {
					obj.style.color = "red";
				} else {
					obj.style.borderColor = "red";
				}
				if (i == 0) {
					showMsgError(msg, "", function() {
						if (obj.getAttribute("type") != "hidden"
								&& (!obj.getAttribute("dataType") || obj
										.getAttribute("dataType") != "date"))
							obj.focus();
					});
					return;
				}
			}
		},
		
		/**
		 * 把成功信息加到页面输入框上，支持多行编辑提示
		 * 
		 * @param fieldName	可以传入id或是对象，比如fid或$(fid)都可以
		 * @param rowNum	只有列表形式的时候需要传入，一般的表单不需要。
		 */
		addFieldSuccess: function (fieldName, rowNum) {
			var obj;
			if (rowNum || rowNum == 0) {// 第一行按照从0开始计算
				obj = document.getElementsByName(fieldName)[rowNum];
			} else {
				if (typeof(fieldName) == "string") {
					if(StringUtil.startWith(fieldName, "#")){
						obj = $(fieldName);
					}else{
						obj = $("#" + fieldName);
					}
				} else {
					obj = fieldName;
				}
			}
			//边框颜色改变无此样式
			//$(obj).addClass('input-txt-correct');

			var parent;
			if ($(obj).parent().hasClass('select-analog')) {
				parent = $(obj).parent();
			} else {
				parent = $(obj);
			}
			if (parent.parent().find(".field_tip").length == 0) {
				parent.after("<span class='field_tip onCorrect' style='line-height:26px'>&nbsp</span>");
			} else {
				parent.parent().find(".field_tip").removeClass("onError")
						.addClass("onCorrect").html("&nbsp").show();
			}
		},
		
		/**
		 * 清除addFieldError方法添加的错误信息
		 * 
		 * @param obj		容器在jquery中的selector或object，如id=testDiv的，则传入#testDiv，可以为空，为空时表示清除当前页面中所有输入框中的提示信息
		 */
		clearFieldError: function (obj) {
			var tgs = ["INPUT:not(:file)", "SELECT", "TEXTAREA"];
			var len = tgs.length;
			for (var j = 0; j < len; j++) {
				if (obj) {
					if (typeof(obj) == "string") {
						var os = jQuery(obj + " " + tgs[j]);
					} else {
						len = 1;// 针对单一确定元素
						var os = jQuery(obj);
					}
				} else {
					var os = jQuery(tgs[j]);
				}
				if (os) {
					for (var i = 0; i < os.length; i++) {
						var o = os[i];

						// 清除focus框
						if (o.tagName == "DIV") {
							$(o).parent().removeClass('input-txt-error');
						} else {
							if(o.tagName == "SELECT"){
								$(o).removeClass('input-sel-error');
							}else{
								$(o).removeClass('input-txt-error');
							}
						}

						var parent;
						if ($(o).parent().hasClass('select-analog')) {
							parent = $(o).parent().parent();
						} else {
							parent = $(o).parent();
						}
						//富文本框编辑器兼容问题处理
						if($(o).attr("id")!="ueditor_textarea_content"){
							parent.find(".field_tip").remove();
						}
						

					}
				}
			}
		},
		
		/**
		 * 以弹出窗口方式显示actionError内容
		 * 
		 * @param msg		要显示的内容
		 * @param callFn	点击弹出窗口的“确定”按钮后调用该回调函数
		 * 
		 */
		addActionError: function (msg, callFn) {
			if (typeof(callFn) == "function") {
				Tips.showErrorWin({"info":msg,"callFn":callFn});
			} else {
				Tips.showErrorWin({"info":msg});
			}
		},
		
		/**
		 * 以弹出窗口方式显示actionMessage内容
		 * 
		 * @param msg		要显示的内容
		 * @param callFn	点击弹出窗口的“确定”按钮后调用该回调函数
		 */
		addActionMessage: function (msg, callFn) {
			if (typeof(callFn) == "function") {
				Tips.showWarnWin({"info":msg,"callFn":callFn});
			} else {
				Tips.showWarnWin({"info":msg});
			}
		},

		/**
		 * 对ajax返回的消息对象进行页面显示（返回的是ReplyDto对象的json格式）
		 * 
		 * @param reply			返回ReplyDto对象的json格式
		 * @param successFn		提示成功后执行的回调函数
		 * @param errorFn		提示出错后执行的回调函数
		 * 
		 */
		drawMessages: function (reply, successFn, errorFn) {
			if(reply.isSuccess){
				//提示成功
				var params;
				if(successFn){
					params = {"info":reply.promptMsg, "callFn":successFn};
				}else{
					params = {"info":reply.promptMsg};
				}
				Tips.showSuccessMsg(params);
			}else{
				//提示出错
				if(Validator.isNotBlank(reply.errorMsg)){
					var params1;
					if(errorFn){
						params1 = {"info":reply.errorMsg, "callFn":errorFn};
					}else{
						params1 = {"info":reply.errorMsg};
					}
					Tips.showErrorMsg(params1);
			   	}else if(reply.fieldErrorMap != null){
			   		$.each(reply.fieldErrorMap, function(key, value){
			   			FieldMsg.addFieldError(key,value+"",false);
			   		});
			   	}
				return;
			}
		}
		
		
	};
	
	
	/**
	 * =============================== util类：常用的js工具方法 ===============================
	 */
	window.Util = {
		// 跳转到指定的页面
		winGo: function(url) {
			window.location.href = url;
		},
			
		// 页面后退
		winBack: function(pos){
			if(Validator.isNotEmpty(pos)){
				window.history.back(pos);
			}else{
				window.history.back();
			}
		},
		
		// 页面内滚动条跳到指定锚点
		winBackTo: function(to, animate) {
			var top = to ? to.offset().top : 0;
			if (typeof(animate) == "undefined") {
				animate = true;
			}
			if (animate) {
				$('html,body').animate({scrollTop: top}, 'slow');
			} else {
				$(window).scrollTop(top);
			}
		},
		
		// 重新加载
		winReload: function() {
			window.location.reload();
		},
		
		// 关闭页面
		winClose: function(){
			window.opener=null;
			window.open('','_self');
			window.close();
		},
		
		// 获取浏览器当前地址栏某一参数值，如地址栏是http://domain/test.htm?unitId=15，则getUrlParams("unitId")=15
		getUrlParams: function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		    var result = window.location.search.substr(1).match(reg);
		    if (result != null) {
		    	return unescape(result[2]); 
		    }
		    return null;
		},
		
		// 添加地址栏参数
		setUrlParams: function(url, name, value){
			var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)","i");
			var match = url.match(reg);
			if (match) {
				return (url.replace(reg, function($0, $1, $2) {
					return ($0.replace($2,value));
				}));
			} else {
				if(url.indexOf('?')==-1) {
					return (url+'?'+name+'='+value);
				} else {
					return (url+'&'+name+'='+value);
				}
			}
		},
		
		// 判断url中是否有某个参数
		hasParams: function(name) {
			name = "?" + name;
			return window.location.search.indexOf(name) >= 0;
		},
		
		// 获取单选框或者复选框选中的值
		getCheckedValues: function(name) {
			var values = new Array();
			var type = $("input[name='" + name + "']").attr("type");
			if (type == "radio") {
				var value = $("input[name='" + name + "']:checked").val();
				if (typeof(value) != "undefined") {
					return value;
				}
				return null;
			} else if (type == "checkbox") {
				$("input[name='" + name + "']:checked").each(function(){
					values.push($(this).val());
				})
			}
			return values;
		},
		
		// 复选框全选
		setCheckedAll: function(obj, name) {
			$("input[name=" + name + "]").attr("checked", obj.checked);
		},
		
		// 鼠标悬停/离开添加样式
		hover: function(obj, cls){
			obj.hover(function(){
				$(this).addClass(cls);
			}, function(){
				$(this).removeClass(cls);
			});
		},
		
		// 获取当前点击的元素
		getElement: function(e) {
			e = window.event || e;
			return e.srcElement || e.target;
		},
		
		// 获取当前点击元素的tagName值
		getElementTagName: function(e) {
			return Util.getElement(e).tagName;
		}
		
	};
	
	
	/**
	 * ========================= string util类：字符串操作工具方法 ===============================
	 */
	window.StringUtil = {
		// 去掉字符串前后空格
		trim:function (str) {
			if(str !='' && str != null){
				//return str.replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' ); //（兼容IE7,8文本模式）
				return str.replace(/(^\s*)|(\s*$)/g, "");
			}
			return str;
		},
		
		// 去掉字符串左侧空格
		ltrim:function (str) {
			if(str !='' && str != null){
				return str.replace(/(^\s*)/g,"");
			}
			return str;
		},
		
		// 去掉字符串右侧空格
		rtrim:function (str) {
			if(str !='' && str != null){
				return str.replace(/(\s*$)/g,"");
			}
			return str;
		},
			
		// 获取字符宽度，一个中文算1个字符宽度，两个英文算1个字符宽度（用于截取多少个字符显示在页面上）
		getWidth: function(value) {
			if (!value) {
				return 0;
			}
			// 字符长度
			var allLen = value.length;
			// 英文字符长度
			var enLen = value.replace(/[^\x00-\xff]/g,"").length;
			return (enLen % 2 == 0 ? enLen / 2 : parseInt(enLen / 2) + 1) + (allLen - enLen);
		},
		
		// 获取字符数，一个中文算1个字符，一个英文算1个字符（用于mysql库时）
		getLength: function(value) {
			if (!value) {
				return 0;
			}
			// 字符长度
			var length = StringUtil.trim(value).length;
			return length;
		},
		
		// 获取字节数，一个中文算2个字节，一个英文算1个字节（用于oracle库gbk编码时）
		getLength2: function(value) {
			if (!value) {
				return 0;
			}
			var len=0;
			// 字符长度
			var length = StringUtil.trim(value).length;
			for (i=0;i<length;i++){
				if (value.charCodeAt(i)>255){len+=2;} else {len++;}
			}
			return len;
		},
		
		// 获取字节数，一个中文算3个字节，一个英文算1个字节（用于oracle库utf-8编码时）
		getLength3: function(value) {
			if (!value) {
				return 0;
			}
			var len=0;
			// 字符长度
			var length = StringUtil.trim(value).length;
			for (i=0;i<length;i++){
				if (value.charCodeAt(i)>255){len+=3;} else {len++;}
			}
			return len;
		},
		
		// 判断是否以指定字符串开头
		startWith: function(parentStr, str){
			return (parentStr.match("^" + str) == str);
		},
		
		// 判断是否以指定字符串结尾
		endWith: function(parentStr, str){
			return (parentStr.match(str + "$") == str);
		},
		
		// 中文编码
		encodeURI: function(value) {
			if (!value) {
				return "";
			}
			value = encodeURIComponent(value);
			return value;
		},
		
		// 对HTML的<>符号转义
		encodeForHTML: function(value) {
			if (!value) {
				return "";
			}
			value = value.replace(/</g, "&lt;").replace(/>/g, "&gt;");
			return value;
		},
		
		// 对HTML的<>符号转义还原
		decodeForHTML: function(value){
			if (!value) {
				return "";
			}
			value = value.replace(/&lt;/g, "<").replace(/&gt;/g, ">");
			return value;
		},
			
		// 对html的所有标签符号进行转义
		htmlFilter: function(str){
			return str.replace(/&/g, "&amp;")
		    .replace(/</g, "&lt;")
		    .replace(/>/g, "&gt;")
		    .replace(/ /g, "&nbsp;")
		    .replace(/'/g, "&#0311")
		    .replace(/\"/g, "&quot;");
		},
		
		// 显示数据产生的时间，格式如：12分种前、54秒前、今天12:10
		getTimeStr: function(current, date){
			var time = current - date;
			if (time < 60000) {
				return parseInt(time / 1000) + "秒前";
			}
			if (time < 3600000) {
				return parseInt(time / 60000) + "分钟前";
			}
			if (time < 86400000) {
				var now1 = new Date(current);
				var now2 = new Date(date);
				if (now1.getDate() != now2.getDate()) {
					return null;
				}
				var hours = now2.getHours();
				var minutes = now2.getMinutes();
				if (hours < 10) {
					hours = "0" + hours;
				}
				if (minutes < 10) {
					minutes = "0" + minutes;
				}
				return "今天" + hours + ":" + minutes;
			}
			return null;
		},
		
		/**
		 *  比较日期的大小，字符串格式：2015-4-5
		 *  @param d1 前面的日期
		 *  @param d2 后面的日期
		 *  @return 1表示d1>d2，0表示d1==d2，-1表示d1<d2
		 */
		compareDate: function (d1, d2){
			if(Validator.isNotBlank(d1) && Validator.isNotBlank(d2)){
				var date1 = d1.split('-');
		 		var date2 = d2.split('-');
		 			
		 		if(eval(date1[0])>eval(date2[0])){
		 		 	return 1;
		 		}else if(eval(date1[0])==eval(date2[0])){
		 		 	if(eval(date1[1])>eval(date2[1])){
		 		 		return 1;
		 		 	}else if(eval(date1[1])==eval(date2[1])){
		 		 	    if(eval(date1[2])>eval(date2[2])){
		 			 		return 1;
		 			 	}else if(eval(date1[2])==eval(date2[2])){
		 			 		return 0;
		 			 	}else{
		 			 	    return -1;
		 			 	}	
		 		 	}else{
		 		 		return -1;
		 		 	}
		 		}else{
		 		 	return -1;
		 		}
			}
		},
		
		// 去掉字符串前面的0
		trimHeadZero: function (str) {
		    return str.replace(/(^0+)/g, "");
		},
		
		//根据身份证获取生日，格式：YYYY-MM-DD（注：不会验证身份证的合法性，也不会验证获得的出生日期合法性）
		getBirthdayIdcard: function (value){
			var birth='';
			var val = StringUtil.trim(value);
			var len = StringUtil.getLength(val);
			if(len == 0) return birth;
			
			if(len == 15){
				birth='19'+val.substring(6,8);
				birth=birth+'-'+val.substring(8,10);
				birth=birth+'-'+val.substring(10,12);
			}
			else if(len == 18){
				birth=val.substring(6,10);
				birth=birth+'-'+val.substring(10,12);
				birth=birth+'-'+val.substring(12,14);
			}
			return birth;
		},
		
		// 根据身份证获取性别：1男，2女（注：不会验证身份证的合法性）
		getSexIdcard: function (value){
			var sex='';
			
			var val = StringUtil.trim(value);
			var len = StringUtil.getLength(val);
			if(len == 0) return sex;
			
			var sexindex = 16;
			if(len == 15){
				sexindex=14;
			}
			sex=val.substring(sexindex, sexindex+1);
			if(Validator.isNumber(sex)){
				if(sex%2==0){
					sex = "2";
				}
				else{
					sex = "1";
				}
			}
			return sex;
		},
		
		//截取字符串,包含中文处理 （str：字符串；len：截取长度）
		subStringChinese: function(str, len){
			var chineseRegex = /[^\x00-\xff]/g;  
			var newLength = 0;  
		    var newStr = "";  
		    var singleChar = "";  
		    var strLength = StringUtil.getLength2(str);
		    if(strLength < len){
		    	return str;
		    }else {
		    	for(var i=0; i<strLength; i++) {
			        singleChar = str.charAt(i).toString();  
			        if(singleChar.match(chineseRegex) != null){  
			            newLength += 2;  
			        }else {  
			            newLength++;  
			        }  
			        if(newLength > len){  
			            break;  
			        }  
			        newStr += singleChar;  
			    }  
			    return newStr + "...";  
		    }
		}
		
	};
	
	
	/**
	 * =============================== Validator类：验证值的合法性 ===============================
	 */
	window.Validator = {
		// 判断字符串非空（空格表示非空）
		isNotBlank: function(value){
			return (value!= undefined && value != null && value.length > 0);
		},
		// 判断字符串非空（空格表示空）
		isNotEmpty: function(value){
			return (value!= undefined && value != null && $.trim(value).length > 0);
		},
		// 判断是否为空串或空格（""或" "返回true，null或有值返回false）
		isBlank: function(value){
			return Validator.test(/^\s*$/g, value);
		},
		// 判断是否为null或空串或空格（即：没有确切值，null或""或" "返回true，有值返回false）
		isEmpty: function(value){
			if(value){
				return Validator.isBlank(value);
			}
			return true;
		},
		// 是否为数字（正数或0）
		isNumber: function(value) {
			return Validator.test(/^\d+$/, value);
		},
		// 是否为整数（包括正数，负数和0）
		isInteger: function(value){
			return Validator.test(/^[\-\+]?\d+$/, value);
		},
		// 是否为大于0的整数
		isPlusInt: function(value){
			return Validator.test(/^[1-9]*[0-9][0-9]*$/, value);
		},
		// 是否为小数，dl表示几位小数
		isFloat: function(value, dl){
			if(!dl){
				var p = /(^\+?\d+\.\d+$)|(^\+?\d+$)|(^\-\d+\.*\d+$)|(^\-\d+$)/;
				return Validator.test(p, value);
			}
			else{
				var _p = "(^\\+?\\d+\\.\\d{1," + dl + "}$)|(^\\+?\\d+$)|(^\\-\\d+\\.*\\d{1," + dl + "}$)|(^\\-\\d+$)";
				var rg = new RegExp(_p);
				return Validator.test(rg, value);
			}
		},
		// 是否为大于等于0的小数，dl表示几位小数
		isNotMinusFloat: function(value, dl){
			if(!dl){
				var p = /(^\+?\d+\.\d+$)|(^\+?\d+$)/;
				return Validator.test(p, value);
			}
			else{
				var _p = "(^\\+?\\d+\\.\\d{1," + dl + "}$)|(^\\+?\\d+$)";
				var rg = new RegExp(_p);
				return Validator.test(rg, value);
			}
		},
		
		// 是否为字母（包括大小写）
		isLetter: function(value) {
			return Validator.test(/^[A-Za-z]+$/, value);
		},
		// 判断输入的文字是否为中文、英文或数字
		isValidText: function(value) {
			return Validator.test(/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/, value);
		},
		// 判断是否为数字或字母
		isLetterOrNum: function(value){
			return Validator.test(/[a-z]|[A-Z]|[0-9]/, value);
		},
		// 判断是否为数字、字母或下划线
		isLetterOrNumOrUnLine: function(value){
			return Validator.test(/^[a-zA-Z0-9_]+$/, value);
		},
		// 用户名校验，字母数字和下划线组成，4-25位
		isUserName: function(value){
			return Validator.test(/^[a-zA-Z0-9_]{4,25}$/, value);
		},
		// 图片格式验证jpg|jpeg|gif|png
		isImage: function(value) {
			return Validator.isFile("\.(?:jpg|jpeg|gif|png)$$", value);
		},
		// 文档格式验证doc|docx|xlsx|xls
		isDoc: function(value) {
			return Validator.isFile("\.(?:doc|docx|xlsx|xls)$$", value);
		},
		// 文档格式验证xlsx|xls
		isXls: function(value) {
			return Validator.isFile("\.(?:xlsx|xls)$$", value);
		},
		// 判断是否含非法字符[<>&~!@#$%|]
		hasIllegalChar: function(value) {
			return Validator.test(/[\<\>\&~!@#$%|]/, value);
		},
		// 是否为电话号码
		isPhone: function(value) {
			return Validator.test(/^((0[1-9]{2}[0-9])?(0[12][0-9])?[-])?\d{6,8}$/, value);
		},
		// 是否为手机号码
		isMobile: function(value) {
			return Validator.test(/^((13[0-9])|(14[5|7])|(15[0-3,5-9])|(18[0-9])|(17[0-9]))\d{8}$/, value);
		},
		// 是否为邮箱地址
		isEmail: function(value){
			return Validator.test(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, value);
		},
		// 是否为QQ
		isQQ : function(value){
			return Validator.test(/^[1-9]\d{4,10}$/, value);
		},
		// 是否为15位身份证号
		isIdCard15 : function(value){
			return Validator.test(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/, value);
		},
		// 是否为18位身份证号
		isIdCard18 : function(value){
			return Validator.test(/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$/, value);
		},
		// 判断是否为http://IP:PORT地址格式
		isValidHttpIpPort: function(value){
			return Validator.test(/^http:\/\/\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\:\d{2,10}$/, value);
		},
		// 判断是否为有效的URL
		isValidUrl: function(value){
			return Validator.test(/^(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?$/, value);
		},
		// 判断输入的课程默认图片code和中文对应关系,格式是否如 xx:小学;cz:初中;gz:高中
		isPicTypeValue: function(value) {
			return Validator.test(/^([\s\S]+:[\u4e00-\u9fa5]+)(;[\s\S]+:[\u4e00-\u9fa5]+)?$/, value);
		},
		// 验证字符串是否符合文件的正则表达式
		isFile: function(reg, value) {
			if (!value) {
				return false;
			}
			value = value.toLowerCase();
			var index = value.indexOf("?");
			if (index != -1) {
				value = value.substring(0, index);
			}
			return RegExp(reg, "i").test(value);
		},
		// 验证字符串是否符合正则表达式
		test: function(reg, value) {
			if (!value) {
				return false;
			}
			return reg.test(value);
		},
		
		/**
		  * 校验日期格式：YYYY-MM-DD
		  */
		 isDateFormat: function (value){
			 var DateValue;
		     var seperator = "-";
		     var day;
		     var month;
		     var year;
		     var leap = 0;
		     var err = 0;
		     var re = /[^0-9\-]+/gi;

		     DateValue =  value.replace(re, "");   //去除所有数字和'-'以外的字符
		     var parts = DateValue.split('-');

		     VALIDATION: {
		         var len = parts.length;
		         if(len != 3) {
		             err = 19;
		             break VALIDATION;
		         }
		       
		 		 if(parts[0].length != 4) {
		             err = 100;
		             break VALIDATION;
		         }        
		         year = parseInt(StringUtil.trimHeadZero(StringUtil.trim(parts[0])), 10);

		         if ((isNaN(year))|| (year == 0)) {
		             err = 20;
		             break VALIDATION;
		         }
		         /* Validation of month*/
		         month = parseInt(StringUtil.trimHeadZero(StringUtil.trim(parts[1])), 10);
		         if ((isNaN(month))|| (month < 1) || (month > 12)) {
		             err = 21;
		             break VALIDATION;
		         }
		         /* Validation of day*/
		         day = parseInt(StringUtil.trimHeadZero(StringUtil.trim(parts[2])), 10);
		         if ((isNaN(day))|| (day < 1)) {
		             err = 22;
		             break VALIDATION;
		         }

		         /* Validation leap-year / february / day */
		         if ((year % 4 == 0) || (year % 100 == 0) || (year % 400 == 0)) {
		             leap = 1;
		         }
		         if ((month == 2) && (leap == 1) && (day > 29)) {
		             err = 23;
		             break VALIDATION;
		         }
		         if ((month == 2) && (leap != 1) && (day > 28)) {
		             err = 24;
		             break VALIDATION;
		         }
		         /* Validation of other months */
		         if ((day > 31) && ((month == "01") || (month == "03") || (month == "05") || (month == "07") || (month == "08") || (month == "10") || (month == "12"))) {
		             err = 25;
		             break VALIDATION;
		         }
		         if ((day > 30) && ((month == "04") || (month == "06") || (month == "09") || (month == "11"))) {
		             err = 26;
		             break VALIDATION;
		         }
		         /* if 00 ist entered, no error, deleting the entry */
		         if ((day == 0) && (month == 0) && (year == 00)) {
		             err = 0; day = ""; month = ""; year = ""; seperator = "";
		         }
		     }
		     /* if no error, write the completed date to Input-elem (e.g. 13.12.2001) */
		     if (err == 0) {    	
		     	if (StringUtil.compareDate(year + seperator + month + seperator + day, "1900" + seperator + "1" + seperator + "1") < 0){
		 			return false;
		     	}
		     	if (StringUtil.compareDate("2038" + seperator + "12" + seperator + "31", year + seperator + month + seperator + day) < 0){
		 			return false;
		     	}
		     }else {
		 		return false;
		     }
			 
		     return true;
		 }
		
		
	};
	
	/**
	 * =============================== Browser类：浏览器相关的函数 ===============================
	 */
	window.Browser = {
		// 是否IE
		IE : !!( window.attachEvent && !window.opera ),
		// 是否IE6
		IE6 : navigator.userAgent.indexOf( 'MSIE 6.0' ) > -1,
		// 是否IE7
		IE7 : navigator.userAgent.indexOf( 'MSIE 7.0' ) > -1,
		// 是否Opera
		Opera : !!window.opera,	
		// 是否WebKit
		WebKit : navigator.userAgent.indexOf( 'AppleWebKit/' ) > -1,
		// 是否Gecko
	    Gecko : navigator.userAgent.indexOf( 'Gecko' ) > -1 && navigator.userAgent.indexOf( 'KHTML' ) == -1,
	    // 是否Chrome
	    Chrome : navigator.userAgent.indexOf( 'Chrome' ) > -1,
	    
	    // 将string复制到剪贴板
	    copy : function( o ){
			function onfail(){
				if ( Object.isElement( o ) ){
					o.select();
				}
			}
			var str;
			if ( Object.isElement( o ) ){
					str = o.value;
				}else{
					str = o;
				}
		  
			if ( window.clipboardData && clipboardData.setData ){
			  if ( clipboardData.setData( 'text', str ) ) return true;
			}else{
			  Tips.showErrorMsg({"info":"您的浏览器不支持脚本复制,请尝试手动复制"});
			  return false;
			}
		    Tips.showErrorMsg({"info":"您的浏览器设置不允许脚本访问剪切板"});
		    return false;
	    }
	};
	
	
	/**      
	 * ===============================  时间格式化  ===============================
	 * 对Date的扩展，将 Date 转化为指定格式的String      
	 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符      
	 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)      
	 * eg:      
	 * (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423      
	 * (new Date()).format("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04      
	 * (new Date()).format("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04      
	 * (new Date()).format("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04      
	 * (new Date()).format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18     
	 */        
	Date.prototype.format=function(fmt) {        
	    var o = {         
	    "M+" : this.getMonth()+1, //月份         
	    "d+" : this.getDate(), //日         
	    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
	    "H+" : this.getHours(), //小时         
	    "m+" : this.getMinutes(), //分         
	    "s+" : this.getSeconds(), //秒         
	    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
	    "S" : this.getMilliseconds() //毫秒         
	    };         
	    var week = {         
	    "0" : "/u65e5",         
	    "1" : "/u4e00",         
	    "2" : "/u4e8c",         
	    "3" : "/u4e09",         
	    "4" : "/u56db",         
	    "5" : "/u4e94",         
	    "6" : "/u516d"        
	    };         
	    if(/(y+)/.test(fmt)){         
	        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
	    }         
	    if(/(E+)/.test(fmt)){         
	        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
	    }         
	    for(var k in o){         
	        if(new RegExp("("+ k +")").test(fmt)){         
	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
	        }         
	    }         
	    return fmt;         
	}
})(window);


/**
 * =============================== 自定义页面对象的扩展函数 ===============================
 */
(function($){
	$.fn.extend({
		/**
		 * 在提交元素上绑定该方法，防止重复提交（全部post方式）
		 * 
		 * @param url  		提交数据的地址，必须·
		 * @param data		提交的数据(json格式或&分隔的key=value串，如：unitId=1&userId)，必须
		 * @param fn 		提交数据成功时返回页面后的回调函数，可以为空
		 * @param dataType 	表示返回数据类型（text,json,jsonp，默认text），可以为空
		 * @param async		是否异步提交，默认同步，可以为空
		 * @param fne		提交数据失败时返回页面后的回调函数，可以为空
		 * 
		 */
		onAjax: function(params) {
			var url = params["url"];
			var data = params["data"];
			var fn = params["fn"];
			var async = params["async"];
			var dataType = params["dataType"];
			var fne = params["fne"];
		
			var that = $(this);
			if (that.data("pending")) {
				return;
			}
			that.data("pending", true);
			
			if (async != undefined && !async){
				async = false;
			}
			$.ajax({
				type: 'post',
				url: url, 
				async: async,
				data: data,
				dataType: dataType || 'text',
				success: function(result){
					that.data("pending", false);
					Sender.dealResult(result, fn, fne);
				},
				error: function(obj, status){
					that.data("pending", false);
					// window.loaction.href = Domain.train + "/error.htm";
					Sender.dealResult(obj.responseText, null, fne);
				}
			});
		},
		
		// 注册回车响应事件
		onEnter: function(fn) {
			this.keyup(function(e){
				e = window.event || e;
				var code = e.which || e.keyCode;
				if (code == 13 && fn) {
					fn();
				}
			});
		},
		
		// 获取输入框中含有占位符的内容，过滤掉占位符
		getVal: function() {
			var obj = $(this);
			var val = obj.val();
			if (val == obj.attr("placeholder")) {
				return "";
			}
			return val;
		},
		
		// 设置元素值，并执行回调函数(兼容IE6)
		setVal: function(val, fn) {	
			var ele = $(this);
			setTimeout(function(){
				ele.val(val);
				if (fn) {
					fn();
				}
			}, 1);
		},
		
		// 设置对象最小高度
		minHeight: function(value){
			value = value + "px";
			$(this).attr("style", "height:auto !important;height:"+value+";min-height:"+value);
		},
		
		// 字符统计
		counter: function(){
			var obj = $(this);
			var value = $.trim(obj.val());
			var len = value ? StringUtil.length(value) : 0;
			var maxlen = parseInt(obj.attr("maxlen"));
			var counter = $("#" + obj.attr("counter"));
			var btn = $("#" + obj.attr("btn"));
			var html = '<span>'+len+'</span> / ' + maxlen;
			if (counter.attr("remainonly")) {
				html = '还可以输入<span> '+maxlen+' </span>字';
			}
			counter.html(html);		
			obj.keyup(checkLen);
			obj.focus(checkLen);
			function checkLen() {
				var obj = $(this);
				var value = obj.val();
				var len = StringUtil.length(value);
				if (len > maxlen) {
					counter.addClass("errors");
					btn.attr("enable", false);
					obj.attr("enable", false);
				} else {
					counter.removeClass("errors");
					btn.attr("enable", true);
					obj.attr("enable", true);
				}
				if (counter.attr("remainonly")) {
					var value = maxlen - len;
					if (value >= 0) {
						counter.html('还可以输入<span> '+value+' </span>字');
					} else {
						counter.html('已超出<span> '+(len - maxlen)+' </span>字');
					}
				} else {
					counter.html('<span>'+len+'</span> / ' + maxlen);
				}
			}
		},
		
		// 字符统计清零
		clearCounter: function() {
			var obj = $(this);
			var maxlen = parseInt(obj.attr("maxlen"));
			var counter = $("#" + obj.attr("counter"));
			var html = '<span>0</span> / ' + maxlen;
			if (counter.attr("remainonly")) {
				html = '还可以输入<span> '+maxlen+' </span>字';
			}
			counter.removeClass("errors");
			counter.html(html);			
		},
		
		// 清空文件对象
		clearFile: function() {
			var obj = $(this);
			obj.replaceWith(obj.clone());
		},
		
		// 滑动删除
		slideRemove: function(fn) {
			$(this).slideUp(function(){
				$(this).remove();
				if (fn) {
					fn();
				}
			});
		},
		
		// 设置光标位置
		setCursorPosition: function(position){
		    if(this.lengh == 0) return this;
		    return $(this).setSelection(position, position);
		},
		
		// 选中文字
		setSelection: function(selectionStart, selectionEnd) {
		    if(this.lengh == 0) return this;
		    input = this[0];
		    if (input.createTextRange) {
		        var range = input.createTextRange();
		        range.collapse(true);
		        range.moveEnd('character', selectionEnd);
		        range.moveStart('character', selectionStart);
		        range.select();
		    } else if (input.setSelectionRange) {
		        input.focus();
		        input.setSelectionRange(selectionStart, selectionEnd);
		    }
		    return this;
		},
		
		// 设置光标停在文字最后
		focusEnd: function(){
		    this.setCursorPosition(this.val().length);
		},
		
		// DIV居中
		center: function() {
			var obj = $(this);
			var top = ($(window).height() - obj.height()) / 2 + $(window).scrollTop();
			var left = ($(window).width() - obj.width()) / 2 + $(window).scrollLeft();
			obj.css({
				"position": "absolute",
				"top": top + "px",
				"left": left + "px"
			});
		},
		
		// 输入框错误闪烁（需要样式支持）
		error: function(unfocus) {
			var that = $(this);
			if (!unfocus) {
				that.focus();
			}
			var active = that.attr("active");
			if (active == "true") {
				return;
			}
			var i = 0;
			var data  = new Array(155, 180, 205, 230, 255);
			var len = data.length;
			var times = 0;
			var t = setInterval(function(){
				var value = data[i];
				that.css("background-color", "rgb(255," + value + "," +value+")");
				if (i == len - 1) {
					i = 0;
					times++;
				}
				if (times > 1) {
					clearInterval(t);
					that.attr("active", false);
				}
				i++;
			}, 95);
			that.attr("active", true);
		},
		
		// 元素是否可见
		isVisible: function(value){
			var that = $(this);
			var top = that.offset().top + (value || 0);
			var outerHeight = that.outerHeight();
			var sTop = $(window).scrollTop();
			if ((sTop > (top + outerHeight)) || ((sTop+$(window).height()) < top)) {
				return false;
			}
			return true;
		},
		
		// 设置元素可见
		vshow: function() {
			$(this).css("visibility", "visible");
		},
		
		// 设置元素不可见（但位置仍然占着）
		vhide: function(){
			$(this).css("visibility", "hidden");
		},
		
		/**
		 *  checkbox全选
		 *  frmId 			包含checkbox的容器id，一般是指form表单id，字符串
		 *  checkboxName 	要选中的checkbox的name值，字符串
		 */
		 setAllSelectCheckbox: function (frmId, checkboxName){
			 $(this).change(function (){
				 if ($(this).attr("checked")){
					 $("#" + frmId +" input[name="+checkboxName+"]").attr("checked","true");
				 }else{
					 $("#" + frmId +" input[name="+checkboxName+"]").removeAttr("checked"); 
				 }
			 });
		 }
		
		
	});
	
})(jQuery);


/**
 * 设置AJAX请求的默认参数选项
 */
$.ajaxSetup({
	cache : false,	// 关闭AJAX相应的缓存
	timeout: 30000	// 超时时长30s
});


