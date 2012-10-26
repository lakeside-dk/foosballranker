<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta-zoomable.ftl">
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
                    draw(data, {width: 500, height: 300,
                        vAxis: {baseline:${baseline?c}, minValue: ${baseline?c}, maxValue: ${baseline?c}},
                        chartArea:{width:385,height:230,left:50,top:30},
                        title: '${titel}'
                    }
            );
        }

        google.setOnLoadCallback(drawVisualization);
    </script>
</head>
<body>
<#--<div id="menu">-->
    <#--<button class="menubutton" onclick="window.location='svgchart'">Back</button>-->
<#--</div>-->
<div id="visualization" style="width: 500px; height: 300px;"></div>
</body>
</html>