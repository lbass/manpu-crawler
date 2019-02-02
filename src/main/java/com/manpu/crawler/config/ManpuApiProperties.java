package com.manpu.crawler.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "manpu-api")
public class ManpuApiProperties {

	@JsonProperty("host")
	private String host;
	@JsonProperty("path-push")
	private String pathPush;
	@JsonProperty("secret-key")
	private String secretKey;
	@JsonProperty("request-log")
	private boolean requestLog = false;
	@JsonProperty("push-on")
	private boolean pushOn = false;
	@JsonProperty("protocol")
	private String protocol = "https";
	@JsonProperty("port")
	private String port;


}