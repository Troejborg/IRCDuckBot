package commands;

import game.BattleGame;

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
  BattleGame game;
  public CommandFactory(){
    game = new BattleGame();
  }
  public Command getCommandType(String sender, String[] cmdStrings, String cmdString){
    String type = cmdStrings[0];
    if(type.equals(BATTLE))
      return new BattleCommand(sender, cmdStrings, game);
    else return new DefaultCommand(sender, cmdString);
  }
}
