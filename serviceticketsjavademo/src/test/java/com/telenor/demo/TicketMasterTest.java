package com.telenor.demo;


import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Unit test for simple TicketMaster.
 */
public class TicketMasterTest {

    private static final Logger logger = Logger.getLogger(TicketMasterTest.class.getName());

    @Test
    public void listIncidents(){
        logger.info("Starting test");
        //demo class
        TicketMaster tm = new TicketMaster();

        //get the access token
        logger.info("Get the access token");
        AccessToken accessToken = tm.getAccessToken();
        assert (accessToken!=null);
        assert (accessToken.getAccess_token()!=null);

        //for demonstrational purposes only check if token is still valid (it probably will be).
        //If not, just get a new one. (refresh token is not yet supported)
        if(accessToken.hasExpired() ){
            //it has expired, so just replace it.
            accessToken = tm.getAccessToken();
        }
        //get tickets
        logger.info("Get the tickets");
        String list = tm.listIncidents(accessToken);
        assert (list!=null);
        assert(!list.isEmpty());
    }


}

