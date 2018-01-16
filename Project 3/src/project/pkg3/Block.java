package project.pkg3;

/* This class creates individual blocks and initiates their memeber variables through corresponding Accessor and mutator methods.
 */
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Base64;
import javax.xml.bind.DatatypeConverter;

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
    String data, prevHash;
    int difficulty;
    int nonce;
    static String currentHash;

    public Block() {

        setIndex(0);

        timestamp = new Timestamp(System.currentTimeMillis());
        data = "{Genisis}";
        prevHash = "";
        nonce = 0;
        difficulty = 2;
        setDifficulty(difficulty);

        setCurrentHash(proofOfWork(difficulty));

    }

    public void setCurrentHash(String hash) {

        currentHash = hash;

    }

    public String getCurrentHash() {

        return currentHash;
    }

    //Constructor to add user defined block of transactions
    public Block(int index, Timestamp timestamp, String data, int difficulty) {
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
        String temp = proofOfWork(difficulty);

        //Setting current hash to this block hash for the next block to set its previous hash value
        setCurrentHash(temp);

    }

    public String calculateHash() {
        /*This method computes a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty.

Returns:
a String holding Hexadecimal characters*/

        String result = null;

        String tempData = getData();

        String data = Integer.toString(getIndex()) + getTimestamp().toString() + getData() + this.prevHash + this.nonce + this.difficulty;

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(data.getBytes("UTF-8"));

            result = DatatypeConverter.printHexBinary(hash);

            return result;

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return result;

    }

    public String proofOfWork(int difficulty) {
        /*The proof of work methods finds a good hash. It increments the nonce until it produces a good hash.

This method calls calculateHash() to compute a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty. If the hash has the appropriate number of leading hex zeroes, it is done and returns that proper hash. If the hash does not have the appropriate number of leading hex zeroes, it increments the nonce by 1 and tries again. It continues this process, burning electricity and CPU cycles, until it gets lucky and finds a good hash.

Parameters:
difficulty - is the number of hex 0's a proper hash must have
Returns:
a String with a hash that has the appropriate number of leading hex zeroes.*/

//starting the nonce value with 0
        int i = 0;
        this.nonce = i;
        String hash = calculateHash();

//Formulating the number of lead zeros required based on difficulty
        String required = "0";
        for (int j = 0; j < difficulty - 1; j++) {
            required += "0";
        }

        while (!hash.substring(0, difficulty).equals(required)) {
            //Increment the nonce untill the lead zeroes in hash meet the required condition
            this.nonce++;
            hash = calculateHash();

        }

//System.out.println("Good Hash found \n"+hash+"\n"+required);
        return hash;
    }

    public int getDifficulty() {
        /*Simple getter method

Returns:
difficulty */
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {

        this.difficulty = difficulty;
    }

    public String toString() {

        /*Override Java's toString method

Overrides:
toString in class java.lang.Object
Returns:
A JSON representation of all of this block's data is returned.*/
        return "[" + this.index + "," + this.timestamp + "," + this.data + "," + this.prevHash + "," + this.nonce + "," + this.difficulty + "]";

    }

    public void setPreviousHash(String previousHash) {
        prevHash = previousHash;

    }

    public String getPreviousHash() {

        return prevHash;

    }

    public int getIndex() {

        return index;

    }

    public void setIndex(int index) {
        this.index = index;

    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;

    }

    public Timestamp getTimestamp() {

        return timestamp;

    }

    public String getData() {

        return data;

    }

    public void setData(String data) {
        this.data = data;

    }

}
