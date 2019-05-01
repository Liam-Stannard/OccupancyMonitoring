/**
 * 
 */
package uk.ac.newcastle.stannard.liam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Liam Stannard
 * Class used to handle the I/O for Person objects
 */
public class ObjectSerialiser<T>
{

    private  String path = "res/ser.ser";
	
	/**
	 * Defualt Constructor 
	 */
	public ObjectSerialiser(String path) {
		this.path = path;
		 
	}
	/**
	 *Loads the String from the resources folder 
	 */
	public T load() {
		File perFile = new File(path);
		if(perFile.exists()) {
		try {
			
		 FileInputStream fileInput = new FileInputStream(path);
		    ObjectInputStream objectIn = new ObjectInputStream(fileInput);
		    T object = (T) objectIn.readObject();
		    
		    objectIn.close();
		    return object;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
	/**
	 *Saves the String to the resources folder 
	 */
	
	public boolean save(T obT) {
		try {
			 FileOutputStream fileOut = new FileOutputStream(path);
			 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			
			objectOut.writeObject(obT);
			
            objectOut.close();
            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return false;
		
		
	}

}
