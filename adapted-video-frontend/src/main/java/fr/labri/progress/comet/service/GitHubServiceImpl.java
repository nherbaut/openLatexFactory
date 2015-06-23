package fr.labri.progress.comet.service;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.stereotype.Service;

import fr.labri.progress.comet.exception.GitHubConnectionException;
import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.OAuthRequest;
import fr.labri.progress.comet.repository.GitUserRepository;
import fr.labri.progress.comet.repository.OAuthRequestRepository;

@Service
public class GitHubServiceImpl implements GitHubService {

	@Inject
	GitUserRepository repo;

	@Inject
	OAuthRequestRepository requestRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.labri.progress.comet.service.GitHubService#getGitUser(org.eclipse.
	 * egit.github.core.client.GitHubClient)
	 */
	@Override
	public GitUser createGitUser(OAuthRequest request)
			throws GitHubConnectionException {

		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(request.getAccess_token());

		UserService userService = new UserService(client);

		try {
			User user = userService.getUser();
			GitUser gitUser = new GitUser();

			gitUser.setName(user.getLogin());
			gitUser.setId(UUID.randomUUID().toString());
			gitUser.getRequests().add(request);

			request.setOwner(gitUser);

			repo.save(gitUser);
			requestRepo.save(request); //cascading doesn't work?

			return gitUser;
		} catch (IOException e) {
			throw new GitHubConnectionException(e.getMessage());
		}

	}

}
