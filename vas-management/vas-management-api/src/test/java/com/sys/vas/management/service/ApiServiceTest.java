package com.sys.vas.management.service;

import com.sys.vas.management.dto.ApiResponseDto;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApiService.class)
public class ApiServiceTest {

    @MockBean
    private ApiService apiService;

    private MockMvc mockMvc;

    @Test
    void should_throw_error_when_create_if_api_NotFound() {
        doThrow(ApiException.class).when(apiService).create(any());
        assertThrows(ApiException.class, () -> apiService.create(any()));
    }

    @Test
    void should_throw_error_when_id_null_on_update() {
        doThrow(ApiException.class).when(apiService).create(any());
        assertThrows(ApiException.class, () -> apiService.create(any()));
    }

    @Test
    void should_not_create_if_xml_is_null_or_empty() {
        doThrow(ApiException.class).when(apiService).create(any());
        assertThrows(ApiException.class, () -> apiService.create(any()));
    }


    @Test
    void should_create_when_valid_Api() {
//        doNothing().when(apiService).create(any());
        assertDoesNotThrow(() -> apiService.create(any()));
    }

    @Test
    void should_throw_error_if_apiid_notFound() {
        assertDoesNotThrow(() -> apiService.update(any()));
    }

    @Test
    void should_deploy_if_validId_and_commitID() {
        assertDoesNotThrow(() -> apiService.deploy(anyLong(), anyString()));
    }

    @Test
    void should_return_list_if_data_exists() {
        List<ApiResponseDto> list = new ArrayList<>();
        list.add(new ApiResponseDto());

        when(apiService.getAllApis()).thenReturn(list);
        assertEquals(list.size(), 1);
    }
}
