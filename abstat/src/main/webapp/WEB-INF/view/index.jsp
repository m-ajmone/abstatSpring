<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<body>
<table border=0>
	<tr>
		<th><b>Dataset Upload</b></th> <th></th> <th><b>Ontology Upload</b></th>
	</tr>
	<tr>
		<td>	
			<form method="POST" action="upload/ds" enctype="multipart/form-data">
    		<input type="file" name="file" /><br/>
    		<input type="submit" value="Submit" />
			</form>
		</td>
		<td></td>
		<td> 
			<form method="POST" action="upload/ont" enctype="multipart/form-data">
    		<input type="file" name="file" /><br/>
    		<input type="submit" value="Submit" />
			</form>
		</td>
	</tr>
	<tr>
		<td colspan = "3"/><hr></td>
	</tr>
	<form:form action = "submit" modelAttribute = "submitConfig" method = "POST" >
	<tr>
		<td>
			<form:select path= "dsId">
    		<form:options items="${listDataset}" itemLabel="name" itemValue="id" />
			</form:select>
		</td>
		<td></td>
		<td>
			<form:select path= "listOntId" multiple = "true">
    		<form:options items="${listOntology}" itemLabel="name" itemValue="id" />
			</form:select>
		</td>
	</tr>
	<tr>
		<td> Calcolo Tipo Minimo </td>
		<td><form:checkbox path = "tipoMinimo" /></td>
	</tr>
	<tr>
		<td> Calcolo inferenze </td>
		<td><form:checkbox path = "inferences" /></td>
	</tr>
	<tr>
		<td> Calcolo cardinalità </td>
		<td><form:checkbox path = "cardinalita" /></td>
	</tr>
	<tr>
		<td> Calcolo property minimalization </td>
		<td><form:checkbox path = "propertyMinimaliz" /></td>
	</tr>
		<tr>
		<td> <input type = "submit" value = "Submit" /> </td>
		<tr>
	</form:form>	
</table>

</body>
</html>