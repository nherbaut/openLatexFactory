package fr.labri.progress.comet.exception;

public class NoSuchRepositoryException extends Exception {

	public NoSuchRepositoryException(String repoId) {
		super("unknow repo " + repoId);
	}

}
