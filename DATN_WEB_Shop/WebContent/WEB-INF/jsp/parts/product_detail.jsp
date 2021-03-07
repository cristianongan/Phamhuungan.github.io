<%@page import="Entity.Product"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%!Product p; %>
<%p=(Product)request.getAttribute("product"); 

%>


				<div class="row">	  
			<div id="gallery" class="span3">
            <a href=<%=p.getUrlimg() %> title="anh">
				<img src=<%=p.getUrlimg() %> style="width:100%" alt="anh"/>
            </a>

			  
			 <div class="btn-toolbar">
			  <div class="btn-group">
			  </div>
			</div>
			</div>
			<div class="span6">
				<h3><%=p.getName().toUpperCase() %> </h3>
				<small><%=p.getMaterial() %></small>
				<hr class="soft"/>
				<form class="form-horizontal qtyFrm" method="post" action="cart/add">
				  <div class="control-group">
					<label class="control-label"><span><%=p.getPrice()%>Đ</span></label>
					<div class="controls">
					<input type="text" class="span1" placeholder="số lượng" name="num"/>
					<input type="hidden" class="span1" placeholder="id" name="id" value='<%=p.getId()%>'/>
					  <button type="submit" class="btn btn-large btn-primary pull-right"> Add to cart <i class=" icon-shopping-cart"></i></button>
					</div>
				  </div>
				
				
				<hr class="soft"/>
				<h4><%= p.getNumOfProduct() %> items in stock</h4>
				  <div class="control-group">
					<label class="control-label"><span>Color</span></label>
					<div class="controls">
					  <select class="span2" name="color">
						  <option><%=p.getColor() %></option>
						</select>
					</div>
					<div class="controls">
					  <select class="span2" name ="size">
						  <% for(String ss : p.getList_size())
							  out.print("<option value='"+ss+"'>"+ss+" </option>");
							  %>
						</select>
					</div>
				  </div>
				</form>
				<hr class="soft clr"/>
				<textarea style="width:500px;height:300px" readonly="readonly">
				<%= p.getNote() %>
				</textarea>
				<br class="clr"/>
			<a href="#" name="detail"></a>
			<hr class="soft"/>
			</div>
			
			<div class="span9">
<!--     	detail -->
          </div>

	</div>