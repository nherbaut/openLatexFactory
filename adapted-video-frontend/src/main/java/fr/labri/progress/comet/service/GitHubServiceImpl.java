package fr.labri.progress.comet.service;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.labri.progress.comet.exception.GitHubConnectionException;
import fr.labri.progress.comet.exception.GitHubDiscrepencyException;
import fr.labri.progress.comet.exception.NoSuchRepositoryException;
import fr.labri.progress.comet.exception.NoSuchUserException;
import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.OAuthRequest;
import fr.labri.progress.comet.repository.GitUserRepository;
import fr.labri.progress.comet.repository.OAuthRequestRepository;
import fr.labri.progress.comet.xml.model.OAuth;

@Service
public class GitHubServiceImpl implements GitHubService {

	@Inject
	GitUserRepository gitUserRepo;

	@Inject
	OAuthRequestRepository requestRepo;

	@Inject
	GitHubWebHookService webHookService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GitHubServiceImpl.class);

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

			gitUserRepo.save(gitUser);
			requestRepo.save(request); // cascading doesn't work?

			return gitUser;
		} catch (IOException e) {
			throw new GitHubConnectionException(e.getMessage());
		}

	}

	@Override
	public boolean updateRepoState(String userId, String repoId, boolean state)
			throws NoSuchRepositoryException, NoSuchUserException {
		GitUser gitUser = gitUserRepo.findOne(userId);
		if (gitUser == null) {
			throw new NoSuchUserException();
		}
		Long repoIdInt = Long.valueOf(repoId);
		for (OAuthRequest oauth : gitUser.getRequests()) {

			RepositoryService repoService = new RepositoryService();
			repoService.getClient().setOAuth2Token(oauth.getAccess_token());
			Repository winnerRepo = null;
			try {
				for (Repository repo : repoService.getRepositories()) {
					if (repoIdInt.equals(repo.getId())) {

						webHookService.getClient().setOAuth2Token(
								oauth.getAccess_token());
						if (!state) {
							webHookService.unregisterWebHook(repo);
						} else {
							webHookService.registerWebHook(gitUser, repo);
						}
						return state;
					}
				}
			} catch (GitHubDiscrepencyException gd) {

				webHookService.SyncWebHookDb(gd.getRepo());

			} catch (IOException ieo) {
				ieo.printStackTrace();
				LOGGER.warn("failed to get r epos", ieo);
				throw new NoSuchRepositoryException(repoId);
			}
		}

		throw new NoSuchRepositoryException(repoId);

	}
}
