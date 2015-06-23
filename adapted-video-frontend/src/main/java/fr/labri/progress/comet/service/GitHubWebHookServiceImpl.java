package fr.labri.progress.comet.service;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Throwables;
import com.google.gson.reflect.TypeToken;

import fr.labri.progress.comet.exception.GitHubDiscrepencyException;
import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.ManagedRepo;
import fr.labri.progress.comet.repository.GitUserRepository;
import fr.labri.progress.comet.repository.ManagedRepoRepository;
import fr.labri.progress.comet.xml.model.WebHook;
import fr.labri.progress.comet.xml.model.WebhookConfig;
import static org.eclipse.egit.github.core.client.IGitHubConstants.*;

@Service
public class GitHubWebHookServiceImpl extends GitHubWebHookService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GitHubWebHookServiceImpl.class);

	@Inject
	ManagedRepoRepository repoRepo;

	@Inject
	GitUserRepository gitUserRepo;

	public GitHubWebHookServiceImpl() {

		super();
	}

	public static final String EVENTS_SEGMENT = "/events";
	@Inject
	@Named("apiRoot")
	String apiRoot;

	private List<WebHook> getWebHooks(
			org.eclipse.egit.github.core.Repository repo) throws IOException {
		final String webhookUrl = SEGMENT_REPOS + "/"
				+ repo.getOwner().getLogin() + "/" + repo.getName()
				+ SEGMENT_HOOKS;
		GitHubRequest request = new GitHubRequest();
		request.setUri(webhookUrl);
		request.setResponseContentType("application/json");
		request.setType(new TypeToken<List<WebHook>>() {
		}.getType());
		GitHubResponse response = this.client.get(request);
		return (List<WebHook>) response.getBody();
	}

	public void registerNewWebHook(GitUser user,
			org.eclipse.egit.github.core.Repository repo) throws IOException {
		final String webhookUrl = SEGMENT_REPOS + "/"
				+ repo.getOwner().getLogin() + "/" + repo.getName()
				+ SEGMENT_HOOKS;

		WebHook wh = cookWebHook();
		WebHook response = this.client.post(webhookUrl, wh, WebHook.class);

		ManagedRepo mrepo = new ManagedRepo();
		mrepo.setHookActive(true);
		mrepo.setHook_name(wh.getName());
		mrepo.setName(repo.getName());
		mrepo.setHookId(response.getId());
		mrepo.setOwner(user);
		mrepo.setId(repo.getId());
		repoRepo.save(mrepo);

	}

	@Override
	public void unregisterWebHook(org.eclipse.egit.github.core.Repository repo)
			throws IOException {
		final String webhookUrl = SEGMENT_REPOS + "/"
				+ repo.getOwner().getLogin() + "/" + repo.getName()
				+ SEGMENT_HOOKS;

		ManagedRepo mr = repoRepo.findOne(repo.getId());
		if (mr == null) {
			LOGGER.warn("ask to unregister an already existing repo...");
			return;
		}

		GitHubRequest request = new GitHubRequest();
		request.setUri(webhookUrl + "/" + mr.getHookId());
		request.setResponseContentType("application/json");
		request.setType(WebHook.class);
		GitHubResponse response = this.client.get(request);
		WebHook wh = (WebHook) response.getBody();
		wh.setActive(false);

		WebHook finalResponse = this.client.post(webhookUrl, wh, WebHook.class);
		LOGGER.debug("updated webhook {}/{}/hook/{}", repo.getOwner()
				.getLogin(), repo.getName(), wh.getId());

		mr.setHookActive(false);
		repoRepo.save(mr);

	}

	@Override
	public void registerWebHook(GitUser user, Repository repo)
			throws IOException, GitHubDiscrepencyException {
		final String webhookUrl = SEGMENT_REPOS + "/"
				+ repo.getOwner().getLogin() + "/" + repo.getName()
				+ SEGMENT_HOOKS;

		for (ManagedRepo repos : user.getRepos()) {
			if (repos.getId().equals(repo.getId())) {
				// found!

				WebHook wh = cookWebHook();
				wh.setActive(true);

				try {
					this.client.post(webhookUrl, wh, WebHook.class);
				} catch (RequestException e) {
					if (e.getStatus() == 422)
						throw new GitHubDiscrepencyException(repo);
				}
				return;

			}
		}

		LOGGER.info("creating a new webhook for repo {}/{}", repo.getOwner()
				.getLogin(), repo.getName());
		registerNewWebHook(user, repo);

	}

	private WebHook cookWebHook() {
		WebHook wh = new WebHook();

		WebhookConfig config = new WebhookConfig();
		config.setContent_type("json");
		config.setUrl(apiRoot + EVENTS_SEGMENT);
		wh.setName("web");
		wh.setActive(true);
		wh.getEvents().add("push");
		wh.setConfig(config);
		return wh;
	}

	@Override
	public void SyncWebHookDb(Repository repository) {
		try {
			for (WebHook webhook : getWebHooks(repository)) {
				if (webhook.getConfig().getUrl().startsWith(apiRoot)) {
					// maybe that's ours
					ManagedRepo repo = new ManagedRepo();
					repo.setHook_name(webhook.getName());
					repo.setHookActive(webhook.isActive());
					repo.setHookId(webhook.getId());
					repo.setId(repository.getId());
					repo.setName(repository.getName());

					GitUser gitUser = gitUserRepo.findByName(repository
							.getOwner().getLogin());

					repo.setOwner(gitUser);
					gitUserRepo.save(gitUser);

					repoRepo.save(repo);
					return;
				}
			}
		} catch (IOException io) {
			LOGGER.error("failed to read repositories");
			Throwables.propagate(io);
		}

		LOGGER.warn("failed to sync our app and github!");

	}

}
