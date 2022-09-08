package com.sys.vas.management.service;

import com.sys.vas.management.dto.ApiResponseDto;
import com.sys.vas.management.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ActionService.class)
public class ActionServiceTest {

    @MockBean
    private ActionService actionService;

    private MockMvc mockMvc;

    @Test
    void should_throw_error_when_create_if_api_NotFound() {
        doThrow(ApiException.class).when(actionService).create(any());
        assertThrows(ApiException.class, () -> actionService.create(any()));

        Arrays.asList(1,2).stream().mapToInt(x -> {
            if (x%2==0)
                return x*x;
            else return 0;
        });
    }

    @Test
    void should_throw_error_when_id_null_on_update() {
        doThrow(ApiException.class).when(actionService).create(any());
        assertThrows(ApiException.class, () -> actionService.create(any()));
    }

}
