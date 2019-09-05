package com.example.filght.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.filght.demo.dto.ExceptionResponse;
import com.example.filght.demo.exceptions.InvalidPaginationParameter;
import com.example.filght.demo.exceptions.InvalidSortParameterException;

@ControllerAdvice
public class CommonExceptionHandler {
	 @ExceptionHandler({InvalidSortParameterException.class})
	    public @ResponseBody ExceptionResponse handleInvalidSortException(InvalidSortParameterException e,HttpServletRequest request){
		 ExceptionResponse error = new ExceptionResponse();
			error.setErrorMessage(e.getMessage());
			error.callerURL(request.getRequestURI());
		 	return error;
	    }
	 @ExceptionHandler({InvalidPaginationParameter.class})
	    public @ResponseBody ExceptionResponse handleInvalidPaginationException(InvalidPaginationParameter e,HttpServletRequest request){
		 ExceptionResponse error = new ExceptionResponse();
			error.setErrorMessage(e.getMessage());
			error.callerURL(request.getRequestURI());
		 	return error;
	    }
}
