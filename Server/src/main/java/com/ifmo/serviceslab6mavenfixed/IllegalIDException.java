package com.ifmo.serviceslab6mavenfixed;
import javax.xml.ws.WebFault;


public class IllegalIDException extends Exception {
    public static IllegalIDException DEFAULT_INSTANCE = new IllegalIDException("Invalid id value");
    
    public IllegalIDException(String message) {
        super(message);
    }
    
}
