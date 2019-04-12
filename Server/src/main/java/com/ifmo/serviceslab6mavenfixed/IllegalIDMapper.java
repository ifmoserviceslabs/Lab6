package com.ifmo.serviceslab6mavenfixed;

import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalIDMapper implements ExceptionMapper<IllegalIDException> {
    @Override
    public Response toResponse(IllegalIDException e){
        HashMap map = new HashMap<String, String>();
        map.put("status", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(map).build();
    }
}
