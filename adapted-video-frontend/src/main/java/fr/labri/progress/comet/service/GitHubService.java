package fr.labri.progress.comet.service;

import fr.labri.progress.comet.exception.GitHubConnectionException;
import fr.labri.progress.comet.exception.NoSuchRepositoryException;
import fr.labri.progress.comet.exception.NoSuchUserException;
import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.OAuthRequest;

public interface GitHubService {

	public abstract GitUser createGitUser(OAuthRequest request)
			throws GitHubConnectionException;

	public abstract boolean updateRepoState(String userId, String repoId,
			boolean state) throws NoSuchRepositoryException, NoSuchUserException;

}