<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
<title>Dataset List</title>
</head>
<h1>List Dataset</h1>
<table border = "1">
	<thead>
		<tr>
			<th> Name </th>
			<th colspan="3">Action</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${listDataset}" var="dataset">
		<tr>
			<td>${dataset.name}</td>
			<td>
				<spring:url value ="/dataset/update/${dataset.id}" var="updateURL"/>
				<a href="${updateURL}">Update</a>
			</td>
			<td>
				<spring:url value ="/dataset/delete/${dataset.id}" var="deleteURL"/>
				<a href="${deleteURL}">Delete MetaData</a>
			</td>
			<td>
				<spring:url value ="/dataset/deleteDir/${dataset.id}" var="deleteDirURL"/>
				<a href="${deleteDirURL}">Delete MetaData and Files</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</html>