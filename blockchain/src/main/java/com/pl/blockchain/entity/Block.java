package com.pl.blockchain.entity;

import com.pl.blockchain.chain.Transaction;
import com.pl.blockchain.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data; //our data will be a simple message.
    private long timeStamp; //as number of milliseconds since 1/1/1970.
    private int nonce;
    public String merkleRoot;//merkle新增
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //merkle新增


    public Block(String data,String previousHash ) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

//    public String calculateHash() {
//        String calculatedhash = StringUtil.applySha256(
//                previousHash +
//                        Long.toString(timeStamp) +
//                        data
//        );
//        return calculatedhash;
//    }
    //需要挖矿时候，要把nonce加入计算
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        data
        );
        return calculatedhash;
    }


    //    低的难度比如1和2，普通的电脑基本都可以马上计算出来，我的建议是在4-6之间进行测试，普通电脑大概会花费3秒时间，
//    在莱特币中难度大概围绕在442592左右，而在比特币中每一次挖矿都要求大概在10分钟左右，当然根据所有网络中的计算能力，
//    难度也会不断的进行修改。
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((previousHash != "0")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
