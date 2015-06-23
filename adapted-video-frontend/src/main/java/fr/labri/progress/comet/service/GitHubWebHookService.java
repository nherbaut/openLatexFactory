package fr.labri.progress.comet.service;

import java.io.IOException;

import org.eclipse.egit.github.core.Repository;

import fr.labri.progress.comet.exception.GitHubDiscrepencyException;
import fr.labri.progress.comet.model.GitUser;

public abstract class GitHubWebHookService extends
		org.eclipse.egit.github.core.service.GitHubService {

	public abstract void registerWebHook(GitUser user,
			org.eclipse.egit.github.core.Repository repo) throws IOException,
			GitHubDiscrepencyException;

	public abstract void unregisterWebHook(
			org.eclipse.egit.github.core.Repository repo) throws IOException;

	public abstract void SyncWebHookDb(Repository repository);
			

}