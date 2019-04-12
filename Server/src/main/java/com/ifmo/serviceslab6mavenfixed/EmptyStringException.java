package com.ifmo.serviceslab6mavenfixed;
import javax.xml.ws.WebFault;

public class EmptyStringException extends Exception {
    public static EmptyStringException DEFAULT_INSTANCE = new EmptyStringException("String value can not be empty");
    
    public EmptyStringException(String message) {
        super(message);
    }
    
}
