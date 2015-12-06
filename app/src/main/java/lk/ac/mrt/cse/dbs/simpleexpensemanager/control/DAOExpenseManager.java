package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Dimuthu on 2015-12-06.
 */
public class DAOExpenseManager extends ExpenseManager{

    DBHandler handler;

    public DAOExpenseManager(Context c) throws ExpenseManagerException {
        handler = new DBHandler(c);
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {
        /*** Begin persistance storage implementation ***/

        TransactionDAO DBTransactionDAO = new DBTransactionDAO(handler);
        setTransactionsDAO(DBTransactionDAO);

        AccountDAO DBAccountDAO = new DBAccountDAO(handler);
        setAccountsDAO(DBAccountDAO);

        /*** End ***/
    }
}
