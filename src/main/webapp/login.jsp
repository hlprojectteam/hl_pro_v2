<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta content="email=no" name="format-detection" />
    <meta content="telephone=no" name="format-detection" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
    <title>登录页面</title>
    <link rel="stylesheet" type="text/css" href="/common/login/css/login.css"/>
    <link href="/common/login/css/drag.css" rel="stylesheet" type="text/css"/>
    <link href="/common/login/css/base.css" rel="stylesheet" type="text/css"/>
    <script src="/common/index/js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/common/login/js/drag.js"></script>
    <script src="/common/js/md5.js"></script>
</head>
<body> 
          <form action="/indexMain" method="post" id="submit-form" > 
                       <div class="login_Goverment_content">
                       <div class="login_Goverment_logo"> 
                              <img src="/common/login/images/logo.png" alt="" />
                              <a class="app_code" id="app_code">app下载</a>
                              <div class="code_img" id="code_img"><img src="/common/login/images/code.png"  alt="" /></div>
                        </div>  
                        <div class="login_Goverment"> 
                            <div class="login_error" style="display:none;">
                                <!-- <b></b>用户名与密码不匹配，请重新输入 -->
                            </div>
                            <div class="login_Goverment_name">
                                <label for="loginName" class="login-label name-label"></label>
                                <input id="loginName"  type="text" class="login_input" name="loginName" autocomplete="off" placeholder="用户名/手机号" onkeyup="change();" />
                                <span class="loginname_cancel"></span>
                            </div>
                            <div class="login_Goverment_password">
                                <label for="password2" class="login-label pw-label"></label>
                                <input id="password2"  name="password2" type="password" class="login_input" autocomplete="off" placeholder="请输入密码" onkeyup="change();" />
                                <input type="hidden" name="password" id="password">
                            </div>
                             <div id="drag">
                                   <input type="hidden" id="code" value="0" />
                             </div>
                            <script type="text/javascript">
                            $('#drag').drag();
                            </script>
                            <div class="login_item">
                                <div class="login_item_in">
                                    <span>
                                        <input id="autoLogin" type="checkbox" class="login_checkbox" />
                                        <label for>自动登录</label>
                                    </span>
                                    <span class="login_fg_name">忘记密码</span>
                                </div>
                            </div>
                            <div class="login_Goverment_button">
                                <div class="login_button_in" id="js-submit">登  录</div>
                            </div>
                            <span class="login_Goverment_register"><a href="#">立即注册></a></span>
                        </div> 
                       </div> 
                       <p class="login_footer">
                        版权所有：广州环龙高速公路有限公司 &nbsp; &nbsp;主办：广州环龙高速公路有限公司 &nbsp; &nbsp;技术支持：广州云汀信息科技有限公司
                       </p>
            </form> 
      
</body>
</html>

<script type="text/javascript">
 
    $('#app_code').click(function(event) {
     $('#code_img').toggle(300);
    });

    var form = $("#submit-form");
    $('#js-submit').on('click',
            function() {
                var html = "";

                $('.login_error').hide();
                var username = form.find("#loginName").val();
                var password = form.find("#password2").val();
                var code = $("#code").val();
                if (username.length < 1) {
                    html = "<b></b>帐号为空或格式不正确，请重新输入!";
                    $('.login_error').show();
                    $("#loginname").focus();
                    $(".login_error").html(html);
                    return false;
                    html = "<b></b>密码为空或格式不正确，请重新输入!";
                    $('.login_error').show();
                    $("#password").focus();
                    $(".login_error").html(html);
                    return false;
                } else if (code == '' || code != '1') {
                    html = "<b></b>滑动验证不通过，请重新拖动!";
                    $('.login_error').show();
                    $(".login_error").html(html);
                    return false;
                }
                $("#js-submit").text("登陆中...");
                $("#password").val(md5(password));
                form.submit();
            });

    if (window.top !== window.self) {
        window.top.location = window.location;
    }
    $(function() {
        //登录错误弹出
        var msg = '${sessionScope.msg}';
        if (msg != "" && msg != null) {
            $('.login_error').show();
            $(".login_error").html("<b></b>" + msg);
            <%HttpSession s = request.getSession();
            s.setAttribute("msg", "");%>
        }
        //如果默认有密码
        var password2 = $("#password2").val();
        if (password2 != null && password2 != "") {
            $("#password").val(md5(password2));
        }

        //addVisit(); //添加访问记录
    });

    function change() {
        var password2 = $("#password2").val();
        $("#password").val(md5(password2));
    }

    //注册事件
    $("#password2").bind({
        keypress : function(event) {
            if (13 == event.keyCode) {
                $("#js-submit").trigger("click");
            }
        }
    });

    //添加访问记录
    function addVisit() {
        $.ajax({
            type : "post",
            async : true,
            dataType : 'json',
            url : '/visit/visit_saveVisit',
            success : function(data) {
                if (data != null) {
                    //alert("保存成功");
                }
            }
        }); 
    }
</script>