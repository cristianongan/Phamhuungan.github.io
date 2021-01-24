<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="Entity.Cart"%>
<%! Cart cart;%>
<%  cart =(Cart) request.getSession().getAttribute("cart");
	if(cart!=null)
		out.print("<div class='well well-small'>"
		+"<a id='myCart' href='/DATN_WEB_Shop/cart'>"
		+"<img src='/DATN_WEB_Shop/themes/images/ico-cart.png' alt='cart'>"+cart.getNumofproduct()+" Items in your cart " 
		+"<span class='badge badge-warning pull-right'>"+cart.getPrice()+"</span>"
		+"</a></div>");
%>

