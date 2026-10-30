package com.devops;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void testGetMessage() {
        assertEquals("Bonjour et bon courage dans votre projet en DevOps", App.getMessage());
    }
}
