package fr.labri.progress.comet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.labri.progress.comet.model.GitUser;

public interface GitUserRepository extends JpaRepository<GitUser, String> {

}
