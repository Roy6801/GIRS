import java.awt.*;
import java.util.prefs.Preferences;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import myPackage.*;

class Main
{
    private static JFrame f;
    private static JPanel p0,p1,p2,p3,p4;
    private static JLabel j[] = new JLabel[3];
    private static final JScrollPane js = new JScrollPane();
    private static JTable t = new JTable();
    private static int page,infoFlag,resultFlag;
    private static int genre,mode,per,age;
    private static Preferences prefs;
    private static String name;
    private static Encode en;
    private static Extras ex;
    
    private Main()
    {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        GUI();
    }
    
    public static void main(String[] args)
    {
        en = new Encode();
        ex = new Extras();
        new Main();
    }
    
    private void GUI()
    {
        f = new JFrame();
                
        boolean check = false;
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setSize(1000,800);
        f.setResizable(false);
                       
        String uid = prefs.get("Username", "");
        String pwd = prefs.get("Password", "");
        
        if(ex.login(uid,pwd))
        {
            check = true;
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
        JLabel head = new JLabel(ex.formatImage(getClass().getResource("/myPackage/Img/head.jpg"),507,150));
        JLabel foot = new JLabel(ex.formatImage(getClass().getResource("/myPackage/Img/foot.jpg"),507,150));
        JLabel poster = new JLabel(ex.formatImage(getClass().getResource("/myPackage/Img/poster.jpg"),460,745));
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

        register.addActionListener(ae -> 
        {

                if(userName.getText()!=null && !userName.getText().isEmpty())
                {
                    if(userPwd.getText()!=null && !userPwd.getText().isEmpty())
                    {
                        if(ex.register(en.encrypt(userName.getText()),en.encrypt(userPwd.getText())))
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
        });

        login.addActionListener(ae -> 
        {

                if(userName.getText()!=null && !userName.getText().isEmpty())
                {
                    if(userPwd.getText()!=null && !userPwd.getText().isEmpty())
                    {
                        if(ex.login(en.encrypt(userName.getText()),en.encrypt(userPwd.getText())))
                        {
                            prefs.put("Username",en.encrypt(userName.getText()));
                            prefs.put("Password",en.encrypt(userPwd.getText()));
                            listPanel(1);
                            gotoPanel(p0,p1);
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
        });
    }
    
    private void listPanel(int page)
    {
        p1 = new JPanel(null);
        Main.page = page;
        
        infoFlag = 1;
        resultFlag = 1;
        
        Font font = new Font("Serif", Font.BOLD, 20);
        
        j = ex.setLabel();
        
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
        
        String[][] r = ex.getData(page);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        t = ex.setTable(r,c);
        js.setBounds(10,215,968,450);
        js.setViewportView(t);
        
        p1.setBackground(Color.decode("#0c0026"));
        p1.add(j[0]);
        p1.add(j[1]);
        p1.add(j[2]);
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
        
        previous.addActionListener(ae -> 
        {
            previous(p1,page);
        });
        
        next.addActionListener(ae -> 
        {
            next(p1,page);
        });
        
        go.addActionListener(ae -> 
        {
            gotoPage(p1,Integer.parseInt(tf.getText()));
        });
        
        search.addActionListener(ae -> 
        {              
            name = searchField.getText();
            t = ex.search(name);
            resultPanel();
            gotoPanel(p1,p3);
        });
        
        logout.addActionListener(ae -> 
        {
            prefs.put("Username","");
            prefs.put("Password","");
            loginPanel();
            gotoPanel(p1,p0);
        });
        
        filter.addActionListener(ae -> 
        {
            filterPanel();
            gotoPanel(p1,p4);
        });
    }
    
    private void infoPanel(int id)
    {
        p2 = new JPanel(null);
        
        JButton back = new JButton("Back");
        back.setBounds(870,720,100,40);
        back.setFont(new Font("Serif",Font.BOLD,20));
        
        j = ex.setLabel();
        
        URL url = null;
        
        try 
        {
            url = new URL("https:"+ex.gameInfo(id).get(2));
        }
        catch(MalformedURLException e)
        {
            System.out.print(e);
        }
        
        JLabel icon = new JLabel(ex.formatImage(url,200,200));
        JLabel gName = new JLabel();
        JLabel gCollection = new JLabel();
        JLabel labelCollection = new JLabel();
        icon.setBounds(777,220,200,200);
        gName.setBounds(10,220,757,120);
        gName.setHorizontalAlignment(SwingConstants.CENTER);
        gName.setForeground(Color.orange);
        gName.setBackground(Color.decode("#2e1447"));
        gName.setText(ex.gameInfo(id).get(0));
        gName.setFont(ex.dFont("Tarrget",40));
        gName.setOpaque(true);
        labelCollection.setBounds(10,350,150,80);
        labelCollection.setForeground(Color.black);
        labelCollection.setBackground(Color.decode("#a56ade"));
        labelCollection.setText("Collection");
        labelCollection.setFont(ex.dFont("Vermin",18));
        labelCollection.setHorizontalAlignment(SwingConstants.CENTER);
        labelCollection.setOpaque(true);
        gCollection.setBounds(170,350,597,80);
        gCollection.setBackground(Color.decode("#a56ade"));
        gCollection.setOpaque(true);
        
        p2.setBackground(Color.decode("#0c0026"));
        p2.add(j[0]);
        p2.add(j[1]);
        p2.add(j[2]);
        p2.add(icon);
        p2.add(gName);
        p2.add(labelCollection);
        p2.add(gCollection);
        p2.add(back);
        
        back.addActionListener(ae -> 
        {
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
                    t = ex.search(name);
                    resultPanel();
                    gotoPanel(p2,p3);
                    break;
                }
                else
                {
                    t = ex.filter(genre,mode,per,age);
                    resultPanel();
                    gotoPanel(p2,p3);
                    break;
                }
            }
            default:System.out.print("Fatal Error!!");
            }
        });
    }
    
    private void resultPanel()
    {
        p3 = new JPanel(null);
        
        infoFlag = 3;
        
        j = ex.setLabel();
        
        JButton back = new JButton("Back");
        back.setBounds(760,720,100,40);
        back.setFont(new Font("Serif",Font.BOLD,20));
        
        j[2].setText("Search Results!");
        js.setBounds(10,215,968,450);
        js.setViewportView(t);
        
        p3.setBackground(Color.decode("#0c0026"));
        p3.add(js);
        p3.add(j[0]);
        p3.add(j[1]);
        p3.add(j[2]);
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
        
        back.addActionListener(ae -> 
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
        });
    }
    
    private void filterPanel()
    {
        p4 = new JPanel(null);
        
        resultFlag = 4;
        
        Font font = new Font("Serif",Font.BOLD,20);
        
        j = ex.setLabel();
        t = ex.setGenre();
        
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
        
        j[2].setText("Filter");
        js.setBounds(25,215,500,450);
        js.setViewportView(t);
        
        p4.setBackground(Color.decode("#0c0026"));
        p4.add(js);
        p4.add(j[0]);
        p4.add(j[1]);
        p4.add(j[2]);
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
        
        clear.addActionListener(ae -> 
        {
            t.getSelectionModel().clearSelection();
            jcbMode.setSelectedIndex(0);
            jcbPer.setSelectedIndex(0);
            jcbAge.setSelectedIndex(0);
        });
        
        filter.addActionListener(ae -> 
        {
            mode = jcbMode.getSelectedIndex();
            per = jcbPer.getSelectedIndex();
            age = jcbAge.getSelectedIndex();
            t = ex.filter(genre,mode,per,age);
            resultPanel();
            gotoPanel(p4,p3);
        });
        
        back.addActionListener(ae -> 
        {
            listPanel(page);
            gotoPanel(p4,p1);
        });
    }
    
    private void gotoPanel(JPanel p1,JPanel p2)
    {
        f.remove(p1);
        f.add(p2);
        f.setVisible(true);
    }

    private void gotoPage(JPanel p, int page)
    {
        if(page>=1 && page<=ex.countPages())
        {
            listPanel(page);
            gotoPanel(p,p1);
        }
        else
        {
            j[2].setText("Total Pages : "+ex.countPages());
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
            j[2].setText("Reached First Page!!!");
        }
    }
    
    private void next(JPanel p,int page)
    {
        if(page<ex.countPages())
        {
            page = page+1;
            listPanel(page);
            gotoPanel(p,p1);
        }
        else
        {
            j[2].setText("Reached Last Page!!!");
        }
    }
}