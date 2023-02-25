package org.contract.controller;

import org.contract.ioc.Ioc;
import org.contract.server.HttpReq;
import org.contract.server.HttpResp;

public interface Route {
    void init(Ioc ioc, String layer);
    HttpResp request(HttpReq httpReq, String layer);
}
