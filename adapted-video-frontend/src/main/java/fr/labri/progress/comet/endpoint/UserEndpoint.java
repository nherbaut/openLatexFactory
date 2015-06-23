package fr.labri.progress.comet.endpoint;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.labri.progress.comet.exception.NoSuchRepositoryException;
import fr.labri.progress.comet.exception.NoSuchUserException;
import fr.labri.progress.comet.service.GitHubService;

@Path("users")
public class UserEndpoint {

	@Inject
	GitHubService githubService;

	@PUT
	@Path("{userId}/{repoId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String changeRepoState(@PathParam("userId") String userId,
			@PathParam("repoId") String repoId, String data) {

		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> result = new ObjectMapper().readValue(data,
					HashMap.class);
			githubService.updateRepoState(userId, repoId,
					Boolean.valueOf(result.get("state").toString()));

			return data;
		} catch (NoSuchRepositoryException e) {
			throw new WebApplicationException(404);
		} catch (IOException e) {

			e.printStackTrace();
			throw new WebApplicationException(500);

		} catch (NoSuchUserException e) {
			throw new WebApplicationException(404);

		}

	}

}
