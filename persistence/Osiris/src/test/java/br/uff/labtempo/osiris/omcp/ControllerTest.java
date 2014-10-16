/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.omcp;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class ControllerTest {
    
    public ControllerTest() {
    }

    @Test
    public void testSomeMethod() throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        Controller c= new Controller() {

            @Override
            public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
                
              String path = "/:aid/controller/:bid/";  
              
              String requestPath = "/23s/controller/ds3/";
              
                Map<String,String> map = extract(requestPath, path);
                
                
                assertTrue(map.containsKey(":aid"));
                 assertTrue(map.containsKey(":bid"));
                
                return null;            
            }
        };
        c.process(null);
        
    }
    
}
