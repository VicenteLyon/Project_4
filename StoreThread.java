//John R. & Vicente L. 
import java.io.*; 
import java.net.*; 
import java.util.ArrayList;
import java.util.*;

public class StoreThread extends Thread {
	private Socket client; 
	public static ArrayList<Account> accounts = new ArrayList<Account>();
	private Account userAccount; 
	private BufferedReader incoming; 
	private PrintWriter outgoing; 

	public StoreThread(Socket client) { 
		this.client = client;

	}//end of StoreThread method 

	public void run() { 
		try { 
			incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
			outgoing = new PrintWriter(client.getOutputStream(), true); 
			System.out.println("Connected to client: " + client.getInetAddress().getHostAddress());
			String request = incoming.readLine(); 
			System.out.println("Request: " + request);
			while(!request.equals("QUIT")) { 
				switch(request) { 
				case "LOGIN" -> { 
					System.out.println("Login...");
					login(accounts, incoming, outgoing);
					break;
				}
				
				case "ACCOUNT_LIST" -> { 
					System.out.println("Sending account list...");
					sendAccountList(outgoing);
					break;
				}
				
				case "PROFILE" -> { 
					System.out.println("Sending profile...");
					sendProfile(outgoing);
					break;
				}
				
				case "CHANGE_PASSWORD" -> { 
					System.out.println("Change password...");
					changePassword(userAccount, incoming, outgoing);
					break;
				}
				
				
				}
				request = incoming.readLine();
			}//end of while loop
			System.out.println("Quitting");
			client.close();
			
		}
		catch(IOException e) { 
			System.out.println("Error: " + e); 
		}
	}//end of run method 

	public void login(ArrayList<Account> accounts, BufferedReader incoming, PrintWriter outgoing) {
		try {
			String username = incoming.readLine();
			String password = incoming.readLine();
			System.out.println("Received: " + username + ", " + password);
			Account account;
			String reply = "";
			boolean foundUser = false;
			for (int i = 0; i < accounts.size(); i++) {
				if (accounts.get(i).getUsername().equals(username)) {
					foundUser = true;
					if (accounts.get(i).verifyPassword(password)) {
						account = accounts.get(i);
						if (account instanceof AdminAccount) {
							reply = "ADMIN";
							System.out.println("Found admin account");
						}
						else {
							reply = "CLIENT";
							System.out.println("Found client account");
						}
						userAccount = account; // Set current user
					}
					else {
						reply = "ERROR: Invalid password";
					}
				}
			}
			if (!foundUser)
				reply = "ERROR: Invalid username";
			System.out.println("Sending reply...");
			outgoing.println(reply);
			outgoing.flush();  // Make sure the data is actually sent!
		}
		catch (Exception e){
			System.out.println("Error: " + e);
		}
	}//end of login method 

	public static void sendAccountList(PrintWriter outgoing) {
		for (int i = 0; i < accounts.size(); i++) {
			outgoing.println(accounts.get(i).getUsername());
			if (accounts.get(i) instanceof AdminAccount)
				outgoing.println("Administrator");
			else
				outgoing.println("Customer");
		}
		outgoing.println("DONE");
		outgoing.flush();
	}//end of sendAccountList method 

	// Send profile for client account
	public void sendProfile(PrintWriter outgoing) {
		outgoing.println(((CustomerAccount) userAccount).getProfile());
		outgoing.flush();
	}//end of sendProfile method     

	// Respond to client's request to change password
	public void changePassword(Account userAccount, BufferedReader incoming, PrintWriter outgoing) {
		try {
			String oldPassword = incoming.readLine();
			String newPassword = incoming.readLine();
			System.out.println("Received: " + oldPassword + ", " + newPassword);
			String reply = "";
			if (userAccount.verifyPassword(oldPassword)) {
				userAccount.setPassword(newPassword);
				// Save new password
				try {
					PrintWriter file = new PrintWriter("accounts.txt");
					for (int i = 0; i < accounts.size(); i++) {
						if (accounts.get(i) instanceof AdminAccount)
							file.println("admin" + "%" + accounts.get(i).getUsername() + "%" + accounts.get(i).getPassword() + "%");
						else
							file.println("client" + "%" + accounts.get(i).getUsername() + "%" + accounts.get(i).getPassword() + "%" + ((CustomerAccount) accounts.get(i)).getProfile());
					}
					file.close();
				}
				catch (IOException e) {
					System.out.println("Error writing data file.");
					System.exit(1);
				}
				// Send reply to client
				if (userAccount instanceof AdminAccount) {
					reply = "ADMIN";
					System.out.println("Found admin account");
				}
				else {
					reply = "CLIENT";
					System.out.println("Found client account");
				}
			}
			else {
				reply = "ERROR: Invalid password";
			}
			System.out.println("Sending reply...");
			outgoing.println(reply);
			outgoing.flush();  // Make sure the data is actually sent!
		}
		catch (Exception e){
			System.out.println("Error: " + e);
		}
	} //end of changePassword method 

}//end of class 
