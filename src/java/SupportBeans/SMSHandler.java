/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SupportBeans;

import Entities.Contribution;
import Entities.SaccoDetails;
import Entities.SaccoMember;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author george.gitere
 */
public class SMSHandler {

    public String sendAfricasTalkingSMS(String recipient, String message, SaccoDetails details) {
        // Specify your login credentials
        String status = "";
        String username = "ackgetrude";
        String apiKey = "ed4513598ff2b01e02ce77a9d140c8657206ccd48c0b1b7fc6a654b55bfb070d";
        JSONObject result;
        String recipients = "+254" + recipient.substring(0);
        AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey);
        try {
            JSONArray results = gateway.sendMessage(recipients, message, details.getSenderID(), 1);
            for (int i = 0; i < results.length(); ++i) {
                result = results.getJSONObject(i);
                //System.out.print("Message Number -->"+ result.getString("status") + ",");
                status = result.getString("status");
            }
        } catch (Exception e) {
            //System.out.println("Encountered an error while sending " + e.getMessage());
            e.printStackTrace();
        }
        if (status != null && status.equalsIgnoreCase("ok")) {
            status = "ok";
        } else {
            status = "fail";
        }
        return status;
    }

    public void sendMemberContributionSMS(SaccoMember member, SaccoDetails sDetails, Contribution contribution) {

        String apiKey = "MyAPIKey";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String recipients = "+254711XXXYYY,+254733YYYZZZ";
        String message = "";
        if (contribution.getShares() > 0 && contribution.getDeposit() == 0) {
            if(member.getTitle()!=null){
                 message = "Hi " +member.getTitle()+" "+ member.getFullName() + "?, we have received your thanksgiving of " + contribution.getShares() + " on " + dateFormat.format(contribution.getContributionDate()) + " Thank You & God bless.";

            }
            else{
                 message = "Hi " + member.getFullName() + "?, we have received your thanksgiving of " + contribution.getShares() + " on " + dateFormat.format(contribution.getContributionDate()) + " Thank You & God bless.";

            }
           
        } else if (contribution.getDeposit() > 0 && contribution.getShares() == 0) {
              if(member.getTitle()!=null){
                    message = "Hi " +member.getTitle()+" "+ member.getFullName() + "?, we have received your tithe of " + contribution.getDeposit() + " on " + dateFormat.format(contribution.getContributionDate()) + " Thank You & God bless.";

              }
              else{
                    message = "Hi " + member.getFullName() + "?, we have received your tithe of " + contribution.getDeposit() + " on " + dateFormat.format(contribution.getContributionDate()) + " Thank You & God bless.";

              }
          
        } else if (contribution.getDeposit() > 0 && contribution.getShares() > 0) {
              if(member.getTitle()!=null){
                     message = "Hi " +member.getTitle()+" "+ member.getFullName() + "?, we have received your thanksgiving of " + contribution.getShares() + " and tithe of " + contribution.getDeposit() + " on " + dateFormat.format(contribution.getContributionDate()) + " Thank You & God bless.";

              }
              else{
                     message = "Hi " +member.getFullName() + "?, we have received your thanksgiving of " + contribution.getShares() + " and tithe of " + contribution.getDeposit() + " on " + dateFormat.format(contribution.getContributionDate()) + " Thank You & God bless.";

              }
         
        }

        // Specify your AfricasTalking shortCode or sender id
        String from = sDetails.getSenderID();

        AfricasTalkingGateway gateway = new AfricasTalkingGateway(sDetails.getSmsGateUsername(), sDetails.getSmsGatePass());

        try {
            JSONArray memberResult = gateway.sendMessage(member.getPhoneNumber(), message, from, 1);
            if (member.getSpouseTel() != null && member.isSpouseAlert()) {

                JSONArray spouseSendResult = gateway.sendMessage(member.getSpouseTel(), message, from, 1);
            }

//            for( int i = 0; i < results.length(); ++i ) {
//                  JSONObject result = results.getJSONObject(i);
//                  System.out.print(result.getString("status") + ",");
//                  System.out.print(result.getString("number") + ",");
//                  System.out.print(result.getString("messageId") + ",");
//                  System.out.println(result.getString("cost"));
//        }
        } catch (Exception e) {
            System.out.println("Encountered an error while sending " + e.getMessage());
        }

    }

}
