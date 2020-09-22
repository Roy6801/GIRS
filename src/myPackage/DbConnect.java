package myPackage;

import java.sql.*;

public class DbConnect
{
    protected Connection con;
    protected PreparedStatement st;
    protected ResultSet rs;
    private boolean flag;
    
    public DbConnect()
    {
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3308/game_db","root","");
        }
        catch(SQLException e)
        {
            System.out.print("Port 3308 No Response!");
        }
    }
    
    public String[][] getData(int page)
    {
        int l = (page*50)-49;
        int u = (page*50);
        String[][] a = new String[50][3];
        
        try
        {
            st = con.prepareStatement("select * from game_list where Sr_No >= (?) and Sr_No <= (?)",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1,l);
            st.setInt(2,u);            
            rs = st.executeQuery();
            
            int i=0;
            while(rs.next())
            {
                a[i][0] = rs.getString("Sr_No");
                a[i][1] = rs.getString("Game_Name");
                a[i][2] = rs.getString("Game_Id");
                i++;
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return a;
    }
    
    public int countPages()
    {
        int i,p=1;
        
        try
        {
            st = con.prepareStatement("select count(*) from game_list",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery();
            rs.next();
            i = rs.getInt(1);

            while(i>50)
            {
                p++;
                i=i-50;
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return p;
    }
    
    public String[][] searchGame(String name)
    {
        String[][] s = null;
        
        try
        {
            st = con.prepareStatement("Select * from game_list where Game_Name like (?) or Collection_Name like (?)",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setString(1, '%'+name+'%');
            st.setString(2, '%'+name+'%');
            rs = st.executeQuery();
            
            int i=0;
            while(rs.next())
            {
                i++;
            }
            i = (i>8?i:8);
            s = new String[i][3];
            rs.beforeFirst();
            i=0;
            while(rs.next())
            {
                s[i][0] = rs.getString("Sr_No");
                s[i][1] = rs.getString("Game_Name");
                s[i][2] = rs.getString("Game_Id");
                i++;
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        
        return s;
    }
    
     public boolean login(String uid,String pwd)
    {
        flag = false;
                
        try
        {
            st = con.prepareStatement("Select * from users",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery();
            
            while(rs.next())
            {
                if(rs.getString("uid").equals(uid))
                {
                    flag = rs.getString("pwd").equals(pwd);
                    break;
                }
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return flag;
    }
    
    public boolean register(String uid,String pwd)
    {
        flag = false;
        
            try
            {
                st = con.prepareStatement("Insert into game_db.users(uid,pwd) values (?,?)",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setString(1,uid);
                st.setString(2,pwd);
                st.executeUpdate();
                flag = true;
            }
            catch(SQLException e)
            {
                System.out.print(e);
                flag = false;
            }
        
        return flag;
    }
}
