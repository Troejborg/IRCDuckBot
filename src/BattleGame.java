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
  private int lastDamageRoll, lastDefenseRoll, lastResultingDamage;
  boolean isMatchOngoing = false;
  private int matchExpWin;
  private int matchExpLoss;

  public BattleGame(Player playerOne, Player playerTwo){
    long rgenseed = System.currentTimeMillis();
    randomizer = new Random();
    randomizer.setSeed(rgenseed);
    rollStartingPositions(playerOne, playerTwo);
  }


  public boolean StartMatch(){
    if(isMatchOngoing)
      return false;
    else
      isMatchOngoing = true;
    RoundNumber = 0;
    return true;
  }

  private void rollStartingPositions(Player playerOne, Player playerTwo) {
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

  public int getLastDamageRoll(){
    return lastDamageRoll;
  }

  public int getLastDefenseRoll(){
    return lastDefenseRoll;
  }

  public int getLastResultingDamage(){
    return lastResultingDamage;
  }

  public void startNextRound(){
    lastDamageRoll = randomizer.nextInt(LEET_ROLLER)%attackingPlayer.getDamageRating();
    lastDefenseRoll = randomizer.nextInt(LEET_ROLLER)%defendingPlayer.getDefenseRating();
    lastResultingDamage = lastDamageRoll - lastDefenseRoll > 0 ? lastDamageRoll - lastDefenseRoll : 0;
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
    matchExpWin = (winningPlayer.getLevel()*5) + 45 - 45*(Math.round((losingPlayer.getLevel()-winningPlayer.getLevel())*0.5f));
    matchExpLoss = (losingPlayer.getLevel()*5) + 15 - 15*(Math.round((losingPlayer.getLevel()-winningPlayer.getLevel())*0.5f));
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
