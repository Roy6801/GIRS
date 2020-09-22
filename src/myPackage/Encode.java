package myPackage;

public class Encode
{
    public String encrypt(String s)
    {
        String str="";
        int len=s.length();
        int j;
        char c;
        
        for(int i=0;i<len;i++)
        {
            j = (int)s.charAt(i);
            if(i%2==0)
            {
                j = j+len;
            }
            else
            {
                j = j-len;
            }
            c = (char)j;
            str = str+c;
        }
        
        return str;
    }
    
    public String decrypt(String s)
    {
        String str="";
        int len=s.length();
        int j;
        char c;
        
        for(int i=0;i<len;i++)
        {
            j = (int)s.charAt(i);
            if(i%2==0)
            {
                j = j-len;
            }
            else
            {
                j = j+len;
            }
            c = (char)j;
            str = str+c;
        }
        
        return str;
    }
}