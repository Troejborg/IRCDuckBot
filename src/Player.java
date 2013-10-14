import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/14/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player {
  private int level;
  private int totalExp;
  private String playerName;
  private Weapon weapon;
  private int wins;
  private int losses;
  private int damageRating;
  private int defenseRating;
  private int totalHealth;
  private boolean isAlive;
  private int currentHealth;
  private Random playerRandomizer;
  private double damageMod;
  private int attackRating;

  public Player(String name){
    CreateNewPlayer(name);
  }

  public void CreateNewPlayer(String name){
    this.playerName = name;
    long rgenseed = System.currentTimeMillis();
    playerRandomizer = new Random();
    playerRandomizer.setSeed(rgenseed);
    isAlive = true;
    initAttributes(playerRandomizer);
  }

  private void initAttributes(Random playerRandomizer) {
    level = 1;
    totalExp = 0;
    damageRating = 10;
    defenseRating = 10;
    totalHealth = 50;
    currentHealth = totalHealth;
    damageMod = 1.5;
  }

  public int getCurrentHealth(){
    return currentHealth;
  }
  public void setIncomingDamage(int damageTaken){
    currentHealth = currentHealth - damageTaken;
  }

  public String getPlayerName() {
    return playerName;
  }

  public double getDamageMod() {
    return damageMod;
  }

  public int getDamageRating() {
    return damageRating*(int)damageMod;
  }

  public int getDefenseRating() {
    return defenseRating;
  }

  public void setIsAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean getIsAlive() {
    return isAlive;
  }
}
