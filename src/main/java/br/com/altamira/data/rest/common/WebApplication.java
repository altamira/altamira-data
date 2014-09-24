package br.com.altamira.data.rest.common;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class WebApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return super.getClasses();
	}

}