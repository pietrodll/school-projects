package ui.clui;

/**
 * This {@code enum} contains all the possible commands of the CLUI, with their keyword and the format of the instruction (that is showed with the method {@link CommandLineController#help()}.
 * @author Pietro Dellino
 */
public enum Command {
	
	SETUP("setup", "setup <networkName> or setup <networkName> <nStations> <nSlots> <side> <nBikes>"),
	RUNTEST("runtest", "runtest <fileName>"),
	STATION_ONLINE("stationOnline", "stationOnline <networkName> <stationID>"),
	STATION_OFFLINE("stationOffline", "stationOffline <networkName> <stationID>"),
	SLOT_ONLINE("slotOnline", "slotOnline <networkName> <slotID> <time>"),
	SLOT_OFFLINE("slotOffline", "slotOffline <networkName> <slotID> <time>"),
	ADD_STATION("addStation", "addStation <networkName> <type> <numSlots> <positionX> <positionY> (the station type can be either \"plus\" or \"standard\")"),
	ADD_SLOT("addSlot", "addSlot <networkName> <stationID> <numSlots>"),
	ADD_BIKE("addBike", "addBike <networkName> <type> <time> or addBike <networkName> <stationID> <type> <time> (the bike type can be either \"electric\" or \"mechanic\")"),
	ADD_USER("addUser", "addUser <username> <cardType> <networkName> (the card type can be \"vmax\", \"vlibre\" or \"credit\")"),
	RETURN_BIKE("returnBike", "returnBike <userID> <stationID> <time> <networkName>"),
	RENT_BIKE("rentBike", "rentBike <userID> <stationID> <bikeType> <time> <networkName>"),
	DISPLAY_USER("displayUser", "displayUser <networkName> <userID>"),
	DISPLAY_STATION("displayStation", "displayStation <networkName> <stationID>"),
	DISPLAY("display", "display <networkName>"),
	SORT_STATION("sortStation", "sortStation <networkName> <sortingStrategy> <startTime> <endTime> (the sorting strategy can be \"more-used\" or \"least-occupied\")"),
	CALCULATE_ITINERARY("calculateItinerary", "calculateItinerary <networkName> <userID> <startX> <startY> <destinationX> <destinationY> <pathStrategy>"),
	DISPLAY_ITINERARY("displayItinerary", "displayItinerary <userID> <networkName>");
	
	
	private String keyword;
	private String commandFormat;
	
	Command(String s, String t) {
		this.keyword = s;
		this.commandFormat = t;
	}
	
	public String getKeyword() { return this.keyword; }
	public String getFormat() { return this.commandFormat; }

}
