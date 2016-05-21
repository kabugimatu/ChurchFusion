package Controllers;

import Entities.Contribution;
import Entities.MemberAccount;
import Entities.MemberLoan; 
import Entities.SaccoDetails;
import Entities.SaccoMember;
import Facades.ContributionFacade;
import Facades.MemberAccountFacade;
import Facades.MemberLoanFacade;
import Facades.SaccoDetailsFacade;
import Facades.SaccoMemberFacade;
import SupportBeans.ContribSupport;
import com.itextpdf.text.Phrase;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies All rights reserved
 *
 *
 * This class handles all sacco statements and reports
 *
 */
@SessionScoped
@ManagedBean
public class ReportsController implements Printable {

    @ManagedProperty(value = "#{mainController}")
    private MainController mainController;
    //entities 
    private SaccoMember dbMember;
    private Contribution dbContribution, memberContribution;
    private MemberAccount memberAccount;
    private MemberLoan memberLoan, currentLoan;
    private SaccoDetails saccoDetails;
    //class variables
    private String searchCriteria, loanNumber, reportType, loansReportType, contributionType;
    private Date queryDate, today;
    private boolean dbMemberFound, membersFound, showFilterReport, showAllLoans, showFilteredLoans, showContributions;
    private boolean showFilterValue, showYear, showMonth, showGenBTN;
    private int month, year;
    private String contMonth, contYear;
    private boolean printStatement, printLoanStatement, printMembers, printLoansReports, reprintStatement;
    private double principalTotal, interestTotal, filterValue, totalLoanBalance;
    private double membershipPeriod;
    private double totalShares, totalDeposits, totalLoanInterests, totalBF, totalRegistration, totalLoanApp;
    private double accShares, deposits, vehicleCollection;
    private String vehicleReg;
    private PrintService[] printServices;
    private String serviceName;
    private DecimalFormat dFormat;
    private String filterReportType, loanSummaryType, defaulterType;
    private boolean showMonthlyReport, showDateRangeReport, renderMonth, renderYear;
    private Date statementFromDate, statementToDate;
    private double depositTotal, shareTotal, collectionTotal, grandTotal;
    private double dashboardSharesTotal, dashboardDepositsTotal;
    private int dashboardUnclearedLoans;
    //lists
    private List<MemberLoan> memberLoans;
    private List<MemberLoan> reportLoans;
    private List<SaccoMember> members;
    private List<SaccoMember> dbMembers;
    private List<MemberAccount> reportMemberAccounts;
    private List<Contribution> loanContributions;
    private List<Contribution> reportContributions;
    private List<MemberLoan> defaultedLoans;
    //ejbs
    @EJB
    private SaccoMemberFacade memberFacade;
    @EJB
    private ContributionFacade contributionFacade;
    @EJB
    private MemberAccountFacade memberAccountFacade;
    @EJB
    private MemberLoanFacade loansFacade;
    @EJB
    private SaccoMemberFacade membersFacade;
    @EJB
    private SaccoDetailsFacade detailsFacade;
    //datamodels
    private DataModel<Contribution> contributionsDM;
    private DataModel<Contribution> loanContributionDM;
    private DataModel<SaccoMember> membersDM;
    private DataModel<MemberAccount> reportMemberAccountsDM;
    private DataModel<MemberLoan> reportLoansDM;
    private DataModel<ContribSupport> saccoContributionDM;
    private DataModel<MemberLoan> defaultedLoansDM;

    public ReportsController() {
        this.dbMember = new SaccoMember();
        this.dbContribution = new Contribution();
        this.memberContribution = new Contribution();
        this.memberAccount = new MemberAccount();
        this.memberLoan = new MemberLoan();
        this.saccoDetails = new SaccoDetails();

        this.memberFacade = new SaccoMemberFacade();
        this.memberAccountFacade = new MemberAccountFacade();
        this.contributionFacade = new ContributionFacade();
        this.loansFacade = new MemberLoanFacade();
        this.membersFacade = new SaccoMemberFacade();
        this.detailsFacade = new SaccoDetailsFacade();

        this.memberLoans = new ArrayList<MemberLoan>();
        this.members = new ArrayList<SaccoMember>();
        this.reportLoans = new ArrayList<MemberLoan>();
        this.loanContributions = new ArrayList<Contribution>();
        this.reportContributions = new ArrayList<Contribution>();
        this.defaultedLoans = new ArrayList<MemberLoan>();

        this.contributionsDM = new ListDataModel<Contribution>();
        this.loanContributionDM = new ListDataModel<Contribution>();
        this.reportLoansDM = new ListDataModel<MemberLoan>();
        this.saccoContributionDM = new ListDataModel<ContribSupport>();
        this.defaultedLoansDM = new ListDataModel<MemberLoan>();

        this.today = new Date();

        this.dFormat = new DecimalFormat("#.00");

    }

    // on change defaulters type
    public void onChangeDefaulters() {
        if (defaulterType.equalsIgnoreCase("month")) {
            renderMonth = true;
        } else {
            renderMonth = false;
        }
    }

    // generate defaulters report
    public void genDefaultersReport() {
        reportLoans = new ArrayList<MemberLoan>();
        defaultedLoans = new ArrayList<MemberLoan>();
        loanContributions = new ArrayList<Contribution>();
        Date queryDate = new Date();
        for (MemberLoan reportLoan : loansFacade.findAll()) {
            if (reportLoan.isApprovalStatus()) {
                if (reportLoan.getBalance() > 0) {
                    reportLoans.add(reportLoan);
                }
            }
        }

        for (MemberLoan mLoan : reportLoans) {
            queryDate.setMonth(month - 1);
            queryDate.setYear(year - 1900);
            if (mLoan.getBalance() > 0) {
               
                if (contributionFacade.checkMonthlyInterest(mLoan.getLoanNUmber(), queryDate).isEmpty()) {
                    defaultedLoans.add(mLoan);
                } else {
                    for (Contribution lContribution : contributionFacade.checkMonthlyInterest(mLoan.getLoanNUmber(), queryDate)) {
                     
                        if ((lContribution.getLoanInterest() + lContribution.getLoanPrincipal()) < mLoan.getMonthlyInstallment()) {
                            defaultedLoans.add(mLoan);
                            loanContributions.add(lContribution);
                        }
                    }
                }
            }

            
            defaultedLoansDM = new ListDataModel<MemberLoan>(defaultedLoans);
        }
    }

    // get contribution sum
    public double contributionSum(String loanNumber) {      
        double contributionTotal = 0.0;        
        
        for (Contribution lContribution : loanContributions) {
            if(lContribution.getLoanNumber().equalsIgnoreCase(loanNumber)){
                
              contributionTotal = contributionTotal + lContribution.getLoanPrincipal() + lContribution.getLoanInterest();  
            }           
        }
        return contributionTotal;
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

    //search member
    public void searchMember() {
        contributionsDM = new ListDataModel<Contribution>();
        FacesContext message = FacesContext.getCurrentInstance();
        if (searchCriteria.equalsIgnoreCase("")) {
            year = 0;
            month = 0;
            dbMemberFound = false;
            contributionsDM = new ListDataModel<Contribution>();
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter search criteria : id number / member number / tel", ""));
        } else {
            if (memberFacade.searchSaccoMember(searchCriteria) != null) {
                dbMember = memberFacade.searchSaccoMember(searchCriteria);
                dbMemberFound = true;
                year = 0;
                month = 0;
                contributionsDM = new ListDataModel<Contribution>();

            } else {
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found", ""));
                dbMember = new SaccoMember();
                dbMemberFound = false;
            }
        }
    }
    // Preprocess Report Document WILL EDIT THIS WHEN I COME....KABUGI

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


        if (membersFound) {
            format.applyPattern("dd/MM/yyyy hh:mm aaa");
            pdf.add(Chunk.NEWLINE);
            pdf.add(Chunk.NEWLINE);
            pdf.add(new Paragraph("All Members Report "));
            pdf.add(new Paragraph("Generated by : " + mainController.getUserLogged().getFullName() + "  at  " + format.format(new Date())));
        }
        if (showFilterReport) {
            format.applyPattern("dd/MM/yyyy hh:mm aaa");
            pdf.add(Chunk.NEWLINE);
            pdf.add(Chunk.NEWLINE);
            if (reportType.equalsIgnoreCase("depositsgreaterthan")) {
                pdf.add(new Paragraph("Members with Deposits greater than : " + filterValue));
            } else if (reportType.equalsIgnoreCase("sharesgreaterthan")) {
                pdf.add(new Paragraph("Members with Shares greater than  :" + filterValue));
            } else if (reportType.equalsIgnoreCase("shareslessthan")) {
                pdf.add(new Paragraph("Members with Shares less than  :" + filterValue));
            } else if (reportType.equalsIgnoreCase("depositslessthan")) {
                pdf.add(new Paragraph("Members with deposits less than  :" + filterValue));
            }
            pdf.add(new Paragraph("Generated by : " + mainController.getUserLogged().getFullName() + "  at  " + format.format(new Date())));

        }
        if (showAllLoans) {
            format.applyPattern("dd/MM/yyyy hh:mm aaa");
            pdf.add(Chunk.NEWLINE);
            pdf.add(Chunk.NEWLINE);
            if (loansReportType.equalsIgnoreCase("allloans")) {
                pdf.add(new Paragraph("All Member Loans "));
            } else if (loansReportType.equalsIgnoreCase("unsettled")) {
                pdf.add(new Paragraph("Members with Unsettled Loans "));
            } else if (loansReportType.equalsIgnoreCase("balancegreaterthan")) {
                pdf.add(new Paragraph("Members with Loan balances greater than: " + filterValue));
            } else if (loansReportType.equalsIgnoreCase("balancelessthan")) {
                pdf.add(new Paragraph("Members with Loan balances less than : " + filterValue));
            }
            pdf.add(new Paragraph("Generated by : " + mainController.getUserLogged().getFullName() + "  at  " + format.format(new Date())));
        }

        if (showContributions) {

            format.applyPattern("MM/yyyy");
            queryDate = new Date();
            queryDate.setYear(Integer.parseInt(contYear) - 1900);

            if (contributionType.equalsIgnoreCase("monthlyCont")) {
                queryDate.setMonth(Integer.parseInt(contMonth) - 1);
                pdf.add(new Paragraph("Monthly Contributions for : " + format.format(queryDate)));
            } else {
                format.applyPattern("yyyy");
                pdf.add(new Paragraph("Yearly Contributions for : " + format.format(queryDate)));
            }
            format.applyPattern("dd/MM/yyyy hh:mm aaa");
            pdf.add(new Paragraph("Generated by : " + mainController.getUserLogged().getFullName() + "  at  " + format.format(new Date())));

        }




    }

    //search member loans
    public void searchMemberLoans() {
        FacesContext messages = FacesContext.getCurrentInstance();
        memberLoans = new ArrayList<MemberLoan>();
        loanContributions = new ArrayList<Contribution>();

        if (searchCriteria.equalsIgnoreCase("")) {
            dbMember = new SaccoMember();
            dbMemberFound = false;
            memberLoans = new ArrayList<MemberLoan>();
            loanContributions = new ArrayList<Contribution>();
            loanContributionDM = new ListDataModel<Contribution>(loanContributions);
            loanNumber = new String();
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter search criteria : id number / member number / tel", ""));
        } else {
            if (memberFacade.searchSaccoMember(searchCriteria) != null) {
                dbMember = memberFacade.searchSaccoMember(searchCriteria);
                dbMemberFound = true;

                if (dbMember.getMemberLoans().isEmpty()) {
                    dbMemberFound = false;
                    messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "This member does not have any loan", ""));

                } else {
                    
                    for (MemberLoan mLoan : dbMember.getMemberLoans()) {
                        memberLoans.add(mLoan);
                        System.out.println("getting here ... " + memberLoans.size());
                    }

                    double pTotal = 0.0;
                    double iTotal = 0.0;

                    for (Contribution contri : contributionFacade.loanServicing(dbMember.getMemberNumber(), memberLoans.get(0).getLoanNUmber())) {
                        System.out.println("getting .... " + contri.getContributionDate());
                        if (contri.getLoanPrincipal() != 0 || contri.getLoanInterest() != 0) {
                            pTotal = pTotal + contri.getLoanPrincipal();
                            iTotal = iTotal + contri.getLoanInterest();
                            loanContributions.add(contri);
                        }
                    }

                    principalTotal = Double.parseDouble(dFormat.format(pTotal));
                    interestTotal = Double.parseDouble(dFormat.format(iTotal));

                    loanNumber = memberLoans.get(0).getLoanNUmber();
                    currentLoan = loansFacade.findLoan(loanNumber);
                    loanContributionDM = new ListDataModel<Contribution>(loanContributions);

                }


            } else {

                dbMember = new SaccoMember();
                dbMemberFound = false;
                loanNumber = new String();
                messages.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member not found", ""));
            }

        }

    }

    //onchanging loan to view
    public void onSelectLoan() {
        loanContributions = new ArrayList<Contribution>();
        if (!loanNumber.equalsIgnoreCase("select")) {
            double pTotal = 0.0;
            double iTotal = 0.0;

            currentLoan = loansFacade.findLoan(loanNumber);

            for (Contribution loanContribution : contributionFacade.loanServicing(dbMember.getMemberNumber(), loanNumber)) {
                if (loanContribution.getLoanPrincipal() != 0 || loanContribution.getLoanInterest() != 0) {
                    System.out.println("contribution date ... " + loanContribution.getContributionDate());
                    loanContributions.add(loanContribution); 
                }

            }

            for (Contribution contri : loanContributions) {
                pTotal = pTotal + contri.getLoanPrincipal();
                iTotal = iTotal + contri.getLoanInterest();

            }

            principalTotal = Double.parseDouble(dFormat.format(pTotal));
            interestTotal = Double.parseDouble(dFormat.format(iTotal));

            loanContributionDM = new ListDataModel<Contribution>(loanContributions);
        } else {
            principalTotal = 0.0;
            interestTotal = 0.0;
            loanContributionDM = new ListDataModel<Contribution>();
        }
    }

    // on change report type
    public void onChangeReportType() {
        if (filterReportType.equalsIgnoreCase("monthly")) {
            showMonthlyReport = true;
            showDateRangeReport = false;
        } else if (filterReportType.equalsIgnoreCase("daterange")) {
            showDateRangeReport = true;
            showMonthlyReport = false;
        } else {
            showDateRangeReport = false;
            showMonthlyReport = false;
        }
    }

    //generate statement
    public void generateStatement() {
        SimpleDateFormat df;
        queryDate = new Date();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (filterReportType.equalsIgnoreCase("select")) {
            contributionsDM = new ListDataModel<Contribution>();
        } else if (filterReportType.equalsIgnoreCase("monthly")) {
            if (month == 0 && year == 0) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select month and year", ""));
                contributionsDM = new ListDataModel<Contribution>();
            } else if (month != 0 && year == 0) {
                contributionsDM = new ListDataModel<Contribution>();
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select year", ""));
            } else if (month == 0 && year != 0) {
                reportContributions = new ArrayList<Contribution>();
                queryDate.setYear(year - 1900);
                reportContributions = contributionFacade.yearlyContributions(dbMember.getMemberNumber(), queryDate);
                //contributionsDM = new ListDataModel<Contribution>(reportContributions);
            } else {
                reportContributions = new ArrayList<Contribution>();
                queryDate.setYear(year - 1900);
                queryDate.setMonth(month - 1);

                reportContributions = contributionFacade.searchContributions(dbMember.getMemberNumber(), queryDate);
                //contributionsDM = new ListDataModel<Contribution>(reportContributions);
            }
        } else if (filterReportType.equalsIgnoreCase("dateRange") && (statementFromDate != null || statementToDate != null)) {
            statementFromDate.setHours(0);
            statementToDate.setHours(23);
            reportContributions = contributionFacade.viewStatementsBtwDate(statementFromDate, statementToDate, dbMember.getMemberNumber());
            // contributionsDM = new ListDataModel<Contribution>();
        }
        generateTotals(reportContributions);
        contributionsDM = new ListDataModel<Contribution>(reportContributions);
    }

    public void generateTotals(List<Contribution> contributions) {
        depositTotal = 0;
        shareTotal = 0;
        collectionTotal = 0;
        grandTotal = 0;

        for (Contribution contri : contributions) {
            depositTotal += contri.getDeposit();
            shareTotal += contri.getShares();
//            collectionTotal += contri.getVehicleCollection();
            grandTotal += contri.getTotal();
        }
    }

    // ---- all reports ----//
    //..... start ......//
    public void viewAllMembers() {

        filterValue = 0.0;
        reportType = null;
        members = new ArrayList<SaccoMember>(membersFacade.findAll());
        if (members.isEmpty()) {
            membersFound = false;
            showFilterReport = false;
            members = new ArrayList<SaccoMember>();
        } else {
            membersFound = true;
            showFilterReport = false;
        }
        List<SaccoMember> filteredMembers = new ArrayList<SaccoMember>();
        for (SaccoMember sMember : members) {
            if (sMember.getMemberNumber() != null) {
                filteredMembers.add(sMember);
            }
        }
        membersDM = new ListDataModel<SaccoMember>(filteredMembers);
    }

    public MemberAccount findMemberAccount(String memberNumber) {
        MemberAccount dbMemberAccount = memberAccountFacade.getMemberAccount(memberNumber);

        return dbMemberAccount;

    }

    public void filterMembers() {
        FacesContext message = FacesContext.getCurrentInstance();

        reportMemberAccounts = new ArrayList<MemberAccount>();

        if (reportType.equalsIgnoreCase("select")) {
            membersFound = false;
            showFilterReport = false;
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select filter criteria", ""));
        } else {
            if (filterValue < 0) {
                membersFound = false;
                showFilterReport = false;
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "filter value must be greater than 0", ""));
            } else {

                if (reportType.equalsIgnoreCase("depositsgreaterthan")) {
                    reportMemberAccounts = memberAccountFacade.filterDeposits(filterValue, reportType);
                    reportMemberAccountsDM = new ListDataModel<MemberAccount>(reportMemberAccounts);
                    membersFound = false;
                    showFilterReport = true;
                } else if (reportType.equalsIgnoreCase("sharesgreaterthan")) {
                    reportMemberAccounts = memberAccountFacade.filterShares(filterValue, reportType);
                    reportMemberAccountsDM = new ListDataModel<MemberAccount>(reportMemberAccounts);
                    membersFound = false;
                    showFilterReport = true;
                } else if (reportType.equalsIgnoreCase("depositslessthan")) {
                    reportMemberAccounts = memberAccountFacade.filterDeposits(filterValue, reportType);
                    reportMemberAccountsDM = new ListDataModel<MemberAccount>(reportMemberAccounts);
                    membersFound = false;
                    showFilterReport = true;
                } else if (reportType.equalsIgnoreCase("shareslessthan")) {
                    reportMemberAccounts = memberAccountFacade.filterShares(filterValue, reportType);
                    reportMemberAccountsDM = new ListDataModel<MemberAccount>(reportMemberAccounts);
                    membersFound = false;
                    showFilterReport = true;
                } else if (reportType.equalsIgnoreCase("membershiplessthan")) {
                    for (MemberAccount memberAcc : memberAccountFacade.findAll()) {
                        if (computeMembershipPeriod(memberAcc.getMemberNumber()) < filterValue) {
                            reportMemberAccounts.add(memberAcc);
                        }
                    }


                    reportMemberAccountsDM = new ListDataModel<MemberAccount>(reportMemberAccounts);
                    membersFound = false;
                    showFilterReport = true;
                } else if (reportType.equalsIgnoreCase("membershipgreaterthan")) {
                    for (MemberAccount memberAcc : memberAccountFacade.findAll()) {
                        if (computeMembershipPeriod(memberAcc.getMemberNumber()) > filterValue) {
                            reportMemberAccounts.add(memberAcc);
                        }
                    }


                    reportMemberAccountsDM = new ListDataModel<MemberAccount>(reportMemberAccounts);
                    membersFound = false;
                    showFilterReport = true;
                } else {
                    membersFound = false;
                    showFilterReport = false;
                    reportMemberAccountsDM = new ListDataModel<MemberAccount>();
                }
            }
        }
    }

    //change report type 
    public void changeReportType() {
        if (loansReportType.equalsIgnoreCase("allloans")) {
            showFilterValue = false;
        } else if (loansReportType.equalsIgnoreCase("unsettled")) {
            showFilterValue = false;
        } else if (loansReportType.equalsIgnoreCase("select")) {
            showFilterValue = false;
        } else {
            showFilterValue = true;
        }
    }

    public void onChangeLoanSummary() {
        if (loanSummaryType.equalsIgnoreCase("pending approval")) {
            reportLoansDM = new ListDataModel<MemberLoan>(loansFacade.rejectedLoans(loanSummaryType));
        } else if (loanSummaryType.equalsIgnoreCase("rejected")) {
            reportLoansDM = new ListDataModel<MemberLoan>(loansFacade.rejectedLoans(loanSummaryType));
        } else if (loanSummaryType.equalsIgnoreCase("all")) {
            reportLoansDM = new ListDataModel<MemberLoan>(loansFacade.findAll());
        } else if (loanSummaryType.equalsIgnoreCase("approved")) {
            reportLoansDM = new ListDataModel<MemberLoan>(loansFacade.rejectedLoans(loanSummaryType));
        } else if (loanSummaryType.equalsIgnoreCase("Ongoing")) {
            reportLoansDM = new ListDataModel<MemberLoan>(loansFacade.rejectedLoans(loanSummaryType));
        } else {
            reportLoansDM = new ListDataModel<MemberLoan>();
        }
    }

    //filter contribution type
    public void togglefilterContributions() {
        if (contributionType.equalsIgnoreCase("select")) {
            showYear = false;
            showMonth = false;
            showGenBTN = false;
        } else if (contributionType.equalsIgnoreCase("monthlyCont")) {
            showMonth = true;
            showYear = true;
            showGenBTN = true;
        } else if (contributionType.equalsIgnoreCase("yearlyCont")) {
            showMonth = false;
            showYear = true;
            showGenBTN = true;
        }


    }

    public double calculateLoanBalance(double principalPaid) {
        double runningBalance = 0;

        runningBalance = currentLoan.getAppliedAmount() - principalPaid;


        return runningBalance;


    }

    public void generateContributions() {
        queryDate = new Date();

        totalShares = 0.0;
        totalBF = 0.0;
        totalLoanInterests = 0.0;
        totalDeposits = 0.0;
        totalLoanApp = 0.0;
        totalRegistration = 0.0;

        if (contributionType.equalsIgnoreCase("monthlyCont")) {
            showContributions = true;
            queryDate.setYear(Integer.parseInt(contYear) - 1900);
            queryDate.setMonth(Integer.parseInt(contMonth) - 1);

            List<ContribSupport> monthlyContributions = new ArrayList<ContribSupport>(contributionFacade.monthlyContributionSupport(queryDate));

            for (ContribSupport contri : monthlyContributions) {
                totalShares = totalShares + contri.getShares();
                totalBF = totalBF + contri.getBenevolent();
                totalDeposits = totalDeposits + contri.getDeposits();
                totalLoanApp = totalLoanApp + contri.getLoanAppFees();
                totalLoanInterests = totalLoanInterests + contri.getInterests();
                totalRegistration = totalRegistration + contri.getRegistrationFees();
            }

            saccoContributionDM = new ListDataModel<ContribSupport>(contributionFacade.monthlyContributionSupport(queryDate));
        } else if (contributionType.equalsIgnoreCase("yearlyCont")) {
            showContributions = true;
            queryDate.setYear(Integer.parseInt(contYear) - 1900);
            if (contributionFacade.yearlyContributionSupport(queryDate).isEmpty()) {
                showContributions = false;
            } else {
                showContributions = true;
            }

            List<ContribSupport> yearlyContributions = new ArrayList<ContribSupport>(contributionFacade.yearlyContributionSupport(queryDate));

            for (ContribSupport contri : yearlyContributions) {
                totalShares = totalShares + contri.getShares();
                totalBF = totalBF + contri.getBenevolent();
                totalDeposits = totalDeposits + contri.getDeposits();
                totalLoanApp = totalLoanApp + contri.getLoanAppFees();
                totalLoanInterests = totalLoanInterests + contri.getInterests();
                totalRegistration = totalRegistration + contri.getRegistrationFees();
            }

            saccoContributionDM = new ListDataModel<ContribSupport>(contributionFacade.yearlyContributionSupport(queryDate));

        }
    }

    //filter loans 
    public void filterLoans() {


        FacesContext message = FacesContext.getCurrentInstance();
        reportLoans = new ArrayList<MemberLoan>();
        if (loansReportType.equalsIgnoreCase("select")) {
            showAllLoans = false;
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select loan report type", ""));

        } else if (loansReportType.equalsIgnoreCase("balancegreaterthan") && filterValue < 0) {
            showAllLoans = false;
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "filter value must be greater than 0", ""));
        } else if (loansReportType.equalsIgnoreCase("balancelessthan") && filterValue < 0) {
            showAllLoans = false;
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "filter value must be greater than 0", ""));
        } else {
            if (loansReportType.equalsIgnoreCase("allloans")) {
                reportLoans = loansFacade.findAll();
                double total = 0.0;
                for (MemberLoan loan : reportLoans) {
                    if (loan.isApprovalStatus()) {
                        total = total + loan.getBalance();
                    }
                }
                totalLoanBalance = total;
                showAllLoans = true;
            }
            if (loansReportType.equalsIgnoreCase("unsettled")) {
                for (MemberLoan reportLoan : loansFacade.findAll()) {
                    if (reportLoan.isApprovalStatus()) {
                        if (reportLoan.getBalance() > 0) {
                            reportLoans.add(reportLoan);
                        }
                    }
                }

                double total = 0.0;
                for (MemberLoan loan : reportLoans) {
                    total = total + loan.getBalance();
                }
                totalLoanBalance = total;
                showAllLoans = true;
            } else if (loansReportType.equalsIgnoreCase("balancegreaterthan")) {
                for (MemberLoan reportLoan : loansFacade.findAll()) {
                    if (reportLoan.isApprovalStatus()) {
                        if (reportLoan.getBalance() > filterValue) {
                            reportLoans.add(reportLoan);
                        }
                    }
                }

                double total = 0.0;
                for (MemberLoan loan : reportLoans) {
                    if (loan.isApprovalStatus()) {
                        total = total + loan.getBalance();
                    }
                }
                totalLoanBalance = total;
                showAllLoans = true;

            } else if (loansReportType.equalsIgnoreCase("balancelessthan")) {
                for (MemberLoan reportLoan : loansFacade.findAll()) {
                    if (reportLoan.isApprovalStatus()) {
                        if (reportLoan.getBalance() < filterValue && reportLoan.getBalance() > 0) {
                            reportLoans.add(reportLoan);
                        }
                    }
                }

                double total = 0.0;
                for (MemberLoan loan : reportLoans) {
                    if (loan.isApprovalStatus()) {
                        total = total + loan.getBalance();
                    }
                }
                totalLoanBalance = total;

                showAllLoans = true;
            }
        }
        reportLoansDM = new ListDataModel<MemberLoan>(reportLoans);
    }

    public SaccoMember findMember(String memberNumber) {
        SaccoMember saccoMember = membersFacade.searchSaccoMember(memberNumber);

        return saccoMember;
    }

    //compute membership period
    public double computeMembershipPeriod(String memberNumber) {

        if (memberNumber != null) {
            membershipPeriod = (today.getTime() - membersFacade.searchSaccoMember(memberNumber).getRegistrationDate().getTime()) / (1000 * 60 * 60 * 24);
        }

        return membershipPeriod / 30;
    }

    //report printing
    public void selectContribution() {
        memberContribution = contributionsDM.getRowData();
    }

    //reprint statement
    public void reprintStatement() {
        reprintStatement = true;
        try {
            System.out.println("getting ... " + memberContribution.getShares());
            dbMember = memberFacade.searchSaccoMember(memberContribution.getMemberNumber());
            accShares = memberContribution.getShares();
            deposits = memberContribution.getDeposit();
            // vehicleCollection = memberContribution.getVehicleCollection();
            // vehicleReg = memberContribution.getVehicleReg();
            printReport();
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //print reports 
    public void printStatements() {
        printStatement = true;
        try {
            printReport();
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printLoanStatements() {
        printLoanStatement = true;
        try {
            printReport();
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //print members reports
    public void printMembers() {
        printMembers = true;
        try {
            printReport();
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //print loan reports
    public void printLoansReport() {
        printLoansReports = true;
        try {
            printReport();
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        //To change body of generated methods, choose Tools | Templates.
        saccoDetails = detailsFacade.findAll().get(0);
        Date reportDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Arial", Font.PLAIN, 14);
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.setFont(font);
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
//        try {
//            image = ImageIO.read(new File(saccoDetails.getLogoUrl()));
//        } catch (IOException ex) {
//            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (reprintStatement) {
            int i;
            g.drawString("   " + saccoDetails.getName(), 5, 10);

            g.drawString("Member Name  ", 5, 25);
            g.drawString(dbMember.getFullName(), 110, 25);
            g.drawString("Member No    ", 5, 40);
            g.drawString(dbMember.getMemberNumber(), 110, 40);
            g.drawString("Date         ", 5, 55);
            g.drawString(dateFormat.format(new Date()), 110, 55);
            g.drawString("------------------------------------------------------", 5, 70);

            i = 70;

            g.drawString("MEMBER CONTRIBUTION", 5, 85);
            g.drawString("------------------------------------------------------", 5, 100);

            g.drawString("THANKSGV", 5, 130);
            g.drawString(dFormat.format(accShares) + "", 110, 130);

            g.drawString("Church Tithe", 5, 145);
            g.drawString(dFormat.format(deposits) + "", 110, 145);

            g.drawString("------------------------------------------------------", 5, 160);
            g.drawString("Total ", 5, 175);
            g.drawString(dFormat.format((accShares + deposits)) + "", 110, 175);
            g.drawString("------------------------------------------------------", 5, 190);
            g.drawString("Server BY : " + mainController.getUserLogged().getFullName(), 5, 215);
        }

        if (printStatement) {
            int i = 0;
            g.drawString("TITHE AND THANKSGIVING STATEMENT", 5, i += 20);
            g.drawImage(image, 400, 20, 100, 100, null);
            g.drawLine(5, 25, 250, 25);
            g.drawString(saccoDetails.getName().toUpperCase(), 5, i += 35);

            g.drawString(dbMember.getFullName().toUpperCase(), 5, i += 35);
            g.drawString("MEMBER NO." + dbMember.getMemberNumber().toUpperCase(), 5, i += 25);
            g.drawString("P.O BOX  " + dbMember.getHomeAddress().toUpperCase(), 5, i += 25);

            g.drawRect(0, i += 30, 580, 740);

            g.drawString("       DATE                          B/F              THANKSGV             TITHE                TOTAL", 10, i += 20);

            g.drawLine(0, i += 10, 580, i);

            g.drawLine(150, i - 30, 150, 910);
            g.drawLine(230, i - 30, 230, 910);
            g.drawLine(340, i - 30, 340, 910);
            g.drawLine(465, i - 30, 465, 910);
            font = new Font("Arial", Font.ITALIC, 12);
            g2d.setFont(font);


            for (Contribution pContribution : reportContributions) {
                double total = 0.0;
                total = pContribution.getBenevolent() + pContribution.getShares() + pContribution.getDeposit();
                g.drawString(dateFormat.format(pContribution.getContributionDate()), 6, i += 25);
                g.drawString(dFormat.format(pContribution.getBenevolent()) + "", 180, i);
                g.drawString(dFormat.format(pContribution.getShares()) + "", 260, i);
                g.drawString(dFormat.format(pContribution.getDeposit()) + "", 375, i);
                g.drawString(total + "", 500, i);
            }

            font = new Font("Arial", Font.ITALIC, 10);
            g2d.setFont(font);
            g2d.setColor(Color.red);
            g.drawString("Note : Any omission or errors in this statement should be reported immediately to the office with 30 days from the date of receipt", 5, 950);
        }

        if (printLoanStatement) {
            int i = 0;

            g.drawString("LOAN REPAYMENT STATEMENT", 5, i += 20);
            g.drawImage(image, 400, 20, 100, 100, null);
            g.drawLine(5, 25, 200, 25);
            g.drawString(saccoDetails.getName().toUpperCase(), 5, i += 35);

            g.drawString(dbMember.getFullName().toUpperCase(), 5, i += 35);
            g.drawString("MEMBER NO.  " + dbMember.getMemberNumber().toUpperCase(), 5, i += 25);
            g.drawString("P.O BOX   " + dbMember.getHomeAddress().toUpperCase(), 5, i += 25);

            g.drawRect(0, i += 30, 580, 740);

            g.drawString("   Date        Prev. Balance    Principal     Interest     Amount Paid      Balance", 10, i += 20);

            font = new Font("Arial", Font.ITALIC, 12);
            g2d.setFont(font);

            for (Contribution pLoanContribution : loanContributions) {
                double total = 0.0;
                total = pLoanContribution.getLoanInterest() + pLoanContribution.getLoanPrincipal();

                g.drawString(dateFormat.format(pLoanContribution.getContributionDate()), 6, i += 20);
                g.drawString(dFormat.format(pLoanContribution.getPreviousBalance()) + "", 100, i);
                g.drawString(dFormat.format(pLoanContribution.getLoanPrincipal()) + "", 180, i);
                g.drawString(dFormat.format(pLoanContribution.getLoanInterest()) + "", 260, i);
                g.drawString(dFormat.format(total) + "", 320, i);
                g.drawString(dFormat.format(pLoanContribution.getPreviousBalance() - (pLoanContribution.getLoanPrincipal() + pLoanContribution.getLoanInterest())) + "", 430, i);
            }
            font = new Font("Arial", Font.ITALIC, 10);
            g2d.setFont(font);
            g2d.setColor(Color.red);
            g.drawString("Note : Any omission or errors in this statement should be reported immediately to the office with 30 days from the date of receipt", 5, 950);
        }

        if (printMembers && showFilterReport == false) {
            int i = 0;

            g.drawString("MEMBERS REPORT", 5, i += 20);
            g.drawImage(image, 400, 20, 100, 100, null);
            g.drawLine(5, 25, 120, 25);
            g.drawString(saccoDetails.getName().toUpperCase(), 5, i += 35);

            g.drawString("Report generated by : " + mainController.getUserLogged().getFullName(), 5, i += 25);

            g.drawString("Date : " + dateFormat.format(reportDate), 5, i += 25);

            g.drawRect(0, i += 30, 580, 775);
            font = new Font("Arial", Font.BOLD, 12);
            g2d.setFont(font);
            g.drawString(" NUMBER                NAME                         MEMBERSHIP            THANKSGV                   TITHE", 10, i += 20);

            g.drawLine(0, i += 10, 580, i);

            g.drawLine(85, i - 30, 85, 910);
            g.drawLine(230, i - 30, 230, 910);
            g.drawLine(340, i - 30, 340, 910);
            g.drawLine(465, i - 30, 460, 910);

            font = new Font("Arial", Font.PLAIN, 12);
            g2d.setFont(font);

            for (SaccoMember pMember : members) {

                g.drawString(pMember.getMemberNumber(), 12, i += 25);
                g.drawString(pMember.getFullName(), 100, i);
                g.drawString((int) computeMembershipPeriod(pMember.getMemberNumber()) + "", 260, i);
                g.drawString(dFormat.format(findMemberAccount(pMember.getMemberNumber()).getShares()) + "", 370, i);
                g.drawString(dFormat.format(findMemberAccount(pMember.getMemberNumber()).getDeposit()) + "", 490, i);
            }
        }
        if (printMembers && showFilterReport) {
            int i = 0;

            g.drawString("MEMBERS REPORT", 5, i += 20);
            g.drawImage(image, 400, 20, 100, 100, null);
            g.drawLine(5, 25, 120, 25);
            g.drawString(saccoDetails.getName().toUpperCase(), 5, i += 35);

            g.drawString("Report generated by : " + mainController.getUserLogged().getFullName(), 5, i += 25);

            g.drawString("Date : " + dateFormat.format(reportDate), 5, i += 25);

            g.drawRect(0, i += 30, 580, 775);
            font = new Font("Arial", Font.BOLD, 12);
            g2d.setFont(font);
            g.drawString(" NUMBER                NAME                         MEMBERSHIP            THANKSGV                   TITHE", 10, i += 20);

            g.drawLine(0, i += 10, 580, i);

            g.drawLine(85, i - 30, 85, 910);
            g.drawLine(230, i - 30, 230, 910);
            g.drawLine(340, i - 30, 340, 910);
            g.drawLine(465, i - 30, 460, 910);

            font = new Font("Arial", Font.PLAIN, 12);
            g2d.setFont(font);

            for (MemberAccount pMemberAccount : reportMemberAccounts) {
                g.drawString(pMemberAccount.getMemberNumber(), 12, i += 25);
                g.drawString(findMember(pMemberAccount.getMemberNumber()).getFullName(), 100, i);
                g.drawString((int) computeMembershipPeriod(pMemberAccount.getMemberNumber()) + "", 260, i);
                g.drawString(dFormat.format(pMemberAccount.getShares()) + "", 370, i);
                g.drawString(dFormat.format(pMemberAccount.getDeposit()) + "", 490, i);
            }
        }
        if (printLoansReports) {
            int i = 0;

            g.drawString("LOANS REPORT", 5, i += 20);
            g.drawImage(image, 400, 20, 100, 100, null);
            g.drawLine(5, 25, 120, 25);
            g.drawString(saccoDetails.getName().toUpperCase(), 5, i += 35);

            g.drawString("Report generated by : " + mainController.getUserLogged().getFullName(), 5, i += 25);

            g.drawString("Date : " + dateFormat.format(reportDate), 5, i += 25);

            g.drawRect(0, i += 30, 580, 775);
            font = new Font("Arial", Font.BOLD, 12);
            g2d.setFont(font);
            g.drawString("Loan Number       Member Name               Member Number          Loan Value            Amount paid                  Balance", 10, i += 20);

            g.drawLine(0, i += 10, 580, i);

            g.drawLine(90, i - 30, 90, 910);
            g.drawLine(200, i - 30, 200, 910);
            g.drawLine(300, i - 30, 300, 910);
            g.drawLine(400, i - 30, 400, 910);
            g.drawLine(490, i - 30, 490, 910);

            font = new Font("Arial", Font.PLAIN, 12);
            g2d.setFont(font);

            for (MemberLoan pLoan : reportLoans) {
                g.drawString(pLoan.getLoanNUmber(), 12, i += 25);
                g.drawString(findMember(pLoan.getMemberNumber()).getFullName(), 100, i);
                g.drawString(pLoan.getMemberNumber(), 220, i);
                g.drawString(dFormat.format(pLoan.getAmountPayable()) + "", 300, i);
                g.drawString((dFormat.format(pLoan.getAmountPayable() - pLoan.getBalance())) + "", 370, i);
                g.drawString(dFormat.format(pLoan.getBalance()) + "", 510, i);
            }
        }
        return PAGE_EXISTS;
    }

    //printing on reports on A4 papers......
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


        printStatement = false;
        printLoanStatement = false;
        printMembers = false;
        printLoansReports = false;
        reprintStatement = false;

        return exitCode;
    }

    //..... end ........//
    // ---- all reports ----//
    //getters and setters
    public SaccoMember getDbMember() {
        return dbMember;
    }

    public void setDbMember(SaccoMember dbMember) {
        this.dbMember = dbMember;
    }

    public Contribution getDbContribution() {
        return dbContribution;
    }

    public void setDbContribution(Contribution dbContribution) {
        this.dbContribution = dbContribution;
    }

    public MemberAccount getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(MemberAccount memberAccount) {
        this.memberAccount = memberAccount;
    }

    public MemberLoan getMemberLoan() {
        return memberLoan;
    }

    public void setMemberLoan(MemberLoan memberLoan) {
        this.memberLoan = memberLoan;
    }

    public MemberLoan getCurrentLoan() {
        return currentLoan;
    }

    public void setCurrentLoan(MemberLoan currentLoan) {
        this.currentLoan = currentLoan;
    }

    public List<MemberLoan> getMemberLoans() {
        return memberLoans;
    }

    public void setMemberLoans(List<MemberLoan> memberLoans) {
        this.memberLoans = memberLoans;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getLoansReportType() {
        return loansReportType;
    }

    public void setLoansReportType(String loansReportType) {
        this.loansReportType = loansReportType;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public boolean isShowContributions() {
        return showContributions;
    }

    public void setShowContributions(boolean showContributions) {
        this.showContributions = showContributions;
    }

    public boolean isDbMemberFound() {
        return dbMemberFound;
    }

    public void setDbMemberFound(boolean dbMemberFound) {
        this.dbMemberFound = dbMemberFound;
    }

    public boolean isMembersFound() {
        return membersFound;
    }

    public void setMembersFound(boolean membersFound) {
        this.membersFound = membersFound;
    }

    public boolean isShowFilterValue() {
        return showFilterValue;
    }

    public void setShowFilterValue(boolean showFilterValue) {
        this.showFilterValue = showFilterValue;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getContMonth() {
        return contMonth;
    }

    public void setContMonth(String contMonth) {
        this.contMonth = contMonth;
    }

    public String getContYear() {
        return contYear;
    }

    public void setContYear(String contYear) {
        this.contYear = contYear;
    }

    public double getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(double filterValue) {
        this.filterValue = filterValue;
    }

    public double getTotalLoanBalance() {
        return totalLoanBalance;
    }

    public void setTotalLoanBalance(double totalLoanBalance) {
        this.totalLoanBalance = totalLoanBalance;
    }

    public double getPrincipalTotal() {
        return principalTotal;
    }

    public void setPrincipalTotal(double principalTotal) {
        this.principalTotal = principalTotal;
    }

    public double getInterestTotal() {
        return interestTotal;
    }

    public void setInterestTotal(double interestTotal) {
        this.interestTotal = interestTotal;
    }

    //reports totals 
    public double getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(double totalShares) {
        this.totalShares = totalShares;
    }

    public double getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(double totalDeposits) {
        this.totalDeposits = totalDeposits;
    }

    public double getTotalLoanInterests() {
        return totalLoanInterests;
    }

    public void setTotalLoanInterests(double totalLoanInterests) {
        this.totalLoanInterests = totalLoanInterests;
    }

    public double getTotalBF() {
        return totalBF;
    }

    public void setTotalBF(double totalBF) {
        this.totalBF = totalBF;
    }

    public double getTotalRegistration() {
        return totalRegistration;
    }

    public void setTotalRegistration(double totalRegistration) {
        this.totalRegistration = totalRegistration;
    }

    public double getTotalLoanApp() {
        return totalLoanApp;
    }

    public void setTotalLoanApp(double totalLoanApp) {
        this.totalLoanApp = totalLoanApp;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public boolean isShowYear() {
        return showYear;
    }

    public void setShowYear(boolean showYear) {
        this.showYear = showYear;
    }

    public boolean isShowMonth() {
        return showMonth;
    }

    public void setShowMonth(boolean showMonth) {
        this.showMonth = showMonth;
    }

    public boolean isShowGenBTN() {
        return showGenBTN;
    }

    public void setShowGenBTN(boolean showGenBTN) {
        this.showGenBTN = showGenBTN;
    }

    public String getContributionType() {
        return contributionType;
    }

    public void setContributionType(String contributionType) {
        this.contributionType = contributionType;
    }

    public DataModel<ContribSupport> getSaccoContributionDM() {
        return saccoContributionDM;
    }

    public void setSaccoContributionDM(DataModel<ContribSupport> saccoContributionDM) {
        this.saccoContributionDM = saccoContributionDM;
    }

    public DataModel<Contribution> getContributionsDM() {
        return contributionsDM;
    }

    public void setContributionsDM(DataModel<Contribution> contributionsDM) {
        this.contributionsDM = contributionsDM;
    }

    public DataModel<MemberLoan> getDefaultedLoansDM() {
        return defaultedLoansDM;
    }

    public void setDefaultedLoansDM(DataModel<MemberLoan> defaultedLoansDM) {
        this.defaultedLoansDM = defaultedLoansDM;
    }

    public DataModel<Contribution> getLoanContributionDM() {
        return loanContributionDM;
    }

    public void setLoanContributionDM(DataModel<Contribution> loanContributionDM) {
        this.loanContributionDM = loanContributionDM;
    }

    public DataModel<SaccoMember> getMembersDM() {
        return membersDM;
    }

    public void setMembersDM(DataModel<SaccoMember> membersDM) {
        this.membersDM = membersDM;
    }

    public boolean isShowFilterReport() {
        return showFilterReport;
    }

    public void setShowFilterReport(boolean showFilterReport) {
        this.showFilterReport = showFilterReport;
    }

    public boolean isShowAllLoans() {
        return showAllLoans;
    }

    public void setShowAllLoans(boolean showAllLoans) {
        this.showAllLoans = showAllLoans;
    }

    public boolean isShowFilteredLoans() {
        return showFilteredLoans;
    }

    public void setShowFilteredLoans(boolean showFilteredLoans) {
        this.showFilteredLoans = showFilteredLoans;
    }

    public DataModel<MemberAccount> getReportMemberAccountsDM() {
        return reportMemberAccountsDM;
    }

    public void setReportMemberAccountsDM(DataModel<MemberAccount> reportMemberAccountsDM) {
        this.reportMemberAccountsDM = reportMemberAccountsDM;
    }

    public DataModel<MemberLoan> getReportLoansDM() {
        return reportLoansDM;
    }

    public void setReportLoansDM(DataModel<MemberLoan> reportLoansDM) {
        this.reportLoansDM = reportLoansDM;
    }

    public PrintService[] getPrintServices() {
        printServices = PrintServiceLookup.lookupPrintServices(null, null);
        return printServices;
    }

    public void setPrintServices(PrintService[] printServices) {
        this.printServices = printServices;
    }

    public double getDepositTotal() {
        return depositTotal;
    }

    public void setDepositTotal(double depositTotal) {
        this.depositTotal = depositTotal;
    }

    public double getShareTotal() {
        return shareTotal;
    }

    public void setShareTotal(double shareTotal) {
        this.shareTotal = shareTotal;
    }

    public double getCollectionTotal() {
        return collectionTotal;
    }

    public void setCollectionTotal(double collectionTotal) {
        this.collectionTotal = collectionTotal;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFilterReportType() {
        return filterReportType;
    }

    public void setFilterReportType(String filterReportType) {
        this.filterReportType = filterReportType;
    }

    public String getLoanSummaryType() {
        return loanSummaryType;
    }

    public void setLoanSummaryType(String loanSummaryType) {
        this.loanSummaryType = loanSummaryType;
    }

    public String getDefaulterType() {
        return defaulterType;
    }

    public void setDefaulterType(String defaulterType) {
        this.defaulterType = defaulterType;
    }

    public boolean isShowMonthlyReport() {
        return showMonthlyReport;
    }

    public void setShowMonthlyReport(boolean showMonthlyReport) {
        this.showMonthlyReport = showMonthlyReport;
    }

    public Date getStatementFromDate() {
        return statementFromDate;
    }

    public void setStatementFromDate(Date statementFromDate) {
        this.statementFromDate = statementFromDate;
    }

    public Date getStatementToDate() {
        return statementToDate;
    }

    public void setStatementToDate(Date statementToDate) {
        this.statementToDate = statementToDate;
    }

    public boolean isShowDateRangeReport() {
        return showDateRangeReport;
    }

    public void setShowDateRangeReport(boolean showDateRangeReport) {
        this.showDateRangeReport = showDateRangeReport;
    }

    public boolean isRenderMonth() {
        return renderMonth;
    }

    public void setRenderMonth(boolean renderMonth) {
        this.renderMonth = renderMonth;
    }

    public boolean isRenderYear() {
        return renderYear;
    }

    public void setRenderYear(boolean renderYear) {
        this.renderYear = renderYear;
    }

    //managed properties
    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public List<SaccoMember> getDbMembers() {
        dbMembers = membersFacade.findAll();
        return dbMembers;
    }

    public void setDbMembers(List<SaccoMember> dbMembers) {
        this.dbMembers = dbMembers;
    }

    public double getDashboardSharesTotal() {
        List<MemberAccount> dbAccounts = memberAccountFacade.findAll();
        dashboardSharesTotal = 0;
        for (MemberAccount mAccount : dbAccounts) {
            dashboardSharesTotal += mAccount.getShares();
        }
        return dashboardSharesTotal;
    }

    public void setDashboardSharesTotal(double dashboardSharesTotal) {
        this.dashboardSharesTotal = dashboardSharesTotal;
    }

    public double getDashboardDepositsTotal() {
        List<MemberAccount> dbAccounts = memberAccountFacade.findAll();
        dashboardDepositsTotal = 0;
        for (MemberAccount mAccount : dbAccounts) {
            dashboardDepositsTotal += mAccount.getDeposit();
        }
        return dashboardDepositsTotal;
    }

    public void setDashboardDepositsTotal(double dashboardDepositsTotal) {
        this.dashboardDepositsTotal = dashboardDepositsTotal;
    }

    public int getDashboardUnclearedLoans() {
        List<MemberLoan> allDBLoans = loansFacade.findAll();
        dashboardUnclearedLoans = 0;
        for (MemberLoan mLoan : allDBLoans) {
            if (mLoan.isApprovalStatus()) {
                if (mLoan.getBalance() > 0) {
                    dashboardUnclearedLoans++;
                }
            }

        }
        return dashboardUnclearedLoans;
    }

    public void setDashboardUnclearedLoans(int dashboardUnclearedLoans) {
        this.dashboardUnclearedLoans = dashboardUnclearedLoans;
    }
}
