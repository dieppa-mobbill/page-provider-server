package com.pageprovider;

import com.pageprovider.rest.PageProviderRest;
import com.pageprovider.util.MysqlPool;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Hello world!
 *
 */
public class App 
{

    private final static String CLASSNAMES = "jersey.config.server.provider.classnames";

    private static boolean live;

    public static void main( String[] args ) throws Exception{


        live = (args.length >0 && "live".equals(args[0]));

        if(live){
            MysqlPool.init("mobbillmaster1.cdotzokutlkn.eu-west-1.rds.amazonaws.com", 3306, "mobbill", "grails", "Gr@1l5");
        }else{
            MysqlPool.init("127.0.0.1", 3306, "mobbill", "root", "pa55w0rd");
        }



        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(80);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(CLASSNAMES, PageProviderRest.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }


    public boolean isLive(){
        return live;
    }
}
