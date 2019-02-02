package com.manpu.crawler.helper;

import com.manpu.crawler.config.ManpuApiProperties;
import com.manpu.crawler.metric.CrawlerMetric;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestHelper {

	@Autowired
	private CrawlerMetric crawlerMetric;

	@Autowired
	private ManpuApiProperties manpuApiProperties;

	protected static final Logger logger = LoggerFactory.getLogger(RestHelper.class);

	public void pushWebtoonInfomation(String webtoonId, String site, String webtoonTitle, String webtoonEpisodeTitle) {
		if(!manpuApiProperties.isPushOn()) {
			logger.debug("push flag off - push pass");
			return;
		}
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(10000);
		factory.setConnectTimeout(10000);

		String port = StringUtils.isEmpty(manpuApiProperties.getPort()) ? null : manpuApiProperties.getPort();

		URI uri = UriComponentsBuilder.newInstance().scheme(manpuApiProperties.getProtocol())
				.host(manpuApiProperties.getHost())
				.port(port)
				.path(manpuApiProperties.getPathPush() + webtoonId)
				.build().toUri();

		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("secret", manpuApiProperties.getSecretKey());

		PushParameter param = new PushParameter();
		param.setTitle(String.format("[%s] %s 업데이트", site , webtoonTitle));
		param.setMessage(webtoonEpisodeTitle);
		HttpEntity<PushParameter> httpRequest = new HttpEntity<>(param, headers);
		logger.info("request : {}", httpRequest.getBody());
		StopWatch timer = StopWatch.createStarted();
		crawlerMetric.addPushApiCount();
		try {
			ResponseEntity<String> body = restTemplate.exchange(uri, HttpMethod.POST, httpRequest, String.class);
			timer.stop();
			final long takeTime = timer.getTime();
			logger.info("response  : {}, {}", takeTime, body);
			crawlerMetric.addPushApiTotalLatency(takeTime);
		} catch (Exception e) {
			logger.error("response  : {}", e.getMessage());
		}
	}

	@Data
	public static class PushParameter {
		private String title;
		private String message;
	}

	class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			if(manpuApiProperties.isRequestLog()) {
				traceRequest(request, body);
			}
			ClientHttpResponse response = execution.execute(request, body);
			return response;
		}

		private void traceRequest(HttpRequest request, byte[] body) throws IOException {
			logger.info("===========================request begin================================================");
			logger.info("URI         : {}", request.getURI());
			logger.info("Method      : {}", request.getMethod());
			logger.info("Headers     : {}", request.getHeaders() );
			logger.info("Request body: {}", new String(body, "UTF-8"));
			logger.info("==========================request end================================================");
		}
	}
}
