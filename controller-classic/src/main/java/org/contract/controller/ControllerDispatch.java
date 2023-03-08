package org.contract.controller;

import org.contract.common.NameSpace;
import org.contract.server.Dispatch;
import org.contract.server.HttpReq;
import org.contract.server.HttpResp;

public class ControllerDispatch implements Dispatch {
    private Route route;

    public ControllerDispatch(Route route) {
        this.route = route;
    }

    @Override
    public HttpResp happyCall(HttpReq httpReq) {
        return route.request(httpReq, NameSpace.CONTROLLER.name());
    }
}
