package BattleGame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
  private String name;
  private int weapon;
  private int wins;
  private int losses;
  private int avgDamage;

  public boolean CreateNewPlayer(String jsonObj){
    Object obj=JSONValue.parse(jsonObj);
    JSONArray playerAttrs =(JSONArray)obj;

    return initAttributes(playerAttrs);
  }

  private boolean initAttributes(JSONArray playerAttrs) {
    level = 1;
    totalExp = 0;
    return false;  //To change body of created methods use File | Settings | File Templates.
  }
}
