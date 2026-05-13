package com.colvir.ms.sys.rms.generated.web.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@Path("/api/feature-flags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class GeneratorFeatureFlagEndpoint {

    /**
     * {@code GET /feature-flags} : Ресурс для определения списка возможностей, поддерживаемых сервисом.
     *
     * Названия возможностей должны быть в нижнем регистре, с дефисом в качестве разделителя слов и указанием
     * номера версии реализации в конце.
     *
     * @return список кодов возможностей
     */
    @GET
    @Operation(summary = "A resource to discover the service’s available features.")
    @APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response getFeatureFlags() {
        List<String> response = List.of("static-enums-v1", "primitive-lists-v1");
        return Response.ok().entity(response).build();
    }
}
