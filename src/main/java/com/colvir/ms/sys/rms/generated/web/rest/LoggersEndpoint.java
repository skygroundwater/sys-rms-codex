package com.colvir.ms.sys.rms.generated.web.rest;

import com.colvir.ms.generated.svc.base.web.rest.vm.LoggerVM;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;


@Path("/management/loggers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class LoggersEndpoint {

    /**
     * Return a list of all loggers and their levels.
     *
     * @return a map of logger names to their corresponding levels
     */
    @GET
    @Operation(
        summary = "Get all loggers",
        description = "Returns a list of all loggers in the application with their current log levels."
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully retrieved logger configurations",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = LoggersWrapper.class)
        )
    )
    public LoggersWrapper getLoggers() {
        Enumeration<String> loggerNames = LogManager.getLogManager().getLoggerNames();

        Map<String, LoggerVM> loggers = Collections
            .list(loggerNames)
            .stream()
            .filter(name -> !name.isBlank())
            .map(this::getLogger)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(logger -> logger.name, Function.identity()));

        return new LoggersWrapper(loggers);
    }

    private LoggerVM getLogger(String loggerName) {
        return Optional.ofNullable(Logger.getLogger(loggerName)).map(logger -> new LoggerVM(loggerName, logger)).orElse(null);
    }

    /**
     * Updates the level of the logger with the given name.
     *
     * @param name the name of the logger to be updated
     * @param loggerVM the loggerVM containing the name and the configured level
     * @return a response indicating the success or failure of the update
     */
    @POST
    @Path("/{name}")
    @Operation(
        summary = "Update logger level",
        description = "Updates the log level for the specified logger. The logger will be created if it doesn't exist."
    )
    @APIResponse(
        responseCode = "200",
        description = "Logger level updated successfully"
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid log level provided"
    )
    @APIResponse(
        responseCode = "500",
        description = "Internal server error while updating logger level"
    )
    public Response updateLoggerLevel(@PathParam("name") String name, LoggerVM loggerVM) {
        Logger logger = Logger.getLogger(name);

        if (logger == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Level level = Level.parse(loggerVM.configuredLevel);
        logger.setLevel(level);

        return Response.ok().build();
    }

    @RegisterForReflection
    public static class LoggersWrapper {

        public final Map<String, LoggerVM> loggers;

        public LoggersWrapper(Map<String, LoggerVM> loggers) {
            this.loggers = loggers;
        }
    }
}
