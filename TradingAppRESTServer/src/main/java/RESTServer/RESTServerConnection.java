package RESTServer;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTServerConnection
{

    public static void main(String[] args) throws Exception {
        Server server = new Server(8090); // TODO Config file

        server.setHandler(getJerseyHandler());

        server.start();
        System.out.println("REST Server started");

        server.join();
        System.out.println("REST Server joined");
    }

    private static Handler getJerseyHandler() {
        System.out.println("Create Jersey handler");
        ServletContextHandler handler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);

        handler.setContextPath("/");

        ServletHolder servletHolder = handler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                RESTServerEndpoint.class.getCanonicalName());

        return handler;
    }
}
