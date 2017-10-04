package br.uff.labtempo.osiris.function.average.configuration;

/**
 * Centralize application global configurations
 * @author andre.ghigo
 * @version 1.0
 * @since 1.8
 */
public class AppConfig {
    public static final String PROPERTIES_FILENAME = "config.properties";
    public static final String RABBITMQ_IP_PROPERTIES_KEY = "rabbitmq.server.ip";
    public static final String RABBITMQ_USERNAME_PROPERTIES_KEY = "rabbitmq.server.user";
    public static final String RABBITMQ_PASSWORD_PROPERTIES_KEY = "rabbitmq.server.pass";
    public static final String DEFAULT_RABBITMQ_IP = "172.17.0.2";
    public static final String DEFAULT_RABBITMQ_USERNAME = "guest";
    public static final String DEFAULT_RABBITMQ_PASSWORD = "guest";
    public static final String FUNCTION_NAME = "average";
    public static final String FUNCTION_DESCRIPTION = "average of all values";
    public static final String MODULE_NAME = "average.function";
    public static final String REQUEST_PARAM = "input";
    public static final String RESPONSE_PARAM = "output";
    public static final String PROTOCOL_VERSION = "1.0";
}
