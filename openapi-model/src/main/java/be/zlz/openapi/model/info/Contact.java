package be.zlz.openapi.model.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact {

    private String name;

    @URL
    private String url;

    @Email
    private String email;
}
