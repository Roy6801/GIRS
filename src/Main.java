import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import myPackage.*;

class Main
{
    private static JFrame f;
    private static JPanel p0,p1,p2,p3;
    private static JTable t;
    private static JScrollPane js;
    private static JLabel j1,j2,j3;
    private static int page,flag;
    private static String name;
    private static DbConnect db;
    private static Encode en;
    
    public Main()
    {
        GUI();
    }
    
    public static void main(String[] args)
    {
        db = new DbConnect();
        en = new Encode();
        new Main();
    }
    
    private void GUI()
    {
        f = new JFrame();
        f.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
        
        f.setSize(1000,800);
        f.setResizable(false);
        loginPanel();
        f.add(p0);
        f.setVisible(true);
    }
    
    private void loginPanel()
    {
        p0 = new JPanel(null);
        
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        login.setBounds(500,700,50,40);
        register.setBounds(300,700,50,40);
        
        JTextField userName = new JTextField();
        JTextField userPwd = new JTextField();
        userName.setBounds(700,100,200,40);
        userPwd.setBounds(700,400,200,40);
        
        JLabel uName = new JLabel();
        JLabel uPwd = new JLabel();
        uName.setText("Username");
        uPwd.setText("Password");
        uName.setBounds(50,100,200,40);
        uPwd.setBounds(50,400,200,40);
        
        p0.add(login);
        p0.add(register);
        p0.add(userName);
        p0.add(userPwd);
        p0.add(uName);
        p0.add(uPwd);

        register.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                                               
                if(userName.getText()!=null && !userName.getText().isEmpty())
                {
                    if(userPwd.getText()!=null && !userPwd.getText().isEmpty())
                    {
                        db.register(en.encrypt(userName.getText()),en.encrypt(userPwd.getText()));
                    }
                }
            }
        });
        
        login.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                
                if(userName.getText()!=null && !userName.getText().isEmpty())
                {
                    if(userPwd.getText()!=null && !userPwd.getText().isEmpty())
                    {
                        if(db.login(en.encrypt(userName.getText()),en.encrypt(userPwd.getText())))
                        {
                            listPanel(1);
                            gotoPanel(p0,p1);
                        }
                    }
                }
            }
        });
    }
    
    private void listPanel(int page)
    {
        p1 = new JPanel(null);
        Main.page = page;
        
        Font font = new Font("Serif", Font.BOLD, 20);
        
        setLabel();
        
        JTextField tf = new JTextField();
        JTextField searchField = new JTextField();
        tf.setBounds(769,670,100,40);
        tf.setText(""+page);
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setFont(font);
        searchField.setBounds(240,670,400,40);
        searchField.setHorizontalAlignment(SwingConstants.CENTER);
        searchField.setFont(font);
        searchField.setText("");
        
        JButton previous = new JButton("←");
        JButton next = new JButton("→");
        JButton go = new JButton("Go");
        JButton search = new JButton("Search");
        previous.setBounds(10,670,100,40);
        previous.setFont(font);
        next.setBounds(120,670,100,40);
        next.setFont(font);
        go.setBounds(879,670,100,40);
        go.setFont(font);
        search.setBounds(649,670,100,40);
        search.setFont(font);
        
        String[][] a = db.getData(page);
        String[] column = {"Sr. No.","Game Name","Game ID"};
        setTable(a,column);
        js.setViewportView(t);
        
        p1.setBackground(Color.decode("#0c0026"));
        p1.add(j1);
        p1.add(j2);
        p1.add(j3);
        p1.add(tf);
        p1.add(searchField);
        p1.add(previous);
        p1.add(next);
        p1.add(go);
        p1.add(search);
        p1.add(js);
        
        t.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                String s = t.getValueAt(t.getSelectedRow(), 2).toString();
                if(s!=null)
                {
                    flag=1;
                    infoPanel(s);
                    gotoPanel(p1,p2);
                }
            }
        });
        
        previous.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                previous(p1,page);
            }
        });
        
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                next(p1,page);
            }
        });
        
        go.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                gotoPage(p1,Integer.parseInt(tf.getText()));
            }
        });
        
        search.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                search(searchField.getText());
                resultPanel();
                gotoPanel(p1,p3);
            }
        });
    }
    
    private void infoPanel(String s)
    {
        p2 = new JPanel();
        Button b = new Button(s);
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                
                switch(flag)
                {
                case 1:
                {
                    listPanel(page);
                    gotoPanel(p2,p1);
                    break;
                }
                case 3:
                {
                    search(name);
                    resultPanel();
                    gotoPanel(p2,p3);
                    break;
                }
                default:System.out.print("Fatal Error!!");
                }
            }
        });
        p2.add(b);
    }
    
    private void resultPanel()
    {
        p3 = new JPanel(null);
        
        setLabel();
        
        Button back = new Button("Back");
        back.setBounds(760,720,100,40);
        back.setFont(new Font("Serif",Font.BOLD,20));
        
        j3.setText("Search Results!");
        js.setViewportView(t);
        
        p3.setBackground(Color.decode("#0c0026"));
        p3.add(js);
        p3.add(j1);
        p3.add(j2);
        p3.add(j3);
        p3.add(back);
        
        t.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                try
                {
                    String s = t.getValueAt(t.getSelectedRow(), 2).toString();
                    flag = 3;
                    infoPanel(s);
                    gotoPanel(p3,p2);
                }
                catch(NullPointerException e)
                {
                    System.out.print(e);
                }
            }
        });
        
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                listPanel(page);
                gotoPanel(p3,p1);
            }
        });
    }
    
    private void gotoPanel(JPanel p1,JPanel p2)
    {
        f.remove(p1);
        f.add(p2);
        f.setVisible(true);
    }
    
    private void setLabel()
    {
        Image timg = (new ImageIcon(getClass().getResource("/myPackage/images.jpg"))).getImage();
        ImageIcon img = new ImageIcon(timg.getScaledInstance(400, 200, Image.SCALE_SMOOTH));
        j1 = new JLabel(img);
        j1.setBounds(10,10,390,200);
        
        j2 = new JLabel();
        j2.setText("G.I.R.S.");
        j2.setFont(new Font("Showcard Gothic",Font.ITALIC,150));
        j2.setHorizontalAlignment(SwingConstants.CENTER);
        j2.setForeground(Color.decode("#6026a1"));
        j2.setBounds(400,10,577,200);
        
        j3 = new JLabel();
        j3.setForeground(Color.decode("#a174d3"));
        j3.setHorizontalAlignment(SwingConstants.CENTER);
        j3.setFont(new Font("Showcard Gothic",Font.BOLD,28));
        j3.setBounds(240,720,509,40);
    }
    
    private void setTable(String[][] a,String[] c)
    {
        js = new JScrollPane();
        js.setBounds(10,215,968,450);

        t = new JTable(a,c){
            @Override
            public Component prepareRenderer(TableCellRenderer tcr,int row,int col)
            {
                Component comp = super.prepareRenderer(tcr, row, col);
                if(!comp.getBackground().equals(getSelectionBackground()))
                {
                    Color bg = ((row%2==0)?Color.decode("#3b1f5b"):Color.decode("#0c0026"));
                    comp.setBackground(bg);
                }
                return comp;
            }
        };
        t.setRowHeight(50);
        t.setFont(new Font("Algerian", Font.BOLD, 30));
        t.setForeground(Color.orange);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        t.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        t.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        t.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        t.getColumnModel().getColumn(0).setPreferredWidth(150);
        t.getColumnModel().getColumn(1).setPreferredWidth(700);
        t.getColumnModel().getColumn(2).setPreferredWidth(150);
        t.setGridColor(Color.decode("#0c0026"));
        
        JTableHeader th = t.getTableHeader();
        th.setFont(new Font("Showcard Gothic", Font.BOLD, 34));
        th.setBackground(Color.decode("#0c0026"));
        th.setForeground(Color.decode("#a174d3"));
        th.setReorderingAllowed(false);
    }
    
    private void gotoPage(JPanel p, int page)
    {
        if(page>=1 && page<=db.countPages())
        {
            listPanel(page);
            gotoPanel(p,p1);
        }
        else
        {
            j3.setText("Total Pages : "+db.countPages());
        }
    }
    
    private void previous(JPanel p,int page)
    {
        if(page>1)
        {
            page = page-1;
            listPanel(page);
            gotoPanel(p,p1);
        }
        else
        {
            j3.setText("Reached First Page!!!");
        }
    }
    
    private void next(JPanel p,int page)
    {
        if(page<db.countPages())
        {
            page = page+1;
            listPanel(page);
            gotoPanel(p,p1);
        }
        else
        {
            j3.setText("Reached Last Page!!!");
        }
    }
    
    private void search(String name)
    {
        Main.name = name;
        
        String[][] a = db.searchGame(name);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        setTable(a,c);
    }
}