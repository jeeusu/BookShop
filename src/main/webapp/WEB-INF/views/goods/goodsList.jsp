<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${goods_sort} 도서 목록</title>
</head>
<body>

<h2>${goods_sort} 도서 목록</h2>

<!-- 정렬 선택 메뉴 -->
<div id="sorting">
    <ul>
        <li><a class="${goods_sort == 'bestseller' ? 'active' : ''}" href="${contextPath}/goods/goodsList.do?goods_sort=bestseller">베스트 셀러</a></li>
        <li><a class="${goods_sort == 'newbook' ? 'active' : ''}" href="${contextPath}/goods/goodsList.do?goods_sort=newbook">최신 출간</a></li>
        <li><a class="${goods_sort == 'steadyseller' ? 'active' : ''}" href="${contextPath}/goods/goodsList.do?goods_sort=steadyseller">스테디 셀러</a></li>
    </ul>
</div>

<!-- 도서 목록 출력 -->
<table border="1" width="100%">
    <thead>
        <tr>
            <th>이미지</th>
            <th>제목 / 작가 / 출판사</th>
            <th>가격</th>
            <th>할인가</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty goodsList}">
                <tr>
                    <td colspan="4">해당 정렬에 대한 도서가 없습니다.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${goodsList}">
                    <tr>
                        <td>
                            <a href="${contextPath}/goods/goodsDetail.do?goods_id=${item.goods_id}">
                                <img width="75" alt="${item.goods_title}" src="${contextPath}/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
                            </a>
                        </td>
                        <td>
                            <strong>${item.goods_title}</strong><br/>
                            ${item.goods_writer} | ${item.goods_publisher} <br/>
                            출간일: <c:out value="${fn:split(item.goods_published_date, ' ')[0]}" />
                        </td>
                        <td>
                            <fmt:formatNumber value="${item.goods_price}" type="number" /> 원
                        </td>
                        <td>
                            <fmt:formatNumber value="${item.goods_price * 0.9}" type="number" /> 원 (10% 할인)
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

</body>
</html>
