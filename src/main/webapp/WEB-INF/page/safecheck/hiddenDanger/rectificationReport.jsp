<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>安全隐患整改报告</title>
    </head>
    <style>
        body{
            background:#f5f5f5;
        }
        .title{
            width:100%;
            margin:10px auto;
            text-align: center;
            
        }
        .col-10{
            width:10%;
        }
        .col-20{
            width:20%;
        }
        .col-30{
            width:30%;
        }
        .cont-img{
            height:300px;
        }
        .cont-img img{
            width:100%;
            height:300px;
            vertical-align:top;
            object-fit: contain;
        }
        .txt-align{
            text-align:center;
        }
        table tr th{
            padding:20px 0px;
        }
    </style>
    <body>

        <h4 class="title">发现安全隐患及整改情况</h4>
        <table border="" cellspacing="0" cellpadding="0" class="content">
            <tr>
                <th>检查地点</th>
                <th>发现隐患及照片</th>
                <th>整改意见</th>
                <th>整改情况及照片</th>
                <th>是否关闭</th>
            </tr>
            <tr>
                <td class="col-10 txt-align">${eventInfoVo.eventAddress}</td>
                <td class="col-30 cont-img">
                    <c:forEach items="${eventInfoVo.imgUrls}" var="imgUrl">
                        <img src="${imgUrl}" alt="" />
                    </c:forEach>
                    <!-- <img src="/common/event/tempImg/10.png" alt="" /> -->
                    <p>105员工座位下堆放易燃纸张</p>
                </td>
                <td class="col-20 txt-align">${processVo.epDealContent}</td>
                <td class="col-30 cont-img">
                    <c:forEach items="${processVo.imgUrls}" var="imgUrl">
                        <img src="${imgUrl}" alt="" />
                    </c:forEach>
                    <!-- <img src="/common/event/tempImg/2.jpg" alt="" /> -->
                    <p>综合行政部已当场清理</p>
                </td>
                <td class="col-10 txt-align">是</td>
            </tr>
            <!-- <tr>
                <td class="col-10 txt-align">检查地点</td>
                <td class="col-30 cont-img">
                    <img src="/common/event/tempImg/9.jpg" alt="" />
                    <p>渠道边</p>
                </td>
                <td class="col-20 txt-align">建议综合行政</td>
                <td class="col-30 cont-img">
                    <img src="/common/event/tempImg/9.jpg" alt="" />
                    <p>综合行政部已当场清理</p>
                </td>
                <td class="col-10 txt-align">是</td>
            </tr>
            <tr>
                <td class="col-10 txt-align">检查地点</td>
                <td class="col-30 cont-img">
                    <img src="/common/event/tempImg/9.jpg" alt="" />
                    <p>渠道边</p>
                </td>
                <td class="col-20 txt-align">建议综合行政</td>
                <td class="col-30 cont-img">
                    <img src="/common/event/tempImg/9.jpg" alt="" />
                    <p>综合行政部已当场清理</p>
                </td>
                <td class="col-10 txt-align">是</td>
            </tr> -->
        </table>

    </body>
</html>
