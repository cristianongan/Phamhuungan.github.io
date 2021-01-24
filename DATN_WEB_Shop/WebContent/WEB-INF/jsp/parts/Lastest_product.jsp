
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="Entity.Category"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entity.Product"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h4>Result </h4>
			  <ul class="thumbnails">
				<%! List<Product> lt; %>
				<% lt=(List<Product>) request.getAttribute("list_lastest_product"); %>
				<% if(lt!=null)
						for(Product p: lt)
						{
							out.print("<li class='span3'>");
							out.print("<div class='thumbnail'>");
							out.print("<a href='/DATN_WEB_Shop/product_detail?id="+p.getId()+"'><img src='/DATN_WEB_Shop/"+p.getUrlimg()+"' alt=''/></a>");
							out.print("<div class='caption'>");
							out.print("<h5>"+p.getName()+"</h5>");
							out.print("<p> num of product:"+p.getNumOfProduct()+"</p>");
							out.print("<h4 style='text-align:center'><a class='btn' href='/DATN_WEB_Shop/product_detail?id="+p.getId()+"'>"+
							"<i class='icon-zoom-in'></i></a> <a class='btn' href='/DATN_WEB_Shop/cart/add?id="+p.getId()+"&size="+p.getList_size().get(0)+"'>Add to <i class='icon-shopping-cart'></i></a>");
							out.print("<a class='btn btn-primary' href='#''>"+p.getPrice()+"Đ</a></h4>");
							out.print("</div></div></li>");
						}
					
				
				%>
			  </ul>	