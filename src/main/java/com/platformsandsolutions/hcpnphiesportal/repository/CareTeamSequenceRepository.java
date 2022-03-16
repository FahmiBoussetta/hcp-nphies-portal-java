package com.platformsandsolutions.hcpnphiesportal.repository;

import com.platformsandsolutions.hcpnphiesportal.domain.CareTeamSequence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CareTeamSequence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CareTeamSequenceRepository extends JpaRepository<CareTeamSequence, Long> {}
