import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class CommunicationThread extends Thread
{
	private MessageServer server;
	private Socket incoming;
	private UsersList users;
	
	private Scanner in;
	private PrintWriter out;
	
	private String username;
	private String receiver;
	
	public CommunicationThread(Socket i)
	{
		incoming = i;
		users = UsersList.getInstance();
	}
	
	public void run()
	{
		try
		{
			try
			{
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();
				
				in = new Scanner(inStream);
				out = new PrintWriter(outStream, true);
				
				out.println("Hello! What is your username?");
				username = in.nextLine().trim();
				
				System.out.println(username);
				
				while(users.list.containsKey(username))
				{
					out.println("This username \"" + username + "\" already exists! Try again: ");
					username = in.nextLine().trim();
				}
				
				users.list.put(username, this);
				
				out.println("Welcome, " + username + "!");
				
				printUsers();
				
				while(users.list.size() >= 1)
				{
					out.println("Who would you like to message? \n");
					receiver = in.nextLine().trim();
					while(!users.list.containsKey(receiver))
					{
						out.println("The user does not exist. \n");
						receiver = in.nextLine().trim();
					}
					break;
				}
				
				System.out.println("Receiver: " + receiver + "\n");
				
				while(in.hasNextLine())
				{
					sendToUser(username, receiver, in.nextLine());
					System.out.println("Message Sent " + username +"\n");
				}
				
				
				
			}
			finally
			{
				close();
				stop();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void sendToClient(String message)
	{
		out.println(message);
	}
	
	public void sendToUser(String sender, String receiver, String text)
	{
		if(users.list.containsKey(receiver))
		{
			users.list.get(receiver).sendToClient(sender + ": " + text);
			if(text.equals("_bye_"))
			{
				try
				{
					close();
				}
				catch(IOException e)
				{
					System.out.println("Error closing thread: " + e);
				}
				stop();
				
			}
		}
		else
		{
			sendToClient("User not found.");
			//printUsers();
			
		}
	}
	
	
	private void printUsers()
	{
		if(users.list.size() == 0)
		{
			out.println("You are the only user logged in!");
		}
		else
		{
			out.print("These are the available users: \n");
			for(String key : users.list.keySet())
			{
				out.print("\t" + key +"\n");
			}
		}
		
		out.flush();
	}
	
	public void close() throws IOException
	{
		users.list.remove(username);
		if(incoming != null)
			incoming.close();
		if(in != null)
			in.close();
		if(out != null)
			out.close();
	}

}
