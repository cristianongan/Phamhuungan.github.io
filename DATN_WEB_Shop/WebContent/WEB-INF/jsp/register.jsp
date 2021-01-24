<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    	 <%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Register</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<!--Less styles -->
   <!-- Other Less css file //different less files has different color scheam
	<link rel="stylesheet/less" type="text/css" href="themes/less/simplex.less">
	<link rel="stylesheet/less" type="text/css" href="themes/less/classified.less">
	<link rel="stylesheet/less" type="text/css" href="themes/less/amelia.less">  MOVE DOWN TO activate
	-->
	<!--<link rel="stylesheet/less" type="text/css" href="themes/less/bootshop.less">
	<script src="themes/js/less.js" type="text/javascript"></script> -->
	
<!-- Bootstrap style --> 
    <link id="callCss" rel="stylesheet" href="themes/bootshop/bootstrap.min.css" media="screen"/>
    <link href="themes/css/base.css" rel="stylesheet" media="screen"/>
<!-- Bootstrap style responsive -->	
	<link href="themes/css/bootstrap-responsive.min.css" rel="stylesheet"/>
	<link href="themes/css/font-awesome.css" rel="stylesheet" type="text/css">
<!-- Google-code-prettify -->	
	<link href="themes/js/google-code-prettify/prettify.css" rel="stylesheet"/>
<!-- fav and touch icons -->
    <link rel="shortcut icon" href="themes/images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="themes/images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="themes/images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="themes/images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="themes/images/ico/apple-touch-icon-57-precomposed.png">
	<style type="text/css" id="enject"></style>
  </head>
<body>
<div id="header">
<div class="container">
<div id="welcomeLine" class="row">
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
    <ul class="breadcrumb">
		<li><a href="${pageContext.request.contextPath}/home">Home</a> <span class="divider">/</span></li>
		<li class="active">Registration</li>
    </ul>
	<h3> Registration</h3>	
	<div class="well">
	<!--
	<div class="alert alert-info fade in">
		<button type="button" class="close" data-dismiss="alert">×</button>
		<strong>Lorem Ipsum is simply dummy</strong> text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s
	 </div>
	<div class="alert fade in">
		<button type="button" class="close" data-dismiss="alert">×</button>
		<strong>Lorem Ipsum is simply dummy</strong> text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s
	 </div>
	 <div class="alert alert-block alert-error fade in">
		<button type="button" class="close" data-dismiss="alert">×</button>
		<strong>Lorem Ipsum is simply</strong> dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s
	 </div> -->
<!-- 	 bat dau form -->
	
	<f:form class="form-horizontal" modelAttribute="user" method="post" action="register" >
		<h4>Thông tin cá nhân</h4>
		<div class="control-group">
		<label class="control-label">Danh xưng <sup>*</sup></label>
		<div class="controls">
		<f:select class="span1" name="title" path="title">
			<f:option value="">-</f:option>
			<f:option value="Mr">Mr.</f:option>
			<f:option value="Mrs">Mrs</f:option>
			<f:option value="Miss">Miss</f:option> 
		</f:select>
		<f:errors path="title" cssClass="error"></f:errors>
		</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="inputFname1">Họ<sup>*</sup></label>
			<div class="controls">
			  <f:input type="text" id="inputFname1" placeholder="First Name" path="firstname"/>
			  <f:errors path="firstname" cssClass="error"></f:errors>
			</div>
		 </div>
		 <div class="control-group">
			<label class="control-label" for="inputLnam">Tên<sup>*</sup></label>
			<div class="controls">
			  <f:input type="text" id="inputLnam" placeholder="Last Name" path="lastname"/>
			</div>
		 </div>
		<div class="control-group">
		<label class="control-label" for="input_email">Username <sup>*</sup></label>
		<div class="controls">
		  <f:input type="text" id="input_email" placeholder="Username" path="username"/>
		  <f:errors path="username" cssClass="error"></f:errors>
		</div>
	  </div>	  
	<div class="control-group">
		<label class="control-label" for="inputPassword1">Password <sup>*</sup></label>
		<div class="controls">
		  <f:input type="password" id="inputPassword1" placeholder="Password" path="password"/>
		  <f:errors path="password" cssClass="error"></f:errors>
		</div>
	  </div>	  
		<div class="control-group">
		<label class="control-label">ngày sinh<sup>*</sup></label>
		<div class="controls">
		  <f:input type="text" path="DOB" />
		  <f:errors path="DOB" cssClass="error"></f:errors>
		</div>
	  </div>

	<div class="alert alert-block alert-error fade in">
		<button type="button" class="close" data-dismiss="alert">×</button>
		<strong></strong> trang web đang trong quá trình hoàn thiện chức năng đăng kí có thể không sử dụng được
	 </div>	
		
		<div class="control-group">
			<label class="control-label" for="address">Địa chỉ<sup>*</sup></label>
			<div class="controls">
			  <f:input type="text" id="address" placeholder="Adress" path="Address"/> <span>Street address, P.O. box, company name, c/o</span>
			  <f:errors path="Address" cssClass="error"></f:errors>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="city">Thành phố<sup>*</sup></label>
			<div class="controls">
			  <f:input type="text" id="city" placeholder="city" path="city"/> 
			  <f:errors path="Address" cssClass="error"></f:errors>
			</div>
		</div>			
		<div class="control-group">
			<label class="control-label" for="aditionalInfo">Thông tin thêm</label>
			<div class="controls">
			  <f:textarea name="aditionalInfo" id="aditionalInfo" cols="26" rows="3" path="additional_information"></f:textarea>
			  <f:errors path="Address" cssClass="error"></f:errors>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="phone">Home phone <sup>*</sup></label>
			<div class="controls">
			  <f:input type="text"  name="phone" id="phone" placeholder="phone" path="homephone"/> <span>You must register at least one phone number</span>
			  <f:errors path="homephone" cssClass="error"></f:errors>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="mobile">Mobile Phone </label>
			<div class="controls">
			  <f:input type="text"  name="mobile" id="mobile" placeholder="Mobile Phone" path="mobilephone"/> 
			  <f:errors path="mobilephone" cssClass="error"></f:errors>
			</div>
		</div>
		
	<p><sup>*</sup>Required field	</p>
	
	<div class="control-group">
			<div class="controls">
				<input type="hidden" name="email_create" value="1">
				<input type="hidden" name="is_new_customer" value="1">
				<input class="btn btn-large btn-success" type="submit" value="Register" />
			</div>
		</div>		
	</f:form>
<!-- 	ket thuc form -->
</div>

</div>
</div>
</div>
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