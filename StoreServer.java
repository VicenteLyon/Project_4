	//John R. & Vicente L.
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

public class StoreServer {

	private static int LISTENING_PORT = 32007;
	public static ArrayList<Account> accounts = new ArrayList<Account>(); // NOTE: Changed to "public" as an easy, last-minute solution to testing sendAccountList()
	public static Account userAccount; // NOTE: Changed to "public" as an easy, last-minute solution to testing sendProfile()
    private static BufferedReader incoming;
    private static PrintWriter outgoing;   // Stream for sending data.

    public static void main(String[] args) {
        if (args.length == 1) // Allow for different port number on command line
        	LISTENING_PORT = Integer.parseInt(args[0]);
        else if (args.length > 1 )
            System.out.println("Usage:  java StoreServer <listening-port>");
        // Read account info from file
     //  readAccounts(accounts); 
        // Create listener socket and begin listening
        ServerSocket listener;  // Listens for incoming connections.
        Socket client;      // For communication with the connecting program.
        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            // Client loop
            while (true) {
            	// Accept next connection request and handle it.
            	client = listener.accept(); 
              //  System.out.println("Connection from " + client.getInetAddress().toString() );
                StoreThread clientThread = new StoreThread(client); 
                clientThread.start();
                /*
                incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
                outgoing = new PrintWriter( client.getOutputStream() );
                System.out.println("Waiting for request...");
                String request = incoming.readLine();
                System.out.println("Request: " + request);
               */ 
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }

    }  // end main()
    /*
    // Read from data file to load account information
    public static void readAccounts(ArrayList<Account> accounts) {
        File dataFile = new File("accounts.txt");
        if ( ! dataFile.exists() ) {
            System.out.println("No data file found.");
            System.exit(1);
        }
        try( Scanner scanner = new Scanner(dataFile) ) {
            while (scanner.hasNextLine()) {
                String accountEntry = scanner.nextLine();
                int separatorPosition = accountEntry.indexOf('%');
                int separatorPosition2 = accountEntry.indexOf('%', separatorPosition + 1);
                int separatorPosition3 = accountEntry.indexOf('%', separatorPosition2 + 1);
                if (separatorPosition == -1)
                    throw new IOException("File is not a valid data file.");
                String accountType = accountEntry.substring(0, separatorPosition).trim();
                String username = accountEntry.substring(separatorPosition + 1, separatorPosition2).trim();
                String password = accountEntry.substring(separatorPosition2 + 1, separatorPosition3).trim();
                if (accountType.equals("admin")) { 
                	//System.out.println(username + "\n" + password);
                	accounts.add(new AdminAccount(username, password, accounts));
                }
                else {
                    String profile = accountEntry.substring(separatorPosition3 + 1);
                	accounts.add(new CustomerAccount(username, password, profile));
                	//System.out.println(username +  "\n" + password); 
            	}
            }
            for(int i =0; i < accounts.size(); i++) { 
            	System.out.println(accounts.get(i).getUsername());
            }
         }
        catch (IOException e) {
            System.out.println("Error in data file.");
            System.exit(1);
        }
    }//end of readAccounts method 
   */
    
    /*
    
    // Send account info to client, including usernames and types 
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
    }

    // Send profile for client account
    public static void sendProfile(PrintWriter outgoing) {
    	outgoing.println(((CustomerAccount) userAccount).getProfile());
    	outgoing.flush();
    }
    
   

    // Evaluate client's login request
    public static void login(ArrayList<Account> accounts, BufferedReader incoming, PrintWriter outgoing) {
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
    }
    
    // Respond to client's request to change password
    public static void changePassword(Account userAccount, BufferedReader incoming, PrintWriter outgoing) {
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
    }
    */
    
} //end class
