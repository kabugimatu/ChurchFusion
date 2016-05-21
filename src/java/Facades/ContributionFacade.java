package Facades;

import Entities.Contribution;
import SupportBeans.ContribSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies All rights reserved
 */
@Stateless
public class ContributionFacade extends AbstractFacade<Contribution> {

    @PersistenceContext(unitName = "SaccoPU")
    private EntityManager em;
    SimpleDateFormat df;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContributionFacade() {
        super(Contribution.class);
    }

    //get statements 
    public List<Contribution> searchContributions(String memberNumber, Date queryDate) {
        List<Contribution> contributions = new ArrayList<Contribution>();
        List<Contribution> filterContributions = new ArrayList<Contribution>();
        df = new SimpleDateFormat("yyyy-MM");

        Query query = getEntityManager().createNamedQuery("searchContribution");
        query.setParameter("membernumber", memberNumber);
        query.setParameter("querydate", df.format(queryDate) + "%");

        contributions = query.getResultList();


        for (Contribution contri : contributions) {
            if (!(contri.getShares() == 0) || !(contri.getDeposit() == 0) || !(contri.getBenevolent() == 0)) {

                filterContributions.add(contri);
            }
        }

        return filterContributions;
    }

    public List<Contribution> yearlyContributions(String memberNumber, Date queryDate) {
        List<Contribution> contributions = new ArrayList<Contribution>();
        List<Contribution> filterContributions = new ArrayList<Contribution>();
        df = new SimpleDateFormat("yyyy");

        Query query = getEntityManager().createNamedQuery("searchContribution");
        query.setParameter("membernumber", memberNumber);
        query.setParameter("querydate", df.format(queryDate) + "%");

        contributions = query.getResultList();

        for (Contribution contri : contributions) {
            if (!(contri.getShares() == 0) || !(contri.getDeposit() == 0) || !(contri.getBenevolent() == 0)) {
                filterContributions.add(contri);
            }
        }

        return filterContributions;
    }

    //find loan servicing trend
    public List<Contribution> loanServicing(String memberNumber, String loanNumber) {
        System.out.println("member number ... " + memberNumber + "  loan number ... " + loanNumber);
        List<Contribution> contributions = new ArrayList<Contribution>();
        Query query = getEntityManager().createNamedQuery("loanContribution");
        query.setParameter("membernumber", memberNumber);
        query.setParameter("loannumber", loanNumber);

        contributions = query.getResultList();
        
        return contributions;
    }

    //get monthly contributions
    public List<Contribution> monthlyContributions(Date month) {

        List<Contribution> contributions = new ArrayList<Contribution>();
        df = new SimpleDateFormat("yyyy-MM");
        System.out.println("getting ...." + df.format(month));
        Query query = getEntityManager().createNamedQuery("monthlyContributions");
        query.setParameter("month", df.format(month) + "%");

        contributions = query.getResultList();
        return contributions;
    }

    public List<ContribSupport> monthlyContributionSupport(Date month) {
        List<ContribSupport> contributions = new ArrayList<ContribSupport>();
        df = new SimpleDateFormat("yyyy-MM");
        Query sumSharesQuery = getEntityManager().createNamedQuery("sumShares");
        Query sumDepositsQuery = getEntityManager().createNamedQuery("sumDeposits");
        Query sumInterestQuery = getEntityManager().createNamedQuery("sumInterests");
        Query sumBenoQuery = getEntityManager().createNamedQuery("sumBenevolent");
        Query sumRegFeeQuery = getEntityManager().createNamedQuery("sumRegFee");
        Query sumAppFeeQuery = getEntityManager().createNamedQuery("sumAppFee");
        sumSharesQuery.setParameter("month", df.format(month) + "%");
        sumDepositsQuery.setParameter("month", df.format(month) + "%");
        sumInterestQuery.setParameter("month", df.format(month) + "%");
        sumBenoQuery.setParameter("month", df.format(month) + "%");
        sumRegFeeQuery.setParameter("month", df.format(month) + "%");
        sumAppFeeQuery.setParameter("month", df.format(month) + "%");

        try {
            ContribSupport contribSupport = new ContribSupport();
            contribSupport.setShares((Double) sumSharesQuery.getSingleResult());
            contribSupport.setDeposits((Double) sumDepositsQuery.getSingleResult());
            contribSupport.setInterests((Double) sumInterestQuery.getSingleResult());
            contribSupport.setBenevolent((Double) sumBenoQuery.getSingleResult());
            contribSupport.setRegistrationFees((Double) sumRegFeeQuery.getSingleResult());
            contribSupport.setLoanAppFees((Double) sumAppFeeQuery.getSingleResult());
            switch (month.getMonth()) {
                case 0:
                    contribSupport.setMonth("Jan");
                    break;
                case 1:
                    contribSupport.setMonth("Feb");
                    break;
                case 2:
                    contribSupport.setMonth("Mar");
                    break;
                case 3:
                    contribSupport.setMonth("Apr");
                    break;
                case 4:
                    contribSupport.setMonth("May");
                    break;
                case 5:
                    contribSupport.setMonth("June");
                    break;
                case 6:
                    contribSupport.setMonth("July");
                    break;
                case 7:
                    contribSupport.setMonth("Aug");
                    break;
                case 8:
                    contribSupport.setMonth("Sep");
                    break;
                case 9:
                    contribSupport.setMonth("Oct");
                    break;
                case 10:
                    contribSupport.setMonth("Nov");
                    break;
                case 11:
                    contribSupport.setMonth("Dec");
                    break;
            }
            contributions.add(contribSupport);
        } catch (Exception ex) {
            contributions = new ArrayList<ContribSupport>();
        }




        return contributions;
    }

    public List<ContribSupport> yearlyContributionSupport(Date year) {
        List<ContribSupport> yearlyContributions = new ArrayList<ContribSupport>();
        int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};


        Query sumSharesQuery = getEntityManager().createNamedQuery("sumShares");
        Query sumDepositsQuery = getEntityManager().createNamedQuery("sumDeposits");
        Query sumInterestQuery = getEntityManager().createNamedQuery("sumInterests");
        Query sumBenoQuery = getEntityManager().createNamedQuery("sumBenevolent");
        Query sumRegFeeQuery = getEntityManager().createNamedQuery("sumRegFee");
        Query sumAppFeeQuery = getEntityManager().createNamedQuery("sumAppFee");
        for (int month : months) {
            df = new SimpleDateFormat("yyyy-MM");
            year.setMonth(month);

            sumSharesQuery.setParameter("month", df.format(year) + "%");
            sumDepositsQuery.setParameter("month", df.format(year) + "%");
            sumInterestQuery.setParameter("month", df.format(year) + "%");
            sumBenoQuery.setParameter("month", df.format(year) + "%");
            sumRegFeeQuery.setParameter("month", df.format(year) + "%");
            sumAppFeeQuery.setParameter("month", df.format(year) + "%");
            try {

                ContribSupport contribSupport = new ContribSupport();
                if (sumSharesQuery.getSingleResult() != null && sumDepositsQuery.getSingleResult() != null && sumInterestQuery.getSingleResult() != null && sumBenoQuery.getSingleResult() != null) {
                    contribSupport.setShares((Double) sumSharesQuery.getSingleResult());
                    contribSupport.setDeposits((Double) sumDepositsQuery.getSingleResult());
                    contribSupport.setInterests((Double) sumInterestQuery.getSingleResult());
                    contribSupport.setBenevolent((Double) sumBenoQuery.getSingleResult());
                    contribSupport.setRegistrationFees((Double) sumRegFeeQuery.getSingleResult());
                    contribSupport.setLoanAppFees((Double) sumAppFeeQuery.getSingleResult());
                    switch (year.getMonth()) {
                        case 0:
                            contribSupport.setMonth("Jan");
                            break;
                        case 1:
                            contribSupport.setMonth("Feb");
                            break;
                        case 2:
                            contribSupport.setMonth("Mar");
                            break;
                        case 3:
                            contribSupport.setMonth("Apr");
                            break;
                        case 4:
                            contribSupport.setMonth("May");
                            break;
                        case 5:
                            contribSupport.setMonth("June");
                            break;
                        case 6:
                            contribSupport.setMonth("July");
                            break;
                        case 7:
                            contribSupport.setMonth("Aug");
                            break;
                        case 8:
                            contribSupport.setMonth("Sep");
                            break;
                        case 9:
                            contribSupport.setMonth("Oct");
                            break;
                        case 10:
                            contribSupport.setMonth("Nov");
                            break;
                        case 11:
                            contribSupport.setMonth("Dec");
                            break;
                    }

                    yearlyContributions.add(contribSupport);
                }








            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }

        System.out.println(yearlyContributions);
        return yearlyContributions;
    }

    public List<Contribution> checkMemberContribute(String memberNumber, Date queryDate) {
        List<Contribution> contributions = new ArrayList<Contribution>();
        List<Contribution> filterContributions = new ArrayList<Contribution>();
        df = new SimpleDateFormat("yyyy-MM");

        Query query = getEntityManager().createNamedQuery("searchContribution");
        query.setParameter("membernumber", memberNumber);
        query.setParameter("querydate", df.format(queryDate) + "%");

        contributions = query.getResultList();


        for (Contribution contri : contributions) {
            if (!(contri.getBenevolent() == 0)) {

                filterContributions.add(contri);
            }
        }

        return filterContributions;
    }

    //check if member has paid monthly interest
    public List<Contribution> checkMonthlyInterest(String loanNumber, Date queryDate) {
        List<Contribution> contributions = new ArrayList<Contribution>();

        df = new SimpleDateFormat("yyyy-MM");
        
        Query query = getEntityManager().createNamedQuery("checkInterest");
        query.setParameter("loannumber", loanNumber);
        query.setParameter("querydate", df.format(queryDate) + "%");
        
        if (query.getResultList() != null) {
            contributions = query.getResultList();
        }

        return contributions;
    }

    // statements between dates
    public List<Contribution> viewStatementsBtwDate(Date fromDate, Date toDate, String memberNumber) {
        Query query = getEntityManager().createNamedQuery("ccontributionByDateRange");
        toDate.setDate(toDate.getDate() + 1);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("memberNumber", memberNumber);
        
        return query.getResultList();
    }
        
     // statements between dates
    public List<Contribution> userContribution(Date fromDate, Date toDate, String userName) {
        Query query = getEntityManager().createNamedQuery("userContributionByDateRange");
        toDate.setDate(toDate.getDate() + 1);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("userName", userName);               
        return query.getResultList();
    }
    
    // statements between dates
    public List<Contribution> usersContribution(Date fromDate, Date toDate) {
        Query query = getEntityManager().createNamedQuery("usersContributionByDateRange");
        toDate.setDate(toDate.getDate() + 1);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);                      
        return query.getResultList();
    }
    
    //
     // statements between dates
    public List<Contribution> viewAllMemberContributions(Date fromDate, Date toDate) {
        Query query = getEntityManager().createNamedQuery("allcontributionByDateRange");
        toDate.setDate(toDate.getDate() + 1);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);        
        System.out.println("size ... " + query.getResultList());
        return query.getResultList();
    }
}
