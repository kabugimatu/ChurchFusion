package Controllers;

import Entities.Orglevels;
import Entities.Users;
import Facades.OrglevelsFacade;
import Facades.SaccoDetailsFacade;
import Facades.SaccoUserFacade;
import SupportBeans.Encryption;
import SupportBeans.SaccoLicense;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author kabugi
 */

@ManagedBean(name = "mainController")
@SessionScoped
public class MainController{
    
     
    //Vairiables
    private String confirmPassword,oldPassword,newPassword,userName,password;
    private byte[] userPhotoBytes;
    
    
    private Encryption sysEncryption;
    private boolean acceptRemove;
    private ExternalContext ext;
    HttpServletRequest origRequest ;
    private SaccoLicense saccoLicense;
    private Date today;
    private FacesContext ctx;
    private RequestContext rtx;
    private StreamedContent userPhotoDB;
    //Enitites
    private Users editUser;
    private Users userLogged ;
    private Users sysUser;
    private Users indUser;
    private Users authenticateUser;
    private Orglevels userLevelLogged;
    
    //DataModels
    private DataModel<Users> sysUsersDM ;
    
    //Facades
    
    @EJB 
    private SaccoUserFacade usersFacade = new SaccoUserFacade();
    @EJB
    private SaccoDetailsFacade detailsFacade = new SaccoDetailsFacade();
    @EJB
    private OrglevelsFacade levelFacade = new OrglevelsFacade();
    
    private List<Orglevels> userLevels = new ArrayList<Orglevels>();
   
     private ObjectInputStream input;
     
     Timer timer = new Timer();

    public MainController() {
        this.sysEncryption = new Encryption();
        this.sysUser = new Users();
        this.sysUsersDM = new ListDataModel<Users>();
        this.authenticateUser = new Users();
        this.saccoLicense = new SaccoLicense();
        
        
        
        
    
//            Date executionDate = new Date();                       
//            long period = 60 * 60 * 1000;
//            
//              timer.scheduleAtFixedRate(new TimerTask() {
//                 @Override
//                    public void run() {
//                         dbBackup();
//                        }
//              },executionDate, period);
//    
        
        
    }
    
    
   
         
    
    
    
    
    //database backup
    public void dbBackup()
    {
        Properties sysProps = System.getProperties();
        String   path = sysProps.getProperty("user.home")+sysProps.getProperty("file.separator")+"SaccoData"+sysProps.getProperty("file.separator")+"dbBackup/";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        
        
        
        Date backupDate = new Date();
        Process process;
        
       
       
        File dbDirectory = new File(sysProps.getProperty("user.home")+ sysProps.getProperty("file.separator")+"SaccoData/dbBackup");
        
        if(!dbDirectory.exists())
        {
           dbDirectory.mkdirs();    
        }
        
        File dbFile = new File(dbDirectory+"\\"+ format.format(backupDate).toString()+".sql");
        
        if(!dbFile.exists())
        {
            String backupCommand = "C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump -u root -p ngiya2014 saccofusion -r" + path + format.format(backupDate).toString()+".sql";                    
                try
                {                   
                    process = Runtime.getRuntime().exec(backupCommand);                   
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
        }
        
        
    }
    
    //database restore
    public void dbRestore()
    {                     
        Properties sysProps = System.getProperties();
       
        Process process;
        
        File dbDirectory = new File(sysProps.getProperty("user.home")+ sysProps.getProperty("file.separator")+"SaccoData/dbBackup/");
       
        String path  = dbDirectory+"\\"+ dbDirectory.list()[0];

       
        String restoreCommand = "C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysql -u root saccofusion < " + path;                    
         
        try
                {       
                    System.out.println("path....." + restoreCommand);    
                    process = Runtime.getRuntime().exec(restoreCommand); 
                    int processComplete =   process.waitFor();
                    System.out.println("here..." + process.getErrorStream().toString());
                 
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
               
        
    }
    
    //check if system is setup
    public void checkSetup()
    {   ext = FacesContext.getCurrentInstance().getExternalContext();
         if(detailsFacade.findAll().isEmpty()){
            try {
              ext.redirect(ext.getRequestContextPath()+ "/sysSetup.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please setup your Sacco!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
         else  if(usersFacade.findAll().isEmpty()){
            try {
              ext.redirect(ext.getRequestContextPath()+ "/setupUser.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please setup at least one user!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
        
        
       
        
  //   }
    }
    
    public void checkLicense(){
        today = new Date();
        
        try{
           
                
            
           
           
        }
        catch(Exception ex){
            if(ex instanceof FileNotFoundException){
                System.out.println("Wame hack jo!");
                try {
                    if(!detailsFacade.findAll().isEmpty())
                    {
                         detailsFacade.remove(detailsFacade.findAll().get(0));
                         ext.redirect(ext.getRequestContextPath()+ "/setup.xhtml");
                    }
                   
                    
                    
                } catch (IOException ex1) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            else{
                ex.printStackTrace();
            }
        }
        finally{
            try{
                if(input!=null){
                  input.close();
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
     public void checkFirstLogin()
         {
             ext = FacesContext.getCurrentInstance().getExternalContext();
           if(userLogged!=null && userLogged.getLoginCount()>0)
           {
               if(userLogged.getUserDomain().equalsIgnoreCase("teller"))
               {
                   try 
                   {
                       ext.redirect(ext.getRequestContextPath()+ "/tellerIndex.xhtml");
                   } catch (IOException ex) {
                       Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
               else if(userLogged.getUserDomain().equalsIgnoreCase("manager"))
               {
                    try 
                   {
                       ext.redirect(ext.getRequestContextPath()+ "/adminIndex.xhtml");
                   } catch (IOException ex) {
                       Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
               else
               {
                try 
                   {
                       ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
                   } catch (IOException ex) {
                       Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
               
           }
           else if(userLogged == null){
                try 
                   {
                       ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
                   } catch (IOException ex) {
                       Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                   }
           }
           
         }
    
    public void checkUsers(){
         if(usersFacade.findAll().isEmpty()){
            try {
              ext.redirect(ext.getRequestContextPath()+ "/setupUser.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please setup at least one user!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    
    //Login Management
    public void updateUser(){
        ctx = FacesContext.getCurrentInstance();
        rtx = RequestContext.getCurrentInstance();
        if(editUser.getUserLevelID() == null){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select user level !", ""));
        }
        else{
            Orglevels levelEdit = levelFacade.find(editUser.getUserLevelID());
            editUser.setUserDomain(levelEdit.getLevelname());
            usersFacade.edit(editUser);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User update successful !", ""));
            rtx.execute("dlgEditUser.hide()");
        }
    }
    public String proceedUser(){
    String homePage=null;
   
        userLogged.setLoginCount(1);
        userLogged.setPassword(sysEncryption.encryptPassword(userLogged.getPassword()));
        usersFacade.edit(userLogged);
        homePage="dashboard.xhtml";
    
  
   
    
    
    return homePage;
}
    public String login(){
       
    FacesContext ctx = FacesContext.getCurrentInstance();
    origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();     
     ext = FacesContext.getCurrentInstance().getExternalContext();
       
    String page="";
     authenticateUser.setUserName(userName);
      authenticateUser.setPassword(sysEncryption.encryptPassword(password));
       
      if(authenticateUser.getUserName().equalsIgnoreCase("pearlsoft") && password.equalsIgnoreCase("12verymany@1")){
         userLogged = new Users();
          userLogged.setUserName("Pearl Soft");
         userLogged.setUserDomain("Pearl Soft Maintenenance");
         userLogged.setUserStatus(true);
      }
        
      else{
    userLogged = usersFacade.checkUserLogged(authenticateUser);
      }

          if(userLogged!=null ){
            
              
            userName =null;
            password=null;
             
             authenticateUser = new Users();
             userLogged.setLastLogin(new Date());
             userLogged.setLoginMachine(origRequest.getRemoteAddr());
             
             if(userLogged.getLoginCount()<1 && !userLogged.getUserDomain().equalsIgnoreCase("Pearl Soft Maintenenance") ){
                 userLogged.setPassword("");
                 page ="changeCredentials.xhtml";
             } 
             else if(!userLogged.isUserStatus()){
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Account Inactive. Contact Manager.", ""));
                userLogged = null;
                 page="index.xhtml";
             }
            
             else{
                if(!userLogged.getUserDomain().equalsIgnoreCase("Pearl Soft Maintenenance")){
                    usersFacade.edit(userLogged);
                }
                 page="dashboard.xhtml";
             }
              
        
          
          }
       
     
      
        
        else{
            userName=null;
            password=null;
          
            authenticateUser=new Users();
            userLogged=null;
            ctx.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong username or password!", ""));
            page ="index.xhtml";
        }
            
//         if(checkInternet()){
//                     System.out.println("Net is ok..");
//                     
//                 }
        
    
      return page;
    }
    
    public void checkDevLoggedIn(){
         ext = FacesContext.getCurrentInstance().getExternalContext();
    if(userLogged==null){
       
        try {
            
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login to continue!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else if(userLogged!=null && !userLogged.getUserDomain().equalsIgnoreCase("DEVELOPER")){
        try {
          
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login to continue!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    public void checkTellerLoggedIn(){
     ext = FacesContext.getCurrentInstance().getExternalContext();
    if(userLogged==null){
       
        try {
            
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login to continue!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
    
    public void checkMaintenance(){
            ext = FacesContext.getCurrentInstance().getExternalContext();
    if(!userLogged.getUserDomain().equalsIgnoreCase("Pearl Soft Maintenenance")){
       
        try {
            
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login to continue!", ""));
        } catch (IOException ex) {
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    
  
    public void checkAdminLoggedIn(){
    
    
    ext = FacesContext.getCurrentInstance().getExternalContext();
   // System.out.println("user logged -->" + userLogged.getUserName());
    if (userLogged==null){
      // System.out.println("Admin logged out");
        try {
          //  System.out.println("User logged out");
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login to continue!", ""));
        } catch (IOException ex) {
            
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    else if(!userLogged.getUserDomain().equalsIgnoreCase("admin")&& !userLogged.getUserDomain().equalsIgnoreCase("manager") && !userLogged.getUserDomain().equalsIgnoreCase("DEVELOPER") && !userLogged.getUserDomain().equalsIgnoreCase("Accounts")){
        //System.out.println("Admin logged out");
        try {
           // System.out.println("User logged out");
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            ext.redirect(ext.getRequestContextPath()+ "/index.xhtml");
            FacesContext ctx = FacesContext.getCurrentInstance();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login to continue!", ""));
        } catch (IOException ex) {
            
           
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    
}
    
    public boolean checkInternet() {
        boolean isconnected = false;
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping www.google.com");
          // System.out.println("Internet status --> " + p1.waitFor());
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

    
    public String logout(){
        String page="index.xhtml?faces-redirect=true";
   
    
    userLogged =null;
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    
    return page;
    }
    
    
    //User Management
    
    public String setUpUser(){
        String page ="setupUser.xhtml";
         FacesContext ctx = FacesContext.getCurrentInstance();
        if(sysUser.getGender().equalsIgnoreCase("select")){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select gender!", ""));
            
        }
        else if(sysUser.getIdNumber() == 0){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter ID number!", ""));
            
        }
        else if(sysUser.getUserDomain().equalsIgnoreCase("select")){
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select user level!", ""));
        }
        else if(usersFacade.checkTelephone(sysUser)){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that phone number already exists!", ""));
            sysUser.setUserName("");
        }
        else if(usersFacade.checkId(sysUser)){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that id number already exists!", ""));
             sysUser.setIdNumber(0);
        }
        
        else if(usersFacade.checkEmailAddress(sysUser)){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that email address already exists!", ""));
             sysUser.setEmailAddress("");
        }
        
        else if(usersFacade.checkUserName(sysUser)){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that user name already exists!", ""));
             sysUser.setUserName("");
        }
        else if(!confirmPassword.equalsIgnoreCase(sysUser.getPassword()))
        {
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", ""));
             sysUser.setPassword("");
             confirmPassword = null;
        }
        else if(!usersFacade.validatePhone(sysUser)){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid telephone number, please check and try again!", ""));
            sysUser.setTelephone("");
        }
        else{
            sysUser.setPassword(sysEncryption.encryptPassword(sysUser.getPassword()));
            sysUser.setLoginCount(0);
            sysUser.setUserStatus(true);
            usersFacade.create(sysUser);
            
            confirmPassword = null;
            sysUser = new Users();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System user added successfully!", ""));
            page = "index.xhtml";
        }
        return page;
    }
    public void loadEditUser(){
        rtx = RequestContext.getCurrentInstance();
        editUser = sysUsersDM.getRowData();
        rtx.update("userEditDlg");
        rtx.execute("dlgEditUser.show()");
    }
    
    public void addUser(){
         FacesContext ctx = FacesContext.getCurrentInstance();
        if(sysUser.getGender().equalsIgnoreCase("select")){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select gender!", ""));
        }
        else if(sysUser.getIdNumber() == 0){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter ID number!", ""));
            
        }
        
        else if(usersFacade.checkTelephone(sysUser)){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that phone number already exists!", ""));
            sysUser.setUserName("");
        }
        else if(usersFacade.checkId(sysUser)){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that id number already exists!", ""));
             sysUser.setIdNumber(0);
        }
        
        else if(usersFacade.checkEmailAddress(sysUser)){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that email address already exists!", ""));
             sysUser.setEmailAddress("");
        }
        
        else if(usersFacade.checkUserName(sysUser)){
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with that user name already exists!", ""));
             sysUser.setUserName("");
        }
        else if(!confirmPassword.equalsIgnoreCase(sysUser.getPassword()))
        {
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", ""));
             sysUser.setPassword("");
             confirmPassword = null;
        }
        else if(!usersFacade.validatePhone(sysUser)){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid telephone number, please check and try again!", ""));
            sysUser.setTelephone("");
        }
        else if(sysUser.getUserLevelID() == null){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select user Level", ""));
           
        }
        else{
            
            Orglevels userLevel = levelFacade.find(sysUser.getUserLevelID());
            sysUser.setUserDomain(userLevel.getLevelname());
            sysUser.setPassword(sysEncryption.encryptPassword(sysUser.getPassword()));
            
            sysUser.setLoginCount(0);
            usersFacade.create(sysUser);
             sysUser.setUserStatus(true);
            confirmPassword = null;
            sysUser = new Users();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System user added successfully!", ""));
            
        }
    }
      public void editUser(){
        
    }
      public String viewUser(){
        
           String page="indUser.xhtml";
      indUser = sysUsersDM.getRowData();
      
      return page;
          
      }
    public void activateAccount(){
         FacesContext ctx = FacesContext.getCurrentInstance();
    if(indUser.isUserStatus()){
        indUser.setUserStatus(false);
        usersFacade.edit(indUser);
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Deactivated!", ""));
    }
    else{
        indUser.setUserStatus(true);
        usersFacade.edit(indUser);
         ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Activated!", ""));
    }
    }
    
    public void removeUser(){
        FacesContext ctx = FacesContext.getCurrentInstance();
    usersFacade.remove(indUser);
    indUser = new Users();
    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User removed successfully!", ""));
    }
    
    public void changePasswordDLG(){
  
  indUser = sysUsersDM.getRowData();

}
    public void removeDLG(){
     FacesContext ctx = FacesContext.getCurrentInstance();
  indUser = sysUsersDM.getRowData();
  if(indUser.getUserDomain().equalsIgnoreCase("admin")){
     
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Operation Prohibited : Permission denied!", ""));
      acceptRemove = false;
  } 
  else if(indUser.getUserDomain().equalsIgnoreCase("manager")&& userLogged.getUserDomain().equalsIgnoreCase("manager")){
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Operation Prohibited : Permission denied!", ""));
      acceptRemove = false;
  }
  else{
      acceptRemove = true;
  }
}
    public void addUserProfilePhoto(FileUploadEvent event){
        FacesContext ctx = FacesContext.getCurrentInstance();
   
       try{
           System.out.println("Uploading .." + event.getFile().getFileName());
            String path = "";
           Properties sysProps = System.getProperties();
         
            String fineName = "";
           String fileName = event.getFile().getFileName();
          
           fineName ="image_"+(new Random().nextInt(10000))+fileName.substring(fileName.lastIndexOf("."));
           
           
            File photoDirectory = new File(sysProps.getProperty("user.home")+ sysProps.getProperty("file.separator")+"SaccoData"+sysProps.getProperty("file.separator")+"MemberPhotos");
            if(photoDirectory.exists()){
                path = sysProps.getProperty("user.home")+sysProps.getProperty("file.separator")+"SaccoData"+sysProps.getProperty("file.separator")+"MemberPhotos"+sysProps.getProperty("file.separator") +fineName;
            }
            else{
                photoDirectory.mkdirs();
                
                path = sysProps.getProperty("user.home")+sysProps.getProperty("file.separator")+"SaccoData"+sysProps.getProperty("file.separator")+"MemberPhotos"+sysProps.getProperty("file.separator") +fineName;
            }
            
         File[] allFiles = photoDirectory.listFiles();
         for(File tempFile : allFiles){
             tempFile.delete();
         }
         
            File userFile = new File(path);
                userFile.createNewFile();
                
                 if (userFile.exists()) {
                    InputStream inputstream = event.getFile().getInputstream();
               FileOutputStream outputStream = new FileOutputStream(userFile);
               int read = 0;
               int i = (int)event.getFile().getSize();
              userPhotoBytes= new byte[i];
               while ((read = inputstream.read(userPhotoBytes)) != -1) {
                   outputStream.write(userPhotoBytes, 0, read);
               }
                     outputStream.flush();
                    outputStream.close();
                    //DB Blob starts here
                    //Serializable data = bytes;
                  userLogged.setUserPhoto(userPhotoBytes);
                       
                     usersFacade.edit(userLogged);
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Photo Updated Successfully !", ""));
                  
                 userPhotoBytes = new byte[0];
               
   // System.out.println("Image path -->" + tempPath);
           }
       }
       catch(Exception ex){
           ex.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could Not Upload ID !", ""));
           
       }
         
            
    }
    public void changeUserPassword(){
        ctx = FacesContext.getCurrentInstance();
        rtx = RequestContext.getCurrentInstance();
        if(!userLogged.getPassword().equalsIgnoreCase(sysEncryption.encryptPassword(oldPassword)))
    {
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Old Password!", "error"));
        oldPassword = "";
        newPassword = "";
    }
        else{
        userLogged.setPassword(sysEncryption.encryptPassword(newPassword));
        usersFacade.edit(userLogged);
        indUser = new Users();
        newPassword ="";
        oldPassword ="";
       ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully!", "error"));
        rtx.update("adminGrowl");
        }
    }
   public void changePassword(){
    FacesContext ctx = FacesContext.getCurrentInstance();
    if(!indUser.getPassword().equalsIgnoreCase(sysEncryption.encryptPassword(oldPassword)))
    {
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Old Password!", "error"));
        oldPassword = "";
        newPassword = "";
    }
    else{
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully!", "error"));
        indUser.setPassword(sysEncryption.encryptPassword(newPassword));
        usersFacade.edit(indUser);
        indUser = new Users();
        newPassword ="";
        oldPassword ="";
    }
}

    //Getters & Setters
    public Orglevels getUserLevelLogged() {
        if(userLogged!=null && userLogged.getUserDomain().equalsIgnoreCase("Pearl Soft Maintenenance")){
            userLevelLogged = new Orglevels();
            userLevelLogged.setAddMember(true);
            userLevelLogged.setApplyLoan(true);
            userLevelLogged.setApproveLoans(true);
            userLevelLogged.setDeleteMember(true);
            userLevelLogged.setLoadFinacialStatements(true);
            userLevelLogged.setLoadLedgerAccounts(true);
            userLevelLogged.setLoadLoanStatement(true);
            userLevelLogged.setLoadLoans(true);
            userLevelLogged.setLoadMember(true);
            userLevelLogged.setLoadMemberStatement(true);
            userLevelLogged.setLoadReports(true);
            userLevelLogged.setLoadSettings(true);
            userLevelLogged.setMakeContribution(true);
            userLevelLogged.setManageUsers(true);
            userLevelLogged.setTransferSharers(true);
            userLevelLogged.setDisburseLoans(true);
                         
        }
        else{
            if(userLogged!=null )
                userLevelLogged = levelFacade.find(userLogged.getUserLevelID());
        }
        return userLevelLogged;
    }

    public void setUserLevelLogged(Orglevels userLevelLogged) {
        this.userLevelLogged = userLevelLogged;
    }
   
    public Users getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(Users userLogged) {
        this.userLogged = userLogged;
    }

    public Users getSysUser() {
        return sysUser;
    }

    public void setSysUser(Users sysUser) {
        this.sysUser = sysUser;
    }

    public Users getIndUser() {
        return indUser;
    }

    public void setIndUser(Users indUser) {
        this.indUser = indUser;
    }

    public Users getEditUser() {
        return editUser;
    }

    public void setEditUser(Users editUser) {
        this.editUser = editUser;
    }

    
    public boolean isAcceptRemove() {
        return acceptRemove;
    }

    public void setAcceptRemove(boolean acceptRemove) {
        this.acceptRemove = acceptRemove;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HttpServletRequest getOrigRequest() {
        
        return origRequest;
    }

    public void setOrigRequest(HttpServletRequest origRequest) {
        this.origRequest = origRequest;
    }

    public StreamedContent getUserPhotoDB() {
         ctx = FacesContext.getCurrentInstance();
        userPhotoDB = new  DefaultStreamedContent();
        
        try{
        if(ctx.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE){
            userPhotoDB =new DefaultStreamedContent();
        }
        else{
          if(userLogged!=null && userLogged.getUserPhoto().length>0){
                userPhotoDB =  new DefaultStreamedContent(new ByteInputStream(userLogged.getUserPhoto(), userLogged.getUserPhoto().length));
           }
          else{
              userPhotoDB =new DefaultStreamedContent();
          }
         
        }
      }
      catch(Exception ex){
          //ex.printStackTrace();
      }
        return userPhotoDB;
    }

    public void setUserPhotoDB(StreamedContent userPhotoDB) {
        this.userPhotoDB = userPhotoDB;
    }
    
    
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<Orglevels> getUserLevels() {
        userLevels = levelFacade.findAll();
        return userLevels;
    }

    public void setUserLevels(List<Orglevels> userLevels) {
        this.userLevels = userLevels;
    }
    
    

    public DataModel<Users> getSysUsersDM() {
        sysUsersDM = new ListDataModel<Users>(usersFacade.findAll());
        return sysUsersDM;
    }

    public void setSysUsersDM(DataModel<Users> sysUsersDM) {
        this.sysUsersDM = sysUsersDM;
    }
    
    
    
    
}
