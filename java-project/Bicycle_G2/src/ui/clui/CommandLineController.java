package ui.clui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;

import bike.BikeFactory;
import card.CardFactory;
import controller.*;
import ride.Itinerary;
import ride.Network;
import ride.path.AvoidPlusStrategy;
import ride.path.FastestPathStrategy;
import ride.path.MinimalWalkingStrategy;
import ride.path.PreferPlusStrategy;
import ride.path.UniformityStrategy;
import sorting.station.LeastOccupiedStation;
import sorting.station.MoreUsedStation;
import station.NoBikeAvailableException;
import station.NoElectricBikeAvailableException;
import station.NoMechanicBikeAvailableException;
import station.NoOngoingRideException;
import station.NoSlotAvailableException;
import station.NoSlotStateAtDateException;
import station.OngoingRideException;
import station.Station;
import station.StationOfflineException;
import station.StationSamePositionException;
import station.TypeStationException;
import tools.Date;
import tools.NegativeTimeException;
import tools.NullDateException;
import tools.Point;
import user.User;

/**
 * This class contains all the methods that can be used in the command line. Every methods take {@code String[]} arguments in order to be compatible with {@link CommandLineReader#parseArgs(String)}.
 * @author Pietro Dellino
 *
 */
public class CommandLineController {
	
	private CommandLineReader clr;
	private CommandLineDisplay cld;
	private NetworkManager nm;
	
	public CommandLineController() {
		this.clr = new CommandLineReader();
		this.cld = new CommandLineDisplay();
		this.nm = new NetworkManager();
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code setup <networkName>} <br>
	 * or <br>
	 * {@code setup <networkName> <nStations> <nSlots> <side> <nBikes>}
	 * @param args an array of {@code String}
	 * @throws ExistingNameException
	 * @throws InvalidArgumentsException
	 */
	public void setup(String[] args) throws ExistingNameException, InvalidArgumentsException {
		if (args.length == 1) {
			nm.setupNetwork(args[0]);
		} else if (args.length == 5) {
			String name = args[0];
			int nStat = Integer.parseInt(args[1]);
			int nSlot = Integer.parseInt(args[2]);
			double side = Double.parseDouble(args[3]);
			int nBikes = Integer.parseInt(args[4]);
			nm.setupNetwork(name, nStat, nSlot, side, nBikes);
		} else {
			throw new InvalidArgumentsException();
		}
		cld.display("Network \"" + args[0] + "\" setup successfully.");
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code stationOnline <networkName> <stationID>}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InexistingStationIdException
	 * @throws InvalidArgumentsException
	 */
	public void stationOnline(String[] args) throws InexistingNetworkNameException, InexistingStationIdException, InvalidArgumentsException {
		if (args.length == 2) {
			String netName = args[0];
			int stationId = Integer.parseInt(args[1]);
			nm.setStationOnline(netName, stationId);
			cld.display("Station" + stationId + " is now online.");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code stationOffline <networkName> <stationID>}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InexistingStationIdException
	 * @throws InvalidArgumentsException
	 */
	public void stationOffline(String[] args) throws InexistingNetworkNameException, InexistingStationIdException, InvalidArgumentsException {
		if (args.length == 2) {
			String netName = args[0];
			int stationId = Integer.parseInt(args[1]);
			nm.setStationOffline(netName, stationId);
			cld.display("Station" + stationId + " is now offline.");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code slotOnline <networkName> <slotID> <time>} <br>
	 * The {@code time} has to be on the following format: {@code YYYY-MM-dd HH:mm}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InexistingSlotIdException
	 * @throws NegativeTimeException
	 * @throws InvalidArgumentsException
	 */
	public void slotOnline(String[] args) throws InexistingNetworkNameException, InexistingSlotIdException, NegativeTimeException, InvalidArgumentsException {
		if (args.length == 3) {
			String netName = args[0];
			int slotId = Integer.parseInt(args[1]);
			LocalDateTime time = Date.dateInput(args[2]);
			nm.setSlotOnline(netName, slotId, time);
			cld.display("Slot" + slotId + " is now online.");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code slotOffline <networkName> <slotID> <time>} <br>
	 * The {@code time} has to be on the following format: {@code YYYY-MM-dd HH:mm}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InexistingSlotIdException
	 * @throws NegativeTimeException
	 * @throws InvalidArgumentsException
	 */
	public void slotOffline(String[] args) throws InexistingNetworkNameException, InexistingSlotIdException, NegativeTimeException, InvalidArgumentsException {
		if (args.length == 3) {
			String netName = args[0];
			int slotId = Integer.parseInt(args[1]);
			LocalDateTime time = Date.dateInput(args[2]);
			nm.setSlotOffline(netName, slotId, time);
			cld.display("Slot" + slotId + " is now offline.");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code addStation <networkName> <type> <numSlots> <positionX> <positionY>} <br>
	 * The type has to be {@code stardard} or {@code plus}. The case is ignored.
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException 
	 * @throws StationSamePositionException 
	 * @throws TypeStationException 
	 * @throws InvalidArgumentsException 
	 */
	public void addStation(String[] args) throws InexistingNetworkNameException, TypeStationException, StationSamePositionException, InvalidArgumentsException {
		if (args.length == 5) {
			String name = args[0];
			Network net = nm.findNetworkByName(name);
			int nSlots = Integer.parseInt(args[2]);
			double x = Double.parseDouble(args[3]);
			double y = Double.parseDouble(args[4]);
			if (args[1].equalsIgnoreCase("plus")) {
				nm.addPlusStation(net, nSlots, x, y);
			} else if (args[1].equalsIgnoreCase("standard")) {
				nm.addStandardStation(net, nSlots, x, y);
			} else {
				throw new InvalidArgumentsException();
			}
			cld.display("Station added successfully to network \"" + name + "\".");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code addSlot <networkName> <stationID> <numSlots>}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException 
	 * @throws InexistingStationIdException 
	 * @throws InvalidArgumentsException 
	 */
	public void addSlot(String[] args) throws InexistingNetworkNameException, InexistingStationIdException, InvalidArgumentsException {
		if (args.length == 3) {
			Network net = nm.findNetworkByName(args[0]);
			int stationID = Integer.parseInt(args[1]);
			int numSlots = Integer.parseInt(args[2]);
			nm.addSlot(net, stationID, numSlots);
			cld.display("Slot added successfully to station" + stationID + ".");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code addBike <networkName> <type> <time>} <br>
	 * or <br>
	 * {@code addBike <networkName> <stationID> <type> <time>}
	 * The {@code time} has to be on the following format: {@code YYYY-MM-dd HH:mm}
	 * The type has to be {@code electric} or {@code mechanic}. The case is ignored.
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException 
	 * @throws NegativeTimeException 
	 * @throws InvalidArgumentsException 
	 * @throws InexistingStationIdException 
	 * @throws NoSlotAvailableException 
	 */
	public void addBike(String[] args) throws InexistingNetworkNameException, NegativeTimeException, InvalidArgumentsException, InexistingStationIdException, NoSlotAvailableException {
		if (args.length == 3) {
			Network net = nm.findNetworkByName(args[0]);
			LocalDateTime time = Date.dateInput(args[2]);
			if (args[1].equalsIgnoreCase("electric")) {
				nm.addElectricBike(net, time);
			} else if (args[1].equalsIgnoreCase("mechanic")) {
				nm.addMechanicBike(net, time);
			} else {
				throw new InvalidArgumentsException();
			}
			cld.display("Bike added successfully.");
		} else if (args.length == 4) {
			Network net = nm.findNetworkByName(args[0]);
			LocalDateTime time = Date.dateInput(args[3]);
			int id = Integer.parseInt(args[1]);
			Station s = nm.findStationByID(id, net);
			if (args[2].equalsIgnoreCase("electric")) {
				nm.addElectricBike(net, s, time);
			} else if (args[2].equalsIgnoreCase("mechanic")) {
				nm.addMechanicBike(net, s, time);
			} else {
				throw new InvalidArgumentsException();
			}
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code addUser <username> <cardType> <networkName>} <br>
	 * The type has to be {@code vlibre}, {@code vmax} or {@code credit}. The case is ignored.
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException 
	 * @throws InvalidArgumentsException 
	 */
	public void addUser(String[] args) throws InexistingNetworkNameException, InvalidArgumentsException {
		if (args.length == 3) {
			if (args[1].equalsIgnoreCase("vlibre")) {
				nm.addUser(args[0], CardFactory.VLIBRE, args[2]);
			} else if (args[1].equalsIgnoreCase("vmax")) {
				nm.addUser(args[0], CardFactory.VMAX, args[2]);
			} else if (args[1].equalsIgnoreCase("credit")) {
				nm.addUser(args[0], CardFactory.CREDIT, args[2]);
			} else {
				throw new InvalidArgumentsException();
			}
			cld.display("User added successfully.");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code returnBike <userID> <stationID> <time> <networkName>} <br>
	 * The {@code time} has to be on the following format: {@code YYYY-MM-dd HH:mm}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException 
	 * @throws NullDateException 
	 * @throws NegativeTimeException 
	 * @throws InexistingStationIdException 
	 * @throws InexistingUserIdException 
	 * @throws InvalidArgumentsException 
	 * @throws StationOfflineException 
	 * @throws NoOngoingRideException 
	 * @throws NoSlotAvailableException 
	 * @throws OngoingRideException 
	 */
	public void returnBike(String[] args) throws InexistingNetworkNameException, InexistingUserIdException, InexistingStationIdException, NegativeTimeException, NullDateException, InvalidArgumentsException, NoSlotAvailableException, NoOngoingRideException, StationOfflineException, OngoingRideException {
		if (args.length == 4) {
			int userID = Integer.parseInt(args[0]);
			int stationID = Integer.parseInt(args[1]);
			LocalDateTime time = Date.dateInput(args[2]);
			Network net = nm.findNetworkByName(args[3]);
			double price = nm.returnBike(userID, stationID, time, net);
			cld.display(String.format("Bike successfully returned at station" + stationID + " by user" + userID + ". The price of the ride is %.2f euros.", price));
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code rentBike <userID> <stationID> <bikeType> <time> <networkName>} <br>
	 * The {@code time} has to be on the following format: {@code YYYY-MM-dd HH:mm}
	 * The type has to be {@code electric} or {@code mechanic}. The case is ignored. The type is optional, if it is not written, any bike will be rented.
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException 
	 * @throws NegativeTimeException 
	 * @throws InexistingStationIdException 
	 * @throws InexistingUserIdException 
	 * @throws InvalidArgumentsException 
	 * @throws StationOfflineException 
	 * @throws NoBikeAvailableException 
	 * @throws OngoingRideException 
	 * @throws NoMechanicBikeAvailableException 
	 * @throws NoElectricBikeAvailableException 
	 */
	public void rentBike(String[] args) throws InexistingNetworkNameException, InexistingUserIdException, InexistingStationIdException, NegativeTimeException, InvalidArgumentsException, OngoingRideException, NoBikeAvailableException, StationOfflineException, NoElectricBikeAvailableException, NoMechanicBikeAvailableException {
		if (args.length == 4) {
			int userID = Integer.parseInt(args[0]);
			int stationID = Integer.parseInt(args[1]);
			LocalDateTime time = Date.dateInput(args[2]);
			Network net = nm.findNetworkByName(args[3]);
			nm.rentBike(userID, stationID, time, net);
			cld.display("Bike successfully rented at station" + stationID + " by user" + userID + ".");
		} else if (args.length == 5) {
			int userID = Integer.parseInt(args[0]);
			int stationID = Integer.parseInt(args[1]);
			int bikeType;
			if (args[2].equalsIgnoreCase("mechanic")) {
				bikeType = BikeFactory.MECHANIC;
			} else if (args[2].equalsIgnoreCase("electric")) {
				bikeType = BikeFactory.ELECTRIC;
			} else {
				throw new InvalidArgumentsException();
			}
			LocalDateTime time = Date.dateInput(args[3]);
			Network net = nm.findNetworkByName(args[4]);
			nm.rentBike(userID, stationID, bikeType, time, net);
			cld.display("Bike successfully rented at station" + stationID + " by user" + userID + ".");
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code displayStation <networkName> <stationID>}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InexistingStationIdException
	 * @throws InvalidArgumentsException
	 */
	public void displayStation(String[] args) throws InexistingNetworkNameException, InexistingStationIdException, InvalidArgumentsException {
		if (args.length == 2) {
			Network net = nm.findNetworkByName(args[0]);
			int id = Integer.parseInt(args[1]);
			Station s = nm.findStationByID(id, net);
			cld.display(s);
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code displayUser <networkName> <userID>}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InexistingUserIdException
	 * @throws InvalidArgumentsException
	 */
	public void displayUser(String[] args) throws InexistingNetworkNameException, InexistingUserIdException, InvalidArgumentsException {
		if (args.length == 2) {
			Network net = nm.findNetworkByName(args[0]);
			int id = Integer.parseInt(args[1]);
			User u = nm.findUserById(id, net);
			cld.display(u);
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code display <networkName>}
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InvalidArgumentsException
	 */
	public void display(String[] args) throws InexistingNetworkNameException, InvalidArgumentsException {
		if (args.length == 1) {
			Network net = nm.findNetworkByName(args[0]);
			cld.display(net);
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code sortStation <networkName> <sortingStrategy> <startTime> <endTime>} <br>
	 * {@code startTime} and {@code endTime} have to be on the following format: {@code YYYY-MM-dd HH:mm}
	 * The sorting strategy can be {@code more-used} or {@code least-occupied}.
	 * @param args an array of {@code String}
	 * @throws InexistingNetworkNameException
	 * @throws InvalidArgumentsException
	 * @throws NullDateException 
	 * @throws NegativeTimeException 
	 * @throws NoSlotStateAtDateException 
	 */
	public void sortStation(String[] args) throws InexistingNetworkNameException, InvalidArgumentsException, NoSlotStateAtDateException, NegativeTimeException, NullDateException {
		if (args.length == 4) {
			Network net = nm.findNetworkByName(args[0]);
			LocalDateTime startTime = Date.dateInput(args[2]);
			LocalDateTime endTime = Date.dateInput(args[3]);
			if (args[1].equalsIgnoreCase("least-occupied")) {
				cld.displaySortedStations(net, new LeastOccupiedStation(startTime, endTime), startTime, endTime);
			} else if (args[1].equalsIgnoreCase("more-used")) {
				cld.displaySortedStations(net, new MoreUsedStation(), startTime, endTime);
			} else {
				throw new InvalidArgumentsException();
			}

		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code calculateItinerary <networkName> <userID> <startX> <startY> <destinationX> <destinationY> <pathStrategy>} <br>
	 * The {@code pathStrategy} can be {@code minimal-walking}, {@code fastest-path}, {@code prefer-plus}, {@code avoid-plus}, or {@code uniformity}.
	 * @throws InexistingUserIdException
	 * @throws InvalidArgumentsException
	 * @throws InexistingNetworkNameException 
	 */
	public void calculateItinerary(String[] args) throws InexistingUserIdException, InvalidArgumentsException, InexistingNetworkNameException {
		if (args.length == 7) {
			Network net = nm.findNetworkByName(args[0]);
			int id = Integer.parseInt(args[1]);
			double startX = Double.parseDouble(args[2]);
			double startY = Double.parseDouble(args[3]);
			double destX = Double.parseDouble(args[4]);
			double destY = Double.parseDouble(args[5]);
			Point start = new Point(startX, startY);
			Point destination = new Point(destX, destY);
			User u = nm.findUserById(id, net);
			Itinerary it;
			if (args[6].equalsIgnoreCase("minimal-walking")) {
				it = u.calculateItinerary(start, destination, new MinimalWalkingStrategy(net));
			} else if (args[6].equalsIgnoreCase("fastest-path")) {
				it = u.calculateItinerary(start, destination, new FastestPathStrategy(net));
			} else if (args[6].equalsIgnoreCase("prefer-plus")) {
				it = u.calculateItinerary(start, destination, new PreferPlusStrategy(net));
			} else if (args[6].equalsIgnoreCase("avoid-plus")) {
				it = u.calculateItinerary(start, destination, new AvoidPlusStrategy(net));
			} else if (args[6].equalsIgnoreCase("uniformity")) {
				it = u.calculateItinerary(start, destination, new UniformityStrategy(net));
			} else {
				throw new InvalidArgumentsException();
			}
			cld.display("Pickup station:");
			cld.display(it.getStartStation());
			cld.display("Return station:");
			cld.display(it.getEndStation());
			String s = clr.readCommand("Do you want to follow this itinerary? [y/n]");
			if (s.trim().equalsIgnoreCase("y")) {
				u.setItinerary(it);
			}
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code displayItinerary <networkName> <userID>} <br>
	 * @throws InexistingNetworkNameException 
	 * @throws InexistingUserIdException 
	 * @throws InvalidArgumentsException 
	 */
	public void displayItinerary(String[] args) throws InexistingNetworkNameException, InexistingUserIdException, InvalidArgumentsException {
		if (args.length == 2) {
			Network net = nm.findNetworkByName(args[0]);
			int userId = Integer.parseInt(args[1]);
			User u = nm.findUserById(userId, net);
			cld.display(u.getItinerary());
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method shows all the possible commands to the user.
	 */
	public void help() {
		String disp = "Welcome to the help center of the myVelib system\nHere are the instructions you can write:\n";
		for (Command c : Command.values()) {
			disp += c.getFormat() + '\n';
		}
		disp += "Please notice that the time format has to be \"YYYY-MM-dd HH:mm\"\n";
		cld.display(disp);
	}
	
	/**
	 * This method applies command line instructions of the form: <br>
	 * {@code runtest <filename>} <br>
	 * The files have to be in the {@code testfiles} folder of the project.
	 * @param args
	 * @throws InvalidArgumentsException 
	 */
	public void runtest(String[] args) throws InvalidArgumentsException {
		if (args.length == 1) {
			String filename = args[0];
			String path = System.getProperty("user.dir") + "\\eval\\";
			String writeFileName = path + filename.substring(0, filename.lastIndexOf('.')) + "Result.txt";
			FileReader file = null;
			BufferedReader reader = null;
			PrintStream oldStream = System.out;
			PrintStream writerStream = null;
			try {
				this.runSetupTestFile();
				writerStream = new PrintStream(writeFileName);
				System.setOut(writerStream);
				file = new FileReader(path + filename);
				reader = new BufferedReader(file);
				String line;
				line = reader.readLine();
				while (line != null) {
					try {
						clr.interpreteCommand(line, this);
					} catch (InvalidCommandException | ExistingNameException | InvalidArgumentsException
							| InexistingNetworkNameException | InexistingStationIdException | InexistingSlotIdException
							| NegativeTimeException | TypeStationException | StationSamePositionException
							| InexistingUserIdException | NullDateException | NoSlotAvailableException
							| NoOngoingRideException | StationOfflineException | OngoingRideException
							| NoBikeAvailableException e) {
						cld.display(e.getMessage());
						line = reader.readLine();
						continue;
					} catch (Exception e) {
						cld.display("An unknown error has occured.");
						e.printStackTrace();
						line = reader.readLine();
						continue;
					}
					line = reader.readLine();
				}
				this.nm.resetNetworks();
				cld.display("Test completed");
				System.setOut(oldStream);
				cld.display("Test result written in file " + writeFileName + '\n' + "Networks reset.");
			} catch (FileNotFoundException e) {
				cld.display("Test File not found");
			} catch (IOException e) {
			} finally {
				if (file != null) {
					try { file.close(); } catch (IOException e) {}
				}
				if (reader != null) {
					try { reader.close(); } catch (IOException e) {}
				}
				if (writerStream != null) {
					writerStream.close(); 
				}
			}
		} else {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method loads a setup file
	 */
	private void runSetupFile(String filename) {
		nm.resetNetworks();
		String path = System.getProperty("user.dir");
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader(path + "\\eval\\" + filename);
			reader = new BufferedReader(file);
			String line;
			line = reader.readLine();
			while (line != null) {
				clr.interpreteCommand(line, this);
				line = reader.readLine();
			}
		} catch (Exception e) {
			cld.display("Error: could not setup correctly.");
		} finally {
			if (file != null) {
				try { file.close(); } catch (IOException e) {}
			}
			if (reader != null) {
				try { reader.close(); } catch (IOException e) {}
			}
		}
		cld.display("Setup file loaded successfully.");
	}
	
	/**
	 * This method runs the setup file used for the CLUI
	 */
	public void runSetupFile() {
		this.runSetupFile("my_Velib.ini");
	}
	
	/**
	 * This method runs the setup file for the tests.
	 */
	public void runSetupTestFile() {
		this.runSetupFile("my_Velib_test.ini");
	}

}
