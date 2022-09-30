package org.zt.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LostItemMapperTest {

    private LostItemMapper lostItemMapper;

    @BeforeEach
    public void setUp() {
        lostItemMapper = new LostItemMapperImpl();
    }
}
