<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta.ftl">
    <script type="text/javascript">
        $(document).ready(function(){
            $("#newopponentform").validate();
        });
    </script>
</head>
<body>
<div id="container">
<#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='../turneringer/html'">Back</button>
        <button class="menubutton" onclick="window.location='svgchart'">Chart</button>
    </div>
    <div id="content">
        <div class="center"><h2 class="toptitle">Opponents</h2></div>
<#list modstandere as modstander>
            <div><h3>${modstander.first} (${modstander.second})</h3></div>
</#list>

        <div class="center"><h2>Add opponent</h2></div>
        <form class="cmxform" id="newopponentform" accept-charset="utf-8" action="add" method="post">
        <fieldset style="border:none">
                <p>
                    <label for="field_id">Username</label><br />
                    <input class="required" autocomplete="off" autofocus="autofocus" type="text" name="id" value="" id="field_id">
                </p>
            </fieldset>
            <div class="center">
                <input class="button submit" type="submit" value="Add"/>
            </div>
        </form>
    </div>
<#include "includes/footer.ftl">
</div>
</body>
</html>