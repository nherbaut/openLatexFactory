package fr.labri.progress.comet.endpoint;

import java.net.URI;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.HttpContext;

import fr.labri.progress.comet.exception.GitHubConnectionException;
import fr.labri.progress.comet.exception.NoSuchOAuthRequest;
import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.OAuthRequest;
import fr.labri.progress.comet.service.FrontEndService;
import fr.labri.progress.comet.service.GitHubOAuthService;
import fr.labri.progress.comet.service.GitHubService;
import fr.labri.progress.comet.xml.model.OAuth;

@Path("github")
public class AuthEndpoint {

	@Inject
	GitHubOAuthService oauthService;

	@Inject
	GitHubService gitHubService;

	@Inject
	FrontEndService frontendService;

	@Path("auth")
	@GET
	public Response SignUpGitHub(
			@QueryParam("callback_uri") String frontEndCallBackUri,
			@Context HttpServletRequest request) {

		return Response.seeOther(
				oauthService.getOAuthURI(request.getRequestURL().toString(),
						frontEndCallBackUri)).build();
	}

	@Path("auth-back")
	@GET
	public Response ReceiveGitHutAuthorize(@QueryParam("code") String code,
			@QueryParam("state") String state,
			@Context HttpServletRequest request) {

		try {
			OAuthRequest oauthRequest = oauthService.receiveOAuthResponse(code,
					state, request.getRequestURL().toString());
			GitUser user = gitHubService.createGitUser(oauthRequest);
			URI nextLocation = frontendService.getSuccessFrontendURI(user,
					oauthRequest.getFrontEndCallBackUri());

			return Response.seeOther(nextLocation).build();
		} catch (NoSuchOAuthRequest e) {
			throw new WebApplicationException(403);
		} catch (GitHubConnectionException e) {
			throw new WebApplicationException(403);
		}

	}
}
