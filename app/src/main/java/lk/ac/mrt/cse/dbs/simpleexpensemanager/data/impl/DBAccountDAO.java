package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Dimuthu on 2015-12-04.
 */
public class DBAccountDAO implements AccountDAO,Serializable {

    private DBHandler handler;

    public DBAccountDAO(DBHandler handler) {
        this.handler = handler;

    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            handler.open();
            Cursor c = handler.getData(new String[]{"account_no","bank_name","account_holder_name","balance"},"account",null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

                String account_no = c.getString(0);

                list.add(account_no);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }
        return list;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> list = new ArrayList<Account>();
        try {
            handler.open();
            Cursor c = handler.getData(new String[]{"account_no","bank_name","account_holder_name","balance"},"account",null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

                String account_no = c.getString(0);
                String bank_name = c.getString(1);
                String account_holder_name = c.getString(2);
                double balance = c.getDouble(3);

                list.add(new Account(account_no,bank_name,account_holder_name,balance));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }
        return list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        try {
            handler.open();
            Cursor c = handler.getData(new String[]{"account_no","bank_name","account_holder_name","balance"},"account","account_no="+accountNo);
            if(c !=null){
                c.moveToFirst();
                String account_no = c.getString(0);
                String bank_name = c.getString(1);
                String account_holder_name = c.getString(2);
                double balance = c.getDouble(3);

                return new Account(account_no,bank_name,account_holder_name,balance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }
        return null;
    }

    @Override
    public void addAccount(Account account) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("account_no", account.getAccountNo());
            cv.put("bank_name", account.getBankName());
            cv.put("account_holder_name", account.getAccountHolderName());
            cv.put("balance", account.getBalance());
            handler.open();
            handler.createEntry(cv, "account");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        try {
            handler.open();
            handler.deleteEntry("account",accountNo);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        List<Account> accountList = getAccountsList();
        boolean isContains = false;
        Account account = null;
        for(int i=0;i<accountList.size();i++){
            if(accountList.get(i).getAccountNo().equals(accountNo)){
                isContains = true;
                account = accountList.get(i);
                break;
            }
        }
        if (!isContains) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put("account_no", account.getAccountNo());
            cv.put("bank_name", account.getBankName());
            cv.put("account_holder_name", account.getAccountHolderName());
            cv.put("balance", account.getBalance());
            handler.open();
            handler.updateEntry(cv, "account", "account_no=" + account.getAccountNo());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            handler.close();
        }

    }
}

