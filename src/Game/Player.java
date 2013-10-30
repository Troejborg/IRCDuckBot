package Game;

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
  private transient int xpNeededForLevel;

  private int weaponId;
  private int wins;
  private int losses;
  private int agility;
  private int level;
  private int totalExp;
  private String playerName;
  private int strength;
  private int stamina;
  private int endurance;
  private int playerID;

  public Player(){
  }
  public Player(String playerName){
    this.playerName = playerName;
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
    stamina = 15;
    strength = 15;
    maxHealth = currentHealth = stamina*10;
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
    return endurance/ENDURANCE_MULTIPLIER;
  }

  public int getMaxHealth() {
    return stamina*10;
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
    stamina += 3;
    endurance += 2;
  }

  private int getExpNeededForNextLevel(){
    return xpNeededForLevel - totalExp;
  }

  public void revitalize() {
    setCurrentHealth(HEALTH_MULTIPLIER * stamina);
    setIsAlive(true);
  }

  public int getAgility() {
    return agility;
  }

  public void setAgility(int agility) {
    this.agility = agility;
  }
  public int getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(int weaponId) {
    this.weaponId = weaponId;
  }
  public void setWins(int wins) {
    this.wins = wins;
  }
  public void setLosses(int losses) {
    this.losses = losses;
  }

  public int getStamina(){
    return stamina;
  }

  public void setStamina(int stamina){
    this.stamina = stamina;
  }
  public void setEndurance(int endurance){
    this.endurance = endurance;
  }
  public int getEndurance(){
    return endurance;
  }
  public int getStrength() {
    return strength;
  }

  public void setStrength(int strength) {
    this.strength = strength;
  }

  public void setName(String name){
    this.playerName = name;
  }

  public String getName(){
    return playerName;
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
  public void setIsAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean getIsAlive() {
    return isAlive;
  }
  public void setLevel(int level){
    this.level = level;
  }
  public int getLevel(){
    return level;
  }

  public int getTotalExp(){
    return totalExp;
  }
  public void setTotalExp(int totalExp){
    this.totalExp = totalExp;
  }

  public int getPlayerID() {
    return playerID;
  }

  public void setPlayerID(int playerID) {
    this.playerID = playerID;
  }
}
