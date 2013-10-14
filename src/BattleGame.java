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
  private Player attackingPlayer, defendingPlayer, playerOne, playerTwo;
  private Random randomizer;
  public int RoundNumber;
  private int lastDamageRoll, lastDefenseRoll, lastResultingDamage;
  boolean isMatchStarted = false;
  public BattleGame(Player playerOne, Player playerTwo){
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
    long rgenseed = System.currentTimeMillis();
    randomizer = new Random();
    randomizer.setSeed(rgenseed);
  }

  public boolean StartMatch(){
    if(isMatchStarted)
      return false;
    else
      isMatchStarted = true;
    RoundNumber = 0;
    int startingPlayerRoll = randomizer.nextInt(LEET_ROLLER)%2;
    if(startingPlayerRoll == 0){
      attackingPlayer = playerOne;
      defendingPlayer = playerTwo;
    }else{
      attackingPlayer = playerTwo;
      defendingPlayer = playerOne;
    }
    return true;
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
    defendingPlayer.setIncomingDamage((lastResultingDamage > 0 ? lastResultingDamage : 0));

    if(defendingPlayer.getCurrentHealth() <= 0)
      defendingPlayer.setIsAlive(false);
    else
    {
      Player tempPlayer = defendingPlayer;
      defendingPlayer = attackingPlayer;
      attackingPlayer = tempPlayer;
    }

    RoundNumber++;
  }

  public boolean isGameOver(){
    if(isMatchStarted && attackingPlayer.getIsAlive() && defendingPlayer.getIsAlive()){
      return false;
    }
    else{
      isMatchStarted = false;
      return true;
    }
  }
}
