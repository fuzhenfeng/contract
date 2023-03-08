package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.controller.Controller;
import org.contract.core.ContractContext;
import org.contract.domain.Service;

public class Boot {

    private final static Logger log = LogManager.getLogger(Boot.class);

    public Boot(String[] args) throws InitException, InterruptedException {
        log.info("start");
        long t1 = System.currentTimeMillis();

        ContractContext contractContext = ContractContext.newBuilder()
                .args(args)
                .classLoader(Boot.class.getClassLoader())
                .config("contract.properties")
                .build();

        Controller controller = contractContext.getController();
        controller.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                controller.stop();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }));

        Service domain = contractContext.getDomain();
        domain.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                domain.stop();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }));

        int loadTime = (int) (System.currentTimeMillis() - t1);
        log.info("load success, spend " + loadTime + " .");
    }
}
