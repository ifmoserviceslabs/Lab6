package com.ifmo.serviceslab6mavenfixed;
 
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;
    
    @Override
    public ContainerRequest filter(ContainerRequest request) {
        authenticate(request);
        return request;
    }
    
    private Boolean checkUserExistence(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM public.user WHERE username = '" + username + "' AND password = '" + password + "'");
            if(rs.next())
                return true;
        } catch(SQLException e){
            return false;
        }
        return false;
    }
    
    private void authenticate(ContainerRequest request) {
        // Extract authentication credentials
        String authentication = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
        if (authentication == null) {
            throw new AuthenticationException("Authentication credentials are required");
        }
        if (!authentication.startsWith("Basic ")) {
            throw new AuthenticationException("Only HTTP Basic authentication is supported");
        }
        authentication = authentication.substring("Basic ".length());
        String[] values = Base64.base64Decode(authentication).split(":");
        if (values.length < 2) {
            throw new AuthenticationException("Invalid syntax for username and password");
        }
        String username = values[0];
        String password = values[1];
        if ((username == null) || (password == null)) {
            throw new AuthenticationException("Missing username or password");
        }
        
        if(checkUserExistence(username, password)) {
            System.out.println("USER AUTHENTICATED");
        } else {
            System.out.println("USER NOT AUTHENTICATED");
            throw new AuthenticationException("Invalid username or password\r\n");
        }
    }
}
