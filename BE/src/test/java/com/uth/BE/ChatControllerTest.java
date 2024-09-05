package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.ChatController;
import com.uth.BE.Entity.model.MessageType;
import com.uth.BE.Service.ChatService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.ChatMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class ChatControllerTest {

    @MockBean
    private MyUserDetailService myUserDetailService;


    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void getMessagesBetweenUsers_ShouldReturnMessages_WhenMessagesExist() throws Exception {
        List<ChatMessageDTO> messages = Arrays.asList(
                new ChatMessageDTO(1, 1, 2, "Hello, how are you?", MessageType.TEXT, LocalDateTime.of(2024, 8, 22, 13, 39, 20)),
                new ChatMessageDTO(2, 2, 1, "I am good, thanks! How about you?", MessageType.TEXT, LocalDateTime.of(2024, 8, 22, 13, 39, 20))
        );

        when(chatService.getMessagesBetweenUsers(1, 2)).thenReturn(messages);

        mockMvc.perform(get("/chat/between/1/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageId").value(1))
                .andExpect(jsonPath("$[0].sender").value(1))
                .andExpect(jsonPath("$[0].recipient").value(2))
                .andExpect(jsonPath("$[0].content").value("Hello, how are you?"))
                .andExpect(jsonPath("$[0].messageType").value("TEXT"))
                .andExpect(jsonPath("$[1].messageId").value(2))
                .andExpect(jsonPath("$[1].sender").value(2))
                .andExpect(jsonPath("$[1].recipient").value(1))
                .andExpect(jsonPath("$[1].content").value("I am good, thanks! How about you?"))
                .andDo(print());
    }

}
