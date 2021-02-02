SELECT PIN,FirstName,LastName,Address1,Address2,State,Zipcode,P.BP as 'Bingo Purchases',P.SP as 'Skill Purchases',P.BW as 'Bingo Withdrawls',P.SW as 'Skill Purchases',P.SP+P.BP as 'Total Purchases',P.BW+P.SW as 'Total Withdrawals' FROM 
(SELECT PlayerTransactions.PlayerAccountNumber,
sum(CASE WHEN TransactionTypeValue=0 AND (PlayerTransactions.ProductTypeValue=0 or PlayerTransactions.ProductTypeValue is null) THEN CashableCash ELSE 0 END) as BP
,sum(CASE WHEN TransactionTypeValue=0 AND PlayerTransactions.ProductTypeValue=1 THEN CashableCash ELSE 0 END) as SP
,sum(CASE WHEN TransactionTypeValue=1 AND PlayerTransactions.ProductTypeValue=0 THEN CashableCash ELSE 0 END) as BW
,sum(CASE WHEN TransactionTypeValue=1 AND PlayerTransactions.ProductTypeValue=1 THEN CashableCash ELSE 0 END) as SW
  FROM [cogas].[dbo].[PlayerTransactions]
  LEFT JOIN ActivityPlays ON ActivityPlays.PlayerTransactionID=PlayerTransactions.ID
  JOIN Players ON Players.AccountNumber=PlayerTransactions.PlayerAccountNumber
  JOIN PINs ON PINs.PlayerAccountNumber=Players.AccountNumber AND PIns.Retired IS NULL
  where PlayerTransactions.Created>=? AND PlayerTransactions.Created<?
  group by PlayerTransactions.PlayerAccountNumber) AS P
  JOIN Players ON Players.AccountNumber=P.PlayerAccountNumber
  JOIN PINs ON PINs.PlayerAccountNumber=Players.AccountNumber AND PIns.Retired IS NULL




