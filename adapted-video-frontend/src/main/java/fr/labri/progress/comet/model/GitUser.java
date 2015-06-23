package fr.labri.progress.comet.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
public class GitUser {

	public List<ManagedRepo> getRepos() {
		return repos;
	}

	public void setRepos(List<ManagedRepo> repos) {
		this.repos = repos;
	}

	@Id
	String id;
	String name;

	public List<OAuthRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<OAuthRequest> requests) {
		this.requests = requests;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;

	}

	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<OAuthRequest> requests = new ArrayList<OAuthRequest>();

	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<ManagedRepo> repos = new ArrayList<ManagedRepo>();

}
