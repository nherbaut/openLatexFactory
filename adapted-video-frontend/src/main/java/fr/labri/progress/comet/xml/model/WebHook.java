package fr.labri.progress.comet.xml.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.IRepositoryIdProvider;

public class WebHook implements IRepositoryIdProvider, Serializable {

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTest_url() {
		return test_url;
	}

	public void setTest_url(String test_url) {
		this.test_url = test_url;
	}

	public String getPing_url() {
		return ping_url;
	}

	public void setPing_url(String ping_url) {
		this.ping_url = ping_url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LastResponse getLast_response() {
		return last_response;
	}

	public void setLast_response(LastResponse last_response) {
		this.last_response = last_response;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String url;
	private String name;
	private String test_url;
	private String ping_url;
	private long id;
	private boolean active;
	private WebhookConfig config;
	private List<String> events = new ArrayList<String>();
	private LastResponse last_response;
	private String updated_at;
	private String created_at;

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events.clear();
		this.events.addAll(events);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 4022408077863155162L;

	@Override
	public String generateId() {

		return null;
	}

	public WebhookConfig getConfig() {
		return config;
	}

	public void setConfig(WebhookConfig config) {
		this.config = config;
	}

}
