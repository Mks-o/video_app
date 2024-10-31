package main.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import main.application.entity.UserAccount;

public interface UserRepository extends JpaRepository<UserAccount, Long>{

	//@Query(nativeQuery = true, value = "SELECT max(id) FROM USERS") - JDBC exception executing SQL [SELECT max(id) FROM USERS] [Table 'VideoContentDataBase.USERS' doesn't exist] [n/a]
	@Query(value = "select max(id) from UserAccount")
	Long findMaxId();

	Optional<UserAccount> findByLogin(String login);

	
	boolean existsByLogin(String login);
}
