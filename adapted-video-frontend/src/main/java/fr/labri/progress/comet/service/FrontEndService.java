package fr.labri.progress.comet.service;

import java.net.URI;

import fr.labri.progress.comet.model.GitUser;

public interface FrontEndService {

	public abstract URI getSuccessFrontendURI(GitUser user,
			String callBackURI);

}