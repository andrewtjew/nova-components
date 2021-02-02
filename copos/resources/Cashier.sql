    SELECT CashDrawerSessions.ID as 'Session ID',Users.[User] as 'Cashier',[Name] as 'Cash Drawer',Opened,Closed,ISNULL(Deposits,0)-ISNULL(AdjustDeposits,0) as 'Purchase Deposits',ISNULL(Withdrawals,0)-ISNULL(AdjustWithdrawals,0) as 'Cashout Withdrawals',Closer
      FROM CashDrawerSessions
      OUTER APPLY (SELECT TOP(1) ID FROM CashDrawerTransactions WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=2 ORDER BY Created) AS OpenerTransaction
      OUTER APPLY (SELECT TOP(1) ID FROM CashDrawerTransactions WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=3 ORDER BY Created DESC) AS CloserTransaction
      CROSS APPLY (SELECT Sum(Denomination*Count) as Closer FROM CashDrawerTransactionAmounts WHERE TransactionID=CloserTransaction.ID) AS Closer
      CROSS APPLY (SELECT Sum(Denomination*Count) as Deposits FROM CashDrawerTransactions JOIN CashDrawerTransactionAmounts ON CashDrawerTransactions.ID=CashDrawerTransactionAmounts.TransactionID WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=0) AS DepositTransactions
      CROSS APPLY (SELECT Sum(Denomination*Count) as Withdrawals FROM CashDrawerTransactions JOIN CashDrawerTransactionAmounts ON CashDrawerTransactions.ID=CashDrawerTransactionAmounts.TransactionID WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=1) AS WithDrawalTransactions
      CROSS APPLY (SELECT Sum(Denomination*Count) as Fills FROM CashDrawerTransactions JOIN CashDrawerTransactionAmounts ON CashDrawerTransactions.ID=CashDrawerTransactionAmounts.TransactionID WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=2) AS FillTransactions
      CROSS APPLY (SELECT Sum(Denomination*Count) as Bleeds FROM CashDrawerTransactions JOIN CashDrawerTransactionAmounts ON CashDrawerTransactions.ID=CashDrawerTransactionAmounts.TransactionID WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=3) AS BleedTransactions
      CROSS APPLY (SELECT Sum(Denomination*Count) as AdjustDeposits FROM CashDrawerTransactions JOIN PlayerTransactions ON PlayerTransactions.ID=CashDrawerTransactions.ExternalTransactionID AND (PlayerTransactions.TransactionTypeValue=4 OR PlayerTransactions.TransactionTypeValue=5) JOIN CashDrawerTransactionAmounts ON CashDrawerTransactions.ID=CashDrawerTransactionAmounts.TransactionID WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=0) AS AdjustDepositTransactions
      CROSS APPLY (SELECT Sum(Denomination*Count) as AdjustWithdrawals FROM CashDrawerTransactions JOIN PlayerTransactions ON PlayerTransactions.ID=CashDrawerTransactions.ExternalTransactionID AND (PlayerTransactions.TransactionTypeValue=3 OR PlayerTransactions.TransactionTypeValue=6) JOIN CashDrawerTransactionAmounts ON CashDrawerTransactions.ID=CashDrawerTransactionAmounts.TransactionID WHERE CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID AND CashDrawerTransactions.TransactionTypeValue=1) AS AdjustWithdrawalsTransactions
      JOIN users ON Users.ID=CashDrawerSessions.CashierID
      JOIN CashDrawers ON CashDrawers.ID=CashDrawerSessions.CashDrawerID
      WHERE 
	  Opened>=? AND Opened<? AND 
	  Closed IS NOT NULL
	  ORDER BY Opened 

 