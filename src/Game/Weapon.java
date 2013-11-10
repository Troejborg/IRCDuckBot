package game;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 14-10-13
 * Time: 18:37
 * To change this template use File | Settings | File Templates.
 */
public class Weapon {
  public static int TOTAL_TYPES = 3;

  protected int damageMod = 1;
  protected int weaponId = 0;

  public Weapon(){

  }

  public int getDamageModifier(){
    return damageMod;
  }

}
