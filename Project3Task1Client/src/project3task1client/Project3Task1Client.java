/*
This is a web client that makes SOAP requests to BlockChain API.
Each of the block data is encrypted and data is send along with its signature to the block chain APIfor validation.

*/


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task1client;

import cmu.edu.ryellajo.NoSuchAlgorithmException_Exception;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

/**
 *
 * @author Raji
 */
public class Project3Task1Client {

       /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException_Exception {
        // TODO code application logic here

        if (genesis()) {
            System.out.println("Genesis block added");
        }

        Scanner in = new Scanner(System.in);

        boolean repeat = true;

        while (repeat) {

            try{
            System.out.println("Select an option from the following");
            System.out.println("1. Add a transaction to the blockchain.\n"
                    + "2. Verify the blockchain.\n"
                    + "3. View the blockchain.\n"
                    + "4. Exit \n");

          
            int choice = Integer.parseInt(in.nextLine());
          
            switch (choice) {

                case 1: {
               
                       System.out.println("Enter difficulty");
                   

                    int difficulty = Integer.parseInt(in.nextLine());

                    System.out.println("Enter transaction");
                    String data = in.nextLine();
                  

                    data = data+"#"+encrypt(data);
                    
                

                    
                    if (addBlock(difficulty,data)) {
                        System.out.println("Block created and added to the chain");
                    }
                    
                    else
                        System.out.println("Block is not added to the chain as information has been tampered");
                    break;

                }
            
                
                case 2: {
                    System.out.println("Verifying");

                    System.out.println("Chain Verification: " + checkHash());

                    break;
                }
                case 3: {

                    System.out.println(showDetails());

                    break;

                }
                case 4:
                    repeat = false;
                    break;
                    
                    
                    
                    
            }
            }
            catch(NumberFormatException e){
            
                System.out.println("Invalid Input.Re-Enter the values");
                
            }
            
            
        
        }

            
    }

    
    //Method that encrypts the input data and returns a signature 
    public static String encrypt(String input) {

        BigInteger e = new BigInteger("65537");
        BigInteger d = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");

        BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

        BigInteger result = new BigInteger("0");
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));


            int size = hash.length;

            //String result = DatatypeConverter.printHexBinary(hash);
            byte[] hashNew = new byte[size+1];

            hashNew[0] = 0;

            for (int i = 1; i < size; i++) {
                hashNew[i] = hash[i - 1];
            }

            BigInteger enc = new BigInteger(hashNew);

            result = enc.modPow(d, n);

        } catch (Exception ex) {

            ex.printStackTrace();

        }
        
     
        return result.toString();
    }

    //Web service Operation to create genesis block of the block chain
    private static boolean genesis() {
        
    
        
        cmu.edu.ryellajo.Project3Task1Server_Service service = new cmu.edu.ryellajo.Project3Task1Server_Service();
        cmu.edu.ryellajo.Project3Task1Server port = service.getProject3Task1ServerPort();
        return port.genesis();
    }

 
 //Web service Operation that validates the block chain
    private static boolean checkHash() {
        cmu.edu.ryellajo.Project3Task1Server_Service service = new cmu.edu.ryellajo.Project3Task1Server_Service();
        cmu.edu.ryellajo.Project3Task1Server port = service.getProject3Task1ServerPort();
        return port.checkHash();
    }

     
 //Web service Operation that prints  the block chain
    private static String showDetails() {
        cmu.edu.ryellajo.Project3Task1Server_Service service = new cmu.edu.ryellajo.Project3Task1Server_Service();
        cmu.edu.ryellajo.Project3Task1Server port = service.getProject3Task1ServerPort();
        return port.showDetails();
    }

     
 //Web service Operation that adds blocks to the block chain
    private static boolean addBlock(int difficulty, java.lang.String data) throws NoSuchAlgorithmException_Exception {
        cmu.edu.ryellajo.Project3Task1Server_Service service = new cmu.edu.ryellajo.Project3Task1Server_Service();
        cmu.edu.ryellajo.Project3Task1Server port = service.getProject3Task1ServerPort();
        return port.addBlock(difficulty, data);
    }
    
}
