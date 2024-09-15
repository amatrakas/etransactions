package com.agile.etransactions.controllers;


import com.agile.etransactions.models.AccountResponseDTO;
import com.agile.etransactions.models.CreateAccountRequestDTO;
import com.agile.etransactions.services.AccountService;
import com.agile.etransactions.validators.AccountValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;



import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private static MockMvc mockMvc;

    @Mock
    private AccountValidator accountValidator;

    @InjectMocks
    private AccountController accountController;


    @Mock
    private AccountService accountService;

    @BeforeClass
    public static void init() {
        org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.getInstance();

    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void createAccountTest() throws Exception {

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setMessage("test");
        accountResponseDTO.setBalance(new BigDecimal("50"));
        accountResponseDTO.setCurrency("GBP");
        accountResponseDTO.setIban("GR7777777777777334777777777");
        CreateAccountRequestDTO requestDTO = new CreateAccountRequestDTO();
        requestDTO.setBalance(new BigDecimal("50"));
        requestDTO.setCurrency("GBP");
        requestDTO.setIban("GR7777777777777334777777777");

        String url = "/api/account/create";

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "createAccountRequestDTO");

        accountValidator.validate(requestDTO, bindingResult);

        when(accountService.createAccount(requestDTO)).thenReturn(accountResponseDTO);

        ResultActions response = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        verify(accountValidator, times(1)).validate(eq(requestDTO), any(BindingResult.class));

        response.andExpect(status().isOk());
    }

    @Test
    public void createAccountExceptionCaseTest() throws Exception {

        CreateAccountRequestDTO requestDTO = new CreateAccountRequestDTO();
        requestDTO.setBalance(new BigDecimal("50"));
        requestDTO.setCurrency("GBP");
        requestDTO.setIban("GR7777777777777334777777777");

        String url = "/api/account/create";

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "createAccountRequestDTO");

        accountValidator.validate(requestDTO, bindingResult);

        when(accountService.createAccount(any(CreateAccountRequestDTO.class)))
                .thenThrow(new Exception("Test exception"));

        ResultActions response = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andDo(print()
        );

        verify(accountValidator, times(1)).validate(eq(requestDTO), any(BindingResult.class));

        response.andExpect(status().isInternalServerError());

    }


}
