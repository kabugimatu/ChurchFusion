/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Entities.Accounts;
import Entities.DeletedTransaction;
import Entities.SaccoDetails;
import Entities.SubAccounts;
import Entities.Transactions;
import Facades.AccountsFacade;
import Facades.DeletedTransactionFacade;
import Facades.SaccoDetailsFacade;
import Facades.SubAccountsFacade;
import Facades.TransactionsFacade;
import SupportBeans.TransactionsSupport;
import com.itextpdf.text.Phrase;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.resource.spi.TransactionSupport;

/**
 *
 * @author kabugi & george - PearlSoft Technologies
 */
@SessionScoped
@ManagedBean
public class AccountingController implements Printable {

    @ManagedProperty(value = "#{mainController}")
    private MainController mainController;
    //Session Beans
    @EJB
    private AccountsFacade accountsFacade;
    @EJB
    private SubAccountsFacade subAccountsFacade;
    @EJB
    private TransactionsFacade transactionFacade;
    @EJB
    private SaccoDetailsFacade detailsFacade = new SaccoDetailsFacade();
    @EJB
    private DeletedTransactionFacade deletedTransFacade;
    //Variables
    private long accountID;
    private long subAccID;
    private long transID;
    private Date today, fromReportDate, toReportDate, queryDate;
    private List<String> accountsSubAccounts;
    private SimpleDateFormat df;
    private String serviceName, transAct;
    private String reportPeriod, reportType, month, year, formatedDate, endDate, startDate, transactionType;
    private boolean showDateRange, showMonthMenu, showYearMenu, showTrialBalance, showBalanceSheet, showPnL;
    private double assetBalance = 0, liabilityBalance = 0, equityBalance = 0, expensesBalance = 0, incomeBalance = 0;
    private double totalCredit = 0, totalDebits = 0;
    //Entities
    private Accounts accounts;
    private SubAccounts subAccounts;
    private SubAccounts selectSub;
    private SubAccounts newSubAccount;
    private Transactions newTransaction;
    private Transactions selectTrans;
    private Accounts reportAssets;
    private Accounts reportLiabilities;
    private Accounts reportEquity;
    private Accounts reportExpenses;
    private Accounts reportIncome;
    private SaccoDetails saccoDetails;
    private DeletedTransaction deletedTransaction;
    //DataModels
    private DataModel<Accounts> accountsDM = new ListDataModel<Accounts>();
    private DataModel<SubAccounts> subAccDM = new ListDataModel<SubAccounts>();
    private DataModel<Transactions> transDM = new ListDataModel<Transactions>();
    private DataModel<Transactions> searchTransDM = new ListDataModel<Transactions>();
    private DataModel<SubAccounts> assetsDM = new ListDataModel<SubAccounts>();
    private DataModel<SubAccounts> liabilitiesDM = new ListDataModel<SubAccounts>();
    private DataModel<SubAccounts> equityDM = new ListDataModel<SubAccounts>();
    private DataModel<TransactionsSupport> assetSupportDM = new ListDataModel<TransactionsSupport>();
    private DataModel<TransactionsSupport> liabilitySupportDM = new ListDataModel<TransactionsSupport>();
    private DataModel<TransactionsSupport> equitySupportDM = new ListDataModel<TransactionsSupport>();
    private DataModel<TransactionsSupport> expensesSupportDM = new ListDataModel<TransactionsSupport>();
    private DataModel<TransactionsSupport> incomeSupportDM = new ListDataModel<TransactionsSupport>();
    private DataModel<DeletedTransaction> deletedTransDM = new ListDataModel<DeletedTransaction>();
    //Lists
    private List<Accounts> allParentAccounts;
    private List<SubAccounts> allSubAccounts;
    private List<SubAccounts> assetsSubAccount;
    private List<SubAccounts> liabilitySubAccounts;
    private List<DeletedTransaction> deletedTrans;
    private PrintService[] printServices;

    public AccountingController() {
        //Facades & Session Beans
        this.accountsFacade = new AccountsFacade();
        this.subAccountsFacade = new SubAccountsFacade();
        this.deletedTransFacade = new DeletedTransactionFacade();

        //Entities
        this.accounts = new Accounts();
        this.subAccounts = new SubAccounts();
        this.selectSub = new SubAccounts();
        this.newSubAccount = new SubAccounts();
        this.newTransaction = new Transactions();
        this.saccoDetails = new SaccoDetails();

        this.today = new Date();
        //Lists
        this.accountsSubAccounts = new ArrayList<String>();
        this.assetsSubAccount = new ArrayList<SubAccounts>();
        this.liabilitySubAccounts = new ArrayList<SubAccounts>();
        this.deletedTrans = new ArrayList<DeletedTransaction>();
        this.selectTrans = new Transactions();
        this.deletedTransaction = new DeletedTransaction();

    }

    //Action Methods
    public void searchTransaction() {
        Transactions searchTrans = transactionFacade.find(transID);
        List<Transactions> searchList = new ArrayList<Transactions>();
        searchList.add(searchTrans);
        searchTransDM = new ListDataModel<Transactions>(searchList);



    }

    public String selectSubAccounts() {
        String page = "managerLedger.xhtml";

        
        selectSub = subAccDM.getRowData();

        transactionType = new String();
        accountsSubAccounts = new ArrayList<String>();
        transDM = new ListDataModel<Transactions>(transactionFacade.getSubAccTransactions(selectSub.getName()));

        return page;
    }

    public void loadSubAccounts() {


        if (String.valueOf(accountID).equalsIgnoreCase("0")) {
            accounts = new Accounts();
        } else {
            accounts = accountsFacade.find(accountID);
        }


        allSubAccounts = accounts.getSubAccounts();
        subAccDM = new ListDataModel<SubAccounts>(allSubAccounts);


    }

    //creating subaccounts
    public void createSubAccount() {
        FacesContext messages = FacesContext.getCurrentInstance();

        allSubAccounts = new ArrayList<SubAccounts>();
        try {
            if (subAccountsFacade.getSubAccByName(newSubAccount.getName()) != null) {
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sub-account with this name exists", ""));
            } else {
                allSubAccounts = accounts.getSubAccounts();
                newSubAccount.setParentAccount(accounts.getName());
                newSubAccount.setDescription(accounts.getDescription());
                subAccountsFacade.create(newSubAccount);
                allSubAccounts.add(newSubAccount);
                accounts.setSubAccounts(allSubAccounts);
                accountsFacade.edit(accounts);

                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sub-account created successfully", ""));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", ""));
        }


        newSubAccount = new SubAccounts();
    }

    //create new transaction
    public void createNewTransaction() {

        String queryString = new String();
        subAccounts = new SubAccounts();
        subAccounts = selectSub;

        Random randId = new Random();
        Long transId = randId.nextLong() + transactionFacade.findAll().size();

        FacesContext messages = FacesContext.getCurrentInstance();

        SubAccounts currentSubAcc = subAccounts;
        Accounts currentAcc = accounts;
        newTransaction.setCreator(mainController.getUserLogged().getFullName());
        newTransaction.setWriteable(true);
        Transactions selectTrans = newTransaction;

        try {
            if (transactionType.equalsIgnoreCase("select")) {
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select transaction type", ""));
            } else if (transAct != null && transAct.equalsIgnoreCase("select")) {
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Action ", ""));
            } else if (newTransaction.getTransferAcc().equalsIgnoreCase("select")) {
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select transfer account", ""));
            } else if (newTransaction.getTransferAcc().equalsIgnoreCase(currentAcc.getName() + ":" + subAccounts.getName()) && !transactionType.equalsIgnoreCase("broughtforward")) {

                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You cannot transfer to the same account", ""));
            } else if ((newTransaction.getAmount() > subAccounts.getBalance() && !transactionType.equalsIgnoreCase("broughtforward")) && transAct.equalsIgnoreCase("decrease")) {
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Amount exceeds available balance", ""));
            } else if (newTransaction.getAmount() > subAccountsFacade.getSubAccByName(newTransaction.getTransferAcc().split(":")[1]).getBalance() && !transactionType.equalsIgnoreCase("broughtforward") && transAct.equalsIgnoreCase("increase")) {
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Amount exceeds available " + newTransaction.getTransferAcc().split(":")[1] + " balance", ""));
            } else {
                if (transactionType.equalsIgnoreCase("broughtforward")) {

                    subAccounts.setBalance(subAccounts.getBalance() + newTransaction.getAmount());
                    subAccountsFacade.edit(subAccounts);

                    accounts.setBalance(accounts.getBalance() + newTransaction.getAmount());
                    accountsFacade.edit(accounts);

                    newTransaction.setSubAccount(subAccounts.getName());
                    newTransaction.setAccountType(accounts.getName());
                    if (accounts.getName().equalsIgnoreCase("assets") || accounts.getName().equalsIgnoreCase("expenses")) {
                        newTransaction.setDebit(newTransaction.getAmount());
                    } else {
                        newTransaction.setCredit(newTransaction.getAmount());
                    }


                    newTransaction.setTransId(transId);
                    transactionFacade.create(newTransaction);
                    messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction successful", ""));

                } else if (transactionType.equalsIgnoreCase("normal")) {

                    actionSupport(accounts);


                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", ""));
        }

        newTransaction = new Transactions();
        subAccounts = new SubAccounts();
        accounts = currentAcc;
        selectSub = currentSubAcc;
        transDM = new ListDataModel<Transactions>(transactionFacade.getSubAccTransactions(selectSub.getName()));
        subAccDM = new ListDataModel<SubAccounts>();
        accountID = 0;

        allSubAccounts = new ArrayList<SubAccounts>();

    }

    public void actionSupport(Accounts account) {
        Random randId = new Random();
        Long transId = randId.nextLong();
        FacesContext messages = FacesContext.getCurrentInstance();
        Accounts currentAcc = accounts;
        newTransaction.setWriteable(true);
        newTransaction.setCreator(mainController.getUserLogged().getFullName());
        Transactions selectTrans = newTransaction;

        if (transAct.equalsIgnoreCase("increase")) {
            subAccounts.setBalance(subAccounts.getBalance() + newTransaction.getAmount());
            subAccountsFacade.edit(subAccounts);
            accounts.setBalance(accounts.getBalance() + newTransaction.getAmount());
            accountsFacade.edit(accounts);

            if (account.getName().equalsIgnoreCase("assets") || account.getName().equalsIgnoreCase("expenses")) {
                newTransaction.setDebit(newTransaction.getAmount());
            } else {
                newTransaction.setCredit(newTransaction.getAmount());
            }

            newTransaction.setSubAccount(selectSub.getName());

            newTransaction.setAccountType(accounts.getName());

            subAccounts = new SubAccounts();
            accounts = new Accounts();

            accounts = accountsFacade.getAccByName(newTransaction.getTransferAcc().split(":")[0]);
            subAccounts = subAccountsFacade.getSubAccByName(newTransaction.getTransferAcc().split(":")[1]);

            subAccounts.setBalance(subAccounts.getBalance() - newTransaction.getAmount());
            subAccountsFacade.edit(subAccounts);


            accounts.setBalance(accounts.getBalance() - newTransaction.getAmount());
            accountsFacade.edit(accounts);

            newTransaction.setTransId(transId);
            transactionFacade.create(newTransaction);
            newTransaction = new Transactions();
            newTransaction.setWriteable(true);
            if (accounts.getName().equalsIgnoreCase("assets") || accounts.getName().equalsIgnoreCase("expenses")) {
                newTransaction.setCredit(selectTrans.getAmount());
            } else if (accounts.getName().equalsIgnoreCase("income") || accounts.getName().equalsIgnoreCase("liabilities") || accounts.getName().equalsIgnoreCase("equity")) {
                newTransaction.setDebit(selectTrans.getAmount());
            }
            newTransaction.setAmount(selectTrans.getAmount());
            newTransaction.setAccountType(accounts.getName());


            newTransaction.setDescription(selectTrans.getDescription());
            newTransaction.setSubAccount(subAccounts.getName());
            newTransaction.setTransactionDate(selectTrans.getTransactionDate());
            newTransaction.setTransferAcc(currentAcc.getName() + ":" + selectSub.getName());
            newTransaction.setCreator(selectTrans.getCreator());
            newTransaction.setTransId(transId);
            transactionFacade.create(newTransaction);

            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction created successfully", ""));


        } else {

            subAccounts.setBalance(subAccounts.getBalance() - newTransaction.getAmount());
            subAccountsFacade.edit(subAccounts);
            accounts.setBalance(accounts.getBalance() - newTransaction.getAmount());
            accountsFacade.edit(accounts);

            if (account.getName().equalsIgnoreCase("assets") || account.getName().equalsIgnoreCase("expenses")) {
                newTransaction.setCredit(newTransaction.getAmount());
            } else {
                newTransaction.setDebit(newTransaction.getAmount());
            }

            newTransaction.setSubAccount(selectSub.getName());

            newTransaction.setAccountType(accounts.getName());

            subAccounts = new SubAccounts();
            accounts = new Accounts();

            accounts = accountsFacade.getAccByName(newTransaction.getTransferAcc().split(":")[0]);
            subAccounts = subAccountsFacade.getSubAccByName(newTransaction.getTransferAcc().split(":")[1]);

            subAccounts.setBalance(subAccounts.getBalance() + newTransaction.getAmount());
            subAccountsFacade.edit(subAccounts);


            accounts.setBalance(accounts.getBalance() + newTransaction.getAmount());
            accountsFacade.edit(accounts);

            newTransaction.setTransId(transId);
            transactionFacade.create(newTransaction);
            newTransaction = new Transactions();
            newTransaction.setWriteable(true);
            if (accounts.getName().equalsIgnoreCase("assets") || accounts.getName().equalsIgnoreCase("expenses")) {
                newTransaction.setDebit(selectTrans.getAmount());
            } else if (accounts.getName().equalsIgnoreCase("income") || accounts.getName().equalsIgnoreCase("liabilities") || accounts.getName().equalsIgnoreCase("equity")) {
                newTransaction.setCredit(selectTrans.getAmount());
            }
            newTransaction.setAmount(selectTrans.getAmount());
            newTransaction.setAccountType(accounts.getName());


            newTransaction.setDescription(selectTrans.getDescription());
            newTransaction.setSubAccount(subAccounts.getName());
            newTransaction.setTransactionDate(selectTrans.getTransactionDate());
            newTransaction.setTransferAcc(currentAcc.getName() + ":" + selectSub.getName());
            newTransaction.setCreator(selectTrans.getCreator());
            newTransaction.setTransId(transId);
            transactionFacade.create(newTransaction);

            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction created successfully", ""));




        }
    }

    public String backLedgerMain() {
        String page = "";
        if (mainController.getUserLogged().getUserDomain().equalsIgnoreCase("teller")) {
            page = "tellerAccounts.xhtml";
        } else if (mainController.getUserLogged().getUserDomain().equalsIgnoreCase("manager")) {
            page = "adminAccounts.xhtml";
        } else if (mainController.getUserLogged().getUserDomain().equalsIgnoreCase("Accounts")) {
            page = "accCharts.xhtml";
        }
        subAccDM = new ListDataModel<SubAccounts>(allSubAccounts);
        return page;

    }

    //reports 
    //select report period
    public void onSelectPeriod() {
        showDateRange = false;
        if (reportPeriod.equalsIgnoreCase("daterange")) {
            showDateRange = true;
            showYearMenu = false;
            showMonthMenu = false;
        } else if (reportPeriod.equalsIgnoreCase("year")) {
            showYearMenu = true;
            showMonthMenu = false;
            showDateRange = false;
        } else if (reportPeriod.equalsIgnoreCase("month")) {
            showYearMenu = true;
            showMonthMenu = true;
            showDateRange = false;
        }
    }

    //generate account statements
    public void preProcessPDF(Object document) throws DocumentException, BadElementException, MalformedURLException, IOException {
        SimpleDateFormat format = new SimpleDateFormat();
        String logo = detailsFacade.findAll().get(0).getLogoUrl();
        format.applyPattern("dd/MM/yyyy");
        Document pdf = (Document) document;
        Phrase phrase = new Phrase(50);
        pdf.open();
        pdf.setPageSize(PageSize.A4);
        Image image = Image.getInstance(logo);
        image.setAbsolutePosition(410f, 750f);
        pdf.add(new Paragraph(""));
        pdf.add(image);
        pdf.add(new Paragraph(""));
        pdf.add(new Paragraph(detailsFacade.findAll().get(0).getName()));
        pdf.add(new Paragraph("P.O BOX " + detailsFacade.findAll().get(0).getAddress()));
        pdf.add(new Paragraph(detailsFacade.findAll().get(0).getTelephone()));
        pdf.add(new Paragraph(detailsFacade.findAll().get(0).getEmail()));


    }

    public void generateTrialBalance() {
        totalCredit = 0;
        totalDebits = 0;

        queryDate = new Date();
        df = new SimpleDateFormat();

        assetBalance = 0;
        incomeBalance = 0;
        expensesBalance = 0;
        liabilityBalance = 0;
        equityBalance = 0;

        FacesContext messages = FacesContext.getCurrentInstance();

        if (reportType.equalsIgnoreCase("select")) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select report type", ""));
        } else if (reportPeriod.equalsIgnoreCase("select")) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select report period", ""));
        } else if (reportPeriod.equalsIgnoreCase("daterange") && (fromReportDate == null || toReportDate == null)) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter report dates", ""));
        } else {
            if (reportType.equalsIgnoreCase("trialbalance")) {
                showTrialBalance = true;
                showBalanceSheet = false;
                showPnL = false;
                if (reportPeriod.equalsIgnoreCase("month")) {
                    df.applyPattern("MMM-yyyy");
                    queryDate.setMonth(Integer.parseInt(month) - 1);
                    queryDate.setYear(Integer.parseInt(year) - 1900);
                    formatedDate = df.format(queryDate);
                } else if (reportPeriod.equalsIgnoreCase("year")) {
                    df.applyPattern("yyyy");
                    queryDate.setYear(Integer.parseInt(year) - 1900);
                    formatedDate = df.format(queryDate);
                }
                if (reportPeriod.equalsIgnoreCase("year") || reportPeriod.equalsIgnoreCase("month")) {
                    reportAssets = accountsFacade.getAccByName("Assets");
                    assetSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportAssets, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportAssets, reportPeriod));

                    reportLiabilities = accountsFacade.getAccByName("Liabilities");
                    liabilitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportLiabilities, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportLiabilities, reportPeriod));

                    reportEquity = accountsFacade.getAccByName("Equity");
                    equitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportEquity, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportEquity, reportPeriod));

                    reportExpenses = accountsFacade.getAccByName("Expenses");
                    expensesSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportExpenses, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportExpenses, reportPeriod));

                    reportIncome = accountsFacade.getAccByName("Income");
                    incomeSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportIncome, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportIncome, reportPeriod));

                } else {

                    df.applyPattern("dd-MMM-yyyy");
                    reportAssets = accountsFacade.getAccByName("Assets");
                    assetSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportAssets));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportAssets));

                    startDate = df.format(fromReportDate);
                    endDate = df.format(toReportDate);

                    reportLiabilities = accountsFacade.getAccByName("Liabilities");
                    liabilitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportLiabilities));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportLiabilities));

                    reportEquity = accountsFacade.getAccByName("Equity");
                    equitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportEquity));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportEquity));

                    reportExpenses = accountsFacade.getAccByName("Expenses");
                    expensesSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportExpenses));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportExpenses));

                    reportIncome = accountsFacade.getAccByName("Income");
                    incomeSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportIncome));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportIncome));
                }
            } else if (reportType.equalsIgnoreCase("balancesheet")) {

                showTrialBalance = false;
                showBalanceSheet = true;
                showPnL = false;

                if (reportPeriod.equalsIgnoreCase("month")) {
                    df.applyPattern("MMM-yyyy");
                    queryDate.setMonth(Integer.parseInt(month) - 1);
                    queryDate.setYear(Integer.parseInt(year) - 1900);
                    formatedDate = df.format(queryDate);
                } else if (reportPeriod.equalsIgnoreCase("year")) {
                    df.applyPattern("yyyy");
                    queryDate.setYear(Integer.parseInt(year) - 1900);
                    formatedDate = df.format(queryDate);
                }
                if (reportPeriod.equalsIgnoreCase("year") || reportPeriod.equalsIgnoreCase("month")) {
                    reportAssets = accountsFacade.getAccByName("Assets");
                    assetSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportAssets, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportAssets, reportPeriod));

                    computeAccountBalances(transactionFacade.getSubAccountBalanceMonth(queryDate, reportAssets, reportPeriod), "Assets");

                    reportLiabilities = accountsFacade.getAccByName("Liabilities");
                    liabilitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportLiabilities, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportLiabilities, reportPeriod));

                    computeAccountBalances(transactionFacade.getSubAccountBalanceMonth(queryDate, reportLiabilities, reportPeriod), "Liabilities");

                    reportEquity = accountsFacade.getAccByName("Equity");
                    equitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportEquity, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportEquity, reportPeriod));

                    computeAccountBalances(transactionFacade.getSubAccountBalanceMonth(queryDate, reportEquity, reportPeriod), "Equity");

                } else {
                    df.applyPattern("dd-MMM-yyyy");
                    reportAssets = accountsFacade.getAccByName("Assets");
                    assetSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportAssets));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportAssets));

                    computeAccountBalances(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportAssets), "Assets");

                    startDate = df.format(fromReportDate);
                    endDate = df.format(toReportDate);

                    reportLiabilities = accountsFacade.getAccByName("Liabilities");
                    liabilitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportLiabilities));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportLiabilities));

                    computeAccountBalances(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportLiabilities), "Liabilities");

                    reportEquity = accountsFacade.getAccByName("Equity");
                    equitySupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportEquity));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportEquity));

                    computeAccountBalances(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportEquity), "Equity");
                }
            } else if (reportType.equalsIgnoreCase("profitandloss")) {

                showTrialBalance = false;
                showBalanceSheet = false;
                showPnL = true;

                if (reportPeriod.equalsIgnoreCase("month")) {
                    df.applyPattern("MMM-yyyy");
                    queryDate.setMonth(Integer.parseInt(month) - 1);
                    queryDate.setYear(Integer.parseInt(year) - 1900);
                    formatedDate = df.format(queryDate);
                } else if (reportPeriod.equalsIgnoreCase("year")) {
                    df.applyPattern("yyyy");
                    queryDate.setYear(Integer.parseInt(year) - 1900);
                    formatedDate = df.format(queryDate);
                }
                if (reportPeriod.equalsIgnoreCase("year") || reportPeriod.equalsIgnoreCase("month")) {
                    reportIncome = accountsFacade.getAccByName("Income");
                    incomeSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportIncome, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportIncome, reportPeriod));

                    computeAccountBalances(transactionFacade.getSubAccountBalanceMonth(queryDate, reportIncome, reportPeriod), "Income");

                    reportExpenses = accountsFacade.getAccByName("Expenses");
                    expensesSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getSubAccountBalanceMonth(queryDate, reportExpenses, reportPeriod));
                    calculateAccountBalance(transactionFacade.getSubAccountBalanceMonth(queryDate, reportExpenses, reportPeriod));

                    computeAccountBalances(transactionFacade.getSubAccountBalanceMonth(queryDate, reportExpenses, reportPeriod), "Expenses");

                } else {
                    df.applyPattern("dd-MMM-yyyy");
                    reportIncome = accountsFacade.getAccByName("Income");
                    incomeSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportIncome));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportIncome));

                    computeAccountBalances(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportIncome), "Income");

                    startDate = df.format(fromReportDate);
                    endDate = df.format(toReportDate);

                    reportExpenses = accountsFacade.getAccByName("Expenses");
                    expensesSupportDM = new ListDataModel<TransactionsSupport>(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportExpenses));
                    calculateAccountBalance(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportExpenses));

                    computeAccountBalances(transactionFacade.getBalanceByRange(fromReportDate, toReportDate, reportExpenses), "Expenses");
                }
            } else {
                assetSupportDM = new ListDataModel<TransactionsSupport>();
                expensesSupportDM = new ListDataModel<TransactionsSupport>();
                liabilitySupportDM = new ListDataModel<TransactionsSupport>();
                equitySupportDM = new ListDataModel<TransactionsSupport>();
                incomeSupportDM = new ListDataModel<TransactionsSupport>();
            }
        }
    }

    public void calculateAccountBalance(List<TransactionsSupport> allTransactions) {

        for (TransactionsSupport tSupport : allTransactions) {
            totalCredit += Double.parseDouble(tSupport.getCredit());
            totalDebits += Double.parseDouble(tSupport.getDebit());
        }
    }

    // compute accounts balances
    public void computeAccountBalances(List<TransactionsSupport> allTransactions, String accountName) {
        System.out.println("getting here ... " + accountName);


        for (TransactionsSupport tSupport : allTransactions) {
            if (accountName.equalsIgnoreCase("Assets")) {
                assetBalance += Double.parseDouble(tSupport.getDebit());
            }
            if (accountName.equalsIgnoreCase("Liabilities")) {
                liabilityBalance += Double.parseDouble(tSupport.getCredit());
            }
            if (accountName.equalsIgnoreCase("Equity")) {
                equityBalance += Double.parseDouble(tSupport.getCredit());
            }
            if (accountName.equalsIgnoreCase("Income")) {
                incomeBalance += Double.parseDouble(tSupport.getCredit());
            }
            if (accountName.equalsIgnoreCase("Expenses")) {
                expensesBalance += Double.parseDouble(tSupport.getDebit());
            }
        }
    }

    public void onChangeTransactionType() {

        if (transactionType.equalsIgnoreCase("normal")) {
            accountsSubAccounts = new ArrayList<String>();

            for (Accounts acc : accountsFacade.findAll()) {
                for (SubAccounts subAcc : acc.getSubAccounts()) {
                    if (!selectSub.getName().equalsIgnoreCase(subAcc.getName())) {
                        accountsSubAccounts.add(acc.getName() + ":" + subAcc.getName());
                    }
                }

            }





        } else if (transactionType.equalsIgnoreCase("broughtforward")) {
            accountsSubAccounts = new ArrayList<String>();
            accountsSubAccounts.add(accounts.getName() + ":" + selectSub.getName());

        } else if (transactionType.equalsIgnoreCase("select")) {
            accountsSubAccounts = new ArrayList<String>();
        }
    }

    //select transaction
    public void selectTransaction() {
        selectTrans = transDM.getRowData();

    }

    //delete transaction
    public void deleteTransaction() {
        FacesContext message = FacesContext.getCurrentInstance();
        Accounts delAccount = new Accounts();
        SubAccounts delSubaccount = new SubAccounts();
        try {

            for (Transactions trans : transactionFacade.delTransactions(selectTrans.getTransId())) {
                delAccount = new Accounts();
                delSubaccount = new SubAccounts();

                delAccount = accountsFacade.getAccByName(trans.getAccountType());
                delSubaccount = subAccountsFacade.getSubAccByName(trans.getSubAccount());



                if (delAccount.getName().equalsIgnoreCase("assets")) {

                    if (trans.getCredit() != 0) {
//                            .out.println("reverse credit ..." + trans.getCredit());

                        delAccount.setBalance(delAccount.getBalance() + trans.getCredit());
                        accountsFacade.edit(delAccount);

                        delSubaccount.setBalance(delSubaccount.getBalance() + trans.getCredit());
                        subAccountsFacade.edit(delSubaccount);
                    } else if (trans.getDebit() != 0) {

//                          System.out.println("reverse debit ..." + trans.getDebit());

                        delAccount.setBalance(delAccount.getBalance() - trans.getDebit());
                        accountsFacade.edit(delAccount);


                        delSubaccount.setBalance(delSubaccount.getBalance() - trans.getDebit());
                        subAccountsFacade.edit(delSubaccount);
                    }


                }
                if (delAccount.getName().equalsIgnoreCase("expenses")) {
                    if (trans.getCredit() != 0) {
                        delAccount.setBalance(delAccount.getBalance() + trans.getCredit());
                        accountsFacade.edit(delAccount);

                        delSubaccount.setBalance(delSubaccount.getBalance() + trans.getCredit());
                        subAccountsFacade.edit(delSubaccount);
                    } else if (trans.getDebit() != 0) {
                        delAccount.setBalance(delAccount.getBalance() - trans.getDebit());
                        accountsFacade.edit(delAccount);

                        delSubaccount.setBalance(delSubaccount.getBalance() - trans.getDebit());
                        subAccountsFacade.edit(delSubaccount);
                    }
                } else if (delAccount.getName().equalsIgnoreCase("equity") || delAccount.getName().equalsIgnoreCase("liability") || delAccount.getName().equalsIgnoreCase("income")) {

                    if (trans.getCredit() != 0) {
                        delAccount.setBalance(delAccount.getBalance() - trans.getAmount());
                        accountsFacade.edit(delAccount);

                        delSubaccount.setBalance(delSubaccount.getBalance() - trans.getAmount());
                        subAccountsFacade.edit(delSubaccount);
                    } else if (trans.getDebit() != 0) {
                        delAccount.setBalance(delAccount.getBalance() + trans.getAmount());
                        accountsFacade.edit(delAccount);

                        delSubaccount.setBalance(delSubaccount.getBalance() + trans.getAmount());
                        subAccountsFacade.edit(delSubaccount);
                    }


                }
                transactionFacade.remove(trans);
            }


            deletedTransaction.setDeletedBy(mainController.getUserLogged().getUserName());
            deletedTransaction.setTransactionId(selectTrans.getId());
            deletedTransaction.setAccountSubacc(delAccount.getName() + ":" + delSubaccount.getName());
            deletedTransaction.setAmount(selectTrans.getAmount());



            deletedTransFacade.create(deletedTransaction);


            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction deleted successfully", ""));

            deletedTransaction = new DeletedTransaction();
            transDM = new ListDataModel<Transactions>(transactionFacade.getSubAccTransactions(selectSub.getName()));

        } catch (Exception ex) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", ""));
        }




    }

    //getters and setters
    public DeletedTransaction getDeletedTransaction() {
        return deletedTransaction;
    }

    public void setDeletedTransaction(DeletedTransaction deletedTransaction) {
        this.deletedTransaction = deletedTransaction;
    }

    public Accounts getAccounts() {

        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public SubAccounts getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(SubAccounts subAccounts) {
        this.subAccounts = subAccounts;
    }

    public DataModel<Accounts> getAccountsDM() {
        return accountsDM;
    }

    public DataModel<Transactions> getSearchTransDM() {
        return searchTransDM;
    }

    public void setSearchTransDM(DataModel<Transactions> searchTransDM) {
        this.searchTransDM = searchTransDM;
    }

    public PrintService[] getPrintServices() {
        printServices = PrintServiceLookup.lookupPrintServices(null, null);
        return printServices;
    }

    public void setPrintServices(PrintService[] printServices) {
        this.printServices = printServices;
    }

    public SubAccounts getSelectSub() {
        return selectSub;
    }

    public void setSelectSub(SubAccounts selectSub) {
        this.selectSub = selectSub;
    }

    public SubAccounts getNewSubAccount() {
        return newSubAccount;
    }

    public void setNewSubAccount(SubAccounts newSubAccount) {
        this.newSubAccount = newSubAccount;
    }

    public Transactions getNewTransaction() {
        return newTransaction;
    }

    public void setNewTransaction(Transactions newTransaction) {
        this.newTransaction = newTransaction;
    }

    public Transactions getSelectTrans() {
        return selectTrans;
    }

    public void setSelectTrans(Transactions selectTrans) {
        this.selectTrans = selectTrans;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getAssetBalance() {
        return assetBalance;
    }

    public void setAssetBalance(double assetBalance) {
        this.assetBalance = assetBalance;
    }

    public double getLiabilityBalance() {
        return liabilityBalance;
    }

    public void setLiabilityBalance(double liabilityBalance) {
        this.liabilityBalance = liabilityBalance;
    }

    public double getEquityBalance() {
        return equityBalance;
    }

    public void setEquityBalance(double equityBalance) {
        this.equityBalance = equityBalance;
    }

    public double getExpensesBalance() {
        return expensesBalance;
    }

    public void setExpensesBalance(double expensesBalance) {
        this.expensesBalance = expensesBalance;
    }

    public double getIncomeBalance() {
        return incomeBalance;
    }

    public void setIncomeBalance(double incomeBalance) {
        this.incomeBalance = incomeBalance;
    }

    public long getTransID() {
        return transID;
    }

    public void setTransID(long transID) {
        this.transID = transID;
    }

    public DataModel<TransactionsSupport> getEquitySupportDM() {
        return equitySupportDM;
    }

    public void setEquitySupportDM(DataModel<TransactionsSupport> equitySupportDM) {
        this.equitySupportDM = equitySupportDM;
    }

    public DataModel<TransactionsSupport> getIncomeSupportDM() {
        return incomeSupportDM;
    }

    public void setIncomeSupportDM(DataModel<TransactionsSupport> incomeSupportDM) {
        this.incomeSupportDM = incomeSupportDM;
    }

    public DataModel<TransactionsSupport> getExpensesSupportDM() {
        return expensesSupportDM;
    }

    public void setExpensesSupportDM(DataModel<TransactionsSupport> expensesSupportDM) {
        this.expensesSupportDM = expensesSupportDM;
    }

    public DataModel<TransactionsSupport> getAssetSupportDM() {
        return assetSupportDM;
    }

    public void setAssetSupportDM(DataModel<TransactionsSupport> assetSupportDM) {
        this.assetSupportDM = assetSupportDM;
    }

    public DataModel<TransactionsSupport> getLiabilitySupportDM() {
        return liabilitySupportDM;
    }

    public void setLiabilitySupportDM(DataModel<TransactionsSupport> liabilitySupportDM) {
        this.liabilitySupportDM = liabilitySupportDM;
    }

    public void setAccountsDM(DataModel<Accounts> accountsDM) {
        this.accountsDM = accountsDM;
    }

    public DataModel<SubAccounts> getSubAccDM() {
//        subAccDM = new ListDataModel<SubAccounts>(allSubAccounts);
        return subAccDM;
    }

    public void setSubAccDM(DataModel<SubAccounts> subAccDM) {
        this.subAccDM = subAccDM;
    }

    public DataModel<SubAccounts> getAssetsDM() {
        return assetsDM;
    }

    public void setAssetsDM(DataModel<SubAccounts> assetsDM) {
        this.assetsDM = assetsDM;
    }

    public DataModel<SubAccounts> getLiabilitiesDM() {
        return liabilitiesDM;
    }

    public void setLiabilitiesDM(DataModel<SubAccounts> liabilitiesDM) {
        this.liabilitiesDM = liabilitiesDM;
    }

    public DataModel<SubAccounts> getEquityDM() {
        return equityDM;
    }

    public void setEquityDM(DataModel<SubAccounts> equityDM) {
        this.equityDM = equityDM;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getTotalDebits() {
        return totalDebits;
    }

    public void setTotalDebits(double totalDebits) {
        this.totalDebits = totalDebits;
    }

    public DataModel<Transactions> getTransDM() {
        return transDM;
    }

    public void setTransDM(DataModel<Transactions> transDM) {
        this.transDM = transDM;
    }

    public List<Accounts> getAllParentAccounts() {
        allParentAccounts = accountsFacade.findAll();
        return allParentAccounts;
    }

    public void setAllParentAccounts(List<Accounts> allParentAccounts) {
        this.allParentAccounts = allParentAccounts;
    }

    public List<SubAccounts> getAllSubAccounts() {
        return allSubAccounts;
    }

    public void setAllSubAccounts(List<SubAccounts> allSubAccounts) {
        this.allSubAccounts = allSubAccounts;
    }

    public List<String> getAccountsSubAccounts() {


        return accountsSubAccounts;
    }

    //generate years
    public List<Integer> genYears() {
        List<Integer> years = new ArrayList<Integer>();
        Date today = new Date();
        today.setYear((today.getYear() + 1900) - 20);
        int y = today.getYear();
        for (int i = 0; i <= 20; i++) {
            years.add(y + i);

        }

        return years;

    }

    public Accounts getReportAssets() {
        return reportAssets;
    }

    public void setReportAssets(Accounts reportAssets) {
        this.reportAssets = reportAssets;
    }

    public Accounts getReportLiabilities() {
        return reportLiabilities;
    }

    public void setReportLiabilities(Accounts reportLiabilities) {
        this.reportLiabilities = reportLiabilities;
    }

    public Accounts getReportEquity() {
        return reportEquity;
    }

    public void setReportEquity(Accounts reportEquity) {
        this.reportEquity = reportEquity;
    }

    public Accounts getReportExpenses() {
        return reportExpenses;
    }

    public void setReportExpenses(Accounts reportExpenses) {
        this.reportExpenses = reportExpenses;
    }

    public Accounts getReportIncome() {
        return reportIncome;
    }

    public void setReportIncome(Accounts reportIncome) {
        this.reportIncome = reportIncome;
    }

    public void setAccountsSubAccounts(List<String> accountsSubAccounts) {
        this.accountsSubAccounts = accountsSubAccounts;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isShowMonthMenu() {
        return showMonthMenu;
    }

    public void setShowMonthMenu(boolean showMonthMenu) {
        this.showMonthMenu = showMonthMenu;
    }

    public boolean isShowYearMenu() {
        return showYearMenu;
    }

    public void setShowYearMenu(boolean showYearMenu) {
        this.showYearMenu = showYearMenu;
    }

    public boolean isShowTrialBalance() {
        return showTrialBalance;
    }

    public void setShowTrialBalance(boolean showTrialBalance) {
        this.showTrialBalance = showTrialBalance;
    }

    public boolean isShowBalanceSheet() {
        return showBalanceSheet;
    }

    public void setShowBalanceSheet(boolean showBalanceSheet) {
        this.showBalanceSheet = showBalanceSheet;
    }

    public boolean isShowPnL() {
        return showPnL;
    }

    public void setShowPnL(boolean showPnL) {
        this.showPnL = showPnL;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public long getSubAccID() {
        return subAccID;
    }

    public void setSubAccID(long subAccID) {
        this.subAccID = subAccID;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getFromReportDate() {
        return fromReportDate;
    }

    public void setFromReportDate(Date fromReportDate) {
        this.fromReportDate = fromReportDate;
    }

    public Date getToReportDate() {
        return toReportDate;
    }

    public void setToReportDate(Date toReportDate) {
        this.toReportDate = toReportDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public boolean isShowDateRange() {
        return showDateRange;
    }

    public void setShowDateRange(boolean showDateRange) {
        this.showDateRange = showDateRange;
    }

    public String getTransAct() {
        return transAct;
    }

    public void setTransAct(String transAct) {
        this.transAct = transAct;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        saccoDetails = detailsFacade.findAll().get(0);
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setFont(font);

        g.drawString("   " + saccoDetails.getName(), 5, i += 10);
        if (reportType.equalsIgnoreCase("trialbalance")) {
            if (reportPeriod.equalsIgnoreCase("month") || reportPeriod.equalsIgnoreCase("year")) {
                g.drawString("Trial Balance as At " + dateFormat.format(getQueryDate()), 5, i += 15);
                g.drawString("Debit", 200, i += 15);
                g.drawString("Credit", 300, i);
                g.drawString("ASSETS", 5, i += 15);

                List<TransactionsSupport> accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportAssets(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);
                }
                g.drawString("LIABILITIES", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportLiabilities(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);
                }
                g.drawString("INCOME", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportIncome(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);
                }
                g.drawString("EXPENSES", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportExpenses(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);


                }

                g.drawString("EQUITY", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportEquity(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);

                }

            } else {
                g.drawString("Trial Balance from " + dateFormat.format(getFromReportDate()) + " to " + dateFormat.format(getToReportDate()), 5, i += 15);
                g.drawString("Debit", 200, i += 15);
                g.drawString("Credit", 300, i);
                g.drawString("ASSETS", 5, i += 15);

                List<TransactionsSupport> accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportAssets());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);
                }
                g.drawString("LIABILITIES", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportLiabilities());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);
                }
                g.drawString("INCOME", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportIncome());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);
                }
                g.drawString("EXPENSES", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportExpenses());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);


                }

                g.drawString("EQUITY", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportEquity());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                    g.drawString(tSupport.getCredit(), 300, i);

                }


            }
            Font font2 = new Font("Arial", Font.BOLD, 14);
            g2d.setFont(font2);
            g.drawLine(5, i += 15, 400, i);
            g.drawString(totalDebits + "", 200, i += 20);
            g.drawString(totalCredit + "", 300, i);
        }
        if (reportType.equalsIgnoreCase("balancesheet")) {
            if (reportPeriod.equalsIgnoreCase("month") || reportPeriod.equalsIgnoreCase("year")) {
                g.drawString("Balance Balance as At " + dateFormat.format(getQueryDate()), 5, i += 15);
                g.drawString("ASSETS", 5, i += 15);

                List<TransactionsSupport> accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportAssets(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                }
                 
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Assets", 5, i += 20);
                g.drawString(liabilityBalance + "", 200, i);
                
                g.drawString("LIABILITIES", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportLiabilities(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getCredit(), 200, i);
                }
                
                
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Liabilities", 5, i += 20);
                g.drawString(liabilityBalance + "", 200, i);

                g.drawString("EQUITY", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportEquity(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getCredit(), 200, i);
                }
                
                
                g.drawString("Total Equity", 5, i += 20);
                g.drawString(equityBalance + "", 200, i);
            } else {
                g.drawString("Balance Sheet from " + dateFormat.format(getFromReportDate()) + " to " + dateFormat.format(getToReportDate()), 5, i += 15);
                g.drawString("ASSETS", 5, i += 15);

                List<TransactionsSupport> accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportAssets());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);

                }
                
               
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Assets", 5, i += 20);
                g.drawString(assetBalance + "", 200, i);
                
                g.drawString("LIABILITIES", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportLiabilities());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getCredit(), 200, i);
                }
               
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Liabilities", 5, i += 20);
                g.drawString(liabilityBalance + "", 200, i);

                g.drawString("EQUITY", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportEquity());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getCredit(), 200, i);
                }

                
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Equity", 5, i += 20);
                g.drawString(equityBalance + "", 200, i);
            }

        }

         if (reportType.equalsIgnoreCase("profitandloss")) {
            if (reportPeriod.equalsIgnoreCase("month") || reportPeriod.equalsIgnoreCase("year")) {
                g.drawString("Profit and Loss as At " + dateFormat.format(getQueryDate()), 5, i += 15);
                g.drawString("Income", 5, i += 15);

                List<TransactionsSupport> accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportIncome(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getCredit(), 200, i);
                }
                 
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Income", 5, i += 20);
                g.drawString(incomeBalance + "", 200, i);
                
                g.drawString("Expenses", 5, i += 20);
                accSupport = transactionFacade.getSubAccountBalanceMonth(getQueryDate(), getReportExpenses(), getReportPeriod());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                }
                
                
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Expenses", 5, i += 20);
                g.drawString(expensesBalance + "", 200, i);
               
            } else {
                g.drawString("Profit and Loss from " + dateFormat.format(getFromReportDate()) + " to " + dateFormat.format(getToReportDate()), 5, i += 15);
                g.drawString("Income", 5, i += 15);

                List<TransactionsSupport> accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportIncome());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getCredit(), 200, i);

                }
                
               
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Income", 5, i += 20);
                g.drawString(incomeBalance + "", 200, i);
                
                g.drawString("Expenses", 5, i += 20);
                accSupport = transactionFacade.getBalanceByRange(getFromReportDate(), getToReportDate(), getReportExpenses());
                for (TransactionsSupport tSupport : accSupport) {
                    g.drawString(tSupport.getSubAccount(), 5, i += 15);
                    g.drawString(tSupport.getDebit(), 200, i);
                }
               
                g.drawLine(5, i += 15, 400, i);
                g.drawString("Total Expenses", 5, i += 20);
                g.drawString(expensesBalance + "", 200, i);
               
            }

        }


        return PAGE_EXISTS;


    }

    public int printReport() throws PrinterException {
        int exitCode = 0;

        try {
            double paperWidth = 8.94, paperHeight = 14.02, leftMargin = 0.4, rightMargin = 0.4, topMargin = 0.4, bottomMargin = 0.4;


            Paper paper = new Paper();
            PageFormat pf = new PageFormat();
            paper.setSize(paperWidth * 72, paperHeight * 72);
            paper.setImageableArea(leftMargin * 72, topMargin * 72, (paperWidth - leftMargin - rightMargin) * 72, (paperHeight - topMargin - bottomMargin) * 72);
            pf.setPaper(paper);

            PrinterJob job = PrinterJob.getPrinterJob();
            pf = job.validatePage(pf);


            for (PrintService pService : printServices) {

                if (pService.toString().equalsIgnoreCase(serviceName)) {

                    job.setPrintService(pService);
                }
            }

            job.setPrintable(this, pf);




            job.print();

        } catch (PrinterException ex) {
            exitCode = 1;

            ex.printStackTrace();
        }



        return exitCode;

    }
}
