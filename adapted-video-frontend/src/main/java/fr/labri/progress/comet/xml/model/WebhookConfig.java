package fr.labri.progress.comet.xml.model;

public class WebhookConfig {
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	private String url;
	private String content_type;
	private Integer insecure_ssl;
	private String secret;

}
