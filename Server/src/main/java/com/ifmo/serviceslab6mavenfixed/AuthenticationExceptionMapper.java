package com.ifmo.serviceslab6mavenfixed;

import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

    @Override
    public Response toResponse(AuthenticationException e) {
        HashMap map = new HashMap<String, String>();
        map.put("status", e.getMessage());
        return Response.status(Status.UNAUTHORIZED).type("application/json").entity(map).build();
    }
}
