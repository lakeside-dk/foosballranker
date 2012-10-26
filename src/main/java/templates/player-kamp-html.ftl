<!DOCTYPE html>
<html lang="da">
<head>
    <#include "includes/meta-nojquery.ftl">
</head>
<body>
<div id="container">
<#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='../ranking/html'">Back</button>
    </div>
    <div id="content">
        <div class="center"><h2 class="toptitle">Register match</h2></div>
        <fieldset class="fieldset">
            <legend id="pickgametype">Type of match</legend>
            <div id="gametypes">
                <button id="onevsone" class="button">1 vs. 1</button>
                <button id="twovstwo" class="button">2 vs. 2</button>
            </div>
            <div id="pickedgametype"></div>
        </fieldset>

        <fieldset class="fieldset">
            <legend id="pickplayers">Players</legend>
            <div id="pickedplayers"></div>
            <div id="players"></div>
        </fieldset>

        <fieldset class="fieldset">
            <legend id="pickscore">Result of match</legend>
            <div id="pickedredscore"></div>
            <div id="pickedbluescore"></div>
            <div id="redscore"></div>
            <div id="bluescore" class="clearleft"></div>
        </fieldset>
        <div style="width: 320px">
            <div class="center">
                <div id="status"></div>
                <button disabled="disabled" id="savebutton" style="width: 160px" class="button">Save</button>
            </div>
        </div>
    </div>
<#include "includes/footer.ftl">
</div>
<script type="application/javascript" src="/js/playerKamp.js"></script>
</body>
</html>