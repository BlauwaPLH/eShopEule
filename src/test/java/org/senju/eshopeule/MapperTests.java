package org.senju.eshopeule;

import org.junit.jupiter.api.Test;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.mappers.PermissionMapper;
import org.senju.eshopeule.model.user.Permission;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest
public class MapperTests {

    @MockBean
    private PermissionMapper permissionMapper;

    @Test
    public void convertPermissionDTO_toEntity() {
        PermissionDTO dto = new PermissionDTO("5rv-we5-18", "perm1", "desc1");
        Permission entity = Permission.builder()
                .id("5rv-we5-18")
                .name("perm1")
                .description("desc1")
                .build();
        when(permissionMapper.convertToEntity(dto)).thenReturn(entity);
    }
}
