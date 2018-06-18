package be.zlz.openapi.model;

import be.zlz.openapi.model.server.Variable;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Server {

    @NotNull
    private String url;

    private String description;

    private Map<String, Variable> variables;
}
