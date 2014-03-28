package game;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/14/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class BattleGame
{
  private static int LEET_ROLLER = 1337;
  private Player attackingPlayer, defendingPlayer, winningPlayer, losingPlayer;
  private Random randomizer;
  public int RoundNumber;
  private double lastDamageRoll;
  private double lastDefenseRoll;
  private int lastResultingDamage;
  boolean isMatchOngoing = false;
  private int matchExpWin;
  private int matchExpLoss;
  private Map<String, Player> playerList;
  private Firebase ref;
  public BattleGame(){
    long rgenseed = System.currentTimeMillis();
    randomizer = new Random();
    randomizer.setSeed(rgenseed);
    playerList = new HashMap<String, Player>();

    // Create a reference to a Firebase location
    ref = new Firebase("https://steamduck.firebaseIO.com/players");

    // Read data and react to changes
    ref.addValueEventListener(new ValueEventListener() {

      @Override
      public void onDataChange(DataSnapshot snap) {
        if(snap.getValue() != null){
          GsonBuilder gsonBuilder = new GsonBuilder();
          Gson gson = gsonBuilder.create();
          Type type = new TypeToken<Map<String, Player>>(){}.getType();
          String json = gson.toJson(snap.getValue());
          playerList = gson.fromJson(json, type);
          System.out.print(playerList);
        }
      }

      @Override public void onCancelled() {

      }
    });
  }
  public boolean StartMatch(){
      if(isMatchOngoing)
        return false;
      else
        isMatchOngoing = true;
      RoundNumber = 0;
      return true;
  }

  public Map<String,Player> getPlayerList(){
    return playerList;
}

  public void rollStartingPositions(Player playerOne, Player playerTwo) {
    int startingPlayerRoll = randomizer.nextInt(LEET_ROLLER)%2;
    if(startingPlayerRoll == 0){
      attackingPlayer = playerOne;
      defendingPlayer = playerTwo;
    }else{
      attackingPlayer = playerTwo;
      defendingPlayer = playerOne;
    }
  }

  public boolean getIsMatchOngoing(){
    return isMatchOngoing;
  }
  public Player getAttackingPlayer(){
    return attackingPlayer;
  }
  public Player getDefendingPlayer(){
    return defendingPlayer;
  }
  public int getLastResultingDamage(){
    return lastResultingDamage;
  }

  public void startNextRound(){
    lastDamageRoll = attackingPlayer.getDamagePotential()*0.5 + (Math.random() * (attackingPlayer.getDamagePotential()*2 - attackingPlayer.getDamagePotential()*0.5));
    lastDefenseRoll = defendingPlayer.getDefensePotential() + (Math.random() * (0.9 - defendingPlayer.getDefensePotential()));
    lastResultingDamage = (int) Math.round(lastDamageRoll*lastDefenseRoll);
    defendingPlayer.setIncomingDamage((lastResultingDamage));

    if(defendingPlayer.getCurrentHealth() <= 0)
      defendingPlayer.setIsAlive(false);

    RoundNumber++;
  }

  public void switchTurns() {
    Player tempPlayer = defendingPlayer;
    defendingPlayer = attackingPlayer;
    attackingPlayer = tempPlayer;
  }

  public boolean evalIsGameOver(){
    if(attackingPlayer.getIsAlive() && defendingPlayer.getIsAlive()){
      return false;
    }
    else{
      winningPlayer = attackingPlayer.getIsAlive() ? attackingPlayer : defendingPlayer;
      losingPlayer = !attackingPlayer.getIsAlive() ? attackingPlayer : defendingPlayer;
      isMatchOngoing = false;
      return true;
    }
  }

  public void save() {
    clearPlayers();

    getPlayerList().put(getWinner().PlayerName, getWinner());
    getPlayerList().put(getLoser().PlayerName, getLoser());
    ref.setValue(playerList);
  }

  public void setMatchExp(){
    float winningPlayermultiplier = 1 + (losingPlayer.Level-winningPlayer.Level)*0.2f;
    float losingPlayerMultiplier = 1 + (winningPlayer.Level-losingPlayer.Level)*0.2f;
    matchExpWin = winningPlayer.Level*5 + 40;
    matchExpWin = Math.round(matchExpWin*winningPlayermultiplier);
    matchExpWin = matchExpWin > 0 ? matchExpWin : 0;

    matchExpLoss = losingPlayer.Level*3 + 25;
    matchExpLoss = Math.round(matchExpLoss*losingPlayerMultiplier);
    matchExpLoss = matchExpLoss > 0 ? matchExpLoss : 0;
  }

  public int getWinningExp(){
    return matchExpWin;
  }

  public int getLosingExp(){
    return matchExpLoss;
  }
  public boolean grantWinnngExp(){
    winningPlayer.Wins++;
    return winningPlayer.grantExp(matchExpWin);
  }

  public boolean grantLosingExp(){
    losingPlayer.Losses++;
    return losingPlayer.grantExp(matchExpLoss);
  }

  public Player getWinner() {
    return winningPlayer;
  }

  public Player getLoser() {
    return losingPlayer;
  }

  public void clearPlayers() {
    attackingPlayer.revitalize();
    defendingPlayer.revitalize();
  }
}
