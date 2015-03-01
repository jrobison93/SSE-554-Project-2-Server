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

				
				int pubNums[] = Diffie.Generate();
				out.println(pubNums[0]);
				out.println(pubNums[1]);
				
				int clientSecret = in.nextInt();
				
				int secretInt = Diffie.Secret(pubNums[0]);
				int secretNum = Diffie.GenMessage(pubNums, secretInt);
				
				out.println(secretNum);
				
				int secretKey = Diffie.GenKey(pubNums, clientSecret, secretInt);
				
				int compKey = in.nextInt();
				
				System.out.println("The secret key is " + secretKey);
				
				if(secretKey != compKey)
				{
					System.out.println("The keys do not match");
					close();
					stop();
					
				}
				
				System.out.println("The keys match!");
				in.nextLine();
				
				out.println("Hello! What is your username?");
				username = in.nextLine();
				
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
					out.println("Who would you like to message?");
					receiver = in.nextLine().trim();
					while(!users.list.containsKey(receiver))
					{
						out.println("The user does not exist.");
						System.out.println("The user does not exist.");
						receiver = in.nextLine().trim();
					}
					out.println("Found");
					break;
				}
				
				System.out.println("Receiver: " + receiver + "\n");
				System.out.println(in.hasNextLine());
				
				while(in.hasNextLine())
				{
					System.out.println("Waiting");
					sendToUser(username, receiver, in.nextLine());
					System.out.println("Message Sent " + username +"\n");
				}
				
				System.out.println("Done.");
				
				
				
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
	
	private void sendToUser(String sender, String receiver, String text)
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
			out.println(users.list.size());
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
