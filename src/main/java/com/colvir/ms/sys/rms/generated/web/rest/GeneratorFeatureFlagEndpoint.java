package com.colvir.ms.sys.rms.generated.web.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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
    public Response getFeatureFlags() {
        List<String> response = List.of("static-enums-v1", "primitive-lists-v1");
        return Response.ok().entity(response).build();
    }
}
