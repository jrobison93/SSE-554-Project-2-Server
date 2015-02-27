import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

public class UsersList 
{
	public HashMap <String, CommunicationThread> list;
	
	private static UsersList instance = null;
	private UsersList()
	{
		list = new HashMap<String, CommunicationThread>();	
	}
	
	public static synchronized UsersList getInstance()
	{
		if(instance == null)
		{
			instance = new UsersList();
		}
		
		return instance;
	}
	

}
