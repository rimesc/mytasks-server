package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import uk.co.zoneofavoidance.my.tasks.domain.UserRecord;

/**
 * Repository for accessing user records from the 'users' table used by
 * {@link JdbcUserDetailsManager}.
 */
public interface UserRepository extends JpaRepository<UserRecord, String> {

}
