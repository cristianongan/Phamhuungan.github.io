<%@page import="java.util.List"%>
<%@page import="Entity.user_full"%>
<%@page import="Entity.Product"%>
<%@page import="Entity.Cart"%>
<%@page import="Entity.order"%>
<%@page import="Entity.user"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Order detail</title>
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
<%! String a ="guest";user u; %>
<% u =(user) request.getSession().getAttribute("user");
if(u!=null)
{
	a=u.getUsername();
}
%>
<div id="header">
<div class="container">
<div id="welcomeLine" class="row">
	<div class="span6">Welcome!<strong><%=a %></strong></div>
	<div class="span6">
	</div>
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
	<%! order od;int z;int tonggia;%>
	<%  od =(order) request.getAttribute("list_order");%>
    <ul class="breadcrumb">
		<li><a href="${pageContext.request.contextPath}/home">Home</a> <span class="divider">/</span></li>
		<li class="active">Detail</li>
    </ul>	
    <h3>  <%=od.getMadh() %> [ <small><%=od.getNgay_tao_don() %> </small>]</h3>	
	<hr class="soft"/>		
			
	<table class="table table-bordered">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  <th>Số lượng</th>
				  <th>Giá</th>
                  <th>Tổng giá</th>
				</tr>
              </thead>
              <tbody>
               <%z=0;tonggia=0;
               for(Product p: od.getList()) 
               {
            	   z= p.getNumOfProduct()*Integer.parseInt(p.getPrice());
            	   out.print(
                           "<td>"+p.getName()+"</td>"+
                           "<td>"+p.getNumOfProduct()+"</td>"+
                           "<td>"+p.getPrice()+"</td>"+
                           "<td>"+z+"</td>"+
                         "</tr>");
            	   tonggia+=z;
               }
              
               %>
				  <tr>
               <td colspan="4" style="text-align:right"><strong>TOTAL =</strong></td>
               <td class="label label-important" style="display:block"> <strong> <%=tonggia %> Đ </strong></td>
             </tr>
               
				</tbody>
            </table>
			
					
	<a href="${pageContext.request.contextPath}/home" class="btn btn-large"><i class="icon-arrow-left"></i> Tiếp tục mua sắm </a>
	
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
	
</body>
</html>