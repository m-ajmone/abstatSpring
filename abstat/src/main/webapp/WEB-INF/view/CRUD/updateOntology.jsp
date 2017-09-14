<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
<title>Ontology</title>
</head>
<body>
<spring:url value = "/ontology/save" var = "saveURL" />

<form:form action = "${saveURL}" modelAttribute = "ontology" method = "POST">
	<form:hidden path= "type" value = "ontology"/>
	<label>Id:</label>
	<form:input disabled="disabled" path="id" value = "${ontology.id}"/> <br/>
	<label>Name: </label>
	<form:input path="name" value = "${ontology.name}"/> <br/>
	<label>Path:</label>
	<form:input path="path" value = "${ontology.path}"/> <br/>
	<label>Timestamp:</label>
	<form:input path="timestamp" value = "${ontology.timestamp}"/> <br/>
	<button type = "submit">Save</button>
</form:form>

</body>
</html>