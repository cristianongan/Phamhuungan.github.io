package com.phamhuungan.thymeleafPractices.Controllers.Advides;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserAdvide {
	@ExceptionHandler(SQLException.class)
	@ResponseStatus()
	public String RegisterAdvide(SQLException e)
	{
		return e.getErrorCode()+"";
	}

}
