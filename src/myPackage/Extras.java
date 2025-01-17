package myPackage;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;

public class Extras extends DbConnect
{
    private static JTable t;
    
    public Font dFont(String font,float n)
    {
        Font th = null;
        try
        {
            switch(font)
                {
                case "Tarrget":
                {
                    th = Font.createFont(Font.TRUETYPE_FONT,getClass().getResource("/myPackage/Font/Tarrget.otf").openStream());
                    break;
                }
                case "VerminVibes":
                {
                    th = Font.createFont(Font.TRUETYPE_FONT,getClass().getResource("/myPackage/Font/VerminVibes.ttf").openStream());
                    break;
                }
                case "Halo3":
                {
                    th = Font.createFont(Font.TRUETYPE_FONT,getClass().getResource("/myPackage/Font/Halo3.ttf").openStream());
                    break;
                }
            }
        }
        catch(FontFormatException | IOException e)
        {
            System.out.print(e);
        }
        if(th!=null)
        {
            th = th.deriveFont(n);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(th);
        }
        
        return th;
    }
    
    public ImageIcon formatImage(URL url,int w,int h)
    {
        ImageIcon imageIcon = null;
        try
        {
            Image image = ImageIO.read(url);
            imageIcon = new ImageIcon(image.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } 
        catch(IOException e)
        {
            System.out.print(e);
        }
        return imageIcon;
    }
    
    public JTable setTable(String[][] r,String[] c)
    {
        t = new JTable(r,c)
        {
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
        return t;
    }
    
    public JLabel[] setLabel()
    {
        JLabel j[] = new JLabel[3];
        
        j[0] = new JLabel(formatImage(getClass().getResource("/myPackage/Img/image.jpg"),400,200));
        j[0].setBounds(10,10,390,200);
        
        j[1] = new JLabel();
        j[1].setText("G.I.R.S.");
        j[1].setFont(new Font("Showcard Gothic",Font.BOLD,150));
        j[1].setHorizontalAlignment(SwingConstants.CENTER);
        j[1].setForeground(Color.decode("#6026a1"));
        j[1].setBounds(400,10,577,200);
        
        j[2] = new JLabel();
        j[2].setForeground(Color.decode("#a174d3"));
        j[2].setHorizontalAlignment(SwingConstants.CENTER);
        j[2].setFont(new Font("Showcard Gothic",Font.BOLD,28));
        j[2].setBounds(240,720,509,40);
        
        return j;
    }
    
    public JTable search(String name)
    {        
        String[][] r = searchGame(name);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        t = setTable(r,c);
        
        return t;
    }
    
    public JTable filter(int genre,int mode, int per,int age)
    {
        String[][] r = filterGame(genre,mode,per,age);
        String[] c = {"Sr. No.","Game Name","Game ID"};
        t = setTable(r,c);
        
        return t;
    }
    
    public JTable setGenre()
    {
        String[][] r = getGenre();
        String[] c = {"Sr. No.","Genre","Genre ID"};
        t = setTable(r,c);
        
        return t;
    }
    
    public String uRating(float r)
    {
        while(r>5)
        {
            r = r-5;
        }
        
        String s = String.format("%.2f",r);
        return s;
    }
    
    public String set(ArrayList<String> a)
    {
        String s = "";
        Iterator i = a.iterator();
        
        while(i.hasNext())
        {
            s = s+i.next()+"; ";
        }
        return s;
    }
}