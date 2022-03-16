package com.platformsandsolutions.hcpnphiesportal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.platformsandsolutions.hcpnphiesportal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CareTeamSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CareTeamSequence.class);
        CareTeamSequence careTeamSequence1 = new CareTeamSequence();
        careTeamSequence1.setId(1L);
        CareTeamSequence careTeamSequence2 = new CareTeamSequence();
        careTeamSequence2.setId(careTeamSequence1.getId());
        assertThat(careTeamSequence1).isEqualTo(careTeamSequence2);
        careTeamSequence2.setId(2L);
        assertThat(careTeamSequence1).isNotEqualTo(careTeamSequence2);
        careTeamSequence1.setId(null);
        assertThat(careTeamSequence1).isNotEqualTo(careTeamSequence2);
    }
}
