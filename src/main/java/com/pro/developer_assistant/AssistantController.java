package com.pro.developer_assistant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistant")
public class AssistantController {
    private final ChatClient chatClient;

    public AssistantController(ChatClient.Builder builder, ProjectTools projectTools, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultTools(projectTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

    }

    public String chat(@RequestParam String message,
            @RequestParam(defaultValue = "default-session") String conversationId) {
        return chatClient.prompt()

                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
