package game;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/14/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player implements java.io.Serializable {
  private transient static final int HEALTH_MULTIPLIER = 10;
  private transient static final int ENDURANCE_MULTIPLIER = 100;

  private transient static int XP_NEEDED_FIRST_LEVEL = 69;
  private transient int maxHealth;
  private transient boolean isAlive = true;
  private transient int currentHealth;

  public int xpNeededForLevel;
  public int WeaponID;
  public int Wins;
  public int Losses;
  public int Agility;
  public int Strength;
  public int Stamina;
  public int Endurance;
  public int Level;
  public int TotalExp;
  public String PlayerName;

  public Player(){
  }
  public Player(String playerName){
    this.PlayerName = playerName;
    initAttributes();
  }

  private void initAttributes() {
    Level = 1;
    Wins = 0;
    Losses = 0;
    TotalExp = 0;
    xpNeededForLevel = XP_NEEDED_FIRST_LEVEL;
    Agility = 10;
    Endurance = 10;
    Stamina = 15;
    Strength = 15;
    maxHealth = currentHealth = Stamina*10;

  }

  public void setIncomingDamage(int damageTaken){
    currentHealth = currentHealth - damageTaken;
  }
  public int getDamagePotential() {
    return Strength*5;
  }

  public double getDefensePotential() {
    return Endurance/ENDURANCE_MULTIPLIER;
  }

  public int getMaxHealth() {
    return Stamina*10;
  }

  public boolean grantExp(int expGain){
    TotalExp = TotalExp + expGain;
    if(TotalExp >= xpNeededForLevel){
      xpNeededForLevel = (int)(0.5f*xpNeededForLevel) + Math.round(xpNeededForLevel*1.1f);
      doLevelUp();
      return true;
    }
    else return false;
  }

  public int getExpForNextLevel(){
    return xpNeededForLevel - TotalExp;
  }

  private void doLevelUp() {
    Level++;
    Strength += 2;
    Stamina += 3;
    Endurance += 2;
  }

  public void revitalize() {
    currentHealth = HEALTH_MULTIPLIER * Stamina;
    isAlive = true;
  }

  public int getCurrentHealth(){
    return currentHealth;
  }

  public void setIsAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean getIsAlive() {
    return isAlive;
  }
}
