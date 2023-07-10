package com.uwi.idworks.exception;

public class RecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    
	 public  RecordNotFoundException() {
		 
	 }
	public RecordNotFoundException(String message) {
       super(message);
   }

   public RecordNotFoundException(String message, Throwable cause) {
       super(message, cause);
   }
}
