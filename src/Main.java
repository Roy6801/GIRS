import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;
import java.net.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import myPackage.*;

class Main
{
    private static JFrame f;
    private static JPanel p0,p1,p2,p2d,p3,p4;
    private static JLabel j[] = new JLabel[3];
    private JButton r5,r4,r3,r2,r1;
    private static final JScrollPane js = new JScrollPane();
    private static JTable t = new JTable();
    private static ImageIcon ustar,star;
    private static int page,infoFlag,resultFlag;
    private static int genre,mode,per,age;
    private static int rating;
    private static String reviewC;
    private static Preferences prefs;
    private static String name,uid,pwd;
    private static URL url;
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
                
        rating = 0;
        boolean check = false;
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setSize(1000,800);
        f.setResizable(false);
                       
        uid = prefs.get("Username", "");
        pwd = prefs.get("Password", "");

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
                if(!userName.getText().isBlank())
                {
                    if(!userPwd.getText().isBlank())
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
                if(!userName.getText().isBlank())
                {
                    if(!userPwd.getText().isBlank())
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
        
        JTextField reviewField = new JTextField();
        reviewField.setBounds(10,710,610,45);
        reviewField.setFont(new Font("Serif",Font.BOLD,16));
        reviewField.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton back = new JButton("Back");
        JButton clear = new JButton("Clear");
        JButton more = new JButton("More");
        JButton review = new JButton("Review");
        
        r5 = new JButton(ustar);
        r4 = new JButton(ustar);
        r3 = new JButton(ustar);
        r2 = new JButton(ustar);
        r1 = new JButton(ustar);
        
        back.setBounds(882,720,95,35);
        back.setFont(new Font("Serif",Font.BOLD,20));
        clear.setBounds(777,720,95,35);
        clear.setFont(new Font("Serif",Font.BOLD,20));
        more.setBounds(630,670,137,30);
        more.setFont(new Font("Serif",Font.BOLD,20));
        review.setBounds(630,710,137,45);
        review.setFont(new Font("Serif",Font.BOLD,20));
        
        url = null;
        
        try 
        {
            url = new URL("https:"+ex.gameInfo(id).get(2));
        }
        catch(MalformedURLException e)
        {
            System.out.print(e);
        }
                      
        ustar = ex.formatImage(getClass().getResource("myPackage/Img/ustar.png"), 100, 100);
        star = ex.formatImage(getClass().getResource("myPackage/Img/star.png"), 100, 100);
               
        JPanel rate = new JPanel(new GridLayout(5,0));
        JLabel icon = new JLabel(ex.formatImage(url,200,200));
        JLabel gName = new JLabel("<html>"+ex.gameInfo(id).get(0)+"</html>");
        JLabel labelCollection = new JLabel("Collection");
        JLabel gCollection = new JLabel("<html>"+ex.gameInfo(id).get(1)+"</html>");
        JLabel labelRelease = new JLabel("Release Date");
        JLabel gRelease = new JLabel(""+en.timestamp(ex.gameInfo(id).get(3)));
        String pop = ex.uRating(Float.parseFloat(ex.gameInfo(id).get(6)));
        JLabel popularity = new JLabel(pop);
        JLabel gGenre = new JLabel("<html>Genre: "+ex.set(ex.gameGenre(id))+"</html");
        JLabel gMode = new JLabel("<html>Mode: "+ex.set(ex.gameMode(id))+"</html");
        JLabel gPerspective = new JLabel("<html>Perspective: "+ex.set(ex.gamePerspective(id))+"</html");
        JLabel gAge = new JLabel("<html><center>For<br><br><br>"+ex.set(ex.gameAge(id)).replaceAll(";","")+"</html");
        JLabel gPlatform = new JLabel("<html>Platform: "+ex.set(ex.gamePlatform(id))+"</html");
        JLabel gCompany = new JLabel("<html>Company: "+ex.set(ex.gameCompany(id))+"</html");
        
        Border border = BorderFactory.createLineBorder(Color.decode("#0c0026"));
        Border b = new EmptyBorder(10,10,10,10);
        r5.setBackground(Color.decode("#0c0026"));
        r5.setBorder(border);
        r4.setBackground(Color.decode("#0c0026"));
        r4.setBorder(border);
        r3.setBackground(Color.decode("#0c0026"));
        r3.setBorder(border);
        r2.setBackground(Color.decode("#0c0026"));
        r2.setBorder(border);
        r1.setBackground(Color.decode("#0c0026"));
        r1.setBorder(border);
        
        rate.setBounds(777,220,200,490);
        rate.add(r5);
        rate.add(r4);
        rate.add(r3);
        rate.add(r2);
        rate.add(r1);
        
        icon.setBounds(777,10,200,200);
        
        gName.setBounds(10,10,757,200);
        gName.setHorizontalAlignment(SwingConstants.CENTER);
        gName.setForeground(Color.decode("#ffd900"));
        gName.setBackground(Color.decode("#2e1447"));
        gName.setFont(ex.dFont("Halo3",60));
        gName.setOpaque(true);
        
        labelCollection.setBounds(10,220,150,60);
        labelCollection.setForeground(Color.decode("#ff1f00"));
        labelCollection.setBackground(Color.decode("#24154d"));
        labelCollection.setFont(ex.dFont("VerminVibes",18));
        labelCollection.setHorizontalAlignment(SwingConstants.CENTER);
        labelCollection.setOpaque(true);
        
        gCollection.setBounds(170,220,597,60);
        gCollection.setForeground(Color.decode("#ff1f00"));
        gCollection.setBackground(Color.decode("#24154d"));
        gCollection.setFont(ex.dFont("VerminVibes",30));
        gCollection.setHorizontalAlignment(SwingConstants.CENTER);
        gCollection.setOpaque(true);
        
        labelRelease.setBounds(10,290,200,60);
        labelRelease.setForeground(Color.decode("#ff3700"));
        labelRelease.setBackground(Color.decode("#412a80"));
        labelRelease.setHorizontalAlignment(SwingConstants.CENTER);
        labelRelease.setFont(ex.dFont("VerminVibes", 20)); 
        labelRelease.setOpaque(true);
        
        gRelease.setBounds(220,290,400,60);
        gRelease.setForeground(Color.decode("#ff3700"));
        gRelease.setBackground(Color.decode("#412a80"));
        gRelease.setHorizontalAlignment(SwingConstants.CENTER);
        gRelease.setFont(ex.dFont("VerminVibes", 20));
        gRelease.setOpaque(true);
        
        gGenre.setBounds(10,360,610,60);
        gGenre.setForeground(Color.decode("#ff7700"));
        gGenre.setBackground(Color.decode("#744fb0"));
        gGenre.setFont(ex.dFont("VerminVibes", 15));
        gGenre.setBorder(b);
        gGenre.setOpaque(true);
        
        gMode.setBounds(10,430,610,60);
        gMode.setForeground(Color.decode("#ff8c00"));
        gMode.setBackground(Color.decode("#7e4899"));
        gMode.setFont(ex.dFont("VerminVibes", 15));
        gMode.setBorder(b);
        gMode.setOpaque(true);
        
        gPerspective.setBounds(10,500,610,60);
        gPerspective.setForeground(Color.decode("#ffa600"));
        gPerspective.setBackground(Color.decode("#683582"));
        gPerspective.setFont(ex.dFont("VerminVibes", 15));
        gPerspective.setBorder(b);
        gPerspective.setOpaque(true);
        
        gPlatform.setBounds(10,570,610,60);
        gPlatform.setForeground(Color.decode("#ffbf00"));
        gPlatform.setBackground(Color.decode("#4e2066"));
        gPlatform.setFont(ex.dFont("VerminVibes", 15));
        gPlatform.setBorder(b);
        gPlatform.setOpaque(true);
        
        gCompany.setBounds(10,640,610,60);
        gCompany.setForeground(Color.decode("#ffd500"));
        gCompany.setBackground(Color.decode("#4d158c"));
        gCompany.setFont(ex.dFont("VerminVibes", 15));
        gCompany.setBorder(b);
        gCompany.setOpaque(true);
        
        popularity.setBounds(630,290,137,180);
        popularity.setForeground(Color.decode("#fca61c"));
        popularity.setBackground(Color.decode("#4d158c"));
        popularity.setHorizontalAlignment(SwingConstants.CENTER);
        popularity.setFont(ex.dFont("VerminVibes", 40));
        popularity.setOpaque(true);
        
        gAge.setBounds(630,480,137,180);
        gAge.setForeground(Color.decode("#fca61c"));
        gAge.setBackground(Color.decode("#4d158c"));
        gAge.setHorizontalAlignment(SwingConstants.CENTER);
        gAge.setFont(ex.dFont("VerminVibes", 18));
        gAge.setOpaque(true);
        
        p2.setBackground(Color.decode("#0c0026"));
        p2.add(rate);
        p2.add(icon);
        p2.add(gName);
        p2.add(labelCollection);
        p2.add(gCollection);
        p2.add(labelRelease);
        p2.add(gRelease);
        p2.add(popularity);
        p2.add(gGenre);
        p2.add(gMode);
        p2.add(gPerspective);
        p2.add(gPlatform);
        p2.add(gCompany);
        p2.add(gAge);
        p2.add(reviewField);
        p2.add(review);
        p2.add(more);
        p2.add(clear);
        p2.add(back);
        
        rating = ex.getRating(id, uid);
        reviewC = ex.getReview(id, uid);
        
        if(reviewC!=null)
        {
            reviewField.setText(reviewC);
        }
                
        switch(rating)
        {
            case 1:r1();break;
            case 2:r2();break;
            case 3:r3();break;
            case 4:r4();break;
            case 5:r5();break;
            default:r0();
        }
               
        r5.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                r5.setBackground(Color.decode("#66ff00"));                
                r4.setBackground(Color.decode("#c8db32"));
                r3.setBackground(Color.decode("#f2de46"));
                r2.setBackground(Color.decode("#ff9021"));
                r1.setBackground(Color.decode("#bf0000"));
            }
            
            @Override
            public void mouseExited(MouseEvent me)
            {
                r5.setBackground(Color.decode("#0c0026"));                
                r4.setBackground(Color.decode("#0c0026"));
                r3.setBackground(Color.decode("#0c0026"));
                r2.setBackground(Color.decode("#0c0026"));
                r1.setBackground(Color.decode("#0c0026"));
            }
        });
        
        r4.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                r4.setBackground(Color.decode("#c8db32"));
                r3.setBackground(Color.decode("#f2de46"));
                r2.setBackground(Color.decode("#ff9021"));
                r1.setBackground(Color.decode("#bf0000"));
            }
            
            @Override
            public void mouseExited(MouseEvent me)
            {
                r4.setBackground(Color.decode("#0c0026"));
                r3.setBackground(Color.decode("#0c0026"));
                r2.setBackground(Color.decode("#0c0026"));
                r1.setBackground(Color.decode("#0c0026"));
            }
        });
        
        r3.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                r3.setBackground(Color.decode("#f2de46"));
                r2.setBackground(Color.decode("#ff9021"));
                r1.setBackground(Color.decode("#bf0000"));
            }
            
            @Override
            public void mouseExited(MouseEvent me)
            {
                r3.setBackground(Color.decode("#0c0026"));
                r2.setBackground(Color.decode("#0c0026"));
                r1.setBackground(Color.decode("#0c0026"));
            }
        });
        
        r2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                r2.setBackground(Color.decode("#ff9021"));
                r1.setBackground(Color.decode("#bf0000"));
            }
            
            @Override
            public void mouseExited(MouseEvent me)
            {
                r2.setBackground(Color.decode("#0c0026"));
                r1.setBackground(Color.decode("#0c0026"));
            }
        });
        
        r1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                r1.setBackground(Color.decode("#bf0000"));
            }
            
            @Override
            public void mouseExited(MouseEvent me)
            {
                r1.setBackground(Color.decode("#0c0026"));
            }
        });
        
        r5.addActionListener(ae ->
        {
            r5();
            ex.rating(id,uid,5);
        });
        
        r4.addActionListener(ae ->
        {
            r4();
            ex.rating(id,uid,4);
        });
        
        r3.addActionListener(ae ->
        {
            r3();
            ex.rating(id,uid,3);
        });
        
        r2.addActionListener(ae ->
        {
            r2();
            ex.rating(id,uid,2);
        });
        
        r1.addActionListener(ae ->
        {
            r1();
            ex.rating(id,uid,1);
        });
        
        review.addActionListener(ae ->
        {
            if(!reviewField.getText().isBlank())
            {
                ex.review(id, uid, reviewField.getText());
            }
        });
        
        more.addActionListener(ae ->
        {
            detailPanel(id);
            gotoPanel(p2, p2d);
        });
        
        clear.addActionListener(ae ->
        {
            r0();
            ex.rating(id, uid, 0);
            reviewField.setText(null);
        });
                
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
    
    private void detailPanel(int id)
    {
        p2d = new JPanel(null);
              
        JLabel icon = new JLabel(ex.formatImage(url,200,200));
        JLabel summary = new JLabel("<html><center><u><b>Summary</b></u><br><br>"+ex.gameInfo(id).get(4)+"</html>");
        JLabel story = new JLabel("<html><center><u><b>Storyline</b></u><br><br>"+ex.gameInfo(id).get(5)+"</html>");
        
        JButton back = new JButton("Back");
        back.setBounds(882,720,95,35);
        
        icon.setBounds(777,10,200,200);
        
        if(ex.gameInfo(id).get(4)==null)
        {
            summary.setText("<html><center>Summary:Unavailable</html>");
        }
        if(ex.gameInfo(id).get(5)==null)
        {
            story.setText("<html><center>Storyline:Unavailable</html>");
        }
        
        summary.setBounds(10,10,757,200);
        summary.setFont(ex.dFont("VerminVibes", 12));
        summary.setForeground(Color.decode("#f7c019"));
        summary.setBackground(Color.decode("#3b1361"));
        summary.setOpaque(true);
        story.setBounds(10,220,967,490);
        story.setFont(ex.dFont("VerminVibes", 18));
        story.setForeground(Color.decode("#f7c019"));
        story.setBackground(Color.decode("#3b1361"));
        story.setOpaque(true);
        
        p2d.setBackground(Color.decode("#0c0026"));
        p2d.add(icon);
        p2d.add(summary);
        p2d.add(story);
        p2d.add(back);
        
        back.addActionListener(ae ->
        {
            infoPanel(id);
            gotoPanel(p2d, p2);
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
    
    private void r5()
    {
        r5.setIcon(star);
        r4.setIcon(star);
        r3.setIcon(star);
        r2.setIcon(star);
        r1.setIcon(star);
    }
    
    private void r4()
    {
        r5.setIcon(ustar);
        r4.setIcon(star);
        r3.setIcon(star);
        r2.setIcon(star);
        r1.setIcon(star);
    }
    
    private void r3()
    {
        r5.setIcon(ustar);
        r4.setIcon(ustar);
        r3.setIcon(star);
        r2.setIcon(star);
        r1.setIcon(star);
    }
    
    private void r2()
    {
        r5.setIcon(ustar);
        r4.setIcon(ustar);
        r3.setIcon(ustar);
        r2.setIcon(star);
        r1.setIcon(star);
    }
    
    private void r1()
    {
        r5.setIcon(ustar);
        r4.setIcon(ustar);
        r3.setIcon(ustar);
        r2.setIcon(ustar);
        r1.setIcon(star);
    }
    
    private void r0()
    {
        r5.setIcon(ustar);
        r4.setIcon(ustar);
        r3.setIcon(ustar);
        r2.setIcon(ustar);
        r1.setIcon(ustar);
    }
}