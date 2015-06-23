package fr.labri.progress.comet.service;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.stereotype.Service;

import com.google.common.base.Throwables;

import fr.labri.progress.comet.exception.NoSuchOAuthRequest;
import fr.labri.progress.comet.model.OAuthRequest;
import fr.labri.progress.comet.repository.OAuthRequestRepository;
import fr.labri.progress.comet.xml.model.OAuth;

@Service
public class GitHubOAuthServiceImpl implements GitHubOAuthService {

	@Inject
	OAuthRequestRepository requestRepo;

	@Inject
	FrontEndService frontendService;

	private static Client client = ClientBuilder.newClient();

	private static final String CLIENT_ID = "854fb966cd34d3669e31";
	private static final String CLIENT_SECRET = "bd29e969fda27f49715454920f2c5a9d00691722";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.labri.progress.comet.service.CitHubOAuthService#getOAuthURI(java.lang
	 * .String)
	 */
	@Override
	public URI getOAuthURI(final String getRequestUri,
			final String frontEndCallBackUri) {
		try {
			OAuthRequest request = new OAuthRequest();
			request.setId(UUID.randomUUID().toString());
			request.setCreatedDate(new Date());
			request.setFrontEndCallBackUri(frontEndCallBackUri);

			URI redirectUri = UriBuilder.fromPath(getRequestUri)
					.path("../auth-back").build();

			requestRepo.save(request);
			return UriBuilder
					.fromPath("https://github.com/login/oauth/authorize")
					.queryParam("client_id", CLIENT_ID)
					.queryParam("redirect_uri", redirectUri.toURL().toString())
					.queryParam("state", request.getId())
					.queryParam("scope", "repo,user").build();

		} catch (MalformedURLException e) {
			throw Throwables.propagate(e);
		}

	}

	@Override
	public OAuthRequest receiveOAuthResponse(final String code,
			final String state, final String getRequestUri)
			throws NoSuchOAuthRequest {
		try {
			OAuthRequest request = requestRepo.findOne(state);
			if (request == null) {
				throw new NoSuchOAuthRequest();
			} else {

				URI redirectUri = UriBuilder.fromPath(getRequestUri)
						.path("../auth-access-token").build();

				OAuth oauth = client
						.target("https://github.com/login/oauth/access_token")
						.queryParam("client_id", CLIENT_ID)
						.queryParam("client_secret", CLIENT_SECRET)
						.queryParam("code", code)
						.queryParam("redirect_uri",
								redirectUri.toURL().toString()).request()

						.accept(MediaType.APPLICATION_XML_TYPE)
						.post(Entity.text("boo"), OAuth.class);

				request.setAccess_token(oauth.getAccess_token());
				request.setScope(oauth.getScope());
				request.setToken_type(oauth.getToken_type());
				

				requestRepo.save(request);

				return request;

			}
		} catch (MalformedURLException e) {
			throw Throwables.propagate(e);
		}

	}

}
