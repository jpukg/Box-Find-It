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
    </style>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://twitter.github.io/typeahead.js/releases/latest/typeahead.bundle.min.js"></script>
    <script src="https://addywaddy.github.io/jquery.tagcloud.js/jquery.tagcloud.0-0-1.js" type="text/javascript"></script>
    <script>
        $(document).ready(function () {

            // remote
            // ------

            var bestPictures = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
//                prefetch: '../data/films/post_1960.json',
                remote: {
//                    url: 'http://twitter.github.io/typeahead.js/data/films/queries/%QUERY.json',
                    url: '/query.json?q=%QUERY',
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
                size: {start: 14, end: 18, unit: 'pt'},
                color: {start: '#cde', end: '#f52'}
            };

            $(function () {
                $('#tags a').tagcloud();
            });

        });
    </script>
</head>
<body class="container-fluid">

<form class="col-lg-12">
    <div class="input-group">
        <input type="text" class="form-control typeahead" placeholder="Search for...">
      <span class="input-group-btn">
        <button class="btn btn-default" type="submit">Go!</button>
      </span>
    </div>
</form>

<div id="tags">
    <a href="/path" rel="7">peace</a>
    <a href="/path" rel="3">unity</a>
    <a href="/path" rel="10">love</a>
    <a href="/path" rel="5">having fun</a>
</div>

</body>
</html>
