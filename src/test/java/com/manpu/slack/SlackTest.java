package com.manpu.slack;

import com.manpu.crawler.alert.slack.SlackSendData.ALERT_LEVEL;
import com.manpu.crawler.alert.slack.SlackSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SlackSender.class})
public class SlackTest {

	@Autowired
	private SlackSender slackSender;

	@Test
	public void test() {
		slackSender.sendSlack("memory 사용량 초과", ALERT_LEVEL.DANGER);
	}
}
