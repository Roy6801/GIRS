package myPackage;

import java.sql.*;
import java.util.ArrayList;


class DbConnect
{
    private Encode en = new Encode();
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
            
            p=i/50;
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
    
    public String[][] filterGame(int genre,int mode,int per,int age)
    {
        String s[][] = null;
        try
        {
            if(genre!=0 && mode!=0 && per!=0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?) AND Game_Mode_Id = (?)"
                        + " AND Player_Perspective_Id = (?) AND Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
                st.setInt(2, mode);
                st.setInt(3, per);
                st.setInt(4, age);
            }
            else if(genre!=0 && mode!=0 && per!=0 && age == 0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?) AND Game_Mode_Id = (?)"
                        + " AND Player_Perspective_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
                st.setInt(2, mode);
                st.setInt(3, per);
            }
            else if(genre!=0 && mode!=0 && per==0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?) AND Game_Mode_Id = (?)"
                        + " AND Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
                st.setInt(2, mode);
                st.setInt(3, age);
            }
            else if(genre!=0 && mode==0 && per!=0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?) AND Player_Perspective_Id = (?)"
                        + " AND Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
                st.setInt(2, per);
                st.setInt(3, age);
            }
            else if(genre==0 && mode!=0 && per!=0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Mode_Id = (?) AND Player_Perspective_Id = (?)"
                        + " AND Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, mode);
                st.setInt(2, per);
                st.setInt(3, age);
            }
            else if(genre!=0 && mode!=0 && per==0 && age==0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?)"
                        + " AND Game_Mode_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
                st.setInt(2, mode);
            }
            else if(genre!=0 && mode==0 && per==0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?)"
                        + " AND Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
                st.setInt(2, age);
            }
            else if(genre==0 && mode==0 && per!=0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Player_Perspective_Id = (?)"
                        + " AND Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, per);
                st.setInt(2, age);
            }
            else if(genre==0 && mode!=0 && per!=0 && age==0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Mode_Id = (?)"
                        + " AND Player_Perspective_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, mode);
                st.setInt(2, per);
            }
            else if(genre!=0 && mode==0 && per==0 && age==0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Genre_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, genre);
            }
            else if(genre==0 && mode==0 && per==0 && age!=0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Age_Tag_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, age);
            }
            else if(genre==0 && mode==0 && per!=0 && age==0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Player_Perspective_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, per);
            }
            else if(genre==0 && mode!=0 && per==0 && age==0)
            {
                st = con.prepareStatement("With a as (Select Distinct Game_Id from games where Game_Mode_Id = (?) ) Select Distinct *"
                        + " From game_list JOIN a ON game_list.Game_Id = a.Game_Id",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, mode);
            }
            
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
            }
        
        return flag;
    }
    
    public String[][] getGenre()
    {
        String[][] s = null;
        
        try
        {
            st = con.prepareStatement("Select * from game_genres",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery();
            
            s = new String[23][3];
            rs.beforeFirst();
            int i=0;
            int j=1;
            while(rs.next())
            {
                s[i][0] = Integer.toString(j);
                s[i][1] = rs.getString("Genre_Name");
                s[i][2] = rs.getString("Genre_Id");
                i++;
                j++;
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        
        return s;
    }
    
    public ArrayList<String> gameInfo(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("Select * from game_list where Game_Id = (?)",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();
            rs.next();

            a.add(rs.getString("Game_Name"));
            a.add(rs.getString("Collection_Name"));
            a.add(rs.getString("Cover_URL"));
            a.add(rs.getString("Release_Date"));
            a.add(rs.getString("Summary"));
            a.add(rs.getString("Storyline"));
            a.add(rs.getString("Popularity"));
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        
        return a;
    }
    
    public ArrayList<String> gameGenre(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("With a as (Select Distinct Game_Genre_Id from games where Game_Id = (?) )"
                    + " Select * from game_genres JOIN a ON game_genres.Genre_Id = a.Game_Genre_Id",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();
            
            while(rs.next())
            {
                a.add(rs.getString("Genre_Name"));
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        
        return a;
    }
    
    public ArrayList<String> gameMode(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("With a as (Select Distinct Game_Mode_Id from games where Game_Id = (?) )"
                    + " Select * from game_modes JOIN a ON game_modes.Mode_Id = a.Game_Mode_Id",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();

            while(rs.next())
            {
                a.add(rs.getString("Mode_Name"));
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
                
        return a;
    }
    
    public ArrayList<String> gamePerspective(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("With a as (Select Distinct Player_Perspective_Id from games where Game_Id = (?) )"
                    + " Select * from game_player_perspectives JOIN a ON game_player_perspectives.Perspective_Id = a.Player_Perspective_Id",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();

            while(rs.next())
            {
                a.add(rs.getString("Perspective_Name"));
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
                
        return a;
    }
    
    public ArrayList<String> gameAge(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("With a as (Select Distinct Age_Tag_Id from games where Game_Id = (?) )"
                    + " Select * from game_age_tags JOIN a ON game_age_tags.Age_Tag_Id = a.Age_Tag_Id",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();

            while(rs.next())
            {
                a.add(rs.getString("Age_Tag"));
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }

        return a;
    }
    
    public ArrayList<String> gamePlatform(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("With a as (Select Distinct Game_Platform_Id from games where Game_Id = (?) )"
                    + " Select * from game_platforms JOIN a ON game_platforms.Platform_Id = a.Game_Platform_Id",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();

            while(rs.next())
            {
                if(rs.getString("Platform_Generation") != null)
                {
                    a.add(rs.getString("Platform_Name")+" Gen - "+rs.getString("Platform_Generation"));
                }
                else
                {
                    a.add(rs.getString("Platform_Name"));
                }
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
                
        return a;
    }
    
    public ArrayList<String> gameCompany(int id)
    {
        ArrayList<String> a = new ArrayList<>();
        
        try
        {
            st = con.prepareStatement("With a as (Select Distinct Game_Company_Id from games where Game_Id = (?) )"
                    + " Select * from game_companies JOIN a ON game_companies.Company_Id = a.Game_Company_Id",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();

            while(rs.next())
            {
                a.add(rs.getString("Company_Name"));
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return a;
    }
    
    public void rating(int id,String uid,int rating)
    {
        try
        {
            if(rating!=0)
            {
                flag = false;
                st = con.prepareStatement("select * from rating where Game_Id = (?) and uid = (?);",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, id);
                st.setString(2, uid);
                rs = st.executeQuery();
                flag = rs.next();
                if(flag)
                {
                    st = con.prepareStatement("update rating set rating = (?) where Game_Id = (?) and uid = (?);",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    st.setInt(1, rating);
                    st.setInt(2, id);
                    st.setString(3, uid);
                }
                else
                {
                    st = con.prepareStatement("insert into rating(Game_Id,uid,Rating) values((?),(?),(?));",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    st.setInt(1, id);
                    st.setString(2, uid);
                    st.setInt(3, rating);
                }
                st.executeUpdate();
            }
            else
            {
                st = con.prepareStatement("delete from rating where Game_Id = (?) and uid = (?);",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, id);
                st.setString(2, uid);
                st.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        
        try
        {
            st = con.prepareStatement("select avg(rating) as Average from rating where Game_Id = (?);",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1, id);
            rs = st.executeQuery();
            rs.next();
            float avg  = Float.parseFloat(rs.getString("Average"));
            
            st = con.prepareStatement("update game_list set Popularity = (?) where Game_Id = (?);",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setFloat(1, avg);
            st.setInt(2, id);
            st.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
    }
    
    public int getRating(int id,String uid)
    {
        int rating = 0;
        
        try
        {
            st = con.prepareStatement("select * from rating where Game_Id = (?) and uid = (?);",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, id);
                st.setString(2, uid);
                rs = st.executeQuery();
                rs.next();
                rating = rs.getInt("Rating");
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return rating;
    }
    
    public void review(int id,String uid,String review)
    {
        try
        {
            if(review!=null)
            {
                flag = false;
                st = con.prepareStatement("select * from rating where Game_Id = (?) and uid = (?);",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, id);
                st.setString(2, uid);
                rs = st.executeQuery();
                flag = rs.next();
                if(flag)
                {
                    st = con.prepareStatement("update rating set review = (?) where Game_Id = (?) and uid = (?);",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    st.setString(1, review);
                    st.setInt(2, id);
                    st.setString(3, uid);
                }
                else
                {
                    st = con.prepareStatement("insert into rating(Game_Id,uid,Review) values((?),(?),(?));",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    st.setInt(1, id);
                    st.setString(2, uid);
                    st.setString(3, review);
                }
                st.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
    }
    
    public String getReview(int id,String uid)
    {
        String review="";
        
        try
        {
            st = con.prepareStatement("select * from rating where Game_Id = (?) and uid = (?);",
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                st.setInt(1, id);
                st.setString(2, uid);
                rs = st.executeQuery();
                rs.next();
                review = rs.getString("Review");
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return review;
    }
    
    public String[][] allReviews(int id)
    {
        String[][] s = null;
        int i=0;
        
        try
        {
            st = con.prepareStatement("select * from rating where Game_Id = (?);",
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            st.setInt(1,id);
            rs = st.executeQuery();
            while(rs.next())
            {
                i++;
            }
            
            i = (i>14?i:14);
            s = new String[i][3];
            rs.beforeFirst();
            i=0;
            while(rs.next())
            {
                s[i][0] = en.decrypt(rs.getString("uid"));
                s[i][1] = rs.getString("Review");
                s[i][2] = rs.getString("Rating");
                i++;
            }
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        return s;
    }
}