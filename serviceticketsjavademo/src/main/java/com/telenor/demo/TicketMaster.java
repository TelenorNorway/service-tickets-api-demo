package com.telenor.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Hello world!
 *
 */
class TicketMaster
{

    //Authorization header name
    private static final String Auth_Header = "Authorization";

    //The authentication mechanism prefix for Basic Authentication
    private static final String Auth_Header_Prefix_BASIC = "Basic "; //note the space

    //The authentication mechanism prefix for the OAUTH token
    private static final String Auth_Header_Prefix_BEARER = "Bearer "; //note the space

    //For illustrative purposes only. Get this from a secure, ecrypted configuration file
    //DO NOT put credentials in your code.
    private static final String CLIENT_ID = {{client_id}};
    private static final String CLIENT_SECRET = {{client_secret}};
    private static final String SYSTEM_USERNAME = {{username}};
    private static final String SYSTEM_PASSWORD = {{password}};

    //Endpoints
    String API_BASE_URL = "https://api.telenor.no";
    String OAUTH_TOKEN_RESOURCE = "/oauth/v2/token";
    String SERVICE_TICKET_RESOURCE = "/service-tickets/v2/incidents";

    /**
     * Will retrieve an access token to be used for the Service Ticket API.
     * @return access_token
     */
    AccessToken getAccessToken(){

        //prepare basic auth header
        String usernameAndPassword = CLIENT_ID + ":" + CLIENT_SECRET;
        String authorizationHeaderValue = Auth_Header_Prefix_BASIC + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

        //prepare body of /token request
        Form form = new Form();
        form.param("grant_type", "password");
        form.param("username", SYSTEM_USERNAME);
        form.param("password", SYSTEM_PASSWORD);

        //build and execute HTTP request
        Client client = ClientBuilder.newClient();
        Response response = client.target(API_BASE_URL)
                .path(OAUTH_TOKEN_RESOURCE)
                .request(MediaType.APPLICATION_JSON)
                .header( Auth_Header, authorizationHeaderValue ) // The basic authentication header goes here
                .header("Content-Type" , "application/x-www-form-urlencoded")
                .post(Entity.form(form),  Response.class);

        //Check response
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            AccessToken jsonResult = response.readEntity(AccessToken.class);
            System.out.println(jsonResult.toString());
            //ok, fetch a list of incidents.
            return jsonResult;
        }else {
            System.err.println(response.getStatus());
            System.err.println(response.readEntity(String.class));
            return null;
        }
    }

    String listIncidents(AccessToken accessToken){

        //for demonstrational purposes, check if token is still valid.
        // If not, just get a new one. (refresh token is not yet supported)
        if(accessToken.hasExpired() ){
            //it has expired, so just replace it.
            accessToken = getAccessToken();
        }

        //build and execute HTTP request
        Client client = ClientBuilder.newClient();
        Response response = client.target(API_BASE_URL)
                .path(SERVICE_TICKET_RESOURCE)
                .request(MediaType.APPLICATION_JSON)
                .header( Auth_Header, Auth_Header_Prefix_BEARER+accessToken.getAccess_token() ) // The Access token goes here!
                .get(Response.class);

        //Check response
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String jsonResult = response.readEntity(String.class);
            System.out.println(jsonResult);
            return jsonResult;
        }else {
            System.err.println(response.getStatus());
            System.err.println(response.readEntity(String.class));
            return null;
        }
    }

    TicketMaster(){}
}
