package Game;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;


public class GameDataProvider {
  private static final String XP_KEY = "xp";
  private static final String NAME_KEY = "name";
  private static final String WINS_KEY = "wins";
  private static final String LOSS_KEY = "loss";
  private static final String AGI_KEY = "agility";
  private static final String STR_KEY = "strength";
  private static final String STA_KEY = "stamina";
  private static final String ENDU_KEY = "endurance";
  private static final String WEP_ID = "wep_id";
  private static final String PLAYER_ID = "id";
  private static final String LEVEL_KEY = "level";
  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String db_name = "ronnie_steamduck";
  static final String DB_URL = "jdbc:mysql://cupcakeserver.dk/ronnie_steamduck";

  Object syncObj;

  public GameDataProvider(){
    syncObj = new Object();
  }

  //  Database credentials
  static final String USER = "user";
  static final String PASS = "pass";

  private Map<String, Player> playerList;
  private FutureTask<Map<String, Player>> downloadDataTask;

  public boolean isDownloadDone() {
    return downloadDataTask.isDone();
  }
  public Map<String, Player> getPlayers(){
    return playerList;
  }
  public void storePlayers(){
    new Thread(new StoreData(playerList)).start();
  }
  public void fetchPlayers(){
    FetchData callable = new FetchData();
    downloadDataTask = new FutureTask<Map<String, Player>>(callable);
    ExecutorService executor = Executors.newFixedThreadPool(1);

    executor.execute(downloadDataTask);

    try
    {
      synchronized(syncObj)
      {
        syncObj.wait();
        playerList = downloadDataTask.get();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (ExecutionException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  private class StoreData implements Runnable{
    private Map<String, Player> players;

    public StoreData(Map<String, Player> players){
      this.players = players;
    }

    @Override
    public void run() {
        storePlayers();
    }

    private void storePlayers() {
      Connection conn = null;
      Statement stmt = null;
        //STEP 2: Register JDBC driver
        try {
          Class.forName("com.mysql.jdbc.Driver");
          //STEP 3: Open a connection
          System.out.println("Connecting to database...");
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          System.out.println("Creating statement...");
          //STEP 4: Execute a query
          for(Map.Entry<String,Player> player : players.entrySet()){
            stmt = conn.createStatement();
            int i = 1;
            PreparedStatement ps;
            if(player.getValue().getPlayerID() > 0){
              ps = conn.prepareStatement("REPLACE INTO Players (id, xp, wins, loss, agility, strength, endurance, stamina, name, wep_id, level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
              ps.setInt(i++, player.getValue().getPlayerID());
            }else{
              ps = conn.prepareStatement("REPLACE INTO Players (xp, wins, loss, agility, strength, endurance, stamina, name, wep_id, level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }

            ps.setInt(i++, player.getValue().getTotalExp());
            ps.setInt(i++, player.getValue().getWins());
            ps.setInt(i++, player.getValue().getLosses());
            ps.setInt(i++, player.getValue().getAgility());
            ps.setInt(i++, player.getValue().getStrength());
            ps.setInt(i++, player.getValue().getEndurance());
            ps.setInt(i++, player.getValue().getStamina());
            ps.setString(i++, player.getValue().getPlayerName());
            ps.setInt(i++, player.getValue().getWeaponId());
            ps.setInt(i++, player.getValue().getLevel());

            ps.executeUpdate();
          }
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
  }

  private class FetchData implements Callable<Map<String,Player>>{

    private Map<String,Player> downloadPlayers() {
      Connection conn = null;
      Statement stmt = null;
      Map<String, Player> players = new HashMap<String, Player>();
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
        sql = "SELECT * FROM Players";
        ResultSet rs = stmt.executeQuery(sql);

        //STEP 5: Extract data from result set
        while(rs.next()){
          Player newPlayer = new Player();
          newPlayer.setPlayerID(rs.getInt(PLAYER_ID));
          newPlayer.setName(rs.getString(NAME_KEY));
          newPlayer.setTotalExp(rs.getInt(XP_KEY));
          newPlayer.setWins(rs.getInt(WINS_KEY));
          newPlayer.setLosses(rs.getInt(LOSS_KEY));
          newPlayer.setAgility(rs.getInt(AGI_KEY));
          newPlayer.setStrength(rs.getInt(STR_KEY));
          newPlayer.setStamina(rs.getInt(STA_KEY));
          newPlayer.setEndurance(rs.getInt(ENDU_KEY));
          newPlayer.setWeaponId(rs.getInt(WEP_ID));
          newPlayer.setLevel(rs.getInt(LEVEL_KEY));

          players.put(newPlayer.getPlayerName(), newPlayer);
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

      synchronized(syncObj)
      {
        System.out.println("Background Thread notifing...");
        syncObj.notify();
      }
      return players;
    }


    @Override
    public Map<String, Player> call() throws Exception {
      return downloadPlayers();
    }
  }
}
