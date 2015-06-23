package fr.labri.progress.comet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.labri.progress.comet.model.OAuthRequest;

public interface OAuthRequestRepository extends
		JpaRepository<OAuthRequest, String> {

}
