package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        Account alreadyExist = accountDAO.getAccountByUsername(account.username);
        if(alreadyExist == null) {
            return accountDAO.createAccount(account);
        }
        return null;
    }
    
}
