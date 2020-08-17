package com.userFront.service;

import java.security.Principal;
import java.util.List;

import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.PrimaryTransaction;
import com.userFront.domain.Recipient;
import com.userFront.domain.SavingsAccount;
import com.userFront.domain.SavingsTransaction;

public interface TransactionService {
	public List<PrimaryTransaction> findPrimaryTransactionList(String username);

	public List<SavingsTransaction> findSavingsTransactionList(String username);

	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);

	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);

	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);

	public void betweenAccountsTransfer(String transferFrom, String transferTo, double amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount);

	public List<Recipient> findRecipientList(Principal principal);

	public void saveRecipient(Recipient recipient);

	public Recipient findRecipientName(String recipientName);

	void deleteRecipientByName(String recipientName);
	
	void toSomeoneElseTransfer(Recipient recipient,String accountType,double amount,PrimaryAccount primaryAccount,SavingsAccount savingsAccount);
}
