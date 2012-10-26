<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta.ftl">
</head>
<body>
<div id="container">
<#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='../../ranking/html'">Back</button>
    </div>
    <div id="content">
        <div class="center"><h2 class="toptitle">Matches in tournament</h2></div>
        <form accept-charset="utf-8" name="form1" method="post" action="../delete">
        <input type="hidden" name="id"/>
        <div class="center">
            <#include "includes/kamp-list.ftl">
        </div>
        </form>
    </div>
<#include "includes/footer.ftl">
</div>
</body>
</html>