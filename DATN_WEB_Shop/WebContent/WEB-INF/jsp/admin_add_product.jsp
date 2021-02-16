<%@page import="java.util.List"%>
<%@page import="Entity.Category2"%>
<%@page import="Entity.Category"%>
<%@page import="Entity.Product"%>
<%@page import="Entity.user"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
     <%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Add product</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
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
</div>
</div>
<!-- Header End====================================================================== -->
<div id="mainBody">
	<div class="container">
	<div class="row">
<!-- Sidebar ================================================== -->
<!-- Sidebar end=============================================== -->
	<div class="span9">	
	<h3><%if(request.getAttribute("stt")!=null) out.print(request.getAttribute("stt")); %></h3>
<%! List<Category> c1; %>
<%c1 =(List<Category>) request.getAttribute("cat"); %>
<f:form class="form-horizontal qtyFrm" method="post" action="add_product" enctype="multipart/form-data" modelAttribute="product">  
			<label>Hình ảnh mô tả</label>
			<img id="output" src="" width="300" height="300" style="float:right;">
			<f:input name="photo" type="file" path="img" accept="image/*" onchange="document.getElementById('output').src = window.URL.createObjectURL(this.files[0])"/>
			<label>Tên sản phẩm</label><f:input path='name' type='text'/><br>
			<label>Màu sản phẩm</label>
			<f:input path='color' type='text'/><br>
			<label>Chất liệu sản phẩm</label>
			<f:input path='material' type='text'/><br>
			<label>Loại sản phẩm</label>
			<f:select path="id_c" type="number" >
				<%for(Category c:c1) 
				{
					for(Category2 cc: c.getList_cat2())
					out.print("<option value='"+c.getCategoryId()+","+cc.getCategoryId2()+"'>"+c.getName()+cc.getName()+
							"</option>");
				}		
					%> 
			</f:select></br>
			<label>Số lượng</label>
			<f:input path='NumOfProduct' type='number'/><br>
			<label>Giá thành</label>
			<f:input path='price' type='number'/><br>
			<label>Sizes</label>
			<f:input path='list_size' type='text'/><br>
			<label>Mô tả</label>	
			<f:textarea path="note" style="width:300px;height:300px" value="note"/><br>
			
				<button type="submit" class="btn btn-large btn-primary pull-right">Thêm sản phẩm</button>
				</f:form>
				<br class="clr"/>
			<a href="#" name="detail"></a>
			<hr class="soft"/>
			</div>
			
			<div class="span9">
<!--     	detail -->
          </div>

	</div>
</div>
</div> 

<!-- MainBody End ============================= -->
<!-- Footer ================================================================== -->
<!-- Placed at the end of the document so the pages load faster ============================================= -->
	<script src="themes/js/jquery.js" type="text/javascript"></script>
	<script src="themes/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="themes/js/google-code-prettify/prettify.js"></script>
	
	<script src="themes/js/bootshop.js"></script>
    <script src="themes/js/jquery.lightbox-0.5.js"></script>
	
	<!-- Themes switcher section ============================================================================================= -->
</body>
</html>