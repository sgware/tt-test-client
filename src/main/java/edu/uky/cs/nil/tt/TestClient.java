package edu.uky.cs.nil.tt;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import edu.uky.cs.nil.tt.io.Connect;
import edu.uky.cs.nil.tt.world.Ending;
import edu.uky.cs.nil.tt.world.State;
import edu.uky.cs.nil.tt.world.Status;
import edu.uky.cs.nil.tt.world.Turn;
import edu.uky.cs.nil.tt.world.World;

/**
 * A {@link Client client} which writes state updates and potential turns to
 * {@link System#out standard output} and solicits the user's choice via {@link
 * System#in standard input}.
 * 
 * @author Stephen G. Ware
 */
public class TestClient extends Client {
	
	/** Used to read choices from standard input */
	private final Scanner input;
	
	/** The last update string printed */
	private String previous = null;
	
	/**
	 * Creates a new test client with the given session preferences and network
	 * configuration.
	 * 
	 * @param name the name the client will use
	 * @param world the name of the world the client wants their session to take
	 * place in, or null if the client has no preference for a story world
	 * @param role the role the client wants to play in the session, or null if
	 * the client is willing to play either role
	 * @param partner the name of the client's desired partner, or null if the
	 * client is willing to play with any partner
	 * @param url the URL of the server to which this client will connect
	 * @param port the network port on which this client will connect
	 */
	public TestClient(String name, String world, Role role, String partner, String url, int port) {
		super(name, world, role, partner, url, port);
		this.input = new Scanner(System.in);
	}
	
	@Override
	protected void onConnect(Connect connect) throws IOException {
		System.out.println("The client has connected and is waiting for a partner.");
	}
	
	@Override
	protected void onStart(World world, Role role, State state) {
		System.out.println("The session has started.\n\n" + state.getDescription() + "\n");
		if(role == Role.PLAYER)
			System.out.println("You are the player. Please wait while the game master goes first.\n");
		else
			System.out.println("You are the game master. You go first.\n");
	}
	
	@Override
	protected void onUpdate(Status status) {
		Turn last = getLastTurn(status);
		String update = null;
		if(last == null || last.type == Turn.Type.SUCCEED)
			update = status.getState().getDescription();
		else if(last.type == Turn.Type.FAIL)
			update = last.getDescription();
		if(update != null && !update.equals(previous)) {
			System.out.println(update + "\n");
			previous = update;
		}
	}
	
	@Override
	protected int onChoice(Status status) {
		// Print the current choices.
		String prompt = "";
		Turn last = getLastTurn(status);
		if(last != null && last.type == Turn.Type.PROPOSE)
			prompt += "Your partner proposed: " + last.getDescription() + "\nHow do you respond?\n";
		else
			prompt += "It is your turn. What will you do?\n";
		for(int i = 0; i < getChoices().size(); i++)
			prompt += "[" + (i + 1) + "] " + getChoices().get(i).getDescription() + "\n";
		System.out.println(prompt);
		// Solicit a choice from the client.
		int index = getChoice();
		Turn choice = getChoices().get(index);
		if(choice.type == Turn.Type.PROPOSE)
			System.out.println("You propose: " + choice.getDescription() + " Please wait for your partner to respond.");
		else if(choice.type == Turn.Type.PASS)
			System.out.println("It is now your partner's turn. Please wait for them to act.");
		System.out.println();
		previous = null;
		return index;
	}
	
	private int getChoice() {
		int choice = -1;
		while(choice < 0) {
			System.out.print("Type the number of your choice: ");
			String in = input.nextLine();
			try {
				choice = Integer.parseInt(in);
			}
			catch(NumberFormatException exception) {
				// Do nothing.
			}
			if(choice < 1 || choice > getChoices().size()) {
				System.out.println("You must type a number between 1 and " + getChoices().size() + ".");
				choice = -1;
			}
		}
		return choice - 1;
	}
	
	@Override
	protected void onEnd(Ending ending) {
		System.out.println(ending.getDescription() + "\n\nThe story has ended. Thank you for playing.");
	}
	
	@Override
	protected void onStop(String message) {
		System.out.println("The session has stopped" + (message == null ? "." : " because \"" + message + "\""));
	}
	
	@Override
	protected void onDisconnect() {
		System.out.println("The client has disconnected.");
	}
	
	private final Turn getLastTurn(Status status) {
		List<Turn> history = status.getHistory();
		if(history.size() == 0)
			return null;
		else
			return history.get(history.size() - 1);
	}
}