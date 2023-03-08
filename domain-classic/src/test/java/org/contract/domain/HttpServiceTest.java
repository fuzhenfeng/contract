package org.contract.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpServiceTest {

    Service service;

    @BeforeEach
    void setUp() {
        service = new HttpService();
    }

    @AfterEach
    void tearDown() {
        service = null;
    }

    @Test
    void init() {
    }

    @Test
    void start() {
    }

    @Test
    void stop() {
    }
}