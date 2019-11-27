package com.pl.blockchain.chain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pl.blockchain.entity.Block;
import org.apache.tomcat.util.json.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

//老的chain，没有交易
public class NoobChain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static void main(String[] args) {
        //下面是不需要挖矿的例子
//        Block firstBlock = new Block("第一个区块", "0");
//        System.out.println("Hash for block 1 : " + firstBlock.hash);
//
//        Block secondBlock = new Block("Yo im the second block",firstBlock.hash);
//        System.out.println("Hash for block 2 : " + secondBlock.hash);
//
//        Block thirdBlock = new Block("Hey im the third block",secondBlock.hash);
//        System.out.println("Hash for block 3 : " + thirdBlock.hash);
//        String blockchainJson = JSON.toJSONString(blockchain);
//        System.out.println(blockchainJson);

        //下面是需要挖矿的例子
        blockchain.add(new Block("Hi im the first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson =  JSON.toJSONString(blockchain, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    //不需要挖矿时校验整个链是否有效
//    public static Boolean isChainValid() {
//        Block currentBlock;
//        Block previousBlock;
//
//        //loop through blockchain to check hashes:
//        for(int i=1; i < blockchain.size(); i++) {
//            currentBlock = blockchain.get(i);
//            previousBlock = blockchain.get(i-1);
//            //compare registered hash and calculated hash:
//            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
//                System.out.println("Current Hashes not equal");
//                return false;
//            }
//            //compare previous hash and registered previous hash
//            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
//                System.out.println("Previous Hashes not equal");
//                return false;
//            }
//        }
//        return true;
//    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
