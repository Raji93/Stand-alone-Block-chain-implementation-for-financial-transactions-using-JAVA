package project3task3client;

/*
JAVA application that acts as a client to the servlet Server program and adds the blocks as XML format inputs.
*/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.naming.spi.DirStateFactory.Result;

/**
 *
 * @author Raji
 */
public class Project3Task3Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
        // TODO code application logic here

        System.out.println(doPut());

        Scanner in = new Scanner(System.in);

        boolean repeat = true;

        while (repeat) {

            try {
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

                        data = data + "#" + encrypt(data);

                        String XML = createXML(data, difficulty, "addBlock");

                        System.out.println(doPost(XML));

                        break;

                    }

                    case 2: {
                        System.out.println("Verifying");

                        String XML = createXML("", 0, "validate");

                        System.out.println(doPost(XML));

                        break;
                    }
                    case 3: {

                        String XML = createXML("", 0, "getList");
                        int type = 2;

                        while (!(type == 1 || type == 0)) {
                            System.out.println("Enter 1 to view the result as list \n 0 to view as XML");

                            type = Integer.parseInt(in.nextLine());

                        }
                        System.out.println(doGet(type));

                        break;

                    }
                    case 4:
                        repeat = false;
                        break;

                }
            } catch (NumberFormatException e) {

                System.out.println("Invalid Input.Re-Enter the values");

            }

        }

    }

    //Method that encrypts the user data and returns a corresponding signature
    public static String encrypt(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        BigInteger e = new BigInteger("65537");
        BigInteger d = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");

        BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

        BigInteger result = new BigInteger("0");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        int size = hash.length;
        //String result = DatatypeConverter.printHexBinary(hash);
        byte[] hashNew = new byte[size + 1];
        hashNew[0] = 0;
        for (int i = 1; i < size; i++) {
            hashNew[i] = hash[i - 1];
        }
        BigInteger enc = new BigInteger(hashNew);
        result = enc.modPow(d, n);

        return result.toString();
    }

    
    //Method that is used to put  a genesis block creation request on the server
    public static String doPut() {

        int status;
        String response = "";

        try {
            URL url = new URL("http://localhost:8080/Project3Task3Server/Project3Task3Server");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            status = conn.getResponseCode();

            // If things went wrong, read the error status and message
            if (status != 201) {

                String msg = conn.getResponseMessage();
                return conn.getResponseCode() + " " + msg;
            }

            // read the response message about genesis block creation
            String output = "";
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                response += output + "\n";
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    //Method that creates a XML format of the user data
    public static String createXML(String data, int difficulty, String Operation) {
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

    // Low level routine to make an HTTP POST request
    public static String doPost(String XML) throws IOException {

        int status = 0;
        String output;

        // Make call to a particular URL
        //   URL url = new URL("http://localhost:8080/Project3Task3Server/Project3Task3Server/");
        URL url = new URL("http://localhost:8080/Project3Task3Server/Project3Task3Server");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // set request method to POST and send name value pair
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        // write to POST data area
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());

        out.write(XML);
        out.close();

        // get HTTP response code sent by server
        status = conn.getResponseCode();

        // get the response message and print it to the user
        String serverMessage = "";
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        serverMessage = br.readLine();

        //close the connection
        conn.disconnect();

        // handle exceptions
        // return HTTP status
        return serverMessage;
    }
    
    //Method to fetch the details of the block chain from the server

    public static String doGet(int type) {

        // Make an HTTP GET passing the name on the URL line
        String response = "";
        HttpURLConnection conn;
        int status = 0;

        try {

            // pass the name on the URL line
            URL url = new URL("http://localhost:8080/Project3Task3Server/Project3Task3Server");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // setting the accepted format for response

            if (type == 0) {
                conn.setRequestProperty("Accept", "text/xml");
            } else {
                conn.setRequestProperty("Accept", "text/plain");
            }
            // wait for response

            status = conn.getResponseCode();

            // If things went wrong, get the error status and message
            if (status != 200) {

                String msg = conn.getResponseMessage();
                return status + "" + msg;
            }

            // get the response message and print it to the user
            String output = "";
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                response += output + "\n";
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
