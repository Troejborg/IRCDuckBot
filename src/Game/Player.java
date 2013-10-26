package Game;

/**
 * Created with IntelliJ IDEA.
 * User: rt
 * Date: 10/14/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player implements java.io.Serializable {
  private transient static int XP_NEEDED_FIRST_LEVEL = 69;
  private int level;
  private int totalExp;
  private String playerName;
  private String playerIdent;
  private int weaponId;
  private int wins;
  private int losses;
  private int agility;
  private int strength;
  private int stamina;
  private int endurance;
  private transient int maxHealth;
  private transient boolean isAlive;
  private transient int currentHealth;
  private transient int xpNeededForLevel;

  public Player(String[] playerInfo){
    CreateNewPlayer(playerInfo);
  }

  public void CreateNewPlayer(String[] playerInfo){
    this.playerName = playerInfo[0];
    this.playerIdent = playerInfo[1];
    long rgenseed = System.currentTimeMillis();
    isAlive = true;
    initAttributes();
  }

  private void initAttributes() {
    level = 1;
    wins = 0;
    losses = 0;
    totalExp = 0;
    xpNeededForLevel = XP_NEEDED_FIRST_LEVEL;
    agility = 10;
    endurance = 10;
    stamina = 20;
    strength = 10;
    currentHealth = stamina*10;
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

  public int getDamagePotential() {
    return strength*5;
  }

  public double getDefensePotential() {
    return (endurance*8)/100;
  }

  public void setIsAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean getIsAlive() {
    return isAlive;
  }

  public void AddWin(){
    wins++;
  }

  public void AddLoss(){
    losses++;
  }

  public int getWins(){
    return wins;
  }
  public int getLosses(){
    return losses;
  }
  public void setLevel(int level){
    this.level = level;
  }
  public int getLevel(){
    return level;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public void setCurrentHealth(int currentHealth) {
    this.currentHealth = currentHealth;
  }

  public boolean grantExp(int expGain){
    totalExp = totalExp + expGain;
    if(totalExp >= xpNeededForLevel){
      xpNeededForLevel = xpNeededForLevel + Math.round(xpNeededForLevel*1.1f);
      doLevelUp();
      return true;
    }
    else return false;
  }

  public int getExpForNextLevel(){
    return xpNeededForLevel - totalExp;
  }

  private void doLevelUp() {
    level++;
    strength += 2;
    stamina += 2;
    endurance += 2;

    setMaxHealth(Math.round(maxHealth*1.2f));
  }

  private int getExpNeededForNextLevel(){
    return xpNeededForLevel - totalExp;
  }
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }

  public void revitalize() {
    setCurrentHealth(maxHealth);
    setIsAlive(true);
  }
}
