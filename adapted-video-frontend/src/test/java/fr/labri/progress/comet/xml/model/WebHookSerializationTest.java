package fr.labri.progress.comet.xml.model;

import org.junit.Test;

import com.google.gson.Gson;

public class WebHookSerializationTest {

	@Test
	public void doall() {

		WebhookConfig config = new WebhookConfig();
		config.setContent_type("json");
		config.setUrl("http://labri.fr");
		WebHook wh = new WebHook();
		wh.setActive(true);
		wh.getEvents().add("push");
		wh.setConfig(config);

		Gson gson = new Gson();
		
		System.out.println(gson.toJson(wh));
	}

}
