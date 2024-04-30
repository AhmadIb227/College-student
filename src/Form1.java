import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.sql.*;

public class Form1 extends JFrame {
    public JRadioButton maleRadioButton;
    public JRadioButton femaleRadioButton;
    public JButton deleteButton;
    public JTextField nameTxt;
    public JTextField ageTxt;
    public JTextField phoneTxt;
    public JTextField addressTxt;
    public JTextField ssnTxt;
    public JComboBox cityCompo;
    public JButton saveButton;
    public JButton addButton;
    public JButton loadImageButton;
    public JTable table1;
    JPanel mainPanel;
    private JLabel imagelabel;
    private JScrollPane scrollPan1;
    private JButton goButton;
    private JTextField searchBySSNTextField;
    private JEditorPane editorPane1;

    static String user="root";
    static String pass="12345";
    static String add="jdbc:mysql://localhost:3307/employee?autoReconnect=true&useSSL=false";
    static Connection c;
    static Statement statement;

    static String query;//
    ButtonGroup radioGroup;
    JScrollPane scrlooPan1;
    static String imagepath;
    public void emptyTxtFilds(){
        nameTxt.setText("");
        ageTxt.setText("");
        ssnTxt.setText("");
        phoneTxt.setText("");
        addressTxt.setText("");
    }
    public void showData(){
        try {

            c = DriverManager.getConnection(add, user, pass);
            statement = c.createStatement();//من شان تنفذ تعليمات
            /*
            * يتم استخدام هذا الكود لاسترداد
            * جميع البيانات (كافة الصفوف والأعمدة) من جدول يسمى "emp" في قاعدة البيانات ووضع
            *  هذه البيانات في جدول GUI على شكل جدول. يبدأ الكود بتعيين الاستعلام المراد استخدامه
            *  في المتغير "query"، ثم يتم تنفيذ الاستعلام باستخدام "statement.executeQuery" وتخزين
            * النتائج في متغير "rs". ثم يتم استخدام حلقة while للتحقق من وجود صفوف أخرى في الاستعلام
            *  واستخراج بيانات كل صف بشكل منفصل ووضع هذه البيانات في جدول GUI باستخدام "model.addRow".
            * */
            query = "SELECT* FROM emp";
            ResultSet rs = statement.executeQuery(query);//خزان لتعليمات سكول
            while (rs.next()) {
                String name = rs.getString("name");
                String age = String.valueOf(rs.getString("age"));
                String city = rs.getString("city");
                String phone = rs.getString("phone");
                String ssn = String.valueOf(rs.getInt("ssn"));
                String address = rs.getString("adderss");
                String gen=rs.getString("gender");
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.addRow(new Object[]{name, age, phone, address, ssn, city,gen});
            }
            c.close();
        }catch (SQLException ee){
            System.out.println(ee.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void run() {

        radioGroup=new ButtonGroup();
        radioGroup.add(maleRadioButton);
        radioGroup.add(femaleRadioButton);
        this.setContentPane(mainPanel);//هذا السطر من الكود يقوم بتعيين محتوى الإطار(JFrame) الرئيسي(content pane) إلى mainPanel. يتم استخدام هذا الأمر لتحديد العنصر الذي يجب عرضه خلال الإطار الرئيسي للتطبيق.
        this.setVisible(true);
        this.setSize(700,500);
        this.setResizable(false);//show interface
        this.setLocation(new Point(400,150));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] col={"Name","Age","Phone","Address","SSN","City","Gender"};
        Object[][] dat={};
        DefaultTableModel model=new DefaultTableModel(dat,col);
        this.table1.setModel(model);
        cityCompo.addItem("Hulanda");
        cityCompo.addItem("idlib");
        cityCompo.addItem("ifran");
        cityCompo.addItem("aleppo");
        cityCompo.addItem("batabo");
        showData();
    }
    public Form1() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    String ssn=ssnTxt.getText();
                    String city = cityCompo.getSelectedItem().toString();
                    int index = table1.getSelectedRow();
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setValueAt(nameTxt.getText(),index,0);
                    model.setValueAt(ageTxt.getText(),index,1);
                    model.setValueAt(phoneTxt.getText(),index,2);
                    model.setValueAt(addressTxt.getText(),index,3);
                    model.setValueAt(ssnTxt.getText(),index,4);
                    c = DriverManager.getConnection(add, user, pass);
                    Connection con = DriverManager.getConnection(add, user, pass);
                    statement = con.createStatement();
                    String name=nameTxt.getText();
                    String age=ageTxt.getText();
                    String address=addressTxt.getText();
                    String phone=phoneTxt.getText();
                    query = "UPDATE emp set name= '"+name+"',age="+age+",city = '"+city+"',phone='"+phone+"',adderss='"+address+"'" +
                            "\n WHERE ssn="+ssn+";";
                    statement.execute(query);
                }catch (ArrayIndexOutOfBoundsException err){
                    JOptionPane.showMessageDialog(saveButton,"no row selected","Error",1);
                }catch (SQLException sq){
                    System.out.println(sq.getMessage());
                }
                deleteButton.setEnabled(false);
                saveButton.setEnabled(false);
                emptyTxtFilds();
           }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model= (DefaultTableModel) table1.getModel();
                try {
//                    c = DriverManager.getConnection(add, user, pass);
                    if(table1.getSelectedRowCount()==1){
                        String ssn= (String) table1.getValueAt(table1.getSelectedRow(),4);
                        model.removeRow(table1.getSelectedRow());
                        query="DELETE FROM emp where ssn="+ssn;
                        statement.execute(query);
                        emptyTxtFilds();
                    }
                    else
                    {
                        for(int i=0;i<table1.getSelectedRows().length;i++)
                        {
                            String ssn=model.getValueAt(table1.getSelectedRows()[i],4).toString();
                            query="DELETE FROM emp where ssn="+ssn;
                            statement.execute(query);
                            model.removeRow(table1.getSelectedRows()[i]);
                        }

                        for(int i=0;i<table1.getSelectedRows().length;i++)
                        {
                            String ssn=model.getValueAt(table1.getSelectedRows()[i],4).toString();
                            query="DELETE FROM emp where ssn="+ssn;
                            statement.execute(query);
                            model.removeRow(table1.getSelectedRows()[i]);
                        }
                        for(int i=0;i<table1.getSelectedRows().length;i++)
                        {
                            String ssn=model.getValueAt(table1.getSelectedRows()[i],4).toString();
                            query="DELETE FROM emp where ssn="+ssn;
                            statement.execute(query);
                            model.removeRow(table1.getSelectedRows()[i]);
                        }
                    }
                    c.close();
                }catch (SQLException e2e){
                    JOptionPane.showMessageDialog(new JOptionPane(),"a");
                }

            }
        });
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    c = DriverManager.getConnection(add, user, pass);
                    char gen='M';
                    if(maleRadioButton.isSelected())
                        gen='M';
                    else if(femaleRadioButton.isSelected())
                        gen='F';


                    String age=(ageTxt.getText().isEmpty())?"null":ageTxt.getText();
                    String name=nameTxt.getText();
                    String city=cityCompo.getSelectedItem().toString();
                    String phone=phoneTxt.getText().toString();
                    int ssn= Integer.parseInt(ssnTxt.getText().toString());
                    String ad=addressTxt.getText();
                    statement=c.createStatement();
                    query="INSERT INTO emp " +
                            "VALUES ('"+name+"',"+age+",'"+city+"','"+phone+"',"+ssn+",'"+ad+"','"+imagepath+"','"+gen+"');";
                    statement.execute(query);
                    DefaultTableModel model= (DefaultTableModel) table1.getModel();
                    model.addRow(new Object[]{nameTxt.getText(),ageTxt.getText(),phoneTxt.getText(),
                            addressTxt.getText(),ssnTxt.getText(),city,gen});
                    emptyTxtFilds();
                    imagelabel.setIcon(null);
                    c.close();
                } catch (SQLIntegrityConstraintViolationException ex1){
                    JOptionPane.showMessageDialog(new JFrame(),"ssn already existes");
                    ssnTxt.setText("");
                } catch (SQLException ex){
                    System.out.println(ex.getMessage());
                } catch (NumberFormatException ex2){
                    JOptionPane.showMessageDialog(new JOptionPane(),"empty filed\nor\nuse letter in (ssn ,age)");
                    ssnTxt.setText("");
                    ageTxt.setText("");
                }
            }
        });
        table1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveButton.setEnabled(true);
                deleteButton.setEnabled(true);
                int x=table1.getSelectedRow();
                DefaultTableModel model= (DefaultTableModel) table1.getModel();

                nameTxt.setText(model.getValueAt(x,0).toString());
                ageTxt.setText(model.getValueAt(x,1).toString());
                phoneTxt.setText(model.getValueAt(x,2).toString());
                addressTxt.setText(model.getValueAt(x,3).toString());
                ssnTxt.setText(model.getValueAt(x,4).toString());
                String ssn1=ssnTxt.getText();
                cityCompo.setSelectedIndex(1);
                String gender=model.getValueAt(x,6).toString();
               query = "SELECT photoPath from emp WHERE ssn = "+ssn1+";";
                try
                {
                    c = DriverManager.getConnection(add, user, pass);
                    Connection con = DriverManager.getConnection(add, user, pass);
                    statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    String pa="";
                    while(rs.next())
                        pa=rs.getString("photoPath");
                    if (!pa.isEmpty())
                    {
                        ImageIcon imIcon=new ImageIcon(pa);
//                        Image image=imageIcon.getImage().getScaledInstance(imagelabel.getWidth(),imagelabel.getHeight(),Image.SCALE_SMOOTH);
                        imagelabel.setIcon(imIcon);
                 //     System.out.println(pa);
                    }
                }catch (SQLException exp){

                }

                if(gender.equals("F")){
                    radioGroup.setSelected(femaleRadioButton.getModel(), true);
                    femaleRadioButton.setSelected(true);
                    maleRadioButton.setSelected(false);
                }
                else if (gender.equals("M")){

                    maleRadioButton.setSelected(true);
                    femaleRadioButton.setSelected(false);
                }
            }

        });
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser=new JFileChooser();
                FileNameExtensionFilter fileNameExtensionFilter=new FileNameExtensionFilter("Images","jpg","png","jfif");
                fileChooser.addChoosableFileFilter(fileNameExtensionFilter);
                int show=fileChooser.showOpenDialog(null);
                if(show == JFileChooser.APPROVE_OPTION){
                    File selectedfile=fileChooser.getSelectedFile();
                    imagepath=selectedfile.getAbsolutePath();
                    ImageIcon imageIcon=new ImageIcon(imagepath);
                    Image image=imageIcon.getImage().getScaledInstance(imagelabel.getWidth(),imagelabel.getHeight(),Image.SCALE_SMOOTH);
                    imagelabel.setIcon(new ImageIcon(image));

                }
            }
        });
//        goButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                String ssn=searchBySSNTextField.getText();
//                int i;
//                for( i=0;i<table1.getRowCount();i++)
//                    if(table1.getValueAt(i,4).toString().equals(ssn))
//                        break;
//                if(i==table1.getRowCount())
//                {
//                    JOptionPane.showMessageDialog(null,"not found");
//                    return;
//                }
//
//                    try {
//                        c=DriverManager.getConnection(add,user,pass);
//                        statement=c.createStatement();
//                        query="select photoPath from emp where ssn= "+ssn+";";
//                        ResultSet rs=statement.executeQuery(query);
//                        String p="";
//                        while(rs.next())
//                            p=rs.getString("photoPath");
//                        ImageIcon imageIcon=new ImageIcon(p);
//                        Image image=imageIcon.getImage().getScaledInstance(imagelabel.getWidth(),imagelabel.getHeight(),Image.SCALE_SMOOTH);
//                    } catch (SQLException ex) {
//                        System.out.println("sldkfj");
//                    }
//                }
//        });

    }
    private void createUIComponents() {

        // TODO: place custom component creation code here
    }
}