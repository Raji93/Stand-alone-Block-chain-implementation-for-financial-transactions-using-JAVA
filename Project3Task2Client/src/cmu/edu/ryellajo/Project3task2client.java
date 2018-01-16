/*
This is a web client that will pass a single message (containing a single String holding an XML
message) to a single JAX-WS service operation. 
Each of the block data is encrypted and data is send along with its signature to the block chain API for validation
in the form of XML

*/


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.ryellajo;


import cmu.edu.ryellajo.NoSuchAlgorithmException_Exception;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 *
 * @author Raji
 */
public class Project3task2client {

       /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException_Exception, UnsupportedEncodingException, NoSuchAlgorithmException, ParserConfigurationException_Exception, SAXException_Exception, IOException_Exception {
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
                    
                
                    String XML = createXML(data,difficulty,"addBlock");
                 
                   

       
                    System.out.println(convertToDoc(XML));
                   
                    break;

                }
            
                
                case 2: {
                    System.out.println("Verifying");

                    String XML = createXML("",0,"validate");
                    
                    
                    System.out.println(convertToDoc(XML));

                    break;
                }
                case 3: {

                     String XML = createXML("",0,"getList");
                    
                    
                    System.out.println(convertToDoc(XML));

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

    public static String encrypt(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        BigInteger e = new BigInteger("65537");
        BigInteger d = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");

        BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

        BigInteger result = new BigInteger("0");
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
        
     
        return result.toString();
    }


    

    
    public static String  createXML(String data,int difficulty,String Operation)
    {
        String xml = "";
        
        xml += "<Block>";
        xml += "\n";
        xml += "\t" + "<data>" + data + "</data>"; 
        xml += "\n";
        xml += "\t" + "<Operation>" + Operation + "</Operation>";
        xml += "\n";
        xml += "\t" + "<difficulty>" + difficulty + "</difficulty>";
        xml += "\n";
        xml += "</Block>";
        xml += "\n";
        
        return xml;
    
    
    }

    private static boolean genesis() {
        cmu.edu.ryellajo.Project3ServerTask2 service = new cmu.edu.ryellajo.Project3ServerTask2();
        cmu.edu.ryellajo.Project3Task2Server port = service.getProject3Task2ServerPort();
        return port.genesis();
    }

    private static String convertToDoc(java.lang.String xmlInput) throws NoSuchAlgorithmException_Exception, SAXException_Exception, ParserConfigurationException_Exception, IOException_Exception {
        cmu.edu.ryellajo.Project3ServerTask2 service = new cmu.edu.ryellajo.Project3ServerTask2();
        cmu.edu.ryellajo.Project3Task2Server port = service.getProject3Task2ServerPort();
        return port.convertToDoc(xmlInput);
    }

  

}
