package com.orpheus.game.stresstests;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MockServletContext implements ServletContext {
    private Map attribute = new HashMap();

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public ServletContext getContext(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public int getMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public int getMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public String getMimeType(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public Set getResourcePaths(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getResource(String arg0) throws MalformedURLException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public InputStream getResourceAsStream(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public RequestDispatcher getNamedDispatcher(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Servlet getServlet(String arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public Enumeration getServlets() {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServletNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public void log(String arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public void log(Exception arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public void log(String arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public String getRealPath(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public String getServerInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public String getInitParameter(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public Enumeration getInitParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public Object getAttribute(String arg0) {
        return this.attribute.get(arg0);
    }

    public Enumeration getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public void setAttribute(String arg0, Object arg1) {
        this.attribute.put(arg0, arg1);
    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public void removeAttribute(String arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * <p>
     * not implemented.
     * </p>
     */
    public String getServletContextName() {
        // TODO Auto-generated method stub
        return null;
    }

}
