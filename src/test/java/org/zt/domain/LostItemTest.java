package org.zt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.zt.web.rest.TestUtil;

class LostItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LostItem.class);
        LostItem lostItem1 = new LostItem();
        lostItem1.setId(1L);
        LostItem lostItem2 = new LostItem();
        lostItem2.setId(lostItem1.getId());
        assertThat(lostItem1).isEqualTo(lostItem2);
        lostItem2.setId(2L);
        assertThat(lostItem1).isNotEqualTo(lostItem2);
        lostItem1.setId(null);
        assertThat(lostItem1).isNotEqualTo(lostItem2);
    }
}
