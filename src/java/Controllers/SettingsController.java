/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers; 

import Entities.Accounts;
import Entities.AuditTrail;
import Entities.BankBranches;
import Entities.Charges;
import Entities.CompanyBranches;
import Entities.FusionDB;
import Entities.Kin;
import Entities.LoanSettings;
import Entities.MemberAccount;
import Entities.MemberSettings;
import Entities.SaccoBanks;
import Entities.SaccoDetails;
import Entities.SaccoMember;
import Entities.SharesSettings;
import Entities.SubAccounts;
import Entities.Users;
import Facades.AccountsFacade;
import Facades.AuditTrailFacade;
import Facades.BankBranchesFacade;
import Facades.BranchesFacade;
import Facades.ChargesFacade;
import Facades.FusionFacade;
import Facades.KinFacade;
import Facades.LoanApprovalFacade;
import Facades.LoanSettingsFacade;
import Facades.MemberAccountFacade;
import Facades.MemberSettingsFacade;
import Facades.SaccoBanksFacade;
import Facades.SaccoDetailsFacade;
import Facades.SaccoMemberFacade;
import Facades.SaccoUserFacade;
import Facades.ShareSettingsFacade;
import Facades.SubAccountsFacade;
import SupportBeans.Encryption;
import SupportBeans.JSONParser;
import SupportBeans.SMSHandler;
import SupportBeans.SaccoLicense;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author george
 */
@ManagedBean
@SessionScoped
public class SettingsController {

    @ManagedProperty(value = "#{mainController}")
    private MainController mainController;
    //entity variables
    
    private SaccoDetails saccoDetails;
    private SaccoDetails editSacco;
    private BankBranches bankBranch;
    private BankBranches editBankBranch;
    private SaccoDetails newSacco;
    private MemberSettings memberSettings;
    private MemberSettings editMemberSettings;
    private LoanSettings loanSettings;
    private LoanSettings editLoanSettings;
    private Charges charges;
    private Charges editCharge;
    private SaccoLicense saccoLicense;
    private Accounts accounts;
    private SubAccounts subAccounts;
    private FusionDB fusionDB;
    private SharesSettings shareSettings;
    private SharesSettings editShareSettings;
    private CompanyBranches companyBranch,editCompanyBranch;
    private SaccoBanks saccoBank,editBank;
    private UploadedFile file;
    //class variables 
    private boolean validTel, validName, activated, activationStatus, clientAdd, alreadyAuth, servCodeGenerate;
    private String clientCode = null, inputCode,oldBranchName = null,approvalMessage = "Kindly note that there are loans that need your approval";
    private Long branchImportID;
    Encryption sysEncryption;
    JSONParser jsonParser;
    private ExternalContext ext;
    private FacesContext ctx;
    private RequestContext rtx;
    JSONObject json;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    
    //Support classes
    private SMSHandler smsHandler = new SMSHandler();
    //ejbs
    @EJB
    private SaccoDetailsFacade saccoDetailsFacade;
    @EJB
    private MemberSettingsFacade memberSettingsFacade;
    @EJB
    private LoanSettingsFacade loanSettingsFacade;
    @EJB
    private ChargesFacade chargesFacade;
    @EJB
    private SaccoUserFacade usersFacade;
    @EJB
    private AccountsFacade accountsFacade;
    @EJB
    private SubAccountsFacade subAccountsFacade;
    @EJB
    private FusionFacade fusionFacade;
    @EJB
    private ShareSettingsFacade shareSettingsFacade;
    @EJB
    private BranchesFacade branchesFacade;
    @EJB
    private SaccoMemberFacade memberFacade = new SaccoMemberFacade();
    @EJB
    KinFacade kinFacade = new KinFacade();
    @EJB
    private MemberAccountFacade memberAccountFacade = new MemberAccountFacade();
    @EJB
    private SaccoBanksFacade banksFacade = new SaccoBanksFacade();
    @EJB
    private BankBranchesFacade bankBranchFacade = new BankBranchesFacade();
    @EJB
    private AuditTrailFacade  auditTrailFacade = new AuditTrailFacade();
    @EJB
    private LoanApprovalFacade approvalFacade = new LoanApprovalFacade();
    //Lists
    private List<Charges> allCharges;
    private List<SubAccounts> allSubAccounts;
    private List<CompanyBranches> allBranches;
    //
    private DataModel<Charges> chargesDM;
    private DataModel<CompanyBranches> branchesDM;
    private DataModel<SaccoBanks> saccoBanksDM;
    private DataModel<BankBranches> bankBranchDM;
    private DataModel<AuditTrail> auditTrailDM;

    public SettingsController() {
        //entities
        this.bankBranch = new BankBranches();
        this.editBank = new SaccoBanks();
        this.saccoBank = new SaccoBanks();
        this.saccoDetails = new SaccoDetails();
        this.newSacco = new SaccoDetails();
        this.editSacco = new SaccoDetails();
        this.memberSettings = new MemberSettings();
        this.editMemberSettings = new MemberSettings();
        this.loanSettings = new LoanSettings();
        this.editLoanSettings = new LoanSettings();
        this.charges = new Charges();
        this.editCharge = new Charges();
        this.saccoLicense = new SaccoLicense();
        this.fusionDB = new FusionDB();
        this.accounts = new Accounts();
        this.subAccounts = new SubAccounts();
        this.editShareSettings = new SharesSettings();
        this.companyBranch =  new CompanyBranches();
        this.editCompanyBranch = new CompanyBranches();
        //facades
        this.saccoDetailsFacade = new SaccoDetailsFacade();
        this.memberSettingsFacade = new MemberSettingsFacade();
        this.loanSettingsFacade = new LoanSettingsFacade();
        this.chargesFacade = new ChargesFacade();
        this.usersFacade = new SaccoUserFacade();
        this.accountsFacade = new AccountsFacade();
        this.subAccountsFacade = new SubAccountsFacade();
        this.fusionFacade = new FusionFacade();
        this.shareSettingsFacade = new ShareSettingsFacade();
        this.branchesFacade = new BranchesFacade();
        //Lists
        this.allCharges = new ArrayList<Charges>();
         this.allBranches = new ArrayList<CompanyBranches>();
        //Datamodels
        this.chargesDM = new ListDataModel<Charges>();
        this.branchesDM = new ListDataModel<CompanyBranches>();
        this.auditTrailDM = new ListDataModel<AuditTrail>();

        //other class variables
        this.validTel = false;
        this.validName = false;
        this.activated = false;
        this.sysEncryption = new Encryption();

    }
    
    public void sendApprovalNotifications(){
        ctx =FacesContext.getCurrentInstance();
        try{
        for(Long userid : approvalFacade.smsApprovals())
        {
            Users dbUser = usersFacade.find(userid);
            smsHandler.sendAfricasTalkingSMS(dbUser.getTelephone(), "Dear "+dbUser.getFullName()+", "+approvalMessage + " - Sacco Fusion",getSaccoDetails());
        }
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notifications sent", ""));
        }
        catch(Exception ex)
        {
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notifications failed", "")); 
        }
        approvalMessage = null;
    }
    
    public void updateBankDetails(){
        ctx =FacesContext.getCurrentInstance();
        try{
            banksFacade.edit(editBank);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bank updated successfull", ""));
        }
        catch(Exception ex){
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", ""));
        
        }
    }
    
    public void loadEditBank(){
        rtx = RequestContext.getCurrentInstance();
        editBank = saccoBanksDM.getRowData();
        rtx.update("dlgEditBank");
        rtx.execute("editBankDLG.show()");
        
    }
    
    public void removeBank(){
        ctx =FacesContext.getCurrentInstance();
        SaccoBanks dbBank = saccoBanksDM.getRowData();
        dbBank.setTrash(true);
        try{
        banksFacade.edit(dbBank);
        List<SaccoMember> dbMembers = memberFacade.getMembersByBankInfo(dbBank.getId());
        for(SaccoMember sMember : dbMembers){
            sMember.setBankBranchId(null);
            sMember.setBankId(null);
            memberFacade.edit(sMember);
        }
        
        
        for(BankBranches sBranch : dbBank.getBranches()){
            sBranch.setTrash(true);
            bankBranchFacade.edit(sBranch);
        }
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bank removed successfully !", ""));
        }
        catch(Exception ex){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed !", ""));
        }
    }
    
    public void removeBranch(){
         ctx = FacesContext.getCurrentInstance();
         try{
        BankBranches dbBranch = bankBranchDM.getRowData();
        List<BankBranches> bankBranches = editBank.getBranches();
        bankBranches.remove(dbBranch);
        editBank.setBranches(bankBranches);
        banksFacade.edit(editBank);
        dbBranch.setTrash(true);
        bankBranchFacade.edit(dbBranch);
        List<SaccoMember> dbMembers = memberFacade.getMembersByBankInfo(dbBranch.getId());
        for(SaccoMember sMember : dbMembers){
            sMember.setBankBranchId(null);
            memberFacade.edit(sMember);
        }
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Branch deleted successfully", ""));
         }
         catch(Exception ex){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed !", ""));
          
         }
    }
    
    public void addBankBranch(){
        ctx = FacesContext.getCurrentInstance();
        List<BankBranches> bankBranches = editBank.getBranches();
        boolean branchAdded = false;
        for(BankBranches branch : bankBranches){
            if(bankBranch.getBranchName().equalsIgnoreCase(branch.getBranchName())){
                branchAdded = true;
                break;
            }
        }
        if(branchAdded){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Branch Already Added", ""));
        }
        else{
            try{
                 bankBranchFacade.create(bankBranch);
            bankBranches.add(bankBranch);
            editBank.setBranches(bankBranches);
            banksFacade.edit(editBank);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Branch Added successfully ", ""));
            }
            catch(Exception ex){
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Operation failed", ""));
            }
           
        }
        bankBranch = new BankBranches();
        
    }
    
    public String loadBankBranches(){
        String page = "bankBranches.xhtml";
        editBank = saccoBanksDM.getRowData();
        
        return page;
        
    }
    public void addSaccoBank(){
        ctx = FacesContext.getCurrentInstance();
        rtx = RequestContext.getCurrentInstance();
        try{
            if(banksFacade.checkBankExists(saccoBank.getBankName())){
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bank Already Added !", ""));
            }
            else{
                banksFacade.create(saccoBank);
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bank Added Successfully!", ""));
            }
           
            saccoBank = new SaccoBanks();
        }
        catch(Exception ex){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured", ""));
        }
    }
    
    //Data Importation
    public String importMembersData(){
       ctx = FacesContext.getCurrentInstance();
           // String path = "";
         if(file==null){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select excel file please !", ""));
         }
         else if(branchImportID ==null){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select branch please", ""));
         }
         else{
           String fileName = file.getFileName();
            int sheetIndex ;
           String branchName  = branchesFacade.find(branchImportID).getBranchname();
            System.out.println("Parsing .." + fileName +  "On sheet " + branchName);
              InputStream inputStream ;
        try {
         inputStream = file.getInputstream();
           // Workbook BOOKE = new  
            
            if(file.getFileName()!=null && fileName.endsWith("xlsx")){
            XSSFWorkbook memberBook = new XSSFWorkbook(inputStream);
             sheetIndex  = memberBook.getSheetIndex("Details");
             System.out.println("Sheet index --> " + sheetIndex);
            XSSFSheet memberSheet = memberBook.getSheetAt(sheetIndex);
            
            Iterator<Row> rowIterator = memberSheet.iterator();
              memberDataIterator(rowIterator);
            }
            else{
                
                HSSFWorkbook memberBook = new HSSFWorkbook(inputStream);
               sheetIndex = memberBook.getSheetIndex(branchName);
               
                System.out.println("Sheet index --> " + sheetIndex);
            HSSFSheet memberSheet = memberBook.getSheetAt(sheetIndex);
            
            Iterator<Row> rowIterator = memberSheet.iterator();
           memberDataIterator(rowIterator);
            }
        } catch (Exception ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
         }
            return "";
    }

    public void memberDataIterator(Iterator<Row> rIterator){
        int memberCount = 0;
           while(rIterator.hasNext()){
               MemberAccount memberAccount = new MemberAccount();
                Row row = rIterator.next();
                SaccoMember sMember = new SaccoMember();
                Kin memberKin = new Kin();
                Iterator<Cell> cellIterator = row.cellIterator();
                if(row.getRowNum() > 0){ //Start itereating from row 1
                 Calendar dateCalendar = Calendar.getInstance();
                 dateCalendar.set(2010, 0, 1);
               while (cellIterator.hasNext()) {
    
                    Cell cell = cellIterator.next();
                     System.out.println("Row num  --> " +  row.getRowNum() + " Column --> "  +cell.getColumnIndex());
                    String cellValueString = "";
                     switch(cell.getCellType()){
                         case Cell.CELL_TYPE_NUMERIC:
                             cellValueString = cell.getNumericCellValue() +"";
                             break;
                             
                         default :
                            cellValueString = cell.getStringCellValue();
                     }
                             
                   switch (cell.getColumnIndex()) {



                       case 1:
                           sMember.setFullName(cellValueString);
                           break;
                       case 2:
                           sMember.setFullName(sMember.getFullName() + " " + cellValueString);
                           break;
                       case 6:
                           sMember.setSpouse(cellValueString);
                           break;
                       case 7:
                           
                           sMember.setMemberNumber(getSaccoPrefix() + cellValueString.substring(0,cellValueString.lastIndexOf('.')));
                           //System.out.println("Member Number --> " + cellValueString);
                           break;
                        case 9:
                           sMember.setOccupation(cellValueString);
                           //System.out.println("Member Number --> " + cellValueString);
                           break;
                        case 10:
                           sMember.setOccupationCategory(cellValueString);
                           //System.out.println("Member Number --> " + cellValueString);
                           break;
                       case 11:
                           sMember.setTalent(cellValueString);
                           break;

                       case 13:
                           sMember.setPhoneNumber(cell.getStringCellValue());
                           break;
                       case 14:
                           sMember.setEmail(cell.getStringCellValue());
                           break;
                    
                             
                    default :
                 
                    }
               }
                  //  kinFacade.create(memberKin);
                   
                    sMember.setBranchId(branchImportID);
                    sMember.setMemberStatus(true);
                    
                    sMember.setRegistrationDate(dateCalendar.getTime());
                  
//        System.out.println("account id generated...."+memberAccount.getId());
                    
                    
                    memberAccount.setMemberNumber(sMember.getMemberNumber());
                    
                    
                    memberAccountFacade.create(memberAccount);
                    
                    memberFacade.create(sMember);
                    
                    
                     memberCount++ ;
                }
                
              
            }
           ctx.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, memberCount + " Imported Successfully !", ""));
    }
    //validations 
    //validate telephone number
    public boolean validateTel(String telNumber) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        boolean validChar = true;
        validTel = true;
        if (telNumber != null) {
            for (int i = 0; i < telNumber.length(); i++) {
                if (!(Character.isDigit(telNumber.charAt(i)) || telNumber.charAt(i) == '-' || telNumber.charAt(i) == '+')) {
                    validChar = false;

                }
            }
        }


        if (validChar == false) {
            validTel = false;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid value for telephone number ", null));
        }
        return validTel;

    }
//Generate member initials
     public String getSaccoPrefix() {
        saccoDetails = saccoDetailsFacade.findAll().get(0);
        String[] names = saccoDetails.getName().split(" ");
        String initials = "";
        for(String name : names){
            initials+=name.charAt(0);
        }
    //    newMemberNumber = initials+"/"+memberFacade.generateMemberNumber();
        String saccoPrefix = initials+"/";
       // newMember.setMemberNumber(newMemberNumber);
        return saccoPrefix;
    }
    //validate sacco name
    public boolean validateSaccoName(String name) {

        validName = true;
        boolean validChar = true;

        FacesContext ctx = FacesContext.getCurrentInstance();

        //ensure fullname has letters, "." and "'" only
        if (name != null) {
            for (int i = 0; i < name.length(); i++) {
                if (!(Character.isLetter((name.charAt(i))) || Character.isDigit((name.charAt(i))) || Character.isSpaceChar(name.charAt(i)) || name.charAt(i) == '.' || name.charAt(i) == '-' || name.charAt(i) == '\'')) {

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

    public void uploadLogo(FileUploadEvent event) {
        String path = "";
        FacesContext ctx = FacesContext.getCurrentInstance();
        Properties sysProps = System.getProperties();
        boolean dirSuccess = false;

        try {
            String s = "p p";
            String fineName = "";
            String photoName = event.getFile().getFileName();

            fineName = "saccoLogo" + photoName.substring(photoName.lastIndexOf("."));


            File photoDirectory = new File(sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData");
            if (photoDirectory.exists()) {
                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + fineName;
            } else {
                photoDirectory.mkdirs();

                path = sysProps.getProperty("user.home") + sysProps.getProperty("file.separator") + "SaccoData" + sysProps.getProperty("file.separator") + fineName;
            }


            System.out.println(path);


            if (editSacco.getLogoUrl() != null) {
                File previousPhoto = new File(editSacco.getLogoUrl());
                previousPhoto.delete();

            }

            File photoFile = new File(path);
            photoFile.createNewFile();
            if (photoFile.exists()) {

                FileOutputStream outputStream = new FileOutputStream(photoFile);
                outputStream.write(event.getFile().getContents());
                outputStream.flush();
                outputStream.close();


                editSacco.setLogoUrl(path);

                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logo Uploaded Successfully !", ""));

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }



    }

    //save sacco details    
    public void saveSyncSaccoDetails() {

        FacesContext messages = FacesContext.getCurrentInstance();

       // fusionDB = fusionFacade.findAll().get(0);
        //System.out.println("Client code --> " + fusionDB.getMaxisEnc() + "Input code --> " + inputCode);

        if (validateSaccoName(newSacco.getName()) == false) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid sacco name", "failed"));
        } else if (validateTel(newSacco.getTelephone()) == false) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid telephone number", "failed"));
        } else if (!inputCode.equalsIgnoreCase(clientCode)) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid service code . ", "failed"));
            inputCode = "";

        } else if (!checkInternet()) {
            clientAdd = false;
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Connect to the internet . ", "failed"));
        } else {


            try {
               
                  //   System.out.println("Heer tumesucceed");




                newSacco.setLogoUrl(null);
                fusionFacade.create(fusionDB);

                fusionDB = new FusionDB();
                clientAdd = false;
                alreadyAuth = true;
                inputCode = null;

                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sacco fusion authenticated successfully", ""));



                // finish Lock ....smiles!






            } catch (Exception e) {
                e.printStackTrace();
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", "failed"));

            }


        }

    }

    public boolean checkInternet() {
        boolean isconnected = false;
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping www.google.com");
            System.out.println("Internet status --> " + p1.waitFor());
            if (p1.waitFor() == 0) {
                isconnected = true;
            } else {
                isconnected = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isconnected;
    }

    public void generateClientCode() {


        FacesContext ctx = FacesContext.getCurrentInstance();
        if (checkInternet()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please connect to the internet before continuing!", ""));
            clientAdd = false;
        } else if (newSacco.getCounty().equalsIgnoreCase("select")) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select county.", ""));
            clientAdd = false;
        } else {
//            System.out.println("generating..");
            try {


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("serverPresent", "checkServer"));
                jsonParser = new JSONParser();
                json = jsonParser.makeHttpRequest("http://www.pearlsoftke.com/Auth/", "GET", params);
                if (json.getString("serverAvailable") != null && json.getString("serverAvailable").equalsIgnoreCase("yes")) {

                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("checkClient", newSacco.getName()));


                    json = jsonParser.makeHttpRequest("http://www.pearlsoftke.com/Auth/", "POST", params);
                    if (json.getString("clientReady") != null && json.getString("clientReady").equalsIgnoreCase("yes")) {
                        clientCode = json.getString("clientCode");
                        alreadyAuth = false;
                        clientAdd = true;
                        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enter Service Code", ""));

                    } else {

                        clientAdd = true;
                        alreadyAuth = false;
                        Random random = new Random();
                        if (clientCode == null) {
                            clientCode = random.nextLong() + "";

                        }
                        if (fusionFacade.findAll().isEmpty()) {
                            fusionDB.setMaxisEnc(sysEncryption.encryptPassword(clientCode));

                            fusionDB.setMaxisBapt(newSacco.getName());
                            fusionDB.setMaxisCapable("no");
                            fusionDB.setMaxisLaunch(new Date());
                            fusionDB.setCountMaxi1(30);
                            fusionDB.setMaxisLoc(newSacco.getCounty());
                            fusionDB.setMaxisCall(newSacco.getTelephone());
                            fusionDB.setMaxisLoc(newSacco.getCounty());
                            
                            //Send auth details before creating DB
                          
                            params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("addClient", fusionDB.getMaxisBapt()));
                            params.add(new BasicNameValuePair("servCode", fusionDB.getMaxisEnc()));
                            params.add(new BasicNameValuePair("tel", fusionDB.getMaxisCall()));
                            params.add(new BasicNameValuePair("status", fusionDB.getMaxisCapable()));
                            params.add(new BasicNameValuePair("count", fusionDB.getCountMaxi1() + ""));
                            params.add(new BasicNameValuePair("county", fusionDB.getMaxisLoc() + ""));
                            json = jsonParser.makeHttpRequest("http://www.pearlsoftke.com/Auth/", "POST", params);
                          
                            if (json.getString("addstatus") != null && json.getString("addstatus").equalsIgnoreCase("success")) {
                                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter your service Code", ""));
                            }
                            else{
                                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not authenticate this copy of Sacco Fusion, please check your internet connection or contact PearlSoft.", ""));
                            }
                        }
                       
                    }

                } else {
                    clientAdd = false;
                    // System.out.println("Heer kumethuka pwana");
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not authenticate Sacco Fusion, please check your internet connection or contact PearlSoft.", ""));


                }



                inputCode = "";


            } catch (Exception ex) {
                ex.printStackTrace();
                clientAdd = false;
                //System.out.println("Heer kumethuka");
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not authenticate Sacco Fusion, please check your internet connection or contact PearlSoft.", ""));
            }

            // System.out.println(clientCode);

        }

    }

    public String skipAuthentication() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String page = "index.xhtml";
        try {
            if (saccoDetailsFacade.findAll().isEmpty()) {
                newSacco.setInstallationDate(new Date());

                saccoDetailsFacade.create(newSacco);

            }
            if (fusionFacade.findAll().isEmpty()) {
                if(alreadyAuth){
                    fusionDB.setMaxisEnc(clientCode);
                }
                else{
                fusionDB.setMaxisEnc(sysEncryption.encryptPassword(clientCode));
                }
                fusionDB.setMaxisBapt(newSacco.getName());
                fusionDB.setMaxisCapable("no");
                fusionDB.setMaxisLaunch(new Date());
                fusionDB.setCountMaxi1(30);
                fusionDB.setMaxisLoc(newSacco.getCounty());
                fusionDB.setMaxisCall(newSacco.getTelephone());
                fusionDB.setMaxisLoc(newSacco.getCounty());
                fusionFacade.create(fusionDB);
            }
            if(accountsFacade.findAll().isEmpty()){
                createSaccoAccounts();
            }
            memberSettings.setLastUpdate(new Date());
            memberSettingsFacade.create(memberSettings);
            
            shareSettings.setMiminumShares(0);
            shareSettingsFacade.create(shareSettings);

            memberSettings = new MemberSettings();
            shareSettings = new SharesSettings();
            fusionDB = new FusionDB();
            newSacco = new SaccoDetails();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sacco Fusion Authenticated successfully", ""));
        } catch (Exception ex) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured, please contact pearlsoft", ""));
            page = "sysSetup.xhtml";
        }
        return page;
    }

    //Create main sacco Accounts
    public void createSaccoAccounts() {
        //Create Asset Account
        List<SubAccounts> dbSubAccounts = new ArrayList<SubAccounts>();
        
        accounts = new Accounts();

        accounts.setName("Assets");
        accounts.setType("assets");
        accounts.setDescription("Main Assets Account");
        accountsFacade.create(accounts);
        dbSubAccounts = accounts.getSubAccounts();

        subAccounts = new SubAccounts();
        subAccounts.setName("Cash in Bank");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Assets");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);
        
       

         subAccounts = new SubAccounts();
        subAccounts.setName("Cash At Hand");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Assets");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);

        accounts.setSubAccounts(dbSubAccounts);
        accountsFacade.edit(accounts);
        
        

        dbSubAccounts = new ArrayList<SubAccounts>();
        accounts = new Accounts();
        //End Create Asset Account                       

        //Create Expenses Account
        accounts.setName("Expenses");
        accounts.setType("expenses");
        accounts.setDescription("Main Expenses Account");
        accountsFacade.create(accounts);
        accounts = new Accounts();

        //Create Liabilities Account
        accounts.setName("Liabilities");
        accounts.setType("liabilities");
        accounts.setDescription("Main Liabilities Account");
        accountsFacade.create(accounts);
        dbSubAccounts = accounts.getSubAccounts();

        
        subAccounts = new SubAccounts();
        subAccounts.setName("Tithes");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Liabilility");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);
        
         subAccounts = new SubAccounts();
        subAccounts.setName("Deposits");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Liabilility");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);
        
        subAccounts = new SubAccounts();

        subAccounts.setName("Audit Fees");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Liabilility");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);
        subAccounts = new SubAccounts();

       

        

        subAccounts.setName("Benevolent");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Liabilility");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);
       


        accounts.setSubAccounts(dbSubAccounts);
        accountsFacade.edit(accounts);

         subAccounts = new SubAccounts();
        dbSubAccounts = new ArrayList<SubAccounts>();
        accounts = new Accounts();
        //End create liabilities Account



        //Create Equity Account              
        accounts.setName("Equity");
        accounts.setDescription("Main equity Account");
        accounts.setType("Equity");

        accountsFacade.create(accounts);
        dbSubAccounts = accounts.getSubAccounts();

        subAccounts.setName("Share Capital");
        subAccounts.setParentAccount(accounts.getName());
        subAccounts.setDescription("Equity Sub-Account");

        subAccountsFacade.create(subAccounts);
        dbSubAccounts.add(subAccounts);
        subAccounts = new SubAccounts();

        accounts.setSubAccounts(dbSubAccounts);
        accountsFacade.edit(accounts);

        dbSubAccounts = new ArrayList<SubAccounts>();
        accounts = new Accounts();

        accounts.setName("Income");
        accounts.setDescription("Income Account");
        accounts.setType("Income");

        accountsFacade.create(accounts);

       
        
        accounts.setSubAccounts(dbSubAccounts);
        accountsFacade.edit(accounts);


ctx = FacesContext.getCurrentInstance();
   ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Accounts/Subaccounts created", ""));
    }

    //get sacco details for editing 
    public void loadSaccoDetails() {

        editSacco = saccoDetailsFacade.findAll().get(0);
    }

    //edit sacco details 
    public void updateSaccoDetails() {
        FacesContext message = FacesContext.getCurrentInstance();

        if (validateSaccoName(editSacco.getName()) == false) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid sacco name", null));
        } else if (validateTel(editSacco.getTelephone()) == false) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid sacco telephone", null));
        } else {
            try {
                System.out.println("Teller logged..." + mainController.getUserLogged().getFullName());

                saccoDetails = editSacco;
                saccoDetails.setEditor(mainController.getUserLogged().getFullName() + "/" + mainController.getUserLogged().getTelephone());
                saccoDetailsFacade.edit(saccoDetails);
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sacco information updated", ""));
            } catch (Exception ex) {
                ex.printStackTrace();
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", ""));
            }
        }
    }
    
    //Add company/sacco branches
    
    public void addCompanyBranch(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(branchesFacade.checkBranchExists(companyBranch)){
           
        }
        else{
            branchesFacade.create(companyBranch);
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "House added successfully !", ""));
        }
        companyBranch = new CompanyBranches();
    }
    //Load branches for editing
    
    public void loadBranch(){
        editCompanyBranch = branchesDM.getRowData();
        oldBranchName = editCompanyBranch.getBranchname();
    }
    
    public void updateBranch(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(!editCompanyBranch.getBranchname().equalsIgnoreCase(oldBranchName)){
            if(branchesFacade.checkBranchExists(editCompanyBranch)){
                ctx.addMessage(null, new  FacesMessage(FacesMessage.SEVERITY_ERROR, "Branch already exists, try again", ""));
            }
            else{
                branchesFacade.edit(editCompanyBranch);
                ctx.addMessage(null, new  FacesMessage(FacesMessage.SEVERITY_INFO, "Branch added successfully .", ""));
            }
        }
        else{
            branchesFacade.edit(editCompanyBranch);
        }
        editCompanyBranch = new CompanyBranches();
    }
    
    public void deleteBranch(){
        branchesFacade.remove(editCompanyBranch);
        editCompanyBranch = new CompanyBranches();
    }
    
     //load share setting for editing
    public void loadShareSettings(){
       
        editShareSettings = shareSettingsFacade.findAll().get(0);
        
            
    }
    
    public void updateShareSettings(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(editShareSettings.getMiminumShares() <0){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Invalid Minimum Shares Value", ""));
        }
        else{
            shareSettingsFacade.edit(editShareSettings);
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Share Settings updated sucesfully !", ""));
        }
    }

    //load members settings fo editing
    public void loadMemberSettings() {
        editMemberSettings = memberSettingsFacade.findAll().get(0);
    }
    
   

    //update membership settings
    public void updateMemberSettings() {
        FacesContext messages = FacesContext.getCurrentInstance();
        memberSettings = editMemberSettings;
        memberSettings.setLastUpdate(new Date());
        if (memberSettings.getMinimumShares() < 0) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid minimum shares", ""));
        } else if (memberSettings.getMinimumDeposit() < 0) {
            messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Min. shares must be greater than 0", ""));
        }  else {
            try {
                memberSettings.setEditor(mainController.getUserLogged().getFullName() + "/" + mainController.getUserLogged().getTelephone());
                memberSettingsFacade.edit(memberSettings);
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Settings updated", ""));
            } catch (Exception ex) {
                ex.printStackTrace();
                messages.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", ""));
            }
        }
    }

    //load loan settings
    public void loadLoanSettings() {
        editLoanSettings = loanSettingsFacade.findAll().get(0);
    }

    //update loan settings
    public void updateLoanSettings() {
        FacesContext message = FacesContext.getCurrentInstance();

        loanSettings = editLoanSettings;
        loanSettings.setLastUpdate(new Date());

        try {
            loanSettings.setEditor(mainController.getUserLogged().getFullName() + "/" + mainController.getUserLogged().getTelephone());
            loanSettingsFacade.edit(loanSettings);
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Settings updated", "success"));
        } catch (Exception ex) {
            ex.printStackTrace();
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", "error"));
        }
    }

    //charges
    //create charges
    public void createCharges() {
        FacesContext message = FacesContext.getCurrentInstance();

        if (validateSaccoName(charges.getName()) == false) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid charge name", ""));
        } else if (charges.getCost() == 0) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Charge cost must be greater than 0", ""));
        } else if (chargesFacade.checkCharges(charges.getName())) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Charge with this name already exists", ""));
        } else {
            try {
                charges.setLastUpdate(new Date());
                charges.setEditor(mainController.getUserLogged().getFullName());
                chargesFacade.create(charges);

                accounts = accountsFacade.getAccByName("Income");
                allSubAccounts = accounts.getSubAccounts();
                subAccounts.setName(charges.getName());
                subAccounts.setDescription("Income");
                subAccounts.setParentAccount(accounts.getName());
                subAccountsFacade.create(subAccounts);
                allSubAccounts.add(subAccounts);
                accounts.setSubAccounts(allSubAccounts);
                accountsFacade.edit(accounts);
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Charges added", ""));
                charges = new Charges();
                subAccounts = new SubAccounts();
                accounts = new Accounts();
                allSubAccounts = new ArrayList<SubAccounts>();
            } catch (Exception ex) {
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", ""));
            }
        }

    }

    //select charges to edit
    public void selectCharges() {
        editCharge = chargesDM.getRowData();
    }

    //clear select charges
    public void clearCharges() {
        editCharge = new Charges();
        charges = new Charges();
    }

    //edit charges
    public void updateCharges() {
        FacesContext message = FacesContext.getCurrentInstance();
        boolean chargeExists = false;
        List<Charges> dbCharges = new ArrayList<Charges>(chargesFacade.findAll());
        dbCharges.remove(editCharge);

        for (Charges dbCharge : dbCharges) {
            if (dbCharge.getName().equalsIgnoreCase(editCharge.getName())) {
                chargeExists = true;
            }
        }

        if (validateSaccoName(editCharge.getName()) == false) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid charge name", ""));
        } else if (editCharge.getCost() == 0) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Charge cost must be greater than 0", ""));
        } else if (chargeExists) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Charge with this name already exists", ""));
        } else {
            try {
                editCharge.setEditor(mainController.getUserLogged().getFullName());
                chargesFacade.edit(editCharge);
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update successful", ""));

            } catch (Exception ex) {
                message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", ""));
            }
        }
    }

    //delete charges
    public void deleteCharges() {
        FacesContext message = FacesContext.getCurrentInstance();

        try {
            chargesFacade.remove(editCharge);
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operation compeleted", ""));

        } catch (Exception ex) {
            message.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Operation failed", ""));
        }
    }

    public void generateServiceCode() {
        Random random = new Random();
        clientCode = sysEncryption.encryptPassword((getSaccoDetails().getName() + random.nextLong()));
        System.out.println(clientCode);
        //SEnd via net...

    }

    public void activateSystem() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (inputCode.equalsIgnoreCase(clientCode)) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System Activated successfully!", ""));
            try {
                input = new ObjectInputStream(new FileInputStream("C:\\Windows\\.pekage.ser"));

                saccoLicense = (SaccoLicense) input.readObject();

                saccoLicense.setTrialVersion(false);
                saccoLicense.setLicensed(true);
                saccoLicense.setServiceCode(sysEncryption.encryptPassword(clientCode));

                output = new ObjectOutputStream(new FileOutputStream("C:\\Windows\\.pekage.ser"));

                output.writeObject(saccoLicense);


                String cmd1[] = {"attrib", "+h", "C:\\Windows\\.pekage.ser"};


                Runtime.getRuntime().exec(cmd1);

                activated = true;

                //Pause ..
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (output != null) {
                        output.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        } else {
            activated = false;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Service Code!", ""));
        }
        inputCode = new String();
    }

    //getters and setters
    public boolean isServCodeGenerate() {
        return servCodeGenerate;
    }

    public void setServCodeGenerate(boolean servCodeGenerate) {
        this.servCodeGenerate = servCodeGenerate;
    }

    public boolean isAlreadyAuth() {
        return alreadyAuth;
    }

    public void setAlreadyAuth(boolean alreadyAuth) {
        this.alreadyAuth = alreadyAuth;
    }

    public boolean isClientAdd() {
        return clientAdd;
    }

    public void setClientAdd(boolean clientAdd) {
        this.clientAdd = clientAdd;
    }

    public CompanyBranches getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranches companyBranch) {
        this.companyBranch = companyBranch;
    }

    public Long getBranchImportID() {
        return branchImportID;
    }

    public void setBranchImportID(Long branchImportID) {
        this.branchImportID = branchImportID;
    }
    
    

    
    public SaccoDetails getSaccoDetails() {

        saccoDetails = saccoDetailsFacade.findAll().get(0);
        return saccoDetails;
    }

    public void setSaccoDetails(SaccoDetails saccoDetails) {
        this.saccoDetails = saccoDetails;
    }

    public SaccoDetails getEditSacco() {

        return editSacco;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public void setEditSacco(SaccoDetails editSacco) {
        this.editSacco = editSacco;
    }

    public SaccoDetails getNewSacco() {
        return newSacco;
    }

    public void setNewSacco(SaccoDetails newSacco) {
        this.newSacco = newSacco;
    }

    public SharesSettings getShareSettings() {
        if(!shareSettingsFacade.findAll().isEmpty()){
             shareSettings = shareSettingsFacade.findAll().get(0);
        }
       
        return shareSettings;
    }

    public void setShareSettings(SharesSettings shareSettings) {
        this.shareSettings = shareSettings;
    }

    public SharesSettings getEditShareSettings() {
        return editShareSettings;
    }

    public void setEditShareSettings(SharesSettings editShareSettings) {
        this.editShareSettings = editShareSettings;
    }

    
    
    public MemberSettings getEditMemberSettings() {
        return editMemberSettings;
    }

    public void setEditMemberSettings(MemberSettings editMemberSettings) {
        this.editMemberSettings = editMemberSettings;
    }

    public MemberSettings getMemberSettings() {
        if(memberSettingsFacade.findAll().isEmpty()){
            memberSettings =  new MemberSettings();
        }
        else{
              memberSettings = memberSettingsFacade.findAll().get(0);
           }
       
        return memberSettings;
    }

    public void setMemberSettings(MemberSettings memberSettings) {
        this.memberSettings = memberSettings;
    }

    public LoanSettings getLoanSettings() {

        if (loanSettingsFacade.findAll().isEmpty()) {
            loanSettings = new LoanSettings();
        } else {
            loanSettings = loanSettingsFacade.findAll().get(0);
        }

        return loanSettings;
    }

    public void setLoanSettings(LoanSettings loanSettings) {
        this.loanSettings = loanSettings;
    }

    public LoanSettings getEditLoanSettings() {
        return editLoanSettings;
    }

    public void setEditLoanSettings(LoanSettings editLoanSettings) {
        this.editLoanSettings = editLoanSettings;
    }

    public SaccoBanks getSaccoBank() {
        return saccoBank;
    }

    public void setSaccoBank(SaccoBanks saccoBank) {
        this.saccoBank = saccoBank;
    }

    public String getApprovalMessage() {
        return approvalMessage;
    }

    public void setApprovalMessage(String approvalMessage) {
        this.approvalMessage = approvalMessage;
    }

    
    
    public Charges getCharges() {
        return charges;
    }

    public void setCharges(Charges charges) {
        this.charges = charges;
    }

    public CompanyBranches getEditCompanyBranch() {
        return editCompanyBranch;
    }

    public void setEditCompanyBranch(CompanyBranches editCompanyBranch) {
        this.editCompanyBranch = editCompanyBranch;
    }
    
    

    public DataModel<CompanyBranches> getBranchesDM() {
        branchesDM = new ListDataModel<CompanyBranches>(branchesFacade.findAll());
        return branchesDM;
    }

    public void setBranchesDM(DataModel<CompanyBranches> branchesDM) {
        this.branchesDM = branchesDM;
    }

    public DataModel<AuditTrail> getAuditTrailDM() {
        auditTrailDM = new ListDataModel<AuditTrail>(auditTrailFacade.findAll());
        return auditTrailDM;
    }

    public void setAuditTrailDM(DataModel<AuditTrail> auditTrailDM) {
        this.auditTrailDM = auditTrailDM;
    }
       
    public DataModel<Charges> getChargesDM() {
        chargesDM = new ListDataModel<Charges>(chargesFacade.findAll());
        return chargesDM;
    }

    public void setChargesDM(DataModel<Charges> chargesDM) {
        this.chargesDM = chargesDM;
    }

    public DataModel<SaccoBanks> getSaccoBanksDM() {
        List<SaccoBanks> dbBanks = new ArrayList<SaccoBanks>();
        
        for(SaccoBanks bank : banksFacade.findAll()){
            if(!bank.isTrash()){
                dbBanks.add(bank);
            }
        }
         saccoBanksDM = new ListDataModel<SaccoBanks>(dbBanks);
        return saccoBanksDM;
    }

    public void setSaccoBanksDM(DataModel<SaccoBanks> saccoBanksDM) {
        
        this.saccoBanksDM = saccoBanksDM;
    }

    public DataModel<BankBranches> getBankBranchDM() {
       bankBranchDM = new ListDataModel<BankBranches>(editBank.getBranches());
        return bankBranchDM;
    }

    public void setBankBranchDM(DataModel<BankBranches> bankBranchDM) {
        this.bankBranchDM = bankBranchDM;
    }

    public BankBranches getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(BankBranches bankBranch) {
        this.bankBranch = bankBranch;
    }

    public BankBranches getEditBankBranch() {
        return editBankBranch;
    }

    public void setEditBankBranch(BankBranches editBankBranch) {
        this.editBankBranch = editBankBranch;
    }
    
    

    public SaccoBanks getEditBank() {
        return editBank;
    }

    public void setEditBank(SaccoBanks editBank) {
        this.editBank = editBank;
    }

   
    
    

    public Charges getEditCharge() {
        return editCharge;
    }

    public void setEditCharge(Charges editCharge) {
        this.editCharge = editCharge;
    }

    
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivationStatus() {
        activationStatus = false;
        try {

            input = new ObjectInputStream(new FileInputStream("C:\\Windows\\.pekage.ser"));
            saccoLicense = (SaccoLicense) input.readObject();

            if (saccoLicense.isLicensed() == true && !saccoLicense.getServiceCode().equalsIgnoreCase("")) {
                activationStatus = true;
            }

        } catch (IOException ex) {

            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }



        return activationStatus;
    }

    public void setActivationStatus(boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    

    
    //facade getters and setters
    public ChargesFacade getChargesFacade() {
        return chargesFacade;
    }

    public void setChargesFacade(ChargesFacade chargesFacade) {
        this.chargesFacade = chargesFacade;
    }

    public List<CompanyBranches> getAllBranches() {
        allBranches = branchesFacade.findAll();
        return allBranches;
    }

    public void setAllBranches(List<CompanyBranches> allBranches) {
        this.allBranches = allBranches;
    }
    
    
}
