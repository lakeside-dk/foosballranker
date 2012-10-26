<#list kampe as kamp>
<div>
    <h3>
    <#if kamp.defender2Id??>${kamp.defender1Id},${kamp.attacker1Id} vs. ${kamp.defender2Id},${kamp.attacker2Id}<#else>${kamp.player1Id} vs. ${kamp.player2Id}</#if>&nbsp;${kamp.score1}&nbsp;${kamp.score2}
    </h3>
    <span class="small">${kamp.time?string("dd/MM/yy HH:mm")}</span>
    <button class="smallbutton" onclick="document.form1.id.value=${kamp.id?c};document.form1.submit();">Delete</button>
</div>
</#list>
