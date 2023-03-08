package org.contract.ioc.controller.remote;

import org.contract.common.Http;

public interface TestRemote {
    @Http(address = "GET http://127.0.0.1:8081/test HTTP/1.1")
    String get(String body);
}
