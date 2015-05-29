<html>
<head>
    <meta charset="UTF-8">
    <title>Here come the innovations!</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="http://twitter.github.io/typeahead.js/releases/latest/typeahead.bundle.min.js"></script>
    <script>
        $(document).ready(function() {

            // remote
            // ------

            var bestPictures = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
//                prefetch: '../data/films/post_1960.json',
                remote: {
                    url: 'http://twitter.github.io/typeahead.js/data/films/queries/%QUERY.json',
                    wildcard: '%QUERY'
                }
            });

            $('#remote .typeahead').typeahead(null, {
                name: 'best-pictures',
                display: 'value',
                source: bestPictures
            });

        });
    </script>
</head>
<body class="container-fluid">

<form class="input-group">
    <input type="text" class="form-control" placeholder="Enter any keywords you’re interested in">
    <div class="input-group-btn">
        <button type="submit" class="btn btn-default">
            <span class="glyphicon glyphicon-search" aria-hidden="true"></span> Search
        </button>
    </div>
</form>

<div id="remote">
    <input class="typeahead" type="text" placeholder="Oscar winners">
</div>

</body>
</html>
