package com.lvr.babab.babab.entities.users;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessAccountRepository extends JpaRepository<BusinessUser, UUID> {
  Optional<BusinessUser> findByKvkNumber(String kvkNumber);

  Optional<BusinessUser> findByCompanyNameIgnoreCase(String companyName);
}
