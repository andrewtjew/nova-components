USE [master]
GO
/****** Object:  Database [cogas]    Script Date: 1/14/2021 10:08:16 PM ******/
CREATE DATABASE [cogas]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'cogas', FILENAME = N'Z:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\cogas.mdf' , SIZE = 4792320KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'cogas_log', FILENAME = N'Z:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\cogas_log.ldf' , SIZE = 1515520KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [cogas] SET COMPATIBILITY_LEVEL = 130
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [cogas].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [cogas] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [cogas] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [cogas] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [cogas] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [cogas] SET ARITHABORT OFF 
GO
ALTER DATABASE [cogas] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [cogas] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [cogas] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [cogas] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [cogas] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [cogas] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [cogas] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [cogas] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [cogas] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [cogas] SET  DISABLE_BROKER 
GO
ALTER DATABASE [cogas] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [cogas] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [cogas] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [cogas] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [cogas] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [cogas] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [cogas] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [cogas] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [cogas] SET  MULTI_USER 
GO
ALTER DATABASE [cogas] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [cogas] SET DB_CHAINING OFF 
GO
ALTER DATABASE [cogas] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [cogas] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [cogas] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [cogas] SET QUERY_STORE = OFF
GO
USE [cogas]
GO
/****** Object:  User [test]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE USER [test] FOR LOGIN [test] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [test]
GO
/****** Object:  Table [dbo].[ActiveCommunityPrizeWinners]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ActiveCommunityPrizeWinners](
	[PlayerAccountNumber] [bigint] NOT NULL,
	[CommunityPrizeID] [bigint] NOT NULL,
	[Notified] [datetime] NULL,
	[PlayerTransactionID] [bigint] NULL,
	[ClaimSessionID] [bigint] NULL,
	[Closed] [datetime] NULL,
	[PublishResult] [varchar](50) NULL,
	[Published] [datetime] NULL,
 CONSTRAINT [PK_ActiveCommunityPrizeWinners_1] PRIMARY KEY CLUSTERED 
(
	[PlayerAccountNumber] ASC,
	[CommunityPrizeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ActivityPlays]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ActivityPlays](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[Created] [datetime] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[ProductTypeValue] [smallint] NOT NULL,
	[Recorded] [datetime] NULL,
	[RecordCashierID] [bigint] NULL,
	[Score] [int] NULL,
	[SequenceNumber] [bigint] NULL,
	[DeskNumber] [int] NULL,
	[GameID] [bigint] NULL,
	[CashedOut] [datetime] NULL,
	[PlayerTransactionID] [bigint] NULL,
	[ScoreBelowMinimum] [bit] NULL,
	[ItemCount] [int] NULL,
	[ItemName] [varchar](50) NULL,
	[ItemValue] [bigint] NULL,
	[PurchasePlayerTransactionID] [bigint] NULL,
	[Comments] [varchar](50) NULL,
	[GameTerminal] [varchar](50) NULL,
 CONSTRAINT [PK_ActivityPlays] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Balances]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Balances](
	[PlayerAccountNumber] [bigint] NOT NULL,
	[CashableCashBalance] [bigint] NOT NULL,
	[NonCashableCashBalance] [bigint] NOT NULL,
	[PointBalance] [bigint] NOT NULL,
	[BingoBalance] [bigint] NOT NULL,
	[SkillBalance] [bigint] NOT NULL,
 CONSTRAINT [PK_Balance] PRIMARY KEY CLUSTERED 
(
	[PlayerAccountNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BillAcceptorSessions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BillAcceptorSessions](
	[SessionID] [bigint] IDENTITY(1,1) NOT NULL,
	[DeviceName] [varchar](50) NOT NULL,
	[Opened] [datetime] NOT NULL,
	[OpenUserID] [bigint] NOT NULL,
	[CloseUserID] [bigint] NULL,
	[Closed] [datetime] NULL,
 CONSTRAINT [PK_BillAcceptorSessions] PRIMARY KEY CLUSTERED 
(
	[SessionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BingoGameCashouts]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BingoGameCashouts](
	[BingoGameCashoutID] [bigint] IDENTITY(1,1) NOT NULL,
	[PIN] [varchar](50) NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[DeskNumber] [int] NOT NULL,
	[Created] [datetime] NOT NULL,
	[SequenceNumber] [bigint] NOT NULL,
	[Points] [bigint] NOT NULL,
	[PlayerTransactionID] [bigint] NOT NULL,
	[ManagerID] [bigint] NULL,
	[StatusValue] [smallint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BingoGameItems]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BingoGameItems](
	[PurchaseID] [bigint] NOT NULL,
	[ItemID] [bigint] NOT NULL,
	[Count] [int] NOT NULL,
	[Value] [bigint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BingoGamePurchases]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BingoGamePurchases](
	[PurchaseID] [bigint] IDENTITY(1,1) NOT NULL,
	[Created] [datetime] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[PIN] [varchar](50) NOT NULL,
	[PlayerTransactionID] [bigint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BingoGames]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BingoGames](
	[BingoGameID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Points] [bigint] NOT NULL,
	[StatusValue] [smallint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDispenserSessions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDispenserSessions](
	[SessionID] [bigint] IDENTITY(1,1) NOT NULL,
	[DeviceName] [varchar](50) NOT NULL,
	[Cassette1] [int] NOT NULL,
	[Cassette2] [int] NOT NULL,
	[Cassette3] [int] NOT NULL,
	[Cassette4] [int] NOT NULL,
	[OpenCassette1] [int] NOT NULL,
	[OpenCassette2] [int] NOT NULL,
	[OpenCassette3] [int] NOT NULL,
	[OpenCassette4] [int] NOT NULL,
	[Opened] [datetime] NOT NULL,
	[OpenUserID] [bigint] NOT NULL,
	[Denomination1] [bigint] NOT NULL,
	[Denomination2] [bigint] NOT NULL,
	[Denomination3] [bigint] NOT NULL,
	[Denomination4] [bigint] NOT NULL,
	[Rejected1] [int] NOT NULL,
	[Rejected2] [int] NOT NULL,
	[Rejected3] [int] NOT NULL,
	[Rejected4] [int] NOT NULL,
	[CloseUserID] [bigint] NULL,
	[Closed] [datetime] NULL,
 CONSTRAINT [PK_CashDispenserSession] PRIMARY KEY CLUSTERED 
(
	[SessionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDispenserTransactions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDispenserTransactions](
	[TransactionID] [bigint] IDENTITY(1,1) NOT NULL,
	[SessionID] [bigint] NOT NULL,
	[Amount] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Closed] [datetime] NULL,
	[Completed1] [int] NULL,
	[Completed2] [int] NULL,
	[Completed3] [int] NULL,
	[Completed4] [int] NULL,
	[Passed1] [int] NULL,
	[Passed2] [int] NULL,
	[Passed3] [int] NULL,
	[Passed4] [int] NULL,
	[Requested1] [int] NULL,
	[Requested2] [int] NULL,
	[Requested3] [int] NULL,
	[Requested4] [int] NULL,
	[Dispensed1] [int] NOT NULL,
	[Dispensed2] [int] NOT NULL,
	[Dispensed3] [int] NOT NULL,
	[Dispensed4] [int] NOT NULL,
	[Skews1] [int] NULL,
	[Skews2] [int] NULL,
	[Skews3] [int] NULL,
	[Skews4] [int] NULL,
	[Abnormals1] [int] NULL,
	[Abnormals2] [int] NULL,
	[Abnormals3] [int] NULL,
	[Abnormals4] [int] NULL,
	[Longs1] [int] NULL,
	[Longs2] [int] NULL,
	[Longs3] [int] NULL,
	[Longs4] [int] NULL,
	[Shorts1] [int] NULL,
	[Shorts2] [int] NULL,
	[Shorts3] [int] NULL,
	[Shorts4] [int] NULL,
	[Doubles1] [int] NULL,
	[Doubles2] [int] NULL,
	[Doubles3] [int] NULL,
	[Doubles4] [int] NULL,
	[TotalRejected1] [int] NULL,
	[TotalRejected2] [int] NULL,
	[TotalRejected3] [int] NULL,
	[TotalRejected4] [int] NULL,
 CONSTRAINT [PK_CashDispenserTransactions] PRIMARY KEY CLUSTERED 
(
	[TransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDrawerAmounts]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDrawerAmounts](
	[SessionID] [bigint] NOT NULL,
	[Denomination] [int] NOT NULL,
	[Count] [bigint] NOT NULL,
 CONSTRAINT [PK_CashDrawerDenominationCounts] PRIMARY KEY CLUSTERED 
(
	[SessionID] ASC,
	[Denomination] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDrawers]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDrawers](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NULL,
	[DeviceUniqueID] [varchar](50) NOT NULL,
	[Created] [datetime] NOT NULL,
 CONSTRAINT [PK_CashDrawers] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDrawerSessions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDrawerSessions](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CashierID] [bigint] NOT NULL,
	[CashDrawerID] [bigint] NOT NULL,
	[Opened] [datetime] NOT NULL,
	[Closed] [datetime] NULL,
	[ClosingNotes] [varchar](200) NULL,
 CONSTRAINT [PK_CashDrawerSessions] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDrawerTransactionAmounts]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDrawerTransactionAmounts](
	[TransactionID] [bigint] NOT NULL,
	[Denomination] [int] NOT NULL,
	[Count] [bigint] NOT NULL,
 CONSTRAINT [PK_CashDrawerTransactionAmounts] PRIMARY KEY CLUSTERED 
(
	[TransactionID] ASC,
	[Denomination] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDrawerTransactions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDrawerTransactions](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TransactionTypeValue] [smallint] NOT NULL,
	[CashDrawerSessionID] [bigint] NOT NULL,
	[ExternalTransactionID] [varchar](50) NULL,
	[ExternalTransaction] [varchar](50) NULL,
	[Created] [datetime] NOT NULL,
 CONSTRAINT [PK_CashDrawerTransactions] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CashDrawerTransactionTypeValues]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CashDrawerTransactionTypeValues](
	[Value] [smallint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Claims]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Claims](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[SequenceNumber] [bigint] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Claimed] [datetime] NULL,
	[CashierID] [bigint] NULL,
	[ProductTypeValue] [smallint] NOT NULL,
	[Score] [int] NULL,
	[DeskNumber] [int] NULL,
	[SkillGameID] [bigint] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CommunityPrizeContributions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CommunityPrizeContributions](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[Contribution] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[UserID] [bigint] NOT NULL,
 CONSTRAINT [PK_SiteContributions] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CommunityPrizes]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CommunityPrizes](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[BaseAmount] [bigint] NOT NULL,
	[ProgressiveAmount] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Awarded] [datetime] NULL,
	[ActivePrize] [bigint] NULL,
	[PresencePrize] [bigint] NULL,
	[Schedule] [varbinary](20) NULL,
	[Awarding] [datetime] NULL,
 CONSTRAINT [PK_CommunityPrizes] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CouponProducts]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CouponProducts](
	[ProductID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Price] [bigint] NOT NULL,
	[ProductTypeValue] [smallint] NOT NULL,
	[StatusValue] [smallint] NOT NULL,
 CONSTRAINT [PK_Products] PRIMARY KEY CLUSTERED 
(
	[ProductID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CouponPurchases]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CouponPurchases](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ReceiptID] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Denomination] [bigint] NOT NULL,
	[TransactionID] [varchar](50) NOT NULL,
	[PlayerTransactionID] [bigint] NULL,
	[Closed] [datetime] NULL,
 CONSTRAINT [PK_CouponPurchases] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CouponReceipts]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CouponReceipts](
	[ReceiptID] [bigint] IDENTITY(1,1) NOT NULL,
	[Created] [datetime] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[PIN] [varchar](50) NOT NULL,
	[DeviceName] [varchar](50) NOT NULL,
	[BillAcceptorSessionID] [bigint] NOT NULL,
	[ProductTypeValue] [smallint] NOT NULL,
 CONSTRAINT [PK_CouponReceipts] PRIMARY KEY CLUSTERED 
(
	[ReceiptID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CouponTournaments]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CouponTournaments](
	[TournamentID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[EntryPrice] [bigint] NOT NULL,
	[StatusValue] [smallint] NOT NULL,
	[Start] [datetime] NOT NULL,
	[End] [datetime] NULL,
	[Ended] [datetime] NULL,
	[Repeat] [bit] NULL,
	[Data] [varchar](50) NULL,
	[TieBreakerManagerID] [bigint] NULL,
	[AwardPoints] [bigint] NULL,
	[AwardTournamentID] [bigint] NULL,
	[MinimumScore] [int] NOT NULL,
	[ManualEndUserID] [bigint] NULL,
	[SequenceNumber] [int] NOT NULL,
	[Closed] [datetime] NULL,
	[TournamentTypeValue] [smallint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DialyLiabilities]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DialyLiabilities](
	[CashableCash] [bigint] NOT NULL,
	[NonCashableCash] [bigint] NOT NULL,
	[Points] [bigint] NOT NULL,
	[Count] [int] NOT NULL,
	[GamingDay] [date] NOT NULL,
	[Created] [datetime] NOT NULL,
 CONSTRAINT [PK_DialyLiabilities] PRIMARY KEY CLUSTERED 
(
	[GamingDay] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GamePlays]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GamePlays](
	[GamePlayID] [bigint] IDENTITY(1,1) NOT NULL,
	[TransactionID] [varchar](50) NOT NULL,
	[SessionID] [bigint] NOT NULL,
	[CashableCashWagered] [bigint] NOT NULL,
	[CashableCashWon] [bigint] NOT NULL,
	[NonCashableCashWagered] [bigint] NOT NULL,
	[NonCashableCashWon] [bigint] NOT NULL,
	[PointsWon] [bigint] NOT NULL,
	[CashableCashBalance] [bigint] NOT NULL,
	[NonCashableCashBalance] [bigint] NOT NULL,
	[PointBalance] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[_move_] [datetime] NULL,
 CONSTRAINT [PK_GamesPlayed] PRIMARY KEY CLUSTERED 
(
	[GamePlayID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GameTerminals]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GameTerminals](
	[GameTerminalID] [bigint] IDENTITY(1,1) NOT NULL,
	[Vendor] [varchar](50) NOT NULL,
	[GameTerminal] [varchar](50) NOT NULL,
	[Created] [datetime] NOT NULL,
	[Host] [varchar](50) NOT NULL,
	[Accessed] [datetime] NULL,
 CONSTRAINT [PK_GameTerminals] PRIMARY KEY CLUSTERED 
(
	[GameTerminalID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KeptCents]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KeptCents](
	[PlayerTransactionID] [bigint] NOT NULL,
	[Cents] [bigint] NOT NULL,
 CONSTRAINT [PK_KeptCents] PRIMARY KEY CLUSTERED 
(
	[PlayerTransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KioskBingoGameBills]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KioskBingoGameBills](
	[PurchaseID] [bigint] NOT NULL,
	[TransactionID] [varchar](50) NOT NULL,
	[Created] [datetime] NOT NULL,
	[Denomination] [bigint] NOT NULL,
	[Closed] [datetime] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KioskBingoGameItems]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KioskBingoGameItems](
	[PurchaseID] [bigint] NOT NULL,
	[ProductTypeValue] [smallint] NOT NULL,
	[ItemID] [bigint] NOT NULL,
	[Count] [int] NOT NULL,
	[Value] [bigint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KioskBingoGamePurchases]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KioskBingoGamePurchases](
	[PurchaseID] [bigint] IDENTITY(1,1) NOT NULL,
	[Created] [datetime] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[BillAcceptorSessionID] [bigint] NOT NULL,
	[PIN] [varchar](16) NOT NULL,
	[Closed] [datetime] NULL,
	[DeviceName] [varchar](50) NOT NULL,
	[PlayerTransactionID] [bigint] NULL,
	[StatusValue] [smallint] NOT NULL,
	[Pending] [datetime] NULL,
	[Change] [bigint] NULL,
	[CashDispenserTransactionID] [bigint] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KioskClearPurchases]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KioskClearPurchases](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[Created] [datetime] NOT NULL,
	[CashierID] [bigint] NOT NULL,
	[PurchaseID] [bigint] NULL,
	[CashDrawerTransactionID] [bigint] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Kiosks]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Kiosks](
	[DeviceName] [varchar](50) NOT NULL,
	[Created] [datetime] NOT NULL,
	[StatusValue] [smallint] NOT NULL,
	[CDULicenseKey] [varchar](50) NULL,
	[BACurrency] [varchar](50) NULL,
	[PINPadComPort] [int] NULL,
	[IndicatorComPort] [int] NULL,
	[BAComPort] [int] NULL,
	[CDUComPort] [int] NULL,
	[ReceiptPrinterComPort] [int] NULL,
	[Location] [varchar](150) NULL,
 CONSTRAINT [PK_Kiosks] PRIMARY KEY CLUSTERED 
(
	[DeviceName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KioskWithdrawals]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KioskWithdrawals](
	[WithdrawalID] [bigint] IDENTITY(1,1) NOT NULL,
	[Created] [datetime] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[PIN] [varchar](50) NOT NULL,
	[Closed] [datetime] NULL,
	[DeviceName] [varchar](50) NOT NULL,
	[PlayerTransactionID] [bigint] NULL,
	[StatusValue] [smallint] NOT NULL,
	[Amount] [bigint] NOT NULL,
	[SequenceClaimID] [bigint] NOT NULL,
	[CashDispenserTransactionID] [bigint] NOT NULL,
	[Error] [varbinary](50) NULL,
	[RefundPlayerTransactionID] [bigint] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[kv]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[kv](
	[k] [varchar](50) NOT NULL,
	[v] [varchar](4000) NOT NULL,
 CONSTRAINT [PK_Store] PRIMARY KEY CLUSTERED 
(
	[k] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LineItems]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LineItems](
	[PurchaseID] [bigint] NOT NULL,
	[ItemID] [bigint] NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Count] [int] NOT NULL,
	[Value] [bigint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PINs]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PINs](
	[PIN] [varchar](16) NOT NULL,
	[CashDrawerSessionID] [bigint] NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Retired] [datetime] NULL,
	[KioskDeviceName] [varchar](50) NULL,
 CONSTRAINT [PK_CreatedPINs] PRIMARY KEY CLUSTERED 
(
	[PIN] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PlayerDesignationValues]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PlayerDesignationValues](
	[Value] [smallint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Players]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Players](
	[AccountNumber] [bigint] IDENTITY(1,1) NOT NULL,
	[FirstName] [varchar](150) NULL,
	[LastName] [varchar](150) NULL,
	[NickName] [varchar](150) NULL,
	[Address1] [varchar](250) NULL,
	[Address2] [varchar](250) NULL,
	[City] [varchar](250) NULL,
	[State] [varchar](150) NULL,
	[Zipcode] [varchar](50) NULL,
	[Country] [varchar](50) NULL,
	[Phone] [varchar](50) NULL,
	[Email] [varchar](350) NULL,
	[ID] [varchar](250) NULL,
	[Notes] [varchar](250) NULL,
	[PlayerStateValue] [smallint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Updated] [datetime] NOT NULL,
	[TaxID] [varchar](50) NULL,
	[ReceiveEmails] [bit] NULL,
	[ReceivePhoneMessages] [bit] NULL,
	[ReceiveMail] [bit] NULL,
	[dateOfBirth] [date] NULL,
	[Source] [varchar](50) NULL,
	[PlayerDesignationValue] [smallint] NULL,
	[Hash] [varbinary](256) NULL,
	[Salt] [varchar](50) NULL,
	[ResetCode] [varchar](50) NULL,
	[ResetCodeCreated] [datetime] NULL,
	[SecurityFails] [int] NULL,
	[LastSecurityFail] [datetime] NULL,
	[LastHash] [varbinary](256) NULL,
	[LastW9] [datetime] NULL,
	[Signature] [varchar](50) NULL,
	[W9CashierID] [bigint] NULL,
 CONSTRAINT [PK_Player] PRIMARY KEY CLUSTERED 
(
	[AccountNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PlayerTransactions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PlayerTransactions](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TransactionTypeValue] [smallint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[CashableCashBalance] [bigint] NOT NULL,
	[NonCashableCashBalance] [bigint] NOT NULL,
	[PointBalance] [bigint] NOT NULL,
	[CashableCash] [bigint] NOT NULL,
	[NonCashableCash] [bigint] NOT NULL,
	[Points] [bigint] NOT NULL,
	[ExternalTransactionID] [varchar](50) NULL,
	[CashierID] [bigint] NULL,
	[ProductTypeValue] [smallint] NULL,
	[KioskDeviceName] [varchar](50) NULL,
	[CouponCash] [bigint] NULL,
 CONSTRAINT [PK_BalanceTransactions_1] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PlayerTransactionTypeValues]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PlayerTransactionTypeValues](
	[Value] [smallint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PresenceCommunityPrizeWinners]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PresenceCommunityPrizeWinners](
	[PlayerAccountNumber] [bigint] NOT NULL,
	[CommunityPrizeID] [bigint] NOT NULL,
	[PlayerTransactionID] [bigint] NULL,
	[PublishResult] [varchar](50) NULL,
	[Published] [datetime] NULL,
 CONSTRAINT [PK_PresenceCommunityPrizeWinners_1] PRIMARY KEY CLUSTERED 
(
	[PlayerAccountNumber] ASC,
	[CommunityPrizeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ProductPurchases]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProductPurchases](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PurchaseID] [bigint] NOT NULL,
	[CouponID] [bigint] NOT NULL,
	[Quantity] [int] NOT NULL,
	[Price] [bigint] NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_ProductPurchases] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ProductTypeValues]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProductTypeValues](
	[Value] [smallint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Roles]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Roles](
	[UserID] [bigint] NOT NULL,
	[RoleValue] [smallint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RoleValues]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RoleValues](
	[Value] [smallint] NOT NULL,
	[Name] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SequenceClaims]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SequenceClaims](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[SequenceNumber] [bigint] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Claimed] [datetime] NULL,
	[CashierID] [bigint] NULL,
	[ProductTypeValue] [smallint] NOT NULL,
	[Score] [int] NULL,
	[SkillGameID] [bigint] NULL,
	[DeskNumber] [int] NULL,
	[PlayerTransactionID] [bigint] NULL,
	[Recorded] [datetime] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Sessions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Sessions](
	[SessionID] [bigint] IDENTITY(1,1) NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[PIN] [varchar](50) NULL,
	[Vendor] [varchar](50) NOT NULL,
	[GameTerminal] [varchar](50) NOT NULL,
	[Host] [varchar](50) NOT NULL,
	[Location] [varchar](250) NULL,
	[NotificationEndPoint] [varchar](250) NULL,
	[CashableCashBalance] [bigint] NOT NULL,
	[NonCashableCashBalance] [bigint] NOT NULL,
	[PointBalance] [bigint] NOT NULL,
	[Opened] [datetime] NOT NULL,
	[Closed] [datetime] NULL,
	[CloseVendor] [varchar](50) NULL,
	[CloseGameTerminal] [varchar](50) NULL,
	[CloseHost] [varchar](50) NULL,
	[CloseCashierID] [bigint] NULL,
 CONSTRAINT [PK_Sessions] PRIMARY KEY CLUSTERED 
(
	[SessionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SkillGamePlays]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SkillGamePlays](
	[GamePlayID] [bigint] IDENTITY(1,1) NOT NULL,
	[PIN] [varchar](50) NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[Score] [int] NOT NULL,
	[SkillGameID] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[SequenceNumber] [bigint] NOT NULL,
	[Points] [bigint] NOT NULL,
	[PlayerTransactionID] [bigint] NULL,
	[ManagerID] [bigint] NULL,
	[StatusValue] [smallint] NOT NULL,
 CONSTRAINT [PK_SkillGamePlays] PRIMARY KEY CLUSTERED 
(
	[GamePlayID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SkillGames]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SkillGames](
	[SkillGameID] [bigint] IDENTITY(1,1) NOT NULL,
	[GameName] [varchar](50) NOT NULL,
	[MinimumScore] [int] NOT NULL,
	[StatusValue] [smallint] NOT NULL,
 CONSTRAINT [PK_SkillGames] PRIMARY KEY CLUSTERED 
(
	[SkillGameID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TournamentEntries]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TournamentEntries](
	[TournamentEntryID] [bigint] IDENTITY(1,1) NOT NULL,
	[TournamentID] [bigint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Count] [int] NOT NULL,
	[PlayerAccountNumber] [bigint] NOT NULL,
	[PIN] [varchar](50) NOT NULL,
	[PlayerTransactionID] [bigint] NOT NULL,
 CONSTRAINT [PK_TournamentEntries] PRIMARY KEY CLUSTERED 
(
	[TournamentEntryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Tournaments]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Tournaments](
	[TournamentID] [bigint] IDENTITY(1,1) NOT NULL,
	[TournamentName] [varchar](50) NOT NULL,
	[EntryPoints] [bigint] NOT NULL,
	[StatusValue] [smallint] NOT NULL,
 CONSTRAINT [PK_Tournaments] PRIMARY KEY CLUSTERED 
(
	[TournamentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserPermissions]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserPermissions](
	[User] [varchar](50) NOT NULL,
	[Permission] [varchar](250) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[User] [varchar](50) NOT NULL,
	[Hash] [varbinary](256) NOT NULL,
	[Salt] [varchar](50) NOT NULL,
	[FirstName] [varchar](50) NULL,
	[LastName] [varchar](50) NULL,
	[StatusValue] [smallint] NOT NULL,
	[Created] [datetime] NOT NULL,
	[Password] [varchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201218-131312]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201218-131312] ON [dbo].[ActivityPlays]
(
	[PlayerAccountNumber] ASC,
	[ProductTypeValue] ASC,
	[Recorded] ASC,
	[CashedOut] ASC,
	[ScoreBelowMinimum] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201029-211356]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-211356] ON [dbo].[BingoGameCashouts]
(
	[PlayerTransactionID] ASC,
	[PIN] ASC,
	[DeskNumber] ASC,
	[SequenceNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201029-213842]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-213842] ON [dbo].[CashDrawerTransactions]
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201029-213932]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-213932] ON [dbo].[CashDrawerTransactions]
(
	[Created] ASC,
	[ID] ASC,
	[TransactionTypeValue] ASC,
	[CashDrawerSessionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201029-215015]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-215015] ON [dbo].[CashDrawerTransactions]
(
	[TransactionTypeValue] ASC,
	[CashDrawerSessionID] ASC,
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201029-215123]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-215123] ON [dbo].[CashDrawerTransactions]
(
	[TransactionTypeValue] ASC,
	[ID] ASC,
	[CashDrawerSessionID] ASC,
	[ExternalTransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20200311-183715]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200311-183715] ON [dbo].[GamePlays]
(
	[TransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200311-184427]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200311-184427] ON [dbo].[GamePlays]
(
	[SessionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200707-154724]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200707-154724] ON [dbo].[GamePlays]
(
	[Created] ASC
)
INCLUDE([CashableCashWagered],[CashableCashWon],[SessionID]) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200716-075451]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200716-075451] ON [dbo].[GamePlays]
(
	[_move_] ASC,
	[GamePlayID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20200311-184837]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200311-184837] ON [dbo].[GameTerminals]
(
	[Vendor] ASC,
	[GameTerminal] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201006-145603]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201006-145603] ON [dbo].[KioskBingoGameBills]
(
	[PurchaseID] ASC,
	[TransactionID] ASC,
	[Denomination] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201023-173012]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201023-173012] ON [dbo].[KioskBingoGameBills]
(
	[PurchaseID] ASC,
	[TransactionID] ASC,
	[Denomination] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201023-172743]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201023-172743] ON [dbo].[KioskBingoGamePurchases]
(
	[PurchaseID] ASC,
	[Closed] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201029-212420]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-212420] ON [dbo].[PINs]
(
	[Retired] ASC,
	[PIN] ASC,
	[PlayerAccountNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201029-212635]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-212635] ON [dbo].[PINs]
(
	[PlayerAccountNumber] ASC,
	[Retired] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200708-150743]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200708-150743] ON [dbo].[Players]
(
	[AccountNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200706-204618]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200706-204618] ON [dbo].[PlayerTransactions]
(
	[Created] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200708-164346]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200708-164346] ON [dbo].[PlayerTransactions]
(
	[TransactionTypeValue] ASC,
	[PlayerAccountNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201029-211106]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-211106] ON [dbo].[PlayerTransactions]
(
	[TransactionTypeValue] ASC,
	[Created] ASC,
	[KioskDeviceName] ASC,
	[ID] ASC,
	[CashableCash] ASC,
	[CashierID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20200311-183608]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200311-183608] ON [dbo].[Sessions]
(
	[Vendor] ASC,
	[GameTerminal] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20200311-185038]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200311-185038] ON [dbo].[Sessions]
(
	[PlayerAccountNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20200316-154602]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20200316-154602] ON [dbo].[Sessions]
(
	[PIN] ASC,
	[Vendor] ASC,
	[GameTerminal] ASC,
	[Host] ASC,
	[Closed] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [NonClusteredIndex-20201029-222619]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-222619] ON [dbo].[Sessions]
(
	[Opened] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201029-222811]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-222811] ON [dbo].[Sessions]
(
	[Vendor] ASC,
	[GameTerminal] ASC,
	[Host] ASC,
	[Location] ASC,
	[Opened] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [NonClusteredIndex-20201029-211655]    Script Date: 1/14/2021 10:08:17 PM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20201029-211655] ON [dbo].[SkillGamePlays]
(
	[PlayerTransactionID] ASC,
	[PIN] ASC,
	[SkillGameID] ASC,
	[SequenceNumber] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Balances] ADD  CONSTRAINT [DF_Balances_BingoBalance]  DEFAULT ((0)) FOR [BingoBalance]
GO
ALTER TABLE [dbo].[Balances] ADD  CONSTRAINT [DF_Balances_SkillBalance]  DEFAULT ((0)) FOR [SkillBalance]
GO
ALTER TABLE [dbo].[CouponTournaments] ADD  CONSTRAINT [DF_CouponTournaments_MinimumScore]  DEFAULT ((0)) FOR [MinimumScore]
GO
ALTER TABLE [dbo].[CouponTournaments] ADD  CONSTRAINT [DF_CouponTournaments_Sequence]  DEFAULT ((0)) FOR [SequenceNumber]
GO
ALTER TABLE [dbo].[CouponTournaments] ADD  CONSTRAINT [DF_CouponTournaments_TournamentTypeValue]  DEFAULT ((0)) FOR [TournamentTypeValue]
GO
/****** Object:  StoredProcedure [dbo].[sp_VendorSales_depot]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[sp_VendorSales_depot]
	-- Add the parameters for the stored procedure here
	@start datetime,
	@end datetime
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	SELECT Vendor,COUNT(*) as 'Games',SUM(CashableCashWagered) as Wagered,SUM(CashableCashWon) as Won,SUM(CashableCashWagered)-SUM(CashableCashWon) as Hold,SUM(CashableCashWon)*100.0/(SUM(CashableCashWagered)+0.0001) as 'Payout %' FROM 
                depot.dbo.GamePlays (NOLOCK) LEFT JOIN Sessions ON Sessions.SessionID=GamePlays.SessionID WHERE Created>=@start AND Created<@end GROUP BY Vendor ORDER By Vendor
END
GO
/****** Object:  StoredProcedure [dbo].[sp_VendorSales_warehouse]    Script Date: 1/14/2021 10:08:17 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[sp_VendorSales_warehouse]
	-- Add the parameters for the stored procedure here
	@start datetime,
	@end datetime
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	SELECT Vendor,COUNT(*) as 'Games',SUM(CashableCashWagered) as Wagered,SUM(CashableCashWon) as Won,SUM(CashableCashWagered)-SUM(CashableCashWon) as Hold,SUM(CashableCashWon)*100.0/(SUM(CashableCashWagered)+0.0001) as 'Payout %' FROM 
                warehouse.dbo.GamePlays (NOLOCK) LEFT JOIN Sessions ON Sessions.SessionID=GamePlays.SessionID WHERE Created>=@start AND Created<@end and _move_ is null GROUP BY Vendor ORDER By Vendor
END
GO
USE [master]
GO
ALTER DATABASE [cogas] SET  READ_WRITE 
GO
