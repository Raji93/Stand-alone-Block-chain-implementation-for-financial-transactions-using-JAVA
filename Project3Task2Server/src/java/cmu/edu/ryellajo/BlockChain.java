package cmu.edu.ryellajo;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Raji
 */
public class BlockChain {

    ArrayList<Block> Chain;
    String chainHash;

    public BlockChain() {

        Chain = new ArrayList<Block>();
        chainHash = new String();

    }

 
    public Timestamp getTime() {
//Returns the current system time
        return new Timestamp(System.currentTimeMillis());

    }

//This method Returns a reference to the most recently added Block
    public Block getLatestBlock() {
        return Chain.get(Chain.size() - 1);

    }

    public void addBlock(Block newBlock) {
        Chain.add(newBlock);
        chainHash = newBlock.getCurrentHash();

    }

//This method uses the toString method defined on each individual block.
    public String toString() {

        String result = "chain: {";
        // let us print all the elements available in list
        for (Block b1 : Chain) {
            result += b1.toString() + "\n";

        }
        result += "}";
         result += "\n chainHash =="+chainHash;
        return result;
    }

    public boolean isChainValid() {
        boolean result = false;

        if (Chain.size() == 1) {
            
        
            Block check = getLatestBlock();

            String checkHash = check.calculateHash();
                
            String required = new String();
            

            int difficultyCheck = check.getDifficulty();

            for (int k = 0; k < difficultyCheck; k++) {
                required += "0";
            }

          
            if (chainHash.equals(checkHash) && checkHash.substring(0, difficultyCheck).equals(required)) {
                result = true;
                    
            } else {
                result = false;
       
            }
        } 
        //Validate the block if block chain has more than one block
        else {
 
            int i = 0;
            int j = 1;
            for (j = 1; j < Chain.size(); j++) {

                Block check = Chain.get(i);
                Block next = Chain.get(j);

                String src = check.calculateHash();

                String target = next.getPreviousHash();

                i = j;
                if (src.equals(target)) {

                    result = true;
                } else {
                    break;
                }

            }

            if (result) {

                Block check = getLatestBlock();

                int difficultyCheck = check.getDifficulty();

                String checkHash = check.calculateHash();

                String required1 = new String();

                for (int k = 0; k < difficultyCheck; k++) {
                    required1 += "0";
                }

                if ((checkHash.substring(0, difficultyCheck).equals(required1))) {
                    result = true;
                } else {
                    result = false;
                }

            }

        }

        return result;
    }
    
     /**
     * Web service operation
     */
  
    public BigInteger decrypt(String input) {

        BigInteger e = new BigInteger("65537");

        BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

        BigInteger x = new BigInteger(input);

        BigInteger decrypt = x.modPow(e, n);
        //TODO write your implementation code here:

       
        return decrypt;
    }
    
    

}
