package fr.labri.progress.comet.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ManagedRepo {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHookname() {
		return hookName;
	}

	public void setHook_name(String hookName) {
		this.hookName = hookName;
	}

	String name;
	@Id
	Long id;
	String hookName;
	
	
	Boolean hookActive = false;

	public String getHookName() {
		return hookName;
	}

	public void setHookName(String hookName) {
		this.hookName = hookName;
	}

	public Boolean getHookActive() {
		return hookActive;
	}

	public void setHookActive(Boolean hookActive) {
		this.hookActive = hookActive;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "OWNER_ID", referencedColumnName = "id")
	private GitUser owner;
	private Long hookId;

	public GitUser getOwner() {
		return owner;
	}

	public void setOwner(GitUser owner) {
		this.owner = owner;
	}

	public Long getHookId() {
		return hookId;
	}

	public void setHookId(Long id) {
		this.hookId = id;

	}

}
