 <%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="org.springframework.web.util.UrlPathHelper"%>
<div class="pagination">
<%! int p1; int p2;int pp;UrlPathHelper uph;%>
<% 
uph =new UrlPathHelper();
try{
	p1 = Integer.parseInt(request.getParameter("p"));
}catch(Exception e)
{
	p1 = 1;
}
pp=(Integer)request.getAttribute("num");
p2 =p1+2;
if(p2>pp) p2 =pp;
if(p1>=1)
out.print("<ul><li><a href='"+uph.getOriginatingRequestUri(request)+"?p="+1+"'>&lsaquo;</a></li>");
else
	out.print("<ul>");
if(p1>2)
	out.print("<li><a href=''>...</a></li>");
for(int i = p1;i<=p2;i++)
{
	if(i!=1)
	out.print("<li><a href='"+uph.getOriginatingRequestUri(request)+"?p="+i+"'>"+i+"</a></li>");
}
if(p2<pp-2)
	out.print("<li><a href=''>...</a></li>");
if(p1<pp)
out.print("<li><a href='"+uph.getOriginatingRequestUri(request)+"?p="+pp+"'>&rsaquo;</a></li></ul>");
else
	out.print("</ul>");
%>
	</div>