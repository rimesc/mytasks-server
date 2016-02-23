package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.zoneofavoidance.my.tasks.domain.UserRecord;

public interface UserRepository extends JpaRepository<UserRecord, String> {

}
