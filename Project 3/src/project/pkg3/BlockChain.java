package project.pkg3;

/* This class creates the hash of the  blocks generated using user input.
It creates objects of Block class and adds them to its object array list.
Through it method isChainValid(),it validates the authenticity of each block in the chain and updates the user if the block chain is valid.
 */
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

        //Class Variables to hold the blocks in chain and the hash of the latest block
        Chain = new ArrayList<Block>();
        chainHash = new String();

    }

    public static void main(String args[]) {

        //Creating a genesis block with no arg constructor
        Block B = new Block();
        int index = B.getIndex();

        BlockChain x = new BlockChain();

        //Adding genesis block to the chain
        x.addBlock(B);
        x.chainHash = B.proofOfWork(B.getDifficulty());

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
            //System.out.println("\n");

            switch (choice) {

                case 1: {
                    System.out.println("Enter difficulty");

                    int difficulty = Integer.parseInt(in.nextLine());

                    System.out.println("Enter transaction");
                    String data = in.nextLine();

                    Timestamp timestamp = x.getTime();

                    Timestamp startTime = timestamp;

                    // Block c = new Block(++index, timestamp, data, 5);
                   // Block c = new Block(++index, timestamp, data, 4);
                    Block c = new Block(++index, timestamp, data, difficulty);

                    //Adding a  new block to the block chain
                    x.addBlock(c);
                    x.chainHash = c.getCurrentHash();

                    Timestamp endTime = x.getTime();

                    long total = endTime.getTime() - startTime.getTime();

                    //  System.out.println("Total time taken to create "+ total);
                    /* The average time taken while adding blocks of
                   Difficulty 5 is approximately 3.52 seconds.
                   Difficulty 4 is approximately 0.425 seconds.
                   
                     */
                    break;

                }
                case 2: {
                    System.out.println("Verifying");

                    System.out.println("Chain Verification: " + x.isChainValid());

                    break;
                }
                case 3: {

                    System.out.println(x.toString());

                    Block value = x.getLatestBlock();

                    x.chainHash = value.proofOfWork(value.getDifficulty());
                    System.out.println("ChainHash =" + x.chainHash);
                    break;

                }
                case 4:
                    repeat = false;

            }
        }
         catch(NumberFormatException e){
            
                System.out.println("Invalid Input.Re-Enter the values");
                
            }

    }
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

    }

//This method uses the toString method defined on each individual block.
    public String toString() {

        String result = "chain: {";
        // let us print all the elements available in list
        for (Block b1 : Chain) {
            result += b1.toString() + "\n";

        }
        result += "}";
        return result;
    }

    public boolean isChainValid() {
        boolean result = false;

        Timestamp startTime = new Timestamp(System.currentTimeMillis());

        //System.out.println("start time taken to create " + startTime);

        //If Block chain has no more  other blocks except genesis block,generate the hash and verify it with lead zeroes needed as per the nonce  and with the chain hash value
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
        } else {

            /**
             * If Block chain has more other blocks other than genesis
             * block,generate the hash and verify it with lead zeroes needed as
             * per the nonce, compare it with the chain hash value and also
             * check if previous hash in each block is equal to hash of its
             * previous block
             */
                
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

      /*  Timestamp endTime = new Timestamp(System.currentTimeMillis());

        System.out.println("end time taken to verify " + endTime);
        long total = endTime.getTime() - startTime.getTime();

        System.out.println("Total time taken to verify " + total);
        
        The total time taken to verify a block chain of blocks with difficulty 4 is nearly 1 second and for difficulty 5 is 2 seconds.
        
        */
      
        return result;
    }

}
