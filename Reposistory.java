package edu.csulb.cecs534;
/**
 * Description: The main method to receive three arguments for coping the 
 * source folder to the repository folder. Simultaneously, creating the manifest fle
 * in the repository folder. It also able to do check-out(accept 5 parameters) and 
 * check-in features(accept 3 parameters).
 * @author Wanli Cheng chengwl2008@gmail.com,
 *		   
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Reposistory {
	private static String firstLevel = null;
	public static void main(String[] args) throws IOException {
		/*
		 * decide what to do based on the first argument(create, checkout, checkin) 
		 */
		if (args[0].equalsIgnoreCase("checkout")) {
			ArrayList<String> filePathList = new ArrayList<>();
			decideFiles(args[1] + " " + args[2], args[3], filePathList);
			checkOutFiles(filePathList, args[3], args[4]);
		} else {
			String src = args[1];
			String des = args[2];
			if (args[0].equalsIgnoreCase("checkin")) {
				checkinPreparation(new File(args[1]));
				src += "/" + firstLevel;
			}
			String[] srcNameArray = src.split("/");
			String desNewsrc = des+"/"+srcNameArray[srcNameArray.length - 1];
			System.out.println(desNewsrc);

			String activity = des+"/Activity";
			new File(desNewsrc).mkdirs();
			new File(activity).mkdirs();
			
			/*
			 * DATA FOR MANIFEST FILE
			 */
			
			// the version and code name of this project
			String ver = "Version 1 - SWE - SCM project part 1";
					
			// name of the manifest file including date and time
			DateFormat df = new SimpleDateFormat("MM-dd-yy HH-mm");
		    Calendar calobj = Calendar.getInstance();
		    String manifestName = "MANIFEST - "+df.format(calobj.getTime());
		    
			// the command user used
			String userCommand = "CREATE REPOSITORY";
			
			// full source path to original source tree given above
			
			// full source path to repository given above
			
			// the detail for each file
			ArrayList<String> artifacrID = new ArrayList<String>();
			ArrayList<String> fileOriginalName = new ArrayList<String>();
			ArrayList<String> projectTreePath = new ArrayList<String>();
			ArrayList<String> repoPath = new ArrayList<String>();

			
			/*
			 * after create source tree and activity folder I now
			 * create a sub folder inside source tree using the file name from
			 * original source tree file. Then inside that sub folder, I copy
			 * the old file of the same name over.
			 */
			File folder = new File(src);
			File[] listOfFiles = folder.listFiles();
			//String[] ListofFileinSrc = srcDir.list();
			for(int i = 0; i < listOfFiles.length; i++){
				if (!listOfFiles[i].isDirectory() && listOfFiles[i].getAbsolutePath().indexOf("DS_Store") == -1) {
					System.out.println(listOfFiles[i].getName());
					// C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/Repository/SourceTree = desNewsrc
					new File(desNewsrc+"/"+listOfFiles[i].getName()).mkdirs();
	
					// now access hello.txt content and call the function that generate 
					// ascii number, get the ascii number them rename the file.
					// get the file content
					String filecontent = readFile(listOfFiles[i]);
					System.out.println(filecontent);
					// get the checksum
					int filechecksum = checksum(filecontent);
					System.out.println(filechecksum);
					// get the file extention 
					String[] filenameArray = listOfFiles[i].getName().split("\\.");
					String fileExtention = filenameArray[filenameArray.length-1];
					
					// the actual new file name (artifactID 1234.6.txt)
					String newFileName = ""+filechecksum + "." + listOfFiles[i].length() + "." +fileExtention;
					System.out.println(newFileName);
					// rename the file
					File subdir = new File(desNewsrc+"/"+listOfFiles[i].getName()+"/"+newFileName);
					System.out.println(subdir);
					//copy the content over
					if(!subdir.exists()){
						copyFile(listOfFiles[i], subdir);
					}
					/*
					 * this section is to store the information for activity file
					 */
					artifacrID.add(newFileName);
					fileOriginalName.add(listOfFiles[i].getName());
					projectTreePath.add(listOfFiles[i].getPath());
					repoPath.add(subdir.toPath().toString());
					System.out.println(newFileName);
					System.out.println(listOfFiles[i].getName());
					System.out.println(listOfFiles[i].getPath());
					System.out.println(subdir.toPath().toString());	
				}
			}
			/*
			 * after we finish checking and copy all of the new version of the source tree file
			 * we can now add an aactivity file.
			 */
			
			/*
			 * combine all of the detail description into 1 full string
			 */
			ArrayList<String> fullDetailDescription = new ArrayList<String>();
			for(int j = 0; j < artifacrID.size(); j++){
				String full = artifacrID.get(j)+"   "+fileOriginalName.get(j)+"   "
						+projectTreePath+ "   "+repoPath;
				fullDetailDescription.add(full);
			}
			// create the manifest file
			try{
			    PrintWriter writer = new PrintWriter(activity+"/"+manifestName+".txt");
			    writer.println(ver);
			    writer.println();
			    writer.println(manifestName);
			    writer.println();
			    writer.println("User Command: "+userCommand);
			    writer.println();
			    writer.println("Full Source Path: "+src);
			    writer.println();
			    writer.println("Full Destination Path: "+des);
			    writer.println();
			    // list of detail description of added file
			    for(int u = 0; u < fullDetailDescription.size(); u++){
				    writer.println(fullDetailDescription.get(u));
			    }
			    writer.close();
			} catch (IOException e) {
				System.out.println("fail to write manifest file");
			}
			cleanUp(src);
		}
	}
	/*
	 * this function 
	 * INPUT: is a string that represent the
	 * content of the file
	 * OUTPUT: is an integer that is the sum of all
	 * the character in the content multiple by 1 3 11 17.
	 */
	public static int checksum (String content){
		int S = 0;
		for(int i = 0; i < content.length(); i = i+4){
			S = S + content.charAt(i) * 1;
			if((i+1) < content.length()){
				S = S + content.charAt(i+1) * 3;
			}
			if((i+2) < content.length()){
				S = S + content.charAt(i+2) * 11;
			}
			if((i+3) < content.length()){
				S = S + content.charAt(i+3) * 17;
			}
		}
		return S;
	}
	
	/*
	 * readFile
	 * INPUT: a path that indicate the target file
	 * OUTPUT: a String that represent the content of the file
	 */
	private static String readFile(File file) throws FileNotFoundException {
		SimpleTokenStream read = new SimpleTokenStream(file);
		String s = "";
		while(read.hasNextToken() == true){
			s = s+read.nextToken();
		}
		read.close();
		return s;
	}
	
	/**
	 * decide what files need to copied out
	 * @param dateTime date and time (yy-MM-dd HH-mm)
	 * @param srcPath source folder path
	 * @param filePathList a list storing files need to be copied 
	 * @throws IOException input output exception
	 */
	public static void decideFiles(String dateTime, String srcPath, ArrayList<String> filePathList) throws IOException {
		String srcFullPath = srcPath + "/" + "Activity" + "/";
		File[] files = new File(srcFullPath).listFiles();
		for (File file : files) {
			if (file.getName().indexOf(dateTime) != -1) {
				srcFullPath += "/" + file.getName();
				break;
			}
		}
		
		BufferedReader br = new BufferedReader(new FileReader(srcFullPath));
		String str = null;
		String tempStr = null;
		while ((tempStr = br.readLine()) != null) {
			str = tempStr;
		}
		br.close();
		
		String newStr = str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("]"));
		String[] filePathes = newStr.split(",");
		for (String string : filePathes) {
			filePathList.add(string.trim());
		}
	}
	
	/**
	 * copy out files
	 * @param fileList a list storing files need to be copied
	 * @param srcFolder source folder path
	 * @param destFolder destination folder path
	 * @throws IOException input output exception
	 */
	public static void checkOutFiles(ArrayList<String> fileList, String srcFolder, String destFolder) throws IOException {
		for (String string : fileList) {
			copyFile(string, srcFolder, destFolder);
		}
	}
	
	/**
	 * copy a specified file to destination
	 * @param filePath source file path
	 * @param srcFolder source folder path
	 * @param destFolder destination folder path
	 * @throws IOException input output exception
	 */
	public static void copyFile(String filePath, String srcFolder, String destFolder) throws IOException {
		filePath = filePath.replaceAll("\\\\", "/");
		srcFolder = srcFolder.replaceAll("\\\\", "/");
		destFolder = destFolder.replaceAll("\\\\", "/");
		String destFilePath = destFolder + "/" + filePath.split(srcFolder + "/")[1];
		File destFile = new File(destFilePath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		copyFile(new File(filePath), destFile);
	}
	
	/**
	 * copy file detail
	 * @param src source file
	 * @param dest destination file
	 * @throws IOException input output exception
	 */
	public static void copyFile(File src, File dest) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(src));
		BufferedWriter bw = new BufferedWriter(new FileWriter(dest));
		char[] chs = new char[1024];
		int len = 0;
		while ((len = br.read(chs)) != -1) {
			bw.write(chs, 0, len);
		}
		bw.close();
		br.close();
	}
	
	/**
	 * preparation for check-in
	 * @param srcFolder source folder
	 * @throws IOException input output exception
	 */
	public static void checkinPreparation(File srcFolder) throws IOException {
		File[] files = srcFolder.listFiles();
		for (File tempfile : files) {
			if (tempfile.getAbsolutePath().indexOf("DS_Store") == -1) {
				if (tempfile.isDirectory()) {
					checkinPreparation(tempfile);
				}else {
					String parentName = tempfile.getParentFile().getName();
					File parentFile = tempfile.getParentFile();
					firstLevel = parentFile.getParentFile().getName();
					parentFile.renameTo(new File(parentFile.getAbsolutePath() + "2"));
					copyFile(new File(tempfile.getParent() + "2/" + tempfile.getName()), new File(tempfile.getParentFile().getParent() + "/" + parentName));
				}				
			}
		}
	}
	
	/**
	 * clean up the leftovers
	 * @param src source folder path
	 */
	public static void cleanUp(String src) {
		File[] files = new File(src).listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				file.renameTo(new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 1)));
			} else {
				file.delete();
			}
		}
	}
}
