<%@ page import="java.io.PrintWriter" %>
<!doctype html>
<html>
<head>
    <title>Results</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
</head>
<body>
<h1>Hello!</h1>
<%
    PrintWriter pw = response.getWriter();
    if (request.getParameter("q") != null) {
        pw.println("Your query is " + request.getAttribute("q"));
    }
%>
</body>
</html>