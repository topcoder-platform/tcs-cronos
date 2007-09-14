<%@ page import="com.jivesoftware.base.User,
                com.topcoder.web.forums.ForumConstants,
                com.topcoder.web.forums.controller.ForumsUtil"
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="tc-webtags.tld" prefix="tc-webtag" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="/orpheus" prefix="orpheus" %>
<c:set var="ctx" value=""/>
<tc-webtag:useBean id="forumFactory" name="forumFactory" type="com.jivesoftware.forum.ForumFactory" toScope="request"/>
<tc-webtag:useBean id="forum" name="forum" type="com.jivesoftware.forum.Forum" toScope="request"/>
<tc-webtag:useBean id="message" name="message" type="com.jivesoftware.forum.ForumMessage" toScope="request"/>
<tc-webtag:useBean id="revisionList" name="revisionList" type="java.util.ArrayList" toScope="request"/>
<tc-webtag:useBean id="unreadCategories" name="unreadCategories" type="java.lang.String" toScope="request"/>

<%  String timezone = (String) request.getAttribute("timezone");
    String threadView = (String)request.getAttribute("threadView"); 
    User user = (User)request.getAttribute("user"); %>

<html>
<head>
    <link rel="icon" type="image/png" href="/i/favicon.png">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>The Ball :: Forums</title>
    <jsp:include page="script.jsp"/>
    <jsp:include page="style.jsp">
        <jsp:param name="key" value="ball_forums"/>
    </jsp:include>
</head>

<body id="page">
<div id="container">
    <c:set var="subbar" value="forums" scope="page"/>
    <%@ include file="includes/header.jsp" %>
    <div id="wrap">

<table cellpadding="0" cellspacing="0" class="rtbcTable">
<tr>
   <td class="categoriesBox" style="padding-right: 20px;">
      <jsp:include page="categoriesHeader.jsp" />
   </td>
   <td nowrap="nowrap" valign="top" width="100%" style="padding-right: 20px;">
       <jsp:include page="searchHeader.jsp" />
   </td>
   <td align="right" nowrap="nowrap" valign="top">   
       <A href="?module=History" class="rtbcLink">My Post History</A>&#160;&#160;|&#160;&#160;<A href="?module=Watches" class="rtbcLink">My Watches</A>&#160;&#160;|&#160;&#160;<A href="?module=Settings" class="rtbcLink">User Settings</A><br>
   </td>
</tr>
<tr>
    <td colspan="3" style="padding-bottom:3px;">
        <b><tc-webtag:iterator id="category" type="com.jivesoftware.forum.ForumCategory" iterator='<%=ForumsUtil.getCategoryTree(forum.getForumCategory())%>'>
            <A href="?module=Category&<%=ForumConstants.CATEGORY_ID%>=<%=category.getID()%>" class="rtbcLink"><%=category.getName()%></A> <img src="/i/interface/exp_w.gif" align="absmiddle"/>  
        </tc-webtag:iterator>
        <A href="?module=ThreadList&<%=ForumConstants.FORUM_ID%>=<%=forum.getID()%>&mc=<%=forum.getMessageCount()%>" class="rtbcLink"><%=forum.getName()%></A> <img src="/i/interface/exp_w.gif" align="absmiddle"/> 
        <A href="?module=Message&<%=ForumConstants.MESSAGE_ID%>=<%=message.getID()%>" class="rtbcLink"><%=message.getSubject()%></A> <img src="/i/interface/exp_w.gif" align="absmiddle"/>
        Revision History 
        <%  if (revisionList.size() == 1) { %> 
                (1 edit) 
        <%  } else { %> 
                (<%=revisionList.size()%> edits) 
        <%  } %>
        </b><br>
    </td>
</tr>
</table>

<%-------------POSTS---------------%>
<table cellpadding="0" cellspacing="0" class="rtTable">
   <tr>
      <td class="rtHeader" colspan="2">
         <div style="float: right; padding-left: 5px; white-space: nowrap;">
            <a name=<%=message.getID()%>><tc-webtag:format object="${message.modificationDate}" format="MMM d, yyyy 'at' h:mm a z" timeZone="${timezone}"/> 
         </div>
         <%=message.getSubject()%></a>
         <%  if (message.getParentMessage() != null) { %>
            (response to <A href="?module=Message&<%=ForumConstants.MESSAGE_ID%>=<%=message.getParentMessage().getID()%><%if (!threadView.equals("")) { %>&<%=ForumConstants.THREAD_VIEW%>=<%=threadView%><% } %>" class="rtbcLink">post</A><%if (message.getParentMessage().getUser() != null) {%> by <tc-webtag:handle id="<%=message.getParentMessage().getUser().getID()%>"/><%}%>)
         <%  } %>
      </td>
   </tr>
   <tr>
      <td class="rtPosterCell">
         <div class="rtPosterSpacer">
            <%  if (ForumsUtil.displayMemberPhoto(user, message.getUser())) { %>
                <img src="<%=message.getUser().getProperty("imagePath")%>" width="55" height="61" border="0" class="rtPhoto" /><br/>
            <%  } %>
             <span class="bodyText"><%if (message.getUser() != null) {%><tc-webtag:handle id="<%=message.getUser().getID()%>"/><%}%></span><br/><%if (message.getUser() != null) {%><A href="?module=History&<%=ForumConstants.USER_ID%>=<%=message.getUser().getID()%>"><%=ForumsUtil.display(forumFactory.getUserMessageCount(message.getUser()), "post")%></A><%}%>
         </div>
      </td>
      <td class="rtTextCell" width="100%"><%=message.getBody()%>
      </td>
   </tr>
</table>

<tc-webtag:iterator id="revision" type="com.topcoder.web.forums.model.Revision" iterator='<%=revisionList.iterator()%>'>
<table cellpadding="0" cellspacing="0" class="rtTable">
   <tr>
      <td class="rtHeader" colspan="2">
         <div style="float: right; padding-left: 5px; white-space: nowrap;">
            <a name=<%=revision.getID()%>><tc-webtag:format object="${revision.modificationDate}" format="MMM d, yyyy 'at' h:mm a z" timeZone="${timezone}"/>
         </div>
         <%=revision.getSubject()%></a>
         <%  if (message.getParentMessage() != null) { %>
            (response to <A href="?module=Message&<%=ForumConstants.MESSAGE_ID%>=<%=message.getParentMessage().getID()%><%if (!threadView.equals("")) { %>&<%=ForumConstants.THREAD_VIEW%>=<%=threadView%><% } %>" class="rtbcLink">post</A><%if (message.getParentMessage().getUser() != null) {%> by <tc-webtag:handle id="<%=message.getParentMessage().getUser().getID()%>"/><%}%>)
         <%  } %>
         </a>
      </td>
   </tr>
   <tr>
      <td class="rtPosterCell">
         <div class="rtPosterSpacer">
            <%  if (ForumsUtil.displayMemberPhoto(user, message.getUser())) { %>
                <img src="<%=message.getUser().getProperty("imagePath")%>" width="55" height="61" border="0" class="rtPhoto" /><br/>
            <%  } %>
            <span class="bodyText"><%if (message.getUser() != null) {%><tc-webtag:handle id="<%=message.getUser().getID()%>"/><%}%></span><br/><%if (message.getUser() != null) {%><A href="?module=History&<%=ForumConstants.USER_ID%>=<%=message.getUser().getID()%>"><%=forumFactory.getUserMessageCount(message.getUser())%> posts</A><%}%>
         </div>
      </td>
      <td class="rtTextCell" width="100%"><%=revision.getBody()%>
      </td>
   </tr>
</table>
</tc-webtag:iterator>
<%-------------POSTS END---------------%>

<div style="clear:both;">&nbsp;</div>

    </div>
</div>
<%@ include file="includes/footer.jsp" %>
</body>
</html>