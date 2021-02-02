SELECT DeviceName as 'Kiosk',KP as 'Kiosk Purchases',KW as 'Kiosk Withdrawals',Name as 'POS',PP as 'POS Purchases',PW as 'POS Withdrawals',ISNULL(KP,0)+ISNULL(KW,0)+ISNULL(PP,0)+ISNULL(PW,0) as 'Hold'
FROM 
(
SELECT Kiosks.DeviceName,KP.*,KW.*
  FROM [cogas].[dbo].[Kiosks]
  CROSS APPLY (
  SELECT sum(Denomination) as 'KP'
FROM CouponReceipts WITH (NOLOCK) 
JOIN CouponPurchases WITH (NOLOCK) on CouponPurchases.ReceiptID=CouponReceipts.ReceiptID
WHERE CouponReceipts.Created>=? AND CouponReceipts.Created<? 
) AS KP
  CROSS APPLY (
SELECT -SUM(Amount-ISNULL(Cents,0)) as 'KW'
  FROM KioskWithdrawals
  LEFT JOIN KeptCents ON KeptCents.PlayerTransactionID=KioskWithdrawals.PlayerTransactionID 
WHERE KioskWithdrawals.Created>=? AND KioskWithdrawals.Created<? AND Closed IS NOT NULL  
  ) AS KW) AS Kiosk

full outer join
(
SELECT * FROM (
SELECT CashDrawers.Name,PP,PW FROM CashDrawers
CROSS APPLY(
SELECT Sum(CASE WHEN PlayerTransactions.TransactionTypeValue=0 THEN CashableCash ELSE 0 END) as 'PP',-Sum(CASE WHEN PlayerTransactions.TransactionTypeValue=1 THEN CashableCash ELSE 0 END) as 'PW' FROM PlayerTransactions WITH (NOLOCK) 
JOIN CashDrawerTransactions WITH (NOLOCK) ON CashDrawerTransactions.ExternalTransactionID=PlayerTransactions.ID
JOIN CashDrawerSessions WITH (NOLOCK) ON CashDrawerSessions.ID=CashDrawerTransactions.CashDrawerSessionID AND CashDrawerSessions.CashDrawerID=CashDrawers.ID
WHERE PlayerTransactions.Created>=? AND PlayerTransactions.Created<?
) AS P)  POS) P ON P.PP>0 AND P.PW>0