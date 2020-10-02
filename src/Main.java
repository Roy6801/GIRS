import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import myPackage.*;

class Main
{
    private static JFrame f;
    private static JPanel p0,p1,p2,p3,p4;
    private static JTable t;
    private static JScrollPane js;
    private static JLabel j1,j2,j3;
    private static int page,infoFlag,resultFlag;
    private static int genre,mode,per,age;
    private static String name;
    private static DbConnect db;
    private static Encode en;
    
    private Main()
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
                
        boolean check = false;
        f.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
        
        f.setSize(1000,800);
        f.setResizable(false);
                       
        try(FileReader fr = new FileReader("cc.txt"))
        {
            String uid = "",pwd = "";
            boolean l = true;
            int i;
            while((i=fr.read())!=-1)
            {
                if((char)i=='\n')
                {
                    l = false;
                }
                if(l)
                {
                    uid = uid + (char)i;
                }
                else
                {
                    pwd = pwd + (char)i;
                }
                pwd = pwd.replace("\n","");
                if(db.login(uid,pwd))
                {
                    check = true;
                }
            }
        }
        catch(IOException e)
        {
            System.out.print(e);
        }

        if(check)
        {
            listPanel(1);
            f.add(p1);
        }
        else
        {
            loginPanel();
            f.add(p0);
        }
        f.setVisible(true);
    }
    
    private void loginPanel()
    {
        p0 = new JPanel(null);
                
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        login.setBounds(700,535,200,40);
        login.setFont(new Font("Showcard Gothic",Font.BOLD,18));
        register.setBounds(500,535,200,40);
        register.setFont(new Font("Showcard Gothic",Font.BOLD,18));
        
        JTextField userName = new JTextField();
        JPasswordField userPwd = new JPasswordField();
        userName.setBounds(700,190,200,40);
        userName.setFont(new Font("Serif",Font.BOLD,28));
        userName.setHorizontalAlignment(SwingConstants.CENTER);
        userPwd.setBounds(700,362,200,40);
        userPwd.setFont(new Font("Serif",Font.BOLD,28));
        userPwd.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel uName = new JLabel();
        JLabel uPwd = new JLabel();
        JLabel head = new JLabel(formatImage(getClass().getResource("/myPackage/head.jpg"),507,150));
        JLabel foot = new JLabel(formatImage(getClass().getResource("/myPackage/foot.jpg"),507,150));
        JLabel poster = new JLabel(formatImage(getClass().getResource("/myPackage/poster.jpg"),460,745));
        JLabel display = new JLabel();
        uName.setText("Username : ");
        uName.setBounds(500,190,200,40);
        uName.setForeground(Color.orange);
        uName.setFont(new Font("Showcard Gothic",Font.BOLD,26));
        uPwd.setText("Password : ");
        uPwd.setBounds(500,362,200,40);
        uPwd.setForeground(Color.orange);
        uPwd.setFont(new Font("Showcard Gothic",Font.BOLD,26));
        head.setBounds(470,10,507,150);
        foot.setBounds(470,605,507,150);
        poster.setBounds(10,10,460,745);
        display.setBounds(500,450,400,40);
        display.setFont(new Font("Showcard Gothic",Font.BOLD,24));
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setForeground(Color.red);

        p0.setBackground(Color.decode("#09234f"));
        p0.add(login);
        p0.add(register);
        p0.add(userName);
        p0.add(userPwd);
        p0.add(uName);
        p0.add(uPwd);
        p0.add(head);
        p0.add(foot);
        p0.add(poster);
        p0.add(display);

        register.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){

                if(userName.getText()!=null && !userName.getText().isEmpty())
                {
                    if(userPwd.getText()!=null && !userPwd.getText().isEmpty())
                    {
                        if(db.register(en.encrypt(userName.getText()),en.encrypt(userPwd.getText())))
                        {
                            display.setText("Registration Succeeded!!!");
                        }
                        else
                        {
                            display.setText("Username Already Exists!");
                        }
                    }
                    else
                    {
                        display.setText("Enter Password!!!");
                    }
                }
                else
                {
                    display.setText("Enter Username!!!");
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
                        try(FileWriter fw = new FileWriter("cc.txt"))
                        {
                            fw.write(en.encrypt(userName.getText()));
                            fw.write("\n"+en.encrypt(userPwd.getText()));
                        }
                        catch(IOException e)
                        {
                            System.out.print(e);
                        }
                        }
                        else
                        {
                            display.setText("Invalid Username/Password!");
                        }
                    }
                    else
                    {
                        display.setText("Enter Password!!!");
                    }
                }
                else
                {
                    display.setText("Enter Username!!!");
                }
            }
        });
    }
    
    private void listPanel(int page)
    {
        p1 = new JPanel(null);
        Main.page = page;
        
        infoFlag = 1;
        resultFlag = 1;
        
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
        JButton logout = new JButton("Logout");
        JButton filter = new JButton("Filter");
        previous.setBounds(10,670,100,40);
        previous.setFont(font);
        next.setBounds(120,670,100,40);
        next.setFont(font);
        go.setBounds(879,670,100,40);
        go.setFont(font);
        search.setBounds(649,670,100,40);
        search.setFont(font);
        logout.setBounds(10,720,100,40);
        logout.setFont(font);
        filter.setBounds(879,720,100,40);
        filter.setFont(font);
        
        String[][] r = db.getData(page);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        setTable(r,c,10,215,968,450);
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
        p1.add(logout);
        p1.add(filter);
        p1.add(js);
        
        t.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int id = Integer.parseInt(t.getValueAt(t.getSelectedRow(), 2).toString());
                    infoPanel(id);
                    gotoPanel(p1,p2);
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
        
        logout.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                File file = new File("cc.txt");
                file.delete();
                loginPanel();
                gotoPanel(p1,p0);
            }
        });
        
        filter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                filterPanel();
                gotoPanel(p1,p4);
            }
        });
    }
    
    private void infoPanel(int id)
    {
        p2 = new JPanel(null);
        
        JButton back = new JButton("Back");
        back.setBounds(870,720,100,40);
        back.setFont(new Font("Serif",Font.BOLD,20));
        
        setLabel();
        
        URL url = null;
        
        try 
        {
            url = new URL("https:"+db.gameInfo(id).get(2));
        }
        catch(MalformedURLException e)
        {
            System.out.print(e);
        }
        
        JLabel icon = new JLabel(formatImage(url,200,200));
        JLabel gName = new JLabel();
        JLabel gCollection = new JLabel();
        JLabel labelCollection = new JLabel();
        icon.setBounds(777,220,200,200);
        gName.setBounds(10,220,757,120);
        gName.setHorizontalAlignment(SwingConstants.CENTER);
        gName.setForeground(Color.orange);
        gName.setBackground(Color.decode("#2e1447"));
        gName.setText(db.gameInfo(id).get(0));
        gName.setFont(dFont("Tarrget",40));
        gName.setOpaque(true);
        labelCollection.setBounds(10,350,150,80);
        labelCollection.setForeground(Color.black);
        labelCollection.setBackground(Color.decode("#a56ade"));
        labelCollection.setText("Collection");
        labelCollection.setFont(dFont("Vermin",18));
        labelCollection.setHorizontalAlignment(SwingConstants.CENTER);
        labelCollection.setOpaque(true);
        gCollection.setBounds(170,350,597,80);
        gCollection.setBackground(Color.decode("#a56ade"));
        gCollection.setOpaque(true);
        
        p2.setBackground(Color.decode("#0c0026"));
        p2.add(j1);
        p2.add(j2);
        p2.add(j3);
        p2.add(icon);
        p2.add(gName);
        p2.add(labelCollection);
        p2.add(gCollection);
        p2.add(back);
        
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                
                switch(infoFlag)
                {
                case 1:
                {
                    listPanel(page);
                    gotoPanel(p2,p1);
                    break;
                }
                case 3:
                {
                    if(resultFlag == 1)
                    {
                        search(name);
                        resultPanel();
                        gotoPanel(p2,p3);
                        break;
                    }
                    else
                    {
                        filter(genre,mode,per,age);
                        resultPanel();
                        gotoPanel(p2,p3);
                        break;
                    }
                }
                default:System.out.print("Fatal Error!!");
                }
            }
        });
    }
    
    private void resultPanel()
    {
        p3 = new JPanel(null);
        
        infoFlag = 3;
        
        setLabel();
        
        JButton back = new JButton("Back");
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
                    int id = Integer.parseInt(t.getValueAt(t.getSelectedRow(), 2).toString());
                    infoPanel(id);
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
                switch(resultFlag)
                {
                case 1:
                {
                    listPanel(page);
                    gotoPanel(p3,p1);
                    break;
                }
                case 4:
                {
                    filterPanel();
                    gotoPanel(p3,p4);
                    break;
                }
                default:System.out.print("Fatal Error!!");
                }
            }
        });
    }
    
    private void filterPanel()
    {
        p4 = new JPanel(null);
        
        resultFlag = 4;
        
        Font font = new Font("Serif",Font.BOLD,20);
        
        setLabel();
        setGenre();
        
        genre = 0;
        mode = 0;
        per = 0;
        age = 0;
        
        String[] modeList = {"Select Mode","Single Player","Multi-Player","Co-Op","Split Screen",
        "MMO","Battle Royale"};
        String[] perspectiveList = {"Select Perspective","First Person","Third Person","Bird View/Isometric",
        "Side View","Text","Auditory","Virtual Reality"};
        String[] ageList = {"Select Age Tag","Everybody","Teen","Mature","Adult"};
        
        JComboBox<String> jcbMode = new JComboBox<>(modeList);
        JComboBox<String> jcbPer = new JComboBox<>(perspectiveList);
        JComboBox<String> jcbAge = new JComboBox<>(ageList);
        jcbMode.setBounds(550,215,400,100);
        jcbMode.setFont(new Font("Showcard Gothic",Font.BOLD,32));
        jcbPer.setBounds(550,375,400,100);
        jcbPer.setFont(new Font("Showcard Gothic",Font.BOLD,32));
        jcbAge.setBounds(550,535,400,100);
        jcbAge.setFont(new Font("Showcard Gothic",Font.BOLD,32));
        
        JButton clear = new JButton("Clear Selection");
        JButton back = new JButton("Back");
        JButton filter = new JButton("Filter");
        clear.setBounds(760,670,219,40);
        clear.setFont(font);
        back.setBounds(760,720,100,40);
        back.setFont(font);
        filter.setBounds(879,720,100,40);
        filter.setFont(font);
        
        j3.setText("Filter");
        js.setViewportView(t);
        
        p4.setBackground(Color.decode("#0c0026"));
        p4.add(js);
        p4.add(j1);
        p4.add(j2);
        p4.add(j3);
        p4.add(jcbMode);
        p4.add(jcbPer);
        p4.add(jcbAge);
        p4.add(clear);
        p4.add(filter);
        p4.add(back);
        
        t.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                genre = Integer.parseInt(t.getValueAt(t.getSelectedRow(), 2).toString());
            }
        });
        
        clear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                t.getSelectionModel().clearSelection();
                jcbMode.setSelectedIndex(0);
                jcbPer.setSelectedIndex(0);
                jcbAge.setSelectedIndex(0);
            }
        });
        
        filter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                mode = jcbMode.getSelectedIndex();
                per = jcbPer.getSelectedIndex();
                age = jcbAge.getSelectedIndex();
                filter(genre,mode,per,age);
                resultPanel();
                gotoPanel(p4,p3);
            }
        });
        
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                listPanel(page);
                gotoPanel(p4,p1);
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
        
        j1 = new JLabel(formatImage(getClass().getResource("/myPackage/image.jpg"),400,200));
        j1.setBounds(10,10,390,200);
        
        j2 = new JLabel();
        j2.setText("G.I.R.S.");
        j2.setFont(new Font("Showcard Gothic",Font.BOLD,150));
        j2.setHorizontalAlignment(SwingConstants.CENTER);
        j2.setForeground(Color.decode("#6026a1"));
        j2.setBounds(400,10,577,200);
        
        j3 = new JLabel();
        j3.setForeground(Color.decode("#a174d3"));
        j3.setHorizontalAlignment(SwingConstants.CENTER);
        j3.setFont(new Font("Showcard Gothic",Font.BOLD,28));
        j3.setBounds(240,720,509,40);
    }
    
    private void setTable(String[][] r,String[] c,int x,int y,int w,int h)
    {
        js = new JScrollPane();
        js.setBounds(x,y,w,h);

        t = new JTable(r,c){
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
        
        String[][] r = db.searchGame(name);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        setTable(r,c,10,215,968,450);
    }
    
    private void filter(int genre,int mode, int per,int age)
    {
        String[][] r = db.filterGame(genre,mode,per,age);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        setTable(r,c,10,215,968,450);
    }
    
    private void setGenre()
    {
        String[][] r = db.getGenre();
        String[] c = {"Sr. No.","Genre","Genre ID"};
        setTable(r,c,25,215,500,450);
    }
    
    private ImageIcon formatImage(URL url,int w,int h)
    {
        ImageIcon img2 = null;
        try
        {
            Image img1 = ImageIO.read(url);
            img2 = new ImageIcon(img1.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } 
        catch(MalformedURLException e)
        {
            System.out.print(e);
        } 
        catch(IOException e)
        {
            System.out.print(e);
        }
        return img2;
    }
    
    private Font dFont(String font,float n)
    {
        Font th = null;
        try
        {
            switch(font)
                {
                case "Tarrget":
                {
                    th = Font.createFont(Font.TRUETYPE_FONT,new File("TarrgetHalfToneItalic-ozyV.otf"));
                    break;
                }
                case "Vermin":
                {
                    th = Font.createFont(Font.TRUETYPE_FONT,new File("VerminVibesV-Zlg3.ttf"));
                }
            }
        }
        catch (FontFormatException | IOException e)
        {
            System.out.print(e);
        }
        
        return th.deriveFont(n);
    }
}