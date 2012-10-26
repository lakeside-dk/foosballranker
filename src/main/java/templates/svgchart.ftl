<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta.ftl">
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load('visualization', '1', {packages: ['corechart']});
    </script>
    <script type="text/javascript">
        function drawVisualization() {
            // Create and populate the data table.
            var data = new google.visualization.DataTable();
        ${data}

            // Create and draw the visualization.
            new google.visualization.LineChart(document.getElementById('visualization')).
                    draw(data, {width: 300, height: 190,
                        vAxis: {baseline:${baseline?c}, minValue: ${baseline?c}, maxValue: ${baseline?c}},
                        chartArea:{width:200,height:120,left:50,top:30},
                        title: '${titel}'
                    }
            );
        }

        google.setOnLoadCallback(drawVisualization);
    </script>
</head>
<body>
<div id="container">
<#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='html'">Back</button>
    </div>
    <div id="content" class="center">
        <div class="center"><h2 class="toptitle">Chart</h2></div>
        <div id="visualization" style="width: 320px; height: 190px;"></div>
        <button class="button" onclick="window.location='bigsvgchart'">Big Chart</button>
    </div>
<#include "includes/footer.ftl">
</div>
</body>
</html>