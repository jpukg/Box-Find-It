<%@ page import="java.io.PrintWriter" %>
<%@ page import="ourapp.TagExtractor" %>
<%@ page import="java.util.List" %>
<%@ page import="box.BoxAccount" %>
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
    String query = request.getParameter("q");
    String entity = request.getParameter("entity");
    if (query != null && entity != null) {
        final BoxAccount account = new BoxAccount(entity);
        final List<Long> fileIds = TagExtractor.findFileIds(query);
        for (Long fileId : fileIds) {
//            BoxAccount.
        }
    } else {
        pw.write("Invalid query.");
    }
%>
</body>
</html>