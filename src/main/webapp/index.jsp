<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.HttpClients" %>
<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.NameValuePair" %>
<%@ page import="org.apache.http.message.BasicNameValuePair" %>
<%@ page import="servlets.BoxApiConstants" %>
<%@ page import="org.apache.http.client.entity.UrlEncodedFormEntity" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="box.BoxAccount" %>
<%@ page import="ourapp.TagExtractor" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="ourapp.Constants" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.*" %>
<%@ page import="box.BoxUserInfo" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>Here come the innovations!</title>
    <style type="text/css">

        html {
            font: normal normal normal 18pt/1.2 "Helvetica Neue", Roboto, "Segoe UI", Calibri, sans-serif;
            color: #292f33;
        }

        .typeahead {
            background-color: #fff;
        }

        .typeahead:focus {
            border: 2px solid #0097cf;
        }

        .tt-menu {
            min-width: 66%;
            margin: 5px 0;
            padding: 4px 0;
            background-color: #fff;
            border: 1px solid #ccc;
            border: 1px solid rgba(0, 0, 0, 0.2);
            border-radius: 4px;
            box-shadow: 0 5px 10px rgba(0, 0, 0, .2);
        }

        .tt-suggestion {
            padding: 3px 20px;
            font-size: 12pt;
            line-height: 16pt;
        }

        .tt-suggestion:hover {
            cursor: pointer;
            color: #fff;
            background-color: #0097cf;
        }

        .tt-suggestion.tt-cursor {
            color: #fff;
            background-color: #0097cf;
        }

        .tt-suggestion p {
            margin: 0;
        }


        .twitter-typeahead {
            float: left;
            width: 100%
        }

        .input-group .twitter-typeahead .form-control:not(:first-child):not(:last-child) {
            border-radius: 4px 0 0 4px;
        }

        .preview {
            width: 242px;
            height: 256px;
            vertical-align: bottom;
        }

        .preview {
            text-align: center;
        }

        .preview .textDiv {
            color: white;
            display: none;
        }

        .preview:hover {
            box-shadow: inset -6px -200px 55px -190px rgba(0,0,0,0.75);
            cursor: pointer;
        }

        .preview:hover .textDiv {
            display: inline-block;
        }

        .input-group {
            margin: 20px auto;
        }

        #spinner {
            display: block;
            margin: auto;
        }
    </style>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

</head>
<body class="container-fluid">

<%
    String entityString = "";
    Map<String, Integer> map = null;
    long id = -1;
    try (PrintWriter pw = response.getWriter()) {
        if (request.getAttribute("error") != null) {
            pw.println("Something went wrong, sorry");
        } else {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            if (!state.equals(Constants.SECURE_STATE)) {
                pw.println("Malformed request, sorry");
            } else {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost post = new HttpPost("https://app.box.com/api/oauth2/token");
                List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("client_id", BoxApiConstants.CLIENT_ID));
                params.add(new BasicNameValuePair("client_secret", BoxApiConstants.CLIENT_SECRET));
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse resp = httpclient.execute(post);
                HttpEntity entity = resp.getEntity();
                if (entity != null) {
                    entityString = EntityUtils.toString(entity);
                    BoxAccount boxAccount = new BoxAccount(entityString);
                    id = BoxUserInfo.getUserId(boxAccount);
                    try {
                        map = TagExtractor.extract(boxAccount, id);
                        System.out.println(map);
                    } catch (URISyntaxException | SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    pw.println("Couldn't connect to server");
                }
            }
        }
    }
%>

<form class="col-lg-12" method="get" onsubmit="doSearch($('#q').val()); return false;">
    <div class="input-group">
        <input type="text" class="form-control typeahead input-lg" id="q" placeholder="Search for...">
      <span class="input-group-btn">
        <input class="btn btn-default input-lg" type="submit" value="Go!">
      </span>
    </div>
</form>

<%--<div id="tags">--%>
    <%--<%--%>
        <%--final Map.Entry<String, Integer>[] entries = new Map.Entry[map.entrySet().size()];--%>
        <%--map.entrySet().toArray(entries);--%>
        <%--Arrays.sort(entries, new Comparator<Map.Entry<String, Integer>>() {--%>
            <%--@Override--%>
            <%--public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {--%>
                <%--return o2.getValue() - o1.getValue();--%>
            <%--}--%>
        <%--});--%>
        <%--int len = Math.min(30, entries.length);--%>
        <%--ArrayList<Map.Entry<String, Integer>> list = new ArrayList(len);--%>
        <%--for (int i = 0; i < len; i++) {--%>
            <%--list.add(entries[i]);--%>
        <%--}--%>
        <%--Collections.shuffle(list);--%>
        <%--for(Map.Entry<String, Integer> entry : list) {--%>
            <%--out.write(String.format("<a onclick=\"doSearch('%s'); $('#q').val('%s');\" rel='%d'>%s </a>", entry.getKey(), entry.getKey(), entry.getValue(), entry.getKey()));--%>
        <%--}--%>
    <%--%>--%>
<%--</div>--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="https://twitter.github.io/typeahead.js/releases/latest/typeahead.bundle.min.js"></script>
<script src="https://addywaddy.github.io/jquery.tagcloud.js/jquery.tagcloud.0-0-1.js" type="text/javascript"></script>
<script>
    $(document).ready(function () {

        var bestPictures = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
//                prefetch: '../data/films/post_1960.json',
            remote: {
//                    url: 'http://twitter.github.io/typeahead.js/data/films/queries/%QUERY.json',
                url: '/query.json?q=%QUERY&id=<%out.print(id);%>',
                wildcard: '%QUERY'
            }
        });

        $('.typeahead').typeahead({
            highlight: true
        }, {
            name: 'best-pictures',
            display: 'value',
            source: bestPictures
        });

        $.fn.tagcloud.defaults = {
            size: {start: 14, end: 36, unit: 'pt'},
            color: {start: '#cde', end: '#f52'}
        };

        $(function () {
            $('#tags').find('a').tagcloud();
        });

    });

    function doSearch(s) {
        var body = document.getElementsByTagName("body")[0];
        var tags = document.getElementById("tags");
        if (tags != undefined) {
            body.removeChild(tags);
        } else {
            body.removeChild(body.lastChild);
        }
        var results = document.createElement("div");
        results.id = "results";
        results.className = "row";
        var spinner = document.createElement("img");
        spinner.id = "spinner";
        spinner.src = "spinner.gif";
        body.appendChild(spinner);
        $.getJSON("/find?tag=" + s + "&entity=<% out.write(URLEncoder.encode(entityString, "UTF-8")); %>" + "&id=<% out.print(id); %>", function(data) {
            body.removeChild(spinner);
            $.each(data, function(key, val) {
                var cell = document.createElement("div");
                var innerDiv = document.createElement("div");
                innerDiv.onclick = function() {
                    window.open(val.link, "_blank");
                };
                cell.className = "col-lg-3 col-md-3 col-sm-4 col-xs-6";
                innerDiv.style.backgroundImage = "url('" + val.preview + "')";
                innerDiv.className = "preview";
                var textDiv = document.createElement("div");
                textDiv.className = "textDiv";
                var s = val.name;
                if (s.length > 25) {
                    s = s.substr(0, 25) + "...";
                }
                var textNode = document.createTextNode(s);
                innerDiv.style.paddingTop = "230px";
                textDiv.appendChild(textNode);
                innerDiv.appendChild(textDiv);
                cell.appendChild(innerDiv);
                results.appendChild(cell);
            });
        });
        body.appendChild(results);
        return false;
    }
</script>
</body>
</html>
