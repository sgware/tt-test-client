package edu.uky.cs.nil.tt;

/**
 * The main entry point for the Tandem Tales Test Client. This class reads the
 * user's preferences from the command line arguments passed to the JVM starts
 * a test client with those settings.
 * 
 * @author Stephen G. Ware
 */
public class TestClientMain {
	
	/** The name of this project */
	public static final String NAME = "Tandem Tales Test Client";
	
	/** The current version of this project */
	public static final String VERSION = "0.9.0";
	
	/** The people who contributed significantly to this project */
	public static final String AUTHORS = "Stephen G. Ware";
	
	/** A full title, including name, version number, and authors */
	public static final String TITLE = NAME + " v" + VERSION + " by " + AUTHORS;
	
	/** The key used to print the usage instructions */
	public static final String HELP_KEY = "help";
	
	/** The key used to specify this client's name */
	public static final String NAME_KEY = "name";
	
	/** The default name used by this client if one is not specified */
	public static final String DEFAULT_NAME = "test";
	
	/**
	 * The key used to specify the name of the story world this client wants to
	 * play in
	 */
	public static final String WORLD_KEY = "world";
	
	/** The key used to specify that this client wants the game master role */
	public static final String GM_KEY = "gm";
	
	/** The key used to specify that this client wants the player role */
	public static final String PLAYER_KEY = "player";
	
	/** 
	 * The key used to specify the name of the partner this client wants to
	 * play with
	 */
	public static final String PARTNER_KEY = "partner";
	
	/** The key used to specify the URL of the server to connect to */
	public static final String URL_KEY = "url";
	
	/** The default server URL to used if one is not specified */
	public static final String DEFAULT_URL = "localhost";
	
	/** The key used to specify the network port to connect to */
	public static final String PORT_KEY = "port";
	
	/** The default network port to be used if one is not specified */
	public static final int DEFAULT_PORT = Client.DEFAULT_PORT;
	
	/** Usage instructions for the test client executable */
	public static final String USAGE = 
		rpad("-" + HELP_KEY) + "Print usage information.\n" +
		rpad("-" + NAME_KEY + " <string>") + "Sets the name of this client (defaults to \"" + DEFAULT_NAME + "\").\n" +
		rpad("-" + WORLD_KEY + " <string>") + "Specifies which world to play in.\n" +
		rpad("-" + GM_KEY) + "Play as the game master (if not set, will play as either role).\n" +
		rpad("-" + PLAYER_KEY) + "Play as the player (if not set, will play as either role).\n" +
		rpad("-" + PARTNER_KEY + " <string>") + "Specifies the name of a partner to play with.\n" +
		rpad("-" + URL_KEY + " <string>") + "Specifies the URL of the server (defaults to \"" + DEFAULT_URL + "\").\n" +
		rpad("-" + PORT_KEY + " <number>") + "Specifies the network port of the server (defaults to " + DEFAULT_PORT + ").";
	
	private static final String rpad(String string) {
		return String.format("%-19s", string);
	}
	
	/**
	 * Configure and run a test client based on Java arguments passed from the
	 * terminal.
	 * 
	 * @param args the arguments passed to this executable
	 * @throws Exception if a problem occurs during the client setup or while
	 * it is running
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(TITLE);
		// Help
		Arguments arguments = new Arguments(args);
		if(arguments.contains(HELP_KEY)) {
			System.out.println(USAGE);
			return;
		}
		// Name
		String name = arguments.getValue(NAME_KEY, DEFAULT_NAME);
		if(name == null)
			name = DEFAULT_NAME;
		// World
		String world = arguments.getValue(WORLD_KEY, null);
		// Role
		Role role;
		if(arguments.contains(GM_KEY) && arguments.contains(PLAYER_KEY))
			throw new IllegalArgumentException("You cannot play as both the game master and player.");
		else if(arguments.contains(GM_KEY))
			role = Role.GAME_MASTER;
		else if(arguments.contains(PLAYER_KEY))
			role = Role.PLAYER;
		else
			role = null;
		// Partner
		String partner = arguments.getValue(PARTNER_KEY, null);
		// Network Settings
		String url = arguments.getValue(URL_KEY, DEFAULT_URL);
		if(url == null)
			url = DEFAULT_URL;
		int port;
		if(arguments.contains(PORT_KEY))
			port = Integer.parseInt(arguments.getValue(PORT_KEY, Integer.toString(DEFAULT_PORT)));
		else
			port = DEFAULT_PORT;
		// Create Client
		try(TestClient client = new TestClient(name, world, role, partner, url, port);) {
			client.call();
		}
	}
	
	private TestClientMain() {}
}