package vn.tima.ai.gateway.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("test", Arrays.asList("auth1", "auth2"),  1f);
        System.out.println(token);
        assert "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aG9yaXRpZXMiOlsiYXV0aDEiLCJhdXRoMiJdLCJleHAiOjE2NTQ5MTY1MzksImlhdCI6MTY1NDgzMDEzOX0.cQIZCXJ3DJkWqviXa1V10mIcXVPVZZnhD3jA-FbXM7k".equals(token);
    }
}