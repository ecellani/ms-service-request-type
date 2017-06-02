package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.User;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

/**
 * Created by ecellani on 01/06/17.
 */
@Component
public class UserRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "0.0.1-SNAPSHOT")
                .apiProperty("cors", "true");

//        rest("/users").description("User Rest Service")
//            .consumes("application/json")
//            .produces("application/json")
//
//        .get().description("Find all users").outTypeList(User.class)
//            .responseMessage().code(OK.value()).message("All users successfully returned").endResponseMessage()
//            .to("bean:userService?method=findUsers")
//
//        .get("/{id}").description("Find user by ID").outType(User.class)
//            .param().name("id").type(path).dataType("integer").description("The ID of the user").endParam()
//            .responseMessage().code(OK.value()).message("The user successfully returned").endResponseMessage()
//            .to("bean:userService?method=findUser(${header.id})")
//
//        .put().description("Update a user").type(User.class)
//            .param().name("body").type(body).description("The user to update").endParam()
//            .responseMessage().code(NO_CONTENT.value()).message("User successfully updated").endResponseMessage()
//            .to("bean:userService?method=updateUser")
//        ;
    }
}
