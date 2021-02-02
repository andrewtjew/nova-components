    select CashDrawerTransactions.Created as 'Time Stamp'
	,CASE WHEN CashDrawerTransactions.TransactionTypeValue=0 THEN Amount
	WHEN CashDrawerTransactions.TransactionTypeValue=1 THEN -Amount
	WHEN CashDrawerTransactions.TransactionTypeValue=2 THEN Amount
	WHEN CashDrawerTransactions.TransactionTypeValue=3 THEN -Amount
	END as 'Amount'
	,CASE WHEN CashDrawerTransactions.TransactionTypeValue=0 THEN 'Deposit' 
	WHEN CashDrawerTransactions.TransactionTypeValue=2 THEN 'Draw' 
	WHEN CashDrawerTransactions.TransactionTypeValue=3 THEN 'Return' 
	ELSE CashDrawerTransactionTypeValues.Name
	END as 'Transaction'
	,CASE WHEN PlayerTransactions.TransactionTypeValue=3 THEN 'Void Purchase' 
	WHEN CashDrawerTransactions.TransactionTypeValue=5 THEN 'Void Deposit' 
	ELSE ''
	END as 'Void'
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