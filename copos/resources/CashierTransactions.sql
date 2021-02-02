    select CashDrawerTransactions.Created as 'Time Stamp'
	,CASE WHEN CashDrawerTransactions.TransactionTypeValue=0 THEN Amount
	ELSE null
	END as 'Deposit'
	,CASE WHEN CashDrawerTransactions.TransactionTypeValue=1 THEN Amount
	ELSE null
	END as 'Withdrawal'
	,CASE WHEN CashDrawerTransactions.TransactionTypeValue=2 THEN Amount
	ELSE null
	END as 'Draw'
	,CASE WHEN CashDrawerTransactions.TransactionTypeValue=3 THEN Amount
	ELSE null
	END as 'Return'

	,CASE WHEN PlayerTransactions.TransactionTypeValue=3 THEN 'Void Purchase' 
	WHEN CashDrawerTransactions.TransactionTypeValue=5 THEN 'Void Deposit' 
	ELSE CashDrawerTransactions.ExternalTransaction
	END as 'Comment'
	,Players.FirstName+' '+Players.LastName as 'Player'
	,PINs.PIN as PIN
	from CashDrawerSessions JOIN CashDrawerTransactions ON CashDrawerTransactions.CashDrawerSessionID=CashDrawerSessions.ID 
    JOIN CashDrawerTransactionTypeValues ON CashDrawerTransactionTypeValues.Value=TransactionTypeValue
    LEFT JOIN PlayerTransactions ON PlayerTransactions.ID=CashDrawerTransactions.ExternalTransactionID
	LEFT JOIN Players ON Players.AccountNumber=PlayerTransactions.PlayerAccountNumber
	LEFT JOIN PINS ON PINS.PlayerAccountNumber=PlayerTransactions.PlayerAccountNumber AND PINs.Retired IS NULL
	LEFT JOIN PlayerTransactionTypeValues ON PlayerTransactionTypeValues.Value=PlayerTransactions.TransactionTypeValue
	CROSS APPLY (SELECT SUM(Denomination*Count) AS Amount FROM CashDrawerTransactionAmounts WHERE TransactionID=CashDrawerTransactions.ID) AS T
  WHERE CashDrawerSessions.ID=?
  ORDER BY CashDrawerTransactions.Created