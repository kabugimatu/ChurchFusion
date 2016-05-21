
package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author George / Kabugi Pearl Soft Technologies 
 * All rights reserved
 * 
 * This Entity represents the sacco member.searchByBranch
 */

@NamedQueries({
    @NamedQuery(name="checkId", query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.idNumber =:idnumber"),
    @NamedQuery(name="checkEmail", query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.email =:email"),
    @NamedQuery(name="checkTel", query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.phoneNumber =:tel"),
    @NamedQuery(name="checkNumber", query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.memberNumber =:number"),
    @NamedQuery(name="searchMember", query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.memberNumber =:searchQuery OR saccomember1.idNumber =:searchQuery OR saccomember1.phoneNumber =:searchQuery"),
    @NamedQuery(name="searchPayroll",query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.payRollNumber =:payrollnumber"),
    @NamedQuery(name="searchByBranch",query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.branch =:branchName"),
    @NamedQuery(name="searchByMemberNumber",query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.memberNumber =:mNumber"),
    @NamedQuery(name="searchByBankInfo",query="SELECT saccomember1 FROM SaccoMember saccomember1 WHERE saccomember1.bankId =:bankid OR saccomember1.bankBranchId =:branchid")
    
    
    
  
})
@Entity

public class SaccoMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String idNumber;
    private String phoneNumber;
    private String gender;
   private double guarantees;
   private String talent;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String homeAddress;
    private String branch;
    private String post;
    private String department;
    private Long branchId;
    private boolean memberStatus;
    private byte[] memberImage;
    private byte[] signatureImage;
    private byte[] memberScanID;

    private String email;
    private String residence;
    private String county;
    private String memberPhoto;
    private String memberSignature;
    private String memberNumber;
    private String memberId;
    private String addedBy;
    private String lastEditedBy;
    private String suspendActivatedBy;
    private String payRollNumber;
    private String bankAccountNumber;
    private String occupation;
    private String spouse;
    private String maritalStatus;
    private Long bankId;
    private Long bankBranchId;
    private double monthlySalary;
    private String occupationCategory;
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    
    @Temporal(TemporalType.DATE)
    private Date employmentDate;
            
    
    
    @OneToMany
    private List<Referee> referees = new ArrayList<Referee>();
    @OneToMany
    private List<Kin> nextOfKin = new ArrayList<Kin>();
    
    @OneToMany
    private List<Contribution> contributions = new ArrayList<Contribution>();
    @OneToMany
    private List<MemberLoan> memberLoans = new ArrayList<MemberLoan>();

    public String getOccupationCategory() {
        return occupationCategory;
    }

    public void setOccupationCategory(String occupationCategory) {
        this.occupationCategory = occupationCategory;
    }

    
    
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    
    
    
    

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }

    
    
    public byte[] getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(byte[] memberImage) {
        this.memberImage = memberImage;
    }

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public byte[] getMemberScanID() {
        return memberScanID;
    }

    public void setMemberScanID(byte[] memberScanID) {
        this.memberScanID = memberScanID;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    
    
    
   
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(String lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }
    
    
    
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMemberPhoto() {
        return memberPhoto;
    }

    public void setMemberPhoto(String memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    public String getMemberSignature() {
        return memberSignature;
    }

    public void setMemberSignature(String memberSignature) {
        this.memberSignature = memberSignature;
    }

    
    
    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public boolean isMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(boolean memberStatus) {
        this.memberStatus = memberStatus;
    }

   

    
    
    
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

  

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getSuspendActivatedBy() {
        return suspendActivatedBy;
    }

    public void setSuspendActivatedBy(String suspendActivatedBy) {
        this.suspendActivatedBy = suspendActivatedBy;
    }

    
    public List<Kin> getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(List<Kin> nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public List<MemberLoan> getMemberLoans() {
        return memberLoans;
    }

    public void setMemberLoans(List<MemberLoan> memberLoans) {
        this.memberLoans = memberLoans;
    }

   

   

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }
    
    

    public List<Contribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    

    
    
    public double getGuarantees() {
        return guarantees;
    }

    public void setGuarantees(double guarantees) {
        this.guarantees = guarantees;
    }

    public String getPayRollNumber() {
        return payRollNumber;
    }

    public void setPayRollNumber(String payRollNumber) {
        this.payRollNumber = payRollNumber;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }
    
    
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaccoMember)) {
            return false;
        }
        SaccoMember other = (SaccoMember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Member[ id=" + id + " ]";
    }
    
}
