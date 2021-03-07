<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
        <%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
  <%@page import="Entity.Category"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entity.Product"%>
<%@page import="java.util.List"%>
<div id="logoArea" class="navbar">
<a id="smallScreen" data-target="#topMenu" data-toggle="collapse" class="btn btn-navbar">
	<span class="icon-bar"></span>
	<span class="icon-bar"></span>
	<span class="icon-bar"></span>
</a>
  <div class="navbar-inner">
    <a class="brand" href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/themes/images/logo.png" alt="Bootsshop"/></a>
		<form class="form-inline navbar-search" method="post" action="/DATN_WEB_Shop/search" >
		<input id="srchFld" class="srchTxt" type="text" name="words" />
		  <select class="srchTxt" name="id_cat">
		  <option value="0">All</option>
			<%! List<Category> lc; %>
			<% lc=(List<Category>) request.getAttribute("cat"); %>
			<%if(lc!=null)
			for(Category c: lc)
			{
				out.print("<option value='"+c.getCategoryId()+"'>"+c.getName()+"</option>");
			}
			
			%>
		</select> 
		  <button type="submit" id="submitButton" class="btn btn-primary">Go</button>
    </form>

    <ul id="topMenu" class="nav pull-right">
	 <li class="">
	 <% 
	 	if(request.getSession().getAttribute("user")==null)
	 		out.print("<a href='"+request.getContextPath()+"/user/register'>Register</a>");
	 	else
	 		out.print("<a href='https://www.facebook.com/goicam.ruoi/'>Contact</a>");
	 %>
	 </li>
	 <li class="">
	 <% if(request.getSession().getAttribute("user")==null)
		 out.print("<a href='#login' role='button' data-toggle='modal' style='padding-right:0'><span class='btn btn-large btn-success'>Login</span></a>");
	 else
		 out.print("<a href='/DATN_WEB_Shop/user/logout' role='button' data-toggle='modal' style='padding-right:0'><span class='btn btn-large btn-success'>Logout</span></a>");
		 %>
	<div id="login" class="modal hide fade in" tabindex="-1" role="dialog" aria-labelledby="login" aria-hidden="false" >
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3>Login Block</h3>
		  </div>
		  <div class="modal-body">
			<form action="/DATN_WEB_Shop/user/login2" method="post" class="form-horizontal loginFrm">
			  <div class="control-group">								
				<input type="username" id="username" placeholder="username" name="username">
			  </div>
			  <div class="control-group">
				<input type="password" id="password" placeholder="Password" name="password">
			  </div>
			  <div class="control-group">
			  </div>
			  <button type="submit" class="btn btn-success">Sign in</button>
			</form>		
			
			<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		  </div>
	</div>
	</li>
    </ul>
  </div>
</div>
