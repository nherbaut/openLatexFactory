package fr.labri.progress.comet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.egit.github.core.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.OAuthRequest;
import fr.labri.progress.comet.repository.GitUserRepository;

@Service
public class RepositoryServiceImpl implements
		fr.labri.progress.comet.service.RepositoryService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RepositoryServiceImpl.class);

	@Inject
	GitUserRepository userRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.labri.progress.comet.service.RepositoryService#getRepoFromUserId(java
	 * .lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<fr.labri.progress.comet.xml.model.Repository> getRepoFromUserId(
			final String userId) {

		try {
			List<fr.labri.progress.comet.xml.model.Repository> res = new ArrayList<fr.labri.progress.comet.xml.model.Repository>();

			org.eclipse.egit.github.core.service.RepositoryService repoService = new org.eclipse.egit.github.core.service.RepositoryService();
			GitUser user = userRepo.findOne(userId);
			for (OAuthRequest request : user.getRequests()) {
				repoService.getClient().setOAuth2Token(
						request.getAccess_token());
				for (Repository repos : repoService.getRepositories()) {
					fr.labri.progress.comet.xml.model.Repository repo = new fr.labri.progress.comet.xml.model.Repository();

					repo.from(repos);
					res.add(repo);

				}

			}

			return res;
		} catch (IOException e) {
			LOGGER.warn("failed to recover repository");
			return Collections.EMPTY_LIST;
		}

	}

}
