package com.safaricom.webservice;

import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import com.safaricom.webservice.bean.LDAPRequest;
import com.safaricom.webservice.bean.ResponseData;
import com.safaricom.webservice.service.LDAPAuthenticationService;

/**
 * A simple Camel REST DSL route that implement the greetings service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {

    	// @formatter:off
        restConfiguration()
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Greeting REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "camel/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
//                .apiProperty("schemes", "")
                .apiContextRouteId("doc-api")
                .enableCORS(true)
            .component("servlet")
            .bindingMode(RestBindingMode.json);
       
        rest("/user/")
        	//swagger
        	.description("Safaricom LDAP API")
        		.produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON)
        		.skipBindingOnErrorCode(false) //Enable json marshalling for body in case of errors
            .post("/auth")
            //swagger
            .description("Authenticate an LDAP user")
            	.type(LDAPRequest.class).description("LDAP Request")
            	.responseMessage().code(200).responseModel(ResponseData.class).endResponseMessage() //OK
            	//.responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage() //Not-OK
            //route
                .route().routeId("auth-api")
                .tracing().log("recieved username : ${body.username}")
                .log("value of conigmap is ${properties:booster.nameToGreet}")
                .to("direct:validateAuth")
        .endRest();

        from("direct:validateAuth").description("Validate Auth by username & password")
            .streamCaching()
            .bean(LDAPAuthenticationService.class,"validateAuthDummy(${body.username},${body.password})");  
        // @formatter:on
    }

}