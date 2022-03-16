package com.platformsandsolutions.hcpnphiesportal.repository;

import com.platformsandsolutions.hcpnphiesportal.domain.Claim;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Claim entity.
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    @Query(
        value = "select distinct claim from Claim claim left join fetch claim.insurances left join fetch claim.items",
        countQuery = "select count(distinct claim) from Claim claim"
    )
    Page<Claim> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct claim from Claim claim left join fetch claim.insurances left join fetch claim.items")
    List<Claim> findAllWithEagerRelationships();

    @Query("select claim from Claim claim left join fetch claim.insurances left join fetch claim.items where claim.id =:id")
    Optional<Claim> findOneWithEagerRelationships(@Param("id") Long id);
}
