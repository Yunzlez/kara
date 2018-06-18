package be.zlz.openapi.model;

import be.zlz.openapi.model.info.Contact;
import be.zlz.openapi.model.info.License;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Info {

    @NotNull
    private String title;

    private String description;

    private String termsOfService;

    private Contact contact;

    private License license;

    @NotNull
    private String version;
}
