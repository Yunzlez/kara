package be.zlz.openapi.model.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class License {

    @NotNull
    private String name;

    @URL
    private String url;
}
