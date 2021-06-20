package mu.mcbc.mcshares.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mu.mcbc.mcshares.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SharesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shares.class);
        Shares shares1 = new Shares();
        shares1.setId(1L);
        Shares shares2 = new Shares();
        shares2.setId(shares1.getId());
        assertThat(shares1).isEqualTo(shares2);
        shares2.setId(2L);
        assertThat(shares1).isNotEqualTo(shares2);
        shares1.setId(null);
        assertThat(shares1).isNotEqualTo(shares2);
    }
}
