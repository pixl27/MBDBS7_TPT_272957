/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.itu.tolotra.javarest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author tolot
 */
@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloResource {
    public HelloResource() { }
    
    @GET
    public String getHello() {
        return "Bonjour ENSMA";
    }
}

