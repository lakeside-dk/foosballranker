<!DOCTYPE html>
<html lang="da">
<head>
<#include "includes/meta.ftl">
    <script type="text/javascript">
        $(document).ready(function(){
            $("#newtournamentform").validate();
        });
    </script>
</head>
<body>
<div id="container">
<#include "includes/header.ftl">
    <div id="menu">
        <button class="menubutton" onclick="window.location='../modstandere/html'">Opponents</button>
    </div>
    <div id="content">
        <div class="center"><h2 class="toptitle">Tournaments</h2></div>
    <#list turneringer as turnering>
        <div class="center">
            <button class="menubutton" onclick="window.location='${turnering.id?c}/ranking/html'">
                <div>${turnering.name}</div>
                <div class="small">Type: ${turnering.type},
                    Status:<#if !turnering.endDate??>igang<#else>afsluttet</#if>
                </div>
                <div class="small">Began: ${turnering.startDate?date}<#if turnering.endDate??>, Ended ${turnering.endDate?date}</#if>
                </div>
            </button>
        </div>
    </#list>

        <div class="center"><h2>Create tournament</h2></div>
        <form class="cmxform" id="newtournamentform" accept-charset="utf-8" name="form1" action="save" method="post">
            <fieldset style="border:none">
                <p>
                    <select name="type" autofocus="autofocus" value="" id="field_type">
                        <option selected="selected" value="ranking">Highest ranking wins</option>
                        <option value="performance">Best performance wins</option>
                    </select>
                </p>
                <p>
                    <label for="field_name">Name</label><br />
                    <input class="required" autocomplete="off" type="text" name="name" value="" id="field_name">
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