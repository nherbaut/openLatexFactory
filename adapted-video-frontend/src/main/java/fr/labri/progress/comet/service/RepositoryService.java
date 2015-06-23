package fr.labri.progress.comet.service;

import java.util.List;

import fr.labri.progress.comet.xml.model.Repository;

public interface RepositoryService {

	public abstract List<fr.labri.progress.comet.xml.model.Repository> getRepoFromUserId(
			String userId);

}