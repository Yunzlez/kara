package be.zlz.openapi.model.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Variable {

    @JsonProperty(value = "enum")
    private String enumField;

    @NotNull
    @JsonProperty(value = "default")
    private String defaultSub;

    private String description;
}
