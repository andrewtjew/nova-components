
SELECT 'Kiosks' as Name,Purchases,Withdrawals,Purchases+Withdrawals as Hold
  FROM [cogas].[dbo].[Kiosks]
  CROSS APPLY (
  SELECT sum(Denomination) as 'Purchases'
FROM CouponReceipts WITH (NOLOCK) 
JOIN CouponPurchases WITH (NOLOCK) on CouponPurchases.ReceiptID=CouponReceipts.ReceiptID
WHERE CouponReceipts.Created>=? AND CouponReceipts.Created<?
) AS KP
  CROSS APPLY (
SELECT SUM(Amount-ISNULL(Cents,0)) as 'Withdrawals'
  FROM KioskWithdrawals
  LEFT JOIN KeptCents ON KeptCents.PlayerTransactionID=KioskWithdrawals.PlayerTransactionID 
WHERE KioskWithdrawals.Created>=? AND KioskWithdrawals.Created<? AND Closed IS NOT NULL AND KioskWithdrawals.StatusValue=?
  ) as KW
UNION
SELECT 'POS' as 'Name',Sum(CASE WHEN PlayerTransactions.TransactionTypeValue=0 THEN CashableCash ELSE 0 END) as 'Purchases',Sum(CASE WHEN PlayerTransactions.TransactionTypeValue=1 THEN CashableCash ELSE 0 END) as 'Withdrawals'
,Sum(CASE WHEN PlayerTransactions.TransactionTypeValue=0 THEN CashableCash ELSE 0 END)-Sum(CASE WHEN PlayerTransactions.TransactionTypeValue=1 THEN CashableCash ELSE 0 END) as 'Hold' FROM PlayerTransactions WITH (NOLOCK) Where KioskDeviceName IS NULL
AND PlayerTransactions.Created>=? AND PlayerTransactions.Created<?
