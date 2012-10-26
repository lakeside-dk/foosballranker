<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta.ftl">
</head>
<body>
<div id="container">
<#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='../../html'">Back</button>
        <button class="menubutton" onclick="window.location='../kamp/html'">Register match</button>
        <button class="menubutton" onclick="window.location='svgchart'">Chart</button>
        <button class="menubutton" onclick="window.location='../kamp/list/html'">Matches</button>
    </div>
    <div id="content">
        <#if players?has_content><div class="center"><h2 class="toptitle">Players (ranking)</h2></div></#if>
    <#list players as player>
        <div class="center"><h3>${player.name} (${player.rating?string("0")})</h3></div>
    </#list>

        <div class="center"><h2>Manage tournament</h2></div>

        <div class="center">
    <#if !turnering.endDate??>
        <form accept-charset="utf-8" action="../afslut">
        <input class="button" type="submit" value="Close"/>
        </form>
    <#else>
        <form accept-charset="utf-8" action="../genaabn">
        <input class="button" type="submit" value="Reopen"/>
        </form>
    </#if>
        </div>
    </div>
<#include "includes/footer.ftl">
</div>
</body>
</html>