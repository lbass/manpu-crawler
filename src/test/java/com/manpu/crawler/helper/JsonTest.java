package com.manpu.crawler.helper;

import com.manpu.crawler.worker.PostProcessWorkerTest;
import com.manpu.crawler.worker.crawler.lezhin.v1.model.LezhinDetailRoot;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTest {

	private Logger logger = LoggerFactory.getLogger(PostProcessWorkerTest.class);

	private final String PRODUCT = "product";
	private final String testString = "product product asdsadasdasdasd product";

	@Test
	public void test() {
		// LezhinDetailRoot root = JacksonJsonHelper.convertToObject(LezhinDetailRoot.class, testString);
		logger.info("{}", testString.replaceFirst(PRODUCT, "\""+ PRODUCT + "\""));
		logger.info("{}", testString.replace(PRODUCT, "\""+ PRODUCT + "\""));
	}

}
