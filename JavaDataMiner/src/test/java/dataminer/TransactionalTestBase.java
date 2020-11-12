// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package dataminer;


import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class TransactionalTestBase {

    protected Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/sampledb", "philipp", "test_pwd");

        onSetUpBeforeTransaction();
        connection.setAutoCommit(false); // Start the Transaction:
        onSetUpInTransaction();
    }

    @AfterEach
    public void tearDown() throws Exception {

        onTearDownInTransaction();
        connection.rollback();
        onTearDownAfterTransaction();

        connection.close();
    }

    protected void onSetUpInTransaction() throws Exception {}

    protected void onSetUpBeforeTransaction() throws Exception {}

    protected void onTearDownInTransaction() throws Exception {}

    protected void onTearDownAfterTransaction() throws Exception {}

}