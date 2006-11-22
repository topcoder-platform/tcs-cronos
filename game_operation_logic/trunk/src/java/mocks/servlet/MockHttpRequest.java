/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <p>
 * Mock implementation of HttpServletRequest, for test purpose.
 * </p>
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MockHttpRequest implements HttpServletRequest {

	private Map parametersMap = new HashMap();
    private Map headers = new HashMap();
    private HttpSession session;

    
    private Map attributes = new HashMap();
    
    public MockHttpRequest(HttpSession session){
    	this.session = session;
    }
    
    public void setParameter(String name, String[] values){
    	parametersMap.put(name, values);
    }
    
    public void setParameter(String name, String value){
    	parametersMap.put(name, value);
    }
    /**
     * <p>
     * Add a header.
     * </p>
     * @param name the name of header.
     * @param value the value of header.
     */
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * <p>
     * Returns the string value of the given header.
     * </p>
     * @param name the name of header.
     * @return the string value of the given header.
     */
    public String getHeader(String name) {
        return (String) headers.get(name);
    }

    /**
     * <p>
     * Returns an Enumeration instance containing all of request headers.
     * </p>
     * @return an Enumeration instance containing all of request headers.
     */
    public Enumeration getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getAuthType() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Cookie[] getCookies() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0
     * @return null.
     */
    public long getDateHeader(String arg0) {
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public int getIntHeader(String arg0) {
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getMethod() {
        return "get";
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getPathInfo() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getPathTranslated() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getContextPath() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getQueryString() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getRemoteUser() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0
     * @return false.
     */
    public boolean isUserInRole(String arg0) {
        return false;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Principal getUserPrincipal() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getRequestedSessionId() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getRequestURI() {
        return "/tc";
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public StringBuffer getRequestURL() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getServletPath() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public HttpSession getSession(boolean arg0) {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return false.
     */
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return false.
     */
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return false.
     */
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return false.
     */
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public Object getAttribute(String arg0) {
        return attributes.get(arg0);
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Enumeration getAttributeNames() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getCharacterEncoding() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg9.
     * @throws UnsupportedEncodingException when the encoding is not supported.
     */
    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return 0.
     */
    public int getContentLength() {
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getContentType() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     * @throws IOException when any io related problems.
     */
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Enumeration getParameterNames() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Map getParameterMap() {
        return this.parametersMap;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getProtocol() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getScheme() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getServerName() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return 0.
     */
    public int getServerPort() {
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     * @throws IOException when any io related problems.
     */
    public BufferedReader getReader() throws IOException {
        return null;
    }

    /**
     * <p>
     * Always return "127.0.0.1" ip addres.
     * </p>
     * @return "127.0.0.1".
     */
    public String getRemoteAddr() {
        return "127.0.0.1";
    }

    /**
     * <p>
     * Always return "localhost".
     * </p>
     * @return "localhost".
     */
    public String getRemoteHost() {
        return "localhost";
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @param arg1 arg1.
     */
    public void setAttribute(String arg0, Object arg1) {
        this.attributes.put(arg0,arg1);
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     */
    public void removeAttribute(String arg0) {
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Locale getLocale() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public Enumeration getLocales() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return false.
     */
    public boolean isSecure() {
        return false;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public RequestDispatcher getRequestDispatcher(String arg0) {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public String getRealPath(String arg0) {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return 0.
     */
    public int getRemotePort() {
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getLocalName() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return null.
     */
    public String getLocalAddr() {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @return 0.
     */
    public int getLocalPort() {
        return 0;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public Enumeration getHeaders(String arg0) {
        return null;
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public String getParameter(String arg0) {
        return (String) parametersMap.get(arg0);
    }

    /**
     * <p>
     * not implemented.
     * </p>
     * @param arg0 arg0.
     * @return null.
     */
    public String[] getParameterValues(String arg0) {
        Object obj = parametersMap.get(arg0);
        if (obj instanceof String){
            return new String[]{(String)obj};
        }else{
            return (String[])obj;
        }
    }
}