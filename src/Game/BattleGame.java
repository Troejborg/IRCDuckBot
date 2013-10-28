package Game;

import java.util.HashMap;
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
  private Map<String, Player> playerList;
  private Random randomizer;
  public int RoundNumber;
  private double lastDamageRoll;
  private double lastDefenseRoll;
  private int lastResultingDamage;
  boolean isMatchOngoing = false;
  private int matchExpWin;
  private int matchExpLoss;

  public BattleGame(){
    long rgenseed = System.currentTimeMillis();
    randomizer = new Random();
    randomizer.setSeed(rgenseed);
    playerList = new HashMap<String, Player>();
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

  public void setMatchExp(){
    // TODO : FIX THIS
    float multiplier = 1+(losingPlayer.getLevel()-winningPlayer.getLevel())*0.1f;
    multiplier = multiplier >= 0 ? multiplier : 0;

    matchExpWin = winningPlayer.getLevel()*5 + 45;
    matchExpWin = Math.round(matchExpWin*multiplier);

    matchExpLoss = losingPlayer.getLevel()*5 + 15;
    matchExpLoss = Math.round(matchExpLoss*multiplier);
  }

  public int getWinningExp(){
    return matchExpWin;
  }

  public int getLosingExp(){
    return matchExpLoss;
  }
  public boolean grantWinnngExp(){
    winningPlayer.AddWin();
    return winningPlayer.grantExp(matchExpWin);
  }

  public boolean grantLosingExp(){
    losingPlayer.AddLoss();
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
