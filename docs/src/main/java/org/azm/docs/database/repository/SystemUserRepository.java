package org.azm.docs.database.repository;

import org.azm.docs.database.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {

    SystemUser getByUsername(String username);

}
