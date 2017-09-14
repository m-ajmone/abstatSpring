<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
<title>Dataset</title>
</head>
<body>
<spring:url value = "/dataset/save" var = "saveURL" />

<form:form action = "${saveURL}" modelAttribute = "dataset" method = "POST">
	<form:hidden path= "type" value = "dataset"/>
	<label>Id:</label>
	<form:input disabled="disabled" path="id" value = "${dataset.id}"/> <br/>
	<label>Name: </label>
	<form:input path="name" value = "${dataset.name}"/> <br/>
	<label>Path:</label>
	<form:input path="path" value = "${dataset.path}"/> <br/>
	<label>Timestamp:</label>
	<form:input path="timestamp" value = "${dataset.timestamp}"/> <br/>
	<label>Split:</label>
	<form:input path="split" value = "${dataset.split}"/> <br/>
	<button type = "submit">Save</button>
</form:form>

</body>
</html>