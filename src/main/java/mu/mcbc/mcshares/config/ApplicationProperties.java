package mu.mcbc.mcshares.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Backend Mc Share.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {}
