<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
<!--Less styles -->
<%-- 	<link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/themes/less/simplex.less"> --%>
<%-- 	<link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/themes/less/classified.less"> --%>
<%-- 	<link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/themes/less/amelia.less">   --%>
	
<%-- 	<link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/themes/less/bootshop.less"> --%>
<!-- 	<script src="lib/themes/js/less.js" type="text/javascript"></script> -->
	
<!-- Bootstrap style --> 
    <link id="callCss" rel="stylesheet" href="${pageContext.request.contextPath}/themes/bootshop/bootstrap.min.css" media="screen"/>
    <link href="${pageContext.request.contextPath}/themes/css/base.css" rel="stylesheet" media="screen"/>
<!-- Bootstrap style responsive -->	
	<link href="${pageContext.request.contextPath}/themes/css/bootstrap-responsive.min.css" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/themes/css/font-awesome.css" rel="stylesheet" type="text/css">
<!-- Google-code-prettify -->	
	<link href="${pageContext.request.contextPath}/themes/js/google-code-prettify/prettify.css" rel="stylesheet"/>
<!-- fav and touch icons -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/themes/images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${pageContext.request.contextPath}/themes/images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${pageContext.request.contextPath}/themes/images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${pageContext.request.contextPath}/themes/images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="${pageContext.request.contextPath}/themes/images/ico/apple-touch-icon-57-precomposed.png">
	<style type="text/css" id="enject"></style>
  </head>
<body>
<div id="header">
<div class="container">
<div id="welcomeLine" class="row">
	<div class="span6">Welcome!<strong>${user}</strong></div>
</div>
<!-- Navbar ================================================== -->
<jsp:include page="parts/search.jsp"></jsp:include>
</div>
</div>
<!-- Header End====================================================================== -->
<div id="mainBody">
	<div class="container">
	<div class="row">
<!-- Sidebar ================================================== -->
<jsp:include page="parts/sidebar.jsp"></jsp:include>
<!-- Sidebar end=============================================== -->
	<div class="span9">
<!-- bread cum -->
    <ul class="breadcrumb">
		<li><a href="${pageContext.request.contextPath}/home">Home</a> <span class="divider">/</span></li>
		<li class="active">Login</li>
    </ul>
	<h3> Login</h3>	
	<hr class="soft"/>
	
	<div class="row">
		<div class="span4">
			<div class="well">
			<h5>CREATE YOUR ACCOUNT</h5><br/>
			.<br/><br/><br/>
			<form action="register" >
			  <div class="controls">
			  <button type="submit" class="btn block">Create Your Account</button>
			  </div>
			</form>
		</div>
		</div>
		<div class="span1"> &nbsp;</div>
		<div class="span4">
			<div class="well">
			<h5>ALREADY REGISTERED ?</h5>
<!-- 			dang nhap -->
			<f:form action="login" method="post" modelAttribute="u">
			  <div class="control-group">
				<label class="control-label" for="inputEmail1">Username</label>
				<div class="controls">
					<f:input class="span3"  type="text" id="inputEmail1" placeholder="Username" path="username"/>
					<f:errors path="username" cssClass="error"></f:errors>
				</div>
			  </div>
			  <div class="control-group">
				<label class="control-label" for="inputPassword1">Password</label>
				<div class="controls">
				  <f:input type="password" class="span3"  id="inputPassword1" placeholder="Password" path="password"/>
				  <f:errors path="password" cssClass="error"></f:errors>
				</div>
			  </div>
			  <div class="control-group">
				<div class="controls">
				  <button type="submit" class="btn">Sign in</button> <a href="forgetpass.html">Forget password?</a>
				</div>
			  </div>
			</f:form>
		</div>
		</div>
	</div>	
	
</div>
</div></div>
</div>
<!-- MainBody End ============================= -->
<!-- Footer ================================================================== -->
		<div  id="footerSection">
	<div class="container">
		<div class="row">
			<div class="span3">
				<h5>ACCOUNT</h5>
				<a href="user/updateInfo">YOUR ACCOUNT</a>
				<a href="/DATN_WEB_Shop/orders">ORDER HISTORY</a>
			 </div>
			<div class="span3">
				<h5>INFORMATION</h5>
				<a href="https://www.facebook.com/goicam.ruoi/">CONTACT</a>  
				<a href="user/register">REGISTRATION</a>  
			 </div>
			<div class="span3">
				<h5>OUR OFFERS</h5>
			 </div>
			<div id="socialMedia" class="span3 pull-right">
				<h5>SOCIAL MEDIA </h5>
				<a href="https://www.facebook.com/goicam.ruoi/"><img width="60" height="60" src="themes/images/facebook.png" title="facebook" alt="facebook"/></a>
				<a href="#"><img width="60" height="60" src="themes/images/twitter.png" title="twitter" alt="twitter"/></a>
				<a href="#"><img width="60" height="60" src="themes/images/youtube.png" title="youtube" alt="youtube"/></a>
			 </div> 
		 </div>
		<p class="pull-right">&copy; Bootshop</p>
	</div><!-- Container End -->
	</div>
<!-- Placed at the end of the document so the pages load faster ============================================= -->
	<script src="themes/js/jquery.js" type="text/javascript"></script>
	<script src="themes/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="themes/js/google-code-prettify/prettify.js"></script>
	
	<script src="themes/js/bootshop.js"></script>
    <script src="themes/js/jquery.lightbox-0.5.js"></script>
	
	<!-- Themes switcher section ============================================================================================= -->

</body>
</html>