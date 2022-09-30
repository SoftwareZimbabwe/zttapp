package org.zt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.zt.web.rest.TestUtil;

class LostItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LostItemDTO.class);
        LostItemDTO lostItemDTO1 = new LostItemDTO();
        lostItemDTO1.setId(1L);
        LostItemDTO lostItemDTO2 = new LostItemDTO();
        assertThat(lostItemDTO1).isNotEqualTo(lostItemDTO2);
        lostItemDTO2.setId(lostItemDTO1.getId());
        assertThat(lostItemDTO1).isEqualTo(lostItemDTO2);
        lostItemDTO2.setId(2L);
        assertThat(lostItemDTO1).isNotEqualTo(lostItemDTO2);
        lostItemDTO1.setId(null);
        assertThat(lostItemDTO1).isNotEqualTo(lostItemDTO2);
    }
}
