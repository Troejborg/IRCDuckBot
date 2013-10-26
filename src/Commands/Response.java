package Commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Troej_000
 * Date: 25-10-13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
public class Response {
  public String Channel;
  public List<String> Messages;
  public Response(){
    Messages = new ArrayList<String>();
  }
}
