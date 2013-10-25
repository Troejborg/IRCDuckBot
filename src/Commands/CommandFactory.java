package Commands;

import Game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 20-10-13
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public class CommandFactory {
  private static String BATTLE = "!battle";
  private static String ADMIN = "!super";
  private Map<String, Player> players;
  public CommandFactory(){
    players = new HashMap<String, Player>();
  }
  public Command getCommandType(String sender, String[] cmdStrings, String cmdString){
    String type = cmdStrings[0];
    if(type.equals(BATTLE))
      return new BattleCommand(sender, cmdStrings, players);
    else return new DefaultCommand(sender, cmdString);
  }
}
