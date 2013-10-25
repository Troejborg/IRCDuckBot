package Commands;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 25-10-13
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class DefaultCommand implements Command {
  private String superAuthedUser = "lite_";
  public static final String THE_TIME = "!time";
  public static final String LAST_MSG = "!lastmsg";
  private static String HELLO_STEAMDUCK = "hello steamduck";
  private static String LOVE_STEAMDUCK = "<3 steamduck";
  private static String ECHO = "!echo ";
  private final String sender;
  private String action;
  Response response;

  public DefaultCommand(String sender, String cmdString) {
    this.sender = sender;
    action = cmdString;
    response = new Response();
  }

  @Override
  public Response interpretCommand() {
    response.Messages.clear();
    if (action.equals(THE_TIME)) {
      String time = new java.util.Date().toString();
      response.Messages.add(sender + ": The time is now " + time);
    }
    if(action.equals(LOVE_STEAMDUCK)){
      response.Messages.add("aaw. I love you too, " + sender + " <3 !");
    }
    if(action.equals(HELLO_STEAMDUCK)){
      if(superAuthedUser.equals(sender)){
        response.Messages.add("Hey there, " + sender + ". youre my favourite person!");
      }else
        response.Messages.add("go away, peasant.");
    }
    else if(action.equalsIgnoreCase("!martin")){
      response.Messages.add("http://www.cupcakeserver.dk/hvordanharmartindet");
    }

    return response;
  }

  @Override
  public Response getAdditionalMessages() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
