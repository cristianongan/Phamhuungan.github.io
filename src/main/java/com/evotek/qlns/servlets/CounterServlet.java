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
///@WebServlet(urlPatterns = "/counter", name = "counterServlet")
public class CounterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7309380177127500464L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        int count = (int)request.getServletContext().getAttribute("counter");

        out.println("Request counter: " + count);
    }

}