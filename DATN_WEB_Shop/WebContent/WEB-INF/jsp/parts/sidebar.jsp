<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="Entity.Category2"%>
<%@page import="Entity.Category"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entity.Product"%>
<%@page import="java.util.List"%>
	<div id="sidebar" class="span3">
<!-- 		cart -->
	<jsp:include page="cart.jsp"></jsp:include>
<!-- end cart -->
		<ul id="sideManu" class="nav nav-tabs nav-stacked">
		<%! List<Category> l; %>
			<% l=(List<Category>) request.getAttribute("cat"); %>
			<%if(l!=null) 
			for(Category c: l)
			{
				out.print("<li class='subMenu'><a>"+c.getName()+"</a><ul style='display:none'>");
				for(Category2 c2: c.getList_cat2())
				{
					out.print("<li><a href='/DATN_WEB_Shop/product_list/"+c.getCategoryId()+"/"+c2.getCategoryId2()+"'><i class='icon-chevron-right'></i>"+c2.getName()+"</a></li>");
				}
				out.print("</ul></li>");
			}
			
			%>
		</ul>
		<br/>
			<div class="thumbnail">
				<img src="/DATN_WEB_Shop/themes/images/payment_methods.png" title="Bootshop Payment Methods" alt="Payments Methods">
				<div class="caption">
				  <h5>Payment Methods</h5>
				</div>
			  </div>
	</div>