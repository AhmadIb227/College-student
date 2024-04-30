import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            Form1 frm=new Form1();
        frm.setResizable(false);
        frm.setTitle("Progect");
        frm.run();

            Connection c= DriverManager.getConnection("jdbc:mysql://localhost:3307/employee?autoReconnect=true&useSSL=false","root","12345");

        }catch (Exception e){
            System.out.println();
        }
    }
}