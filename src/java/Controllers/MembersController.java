package Controllers;

import Entities.Accounts;
import Entities.AuditTrail;
import Entities.BankBranches;
import Entities.Business;
import Entities.Charges;
import Entities.CompanyBranches;
import Entities.Contribution;
import Entities.Kin;
import Entities.LoanGuarantors;
import Entities.LoanSecurity;
import Entities.LoanSettings;
import Entities.MemberAccount;
import Entities.MemberLoan;
import Entities.MemberSettings;
import Entities.Referee;
import Entities.SaccoAccount;
import Entities.SaccoBanks;
import Entities.SaccoDetails;
import Entities.SaccoMember;
import Entities.SharesTransfer;
import Entities.SubAccounts;
import Entities.Transactions;
import Entities.Users;
import Facades.AccountsFacade;
import Facades.AuditTrailFacade;
import Facades.BankBranchesFacade;
import Facades.BusinessFacade;
import Facades.ChargesFacade;
import Facades.ContributionFacade;
import Facades.BranchesFacade;
import Facades.KinFacade;
import Facades.LoanGuarantorsFacade;
import Facades.LoanSecurityFacade;
import Facades.LoanSettingsFacade;
import Facades.MemberAccountFacade;
import Facades.MemberLoanFacade;
import Facades.MemberSettingsFacade;
import Facades.RefereeFacade;
import Facades.SaccoAccountFacade;
import Facades.SaccoBanksFacade;
import Facades.SaccoDetailsFacade;
import Facades.SaccoMemberFacade;
import Facades.SaccoUserFacade;
import Facades.ShareSettingsFacade;
import Facades.SharesTransferFacade;
import Facades.SubAccountsFacade;
import Facades.TransactionsFacade;
import SupportBeans.SMSHandler;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
//import com.sun.msv.datatype.xsd.regex.RegExp;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.imageio.stream.FileImageOutputStream;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.hibernate.validator.internal.util.Contracts;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies All rights reserved
 */
@SessionScoped
@ManagedBean
public class MembersController implements Printable {

    @ManagedProperty(value = "#{mainController.userLogged}")
    private Users tellerLogged = new Users();
    
    private SMSHandler smsHandler;
    //Entities
    private SaccoMember newMember;
    private SaccoDetails saccoDetails;
    private Referee referee;
    private Business business;
    private Business memberBusiness;
    private Kin kin;
    private Charges charge;
    private Charges selectedCharge;
    private MemberAccount memberAccount;
    private Contribution contribution, editContribution;
    private SaccoMember dbMember, refMember;
    private SaccoMember editMember;
    private MemberAccount dbAccount;
    private Business editBusiness;
    private Referee editReferee;
    private Kin editKin;
    private MemberLoan dbLoan;
    private SaccoAccount saccoAccount;
    private MemberSettings memberSettings;
    private LoanSettings loanSettings;
    private SaccoMember receiverMember;
    private SharesTransfer transfer;
    private Accounts accounts;
    private SubAccounts subAccount;
    private Transactions transaction;
    private AuditTrail newAuditTrail;
    private DecimalFormat dFormat;
    //class variables
    private String licenseStatus, memberPhoto, newMemberNumber, searchCriteria, fileSeparator, loanNumber, searchReceiver, searchAdminMember, searchRefMember;
    private boolean showLicensed = true, printNewMember, printContribution, tellerMemberFound, printStatement, addMemberSuccess, dbMemberFound, adminMemberFound, memberHasLoans, dataValid, transferSuccess = false, receiverFound, validTransfer;
    private boolean payFromDeposit, hasApprovedLoan = false;
    private Long chargeId = null, branchRepID, contributionID = null;
    private double totalPayments;
    private double loanPrincipal, loanInterest, applicationFee, serviceLoan, insuranceFee;
    private double shares, accDeposits, accShares;
    private double deposits;
    private double dbShares; // Load member shares here from view members
    private double dbDeposits; //Load member deposits here from view members
    private Date queryDate, maxDOB, minDOB, queryMonth, today, contribDate;
    private String memberInt, branchName;
    private boolean printRecieptOK, refFound = false;
    byte[] memberPhotoBytes, signatureBytes, memberIdBytes;
    private PrintService printerService;
    private String serviceName, payRollNumber;
    private RequestContext rContext;
    private StreamedContent memberPhotoDB, memberIDScanDB;
    private boolean showButton, emptyList, validName, validId, validTel, showchargesPG, transactionComplete = false, shareLimitReached = false;
    private FacesContext ctx;
    private String filterValue, transSearchValue, deleteReason;
    private boolean showDateRange, showUsers, showMembers, showLoanData;
    private Date fromTransDate, toTransDate;
    private String globalSearchCriteria;
    private boolean memberFound, showMember, validBulkLoan;
    private Date loanStartDate, loanEndDate;
    private int loanInstallments;
    //ejbs
    @EJB
    private SaccoMemberFacade memberFacade = new SaccoMemberFacade();
    @EJB
    private SaccoDetailsFacade detailsFacade = new SaccoDetailsFacade();
    @EJB
    private MemberSettingsFacade memberSettingsFacade;
    @EJB
    private LoanSettingsFacade loanSettingsFacade;
    @EJB
    private ChargesFacade chargesFacade;
    @EJB
    private RefereeFacade refFacade = new RefereeFacade();
    @EJB
    private KinFacade kinsFacade = new KinFacade();
    @EJB
    private MemberAccountFacade memberAccountFacade = new MemberAccountFacade();
    @EJB
    private BusinessFacade businessFacade = new BusinessFacade();
    @EJB
    private ContributionFacade contributionFacade;
    @EJB
    private MemberLoanFacade loanFacade;
    @EJB
    private SaccoAccountFacade saccoAccountFacade;
    @EJB
    private SharesTransferFacade transferFacade;
    @EJB
    private LoanGuarantorsFacade guarantorFacade;
    @EJB
    private LoanSecurityFacade loanSecFacade;
    @EJB
    private AccountsFacade accountsFacade;
    @EJB
    private SubAccountsFacade subAccountsFacade;
    @EJB
    private TransactionsFacade transactionFacade;
    @EJB
    private BranchesFacade branchesFacade;
    @EJB
    private ShareSettingsFacade shareSettingsFacade;
    @EJB
    private SaccoBanksFacade banksFacade;
    @EJB
    private BankBranchesFacade bankBranchFacade;
    @EJB
    private SaccoUserFacade usersFacde;
    @EJB
    private AuditTrailFacade auditTrailFacade;
    //lists
    private List<Referee> tempReferees;
    private List<Kin> kinList;
    private List<Charges> membershipCharges;
    private List<Charges> allCharges;
    private List<SaccoMember> allSaccoMembers; // Used for validation purposes
    private List<Referee> allMemberReferees;
    private List<Kin> allMemberKins;
    private List<MemberLoan> unclearedLoans;
    private List<SharesTransfer> dbTransfers;
    private List<CompanyBranches> allCompanyBranches;
    private List<SaccoBanks> allSaccoBanks;
    private List<BankBranches> bankBranches;
    private PrintService[] printServices;
    private List<Users> systemUsers;
    private Users transactionUser;
    private SaccoMember transactionMember;
    //data models
    private DataModel<Referee> tempRefereesDM;
    private DataModel<Kin> kinsDM;
    private DataModel<Charges> chargesDM;
    private DataModel<SaccoMember> allMembersDM;
    private DataModel<Referee> memberRefereesDM;
    private DataModel<Kin> memberKinsDM;
    private DataModel<SaccoMember> tellerMembersDM;
    private DataModel<SharesTransfer> transfersDM;
    private DataModel<SaccoMember> branchMembersDM;
    private DataModel<Contribution> contributionsDM;
    private DataModel<LoanGuarantors> loansGuaranteedDM = new ListDataModel<LoanGuarantors>();

    public MembersController() {
        this.deposits = 0.0;
        this.shares = 0.0;
        this.totalPayments = 0.0;
        //entities
        this.newMember = new SaccoMember();
        this.saccoDetails = new SaccoDetails();
        this.referee = new Referee();
        this.business = new Business();
        this.kin = new Kin();
        this.charge = new Charges();
        this.selectedCharge = new Charges();
        this.memberAccount = new MemberAccount();
        this.contribution = new Contribution();
        this.editContribution = new Contribution();
        this.dbMember = new SaccoMember();
        this.dbAccount = new MemberAccount();
        this.memberBusiness = new Business();
        this.editBusiness = new Business();
        this.editReferee = new Referee();
        this.dbLoan = new MemberLoan();
        this.saccoAccount = new SaccoAccount();
        this.memberSettings = new MemberSettings();
        this.loanSettings = new LoanSettings();
        this.receiverMember = new SaccoMember();
        this.transfer = new SharesTransfer();
        this.accounts = new Accounts();
        this.subAccount = new SubAccounts();
        this.transaction = new Transactions();
        this.refMember = new SaccoMember();
        this.transactionUser = new Users();
        this.transactionMember = new SaccoMember();
        this.newAuditTrail = new AuditTrail();

        //ejbs
        this.banksFacade = new SaccoBanksFacade();
        this.bankBranchFacade = new BankBranchesFacade();
        this.memberSettingsFacade = new MemberSettingsFacade();
        this.chargesFacade = new ChargesFacade();
        this.contributionFacade = new ContributionFacade();
        this.loanFacade = new MemberLoanFacade();
        this.saccoAccountFacade = new SaccoAccountFacade();
        this.transferFacade = new SharesTransferFacade();
        this.guarantorFacade = new LoanGuarantorsFacade();
        this.loanSecFacade = new LoanSecurityFacade();
        this.accountsFacade = new AccountsFacade();
        this.subAccountsFacade = new SubAccountsFacade();
        this.transactionFacade = new TransactionsFacade();
        this.loanSettingsFacade = new LoanSettingsFacade();
        this.branchesFacade = new BranchesFacade();
        this.shareSettingsFacade = new ShareSettingsFacade();
        this.usersFacde = new SaccoUserFacade();
        this.auditTrailFacade = new AuditTrailFacade();

        //Lists
        this.allSaccoBanks = new ArrayList<SaccoBanks>();
        this.bankBranches = new ArrayList<BankBranches>();
        this.allCompanyBranches = new ArrayList<CompanyBranches>();
        this.tempReferees = new ArrayList<Referee>();
        this.kinList = new ArrayList<Kin>();
        this.membershipCharges = new ArrayList<Charges>();
        this.allCharges = new ArrayList<Charges>();
        this.allSaccoMembers = new ArrayList<SaccoMember>();
        this.allMemberKins = new ArrayList<Kin>();
        this.unclearedLoans = new ArrayList<MemberLoan>();
        this.dbTransfers = new ArrayList<SharesTransfer>();
        this.systemUsers = new ArrayList<Users>();

        //data models
        this.branchMembersDM = new ListDataModel<SaccoMember>();
        this.tempRefereesDM = new ListDataModel<Referee>();
        this.kinsDM = new ListDataModel<Kin>();
        this.chargesDM = new ListDataModel<Charges>();
        this.allMembersDM = new ListDataModel<SaccoMember>();
        this.tellerMembersDM = new ListDataModel<SaccoMember>();
        this.transfersDM = new ListDataModel<SharesTransfer>();
        contributionsDM = new ListDataModel<Contribution>();
        //other variables
        this.licenseStatus = null;
        this.showLicensed = true;
        this.editMember = new SaccoMember();
        this.minDOB = new Date();
        this.maxDOB = new Date();
        FacesContext ctx = FacesContext.getCurrentInstance();

        this.dFormat = new DecimalFormat("#.00");

       smsHandler = new SMSHandler();
    }

    public void onselectBank() {
        if (editMember.getBankId() != null) {
            bankBranches = banksFacade.find(editMember.getBankId()).getBranches();
        } else if (newMember.getBankId() != null) {
            bankBranches = banksFacade.find(newMember.getBankId()).getBranches();
        } else {
            bankBranches = new ArrayList<BankBranches>();
        }
    }

    public String loadMemberBankBranch() {
        String dbBankBranch = "";

        try {
            if (editMember.getBankId() == null) {
                dbBankBranch = "Not Set";
            } else {

                dbBankBranch = bankBranchFacade.find(editMember.getBankBranchId()).getBranchName();

            }
        } catch (Exception ex) {
            dbBankBranch = "Not Set";
        }

        return dbBankBranch;
    }

    public String loadMemberBank() {
        String dbBank = "";

        try {
            if (editMember.getBankId() == null) {
                dbBank = "Not Set";
            } else {
                dbBank = banksFacade.find(editMember.getBankId()).getBankName();
            }
        } catch (Exception ex) {
            dbBank = "Not Set";
        }

        return dbBank;
    }

    public String loadMemberBranch(SaccoMember member) {
        String branchLoad = null;
        System.out.println("Branch id -->  " + member.getBranchId());
        if (member.getBranchId() != null) {
            CompanyBranches mBranch = branchesFacade.find(member.getBranchId());
            branchLoad = mBranch.getBranchname();
        }

        if (branchLoad != null) {
            return branchLoad;
        } else {
            return "";
        }
    }

    // edit transactions
    public void onChangeFilterValue() {
        if (filterValue.equalsIgnoreCase("Member")) {
            showDateRange = true;
            showMembers = true;
            showUsers = false;
        } else if (filterValue.equalsIgnoreCase("User")) {
            showDateRange = true;
            showMembers = false;
            showUsers = true;
        } else {
            showDateRange = false;
            showMembers = false;
            showUsers = false;
        }

        transSearchValue = "";
    }

    // fetch transactions
    public void searchTransaction() {
        try {

            if (filterValue.equalsIgnoreCase("Member") && !transSearchValue.equalsIgnoreCase("")) {
                if (transSearchValue.equalsIgnoreCase("All Members")) {
                    contributionsDM = new ListDataModel<Contribution>(contributionFacade.viewAllMemberContributions(fromTransDate, toTransDate));
                } else {
                    contributionsDM = new ListDataModel<Contribution>(contributionFacade.viewStatementsBtwDate(fromTransDate, toTransDate, transSearchValue));
                }
            } //userContribution
            else if (filterValue.equalsIgnoreCase("User") && !transSearchValue.equalsIgnoreCase("")) {
                if (transSearchValue.equalsIgnoreCase("allusers")) {
                    contributionsDM = new ListDataModel<Contribution>(contributionFacade.usersContribution(fromTransDate, toTransDate));
                } else {
                    contributionsDM = new ListDataModel<Contribution>(contributionFacade.userContribution(fromTransDate, toTransDate, transSearchValue));
                }
            } else {
                contributionsDM = new ListDataModel<Contribution>();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            contributionsDM = new ListDataModel<Contribution>();
        }
    }

    // load contribution
    public void loadContribution() {
        editContribution = contributionsDM.getRowData();
        showLoanData = false;

        if (editContribution.getLoanNumber() != null) {
            showLoanData = true;
        }
    }

    // delete transaction
    public void deleteContribution() {
        rContext = RequestContext.getCurrentInstance();
        ctx = FacesContext.getCurrentInstance();
        editContribution.setTotal(0);
        editContribution.setShares(0);
        editContribution.setDeposit(0);
        editContribution.setLoanInterest(0);
        editContribution.setLoanPrincipal(0);
        editContribution.setStatus(false);
        editContribution.setEditReason(deleteReason);
        try {
            editTransaction();
            rContext.execute("deleteDLG.hide();");
        } catch (Exception ex) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", "fail"));
        }

        deleteReason = "";
        searchTransaction();
    }

    // edit transaction
    public void editTransaction() {
        Contribution hContribution = new Contribution();
        MemberAccount hAccount = new MemberAccount();
        MemberLoan hLoan = new MemberLoan();
        Transactions hTransaction = new Transactions();
        newAuditTrail = new AuditTrail();
        double netShares = 0, netDeposits = 0, netLoanPrincipal = 0, netLoanInterest = 0, contributionTotal = 0;

        ctx = FacesContext.getCurrentInstance();

        try {
            hContribution = contributionFacade.find(editContribution.getId());
            hAccount = memberAccountFacade.getMemberAccount(editContribution.getMemberNumber());
            if (!loanFacade.searchSingleLoan(hContribution.getLoanNumber()).isEmpty()) {
                hLoan = loanFacade.searchSingleLoan(hContribution.getLoanNumber()).get(0);
            }

            netShares = editContribution.getShares() - hContribution.getShares();
            netDeposits = editContribution.getDeposit() - hContribution.getDeposit();
            netLoanPrincipal = editContribution.getLoanPrincipal() - hContribution.getLoanPrincipal();
            netLoanInterest = editContribution.getLoanInterest() - hContribution.getLoanInterest();

            contributionTotal = editContribution.getShares() + editContribution.getDeposit() + editContribution.getLoanPrincipal() + editContribution.getLoanInterest();
            hAccount.setShares(hAccount.getShares() + netShares);
            hAccount.setDeposit(hAccount.getDeposit() + netDeposits);

            editContribution.setTotal(contributionTotal);
            memberAccountFacade.edit(hAccount);
            contributionFacade.edit(editContribution);

            if (netShares != 0) {
                subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netShares) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netShares);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                if (!transactionFacade.searchTransaction(hContribution.getId(), "Shares contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Shares contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").get(0);

                    try {
                        saveAuditTrail("Transaction - Cash in Bank", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netShares) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netShares);
                    transactionFacade.edit(hTransaction);
                }


                accounts = accountsFacade.getAccByName("Assets");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netShares) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netShares);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                subAccount = subAccountsFacade.getSubAccByName("Share Capital");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netShares) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netShares);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                if (!transactionFacade.searchTransaction(hContribution.getId(), "Shares contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Share Capital").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Shares contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Share Capital").get(0);

                    try {
                        saveAuditTrail("Transaction - Share Capital", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netShares) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netShares);
                    transactionFacade.edit(hTransaction);
                }

                accounts = accountsFacade.getAccByName("Equity");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netShares) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netShares);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                try {
                    saveAuditTrail("Member Account", "Shares", hContribution.getShares() + "", editContribution.getShares() + "", "Update", editContribution.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }

                saccoAccount = saccoAccountFacade.findAll().get(0);
                saccoAccount.setShares(saccoAccount.getShares() + netShares);
                saccoAccountFacade.edit(saccoAccount);
            }

            if (netDeposits != 0) {
                subAccount = subAccountsFacade.getSubAccByName("Deposits");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netDeposits) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netDeposits);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                if (!transactionFacade.searchTransaction(hContribution.getId(), "Deposits contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Deposits").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Deposits contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Deposits").get(0);

                    try {
                        saveAuditTrail("Transaction - Deposits", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netDeposits) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netDeposits);
                    transactionFacade.edit(hTransaction);
                }

                accounts = accountsFacade.getAccByName("Liabilities");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netDeposits) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netDeposits);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netDeposits) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netDeposits);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                if (!transactionFacade.searchTransaction(hContribution.getId(), "Deposits contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Deposits contribution :" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").get(0);

                    try {
                        saveAuditTrail("Transaction - Cash in Bank", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netShares) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netDeposits);
                    transactionFacade.edit(hTransaction);
                }

                accounts = accountsFacade.getAccByName("Assets");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netDeposits) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netDeposits);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                try {
                    saveAuditTrail("Member Account", "Deposits", hContribution.getDeposit() + "", editContribution.getDeposit() + "", "Update", editContribution.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }

                saccoAccount = saccoAccountFacade.findAll().get(0);
                saccoAccount.setDeposits(saccoAccount.getDeposits() + netDeposits);
                saccoAccountFacade.edit(saccoAccount);
            }

            if (netLoanPrincipal != 0 && !loanFacade.searchSingleLoan(hContribution.getLoanNumber()).isEmpty()) {
                subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netLoanPrincipal) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netLoanPrincipal);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                //transaction.setDescription("Shares contribution :" + dbMember.getFullName());
                if (!transactionFacade.searchTransaction(hContribution.getId(), "Loan Principal:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Loan Principal:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").get(0);

                    try {
                        saveAuditTrail("Transaction - Cash in Bank", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netLoanPrincipal) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netLoanPrincipal);
                    transactionFacade.edit(hTransaction);
                }

                subAccount = subAccountsFacade.getSubAccByName("Loan to Members");
                if (netLoanPrincipal > 0) {
                    contributionTotal = contributionTotal + editContribution.getLoanPrincipal();
                    hLoan.setPrincipalPaid(hLoan.getPrincipalPaid() + netLoanPrincipal);
                    hLoan.setBalance(hLoan.getBalance() - netLoanPrincipal);
                    System.out.println("getting total ... " + hTransaction.getAmount() + netLoanPrincipal);
                    try {
                        saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() - netLoanPrincipal) + "", "Update", subAccount.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    subAccount.setBalance(subAccount.getBalance() - netLoanPrincipal);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    if (!transactionFacade.searchTransaction(hContribution.getId(), "Loan Principal:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").isEmpty()) {
                        hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Loan Principal:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").get(0);

                        try {
                            saveAuditTrail("Transaction - Loan to Members", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netLoanPrincipal) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                        } catch (Exception ex) {
                        }
                        hTransaction.setAmount(hTransaction.getAmount() + netLoanPrincipal);
                        transactionFacade.edit(hTransaction);
                    }
                } else if (netLoanPrincipal < 0) {
                    hLoan.setPrincipalPaid(hLoan.getPrincipalPaid() + netLoanPrincipal);
                    hLoan.setBalance(hLoan.getBalance() - netLoanPrincipal);
                    try {
                        saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netLoanPrincipal) + "", "Update", subAccount.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    subAccount.setBalance(subAccount.getBalance() - netLoanPrincipal);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    if (!transactionFacade.searchTransaction(hContribution.getId(), "Loan Principal:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").isEmpty()) {
                        hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Loan Principal:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").get(0);

                        try {
                            saveAuditTrail("Transaction - Loan to Members", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netLoanPrincipal) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                        } catch (Exception ex) {
                        }
                        hTransaction.setAmount(hTransaction.getAmount() + netLoanPrincipal);
                        transactionFacade.edit(hTransaction);
                    }
                }


                accounts = accountsFacade.getAccByName("Equity");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netShares) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netShares);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                try {
                    saveAuditTrail("Member Account", "Shares", hContribution.getShares() + "", editContribution.getShares() + "", "Update", editContribution.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
            }

            if (netLoanInterest != 0 && !loanFacade.searchSingleLoan(hContribution.getLoanNumber()).isEmpty()) {
                contributionTotal = contributionTotal + editContribution.getLoanInterest();
                hLoan.setInterestPaid(hLoan.getInterestPaid() + netLoanInterest);

                subAccount = subAccountsFacade.getSubAccByName("loanInterest");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netLoanInterest) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netLoanInterest);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                if (!transactionFacade.searchTransaction(hContribution.getId(), "Loan Interest:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Loan Interest").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Loan Interest:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Loan Interest").get(0);

                    try {
                        saveAuditTrail("Transaction - Loan Interest", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netLoanInterest) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netLoanInterest);
                    transactionFacade.edit(hTransaction);
                }

                accounts = accountsFacade.getAccByName("Income");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netLoanInterest) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netLoanInterest);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                try {
                    saveAuditTrail("Sub Account - " + subAccount.getName(), "Balance", subAccount.getBalance() + "", (subAccount.getBalance() + netLoanInterest) + "", "Update", subAccount.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                subAccount.setBalance(subAccount.getBalance() + netLoanInterest);
                subAccountsFacade.edit(subAccount);
                subAccount = new SubAccounts();

                if (!transactionFacade.searchTransaction(hContribution.getId(), "DLoan Interest:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").isEmpty()) {
                    hTransaction = transactionFacade.searchTransaction(hContribution.getId(), "Loan Interest:" + memberFacade.getMemberByMemberNumber(hContribution.getMemberNumber()).getFullName(), "Cash in Bank").get(0);

                    try {
                        saveAuditTrail("Transaction - Cash in Bank", "Amount", hTransaction.getAmount() + "", (hTransaction.getAmount() + netLoanInterest) + "", "Update", hTransaction.getId(), tellerLogged.getId());
                    } catch (Exception ex) {
                    }
                    hTransaction.setAmount(hTransaction.getAmount() + netLoanInterest);
                    transactionFacade.edit(hTransaction);
                }

                accounts = accountsFacade.getAccByName("Assets");
                try {
                    saveAuditTrail("Account - " + accounts.getName(), "Balance", accounts.getBalance() + "", (accounts.getBalance() + netLoanInterest) + "", "Update", accounts.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }
                accounts.setBalance(accounts.getBalance() + netLoanInterest);
                accountsFacade.edit(accounts);
                accounts = new Accounts();

                try {
                    saveAuditTrail("Member Account", "Loan Interest", hContribution.getLoanInterest() + "", editContribution.getLoanInterest() + "", "Update", editContribution.getId(), tellerLogged.getId());
                } catch (Exception ex) {
                }

                saccoAccount = saccoAccountFacade.findAll().get(0);
                saccoAccount.setInterests(saccoAccount.getInterests() + netLoanInterest);
                saccoAccountFacade.edit(saccoAccount);
            }
            loanFacade.edit(hLoan);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update successful", "success"));

        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", "fail"));
        }

        hAccount = new MemberAccount();
        //editContribution = new Contribution();
        hContribution = new Contribution();
        newAuditTrail = new AuditTrail();

        searchTransaction();


        //update member account - done
        //update loan
        //update ledger accounts 
        //edit transaction
    }

    //new audit trail record
    public void saveAuditTrail(String entity, String fieldName, String oldValue, String newValue, String operation, Long recordID, Long userID) {
        try {
            newAuditTrail = new AuditTrail();
            newAuditTrail = new AuditTrail();
            newAuditTrail.setAuditDate(new Date());
            newAuditTrail.setEntity(entity);
            newAuditTrail.setFieldName(fieldName);
            newAuditTrail.setOldValue(oldValue);
            newAuditTrail.setNewValue(newValue);
            newAuditTrail.setOperationType(operation);
            newAuditTrail.setRecordID(recordID);
            newAuditTrail.setUserID(userID);

            auditTrailFacade.create(newAuditTrail);
        } catch (Exception ex) {
        }
    }

    public void loadMembersByBranches() {
        rContext = RequestContext.getCurrentInstance();
        try {
            if (branchName != null) {
                branchMembersDM = new ListDataModel<SaccoMember>(memberFacade.getMemberByBranchID(branchName));

            } else {
                branchMembersDM = new ListDataModel<SaccoMember>();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            branchMembersDM = new ListDataModel<SaccoMember>();
        }
        rContext.update("memberBranchTBL");
    }

    public SaccoMember loadMemberByNumber(String memberNumber) {
        SaccoMember saccoMember = null;

        try {
            saccoMember = memberFacade.getMemberByMemberNumber(memberNumber);
        } catch (Exception ex) {
            saccoMember = null;
        }


        return saccoMember;
    }

    public List<Integer> genYears() {
        List<Integer> years = new ArrayList<Integer>();
        Date today = new Date();
        today.setYear((today.getYear() + 1900) - 100);
        int y = today.getYear();
        for (int i = 0; i <= 100; i++) {
            years.add(y + i);

        }

        return years;

    }

    //Validation    
    public boolean checkFullName(String name) {

        validName = true;
        boolean validChar = true;

        FacesContext ctx = FacesContext.getCurrentInstance();

        //ensure fullname has letters, "." and "'" only
        if (name != null) {
            for (int i = 0; i < name.length(); i++) {
                if (!(Character.isLetter((name.charAt(i))) || Character.isSpaceChar(name.charAt(i)) || name.charAt(i) == '.' || name.charAt(i) == '\'')) {

                    validChar = false;
                }
            }
        }


        if (validChar == false) {
            validName = false;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid character(s) for a name", ""));
        }
        return validName;
    }

    //dont delete. very crusial
    //check if reciept is to be printed on contribution
    public void onChangePrintOK() {
    }

    //validate id number ---- digits only
    public boolean validateID(String idNumber) {
        validId = true;
        boolean validChar = true;
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (idNumber != null) {
            for (int i = 0; i < idNumber.length(); i++) {
                if (!Character.isDigit(idNumber.charAt(i))) {
                    validChar = false;

                }
            }

            if (validChar == false) {
                validId = false;
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Id number should contain digits only", null));
            }
        }
        return validId;
    }

    //validate telephone number
    public boolean validateTel(String telNumber) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        boolean validChar = true;
        validTel = true;
        if (telNumber != null) {
            for (int i = 0; i < telNumber.length(); i++) {
                if (!Character.isDigit(telNumber.charAt(i))) {
                    validChar = false;

                }
            }



            if (!telNumber.startsWith("0")) {
                validTel = false;
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number should start with 0", null));
            } else {

                if (validChar == false) {
                    validTel = false;
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number should containg numbers only ", null));
                } else if (telNumber.length() < 10) {
                    validTel = false;
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number too short ", null));
                } else if (telNumber.length() > 10) {
                    validTel = false;
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number very long ", null));
                }

            }
        }
        return validTel;

    }

    //validate percentages --- 0-100
    public boolean validatePercentage(double value) {
        boolean validPercent = true;
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (value < 0 || value > 100) {
            validPercent = false;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Percentage value should be btw 0 and 100", null));
        }

        return validPercent;
    }

    //changing sacco member's business licence status
    public void changeLicenseStatus() {
        if (licenseStatus.equalsIgnoreCase("licensed")) {
            showLicensed = true;
            business = new Business();
        } else if (licenseStatus.equalsIgnoreCase("notLicensed")) {
            showLicensed = false;
            business = new Business();
        }
    }

    public String editNewMember() {

        String page = "newMember.xhtml";

        return page;
    }

    public void addNewMember() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        addMemberSuccess = false;
        contribution = new Contribution();

        if (saccoAccountFacade.findAll().isEmpty()) {
            saccoAccount = new SaccoAccount();
        } else {
            saccoAccount = saccoAccountFacade.findAll().get(0);
        }

        try {
            //Create member account
            memberAccount = new MemberAccount();
            Random randAccNumber = new Random();
            memberAccount.setId(Long.parseLong(String.valueOf(randAccNumber.nextInt(1000))));

            for (MemberAccount dbmemberAcc : memberAccountFacade.findAll()) {
                if (dbmemberAcc.getId() == memberAccount.getId()) {
                    memberAccount.setId(1234 + (long) (randAccNumber.nextDouble() * (4321 - 1234)));
                }
            }



//        System.out.println("account id generated...."+memberAccount.getId());

//            memberAccount.setShares(memberSettingsFacade.findAll().get(0).getMinimumShares());
            memberAccount.setMemberNumber(newMember.getMemberNumber());


            memberAccountFacade.create(memberAccount);

            //Create Member business
            business.setMemberNumber(newMember.getMemberNumber());
            business.setLicensed(showLicensed);
            businessFacade.create(business);


            for (Charges memberCharges : membershipCharges) {
                accounts = new Accounts();
                subAccount = new SubAccounts();
                transaction = new Transactions();

                transact(memberCharges.getName(), memberCharges.getCost(), "Charge");


            }
            contribution.setServedBy(tellerLogged.getFullName());
            contribution.setMemberNumber(newMember.getMemberNumber());
            contribution.setContributionDate(newMember.getRegistrationDate());
            contribution.setTotal(contribution.getRegistrationFees());


            contributionFacade.create(contribution);
            saccoAccountFacade.edit(saccoAccount);

            for (Referee ref : tempReferees) { // add referees to dbase
                refFacade.create(ref);
            }
            for (Kin newKin : kinList) { // add kins to dbase
                kinsFacade.create(newKin);
            }


            newMember.setNextOfKin(kinList);
            //newMember.setMemberNumber(newMemberNumber + getMemberInt());
            newMember.setReferees(tempReferees);
            newMember.setMemberImage(memberPhotoBytes);
            newMember.setMemberScanID(memberIdBytes);
            newMember.setFullName(newMember.getFullName().toUpperCase());
            newMember.setAddedBy(tellerLogged.getFullName() + "/" + tellerLogged.getTelephone());
            newMember.setMemberStatus(true);
            memberFacade.create(newMember);

            //print reciept for new member
        /*
             if(!tellerLogged.getUserDomain().equalsIgnoreCase("DEVELOPER")){
             printNewMember = true;        
        
           
             if(printReceipt()==1)
             {
             throw new PrinterException("Transaction complete but printing failed.");
             }
             }
             */


            newMember = new SaccoMember();
            memberInt = new String();
            memberIdBytes = new byte[0];
            memberPhotoBytes = new byte[0];
            kinList = new ArrayList<Kin>();
            kinsDM = new ListDataModel<Kin>();

            tempReferees = new ArrayList<Referee>();
            tempRefereesDM = new ListDataModel<Referee>();

            membershipCharges = new ArrayList<Charges>();
            chargesDM = new ListDataModel<Charges>();

            business = new Business();
            memberAccount = new MemberAccount();
            contribution = new Contribution();
            addMemberSuccess = true;
            printNewMember = false;
            showButton = false;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Member added successfully", ""));
        } catch (Exception ex) {
            addMemberSuccess = true;
            //Remember to log this error to file!!!!
            ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Operation Failed, contact Admin.", ""));
        }

    }

    public void removeMemberKin() {
        rContext = RequestContext.getCurrentInstance();
        ctx = FacesContext.getCurrentInstance();
        Kin delKin = memberKinsDM.getRowData();
        kinList = editMember.getNextOfKin();
        kinList.remove(delKin);
        editMember.setNextOfKin(kinList);
        memberFacade.edit(editMember);

        kinsFacade.remove(delKin);
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Member Kin Removed Successfully !", ""));

    }

    public void saveEditKin() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        kinList = new ArrayList<Kin>(editMember.getNextOfKin());
        kinList.remove(editKin);
        double totalPercentage = 0.0;
        for (Kin eKin : kinList) {
            totalPercentage = totalPercentage + eKin.getSharesPercentage();
        }

        if (checkFullName(editKin.getName()) == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid kin's name", ""));
        } else if (validatePercentage(editKin.getSharesPercentage()) == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid shares value", ""));
        } else if (editKin.getIdNumber().equalsIgnoreCase(editMember.getIdNumber())) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kin's ID Number cannot be same as member's ID Number.", ""));
        } else if (totalPercentage + editKin.getSharesPercentage() > 100) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Allocating this % exceeds the allowed 100% shares", ""));
        } else {

            allMemberKins.remove(editKin);
            if (!checkMemberKin(allMemberKins).equalsIgnoreCase("no")) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, checkMemberKin(allMemberKins), ""));
            } else {

                kinsFacade.edit(editKin);
                allMemberKins.add(editKin);
                editMember.setNextOfKin(allMemberKins);
                if (tellerLogged != null) {
                    editMember.setLastEditedBy(tellerLogged.getFullName() + "/" + tellerLogged.getTelephone());
                }
                memberFacade.edit(editMember);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kin edited successfully", ""));
            }
        }
    }

    public void transact(String subName, double debitCredit, String type) {
        Random randId = new Random();
        Long transId = randId.nextLong() + transactionFacade.findAll().size();
        if (type.equalsIgnoreCase("Charge")) {
            subAccount = subAccountsFacade.getSubAccByName(subName);
            subAccount.setBalance(subAccount.getBalance() + debitCredit);
            subAccountsFacade.edit(subAccount);
            subAccount = new SubAccounts();

            accounts = accountsFacade.getAccByName("Income");
            accounts.setBalance(accounts.getBalance() + debitCredit);
            accountsFacade.edit(accounts);
            accounts = new Accounts();

            subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
            subAccount.setBalance(subAccount.getBalance() + debitCredit);
            subAccountsFacade.edit(subAccount);
            subAccount = new SubAccounts();

            accounts = accountsFacade.getAccByName("Assets");
            accounts.setBalance(accounts.getBalance() + debitCredit);
            accountsFacade.edit(accounts);

            //start transactions
            transaction.setTransactionDate(newMember.getRegistrationDate());
            transaction.setDescription(subName + " from " + newMember.getFullName());
            transaction.setTransferAcc("Income:" + subName);
            transaction.setSubAccount("Cash in Bank");
            transaction.setAccountType("Assets");
            transaction.setCreator(tellerLogged.getFullName());
            transaction.setAmount(debitCredit);
            transaction.setDebit(debitCredit);
            transaction.setTransId(transId);
            transaction.setContributionID(contributionID);
            transactionFacade.create(transaction);
            transaction = new Transactions();

            transaction.setTransactionDate(newMember.getRegistrationDate());
            transaction.setDescription(subName + " from " + newMember.getFullName());
            transaction.setTransferAcc("Assets:" + "Cash in Bank");
            transaction.setSubAccount(subName);
            transaction.setAccountType("Income");
            transaction.setCreator(tellerLogged.getFullName());
            transaction.setAmount(debitCredit);
            transaction.setCredit(debitCredit);
            transaction.setTransId(transId);
            transaction.setContributionID(contributionID);
            transactionFacade.create(transaction);
            transaction = new Transactions();
        }

        if (type.equalsIgnoreCase("loan")) {
            subAccount = subAccountsFacade.getSubAccByName(subName);
            subAccount.setBalance(subAccount.getBalance() + loanInterest);
            subAccountsFacade.edit(subAccount);
            subAccount = new SubAccounts();

            accounts = accountsFacade.getAccByName("Income");
            accounts.setBalance(accounts.getBalance() + loanInterest);
            accountsFacade.edit(accounts);
            accounts = new Accounts();

            transaction.setTransactionDate(contribDate);
            transaction.setDescription("Loan Interest:" + dbMember.getFullName());
            transaction.setAccountType("Income");
            transaction.setTransferAcc("Assets : Cash in Bank");
            transaction.setSubAccount(subName);
            transaction.setCreator(tellerLogged.getFullName());
            transaction.setAmount(loanInterest);
            transaction.setCredit(loanInterest);
            transaction.setTransId(transId);
            transaction.setContributionID(contributionID);
            transactionFacade.create(transaction);
            transaction = new Transactions();


            subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
            subAccount.setBalance(subAccount.getBalance() + loanInterest);
            subAccountsFacade.edit(subAccount);
            subAccount = new SubAccounts();

            accounts = accountsFacade.getAccByName("Assets");
            accounts.setBalance(accounts.getBalance() + loanInterest);
            accountsFacade.edit(accounts);
            accounts = new Accounts();


            transaction.setTransactionDate(contribDate);
            transaction.setDescription("Loan Interest:" + dbMember.getFullName());
            transaction.setAccountType("Assets");
            transaction.setTransferAcc("Income : Loan Interest");
            transaction.setSubAccount("Cash in Bank");
            transaction.setCreator(tellerLogged.getFullName());
            transaction.setAmount(loanInterest);
            transaction.setDebit(loanInterest);
            transaction.setTransId(transId);
            transaction.setContributionID(contributionID);
            transactionFacade.create(transaction);
            transaction = new Transactions();
        }

        if (type.equalsIgnoreCase("Principal")) {

            subAccount = subAccountsFacade.getSubAccByName(subName);
            subAccount.setBalance(subAccount.getBalance() + loanPrincipal);
            subAccountsFacade.edit(subAccount);
            subAccount = new SubAccounts();



            transaction.setTransactionDate(contribDate);
            transaction.setDescription("Loan Principal:" + dbMember.getFullName());
            transaction.setAccountType("Assets");
            transaction.setTransferAcc("Assets : Loan to Members");
            transaction.setSubAccount(subName);
            transaction.setCreator(tellerLogged.getFullName());
            transaction.setAmount(loanPrincipal);
            transaction.setDebit(loanPrincipal);
            transaction.setTransId(transId);
            transaction.setContributionID(contributionID);
            transactionFacade.create(transaction);
            transaction = new Transactions();


            subAccount = subAccountsFacade.getSubAccByName("Loan to Members");
            subAccount.setBalance(subAccount.getBalance() - loanPrincipal);
            subAccountsFacade.edit(subAccount);
            subAccount = new SubAccounts();




            transaction.setTransactionDate(contribDate);
            transaction.setDescription("Loan Principal:" + dbMember.getFullName());
            transaction.setAccountType("Assets");
            transaction.setTransferAcc("Assets : Cash in Bank");
            transaction.setSubAccount("Cash in Bank");
            transaction.setCreator(tellerLogged.getFullName());
            transaction.setAmount(loanPrincipal);
            transaction.setCredit(loanPrincipal);
            transaction.setTransId(transId);
            transaction.setContributionID(contributionID);
            transactionFacade.create(transaction);
            transaction = new Transactions();

        }
        // End Transactions                   
        //contribution.setRegistrationFees(debitCredit); //TO BE REMOVEDUSER SHOULD ACCESS THIS VIA ACCOUNTS

        saccoAccount.setRegistrationFee(saccoAccount.getRegistrationFee() + debitCredit);
    }

    //Load member kin for editing
    public void loadMemberKin() {
        editKin = memberKinsDM.getRowData();
        allMemberKins = editMember.getNextOfKin();

    }

    //Save/Search edit referee
    public void searchReferee() {
        //  List<SaccoMember> searchMembers = new ArrayList<SaccoMember>();

        if (searchRefMember.trim().equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Search Criteria .", ""));
            refFound = false;

            refMember = new SaccoMember();
        } else {
            if (memberFacade.searchSaccoMember(searchRefMember) != null) {
                refMember = memberFacade.searchSaccoMember(searchRefMember);

                refFound = true;
                //searchMembers.add(dbMember);

            } else {
                refMember = new SaccoMember();
                refFound = false;
                //tellerMembersDM = new ListDataModel<SaccoMember>();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found !", ""));
            }
        }
    }

    public void saveEditReferee() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (checkFullName(editReferee.getName()) == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid referee name", ""));

        } else if (validateTel(editReferee.getTelephoneNumber()) == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid referee's phone number", ""));

        } else if (editReferee.getTelephoneNumber().equalsIgnoreCase(editMember.getPhoneNumber())) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Referee Telephone number cannot be same as member's telephone number!", ""));
        } else {

            allMemberReferees.remove(editReferee);

            if (!checkMemberReferee(allMemberReferees).equalsIgnoreCase("no")) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, checkMemberReferee(allMemberReferees), ""));
            } else {
                refFacade.edit(editReferee);
                allMemberReferees.add(editReferee);
                if (tellerLogged != null) {
                    editMember.setLastEditedBy(tellerLogged.getFullName() + "/" + tellerLogged.getTelephone());
                }
                editMember.setReferees(allMemberReferees);
                memberFacade.edit(editMember);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Referee edited successfully", ""));

            }

        }


    }

    //Load member referee for editing
    public void loadMemberRef() {
        editReferee = memberRefereesDM.getRowData();
        allMemberReferees = editMember.getReferees();


    }

    //Load member's business for editing
    public void loadMemberBusiness() {
        editBusiness = businessFacade.getBusinessByMemberNumber(editMember.getMemberNumber());
    }
    //Save Member business after editing

    public void saveEditMemberBusiness() {
        FacesContext ctx = FacesContext.getCurrentInstance();


        editBusiness.setLicensed(showLicensed);
        if (checkFullName(editBusiness.getContactPerson()) == false && editBusiness.isLicensed() == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid contact person's name", ""));
        } else if (validateTel(editBusiness.getTelephone()) == false && editBusiness.isLicensed() == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid telephone number", ""));
        } else if (editBusiness.getTelephone() != null && editBusiness.getTelephone().equalsIgnoreCase(editMember.getPhoneNumber()) && editBusiness.isLicensed() == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contact person's tel cannot be same as sacco member's", ""));
        } else {
            if (!editBusiness.isLicensed()) {
                editBusiness.setLicenseNumber(null);
                editBusiness.setYearOfLicense(null);
            } else if (editBusiness.isLicensed()) {
                editBusiness.setContactPerson(null);
                editBusiness.setBusinessName(null);
                editBusiness.setAddress(null);
                editBusiness.setTelephone(null);
                editBusiness.setBuilding(null);
                editBusiness.setNextBusiness(null);
            }
            if (tellerLogged != null) {
                editMember.setLastEditedBy(tellerLogged.getFullName() + "/" + tellerLogged.getTelephone());
            }
            businessFacade.edit(editBusiness);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Business details edited successfully", ""));
        }
    }

    //clear business edit dialog
    public void clearDialog() {
        editBusiness = new Business();
    }

    //Load member contribution
    public String memberContribution() {
        String page = "contributions.xhtml";
        unclearedLoans = new ArrayList<MemberLoan>();
        editMember = null;

        dbMember = tellerMembersDM.getRowData();
        dbMemberFound = true;

        for (MemberLoan loan : dbMember.getMemberLoans()) {

            if (Double.parseDouble(dFormat.format(loan.getBalance())) > 0.00) {
                if (loan.isApprovalStatus()) {
                    hasApprovedLoan = true;
                }
                unclearedLoans.add(loan);
            }
        }
        if (memberAccountFacade.getMemberAccount(dbMember.getMemberNumber()).getShares() == shareSettingsFacade.findAll().get(0).getMiminumShares()) {
            shareLimitReached = true;
        } else {
            shareLimitReached = false;
        }

        if (unclearedLoans.isEmpty()) {
            memberHasLoans = false;
            dbLoan = new MemberLoan();
        } else if (unclearedLoans.size() == 1) {
            memberHasLoans = true;
            dbLoan = unclearedLoans.get(0);
        } else {
            memberHasLoans = true;
        }

        dbAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());

        return page;
    }

    // View individual member
    public String viewMember() {
        String page = "indMember.xhtml";
        editMember = tellerMembersDM.getRowData();
        dbMember = null;
        if (editMember.getBranchId() != null) {
            CompanyBranches branchM = branchesFacade.find(editMember.getBranchId());
            if (branchM != null) {
                editMember.setBranch(branchM.getBranchname());
            }
        }
        
        loansGuaranteedDM = new ListDataModel<LoanGuarantors>(guarantorFacade.memberGuarantors(editMember.getMemberNumber()));

        allSaccoMembers = memberFacade.findAll();
        allSaccoMembers.remove(editMember);
        dbAccount = memberAccountFacade.getMemberAccount(editMember.getMemberNumber());

        return page;
    }
    
    // update loan number on guarantors 
    public void updateLoans(){
        LoanGuarantors dbGuarantor = new LoanGuarantors();
        for(MemberLoan mLoan : loanFacade.findAll()){
            if(!mLoan.getGuarantors().isEmpty()){
                for(LoanGuarantors lGuarantor : mLoan.getGuarantors()){
                    dbGuarantor = lGuarantor;
                    dbGuarantor.setLoanNumber(mLoan.getLoanNUmber());
                    guarantorFacade.edit(dbGuarantor);
                    System.out.println("update loan number ...  " + dbGuarantor.getLoanNumber());
                }
            }
        }
    }

    public String adminViewMember() {
        String page = "membersAdmin.xhtml";
        editMember = allMembersDM.getRowData();
        allSaccoMembers = memberFacade.findAll();
        allSaccoMembers.remove(editMember);
        dbAccount = memberAccountFacade.getMemberAccount(editMember.getMemberNumber());

        return page;
    }
    //Save edited member

    public void saveEditMember() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (checkFullName(editMember.getFullName()) == false) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid name", ""));

        } else if (editMember.getFullName().indexOf(" ") == -1) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter at least two names!", ""));
        } 
//        else if (validateTel(editMember.getPhoneNumber()) == false) {
////            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid telephone number", ""));
////
////        } 
//else if (editMember.getCounty().equalsIgnoreCase("select")) {
//            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select member's county", ""));
//        } 
//        else if (!checkEditMember(allSaccoMembers).equalsIgnoreCase("no")) {
//            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, checkEditMember(allSaccoMembers), ""));
//        } 
        
        else {
            if (tellerLogged != null) {
                editMember.setLastEditedBy(tellerLogged.getFullName() + "/" + tellerLogged.getTelephone());
            }


            memberFacade.edit(editMember);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Member details updated successfully!", ""));

        }
    }
    //check for duplicate kins

    public String checkMemberKin(List<Kin> editKins) {
        String exists = "no";
        for (Kin mKin : editKins) {
            if (mKin.getIdNumber().equalsIgnoreCase(editKin.getIdNumber())) {
                exists = "Operation aborted : Kin with that ID number exists!";
                break;
            }

        }

        return exists;
    }

    //Check for duplicate referees
    public String checkMemberReferee(List<Referee> editReferees) {
        String exists = "no";
        for (Referee mReferee : editReferees) {
            if (editReferee.getTelephoneNumber().equalsIgnoreCase(mReferee.getTelephoneNumber())) {
                exists = "Operation aborted : Referee with same telephone number exists";
                break;
            }

        }

        return exists;
    }

    //Check to see if another member exists with same credentials
    public String checkEditMember(List<SaccoMember> editMembers) {
        String exists = "no";
        for (SaccoMember sMember : editMembers) {
            if (editMember.getEmail().equalsIgnoreCase(sMember.getEmail())) {
                exists = "Member with that email address exists!";

                break;
            } else if (editMember.getPhoneNumber().equalsIgnoreCase(sMember.getPhoneNumber())) {
                exists = "Member with that phone number exists!";

                break;

            }
        }


        return exists;
    }

    /*----- members contributions start ---*/
    //search member using id or member number
    public void searchTellerMember() {
        List<SaccoMember> searchMembers = new ArrayList<SaccoMember>();

        if (searchAdminMember.trim().equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Search Criteria .", ""));
            tellerMemberFound = false;
            dbMemberFound = false;
            dbMember = new SaccoMember();
        } else {
            if (memberFacade.searchSaccoMember(searchAdminMember) != null) {
                dbMember = memberFacade.searchSaccoMember(searchAdminMember);

                tellerMemberFound = true;
                searchMembers.add(dbMember);

                dbMember = new SaccoMember();
                dbAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());
                tellerMembersDM = new ListDataModel<SaccoMember>(searchMembers);
            } else {
                dbMember = new SaccoMember();
                tellerMemberFound = false;
                tellerMembersDM = new ListDataModel<SaccoMember>();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found .", ""));
            }
        }
    }

    public void searchAdminMember() {


        List<SaccoMember> searchMembers = new ArrayList<SaccoMember>();
        if (searchAdminMember.trim().equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Search Criteria .", ""));
            adminMemberFound = false;
            dbMember = new SaccoMember();
        } else {

            if (memberFacade.searchSaccoMember(searchAdminMember) != null) {
                dbMember = memberFacade.searchSaccoMember(searchAdminMember);

                adminMemberFound = true;
                searchMembers.add(dbMember);

                dbMember = new SaccoMember();
                dbAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());
                allMembersDM = new ListDataModel<SaccoMember>(searchMembers);
            } else {
                dbMember = new SaccoMember();
                adminMemberFound = false;
                allMembersDM = new ListDataModel<SaccoMember>();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found.", ""));
            }
        }
    }

    public void searchMember() {

        unclearedLoans = new ArrayList<MemberLoan>();
        if (searchCriteria.trim().equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Search Criteria.", ""));
            dbMemberFound = false;
            receiverFound = false;
            dbMember = new SaccoMember();
        } else {
            if (memberFacade.searchSaccoMember(searchCriteria) != null) {
                dbMember = memberFacade.searchSaccoMember(searchCriteria);
                dbMemberFound = true;
                receiverFound = false;
                receiverMember = new SaccoMember();
                searchReceiver = new String();

                if (memberAccountFacade.getMemberAccount(dbMember.getMemberNumber()).getShares() >= shareSettingsFacade.findAll().get(0).getMiminumShares()) {
                    shareLimitReached = true;
                } else {
                    shareLimitReached = false;
                }

                for (MemberLoan loan : dbMember.getMemberLoans()) {
                    if (Double.valueOf(dFormat.format(loan.getBalance())) > 0.00 && loan.isApprovalStatus()) {
                        unclearedLoans.add(loan);
                    }
                }

                if (unclearedLoans.isEmpty()) {
                    memberHasLoans = false;
                    dbLoan = new MemberLoan();
                } else if (unclearedLoans.size() == 1) {
                    memberHasLoans = true;
                    dbLoan = unclearedLoans.get(0);
                } else {
                    memberHasLoans = true;
                }
                dbAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());
            } else {
                dbMember = new SaccoMember();
                dbMemberFound = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found.", ""));
            }
        }
    }

    //load member loan details on changing loan number
    public void onChangeLoanNumber() {

        if (!loanNumber.equalsIgnoreCase("select")) {
            dbLoan = loanFacade.findMemberLoan(loanNumber, dbMember.getMemberNumber());
            applicationFee = 0.0;
            loanInterest = 0.0;
            loanPrincipal = 0.0;
            serviceLoan = dbLoan.getMonthlyInstallment();
        } else {
            dbLoan = new MemberLoan();

        }

    }
    //Finish  and close  transaction

    public String finishContribution() {
        String page = "tellerIndex.xhtml";
        searchAdminMember = null;
        tellerMemberFound = false;

        tellerMembersDM = new ListDataModel<SaccoMember>();
        return page;
    }
    //validate contribution details

    public void prepareContributionAdd() {
        //Useful dont delete...
        FacesContext ctx = FacesContext.getCurrentInstance();

        loanSettings = loanSettingsFacade.getLoanType(dbLoan.getPurpose());

        System.out.println("Service loan ... " + serviceLoan);
        dataValid = false;
        transactionComplete = false;
        memberAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());

        if (contribDate == null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter contribution date", ""));
        } else if ((shares == 0 && deposits == 0) && serviceLoan == 0) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No transaction made. Reason : No contribution made.", ""));
        } else if (Double.isNaN(shares)) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid shares value.", ""));
        } else if (Double.isNaN(deposits)) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid deposits value.", ""));
        } else if (Double.isNaN(serviceLoan)) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid loan repayment value.", ""));
        } else if (shares > shareSettingsFacade.findAll().get(0).getMiminumShares()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Shares greater than allowed share amount of " + shareSettingsFacade.findAll().get(0).getMiminumShares(), ""));
        } else if (serviceLoan != 0) {
            if (serviceLoan > (dbLoan.getBalance() + (dbLoan.getMonthlyInstallment() - (dbLoan.getAppliedAmount() / dbLoan.getRepaymentPeriod()))) && (dbLoan.getApplicationFeePaid() == dbLoan.getApplicationFee())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loan service value entered exceeds loan balance", ""));
            } else if (serviceLoan > (dbLoan.getBalance() + loanSettings.getApplicationFee() * 0.01 * dbLoan.getApprovedAmount() + (dbLoan.getMonthlyInstallment() - (dbLoan.getAppliedAmount() / dbLoan.getRepaymentPeriod()))) && (dbLoan.getApplicationFeePaid() != dbLoan.getApplicationFee())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loan service value entered exceeds loan balance", ""));
            } else if (serviceLoan < Double.parseDouble(dFormat.format((dbLoan.getMonthlyInstallment() - (dbLoan.getAppliedAmount() / dbLoan.getRepaymentPeriod())))) && serviceLoan != 0) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Initial loan service paid must cover application fee and this month's interest", ""));
            } else if (serviceLoan < Double.parseDouble(dFormat.format((dbLoan.getMonthlyInstallment() - (dbLoan.getAppliedAmount() / dbLoan.getRepaymentPeriod())))) && serviceLoan != 0 && contributionFacade.checkMonthlyInterest(dbLoan.getLoanNUmber(), contribDate).isEmpty()) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loan service paid must cover this month's interest", ""));
            } else if (payFromDeposit && (memberAccount.getDeposit() - dbMember.getGuarantees()) < serviceLoan) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loan service entered exceeds members savings", ""));
            } else {
                dataValid = true;
            }
        } else {
            dataValid = true;
        }
    }
    //release Guarantors after loan repayment

    public void releaseGuarantors(double repayPrincipal) {
        List<LoanGuarantors> guarantors = new ArrayList<LoanGuarantors>();
        guarantors = dbLoan.getGuarantors();

        for (LoanGuarantors guarantor : guarantors) {
            SaccoMember gMember = memberFacade.searchSaccoMember(guarantor.getMemberNumber());
            double releaseRate = (repayPrincipal / dbLoan.getAppliedAmount()) * guarantor.getSavings();
            System.out.println("Release rate ->" + releaseRate);
            if (gMember.getMemberNumber().equalsIgnoreCase(dbMember.getMemberNumber())) {
                dbMember.setGuarantees(dbMember.getGuarantees() - releaseRate);
                if (dbMember.getGuarantees() < 0.01) {
                    dbMember.setGuarantees(0.00);
                }
                guarantor.setGuarantees(guarantor.getGuarantees() - releaseRate);

                if (guarantor.getGuarantees() < 0.01) {
                    guarantor.setGuarantees(0.00);
                }
                guarantorFacade.edit(guarantor);
                memberFacade.edit(dbMember);
            } else {
                gMember.setGuarantees(gMember.getGuarantees() - releaseRate);
                if (gMember.getGuarantees() < 0.01) {
                    gMember.setGuarantees(0.00);
                }
                guarantor.setGuarantees(guarantor.getGuarantees() - releaseRate);
                if (guarantor.getGuarantees() < 0.01) {
                    guarantor.setGuarantees(0.00);
                }
                guarantorFacade.edit(guarantor);
                memberFacade.edit(gMember);
            }
        }
    }

    /* Make bulk loan contribution
     * Changed By : George Gitere
     * Client : Agrimar Sacco
     * Date : 28-09-2015    
     */
    // global member search
    public void globalMemberSearch() {
        if (globalSearchCriteria.trim().equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Search Criteria .", ""));
            memberFound = false;
            dbMember = new SaccoMember();
        } else {
            if (memberFacade.searchSaccoMember(globalSearchCriteria) != null) {

                memberFound = true;
                dbMember = memberFacade.searchSaccoMember(globalSearchCriteria);
                unclearedLoans = new ArrayList<MemberLoan>();

                dbAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());

                for (MemberLoan loan : dbMember.getMemberLoans()) {
                    if (Double.valueOf(dFormat.format(loan.getBalance())) > 0.00 && loan.isApprovalStatus()) {
                        System.out.println("getting loan details ... " + loan.getLoanNUmber() + "  balance ... " + loan.getBalance());
                        unclearedLoans.add(loan);
                    }
                }

                if (unclearedLoans.isEmpty()) {
                    memberHasLoans = false;
                    dbLoan = new MemberLoan();
                } else if (unclearedLoans.size() == 1) {
                    memberHasLoans = true;
                    dbLoan = unclearedLoans.get(0);
                    serviceLoan = dbLoan.getMonthlyInstallment();
                } else {
                    memberHasLoans = true;
                }

            } else {
                dbMember = new SaccoMember();
                memberFound = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found .", ""));
            }
        }
    }

    public void onChangeServiceDates() {
        try {
            computeLoanInstallments();
            System.out.println("installments ... " + loanInstallments);
        } catch (Exception ex) {
        }

    }

    // calculate bulk loan service installments
    public int computeLoanInstallments() {
        loanInstallments = 0;
        int startYear = 0, endYear = 0, startMonth = 0, endMonth = 0;
        startYear = loanStartDate.getYear();
        endYear = loanEndDate.getYear();

        startMonth = loanStartDate.getMonth();
        endMonth = loanEndDate.getMonth();

        loanInstallments = (endYear - startYear) * 12 + (endMonth - startMonth) + 1;
        return loanInstallments;
    }

    // process bulk loan processing
    public void processBulkLoanService() {
        loanInstallments = 0;
        rContext = RequestContext.getCurrentInstance();
        ctx = FacesContext.getCurrentInstance();
        int startMonth = 0, startYear = 0;
        startMonth = loanStartDate.getMonth();
        startYear = loanStartDate.getYear();
        loanInstallments = computeLoanInstallments();
        System.out.println("loan installments ... " + loanInstallments);
        loanStartDate.setMonth(loanStartDate.getMonth() - 1);
        try {
            if (loanInstallments != 0) {
                for (int i = 0; i < loanInstallments; i++) {
                    loanStartDate.setMonth(loanStartDate.getMonth() + 1);
                    System.out.println("date ... " + loanStartDate);
                    serviceLoan(loanStartDate, serviceLoan);
                }
            }
            //print reciept
            if (printRecieptOK) {
                if (printReceipt() == 1) {
                    throw new PrinterException("Transaction complete but printing failed.");
                }
            }
            globalMemberSearch();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operation successful", ""));
        } catch (Exception ex) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", ""));
            ex.printStackTrace();
        }
        loanEndDate = null;
        loanStartDate = null;
        loanInstallments = 0;
        loanInterest = 0;
        loanPrincipal = 0;
        printRecieptOK = false;
        rContext.execute("bulkLoanServiceDLG.hide();");
    }

    // validate bulk loan service
    public boolean validateBulkLoanService() {
        validBulkLoan = false;
        ctx = FacesContext.getCurrentInstance();
        rContext = RequestContext.getCurrentInstance();
        if (loanStartDate == null || loanEndDate == null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start and end dates are mandatory fields", ""));
        } else if (loanStartDate.before(dbLoan.getApplicationDate())) {
        } else if (loanStartDate.after(loanEndDate)) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End date must be after start date", ""));
        } else if (computeLoanInstallments() > dbLoan.getRepaymentPeriod()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Installments exceeds loan's repayment period", ""));
        } else if (payFromDeposit && (dbAccount.getDeposit() < serviceLoan * loanInstallments)) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loan service amount exceeds member's savings", ""));
        } else {
            validBulkLoan = true;
        }

        rContext.execute("bulkLoanServiceDLG.show();");
        //System.out.println("validate status .... " + validBulkLoan);
        return validBulkLoan;
    }

    // loan processing
    public void serviceLoan(Date loanServiceDate, double loanService) {
        Random newRandom = new Random();
        ctx = FacesContext.getCurrentInstance();
        List<LoanSecurity> loanSecurities = new ArrayList<LoanSecurity>();

        contributionID = null;
        accDeposits = deposits;
        accShares = shares;
        contributionID = newRandom.nextLong() + contributionFacade.findAll().size();
        transactionComplete = false;
        queryDate = new Date();
        contribDate = loanServiceDate;
        serviceLoan = loanService;

        memberSettings = memberSettingsFacade.findAll().get(0);

        //loanSettings = loanSettingsFacade.findAll().get(0);
        memberAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());
        System.out.println("member account ... " + memberAccount.getMemberNumber());
        if (saccoAccountFacade.findAll().isEmpty()) {
            saccoAccount = new SaccoAccount();
        } else {
            saccoAccount = saccoAccountFacade.findAll().get(0);
        }

        if (validBulkLoan) {
            contribution.setMemberNumber(dbMember.getMemberNumber());
            try {
                List<Contribution> memberContributions = new ArrayList<Contribution>();
                //service loan
                if (dbLoan.getLoanNUmber() != null && serviceLoan != 0) {
                    contribution.setPreviousBalance(dbLoan.getBalance());
                    if (payFromDeposit) {
                        memberAccount.setDeposit(memberAccount.getDeposit() - serviceLoan);
                        memberAccountFacade.edit(memberAccount);
                        payFromDeposit = false;
                    }
                    //Check if has paid interest for that particular contrib date
                    if (contributionFacade.checkMonthlyInterest(dbLoan.getLoanNUmber(), contribDate).isEmpty()) {
                        //loanInterest = Double.parseDouble(dFormat.format(0.01 * loanSettings.getInterestRate() * (dbLoan.getBalance())));
                        loanInterest = Double.parseDouble(dFormat.format(dbLoan.getMonthlyInstallment() - (dbLoan.getAppliedAmount() / dbLoan.getRepaymentPeriod())));

                        loanPrincipal = serviceLoan - loanInterest;
                        System.out.println("Total Interest -->" + dbLoan.getInterest() + "Loan Interest ->" + loanInterest + "Loan principal -->" + loanPrincipal + " applied amount ->" + dbLoan.getAppliedAmount());
                        dbLoan.setPrincipalPaid(dbLoan.getPrincipalPaid() + loanPrincipal);
                        dbLoan.setInterestPaid(dbLoan.getInterestPaid() + loanInterest);
                        dbLoan.setBalance(Double.parseDouble(dFormat.format(dbLoan.getBalance() - serviceLoan)));
                    } else {
                        loanInterest = 0;
                        loanPrincipal = serviceLoan;
                        dbLoan.setPrincipalPaid(dbLoan.getPrincipalPaid() + loanPrincipal);
                        dbLoan.setBalance(Double.parseDouble(dFormat.format(dbLoan.getBalance() - loanPrincipal)));
                    }
                    contribution.setShares(accShares);
                    contribution.setDeposit(accDeposits);
                    contribution.setServedBy(tellerLogged.getFullName());

                    contribution.setLoanInterest(loanInterest);
                    contribution.setLoanPrincipal(loanPrincipal);
                    contribution.setLoanNumber(dbLoan.getLoanNUmber());

                    // ACCOUNTING
                    if (loanInterest != 0) {
                        transact("Loan Interest", loanInterest, "Loan");
                    }

                    if (loanPrincipal != 0) {
                        transact("Cash in Bank", loanPrincipal, "Principal");
                    }
                    if (dbLoan.getBalance() < 0.01) {
                        loanSecurities = dbLoan.getSecurities();
                        dbLoan.setStatus("cleared");
                        dbLoan.setBalance(0.00);
                        for (LoanSecurity loanSec : loanSecurities) {
                            loanSec.setReleased(true);
                            loanSecFacade.edit(loanSec);
                        }
                    }
                    releaseGuarantors(loanPrincipal);
                    //dbLoan.setStatus("Ongoing");
                    loanFacade.edit(dbLoan);
                }
                //sacco account
                //bf and deposits

                if (contributionFacade.checkMemberContribute(dbMember.getMemberNumber(), contribDate).isEmpty()) {
                    contribution.setBenevolent(memberSettings.getBenevolentFee());
//               System.out.println("Contribution date =>" + contribDate);
//               System.out.println("pay from deposits" + payFromDeposit);

                    if (deposits == 0 && payFromDeposit == false) {
                        //System.out.println("am here1....");

                        accShares = shares - memberSettings.getBenevolentFee();
                        saccoAccount.setBenevolentFee(saccoAccount.getBenevolentFee() + memberSettings.getBenevolentFee());
                        memberAccount.setShares(memberAccount.getShares() + accShares);

                    } else if (deposits < memberSettings.getBenevolentFee() && deposits != 0 && payFromDeposit == false) {
//                   System.out.println("am here2....");

                        saccoAccount.setBenevolentFee(saccoAccount.getBenevolentFee() + memberSettings.getBenevolentFee());
                        memberAccount.setDeposit(memberAccount.getDeposit() - (memberSettings.getBenevolentFee() - deposits));
                    } else {
                        if (payFromDeposit == false) {

                            accDeposits = deposits - memberSettings.getBenevolentFee();
                            saccoAccount.setBenevolentFee(saccoAccount.getBenevolentFee() + memberSettings.getBenevolentFee());
                            saccoAccount.setDeposits(saccoAccount.getDeposits() + accDeposits);
                            memberAccount.setDeposit(memberAccount.getDeposit() + accDeposits);
                            memberAccount.setShares(memberAccount.getShares() + accShares);

                        }
                    }
                } else {

                    memberAccount.setDeposit(memberAccount.getDeposit() + accDeposits);
                    memberAccount.setShares(memberAccount.getShares() + accShares);
                    saccoAccount.setDeposits(saccoAccount.getDeposits() + accDeposits);
                    //  ....
                    contribution.setBenevolent(0);

//               System.out.println("am here...." + contribution.getBenevolent());
                }

                //interests
                saccoAccount.setInterests(saccoAccount.getInterests() + loanInterest);

                //application fee
                saccoAccount.setLoanApplicationFee(saccoAccount.getLoanApplicationFee() + applicationFee);

                //shares 
                saccoAccount.setShares(saccoAccount.getShares() + accShares);

                saccoAccountFacade.edit(saccoAccount);
                contribution.setContributionDate(contribDate);
                contribution.setShares(accShares);
                contribution.setDeposit(accDeposits);
                contribution.setServedBy(tellerLogged.getFullName());

                contribution.setTotal(accShares + accDeposits + contribution.getBenevolent() + serviceLoan);

                emptyList = contributionFacade.checkMemberContribute(dbMember.getMemberNumber(), contribDate).isEmpty();

                contribution.setId(contributionID);
                contributionFacade.create(contribution);

                memberContributions = dbMember.getContributions();

                memberContributions.add(contribution);

                dbMember.setContributions(memberContributions);
                memberFacade.edit(dbMember);

                memberAccountFacade.edit(memberAccount);

                //Accounting
                accounts = new Accounts();
                subAccount = new SubAccounts();

                Random randId = new Random();
                Long transId = randId.nextLong() + transactionFacade.findAll().size();

                if (accShares != 0) {
                    subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                    subAccount.setBalance(subAccount.getBalance() + accShares);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Assets");
                    accounts.setBalance(accounts.getBalance() + accShares);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Shares contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Assets");
                    transaction.setSubAccount("Cash in Bank");
                    transaction.setTransferAcc("Equity : Share Capital");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accShares);
                    transaction.setDebit(accShares);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);
                    transaction = new Transactions();

                    subAccount = subAccountsFacade.getSubAccByName("Share Capital");
                    subAccount.setBalance(subAccount.getBalance() + accShares);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Equity");
                    accounts.setBalance(accounts.getBalance() + accShares);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Shares contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Equity");
                    transaction.setSubAccount("Share Capital");
                    transaction.setTransferAcc("Assets: Cash in Bank");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accShares);
                    transaction.setCredit(accShares);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);


                    transaction = new Transactions();

                }

                if (accDeposits != 0) {
                    subAccount = subAccountsFacade.getSubAccByName("Deposits");
                    subAccount.setBalance(subAccount.getBalance() + accDeposits);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Liabilities");
                    accounts.setBalance(accounts.getBalance() + accDeposits);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Deposits contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Liabilities");
                    transaction.setSubAccount("Deposits");
                    transaction.setTransferAcc("Assets : Cash in Bank");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accDeposits);
                    transaction.setCredit(accDeposits);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);

                    subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                    subAccount.setBalance(subAccount.getBalance() + accDeposits);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Assets");
                    accounts.setBalance(accounts.getBalance() + accDeposits);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Deposits contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Assets");
                    transaction.setSubAccount("Cash in Bank");
                    transaction.setTransferAcc("Liabilities : Deposits");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accDeposits);
                    transaction.setDebit(accDeposits);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);
                    transaction = new Transactions();


                }

                if (contribution.getBenevolent() != 0) {
                    double benevolent = memberSettings.getBenevolentFee();
                    subAccount = subAccountsFacade.getSubAccByName("Benevolent");
                    subAccount.setBalance(subAccount.getBalance() + benevolent);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Liabilities");
                    accounts.setBalance(accounts.getBalance() + benevolent);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Benevolent contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Liabilities");
                    transaction.setSubAccount("Benevolent");
                    transaction.setTransferAcc("Assets : Cash in Bank");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(benevolent);
                    transaction.setCredit(benevolent);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);

                    subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                    subAccount.setBalance(subAccount.getBalance() + benevolent);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Assets");
                    accounts.setBalance(accounts.getBalance() + benevolent);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Benevolent contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Assets");
                    transaction.setSubAccount("Cash in Bank");
                    transaction.setTransferAcc("Liabilities : Benevolent");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(benevolent);
                    transaction.setDebit(benevolent);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);
                    transaction = new Transactions();
                }

                //print reciept
                //if (printRecieptOK) {
                //    if (printReceipt() == 1) {
                //        throw new PrinterException("Transaction complete but printing failed.");
                //     }
                // }
                // ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction successful.", ""));

            } catch (Exception ex) {
                transactionComplete = true;
                if (ex instanceof PrinterException) {
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), ""));
                } else {

                    ex.printStackTrace();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaction failed.", ""));
                }
            }
        }
    }
    public void sendContributionSMS(SaccoMember member){
        
    }

    public void newMemberContribution() {
        Random newRandom = new Random();
        contributionID = null;
        accDeposits = deposits;
        accShares = shares;
        //contributionID = Long.valueOf(String.valueOf(contributionFacade.findAll().size() + 1));
        contributionID = newRandom.nextLong() + contributionFacade.findAll().size();
        List<LoanSecurity> loanSecurities = new ArrayList<LoanSecurity>();
        transactionComplete = false;
        queryDate = new Date();
        FacesContext ctx = FacesContext.getCurrentInstance();
        memberSettings = memberSettingsFacade.findAll().get(0);
        //loanSettings = loanSettingsFacade.findAll().get(0);
        memberAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());
        if (saccoAccountFacade.findAll().isEmpty()) {
            saccoAccount = new SaccoAccount();
        } else {
            saccoAccount = saccoAccountFacade.findAll().get(0);
        }

        if (dataValid = true) {
            contribution.setMemberNumber(dbMember.getMemberNumber());
            try {
                List<Contribution> memberContributions = new ArrayList<Contribution>();
                //service loan
                if (dbLoan.getLoanNUmber() != null && serviceLoan != 0) {
                    contribution.setPreviousBalance(dbLoan.getBalance());
                    if (payFromDeposit) {
                        memberAccount.setDeposit(memberAccount.getDeposit() - serviceLoan);
                        memberAccountFacade.edit(memberAccount);
                        payFromDeposit = false;
                    }
                    //Check if has paid interest for that particular contrib date
                    if (contributionFacade.checkMonthlyInterest(dbLoan.getLoanNUmber(), contribDate).isEmpty()) {
                        //loanInterest = Double.parseDouble(dFormat.format(0.01 * loanSettings.getInterestRate() * (dbLoan.getBalance())));
                        loanInterest = Double.parseDouble(dFormat.format(dbLoan.getMonthlyInstallment() - (dbLoan.getAppliedAmount() / dbLoan.getRepaymentPeriod())));

                        loanPrincipal = serviceLoan - loanInterest;
                        System.out.println("Total Interest -->" + dbLoan.getInterest() + "Loan Interest ->" + loanInterest + "Loan principal -->" + loanPrincipal + " applied amount ->" + dbLoan.getAppliedAmount());
                        dbLoan.setPrincipalPaid(dbLoan.getPrincipalPaid() + loanPrincipal);
                        dbLoan.setInterestPaid(dbLoan.getInterestPaid() + loanInterest);
                        dbLoan.setBalance(Double.parseDouble(dFormat.format(dbLoan.getBalance() - serviceLoan)));
                    } else {
                        loanInterest = 0;
                        loanPrincipal = serviceLoan;
                        dbLoan.setPrincipalPaid(dbLoan.getPrincipalPaid() + loanPrincipal);
                        dbLoan.setBalance(Double.parseDouble(dFormat.format(dbLoan.getBalance() - loanPrincipal)));
                    }
                    contribution.setShares(accShares);
                    contribution.setDeposit(accDeposits);
                    contribution.setServedBy(tellerLogged.getFullName());

                    contribution.setLoanInterest(loanInterest);
                    contribution.setLoanPrincipal(loanPrincipal);
                    contribution.setLoanNumber(dbLoan.getLoanNUmber());

                    // ACCOUNTING
                    if (loanInterest != 0) {
                        transact("Loan Interest", loanInterest, "Loan");
                    }

                    if (loanPrincipal != 0) {
                        transact("Cash in Bank", loanPrincipal, "Principal");
                    }
                    if (dbLoan.getBalance() < 0.01) {
                        loanSecurities = dbLoan.getSecurities();
                        dbLoan.setStatus("cleared");
                        dbLoan.setBalance(0.00);
                        for (LoanSecurity loanSec : loanSecurities) {
                            loanSec.setReleased(true);
                            loanSecFacade.edit(loanSec);
                        }
                    }
                    releaseGuarantors(loanPrincipal);
                    //dbLoan.setStatus("Ongoing");
                    loanFacade.edit(dbLoan);
                }
                //sacco account
                //bf and deposits

                if (contributionFacade.checkMemberContribute(dbMember.getMemberNumber(), contribDate).isEmpty()) {
                    contribution.setBenevolent(memberSettings.getBenevolentFee());
//               System.out.println("Contribution date =>" + contribDate);
//               System.out.println("pay from deposits" + payFromDeposit);

                    if (deposits == 0 && payFromDeposit == false) {
                        //System.out.println("am here1....");

                        accShares = shares - memberSettings.getBenevolentFee();
                        saccoAccount.setBenevolentFee(saccoAccount.getBenevolentFee() + memberSettings.getBenevolentFee());
                        memberAccount.setShares(memberAccount.getShares() + accShares);

                    } else if (deposits < memberSettings.getBenevolentFee() && deposits != 0 && payFromDeposit == false) {
//                   System.out.println("am here2....");

                        saccoAccount.setBenevolentFee(saccoAccount.getBenevolentFee() + memberSettings.getBenevolentFee());
                        memberAccount.setDeposit(memberAccount.getDeposit() - (memberSettings.getBenevolentFee() - deposits));
                    } else {
                        if (payFromDeposit == false) {

                            accDeposits = deposits - memberSettings.getBenevolentFee();
                            saccoAccount.setBenevolentFee(saccoAccount.getBenevolentFee() + memberSettings.getBenevolentFee());
                            saccoAccount.setDeposits(saccoAccount.getDeposits() + accDeposits);
                            memberAccount.setDeposit(memberAccount.getDeposit() + accDeposits);
                            memberAccount.setShares(memberAccount.getShares() + accShares);

                        }
                    }
                } else {

                    memberAccount.setDeposit(memberAccount.getDeposit() + accDeposits);
                    memberAccount.setShares(memberAccount.getShares() + accShares);
                    saccoAccount.setDeposits(saccoAccount.getDeposits() + accDeposits);
                    //  ....
                    contribution.setBenevolent(0);

//               System.out.println("am here...." + contribution.getBenevolent());
                }

                //interests
                saccoAccount.setInterests(saccoAccount.getInterests() + loanInterest);

                //application fee
                saccoAccount.setLoanApplicationFee(saccoAccount.getLoanApplicationFee() + applicationFee);

                //shares 
                saccoAccount.setShares(saccoAccount.getShares() + accShares);

                saccoAccountFacade.edit(saccoAccount);
                contribution.setContributionDate(contribDate);
                contribution.setShares(accShares);
                contribution.setDeposit(accDeposits);
                contribution.setServedBy(tellerLogged.getFullName());


                contribution.setTotal(accShares + accDeposits + contribution.getBenevolent() + serviceLoan);

                emptyList = contributionFacade.checkMemberContribute(dbMember.getMemberNumber(), contribDate).isEmpty();

                contribution.setId(contributionID);
                contributionFacade.create(contribution);

                memberContributions = dbMember.getContributions();

                memberContributions.add(contribution);

                dbMember.setContributions(memberContributions);
                memberFacade.edit(dbMember);

                memberAccountFacade.edit(memberAccount);
                saccoDetails = saccoDetails = detailsFacade.findAll().get(0);;
                smsHandler.sendMemberContributionSMS(dbMember, saccoDetails, contribution);
                //Accounting
                accounts = new Accounts();
                subAccount = new SubAccounts();

                Random randId = new Random();
                Long transId = randId.nextLong() + transactionFacade.findAll().size();

                if (accShares != 0) {
                    subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                    subAccount.setBalance(subAccount.getBalance() + accShares);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Assets");
                    accounts.setBalance(accounts.getBalance() + accShares);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Shares contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Assets");
                    transaction.setSubAccount("Cash in Bank");
                    transaction.setTransferAcc("Equity : Share Capital");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accShares);
                    transaction.setDebit(accShares);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);
                    transaction = new Transactions();

                    subAccount = subAccountsFacade.getSubAccByName("Share Capital");
                    subAccount.setBalance(subAccount.getBalance() + accShares);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Equity");
                    accounts.setBalance(accounts.getBalance() + accShares);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Shares contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Equity");
                    transaction.setSubAccount("Share Capital");
                    transaction.setTransferAcc("Assets: Cash in Bank");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accShares);
                    transaction.setCredit(accShares);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);


                    transaction = new Transactions();

                }

                if (accDeposits != 0) {
                    subAccount = subAccountsFacade.getSubAccByName("Deposits");
                    subAccount.setBalance(subAccount.getBalance() + accDeposits);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Liabilities");
                    accounts.setBalance(accounts.getBalance() + accDeposits);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Deposits contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Liabilities");
                    transaction.setSubAccount("Deposits");
                    transaction.setTransferAcc("Assets : Cash in Bank");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accDeposits);
                    transaction.setCredit(accDeposits);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);

                    subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                    subAccount.setBalance(subAccount.getBalance() + accDeposits);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Assets");
                    accounts.setBalance(accounts.getBalance() + accDeposits);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Deposits contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Assets");
                    transaction.setSubAccount("Cash in Bank");
                    transaction.setTransferAcc("Liabilities : Deposits");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(accDeposits);
                    transaction.setDebit(accDeposits);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);
                    transaction = new Transactions();


                }

                if (contribution.getBenevolent() != 0) {
                    double benevolent = memberSettings.getBenevolentFee();
                    subAccount = subAccountsFacade.getSubAccByName("Benevolent");
                    subAccount.setBalance(subAccount.getBalance() + benevolent);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Liabilities");
                    accounts.setBalance(accounts.getBalance() + benevolent);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Benevolent contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Liabilities");
                    transaction.setSubAccount("Benevolent");
                    transaction.setTransferAcc("Assets : Cash in Bank");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(benevolent);
                    transaction.setCredit(benevolent);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);

                    subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                    subAccount.setBalance(subAccount.getBalance() + benevolent);
                    subAccountsFacade.edit(subAccount);
                    subAccount = new SubAccounts();

                    accounts = accountsFacade.getAccByName("Assets");
                    accounts.setBalance(accounts.getBalance() + benevolent);
                    accountsFacade.edit(accounts);
                    accounts = new Accounts();

                    transaction = new Transactions();
                    transaction.setTransactionDate(contribDate);
                    transaction.setDescription("Benevolent contribution :" + dbMember.getFullName());
                    transaction.setAccountType("Assets");
                    transaction.setSubAccount("Cash in Bank");
                    transaction.setTransferAcc("Liabilities : Benevolent");
                    transaction.setCreator(tellerLogged.getFullName());
                    transaction.setAmount(benevolent);
                    transaction.setDebit(benevolent);
                    transaction.setTransId(transId);
                    transaction.setContributionID(contributionID);
                    transactionFacade.create(transaction);
                    transaction = new Transactions();
                }


                //print reciept
                if (printRecieptOK) {
                    if (printReceipt() == 1) {
                        throw new PrinterException("Transaction complete but printing failed.");
                    }
                }
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction successful.", ""));

            } catch (Exception ex) {
                transactionComplete = true;
                if (ex instanceof PrinterException) {
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), ""));
                } else {

                    ex.printStackTrace();
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaction failed.", ""));
                }

            }


            shares = 0.0;
            deposits = 0.0;

            dbMemberFound = false;
            printContribution = false;
            searchCriteria = null;
            dbMember = new SaccoMember();
            dbLoan = new MemberLoan();

            applicationFee = 0.0;
            loanInterest = 0.0;
            loanPrincipal = 0.0;
            contribDate = null;
            printRecieptOK = false;
            contributionID = null;

            serviceLoan = 0.0;

            memberAccount = new MemberAccount();
            saccoAccount = new SaccoAccount();
            contribution = new Contribution();
            transactionComplete = true;
            loanNumber = new String();
            payFromDeposit = false;
        }
    }

    //automated deduction of BF if a member does not contribute in a month x of year x
    public void autoBFDeduction() {
        Random randId = new Random();
        Long transId = randId.nextLong() + transactionFacade.findAll().size();

        if (!contributionFacade.findAll().isEmpty()) {


            Date lastContributionDate = contributionFacade.findAll().get(contributionFacade.findAll().size() - 1).getContributionDate();
            queryMonth = new Date();

            if (queryMonth.getYear() >= lastContributionDate.getYear() && (queryMonth.getMonth() - lastContributionDate.getMonth() > 0)) {


                List<Contribution> monthlyContributions = new ArrayList<Contribution>();
                List<SaccoMember> allMembers = new ArrayList<SaccoMember>();
                memberSettings = memberSettingsFacade.findAll().get(0);


                queryMonth.setMonth(queryMonth.getMonth() - 1);

                monthlyContributions = contributionFacade.monthlyContributions(queryMonth);
                allMembers = memberFacade.findAll();



                for (SaccoMember member : allMembers) {
                    if (member.isMemberStatus()) {
                        if (contributionFacade.checkMemberContribute(member.getMemberNumber(), queryMonth).isEmpty()) {


                            MemberAccount memberAcc = new MemberAccount();
                            SaccoAccount saccoAcc = new SaccoAccount();


                            if (saccoAccountFacade.findAll().isEmpty()) {

                                saccoAcc = new SaccoAccount();
                            } else {
                                saccoAcc = saccoAccountFacade.findAll().get(0);


                            }

                            contribution = new Contribution();

                            contribution.setMemberNumber(member.getMemberNumber());
                            contribution.setApplicationFee(0);
                            contribution.setContributionDate(queryMonth);
                            contribution.setDeposit(0);
                            contribution.setShares(0);
                            contribution.setLoanInterest(0);
                            contribution.setLoanPrincipal(0);
                            contribution.setLoanNumber(null);
                            contribution.setBenevolent(memberSettings.getBenevolentFee());
                            contribution.setTotal(memberSettings.getBenevolentFee());
                            contribution.setServedBy("Auto");


                            contributionFacade.create(contribution);

                            subAccount = subAccountsFacade.getSubAccByName("Benevolent");
                            subAccount.setBalance(subAccount.getBalance() + memberSettings.getBenevolentFee());
                            subAccountsFacade.edit(subAccount);
                            subAccount = new SubAccounts();


                            accounts = accountsFacade.getAccByName("Liabilities");
                            accounts.setBalance(accounts.getBalance() + memberSettings.getBenevolentFee());
                            accountsFacade.edit(accounts);
                            accounts = new Accounts();


                            transaction = new Transactions();
                            transaction.setTransactionDate(queryMonth);
                            transaction.setDescription("Benevolent contribution :" + member.getMemberNumber());
                            transaction.setAccountType("Liabilities");
                            transaction.setSubAccount("Benevolent");
                            transaction.setTransferAcc("Assets : Cash in Bank");
                            transaction.setCreator(tellerLogged.getFullName());
                            transaction.setAmount(memberSettings.getBenevolentFee());
                            transaction.setCredit(memberSettings.getBenevolentFee());
                            transaction.setTransId(transId);
                            transactionFacade.create(transaction);

                            subAccount = subAccountsFacade.getSubAccByName("Cash in Bank");
                            subAccount.setBalance(subAccount.getBalance() + memberSettings.getBenevolentFee());
                            subAccountsFacade.edit(subAccount);
                            subAccount = new SubAccounts();

                            accounts = accountsFacade.getAccByName("Assets");
                            accounts.setBalance(accounts.getBalance() + memberSettings.getBenevolentFee());
                            accountsFacade.edit(accounts);
                            accounts = new Accounts();

                            transaction = new Transactions();
                            transaction.setTransactionDate(contribDate);
                            transaction.setDescription("Benevolent contribution :" + dbMember.getFullName());
                            transaction.setAccountType("Assets");
                            transaction.setSubAccount("Cash in Bank");
                            transaction.setTransferAcc("Liabilities : Benevolent");
                            transaction.setCreator(tellerLogged.getFullName());
                            transaction.setAmount(memberSettings.getBenevolentFee());
                            transaction.setDebit(memberSettings.getBenevolentFee());
                            transaction.setTransId(transId);
                            transactionFacade.create(transaction);
                            transaction = new Transactions();


                            subAccount = subAccountsFacade.getSubAccByName("Deposits");
                            subAccount.setBalance(subAccount.getBalance() - memberSettings.getBenevolentFee());
                            subAccountsFacade.edit(subAccount);
                            subAccount = new SubAccounts();

                            accounts = accountsFacade.getAccByName("Liabilities");
                            accounts.setBalance(accounts.getBalance() - memberSettings.getBenevolentFee());
                            accountsFacade.edit(accounts);
                            accounts = new Accounts();

                            transaction.setTransactionDate(contribDate);
                            transaction.setDescription("Benevolent contribution :" + dbMember.getFullName());
                            transaction.setAccountType("Liabilities");
                            transaction.setSubAccount("Deposits");
                            transaction.setTransferAcc("Liabilities : Benevolent");
                            transaction.setCreator(tellerLogged.getFullName());
                            transaction.setAmount(memberSettings.getBenevolentFee());
                            transaction.setDebit(memberSettings.getBenevolentFee());
                            transaction.setTransId(transId);
                            transactionFacade.create(transaction);
                            transaction = new Transactions();

                            contribution = new Contribution();

                            contribution.setMemberNumber(member.getMemberNumber());
                            contribution.setApplicationFee(0);
                            contribution.setContributionDate(queryMonth);
                            contribution.setDeposit(0);
                            contribution.setShares(0);
                            contribution.setLoanInterest(0);
                            contribution.setLoanPrincipal(0);
                            contribution.setLoanNumber(null);
                            contribution.setBenevolent(memberSettings.getBenevolentFee());
                            contribution.setTotal(memberSettings.getBenevolentFee());
                            contribution.setServedBy("Auto");


                            contributionFacade.create(contribution);

                            memberAcc = memberAccountFacade.getMemberAccount(member.getMemberNumber());

                            memberAcc.setDeposit(memberAcc.getDeposit() - memberSettings.getBenevolentFee());
                            if (memberAcc.getDeposit() == 0) {
                                member.setMemberStatus(false);
                            }
                            memberFacade.edit(member);
                            memberAccountFacade.edit(memberAcc);

                            saccoAcc.setBenevolentFee(saccoAcc.getBenevolentFee() + memberSettings.getBenevolentFee());
                            saccoAccountFacade.edit(saccoAcc);
                        }
                    }
                }
            }

            contribution = new Contribution();
        }

    }

    // Print Methods
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        //To change body of generated methods, choose Tools | Templates.
        saccoDetails = detailsFacade.findAll().get(0);
        memberSettings = memberSettingsFacade.findAll().get(0);
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy : hh:mm aaa");
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */

        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Arial", Font.PLAIN, 10);
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setFont(font);


        if (printRecieptOK) {

            g.drawString("   " + saccoDetails.getName(), 5, 10);

            g.drawString("Name:     " + dbMember.getFullName(), 5, 25);
            g.drawString("Member No: " + dbMember.getMemberNumber(), 5, 40);
            g.drawString("Date:      " + dateFormat.format(new Date()), 5, 55);
            g.drawString("---------------------------------------------------------------", 5, 70);

            i = 65;
            if (!(shares == 0 && deposits == 0)) {

                g.drawString("SHARES AND DEPOSIT CONTRIBUTION", 5, 85);
                g.drawString("---------------------------------------------------------------", 5, 100);
                if (emptyList && deposits == 0) // If no deposits have been made, cut benevolent from shares
                {
                    g.drawString("Deposits", 5, 115);
                    g.drawString(dFormat.format(accDeposits) + "", 110, 115);
                    g.drawString("Shares", 5, 130);
                    g.drawString(dFormat.format(accShares) + "", 110, 130);
                    g.drawString("Benevolent", 5, 145);
                    g.drawString(dFormat.format(memberSettings.getBenevolentFee()) + "", 110, 145);
                    g.drawString("---------------------------------------------------------------", 5, 160);
                    i = 160;
                } else if (emptyList && deposits != 0) { // If  deposits have been made, cut benevolent from deposits
                    g.drawString("Depostis", 5, 115);
                    g.drawString(dFormat.format(accDeposits) + "", 110, 115);
                    g.drawString("Shares", 5, 130);
                    g.drawString(dFormat.format(accShares) + "", 110, 130);
                    g.drawString("Benevolent", 5, 145);
                    g.drawString(dFormat.format(memberSettings.getBenevolentFee()) + "", 110, 145);
                    g.drawString("---------------------------------------------------------------", 5, 160);
                    i = 160;
                } else {

                    g.drawString("Depostis", 5, 115);
                    g.drawString(dFormat.format(accDeposits) + "", 110, 115);
                    g.drawString("Shares", 5, 130);
                    g.drawString(dFormat.format(accShares) + "", 110, 130);
                    g.drawString("---------------------------------------------------------------", 5, 145);
                    i = 145;
                }
            }

            if (dbLoan.getLoanNUmber() != null && serviceLoan != 0) {
                if (!validBulkLoan) {
                    g.drawString("LOAN REPAYMENT " + "- " + dbLoan.getPurpose().toUpperCase() + "", 5, i += 15);
                    g.drawString("-----------------------------------------------------------", 5, i += 15);
                    g.drawString("Loan Number    " + dbLoan.getLoanNUmber(), 5, i += 15);

                    if (loanPrincipal != 0) {
                        g.drawString("Principal", 5, i += 15);
                        g.drawString(dFormat.format((loanPrincipal)) + "", 110, i);
                    }
                    if (loanInterest != 0) {
                        g.drawString("Interest", 5, i += 15);
                        g.drawString(dFormat.format(loanInterest) + "", 110, i);
                    }
                    if (applicationFee != 0) {
                        g.drawString("Application Fee", 5, i += 15);
                        g.drawString(dFormat.format(applicationFee) + "", 110, i);
                    }
                    if (insuranceFee != 0) {
                        g.drawString("Insurance Fee", 5, i += 15);
                        g.drawString(dFormat.format(insuranceFee) + "", 110, i);
                    }
                    g.drawString("-----------------------------------------------------------", 5, i += 15);

                    g.drawString("TOTAL (Kshs.)", 5, i += 15);
                    g.drawString(dFormat.format(serviceLoan) + "", 110, i);
                } else {
                    g.drawString("LOAN REPAYMENT " + "- " + dbLoan.getPurpose().toUpperCase() + "", 5, i += 15);
                    g.drawString("-----------------------------------------------------------", 5, i += 15);
                    g.drawString("Loan Number    " + dbLoan.getLoanNUmber(), 5, i += 15);

                    if (loanPrincipal != 0) {
                        g.drawString("Principal", 5, i += 15);
                        g.drawString(dFormat.format((loanPrincipal) * loanInstallments) + "", 110, i);
                    }
                    if (loanInterest != 0) {
                        g.drawString("Interest", 5, i += 15);
                        g.drawString(dFormat.format((loanInterest) * loanInstallments) + "", 110, i);
                    }
                    if (applicationFee != 0) {
                        g.drawString("Application Fee", 5, i += 15);
                        g.drawString(dFormat.format(applicationFee) + "", 110, i);
                    }
                    if (insuranceFee != 0) {
                        g.drawString("Insurance Fee", 5, i += 15);
                        g.drawString(dFormat.format(insuranceFee) + "", 110, i);
                    }

                    if (loanInstallments != 0) {
                        g.drawString("Installments", 5, i += 15);
                        g.drawString(loanInstallments + "", 110, i);
                    }
                    g.drawString("-----------------------------------------------------------", 5, i += 15);

                    g.drawString("TOTAL (Kshs.)", 5, i += 15);
                    g.drawString(dFormat.format(serviceLoan * loanInstallments) + "", 110, i);
                }
            }


            g.drawString("Loan Balance", 5, i += 15);
            g.drawString(dFormat.format(dbLoan.getBalance()) + "", 110, i);

            g.drawString("-----------------------------------------------------------", 5, i += 15);
            g.drawString("Served By : " + contribution.getServedBy(), 5, i += 15);
            g.drawString("Thank you.", 5, i += 15);
        }

        if (printNewMember) {
            i = 10;
            g.drawString(saccoDetails.getName(), 5, i += 15);
            g.drawString("Name:      " + newMember.getFullName(), 5, i += 15);
            g.drawString("Member No: " + newMember.getMemberNumber(), 5, i += 15);
            g.drawString("Date:      " + dateFormat.format(new Date()), 5, i += 15);
            g.drawString("---------------------------------------------------------------", 5, i += 15);
            g.drawString("SHARES AND DEPOSIT", 5, i += 15);
            g.drawString("---------------------------------------------------------------", 5, i += 15);
            g.drawString("Deposits                " + memberAccount.getDeposit(), 5, i += 15);
            g.drawString("Shares                  " + memberAccount.getShares(), 5, i += 15);


            g.drawString("---------------------------------------------------------------", 5, i += 15);
            g.drawString("MEMBERSHIP CHARGES", 5, i += 15);
            g.drawString("---------------------------------------------------------------", 5, i += 15);

            for (Charges mCharge : membershipCharges) {
                g.drawString(mCharge.getName() + "            " + mCharge.getCost(), 5, i += 15);
            }

            g.drawString("---------------------------------------------------------------", 5, i += 15);
            g.drawString("Served By " + contribution.getServedBy(), 5, i += 15);
            g.drawString("Thank you.", 5, i += 15);
        }


        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;

    }

    //printing on a thermal printer....
    public int printReceipt() throws PrinterException {

        int exitCode = 0;

        try {
            double paperWidth = 3.25, paperHeight = 11.69, leftMargin = 0.19, rightMargin = 0.19, topMargin = 0, bottomMargin = 0.01;


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
                    break;
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

    /*----- members contributions end ---*/
    /*---- transfer shares start */
    //find shares receiver
    public void findReceiver() {
        FacesContext message = FacesContext.getCurrentInstance();
        if (!searchReceiver.equalsIgnoreCase("")) {
            if (memberFacade.searchSaccoMember(searchReceiver) != null) {
                receiverMember = memberFacade.searchSaccoMember(searchReceiver);
                if (receiverMember.getMemberNumber().equalsIgnoreCase(dbMember.getMemberNumber())) {
                    message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "cannot transfer to the same member", ""));
                    receiverMember = new SaccoMember();
                    receiverFound = false;
                } else {
                    receiverFound = true;
                }
            } else {
                receiverMember = new SaccoMember();
                receiverFound = false;
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found", ""));
            }
        } else {
            receiverMember = new SaccoMember();
            receiverFound = false;
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter member number, id, or tel", ""));
        }
    }

    //validate transfer
    public void validateTransfer() {
        transferSuccess = false;
        validTransfer = false;
        FacesContext message = FacesContext.getCurrentInstance();
        if (receiverMember.getMemberNumber() == null) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the receiving member", ""));
        } else if (transfer.getAmount() <= 0) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter transfer amount", ""));
        } else if (transfer.getAmount() > memberAccountFacade.getMemberAccount(dbMember.getMemberNumber()).getShares()) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "amount greater than shares available for transfer", ""));
        } else if (transfer.getTransferReasons().equalsIgnoreCase("select")) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select transfer reason.", ""));
        } else {
            validTransfer = true;
        }
    }

    //Activate Members
    public void activateMemberAccount() {
        editMember = allMembersDM.getRowData();
        FacesContext ctx = FacesContext.getCurrentInstance();
        try {
            if (editMember.isMemberStatus()) {
                editMember.setMemberStatus(false);

                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Suspended", ""));
            } else {
                editMember.setMemberStatus(true);

                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Activated.", ""));
            }
            if (tellerLogged != null) {
                editMember.setSuspendActivatedBy(tellerLogged.getFullName() + "/" + tellerLogged.getTelephone());
            }
            memberFacade.edit(editMember);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        editMember = new SaccoMember();
    }

    //complete transfer
    public void completeTransfer() {

        FacesContext message = FacesContext.getCurrentInstance();
        if (validTransfer) {
            memberAccount = new MemberAccount();
            transfer.setTransferDate(new Date());
            transfer.setFromNumber(dbMember.getMemberNumber());
            transfer.setToNumber(receiverMember.getMemberNumber());
            transfer.setServedBy(tellerLogged.getFullName());
            try {

                transferFacade.create(transfer);

                memberAccount = memberAccountFacade.getMemberAccount(dbMember.getMemberNumber());

                memberAccount.setShares(memberAccount.getShares() - transfer.getAmount());

                memberAccountFacade.edit(memberAccount);

                memberAccount = new MemberAccount();

                memberAccount = memberAccountFacade.getMemberAccount(receiverMember.getMemberNumber());

                memberAccount.setShares(memberAccount.getShares() + transfer.getAmount());

                memberAccountFacade.edit(memberAccount);




                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transfer complete", ""));

            } catch (Exception ex) {

                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transfer failed", ""));
            }

            transfer = new SharesTransfer();
            receiverMember = new SaccoMember();
            dbMember = new SaccoMember();
            memberAccount = new MemberAccount();
            transferSuccess = true;
            searchCriteria = new String();
            searchReceiver = new String();

            dbMemberFound = false;
            receiverFound = false;


        }


    }

    //cancel transaction
    public void cancelTransaction() {
        transfer = new SharesTransfer();
        receiverFound = false;
        receiverMember = new SaccoMember();
        dbMember = new SaccoMember();
        dbMemberFound = false;
        validTransfer = false;
        searchCriteria = new String();
        searchReceiver = new String();
    }

    /*---- transfer shares end */
    public String finishMemberReg() {
        addMemberSuccess = false;
        newMember = new SaccoMember();
        return "tellerIndex.xhtml";
    }

    public String wizardListener(FlowEvent event) {
        if (tellerLogged.getUserDomain().equalsIgnoreCase("DEVELOPER")) {
            newMember.setMemberPhoto("/images/addPhoto.jpg");
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        String step = "";

        String message = memberFacade.checkMemberExists(newMember);

        if (!message.equalsIgnoreCase("no") && event.getNewStep().equalsIgnoreCase("step2")) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
            step = event.getOldStep();
        } else if (newMember.getGender().equalsIgnoreCase("select") && event.getNewStep().equalsIgnoreCase("step2")) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Gender", ""));
            step = event.getOldStep();
        } else if (checkFullName(newMember.getFullName()) == false && event.getNewStep().equalsIgnoreCase("step2")) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid full name", ""));
            step = event.getOldStep();
        } else if (validateID(newMember.getIdNumber()) == false && event.getNewStep().equalsIgnoreCase("step2")) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid id number", ""));
            step = event.getOldStep();
        } else if (memberFacade.searchSaccoMember(newMember.getIdNumber()) != null && event.getNewStep().equalsIgnoreCase("step2")) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member with that id exists !", ""));
            step = event.getOldStep();
        } //          else if(validateTel(newMember.getPhoneNumber())==false && event.getNewStep().equalsIgnoreCase("step2"))
        //        {
        //            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid phone number", ""));
        //            step = event.getOldStep();
        //        }
        //          else if((new Date().getYear() - newMember.getDateOfBirth().getYear()) >100){
        //              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member age age exceeds the allowed limit.!", ""));
        //             step = event.getOldStep();
        //          }
//        else if (newMember.getMemberPhoto() == null) {
//            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload or take photo!", ""));
//            step = event.getOldStep();
//        } 
        else if (memberInt.trim().equalsIgnoreCase("")) {

            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter member number!", ""));
//             try{
//                 
//             }
//             catch()
            step = event.getOldStep();
        } else {
            step = event.getNewStep();
        }

        //go to step 3
        if (event.getNewStep().equalsIgnoreCase("step3")) {
            if (newMember.getBranchId() == null) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select Branch !", ""));
                step = event.getOldStep();
            } else {
                step = event.getNewStep();
                if (membershipCharges.size() < 1) {
                    membershipCharges = chargesFacade.getMandatoryCharges();
                }


                chargesDM = new ListDataModel<Charges>(membershipCharges);
            }

        }

        return step;
    }

    //upload member signature
    public void uploadSignature(FileUploadEvent event) {
        FacesContext message = FacesContext.getCurrentInstance();
        String path = new String();
        Properties sysProps = System.getProperties();

        try {
            String s = "p p";
            String fineName = "";
            String signatureName = event.getFile().getFileName();

            fineName = newMember.getMemberNumber().toString().replace("/", "_") + signatureName.substring(signatureName.lastIndexOf("."));

            File signDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberSignatures");


            if (signDirectory.exists()) {
                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberSignatures" + sysProps.getProperty("file.separator") + fineName;
            } else {
                signDirectory.mkdirs();

                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberSignatures" + sysProps.getProperty("file.separator") + fineName;
            }

            if (newMember.getMemberSignature() != null) {
                File previousSignature = new File(newMember.getMemberSignature());

                previousSignature.delete();

            }

            File signatureFile = new File(path);
            signatureFile.createNewFile();
            if (signatureFile.exists()) {

                FileOutputStream outputStream = new FileOutputStream(signatureFile);
                outputStream.write(event.getFile().getContents());
                outputStream.flush();
                outputStream.close();
                newMember.setMemberSignature(path);

                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Photo Uploaded Successfully !", ""));

            }


        } catch (Exception ex) {
        }

    }

    public void prepareUploadDLG() {
        // Bind Member Int dont delete
        showButton = false;
        memberInt = memberInt.toUpperCase();

        newMember.setMemberNumber(getNewMemberNumber() + getMemberInt());
        if (memberInt.trim().equalsIgnoreCase("")) {
            System.out.println("empty string");
            showButton = false;
        } else {
            int a = 0;
            boolean validNumber = true;
            for (int i = 0; i < memberInt.length(); i++) {
                if (Character.isLetter(memberInt.charAt(i))) {
                    a = a + 1;
                }
            }

            for (int i = 0; i < memberInt.length(); i++) {
                if (!(Character.isDigit(memberInt.charAt(i)) || (memberInt.endsWith("F") || memberInt.endsWith("M")))) {
                    validNumber = false;
                }
            }


            if (!(Character.isDigit(memberInt.charAt(0))) || a > 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid member number. Example 1F, 1M or 1", null));
                showButton = false;
            } else if (validNumber == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid member number. Example 1F, 1M or 1", null));
                showButton = false;
            } else if (a == 1 && !(memberInt.endsWith("F") || memberInt.endsWith("M"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid member number. Example 1F, 1M or 1", null));
                showButton = false;
            } else if (!memberFacade.checkMemberExists(newMember).equalsIgnoreCase("no")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, memberFacade.checkMemberExists(newMember), null));
                showButton = false;
            } else {
                showButton = true;
            }
        }
    }

    public void loadEmployeeInfo() {

        payRollNumber = editMember.getPayRollNumber();
        System.out.println("Loaded employee info" + payRollNumber);

    }

    public void saveEmployeeInfo() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        
            memberFacade.edit(editMember);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Member details updated successfully ! ", ""));
        

    }

    public void editMemberId(FileUploadEvent event) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        try {
            System.out.println("Uploading .." + event.getFile().getFileName());
            String path = "";
            Properties sysProps = System.getProperties();

            String fineName = "";
            String fileName = event.getFile().getFileName();

            fineName = "image_" + (new Random().nextInt(10000)) + fileName.substring(fileName.lastIndexOf("."));


            File photoDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos");
            if (photoDirectory.exists()) {
                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            } else {
                photoDirectory.mkdirs();

                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            }

            File[] allFiles = photoDirectory.listFiles();
            for (File tempFile : allFiles) {
                tempFile.delete();
            }

            File memberFile = new File(path);
            memberFile.createNewFile();

            if (memberFile.exists()) {
                InputStream inputstream = event.getFile().getInputstream();
                FileOutputStream outputStream = new FileOutputStream(memberFile);
                int read = 0;
                int i = (int) event.getFile().getSize();
                memberIdBytes = new byte[i];
                while ((read = inputstream.read(memberIdBytes)) != -1) {
                    outputStream.write(memberIdBytes, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                //DB Blob starts here
                //Serializable data = bytes;


                editMember.setMemberId(path);
                editMember.setMemberScanID(memberIdBytes);
                memberFacade.edit(editMember);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Photo Updated Successfully !", ""));

                memberIdBytes = new byte[0];

                // System.out.println("Image path -->" + tempPath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could Not Upload Photo !", ""));

        }



    }

    public void editMemberPhoto(FileUploadEvent event) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        try {
            System.out.println("Uploading .." + event.getFile().getFileName());
            String path = "";
            Properties sysProps = System.getProperties();

            String fineName = "";
            String fileName = event.getFile().getFileName();

            fineName = "image_" + (new Random().nextInt(10000)) + fileName.substring(fileName.lastIndexOf("."));


            File photoDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos");
            if (photoDirectory.exists()) {
                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            } else {
                photoDirectory.mkdirs();

                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            }

            File[] allFiles = photoDirectory.listFiles();
            for (File tempFile : allFiles) {
                tempFile.delete();
            }

            File memberFile = new File(path);
            memberFile.createNewFile();

            if (memberFile.exists()) {
                InputStream inputstream = event.getFile().getInputstream();
                FileOutputStream outputStream = new FileOutputStream(memberFile);
                int read = 0;
                int i = (int) event.getFile().getSize();
                memberPhotoBytes = new byte[i];
                while ((read = inputstream.read(memberPhotoBytes)) != -1) {
                    outputStream.write(memberPhotoBytes, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                //DB Blob starts here
                //Serializable data = bytes;


                editMember.setMemberPhoto(path);
                editMember.setMemberImage(memberPhotoBytes);
                memberFacade.edit(editMember);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ID Updated Successfully !", ""));

                memberPhotoBytes = new byte[0];

                // System.out.println("Image path -->" + tempPath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could Not Upload ID !", ""));

        }



    }

    public void uploadMemberId(FileUploadEvent event) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        try {
            System.out.println("Uploading .." + event.getFile().getFileName());
            String path = "";
            Properties sysProps = System.getProperties();

            String fineName = "";
            String fileName = event.getFile().getFileName();

            fineName = "image_" + (new Random().nextInt(10000)) + fileName.substring(fileName.lastIndexOf("."));


            File photoDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos");
            if (photoDirectory.exists()) {
                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            } else {
                photoDirectory.mkdirs();

                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            }

            File[] allFiles = photoDirectory.listFiles();
            for (File tempFile : allFiles) {
                tempFile.delete();
            }

            File memberFile = new File(path);
            memberFile.createNewFile();

            if (memberFile.exists()) {
                InputStream inputstream = event.getFile().getInputstream();
                FileOutputStream outputStream = new FileOutputStream(memberFile);
                int read = 0;
                int i = (int) event.getFile().getSize();
                memberIdBytes = new byte[i];
                while ((read = inputstream.read(memberIdBytes)) != -1) {
                    outputStream.write(memberIdBytes, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                //DB Blob starts here
                //Serializable data = bytes;


                newMember.setMemberId(path);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ID Uploaded Successfully !", ""));



                // System.out.println("Image path -->" + tempPath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could Not Upload ID !", ""));

        }


    }

    public void uploadMemberPhoto(FileUploadEvent event) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        try {
            System.out.println("Uploading .." + event.getFile().getFileName());
            String path = "";
            Properties sysProps = System.getProperties();

            String fineName = "";
            String fileName = event.getFile().getFileName();

            fineName = "image_" + (new Random().nextInt(10000)) + fileName.substring(fileName.lastIndexOf("."));


            File photoDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos");
            if (photoDirectory.exists()) {
                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            } else {
                photoDirectory.mkdirs();

                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + fineName;
            }

            File[] allFiles = photoDirectory.listFiles();
            for (File tempFile : allFiles) {
                tempFile.delete();
            }

            File memberFile = new File(path);
            memberFile.createNewFile();

            if (memberFile.exists()) {
                InputStream inputstream = event.getFile().getInputstream();
                FileOutputStream outputStream = new FileOutputStream(memberFile);
                int read = 0;
                int i = (int) event.getFile().getSize();
                memberPhotoBytes = new byte[i];
                while ((read = inputstream.read(memberPhotoBytes)) != -1) {
                    outputStream.write(memberPhotoBytes, 0, read);
                }
                outputStream.flush();
                outputStream.close();

                newMember.setMemberPhoto(path);
                //DB Blob starts here
                //Serializable data = bytes;



                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Document Uploaded Successfully !", ""));



                // System.out.println("Image path -->" + tempPath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could Not Upload ID !", ""));

        }

    }

    //capture photo from webcam
    public void onCapture(CaptureEvent captureEvent) {
        String path = "";
        FacesContext ctx = FacesContext.getCurrentInstance();
        Properties sysProps = System.getProperties();
        String photo = newMember.getMemberNumber().toString().replace("/", "_");

        byte[] data = captureEvent.getData();



        File photoDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos");
        if (photoDirectory.exists()) {
            path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + photo + ".png";
        } else {
            photoDirectory.mkdirs();

            path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + "MemberPhotos" + sysProps.getProperty("file.separator") + photo + ".png";
        }



        if (newMember.getMemberPhoto() != null) {
            File previousPhoto = new File(newMember.getMemberPhoto());
            previousPhoto.delete();

        }
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Image Captured successively", ""));
            newMember.setMemberPhoto(path);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in writing captured image.", ""));
            throw new FacesException("Error in writing captured image.");

        }
    }

    // add member refereed to list
    public void addReferee() {
        Random refId = new Random();

        FacesContext ctx = FacesContext.getCurrentInstance();

        referee.setId(refId.nextLong());
        referee.setName(refMember.getFullName());
        referee.setAddress(refMember.getHomeAddress());
        referee.setTelephoneNumber(refMember.getPhoneNumber());
        referee.setMemberNumber(refMember.getMemberNumber());
        referee.setBranch(refMember.getBranch());

        if (tempReferees.isEmpty()) {
            if (checkFullName(referee.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's name", null));
            } else if (validateTel(referee.getTelephoneNumber()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's telephone", null));
            } else if (referee.getTelephoneNumber().equalsIgnoreCase(newMember.getPhoneNumber())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member cannon be a referee to him/her self", null));
            } else {
                tempReferees.add(referee);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Referee added", "added"));
                referee = new Referee();
            }
        } else {
            boolean exists = false;
            for (Referee testRef : tempReferees) {
                if (referee.getMemberNumber().equalsIgnoreCase(testRef.getMemberNumber()) || referee.getTelephoneNumber().equalsIgnoreCase(testRef.getTelephoneNumber())) {
                    exists = true;
                }

            }

            if (checkFullName(referee.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's name", null));
            } else if (validateTel(referee.getTelephoneNumber()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's telephone", null));
            } else if (exists) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Referee with such details already added", "error"));
            } else if (referee.getTelephoneNumber().equalsIgnoreCase(newMember.getPhoneNumber())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member cannon be a referee to him/her self", null));
            } else {
                tempReferees.add(referee);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Referee added", "added"));
                referee = new Referee();
            }



        }
        searchRefMember = null;
        refFound = false;
        refMember = new SaccoMember();
        tempRefereesDM = new ListDataModel<Referee>(tempReferees);
    }

    public void clearReferee() {
        referee = new Referee();
    }

    //remove referee from list
    public void removeReferee() {

        referee = tempRefereesDM.getRowData();

        tempReferees.remove(referee);

        tempRefereesDM = new ListDataModel<Referee>(tempReferees);
    }

    //add kin to kin list
    public void addKin() {
        Random kinId = new Random();

        FacesContext ctx = FacesContext.getCurrentInstance();

        String message = null;
        double totalPercentage = 0.0;



        kin.setId(kinId.nextLong());

        if (kinList.isEmpty()) {
            if (checkFullName(kin.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid kin name", ""));
            } 
//            else if (kin.getSharesPercentage() == 0) {
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "shares % should be greater than 0", ""));
//            } 
//            else if (validatePercentage(kin.getSharesPercentage()) == false) {
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Percentage value should be btw 0 and 100", ""));
//            }
            else {
                kinList.add(kin);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kin Added to list", ""));
                kin = new Kin();
            }
        } else {

            for (Kin testKin : kinList) {
                totalPercentage = totalPercentage + testKin.getSharesPercentage();
                if (kin.getName().equalsIgnoreCase(testKin.getName())) {
                    message = "A kin with this name already added";
                }

            }


            if (message != null) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
            } else if (checkFullName(kin.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid kin name", ""));
            } else if (validateID(kin.getIdNumber()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid id number", ""));
            } else if (kin.getSharesPercentage() == 0) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "shares % should be greater than 0", ""));
            } else if (totalPercentage + kin.getSharesPercentage() > 100) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "This allocation will exceed 100% shares", ""));
            } else if (validatePercentage(kin.getSharesPercentage()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Percentage value should be btw 0 and 100", ""));
            } else if (kin.getIdNumber().equalsIgnoreCase(newMember.getIdNumber())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member cannot be his/her next of kin", ""));
            } else {
                kinList.add(kin);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kin Added to list", ""));
                kin = new Kin();
            }
        }

        kinsDM = new ListDataModel<Kin>(kinList);

    }

    //remove kin from list
    public void removeKin() {
        kin = kinsDM.getRowData();

        kinList.remove(kin);

        kinsDM = new ListDataModel<Kin>(kinList);
    }

    //clear kin details on closing dialog
    public void clearKin() {
        kin = new Kin();
    }

    public void removeRefereeFromMember() {
        ctx = FacesContext.getCurrentInstance();
        try {
            Referee refDel = memberRefereesDM.getRowData();
            List<Referee> refList = editMember.getReferees();
            refList.remove(refDel);
            editMember.setReferees(refList);
            memberFacade.edit(editMember);

            refFacade.remove(refDel);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Referee removed successfully !", ""));
        } catch (Exception ex) {
        }

    }

    public void addRefToMember() {

        ctx = FacesContext.getCurrentInstance();
        tempReferees = editMember.getReferees();

        referee.setName(refMember.getFullName());
        referee.setAddress(refMember.getHomeAddress());
        referee.setTelephoneNumber(refMember.getPhoneNumber());
        referee.setMemberNumber(refMember.getMemberNumber());
        referee.setBranch(refMember.getBranch());

        if (tempReferees.isEmpty()) {
            if (checkFullName(referee.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's name", null));
            } else if (validateTel(referee.getTelephoneNumber()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's telephone", null));
            } else if (referee.getTelephoneNumber().equalsIgnoreCase(newMember.getPhoneNumber())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member cannot be a referee to him/her self", null));
            } else {
                try {
                    refFacade.create(referee);

                    tempReferees.add(referee);

                    editMember.setReferees(tempReferees);

                    memberFacade.edit(editMember);


                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Referee added successfully !", "added"));
                    referee = new Referee();
                } catch (Exception ex) {
                }
                tempReferees = new ArrayList<Referee>();
            }
        } else {
            boolean exists = false;
            for (Referee testRef : tempReferees) {
                if (referee.getMemberNumber().equalsIgnoreCase(testRef.getMemberNumber()) || referee.getTelephoneNumber().equalsIgnoreCase(testRef.getTelephoneNumber())) {
                    exists = true;
                }

            }

            if (checkFullName(referee.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's name", null));
            } else if (validateTel(referee.getTelephoneNumber()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid referee's telephone", null));
            } else if (exists) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Referee with such details already added", "error"));
            } else if (referee.getTelephoneNumber().equalsIgnoreCase(newMember.getPhoneNumber())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member cannon be a referee to him/her self", null));
            } else {
                try {
                    refFacade.create(referee);

                    tempReferees.add(referee);

                    editMember.setReferees(tempReferees);

                    memberFacade.edit(editMember);


                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Referee added successfully !", "added"));
                    referee = new Referee();
                } catch (Exception ex) {
                }
            }



        }
        searchRefMember = null;
        refFound = false;
        refMember = new SaccoMember();
        tempRefereesDM = new ListDataModel<Referee>(tempReferees);
    }

    public void addKinToMember() {



        ctx = FacesContext.getCurrentInstance();

        String message = null;
        double totalPercentage = 0.0;

        kinList = editMember.getNextOfKin();


        if (kinList.isEmpty()) {
            if (checkFullName(kin.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid kin name", ""));
            }
//            } else if (kin.getSharesPercentage() == 0) {
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "shares % should be greater than 0", ""));
//            } else if (validatePercentage(kin.getSharesPercentage()) == false) {
//                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Percentage value should be btw 0 and 100", ""));
//            } 
            else {
                kinList.add(kin);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kin Added to list", ""));
                kin = new Kin();
            }
        } else {

            for (Kin testKin : kinList) {
                totalPercentage = totalPercentage + testKin.getSharesPercentage();
                if (kin.getName().equalsIgnoreCase(testKin.getName())) {
                    message = "A kin with this name already added";
                }

            }


            if (message != null) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
            } else if (checkFullName(kin.getName()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid kin name", ""));
            } else if (validateID(kin.getIdNumber()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid id number", ""));
            } else if (kin.getSharesPercentage() == 0) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "shares % should be greater than 0", ""));
            } else if (totalPercentage + kin.getSharesPercentage() > 100) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "This allocation will exceed 100% shares", ""));
            } else if (validatePercentage(kin.getSharesPercentage()) == false) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Percentage value should be btw 0 and 100", ""));
            } else if (kin.getIdNumber().equalsIgnoreCase(newMember.getIdNumber())) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member cannot be his/her next of kin", ""));
            } else {
                kinsFacade.create(kin);
                kinList.add(kin);
                editMember.setNextOfKin(kinList);
                memberFacade.edit(editMember);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kin Added Successfully", ""));
                kin = new Kin();
            }
        }



    }

    //load membership charges
    public void addMembershipCharges() {
        FacesContext ctx = FacesContext.getCurrentInstance();




        if (chargeId == null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select charges to be added", ""));
        } else {
            charge = chargesFacade.find(chargeId);
            String message = null;

            if (membershipCharges.isEmpty()) {
                membershipCharges.add(charge);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Charges added to list", ""));
                charge = new Charges();
                chargesDM = new ListDataModel<Charges>(membershipCharges);

            } else {
                for (Charges testCharge : membershipCharges) {
                    if (charge.getId().equals(testCharge.getId())) {
                        message = "Charge already added to list";
                    }

                }

                if (message != null) {
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
                } else {
                    membershipCharges.add(charge);
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Charges added to list", ""));
                    charge = new Charges();
                    chargesDM = new ListDataModel<Charges>(membershipCharges);



                }
            }


        }

        showchargesPG = false;
    }

    //remove membership charges
    public void removeCharges() {
        selectedCharge = chargesDM.getRowData();

        membershipCharges.remove(selectedCharge);

        chargesDM = new ListDataModel<Charges>(membershipCharges);


    }

    //select charges
    public void onSelectCharges() {
        if (chargeId != null) {
            selectedCharge = chargesFacade.find(chargeId);
            showchargesPG = true;
        } else {
            showchargesPG = false;
            selectedCharge = new Charges();
        }


    }

    //getters and setters
    public DataModel<SharesTransfer> getTransfersDM() {

        dbTransfers = new ArrayList<SharesTransfer>(transferFacade.findAll());
        transfersDM = new ListDataModel<SharesTransfer>(dbTransfers);

        return transfersDM;
    }

    public void setTransfersDM(DataModel<SharesTransfer> transfersDM) {
        this.transfersDM = transfersDM;
    }

    public MemberAccount getmemberAccount(String memberNumber) {
        MemberAccount dbmemberAccount = memberAccountFacade.getMemberAccount(memberNumber);

        return dbmemberAccount;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getDeposits() {
        return deposits;
    }

    public void setDeposits(double deposits) {
        this.deposits = deposits;
    }

    public double getLoanPrincipal() {
        return loanPrincipal;
    }

    public void setLoanPrincipal(double loanPrincipal) {
        this.loanPrincipal = loanPrincipal;
    }

    public double getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(double loanInterest) {
        this.loanInterest = loanInterest;
    }

    public SaccoMember getNewMember() {
        return newMember;
    }

    public void setNewMember(SaccoMember newMember) {
        this.newMember = newMember;
    }

    public SaccoMember getDbMember() {
        return dbMember;
    }

    public void setDbMember(SaccoMember dbMember) {
        this.dbMember = dbMember;
    }

    public SaccoMember getEditMember() {
        return editMember;
    }

    public void setEditMember(SaccoMember editMember) {
        this.editMember = editMember;
    }

    public SaccoMember getReceiverMember() {
        return receiverMember;
    }

    public void setReceiverMember(SaccoMember receiverMember) {
        this.receiverMember = receiverMember;
    }

    public SharesTransfer getTransfer() {
        return transfer;
    }

    public void setTransfer(SharesTransfer transfer) {
        this.transfer = transfer;
    }

    public MemberAccount getDbAccount() {
        return dbAccount;
    }

    public void setDbAccount(MemberAccount dbAccount) {
        this.dbAccount = dbAccount;
    }

    public Business getMemberBusiness() {
        memberBusiness = businessFacade.getBusinessByMemberNumber(editMember.getMemberNumber());
        return memberBusiness;
    }

    public void setMemberBusiness(Business memberBusiness) {
        this.memberBusiness = memberBusiness;
    }

    public Business getEditBusiness() {
        return editBusiness;
    }

    public void setEditBusiness(Business editBusiness) {
        this.editBusiness = editBusiness;
    }

    public Referee getEditReferee() {
        return editReferee;
    }

    public void setEditReferee(Referee editReferee) {
        this.editReferee = editReferee;
    }

    public SaccoMember getRefMember() {
        return refMember;
    }

    public void setRefMember(SaccoMember refMember) {
        this.refMember = refMember;
    }

    public List<Users> getSystemUsers() {
        systemUsers = new ArrayList<Users>(usersFacde.findAll());
        return systemUsers;
    }

    public void setSystemUsers(List<Users> systemUsers) {
        this.systemUsers = systemUsers;
    }

    public Users getTransactionUser() {
        return transactionUser;
    }

    public void setTransactionUser(Users transactionUser) {
        this.transactionUser = transactionUser;
    }

    public SaccoMember getTransactionMember() {
        return transactionMember;
    }

    public void setTransactionMember(SaccoMember transactionMember) {
        this.transactionMember = transactionMember;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Kin getKin() {
        return kin;
    }

    public void setKin(Kin kin) {
        this.kin = kin;
    }

    public Kin getEditKin() {
        return editKin;
    }

    public void setEditKin(Kin editKin) {
        this.editKin = editKin;
    }

    public Charges getSelectedCharge() {
        return selectedCharge;
    }

    public void setSelectedCharge(Charges selectedCharge) {
        this.selectedCharge = selectedCharge;
    }

    public String getNewMemberNumber() {
        saccoDetails = detailsFacade.findAll().get(0);
        String[] names = saccoDetails.getName().split(" ");
        String initials = "";
        for (String name : names) {
            initials += name.charAt(0);
        }
        //    newMemberNumber = initials+"/"+memberFacade.generateMemberNumber();
        newMemberNumber = initials + "/";
        // newMember.setMemberNumber(newMemberNumber);
        return newMemberNumber;
    }

    public void setNewMemberNumber(String newMemberNumber) {
        this.newMemberNumber = newMemberNumber;
    }

    public String getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public String getSearchRefMember() {
        return searchRefMember;
    }

    public void setSearchRefMember(String searchRefMember) {
        this.searchRefMember = searchRefMember;
    }

    public Long getBranchRepID() {
        return branchRepID;
    }

    public void setBranchRepID(Long branchRepID) {
        this.branchRepID = branchRepID;
    }

    public boolean isRefFound() {
        return refFound;
    }

    public void setRefFound(boolean refFound) {
        this.refFound = refFound;
    }

    public boolean isShowLicensed() {
        return showLicensed;
    }

    public boolean isPayFromDeposit() {
        return payFromDeposit;
    }

    public void setPayFromDeposit(boolean payFromDeposit) {
        this.payFromDeposit = payFromDeposit;
    }

    public boolean isPrintRecieptOK() {
        return printRecieptOK;
    }

    public void setPrintRecieptOK(boolean printRecieptOK) {
        this.printRecieptOK = printRecieptOK;
    }

    public void setShowLicensed(boolean showLicensed) {
        this.showLicensed = showLicensed;
    }

    public boolean isShowchargesPG() {
        return showchargesPG;
    }

    public void setShowchargesPG(boolean showchargesPG) {
        this.showchargesPG = showchargesPG;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    public String getMemberPhoto() {
        return memberPhoto;
    }

    public void setMemberPhoto(String memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    public Long getChargeId() {
        return chargeId;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getSearchReceiver() {
        return searchReceiver;
    }

    public void setSearchReceiver(String searchReceiver) {
        this.searchReceiver = searchReceiver;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    public boolean isAddMemberSuccess() {
        return addMemberSuccess;
    }

    public void setAddMemberSuccess(boolean addMemberSuccess) {
        this.addMemberSuccess = addMemberSuccess;
    }

    public boolean isTellerMemberFound() {
        return tellerMemberFound;
    }

    public void setTellerMemberFound(boolean tellerMemberFound) {
        this.tellerMemberFound = tellerMemberFound;
    }

    public boolean isTransactionComplete() {
        return transactionComplete;
    }

    public void setTransactionComplete(boolean transactionComplete) {
        this.transactionComplete = transactionComplete;
    }

    public boolean isDbMemberFound() {
        return dbMemberFound;
    }

    public void setDbMemberFound(boolean dbMemberFound) {
        this.dbMemberFound = dbMemberFound;
    }

    public boolean isReceiverFound() {
        return receiverFound;
    }

    public void setReceiverFound(boolean receiverFound) {
        this.receiverFound = receiverFound;
    }

    public boolean isTransferSuccess() {
        return transferSuccess;
    }

    public void setTransferSuccess(boolean transferSuccess) {
        this.transferSuccess = transferSuccess;
    }

    public boolean isAdminMemberFound() {
        return adminMemberFound;
    }

    public void setAdminMemberFound(boolean adminMemberFound) {
        this.adminMemberFound = adminMemberFound;
    }

    public Date getContribDate() {
        return contribDate;
    }

    public void setContribDate(Date contribDate) {
        this.contribDate = contribDate;
    }

    public boolean isValidTransfer() {
        return validTransfer;
    }

    public void setValidTransfer(boolean validTransfer) {
        this.validTransfer = validTransfer;
    }

    public MemberLoan getDbLoan() {

        return dbLoan;
    }

    public void setDbLoan(MemberLoan dbLoan) {
        this.dbLoan = dbLoan;
    }

    public String getSearchAdminMember() {
        return searchAdminMember;
    }

    public void setSearchAdminMember(String searchAdminMember) {
        this.searchAdminMember = searchAdminMember;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getToday() {
        today = new Date();
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getPayRollNumber() {
        return payRollNumber;
    }

    public void setPayRollNumber(String payRollNumber) {
        this.payRollNumber = payRollNumber;
    }

    public String getFileSeparator() {
        fileSeparator = System.getProperty("file.separator");
        return fileSeparator;
    }

    public void setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
    }

    //Facades
    public SaccoMemberFacade getMemberFacade() {
        return memberFacade;
    }

    public void setMemberFacade(SaccoMemberFacade memberFacade) {
        this.memberFacade = memberFacade;
    }

    public MemberAccountFacade getMemberAccountFacade() {
        return memberAccountFacade;
    }

    public void setMemberAccountFacade(MemberAccountFacade memberAccountFacade) {
        this.memberAccountFacade = memberAccountFacade;
    }

    public List<Referee> getAllMemberReferees() {
        return allMemberReferees;
    }

    public void setAllMemberReferees(List<Referee> allMemberReferees) {
        this.allMemberReferees = allMemberReferees;
    }

    public List<Kin> getAllMemberKins() {
        return allMemberKins;
    }

    public List<CompanyBranches> getAllCompanyBranches() {
        allCompanyBranches = branchesFacade.findAll();
        return allCompanyBranches;
    }

    public void setAllCompanyBranches(List<CompanyBranches> allCompanyBranches) {
        this.allCompanyBranches = allCompanyBranches;
    }

    public void setAllMemberKins(List<Kin> allMemberKins) {
        this.allMemberKins = allMemberKins;
    }

    public boolean isMemberHasLoans() {
        return memberHasLoans;
    }

    public void setMemberHasLoans(boolean memberHasLoans) {
        this.memberHasLoans = memberHasLoans;
    }

    public boolean isShareLimitReached() {
        return shareLimitReached;
    }

    public void setShareLimitReached(boolean shareLimitReached) {
        this.shareLimitReached = shareLimitReached;
    }

    public boolean isDataValid() {
        return dataValid;
    }

    public void setDataValid(boolean dataValid) {
        this.dataValid = dataValid;
    }

    public boolean isHasApprovedLoan() {
        return hasApprovedLoan;
    }

    public void setHasApprovedLoan(boolean hasApprovedLoan) {
        this.hasApprovedLoan = hasApprovedLoan;
    }

    public List<MemberLoan> getUnclearedLoans() {
        return unclearedLoans;
    }

    public void setUnclearedLoans(List<MemberLoan> unclearedLoans) {
        this.unclearedLoans = unclearedLoans;
    }

    public PrintService[] getPrintServices() {
        printServices = PrintServiceLookup.lookupPrintServices(null, null);

        return printServices;
    }

    public void setPrintServices(PrintService[] printServices) {
        this.printServices = printServices;
    }

    public List<BankBranches> getBankBranches() {

        return bankBranches;
    }

    public void setBankBranches(List<BankBranches> bankBranches) {
        this.bankBranches = bankBranches;
    }

    public List<SaccoBanks> getAllSaccoBanks() {
        allSaccoBanks = new ArrayList<SaccoBanks>();
        for (SaccoBanks sBank : banksFacade.findAll()) {
            if (!sBank.isTrash()) {
                allSaccoBanks.add(sBank);
            }

        }
        return allSaccoBanks;
    }

    public void setAllSaccoBanks(List<SaccoBanks> allSaccoBanks) {
        this.allSaccoBanks = allSaccoBanks;
    }

    public List<Charges> getAllCharges() {
        allCharges = chargesFacade.findAll();

        return allCharges;
    }

    public void setAllCharges(List<Charges> allCharges) {
        this.allCharges = allCharges;
    }

    public List<SaccoMember> getAllSaccoMembers() {
        allSaccoMembers = memberFacade.findAll();
        return allSaccoMembers;
    }

    public void setAllSaccoMembers(List<SaccoMember> allSaccoMembers) {
        this.allSaccoMembers = allSaccoMembers;
    }

    public DataModel<Referee> getTempRefereesDM() {
        return tempRefereesDM;
    }

    public void setTempRefereesDM(DataModel<Referee> tempRefereesDM) {
        this.tempRefereesDM = tempRefereesDM;
    }

    public DataModel<Kin> getKinsDM() {
        return kinsDM;
    }

    public void setKinsDM(DataModel<Kin> kinsDM) {
        this.kinsDM = kinsDM;
    }

    public DataModel<Charges> getChargesDM() {

        return chargesDM;
    }

    public void setChargesDM(DataModel<Charges> chargesDM) {
        this.chargesDM = chargesDM;
    }

    public DataModel<SaccoMember> getBranchMembersDM() {
        return branchMembersDM;
    }

    public void setBranchMembersDM(DataModel<SaccoMember> branchMembersDM) {
        this.branchMembersDM = branchMembersDM;
    }

    public DataModel<Contribution> getContributionsDM() {
        return contributionsDM;
    }

    public void setContributionsDM(DataModel<Contribution> contributionsDM) {
        this.contributionsDM = contributionsDM;
    }

    public DataModel<LoanGuarantors> getLoansGuaranteedDM() {
        return loansGuaranteedDM;
    }

    public void setLoansGuaranteedDM(DataModel<LoanGuarantors> loansGuaranteedDM) {
        this.loansGuaranteedDM = loansGuaranteedDM;
    }
    
    

    public DataModel<SaccoMember> getTellerMembersDM() {

        return tellerMembersDM;
    }

    public void setTellerMembersDM(DataModel<SaccoMember> tellerMembersDM) {

        this.tellerMembersDM = tellerMembersDM;
    }

    public DataModel<SaccoMember> getAllMembersDM() {

        return allMembersDM;
    }

    public void setAllMembersDM(DataModel<SaccoMember> allMembersDM) {
        this.allMembersDM = allMembersDM;
    }

    public DataModel<Referee> getMemberRefereesDM() {
        memberRefereesDM = new ListDataModel<Referee>(memberFacade.find(editMember.getId()).getReferees());
        return memberRefereesDM;
    }

    public void setMemberRefereesDM(DataModel<Referee> memberRefereesDM) {
        this.memberRefereesDM = memberRefereesDM;
    }

    public DataModel<Kin> getMemberKinsDM() {
        memberKinsDM = new ListDataModel<Kin>(editMember.getNextOfKin());
        return memberKinsDM;
    }

    public void setMemberKinsDM(DataModel<Kin> memberKinsDM) {
        this.memberKinsDM = memberKinsDM;
    }

    public Users getTellerLogged() {
        return tellerLogged;
    }

    public void setTellerLogged(Users tellerLogged) {
        this.tellerLogged = tellerLogged;
    }

    public Date getMaxDOB() {
        Date today = new Date();
        maxDOB.setYear(today.getYear() - 18);
        return maxDOB;
    }

    public void setMaxDOB(Date maxDOB) {
        this.maxDOB = maxDOB;
    }

    public Date getMinDOB() {
        Date today = new Date();
        minDOB.setYear(today.getYear() - 100);
        return minDOB;
    }

    public void setMinDOB(Date minDOB) {
        this.minDOB = minDOB;
    }

    public String getMemberInt() {
        return memberInt;
    }

    public void setMemberInt(String memberInt) {
        this.memberInt = memberInt;
    }

    public double getServiceLoan() {
        return serviceLoan;
    }

    public void setServiceLoan(double serviceLoan) {
        this.serviceLoan = serviceLoan;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public byte[] getMemberIdBytes() {
        return memberIdBytes;
    }

    public void setMemberIdBytes(byte[] memberIdBytes) {
        this.memberIdBytes = memberIdBytes;
    }

    public StreamedContent getMemberPhotoDB() {
        ctx = FacesContext.getCurrentInstance();
        memberPhotoDB = new DefaultStreamedContent();

        try {
            if (ctx.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
                memberPhotoDB = new DefaultStreamedContent();
            } else {
                if ((editMember != null || dbMember != null) && editMember.getMemberImage().length > 0) {
                    memberPhotoDB = new DefaultStreamedContent(new ByteInputStream(editMember.getMemberImage(), editMember.getMemberImage().length));
                } else if ((editMember != null || dbMember != null) && dbMember.getMemberImage().length > 0) {
                    memberPhotoDB = new DefaultStreamedContent(new ByteInputStream(dbMember.getMemberImage(), dbMember.getMemberImage().length));
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return memberPhotoDB;
    }

    public void setMemberPhotoDB(StreamedContent memberPhotoDB) {
        this.memberPhotoDB = memberPhotoDB;
    }

    public boolean isShowDateRange() {
        return showDateRange;
    }

    public void setShowDateRange(boolean showDateRange) {
        this.showDateRange = showDateRange;
    }

    public String getGlobalSearchCriteria() {
        return globalSearchCriteria;
    }

    public void setGlobalSearchCriteria(String globalSearchCriteria) {
        this.globalSearchCriteria = globalSearchCriteria;
    }

    public boolean isMemberFound() {
        return memberFound;
    }

    public void setMemberFound(boolean memberFound) {
        this.memberFound = memberFound;
    }

    public boolean isShowMember() {
        return showMember;
    }

    public void setShowMember(boolean showMember) {
        this.showMember = showMember;
    }

    public boolean isValidBulkLoan() {
        return validBulkLoan;
    }

    public void setValidBulkLoan(boolean validBulkLoan) {
        this.validBulkLoan = validBulkLoan;
    }

    public boolean isShowUsers() {
        return showUsers;
    }

    public void setShowUsers(boolean showUsers) {
        this.showUsers = showUsers;
    }

    public boolean isShowMembers() {
        return showMembers;
    }

    public void setShowMembers(boolean showMembers) {
        this.showMembers = showMembers;
    }

    public Contribution getEditContribution() {
        return editContribution;
    }

    public void setEditContribution(Contribution editContribution) {
        this.editContribution = editContribution;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getTransSearchValue() {
        return transSearchValue;
    }

    public void setTransSearchValue(String transSearchValue) {
        this.transSearchValue = transSearchValue;
    }

    public boolean isShowLoanData() {
        return showLoanData;
    }

    public void setShowLoanData(boolean showLoanData) {
        this.showLoanData = showLoanData;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public Date getFromTransDate() {
        return fromTransDate;
    }

    public void setFromTransDate(Date fromTransDate) {
        this.fromTransDate = fromTransDate;
    }

    public Date getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(Date loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public int getLoanInstallments() {
        return loanInstallments;
    }

    public void setLoanInstallments(int loanInstallments) {
        this.loanInstallments = loanInstallments;
    }

    public Date getLoanEndDate() {
        return loanEndDate;
    }

    public void setLoanEndDate(Date loanEndDate) {
        this.loanEndDate = loanEndDate;
    }

    public Date getToTransDate() {
        return toTransDate;
    }

    public void setToTransDate(Date toTransDate) {
        this.toTransDate = toTransDate;
    }

    public StreamedContent getMemberIDScanDB() {
        ctx = FacesContext.getCurrentInstance();
        memberIDScanDB = new DefaultStreamedContent();
        try {
            if (ctx.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
                memberIDScanDB = new DefaultStreamedContent();
            } else {
                if ((dbMember != null || dbMember != null) && editMember.getMemberScanID().length > 0) {
                    memberIDScanDB = new DefaultStreamedContent(new ByteInputStream(editMember.getMemberScanID(), editMember.getMemberScanID().length));
                } else if ((dbMember != null || dbMember != null) && dbMember.getMemberScanID().length > 0) {
                    memberIDScanDB = new DefaultStreamedContent(new ByteInputStream(dbMember.getMemberScanID(), dbMember.getMemberScanID().length));
                }

            }
        } catch (Exception ex) {
            //   ex.printStackTrace();
        }
        return memberIDScanDB;
    }

    public void setMemberIDScanDB(StreamedContent memberIDScanDB) {
        this.memberIDScanDB = memberIDScanDB;
    }
}
