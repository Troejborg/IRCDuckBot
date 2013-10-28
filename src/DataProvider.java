import Game.Player;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class DataProvider implements Callable<List<Player>>{
  private transient static final String XP_KEY = "xp";
  private transient static final String NAME_KEY = "playername";
  private transient static final String WINS_KEY = "wins";
  private transient static final String LOSS_KEY = "loss";
  private transient static final String AGI_KEY = "agility";
  private transient static final String STR_KEY = "strength";
  private transient static final String STA_KEY = "stamina";
  private transient static final String ENDU_KEY = "endurance";
  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost/EMP";

  //  Database credentials
  static final String USER = "username";
  static final String PASS = "password";

  private List<Player> getPlayers() {
    Connection conn = null;
    Statement stmt = null;
    List<Player> players = new ArrayList<Player>();
    try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM Employees";
      ResultSet rs = stmt.executeQuery(sql);

      //STEP 5: Extract data from result set
      while(rs.next()){
        Player newPlayer = new Player();

        newPlayer.setName(rs.getString(NAME_KEY));
        newPlayer.setTotalExp(rs.getInt(XP_KEY));
        newPlayer.setWins(rs.getInt(WINS_KEY));
        newPlayer.setLosses(rs.getInt(LOSS_KEY));
        newPlayer.setAgility(rs.getInt(AGI_KEY));
        newPlayer.setStrength(rs.getInt(STR_KEY));
        newPlayer.setStamina(rs.getInt(STA_KEY));
        newPlayer.setEndurance(rs.getInt(ENDU_KEY));;

        players.add(newPlayer);
      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
    }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
    }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
    }finally{
      //finally block used to close resources
      try{
        if(stmt!=null)
          stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
        if(conn!=null)
          conn.close();
      }catch(SQLException se){
        se.printStackTrace();
      }//end finally try
    }//end try
    return players;
  }

  public boolean storePlayers(List<Player> playerList){
    return true;
  }

  @Override
  public List<Player> call() throws Exception {
    return getPlayers();
  }
}
