package fr.labri.progress.comet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.labri.progress.comet.model.GitUser;
import fr.labri.progress.comet.model.ManagedRepo;

public interface ManagedRepoRepository extends
		JpaRepository<ManagedRepo, Long> {

}
