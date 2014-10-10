package br.com.altamira.data.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author alessandro.holanda
 */
@ApplicationPath("/rest")
public class WebApplication extends Application {
	
    /**
     *
     */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "http://localhost:8100";
	
}