package org.contract.transaction;

import org.contract.config.Config;

public interface TCCTransaction {
    void init(Config config);
    void tryDo();
    void confirm();
    void cancel();
}
