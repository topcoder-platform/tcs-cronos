<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="/paging" prefix="p" %>
<%@ taglib uri="/orpheus" prefix="orpheus" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>The Ball :: Games</title>
    <link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/plugin.css"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="Wed, 09 Aug 2000 08:21:57 GMT"/>
    <script type="text/javascript" xml:space="preserve" src="${ctx}/js/pluginSupport.js">
    </script>
</head>

<body id="pagePlugin"
    <c:if test="${param['logged'] eq 'true' and empty param['sort'] and empty param['page']}">
      onload="notifyOnLogin();"
    </c:if>>
<div id="containerPlugin">
    <div id="mastHead"><img src="${ctx}/i/plugin_stripes.jpg" width="668" height="62" alt=""/></div>

    <div id="pluginTitle">ACTIVE <span class="main">GAMES</span></div>

    <div id="wrapPlugin">

        <div class="tabletop"></div>

        <p:dataPaging pageSize="10" data="${orpheus:convertToList(pendingGames)}" id="pager"
                      requestURL="${ctx}/server/plugin/activeGames.do">
            <p:page>
                <div id="plugin-table">
                    <ul>
                        <li class="auction-block">
                            <p:table border="0" cellpadding="0" cellspacing="0"
                                     comparator="${orpheus:getPluginAllGamesComparator(playerRegisteredGames)}">
                                <p:header styleClass="altHeader">
                                    <p:column width="127" index="1" name="Game"/>
                                    <p:column width="85" index="2" name="Start Date"/>
                                    <p:column width="127" index="3" name="Start URL"/>
                                    <p:column width="136" index="4" name="Last Unlocked Domain"/>
                                    <p:column width="66" index="5" name="Prize" styleClass="right"/>
                                    <p:column width="104" index="6" name="User Status"/>
                                </p:header>
                                <p:rowData id="item" rowId="row" rowType="com.orpheus.game.persistence.Game"
                                           renderTR="false">
                                    <c:if test="${orpheus:isGameRunning(row)}">
                                    <c:set var="playerStatus"
                                           value="${orpheus:getPlayerStatus(row, playerRegisteredGames)}"/>
                                    <tr style="cursor: pointer;"
                                        onclick="goto('${ctx}/server/plugin/gameUnlockedDomains.do?tab=1&gameId=${row.id}');"
                                        <c:if test="${playerStatus eq 'Player'}">
                                            class='altOrange'
                                        </c:if>
                                        <c:if test="${playerStatus ne 'Player'}">
                                            <c:if test="${item.rowNumberFromStart mod 2 ne 0}">
                                                class='alt'
                                            </c:if>
                                            onmouseover="this.style.backgroundColor='#FEF1C4';"
                                            onmouseout="this.style.backgroundColor='${item.rowNumberOnPage mod 2 eq 0 ? '#ffffff' : '#f4f4f4'}';"
                                        </c:if>
                                        >
                                        <td>${row.name}</td>
                                        <td><fmt:formatDate value="${row.startDate}" pattern="MM/dd/yyyy"/></td>
                                        <td>${orpheus:getStartingUrl(row)}&nbsp;</td>
                                        <td>
                                            <c:if test="${playerStatus eq 'Player'}">
                                                ${playerCompletedSlots[row.id].domain.domainName}&nbsp;
                                            </c:if>
                                            <c:if test="${playerStatus ne 'Player'}">
                                                &nbsp;
                                            </c:if>
                                        </td>
                                        <td align="right">
                                            $<fmt:formatNumber value="${orpheus:getMinimumPayout(row)}"
                                                               pattern="#,##0.00"/>
                                        </td>
                                        <td align="center">
                                            <c:if test="${playerStatus eq 'Player'}">
                                                ${playerStatus}
                                            </c:if>
                                            <c:if test="${playerStatus ne 'Player'}">
                                                <a href="${ctx}/server/plugin/startGameJoin.do?gameId=${row.id}" title="Join">
                                                    <img src="${ctx}/i/b/join.gif" alt="Join" width="36" height="15"/>
                                                </a>
                                            </c:if>
                                        </td>
                                    </tr>
                                    </c:if>
                                </p:rowData>
                            </p:table>
                        </li>
                    </ul>
                </div>

                <div class="pagination">
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>Showing Games ${orpheus:getDataPagingFooterMessage(pager)}</td>
                            <td>
                                <ul>Page:
                                    <p:prevLink>&laquo;</p:prevLink>
                                    <p:jumpLinks maxCount="5" prefix="<li>" suffix="</li>"/>
                                    <p:nextLink>&raquo;</p:nextLink>
                                </ul>
                            </td>
                        </tr>
                    </table>
                </div>
            </p:page>
        </p:dataPaging>

        <div class="tablebot"></div>
    </div>
</div>
<script type="text/javascript" xml:space="preserve">
    <!--
    window.focus();
    -->
</script>

</body>
</html>