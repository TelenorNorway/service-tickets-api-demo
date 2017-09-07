package com.telenor.demo;


import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple TicketMaster.
 */
public class TicketMasterTest {

    @Test
    public void listIncidents(){
        long start = System.currentTimeMillis();
        //demo class
        TicketMaster tm = new TicketMaster();

        //get the access token
        AccessToken accessToken = tm.getAccessToken();
        assert (accessToken!=null);
        assert (accessToken.getAccess_token()!=null);
        //get tickets
        System.out.println("Fetching tickets");
        String list = tm.listIncidents(accessToken);
        assert (list!=null);
        assert(!list.isEmpty());
    }


}

