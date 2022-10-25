package com.qa.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND,reason="product doesnt exist")
public class ProductNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
