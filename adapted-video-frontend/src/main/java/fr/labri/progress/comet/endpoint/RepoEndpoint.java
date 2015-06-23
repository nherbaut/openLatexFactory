package fr.labri.progress.comet.endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import fr.labri.progress.comet.exception.NoSuchUserException;
import fr.labri.progress.comet.service.RepositoryService;
import fr.labri.progress.comet.xml.model.Repository;

@Path("repos")
public class RepoEndpoint {

	@Inject
	RepositoryService repoService;

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Repository> getRepos(@PathParam("userId") String userId) {
		try {
			return repoService.getRepoFromUserId(userId);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(404);
		}
	}
}
