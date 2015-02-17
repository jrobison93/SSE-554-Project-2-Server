import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MessageServer 
{
	private static UsersList users;
	private static final int port = 8189;
	
	public static void main(String[] args)
	{
		users = UsersList.getInstance();
		try
		{
			int sockets = 1;
			ServerSocket s = new ServerSocket(port);
			
			while(true)
			{
				Socket incoming = s.accept();
				Runnable r = new CommunicationThread(incoming);
				Thread t = new Thread(r);
				t.start();
				sockets++;
			}
			
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}

}
