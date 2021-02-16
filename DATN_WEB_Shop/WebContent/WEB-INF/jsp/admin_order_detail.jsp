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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/favicon.png">
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
<div id="header">
<div class="container">
<div id="welcomeLine" class="row">
	<div class="span6">ADMIN - controller<strong></strong></div>
	<div class="span6">
	</div>
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
	<%! order od;int z;int tonggia;String[] st ={"Đang giao hàng","Đóng gói","Đã giao hàng"};%>
	<%  od =(order) request.getAttribute("_order");%>
    <ul class="breadcrumb">
		<li><a href="${pageContext.request.contextPath}/admin">Home</a> <span class="divider">/</span></li>
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
            <table class="table table-bordered">
			 <tbody><tr><th>Đơn hàng</th></tr>
			 <tr> 
			 <td>
				<form class="form-horizontal" method='post' action='${pageContext.request.contextPath}/admin/admin_update_order'>
				<input type='hidden' name='madh' value='<%=od.getMadh() %>'/>
				  <div class="control-group">
					<label class="control-label" for="inputCountry">Địa chỉ giao hàng:</label>
					<div class="controls">
					  <input type="text" id="inputCountry" placeholder="Country" name='address' value='<%=od.getAddr() %>'>
					</div>
				  </div>
<!-- 				  <tr> -->
<!--                <td colspan="4" style="text-align:right"><strong>Trạng thái :</strong></td> -->
<%--                <td class="label label-important" style="display:block"> <strong> <%=od.getStatus() %></strong></td> --%>
<!--              </tr> -->
					<div class="control-group">
					<label class="control-label" for="inputPost">Trạng thái</label>
					<div class="controls">
					  <select name="status">
					  <option value='<%=od.getStatus() %>'><%=od.getStatus() %></option>
					  <%for(String s: st)
						  {
						  	if(!s.equals(od.getStatus()))
						  		out.print("<option value='"+s+"'>"+s+"</option>");
						  }
						  %>
					  	
					  </select>
					</div>
				  </div>
				  <div class="control-group">
					<div class="controls">
					  <button type="submit" class="btn">Cập nhật </button>
					</div>
				  </div>
				</form>				  
			  </td>
			  </tr>
            </tbody></table>
			
	<a href='${pageContext.request.contextPath}/admin/drop?p=<%=od.getMadh() %>' class="btn btn-large"> Hủy đơn hàng </a>			
	<a href='${pageContext.request.contextPath}/admin/excel?p=<%=od.getMadh() %>' class="btn btn-large"> Lấy đơn hàng </a>
	
</div>
</div></div>
</div>
<!-- MainBody End ============================= -->
<!-- Footer ================================================================== -->
		
<!-- Placed at the end of the document so the pages load faster ============================================= -->
	<script src="themes/js/jquery.js" type="text/javascript"></script>
	<script src="themes/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="themes/js/google-code-prettify/prettify.js"></script>
	
	<script src="themes/js/bootshop.js"></script>
    <script src="themes/js/jquery.lightbox-0.5.js"></script>
	
</body>
</html>