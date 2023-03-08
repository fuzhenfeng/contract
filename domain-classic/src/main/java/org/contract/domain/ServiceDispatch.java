package org.contract.domain;

import org.contract.common.NameSpace;
import org.contract.server.Dispatch;
import org.contract.server.HttpReq;
import org.contract.server.HttpResp;

public class ServiceDispatch implements Dispatch {
    private Route route;

    public ServiceDispatch(Route route) {
        this.route = route;
    }

    @Override
    public HttpResp happyCall(HttpReq httpReq) {
        return route.request(httpReq, NameSpace.SERVICE.name());
    }
}
