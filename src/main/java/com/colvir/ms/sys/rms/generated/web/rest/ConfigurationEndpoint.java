package com.colvir.ms.sys.rms.generated.web.rest;

import com.colvir.ms.generated.svc.base.web.rest.vm.EnvVM;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;

@Path("/management")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ConfigurationEndpoint {

    /**
     * Get the configuration sources.
     *
     * This endpoint returns a map of configuration sources with their
     * associated properties.
     *
     * @return a map of configuration sources with their associated properties
     */
    @GET
    @Path("/env")
    @Operation(
        summary = "Get all configuration properties",
        description = "Retrieves all configuration properties from the application's configuration sources."
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully retrieved configuration properties",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                type = SchemaType.OBJECT,
                description = "Map of configuration property sources to their properties"
            )
        )
    )
    public EnvVM getEnvs() {
        Iterable<ConfigSource> configSources = ConfigProvider.getConfig().getConfigSources();
        List<EnvVM.PropertySource> propertySources = StreamSupport
            .stream(configSources.spliterator(), false)
            .map(configSource -> new EnvVM.PropertySource(configSource.getName(), configSource.getProperties()))
            .collect(Collectors.toList());

        return new EnvVM(ConfigUtils.getProfiles(), propertySources);
    }
}
