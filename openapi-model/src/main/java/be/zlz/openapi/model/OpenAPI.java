package be.zlz.openapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class OpenAPI {

    private static final String openapi = "3.0.0";

    @NotNull
    private Info info;

    private List<Server> server;

    @NotNull
    private Paths paths;

    private Components components;

    private List<SecurityRequirements> security;

    private List<Tag> tags;

    private ExternalDocs externalDocs;
}
