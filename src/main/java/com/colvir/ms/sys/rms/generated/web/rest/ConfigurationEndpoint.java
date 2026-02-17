package com.colvir.ms.sys.rms.generated.web.rest;

import com.colvir.ms.generated.svc.base.web.rest.vm.EnvVM;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("/management")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ConfigurationEndpoint {

    @GET
    @Path("/env")
    public EnvVM getEnvs() {
        Iterable<ConfigSource> configSources = ConfigProvider.getConfig().getConfigSources();
        List<EnvVM.PropertySource> propertySources = StreamSupport
            .stream(configSources.spliterator(), false)
            .map(configSource -> new EnvVM.PropertySource(configSource.getName(), configSource.getProperties()))
            .collect(Collectors.toList());

        return new EnvVM(ConfigUtils.getProfiles(), propertySources);
    }
}
