package fr.labri.progress.comet.service;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.stereotype.Service;

import fr.labri.progress.comet.model.GitUser;

@Service
public class FrontEndServiceImpl implements FrontEndService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.labri.progress.comet.service.FrontEndService#getSuccessFrontendURI
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public URI getSuccessFrontendURI(GitUser user, String callBackURI) {

		return UriBuilder.fromPath(callBackURI)

		.queryParam("user_name", user.getName()).queryParam("user_id", user.getId())

		.build();

	}
}
