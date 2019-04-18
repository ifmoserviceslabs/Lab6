package com.ifmo.serviceslab5client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.MediaType;


public class App {
    private static final String URL = "http://localhost:8080/rest/employees";
    
    public static void main(String[] args) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("user", "password"));
        printList(filterEmployees(client, null, null, 0, null, 0));
        System.out.println();
        printList(filterEmployees(client, null, "Иванов", 0, null, 0));
        System.out.println();
        printList(filterEmployees(client, "Егор", null, 0, null, 0));
        int id = createEmployee(client, "Иван", "Иванов", 31, "city", 21);
        System.out.println("Created employee with id " + id);
        String status = updateEmployee(client, id, null, null, 0, null, 22);
        System.out.println("Update employee status: " + status);
        status = deleteEmployee(client, id);
        System.out.println("Delete employee status: " + status);
        System.out.println("Errors processing demonstration");
        try{
            updateEmployee(client, 0, null, null, 0, null, 22);
        } catch(IllegalStateException e){
            System.out.println("Update employee status: " + e.getMessage());
        }
        try {
            deleteEmployee(client, 0);
        } catch(IllegalStateException e){
            System.out.println("Delete employee status: " + e.getMessage());
        }
        try {
            createEmployee(client, "", "", 0, "", 0);
        } catch(IllegalStateException e){
            System.out.println("Create employee status: " + e.getMessage());
        }
        System.out.println("Attempt to get data with wrong user");
        try {
            client.addFilter(new HTTPBasicAuthFilter("admin", "password"));
            filterEmployees(client, null, "Иванов", 0, null, 0);
        }
        catch(IllegalStateException e){
            System.out.println("Got error " + e.getMessage());
        }
    }
    
    private static WebResource getWebResource(Client client, int id, String name,
            String surname,
            int age,
            String city,
            int department){
        WebResource webResource = client.resource(URL);
        if (id > 0) {
            webResource = webResource.queryParam("id", String.valueOf(id));
        }
        if (name != null) {
            webResource = webResource.queryParam("name", name);
        }
        if (surname != null) {
            webResource = webResource.queryParam("surname", surname);
        }
        if (age > 0) {
            webResource = webResource.queryParam("age", String.valueOf(age));
        }
        if (city != null ) {
            webResource = webResource.queryParam("city", city);
        }
        if (department > 0) {
            webResource = webResource.queryParam("department", String.valueOf(department));
        }
        return webResource;
    }
    
    private static int createEmployee(Client client, String name,
            String surname,
            int age,
            String city,
            int department){
        WebResource webResource = getWebResource(client, 0, name, surname, age, city, department);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            GenericType<HashMap<String, String>> type = new GenericType<HashMap<String, String>>() {};
            HashMap map = response.getEntity(type);
            throw new IllegalStateException((String) map.get("status"));
        }
        return response.getEntity(int.class);
    }
    
    private static String updateEmployee(Client client, int id,
            String name,
            String surname,
            int age,
            String city,
            int department){
        WebResource webResource = getWebResource(client, id, name, surname, age, city, department);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);
        GenericType<HashMap<String, String>> type = new GenericType<HashMap<String, String>>() {};
        HashMap map = response.getEntity(type);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException((String) map.get("status"));
        }
        return (String) map.get("status");
    }
    
    private static String deleteEmployee(Client client, int id){
        WebResource webResource = getWebResource(client, id, null, null, 0, null, 0);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        GenericType<HashMap<String, String>> type = new GenericType<HashMap<String, String>>() {};
        HashMap map = response.getEntity(type);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException((String) map.get("status"));
        }
        return (String) map.get("status");
    }
    
    private static List<Employee> filterEmployees(
            Client client,
            String name,
            String surname,
            int age,
            String city,
            int department
            ) {
        WebResource webResource = getWebResource(client, 0, name, surname, age, city, department);
        ClientResponse response =
        webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed");
        }
        GenericType<List<Employee>> type = new GenericType<List<Employee>>() {};
        return response.getEntity(type);
    }
    
    private static void printList(List<Employee> persons) {
        for (Employee person : persons) {
            System.out.println(person);
        }
    }
}