package com.userFront.service.ServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userFront.dao.PrimaryAccountDao;
import com.userFront.dao.PrimaryTransactionDao;
import com.userFront.dao.RecipientDao;
import com.userFront.dao.SavingsAccountDao;
import com.userFront.dao.SavingsTransactionDao;
import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.PrimaryTransaction;
import com.userFront.domain.Recipient;
import com.userFront.domain.SavingsAccount;
import com.userFront.domain.SavingsTransaction;
import com.userFront.domain.User;
import com.userFront.service.UserService;

@Service
public class TransactionServiceImpl implements com.userFront.service.TransactionService {
	@Autowired
	private UserService useService;
	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;
	@Autowired
	private SavingsTransactionDao savingsTransactionDao;
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	@Autowired
	private RecipientDao recipientDao;

	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = useService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactions = user.getPrimaryAccount().getPrimaryTransactionList();
		return primaryTransactions;
	}

	@Override
	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = useService.findByUsername(username);
		List<SavingsTransaction> savingsTransactions = user.getSavingsAccount().getSavingsTransactionList();
		return savingsTransactions;
	}

	@Override
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	@Override
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
	}

	@Override
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	@Override
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, double amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);
			Date date = new Date();
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished",
					amount, primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		}
		if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);
			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction(date,
					"between account transfer from " + transferTo + " to " + transferFrom, "Account", "Finished",
					amount, savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		}
	}

	@Override
	public List<Recipient> findRecipientList(Principal principal) {
		User user = useService.findByUsername(principal.getName());
		return user.getRecipientList();
	}

	@Override
	public void saveRecipient(Recipient recipient) {
		recipientDao.save(recipient);
	}

	@Override
	public Recipient findRecipientName(String recipientName) {
		return recipientDao.findByName(recipientName);
	}

	@Override
	public void deleteRecipientByName(String recipientName) {
		recipientDao.deleteByName(recipientName);
	}

	@Override
	public void toSomeoneElseTransfer(Recipient recipient, String accountType, double amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
		if(accountType.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			Date date = new Date();
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if(accountType.equalsIgnoreCase("Savings")) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		}
	}

}
