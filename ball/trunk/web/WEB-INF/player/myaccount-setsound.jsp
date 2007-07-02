<%@ page import="com.topcoder.user.profile.UserProfile,
				 com.orpheus.user.persistence.UserConstants" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="/paging" prefix="p" %>
<%@ taglib uri="/orpheus" prefix="orpheus" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<% 	UserProfile profile = (UserProfile)request.getAttribute("profile"); %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>The Ball :: My Account</title>
    <link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/styles.css"/>
</head>

<body id="page">
<div id="container">
<c:set var="subbar" value="myaccount" scope="page"/>
<%@ include file="header.jsp" %>

<div id="wrap">
    <div id="accountData">
    	<form action="${ctx}/server/player/myAccount-setSound.do" name="SetSoundForm" id="SetSoundForm" method="POST">
		    <table width="0" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td colspan=2><img src="${ctx}/i/h/title_setsound.gif" alt="Set Sound Preferences" /></td>
			  </tr>
			  <tr>
			    <td width="28%">&nbsp;</td>
			    <td width="72%">&nbsp;</td>
			  </tr>
			  <tr>
			    <td valign="top">
			      <%@ include file="myaccount-left.jsp" %>
			    </td>
			    <td valign="top">
			      <p>To listen to any of the sounds below, click on the name.&nbsp; When you are ready to select a sound that will be associated with finding a target when you are playing the Ball, click the radio button next to the sound.&nbsp; Note that you may only select one sound, or choose the "No Sound" option.<br /></p>
			      <table width="0" border="0" cellspacing="0" cellpadding="0">
			        <tr>
			          <td width="13%" align="right"><input type="radio" name="sound" id="sound" value="1" /></td>
			          <td width="87%">
			            <a href="#">Sound 1</a>
			          </td>
			        </tr>
			        <tr>
			          <td align="right"><input type="radio" name="sound" id="sound2" value="2" /></td>
			          <td><a href="#">Sound 2</a></td>
			        </tr>
			        <tr>
			          <td align="right"><input type="radio" name="sound" id="sound3" value="3" /></td>
			          <td><a href="#">Sound 3</a></td>
			        </tr>
			        <tr>
			          <td align="right"><input type="radio" name="sound" id="sound4" value="4" /></td>
			          <td><a href="#">Sound 4</a></td>
			        </tr>
			        <tr>
			          <td align="right"><input type="radio" name="sound" id="sound5" value="5" /></td>
			          <td><a href="#">Sound 5</a></td>
			        </tr>
			        <tr>
			          <td align="right"><input type="radio" name="sound" id="sound6" value="6" /></td>
			          <td><a href="#">Sound 6</a></td>
			        </tr>
			        <tr>
			          <td align="right"><input type="radio" name="sound" id="sound7" value="-1" /></td>
			          <td>No Sound</td>
			        </tr>
			  		<tr>
	          		  <td align="right">&nbsp;</td>
	          		  <td><input type="image" src="${ctx}/i/b/btn_save.gif" alt="Save"/></td>
	          	 	</tr>
			      </table>      
			    </td>
			  </tr>
			</table>
		</form>
	</div>
    <div class="tablebot">
      <p>&nbsp;</p>
    </div>
  </div>
</div>

</div>
<%@ include file="/includes/footer.jsp" %>
</body>
</html>