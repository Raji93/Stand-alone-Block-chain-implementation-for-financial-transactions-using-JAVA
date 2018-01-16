/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmu.edu.ryellajo;

/*
Servlet application that acts as a Server program and accepts the user blocks as XML format inputs,validates them adds them to the block chain
*/


/**
 *
 * @author Raji
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// This example demonstrates Java servlets and HTTP
// This web service operates on Block chain class object.
@WebServlet(name = "Project3Task3Server", urlPatterns = {"/Project3Task3Server"})
public class Project3Task3Server extends HttpServlet {

    BlockChain blockChain;
    Block gen;
    static int index = 0;

    //Method that creates genesis block of the block chain
     @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
        System.out.println("Console: doPut visited");
        
        String output = "";
        PrintWriter out = response.getWriter();
        
        gen = new Block();
        blockChain = new BlockChain();

        //Adding genesis block to the chain
        blockChain.addBlock(gen);
        blockChain.chainHash = gen.proofOfWork(gen.difficulty);
            
            response.setStatus(201);
            output = "Genesis block created";
            out.println(output);
            out.flush();
        }
               
    
//Method to fetch the details of the block chain
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        System.out.println("Console: doGET visited");
        
        String acceptType = request.getHeader("Accept");  
        System.out.println("Accept type: " + acceptType);
        
        String output = "";        
        PrintWriter out = response.getWriter(); 
      
         
            // generate required formatted representation of the response message according to the value of "Accept"
            if (acceptType.equals("text/xml")) {
                response.setContentType("text/xml;charset=UTF-8");
                output = blockChain.createXML();
               
                System.out.println("Server side print out: " + output);

            }
            
            if (acceptType.equals("text/plain")) {
                response.setContentType("text/plain;charset=UTF-8");
                output = blockChain.toString();
                System.out.println("Server side print out: " + output);

            }
            
             if(output!=null)
                     response.setStatus(200);
           
             else
                 response.setStatus(400);

             out.println(output);
            out.flush();

        
    }


    // Method that creates the block based on user input data after proper validation
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnsupportedEncodingException {

       
        System.out.println("Console: doPost visited");

     

        // get the input xml request body containing the need information
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String xmlString = "";
        StringBuilder xml = new StringBuilder();
        String input;
        while ((input = br.readLine()) != null) {
            xml.append(input);
        }

        xmlString = xml.toString();

        System.out.println(xmlString);
    

        // if the request body is empty, return error
        if (xmlString.equals("")) {
            response.setStatus(404);
        } else {
            
        // get the needed field from the xml request body
        org.w3c.dom.Document doc = gen.getDocument(xmlString);
        doc.getDocumentElement().normalize();

        String root = doc.getDocumentElement().getNodeName();

        NodeList nodeList = doc.getElementsByTagName(root);
        Node node = nodeList.item(0);

        Element element = (Element) node;

        String operation = element.getElementsByTagName("Operation").item(0).getTextContent();

        String result = "";


        // implement  operations based on the operation field in the xml file
        switch (operation) {
            case "addBlock": {
            try {
                String data = element.getElementsByTagName("data").item(0).getTextContent();
                int difficulty = Integer.parseInt(element.getElementsByTagName("difficulty").item(0).getTextContent());

                String resultData = gen.validateBlock(data);
                
                if (resultData != null) {

                    Block block = new Block(++index, blockChain.getTime(),data,difficulty);
                    
                    blockChain.addBlock(block);
                    result = "Block added to the block chain";

                }
                
                else 
                    result = "Failed to add block to the block chain as data seems to be tampered";

                break;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Project3Task3Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            case "getList": {

                result = blockChain.toString();
                break;
            }

           case "validate": {

                
                    result = "Chain Verification "+blockChain.isChainValid();
                
                
                break;
            }
            
        }
        
             
        PrintWriter out = response.getWriter(); 
        
       
        if(!result.equals("")){

              System.out.println(result);
             response.setStatus(200);
            out.println(result);
            out.flush();
                
            } else {
                response.setStatus(404);
            }  

        }
    }

// Makes an HTTP GET request to the server. This is similar to the doGet provided on the client
// but this one uses a different URL. 
// This method makes a call to the HTTP GET method using 
// http://localhost:8080/WebServiceDesignStyles3ProjectServerLab/VariableMemory/"
}
