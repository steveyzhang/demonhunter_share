<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Demon Hunter Converting</title>
</head>
<body>
<h1>Welcome To Demon Hunter!</h1>
<p><a href="<s:url action='hello'/>">Hello World</a></p>

<s:url action="hello" var="helloLink">
  <s:param name="platform">WP</s:param>
</s:url>

<p><a href="${helloLink}">Try WP7</a></p>

<p>Let me know what platform you will convert</p>

<s:form action="hello">
	<s:textfield name="platform" label="Your Platform:Android or WP" />
	<s:submit value="Submit" />
</s:form>
</body>
</html>
