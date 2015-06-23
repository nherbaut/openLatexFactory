package fr.labri.progress.comet.service;

import java.net.URI;

import fr.labri.progress.comet.exception.NoSuchOAuthRequest;
import fr.labri.progress.comet.model.OAuthRequest;
import fr.labri.progress.comet.xml.model.OAuth;

public interface GitHubOAuthService {

	public abstract URI getOAuthURI(final String getRequestUri,
			final String frontEndCallBackUri);

	public abstract OAuthRequest receiveOAuthResponse(final String code,
			final String state, final String getRequestUri)
			throws NoSuchOAuthRequest;

}