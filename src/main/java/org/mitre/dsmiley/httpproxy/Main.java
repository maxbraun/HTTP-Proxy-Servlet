package org.mitre.dsmiley.httpproxy;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    private static final String firstArg = "First Argument needs to be a number -> local port";
    private static final String secondArg = "second argument needs to be an url -> remote url";
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException(firstArg + "\n" + secondArg);
        }
        Server server = new Server(port(args[0]));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = new ServletHolder(new URITemplateProxyServlet());
        servletHolder.setInitParameter(ProxyServlet.P_TARGET_URI, url(args[1]));
        context.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }

    public static int port(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(firstArg);
        }
    }

    public static String url(String arg) {
        try {
            return new URL(arg).toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(secondArg);
        }
    }
}
