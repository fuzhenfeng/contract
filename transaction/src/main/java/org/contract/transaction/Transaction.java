package org.contract.transaction;

public interface Transaction {
    void prepare();
    void commit();
    void rollback();
}
