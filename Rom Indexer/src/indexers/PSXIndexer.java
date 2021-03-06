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
	
	Writer writer;		// This writes data to the file
	long startTime;		// Start time for how long the program is running
	long endTime;		// End time for program run time
	public String fileTitle = "PSX Titles";	// Title of the file that will be created
	
	/**
	 * 
	 */
	public PSXIndexer(){
		System.out.println("Initializing...");

		try {
			this.run();
		} catch (IOException e) {e.printStackTrace();}
		
		endTime = System.currentTimeMillis();
		long totalTime = (endTime - startTime) / 1000;
		System.out.println("Finished in " + totalTime + "seconds!");
	}
	
	/**
	 * 
	 */
	public void run() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("Input file location: ");
		String fileNameIn = in.nextLine();

		System.out.println("Running...");
		startTime = System.currentTimeMillis();

		File statText = new File("./" + fileTitle + ".txt");
		FileOutputStream is = new FileOutputStream(statText);
		OutputStreamWriter osw = new OutputStreamWriter(is);
		writer = new BufferedWriter(osw);
		
		outputInfo(fileNameIn);
		writer.close();
		in.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PSXIndexer();
	}

	/**
	 * @param location The location 
	 */
	public void outputInfo(String location) {
		File currentFolder = new File(location);
		File[] allFiles = currentFolder.listFiles();

		for (int i = 0; i < allFiles.length; i++) {
			String fileExtention = allFiles[i].getName().substring(allFiles[i].getName().length() - 3, allFiles[i].getName().length());
			if (allFiles[i].isFile() && !fileExtention.equalsIgnoreCase(".db") && !fileExtention.equalsIgnoreCase(".")	// This chooses only files that aren't system files or folders
				&& !fileExtention.equalsIgnoreCase(".ini") && !fileExtention.equalsIgnoreCase(".desktop")) {
				String fullFileName = allFiles[i].getName().substring(0, allFiles[i].getName().lastIndexOf('.'));	//This grabs the name of the file without the extension
				List<Integer> breakLocationsStart = findIndexOfMore(fullFileName, "[");
				if(breakLocationsStart.size() == 0 ){
					break;
				}
				List<Integer> breakLocationsEnd = findIndexOfMore(fullFileName, "]");
				
				String title = "";
				String[] vars = new String[6];
				try{
					title = fullFileName.substring(0, breakLocationsStart.get(0) - 1);
					vars[0] = fullFileName.substring(breakLocationsStart.get(0), breakLocationsEnd.get(0) + 1);
					vars[1] = fullFileName.substring(breakLocationsStart.get(1), breakLocationsEnd.get(1) + 1);
					vars[2] = fullFileName.substring(breakLocationsStart.get(2), breakLocationsEnd.get(2) + 1);
				}catch(Exception e){
					System.out.println("Fail with vars");
				}
				
				for(int j = 0; j < 3; j++){
					if(vars[j] == null){
						break;
					}else if(vars[j].substring(1, 2).toLowerCase().equals("s")){
						vars[3] = vars[j];
					}else if(vars[j].substring(1, 2).toLowerCase().equals("d")){
						vars[4] = vars[j];
					}
				}
				
//				System.out.println(fullFileName);
//				System.out.println("[DEBUG] This is title: " + title);
//				System.out.println("[DEBUG] This is vars[0]: " + vars[0]);
//				System.out.println("[DEBUG] This is vars[1]: " + vars[1]);
//				System.out.println("[DEBUG] This is vars[2]: " + vars[2]);
//				System.out.println("[DEBUG] This is vars[3]: " + vars[3]);
//				System.out.println("[DEBUG] This is vars[4]: " + vars[4]);
//				System.out.println("[DEBUG] This is vars[5]: " + vars[5]);

				try {
					writer.write(vars[3] + "\t" + title + "\n");
				} catch (IOException e) {
					System.out.println("[ERROR] There was an error writing to file!!");
				}

				System.out.println();
				break;
			} else if (allFiles[i].isDirectory()) {
				outputInfo(allFiles[i].getAbsolutePath());
			}
		}
	}
	
	private List<Integer> findIndexOfMore(String stringIn, String regex){
		List<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i <= stringIn.length(); i++){
			try{
				if (stringIn.substring(i, i+1).equals(regex)){
					output.add(i);
				}
			}catch(Exception e){
				System.err.println("Failure within findIndexOf()!!");
			}
		}
		return output;
	}
}
