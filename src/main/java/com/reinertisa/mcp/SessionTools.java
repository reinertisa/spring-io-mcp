package com.reinertisa.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.asm.TypeReference;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class SessionTools {

    private static final Logger log = Logger.getLogger(SessionTools.class.getName());
    private List<Session> sessions = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public SessionTools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Tool(name = "spring-io-sessions", description = "Load sessions from Spring I/O 2025")
    public List<Session> findAllSessions() {
        return sessions;
    }

    @PostConstruct
    private void init() {
        log.info("Loading sessions from Spring I/O 2025");
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/sessions.json")) {
            var conference = objectMapper.readValue(inputStream, Conference.class);
            this.sessions = conference.sessions();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read sessions from JSON");
        }
    }
}
