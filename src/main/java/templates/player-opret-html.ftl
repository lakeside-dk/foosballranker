<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta.ftl">
    <script type="text/javascript">
        $(document).ready(function(){
            $("#newaccountform").validate();
        });
    </script>
</head>
<body>
<div id="container">
    <#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='/index.html'">Back</button>
    </div>
    <div id="content">
        <div class="center"><h2>New account</h2></div>
        <form class="cmxform" id="newaccountform" accept-charset="utf-8" action="save" method="post">
            <fieldset style="border:none">
                <p>
                    <label for="field_name">Name</label><br />
                    <input class="required" autocomplete="off" autofocus="autofocus" type="text" name="name" value="" id="field_name">
                </p>
                <p>
                    <label for="field_id">Username</label><br />
                    <input class="required" autocomplete="off" autofocus="autofocus" type="text" name="id" value="" id="field_id">
                </p>
                <p>
                    <label for="field_password">Password</label><br />
                    <input class="required" autocomplete="off" type="password" name="password" value="" id="field_password">
                </p>
            </fieldset>
            <div class="center">
                <input class="button submit" type="submit" value="Create"/>
            </div>
        </form>
    </div>
    <#include "includes/footer.ftl">
</div>
</body>
</html>