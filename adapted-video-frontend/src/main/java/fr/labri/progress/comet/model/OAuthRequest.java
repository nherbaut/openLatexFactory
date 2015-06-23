package fr.labri.progress.comet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;

/**
 * Entity implementation class for Entity: OAuthRequest
 *
 */
@Entity
public class OAuthRequest implements Serializable {

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private Date createdDate;
	private String token_type;
	private String scope;
	private String access_token;
	private String frontEndCallBackUri;

	
	@ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name = "OWNER_ID",referencedColumnName="id")
	private GitUser owner;

	public GitUser getOwner() {
		return owner;
	}

	public void setOwner(GitUser owner) {
		this.owner = owner;
	}

	public String getFrontEndCallBackUri() {
		return frontEndCallBackUri;
	}

	public OAuthRequest() {
		super();
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFrontEndCallBackUri(String frontEndCallBackUri) {
		this.frontEndCallBackUri = frontEndCallBackUri;

	}

}
