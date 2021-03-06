package com.tui.proof.application.controller;

import com.tui.proof.domain.api.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class SearchOrdersSecurityIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void whenUnauthorizedSearchingOrders_thenHttpResponse401() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/order/v1_0/search"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("admin")
    public void whenAuthorizedSearchingOrders_thenHttpResponse200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/order/v1_0/search"))
                .andExpect(status().isOk());
    }

}
