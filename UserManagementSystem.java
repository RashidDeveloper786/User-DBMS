
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

// import javax.swing.plaf.nimbus.State;

import java.sql.Statement;

public class UserManagementSystem {


    private static final String url = "jdbc:mysql://localhost:3306/UserDetail";
    private static final String uname = "root";
    private static final String pass = "Rashid@2004";
    private static final String admin = "admin@123";
    private static final String admpass = "admin420";
    // private static int ID = 1000;

    public static void newUserDetail(Statement st)throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the following details for new Registration....");

        System.out.print("Enter your Full Name : ");
        String name = sc.nextLine();
        System.out.print("Enter your Course : ");
        String course = sc.nextLine();
        System.out.print("Enter your mobile number : ");
        long Phone = Long.parseLong(sc.nextLine());
        System.out.print("Enter your E-mail ID : ");
        String email = sc.nextLine();
        System.out.println();
        System.out.print("Create a username : ");
        String username = sc.nextLine();

        String qeury1 = "Select username from user";
        ResultSet rs = st.executeQuery(qeury1);

        while(rs.next()) {
            String uname = rs.getString("username");
            while (true) {
                if (username.equals(uname)) {
                    System.out.println("Username already exists try another....");
                    System.out.print("Create a username : ");
                    username = sc.nextLine();
                }
                else{
                    break;
                }
            }
        }

        System.out.print("Set you password : ");
        String pass = sc.nextLine();
        sc.close();

        String query2 = "SELECT userid FROM user ORDER BY userid DESC LIMIT 1";
        ResultSet rs2 = st.executeQuery(query2);
        int ID=0;
        if (rs2.next()) {
            ID = rs2.getInt("userid");
        }
        ID++;
        String query3 = String.format("INSERT INTO user(userid , name , course , Phone , email , username , pass) VALUES(%d , '%s' , '%s' , %d , '%s' , '%s' , '%s')", ID,name,course,Phone,email,username,pass);
        int rowsAffected = st.executeUpdate(query3);
        System.out.println(rowsAffected+ " rows affected...");
        System.out.println("You are successfully registered....");
    }


    public static void userLoginDetail(Statement st) throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username : ");
        String username = sc.nextLine();

        String query1 = "SELECT username FROM user";
        ResultSet rs = st.executeQuery(query1);

        boolean userFound = false;
        boolean passFound = false;

        while (rs.next()) {
            if (username.equals(rs.getString("username"))) {
                userFound = true;
                break;
            }
        }

        if (userFound) {
            System.out.print("Enter your password : ");
            String pass = sc.nextLine();

            String query2 = "SELECT pass FROM user";
            ResultSet rs2 = st.executeQuery(query2);

            while (rs2.next()) {
                if (pass.equals(rs2.getString("pass"))) {
                    passFound = true;
                    break;
                }
            }
            if (passFound) {
                System.out.println("Logged in Successfully.....");
                System.out.println();
                System.out.println("Are you want to see your details.... (y/n): ");
                String response = sc.nextLine();
                System.out.println();
                if (response.equals("y")) {
                    userDetail(st, username);
                }
                else{
                    return;
                }
            }
            else{
                System.out.println("Incorrect Password....");
            }
            
        }

        else{
            System.out.println("Invalid username!");
            
        }
        sc.close();

        // System.out.println("");

    }

    public static void userDetail(Statement st, String username) throws SQLException{
        
        String query = String.format("SELECT * FROM user WHERE username = '%s'", username);
        
        ResultSet rs = st.executeQuery(query);
        
        if(rs.next()) {
            int id = rs.getInt("userid");
            String name = rs.getString("name");
            String course = rs.getString("course");
            long phone = rs.getLong("Phone");
            String email = rs.getString("email");

            System.out.println();
            System.out.println("Your Details are following...");
            System.out.println();
            System.out.println("User ID : "+id+", Name : "+name+", Course : "+course+", Phone : "+phone+", Email : "+email);
            System.out.println();
        
        }
    }

    public static void adminLogin(Statement st) throws SQLException{
        Scanner sc = new Scanner(System.in);
        
        
        int i =1;
        
        while (i<=3) {
            System.out.print("Enter admin username : ");
            String admuser = sc.nextLine();
            if (admuser.equals(admin)) {
                int j = 1;
                while (j<=3) {
                    System.out.print("Enter password : ");
                    String adminpass = sc.nextLine();
                    if (adminpass.equals(admpass)) {
                        System.out.println("Admin logged in Successfully....");
                        System.out.println();
                        adminAction(st);
                        break;
                    }
                    else{
                        if ((3-j)==0) {
                            System.out.println("Sorry too many attemps please try after sometimes....");
                            break;
                        }
                        else{
                            System.out.println("Incorrect password...! only "+(3-j)+" try left.....");
                            j++;
                        }
                    }
                }
                break;
            }
            else{
                if ((3-i)==0) {
                    System.out.println("Sorry too many attemps please try after sometimes....");
                    break;
                }
                else{
                    System.out.println("Invalid username...! only "+(3-i)+" try left.....");
                    i++;
                }
            }
        }
        
        
        
        sc.close();
    }

    private static void adminAction(Statement st) throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.println("You can perform the following operations....");
        System.out.println();
        System.out.println("1. See all the data of the users...");
        System.out.println("2. Delete the record of any user...");
        System.out.println("Press 0 for Log out...");
        System.out.println();

        System.out.print("Please Enter your choice : ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                showAllDetails(st);
                
                break;
            
            case 2:
                deleteUsersDetail(st);

            case 0:
                System.out.println();
                System.out.println("Logged out Successfully....");
                break;
            default:
                System.out.println("Wrong Input....");
                break;
        }
        sc.close();

        
    }

    private static void showAllDetails(Statement st) throws SQLException{
        String query = "SELECT * FROM user";
        ResultSet rs = st.executeQuery(query);
        System.out.println();
        System.out.println("Your Details are following...");
        System.out.println();
        while (rs.next()) {
            int id = rs.getInt("userid");
            String name = rs.getString("name");
            String course = rs.getString("course");
            long phone = rs.getLong("Phone");
            String email = rs.getString("email");

            
            System.out.println("User ID : "+id+", Name : "+name+", Course : "+course+", Phone : "+phone+", Email : "+email);
            System.out.println();
            
        }
    }

    private static void deleteUsersDetail(Statement st) throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter userid : ");
        int uid = Integer.parseInt(sc.nextLine());

        String query = "SELECT userid FROM user";
        ResultSet rs = st.executeQuery(query);
        boolean userFound =false;
        while (rs.next()) {
            if (uid==rs.getInt("userid")) {
                userFound = true;
                
            }
        }
        if (userFound) {
                String query2 = String.format("DELETE FROM user WHERE userid = %d", uid);
                st.executeUpdate(query2);
                System.out.println("DELETED SUCCESSFULLY......");
                System.out.println();
                System.out.println("Do you want to see the updated record....(y/n)");
                String ch = sc.nextLine();
                if (ch.equals("y")) {
                    showAllDetails(st);
                    System.out.println();
                    System.out.println("Logged out Successfully....");
                }
        }
        else{
            System.out.println("Invalid userid..!");
        }
        sc.close();
        
    }
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, uname, pass);
            Statement st =  con.createStatement();
            
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("===============> USER DETAILS <===============");
            System.out.println();
            System.out.println();

            System.out.println("1. For new Registration......");
            System.out.println("2. For User Login if already exist.....");
            System.out.println("3. For Admin Login");
            System.out.println("0. For exit....");
            System.out.println();

            System.out.print("Please Enter your choice : ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    newUserDetail(st);
                    break;
                case 2:
                    userLoginDetail(st);
                    break;

                case 3:
                    adminLogin(st);
                    break;
                case 0:
                    System.out.println("Session terminated....");
                    break;
                default:
                    System.out.println("Sorry! Wrong Choice.....");
                    break;
            }
            

            con.close();
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
}
