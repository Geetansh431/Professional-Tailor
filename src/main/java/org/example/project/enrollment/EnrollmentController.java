package org.example.project.enrollment;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.logging.Logger;

public class EnrollmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField EndrolledCity;

    @FXML
    private DatePicker EndrolledDOB;

    @FXML
    private ComboBox<String> EndrolledGender;

    @FXML
    private TextField EnrolledAddress;

    @FXML
    private TextField EnrolledName;

    @FXML
    private TextField EnrolledNumber;

    @FXML
    private Button btnSend;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextArea txtMsgArea;


    @FXML
    void DoClearAll(ActionEvent event) {
        try{
            stmt = con.prepareStatement("Delete From profile where Mobile=?");
            stmt.setString(1,EnrolledNumber.getText());
            int count = stmt.executeUpdate();
            if(count==1){
                ShowMyMsg("Record Cleared Successfully");
                System.out.println("Record Deleted");
            }else{
                ShowMyMsg("Invalid Number");
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void DoEditData(ActionEvent event) {
        //Mobile, cname, address, city, gender, dob date;
        try {
            stmt = con.prepareStatement("UPDATE profile SET cname = ?, address = ?, city = ?, gender = ?, dob = ? WHERE Mobile = ?");

            stmt.setString(1, EnrolledName.getText());
            stmt.setString(2, EnrolledAddress.getText());
            stmt.setString(3, EndrolledCity.getText());
            stmt.setString(4, EndrolledGender.getSelectionModel().getSelectedItem());

            LocalDate local = EndrolledDOB.getValue();
            java.sql.Date date = java.sql.Date.valueOf(local);
            stmt.setDate(5, date);

            // Assuming Mobile number is the unique key for updating the record
            stmt.setString(6, EnrolledNumber.getText());

            int count = stmt.executeUpdate();
            if (count == 1) {
                ShowMyMsg("Record Updated Successfully");
                System.out.println("Record Updated");
            } else {
                ShowMyMsg("Invalid Number");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    PreparedStatement stmt;
    @FXML
    void DoEnroll(ActionEvent event) {
        try{
            //Mobile , cname , address , city , gender , dob date ,
            stmt = con.prepareStatement("Insert Into Profile Values (?,?,?,?,?,?)");
            stmt.setString(1,EnrolledNumber.getText());
            stmt.setString(2,EnrolledName.getText());
            stmt.setString(3,EnrolledAddress.getText());
            stmt.setString(4,EndrolledCity.getText());
            stmt.setString(5,EndrolledGender.getSelectionModel().getSelectedItem());

            LocalDate local = EndrolledDOB.getValue();
            java.sql.Date date = java.sql.Date.valueOf(local);
            stmt.setDate(6,date);
            stmt.executeUpdate();
            System.out.println("Record Saved Successfully");

        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

//    @FXML
//    void DoFetchData(ActionEvent event) {
//        try{
//            stmt = con.prepareStatement("Select * from profile where Mobile=?");
//            stmt.setString(1,EnrolledNumber.getText());
//            ResultSet records = stmt.executeQuery();
//            while(records.next()){
//                String mobilee = records.getString("mobile");
//                String cnamee = records.getString("cname");
//                String addresss = records.getString("address");
//                String cityy = records.getString("city");
//                String genderr = records.getString("gender");
//                Date dt = records.getDate("dob");
//                System.out.println(mobilee + " " + cnamee + " " + addresss + " " + cityy + " " + genderr + " " + dt);
//            }
//        }catch(Exception exp){
//            exp.printStackTrace();
//        }
//    }

    @FXML
    void DoFetchData(ActionEvent event) {
        try {
            stmt = con.prepareStatement("Select * from profile where Mobile=?");
            stmt.setString(1, EnrolledNumber.getText());
            ResultSet records = stmt.executeQuery();
            if (records.next()) {
                String mobile = records.getString("mobile");
                String cname = records.getString("cname");
                String address = records.getString("address");
                String city = records.getString("city");
                String gender = records.getString("gender");
                Date dob = records.getDate("dob");

                // Set the retrieved data to the corresponding form fields
                EnrolledName.setText(cname);
                EnrolledAddress.setText(address);
                EndrolledCity.setText(city);
                EndrolledGender.getSelectionModel().select(gender);
                EndrolledDOB.setValue(dob.toLocalDate());

                System.out.println(mobile + " " + cname + " " + address + " " + city + " " + gender + " " + dob);
            } else {
                ShowMyMsg("No record found for the given mobile number");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
    Connection con;
    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if(con==null){
            System.out.println("Connection Did Not Establish");
        }else{
            System.out.println("Connection Done");
        }
    }

    void ShowMyMsg(String Msg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Its Header");
        alert.setContentText(Msg);

        alert.showAndWait();
    }


    @FXML
    void SendEmailOnAction(ActionEvent event) throws MessagingException {
        String recipientEmail = txtEmail.getText();
        sendEmail(recipientEmail);
    }

    private void sendEmail(String recipientEmail) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myAccountEmail = "Geetanshg2@gmail.com";
        String password = "twvw amjp xeze laco";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail,password);
            }
        });

        Message message = prepareMessage(session,myAccountEmail,recipientEmail,txtMsgArea.getText());
        if(message!=null){
            new  Alert(Alert.AlertType.INFORMATION,"Send Email Successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Try Again").show();
        }
    }

    private Message prepareMessage(Session session, String myAccountEmail, String recipientEmail, String msg) {
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
               new InternetAddress(recipientEmail)
            });

            message.setSubject("Messages");
            message.setText(msg);
            return message;
        }catch(Exception e){
//            Logger.getLogger(EmailsFormController.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }

        return null;
    }
}

