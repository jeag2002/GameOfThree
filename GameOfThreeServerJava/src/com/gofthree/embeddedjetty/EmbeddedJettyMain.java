package com.gofthree.embeddedjetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.gofthree.embeddedjetty.servlet.*;

import java.net.URL;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;


/**
 * Embedded Jetty Server. Contains GameEngineServlet + webapp resources.
 * @author Usuario
 *
 */

public class EmbeddedJettyMain {

		public static void main(String[] args) throws Exception {
			
			
			//create servlet handler; "GameEngineServlet" asociate to web context "app"
			ServletContextHandler servlet_handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			servlet_handler.setContextPath("/app");
		    
		    ServletHolder jerseyServlet = servlet_handler.addServlet(
		              org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		    jerseyServlet.setInitOrder(0);
		    
		    jerseyServlet.setInitParameter(
		            "jersey.config.server.provider.classnames",
		            GameEngineServlet.class.getCanonicalName());
		    
		    //create resource handler (index.html, chat.js, jquery-1.9.1.js) asociate to root context ("/")
	        ResourceHandler resource_handler = new ResourceHandler();
	        resource_handler.setDirectoriesListed(true);
	        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
	        resource_handler.setResourceBase("./webapp/.");  
		    
	        //create http server listening by port 8080
	        Server server = new Server(8080);
	        HandlerList handlers = new HandlerList();
	        handlers.setHandlers(new Handler[] { resource_handler, servlet_handler});
	        server.setHandler(handlers);
	        
	        
		    try {
	            server.start();
	            server.join();
	        } finally {
	           server.destroy();
	        }

		}

}
	
	