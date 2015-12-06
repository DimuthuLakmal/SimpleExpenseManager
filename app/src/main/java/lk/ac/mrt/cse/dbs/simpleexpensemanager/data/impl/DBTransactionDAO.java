package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Dimuthu on 2015-12-04.
 */
public class DBTransactionDAO implements TransactionDAO,Serializable {
    private List<Transaction> transactions;
    private DBHandler handler;

    public DBTransactionDAO(DBHandler handler) {
        transactions = new LinkedList<Transaction>();
        this.handler = handler;
    }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);
        try{
            ContentValues cv = new ContentValues();
            cv.put("account_no",accountNo);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            cv.put("date",df.format(date));
            cv.put("expense_type",expenseType.toString());
            cv.put("amount",amount);
            handler.open();
            handler.createEntry(cv,"transactions");
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            handler.close();
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        transactions = new LinkedList<Transaction>();
        try {
            handler.open();
            Cursor c = handler.getData(new String[]{"account_no","date","expense_type","amount"},"transactions",null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

                String account_no = c.getString(0);
                String strdate = c.getString(1);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(strdate);

                ExpenseType expenseType = ExpenseType.valueOf(c.getString(2).toUpperCase());
                double amount = c.getDouble(3);

                transactions.add(new Transaction(date,account_no,expenseType,amount));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> list = getAllTransactionLogs();
        int size = list.size();
        if (size <= limit) {
            return list;
        }
        // return the last <code>limit</code> number of transaction logs
        return list.subList(size - limit, size);
    }

}