package fr.labri.progress.comet.xml.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Repository {

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private long id;
	private String name;
	private boolean active;

	public void from(org.eclipse.egit.github.core.Repository repos) {
		this.id = repos.getId();
		this.name = repos.getName();

	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
