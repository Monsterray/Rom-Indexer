package indexers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;

/**
 * @author Monsterray
 *
 */
public class PSXIndexer {
	
	public String fileNameIn;	//The input where roms should be looked for
	public File folder;			//The File folder of the input location
	public File statText;		//The file where all the stats are going to be placed
	public FileOutputStream is;	
	public OutputStreamWriter osw;
	public Writer w;
	public static long startTime;	//Start time for how long the program is running
	public static long endTime;		//End time for program run time
	public String fileTitle = "PSX Titles";	//Title of the file that will be created
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Initializing...");
		PSXIndexer pullMovieNames = new PSXIndexer();

		try {
			pullMovieNames.run();
		} catch (IOException e) {e.printStackTrace();}
		
		endTime = System.currentTimeMillis();
		long totalTime = (endTime - startTime) / 1000;
		System.out.println("Finished in " + totalTime + "seconds!");
	}

	/**
	 * @param location String
	 */
	@SuppressWarnings("unused")
	public void outputInfo(String location) {
		File currentFolder = new File(location);
		File[] allFiles = currentFolder.listFiles();

		for (int i = 0; i < allFiles.length; i++) {
			String fileExtention = allFiles[i].getName().substring(allFiles[i].getName().length() - 3, allFiles[i].getName().length());
			if (allFiles[i].isFile() && !fileExtention.equalsIgnoreCase(".db") && !fileExtention.equalsIgnoreCase(".")) {
				try {
					String fullFileName = allFiles[i].getName().substring(0, allFiles[i].getName().length() - 4);
					String title = "";
					String countryCode = "";
					String diskNumber = "";
					String ID = "";
					List<Integer> breakLocationsStart = findIndexOfMore(fullFileName, "[");
					List<Integer> breakLocationsEnd = findIndexOfMore(fullFileName, "]");
					int lengthStart = breakLocationsStart.size();
					
					if(lengthStart == 1){
						title = fullFileName.substring(0, breakLocationsStart.get(0) - 1);
						ID = fullFileName.substring(breakLocationsStart.get(0), breakLocationsEnd.get(0) + 1);
					}else if(lengthStart == 2){
						title = fullFileName.substring(0, breakLocationsStart.get(0) - 1);
						countryCode = fullFileName.substring(breakLocationsStart.get(0), breakLocationsEnd.get(0) + 1);
						ID = fullFileName.substring(breakLocationsStart.get(1), breakLocationsEnd.get(1) + 1);
					}else if(lengthStart == 3){
						title = fullFileName.substring(0, breakLocationsStart.get(0) - 1);
						countryCode = fullFileName.substring(breakLocationsStart.get(0), breakLocationsEnd.get(0) + 1);
						diskNumber = fullFileName.substring(breakLocationsStart.get(1), breakLocationsEnd.get(1) + 1);
						ID = fullFileName.substring(breakLocationsStart.get(2), breakLocationsEnd.get(2) + 1);
					}else{
						title = fullFileName;
					}
					System.out.println(fullFileName);
					System.out.println("[DEBUG] This is lengthStart: " + lengthStart);
					System.out.println("[DEBUG] This is title: " + title);
					System.out.println("[DEBUG] This is countryCode: " + countryCode);
					System.out.println("[DEBUG] This is diskNumber: " + diskNumber);
					System.out.println("[DEBUG] This is ID: " + ID);
//					
					int indexOfCountryStart = fullFileName.indexOf("[");
					int indexOfCountryEnd = fullFileName.indexOf("]");
					int indexOfIDStart = fullFileName.indexOf("[", indexOfCountryStart + 1);
					int indexOfIDEnd = fullFileName.indexOf("]", indexOfCountryEnd + 1);
					int indexOfStart = fullFileName.indexOf("[", indexOfIDStart + 1);
					int indexOfEnd = fullFileName.indexOf("]", indexOfIDEnd + 1);

					System.out.println("[DEBUG] This is indexOfCountryStart: " + indexOfCountryStart);
					System.out.println("[DEBUG] This is indexOfCountryEnd: " + indexOfCountryEnd);
					System.out.println("[DEBUG] This is indexOfIDStart: " + indexOfIDStart);
					System.out.println("[DEBUG] This is indexOfIDEnd: " + indexOfIDEnd);
					System.out.println("[DEBUG] This is indexOfStart: " + indexOfStart);
					System.out.println("[DEBUG] This is indexOfEnd: " + indexOfEnd);
					
					title = fullFileName.substring(0, indexOfCountryStart - 1);
					if(indexOfStart == -1){
						ID = fullFileName.substring(indexOfIDStart, indexOfIDEnd + 1);
					}else{
						ID = fullFileName.substring(indexOfStart, indexOfEnd + 1);
					}
					w.write(ID + "\t" + title + "\n");
					break;
				} catch (Exception e) {
					System.err.println("Failure!!");
//					e.printStackTrace();
				}
			} else if (allFiles[i].isDirectory()) {
				outputInfo(allFiles[i].getAbsolutePath());
			}
		}
	}
	
	private List<Integer> findIndexOfMore(String stringIn, String regex){
		List<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i <= stringIn.length(); i++){
			try{
//				System.out.println(stringIn.substring(i, i+1));
				if (stringIn.substring(i, i+1).equals(regex)){
					output.add(i);
//					System.out.println("this is i: " + i);
//					System.out.println("this is output.length: " + output.length);
				}
			}catch(Exception e){
				
			}
		}
		
		return output;
	}
	
	/**
	 * 
	 */
	public void run() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("Input file location: ");
		fileNameIn = in.nextLine();

		System.out.println("Running...");
		startTime = System.currentTimeMillis();

		folder = new File(fileNameIn);
		statText = new File("./" + fileTitle + ".txt");
		is = new FileOutputStream(statText);
		osw = new OutputStreamWriter(is);
		w = new BufferedWriter(osw);
		
		outputInfo(fileNameIn);
		w.close();
		in.close();
	}
}
