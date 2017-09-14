<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<header>Selected Configuration:</header>
<table>
	<tr>
		<td>Dataset:</td>
		<td>${datasetName}</td>
	</tr>
	<tr>
		<td>Ontologies</td>
		<td>${ontologyNames}</td>
	</tr>
	<tr>
		<td>Calculate Minimal Types:</td>
		<td>${submitConfig.isTipoMinimo()} </td>
	</tr>
	<tr>
		<td>Inferences:</td>
		<td>${submitConfig.isInferences()}</td>
	</tr>
	<tr>
		<td>Cardinality:</td>
		<td>${submitConfig.isCardinalita()}</td>
	</tr>
	<tr>
		<td>Property Minimalization: </td>
		<td>${submitConfig.isPropertyMinimaliz()}</td>
	</tr>
</table>

<form:form action = "processOntology" method = "post">
	<input type="hidden" name="subCfgId" value="${submitConfig.getId()}" />
	<input type="hidden"name="${_csrf.parameterName}"value="${_csrf.token}"/>
	<input type = "submit" value = "ProcessOntology">
</form:form>

<form:form action = "processTriples" method = "post">
	<input type="hidden" name="subCfgId" value="${submitConfig.getId()}" />
	<input type="hidden"name="${_csrf.parameterName}"value="${_csrf.token}"/>
	<input type = "submit" value = "ProcessTriples">

</form:form>