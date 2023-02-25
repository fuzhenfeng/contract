package org.example.controller.remote;

import org.contract.common.Http;

public interface ExampleRemote {
    @Http(address = "GET http://127.0.0.1:8081/example HTTP/1.1")
    String get(String body);
}
