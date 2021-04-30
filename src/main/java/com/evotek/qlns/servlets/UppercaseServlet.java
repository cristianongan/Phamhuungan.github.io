/**
 * 
 */
package com.evotek.qlns.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LinhLH
 *
 */

//@WebServlet(urlPatterns = "/uppercase", name = "uppercaseServlet")
public class UppercaseServlet extends HttpServlet {

	private static final long serialVersionUID = 3832588190637142310L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String inputString = request.getParameter("input").toUpperCase();

		PrintWriter out = response.getWriter();

		out.println(inputString);
	}

}
