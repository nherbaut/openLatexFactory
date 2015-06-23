package fr.labri.progress.comet.exception;

import org.eclipse.egit.github.core.Repository;

public class GitHubDiscrepencyException extends Exception {

	private Repository repo;

	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

	public GitHubDiscrepencyException(Repository repo) {
		this.repo = repo;
	}

}
