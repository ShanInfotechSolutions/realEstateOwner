package com.shanInfotech.springBootMicroservicesOwnerClient.exception;

public class OwnerIdNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public OwnerIdNotFoundException(String message) {
        super(message);
    }

}
