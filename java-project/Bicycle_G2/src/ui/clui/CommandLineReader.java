package ui.clui;

import java.util.Scanner;

import controller.ExistingNameException;
import controller.InexistingNetworkNameException;
import controller.InexistingSlotIdException;
import controller.InexistingStationIdException;
import controller.InexistingUserIdException;
import station.NoBikeAvailableException;
import station.NoElectricBikeAvailableException;
import station.NoMechanicBikeAvailableException;
import station.NoOngoingRideException;
import station.NoSlotAvailableException;
import station.NoSlotStateAtDateException;
import station.OngoingRideException;
import station.StationOfflineException;
import station.StationSamePositionException;
import station.TypeStationException;
import tools.NegativeTimeException;
import tools.NullDateException;

/**
 * This method contains methods to parse command and arguments from a command line instruction. It also contains the only main method of the project, hence it is the one running the command line interface.
 * @author Pietro Dellino
 *
 */
public class CommandLineReader {
	
	private Scanner sc;
	
	public CommandLineReader() {
		super();
		sc = new Scanner(System.in);
	}
	
	/**
	 * This method asks the user a keyboard input.
	 */
	public String readCommand() {
		String command = sc.nextLine();
		return command;
	}
	
	/**
	 * This method prints a {@code String} on the console and asks the user a keyboard input.
	 * @param s
	 */
	public String readCommand(String s) {
		System.out.println(s);
		String command = sc.nextLine();
		return command;
	}
	
	/**
	 * This method parses the command of a {@code String} instruction.
	 * @param s
	 * @return a {@link Command}
	 * @throws InvalidCommandException
	 */
	public Command parseCommand(String s) throws InvalidCommandException {
		String com = s.split(" ")[0];
		for (Command c : Command.values()) {
			if (com.equals(c.getKeyword())) {
				return c;
			}
		}
		throw new InvalidCommandException(com);
	}
	
	/**
	 * This method parses the arguments of a {@code String} instruction.
	 * @param s
	 * @return an array of {@code String}
	 * @throws InvalidArgumentsException
	 */
	public String[] parseArgs(String s) throws InvalidArgumentsException {
		try {
			String s1 = s.substring(s.indexOf('<')+1, s.lastIndexOf('>'));
			return s1.split(">{1} *<{1}");
		} catch (StringIndexOutOfBoundsException e) {
			throw new InvalidArgumentsException();
		}
	}
	
	/**
	 * This method closes the scanner of the {@code CommandLineReader}.
	 */
	public void close() {
		sc.close();
	}
	
	/**
	 * This method calls the method of the {@code CommandLineController} corresponding to the instruction.
	 * @param instruction
	 * @param clc
	 * @throws InvalidCommandException
	 * @throws ExistingNameException
	 * @throws InvalidArgumentsException
	 * @throws InexistingNetworkNameException
	 * @throws InexistingStationIdException
	 * @throws InexistingSlotIdException
	 * @throws NegativeTimeException
	 * @throws TypeStationException
	 * @throws StationSamePositionException
	 * @throws InexistingUserIdException
	 * @throws NullDateException
	 * @throws NoSlotAvailableException
	 * @throws NoOngoingRideException
	 * @throws StationOfflineException
	 * @throws OngoingRideException
	 * @throws NoBikeAvailableException
	 * @throws NoMechanicBikeAvailableException 
	 * @throws NoElectricBikeAvailableException 
	 * @throws NoSlotStateAtDateException 
	 */
	public void interpreteCommand(String instruction, CommandLineController clc) throws InvalidCommandException, ExistingNameException, InvalidArgumentsException, InexistingNetworkNameException, InexistingStationIdException, InexistingSlotIdException, NegativeTimeException, TypeStationException, StationSamePositionException, InexistingUserIdException, NullDateException, NoSlotAvailableException, NoOngoingRideException, StationOfflineException, OngoingRideException, NoBikeAvailableException, NoElectricBikeAvailableException, NoMechanicBikeAvailableException, NoSlotStateAtDateException {
		Command com = this.parseCommand(instruction);
		String[] args = this.parseArgs(instruction);
		switch (com) {
		case SETUP:
			clc.setup(args);
			break;
		case RUNTEST:
			clc.runtest(args);
			break;
		case STATION_ONLINE:
			clc.stationOnline(args);
			break;
		case STATION_OFFLINE:
			clc.stationOffline(args);
			break;
		case SLOT_ONLINE:
			clc.slotOnline(args);
			break;
		case SLOT_OFFLINE:
			clc.slotOffline(args);
			break;
		case ADD_STATION:
			clc.addStation(args);
			break;
		case ADD_SLOT:
			clc.addSlot(args);
			break;
		case ADD_USER:
			clc.addUser(args);
			break;
		case ADD_BIKE:
			clc.addBike(args);
			break;
		case RETURN_BIKE:
			clc.returnBike(args);
			break;
		case RENT_BIKE:
			clc.rentBike(args);
			break;
		case DISPLAY_USER:
			clc.displayUser(args);
			break;
		case DISPLAY_STATION:
			clc.displayStation(args);
			break;
		case DISPLAY:
			clc.display(args);
			break;
		case SORT_STATION:
			clc.sortStation(args);
			break;
		case CALCULATE_ITINERARY:
			clc.calculateItinerary(args);
			break;
		case DISPLAY_ITINERARY:
			clc.displayItinerary(args);
		}
	}
	
	public static void main(String[] args) {
		CommandLineController clc = new CommandLineController();
		CommandLineDisplay cld = new CommandLineDisplay();
		CommandLineReader clr = new CommandLineReader();
		clc.runSetupFile();
		cld.display("Welcome to the myVelib system. You can type \"help\" to see the possible commands and \"exit\" to stop the system");
		String instruction = clr.readCommand("Please write your instruction:");
		while (!instruction.equalsIgnoreCase("exit")) {
			if (instruction.equalsIgnoreCase("help")) {
				clc.help();
			} else {
				try {
					clr.interpreteCommand(instruction, clc);
				} catch (InvalidCommandException | ExistingNameException | InvalidArgumentsException
						| InexistingNetworkNameException | InexistingStationIdException | InexistingSlotIdException
						| NegativeTimeException | TypeStationException | StationSamePositionException
						| InexistingUserIdException | NullDateException | NoSlotAvailableException | NoOngoingRideException
						| StationOfflineException | OngoingRideException | NoBikeAvailableException
						| NoElectricBikeAvailableException | NoMechanicBikeAvailableException e) {
					cld.display(e.getMessage());
					instruction = clr.readCommand("Please write your command:");
					continue;
				} catch (Exception e) {
					cld.display("An unknown error has occured.");
					e.printStackTrace();
					instruction = clr.readCommand("Please write your command:");
					continue;
				}
			}
			instruction = clr.readCommand("Please write your instruction:");
		}
		cld.display("It has been a pleasure to work for you.");
		clr.close();
	}

}
