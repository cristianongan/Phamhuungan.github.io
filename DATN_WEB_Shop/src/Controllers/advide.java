package Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import Entity.wrong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import exception.user_erro;

@ControllerAdvice
public class advide {
	@ExceptionHandler(user_erro.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String not_found(HttpServletRequest req,user_erro e )
	{
		req.setAttribute("message",new wrong(HttpStatus.NOT_FOUND,e.getMessage()));
		return "e";
	}
	@ExceptionHandler(Exception.class)
	public String undefinded(HttpServletRequest req, Exception e) 
	{
		req.setAttribute("message",new wrong(HttpStatus.INTERNAL_SERVER_ERROR,"loi khong xac dinh!!!--->"+e.getMessage()));
		e.printStackTrace();
		return "e";
	}
	

}

