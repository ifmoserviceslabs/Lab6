package com.ifmo.serviceslab6mavenfixed;

import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EmptyStringMapper implements ExceptionMapper<EmptyStringException> {
    @Override
    public Response toResponse(EmptyStringException e){
        HashMap map = new HashMap<String, String>();
        map.put("status", e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(map).build();
    }
}
