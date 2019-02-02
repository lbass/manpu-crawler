package com.manpu.crawler.alert.slack;

import com.manpu.crawler.alert.slack.SlackSendData.ALERT_LEVEL;
import com.manpu.crawler.alert.slack.SlackSendData.Attachment;
import com.manpu.crawler.helper.JacksonJsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;

@Component
@Slf4j
public class SlackSender {

	private static Logger logger = LoggerFactory.getLogger(SlackSender.class);
	private final static String WebHookUrl = "https://hooks.slack.com/services/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";

	@Value("${crawler-alert-noti-on}")
	private boolean isNotifying;
	
	public String sendSlack(String message, ALERT_LEVEL level) {
		String result = null;
		if(!isNotifying) {
			log.warn("Alert notification setting(crawler-alert-noti-on) is off..!");
			return result;
		}
		try {
			SlackSendData sendData = new SlackSendData();
			Attachment atta = new Attachment();
			atta.setText(message);
			atta.setColor(level.getColor());

			sendData.setAttachments(newArrayList(atta));

			String jsonString = JacksonJsonHelper.writeValueAsString(sendData);
			logger.debug("[Send Slack]");
			logger.debug("Send Data : " + jsonString);

			result = Jsoup.connect(WebHookUrl)
					.ignoreContentType(true)
					.requestBody(jsonString)
					.post()
					.body()
					.toString();

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
}