package cmu.edu.ryellajo;



import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Raji
 */
public class Block {
    
    int index;
   Timestamp timestamp;
   String data,prevHash;
   int difficulty;
   int nonce;
   static String currentHash;
   
 public Block()
 {
      
     
      
      setIndex(0);
   	 
   	  timestamp = new Timestamp(System.currentTimeMillis());
      data = "{Genisis}";
   	  prevHash = "";
      nonce = 0;
      difficulty =2;
      setDifficulty(difficulty);

    
      setCurrentHash(proofOfWork(difficulty));
  
     
        
 }
  public void setCurrentHash(String hash)
  {
      
      currentHash = hash;
     
  }
  
   public String getCurrentHash()
  {
        
      return currentHash;
  }

 //Constructor to add user defined block of transactions
public Block(int index, Timestamp timestamp,String data, int difficulty)
{
/*This the Block constructor.

Parameters:
index - This is the position within the chain. Genesis is at 0.
timestamp - This is the time this block was added.
data - This is the transaction to be included on the blockchain.
difficulty - This is the number of leftmost nibbles that need to be 0.
  */  
      


       //Setting the previous hash value to hash of previous block
       setPreviousHash(getCurrentHash());

       //Setting previous hash value of this block with pevious block hash
       	this.prevHash = getPreviousHash();
 
       	setIndex(index);

       	this.index = index;
       


        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;

       	//Setting nonce value for this block through proof of work routine
        String temp =proofOfWork(difficulty);

      
        
        //Setting current hash to this block hash for the next block to set its previous hash value
        setCurrentHash(temp);
    
    
  
 } 

   public String validateBlock(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
{
           

        String dataDetails[] = data.split("#");
        String result;
        byte[] hashNew;

       BigInteger check = new BigInteger("0");

        try {
           
           
            MessageDigest digest = MessageDigest.getInstance("SHA-256");


            byte[] hash = digest.digest(dataDetails[0].getBytes("UTF-8"));
        
            
            int size = hash.length;
            
            hashNew = new byte[size+1];

            hashNew[0] = 0;

            for (int i = 1; i < size; i++) {
                hashNew[i] = hash[i - 1];
            }

          check = new BigInteger(hashNew);

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Project3Task2Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        
      if (decrypt(dataDetails[1]).compareTo(check)==0)
        {
              data = dataDetails[0];
             
             return data;
        }

        else
                    return null;
        
        
    }
   }
  public static BigInteger decrypt(String input) {

        BigInteger e = new BigInteger("65537");

        BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

        BigInteger x = new BigInteger(input);

        //Decrypt the message using public keys
        BigInteger decrypt = x.modPow(e, n);
      

       
        return decrypt;
    }

public String calculateHash(){
/*This method computes a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty.

Returns:
a String holding Hexadecimal characters*/

        String result = null;

        String tempData = getData();
      

        String data = Integer.toString(getIndex())+ getTimestamp().toString()+getData()+this.prevHash+this.nonce+this.difficulty;
 

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(data.getBytes("UTF-8"));

          

			 result = DatatypeConverter.printHexBinary(hash);

			return result;


        }catch(Exception ex) {

            ex.printStackTrace();

        }

        return result;

    }

    


public String proofOfWork(int difficulty){
/*The proof of work methods finds a good hash. It increments the nonce until it produces a good hash.

This method calls calculateHash() to compute a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty. If the hash has the appropriate number of leading hex zeroes, it is done and returns that proper hash. If the hash does not have the appropriate number of leading hex zeroes, it increments the nonce by 1 and tries again. It continues this process, burning electricity and CPU cycles, until it gets lucky and finds a good hash.

Parameters:
difficulty - is the number of hex 0's a proper hash must have
Returns:
a String with a hash that has the appropriate number of leading hex zeroes.*/

//starting the nonce value with 0
int i=0;
this.nonce = i;
String hash = calculateHash();

//Formulating the number of lead zeros required based on difficulty
String required = "0";
for(int j=0;j<difficulty-1;j++)
	required +="0"; 



while(!hash.substring(0,difficulty).equals(required))
	{
		//Increment the nonce untill the lead zeroes in hash meet the required condition
		this.nonce++;
		hash = calculateHash();
		
	}

//System.out.println("Good Hash found \n"+hash+"\n"+required);

return hash;
}


public int getDifficulty(){
/*Simple getter method

Returns:
difficulty */
       return this.difficulty;
}

public void setDifficulty(int difficulty){

    this.difficulty = difficulty;
}

public String toString()
{
    
    /*Override Java's toString method

Overrides:
toString in class java.lang.Object
Returns:
A JSON representation of all of this block's data is returned.*/

    return  "[" + this.index + "," + this.timestamp + "," +"\n"+ this.data + ","+"\n"+this.prevHash+","+this.nonce +","+this.difficulty+"]";
        
}

public void setPreviousHash(String previousHash)
{
	prevHash = previousHash;
	
	


}

 public String getPreviousHash()
 {

         	return prevHash;


 }

  public int getIndex()
  {
  
  return index;
  
  }
          
  public void setIndex(int index)
  {
     	this.index = index;
  
  }
          
  public void setTimestamp(Timestamp timestamp)
  {
      this.timestamp = timestamp;
      
  }
  
  public Timestamp getTimestamp()
  {
      
      return timestamp;
  
  }
   public String getData()
   {
       
       return data;
   
   }
           
    public void setData(String data)
    {
        this.data = data;
    
    }
        
  public String printString(String x)
   {
       
       return x;
   
   }
       
 public Document getDocument(String xmlInput) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document blockChainMessage = null;
        
        try {
            builder = factory.newDocumentBuilder();
            blockChainMessage = builder.parse(new InputSource(new StringReader(xmlInput)));
        } catch(Exception e) {            
            e.printStackTrace();           
        }
        return blockChainMessage;
              
    }

}
