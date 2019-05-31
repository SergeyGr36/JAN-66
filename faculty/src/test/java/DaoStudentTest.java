import java.sql.*;

public class DaoStudentTest {
        private static Connection con = null;
        private static String username = "name";
        private static String password = "pass";
        private static String URL = "jdbc:jtds:sqlserver://localhost:1433";

        public static void main(String[] args) throws SQLException {
            /* DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());*/

            con = DriverManager.getConnection(URL, username, password);

            if(con!=null) System.out.println("Connection Successful !\n");
            if (con==null) System.exit(0);
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("select hd from pc group by hd having count(hd)>=2");

            int x = rs.getMetaData().getColumnCount();

            while(rs.next()){
                for(int i=1; i<=x;i++){
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
            System.out.println();
            if(rs!=null)rs.close();
            if(st!=null)st.close();
            if(con!=null)con.close();
        }
    }


