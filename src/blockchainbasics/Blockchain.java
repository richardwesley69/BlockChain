package blockchainbasics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private String data;
    private String hash;

    public Block(int index, long timestamp, String previousHash, String data) {
        this.index = index;
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.data = data;
        this.hash = calculateHash();
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public String getData() {
        return data;
    }

    public String calculateHash() {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String input = index + timestamp + previousHash + data;
            byte[] hashBytes = sha256.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

public class Blockchain {
    private List<Block> blockchain;

    public Blockchain() {
        this.blockchain = new ArrayList<>();
        addGenesisBlock();
    }

    private void addGenesisBlock() {
        blockchain.add(new Block(0, new Date().getTime(), "0", "Genesis Block"));
    }

    public void addBlock(String data) {
        Block previousBlock = blockchain.get(blockchain.size() - 1);
        Block newBlock = new Block(previousBlock.getIndex() + 1, new Date().getTime(), previousBlock.getHash(), data);
        blockchain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        blockchain.addBlock("Transaction 1");
        blockchain.addBlock("Transaction 2");
        blockchain.addBlock("Transaction 3");

        System.out.println("Blockchain is valid: " + blockchain.isChainValid());
        System.out.println("Blockchain:");
        for (Block block : blockchain.blockchain) {
            System.out.println("Index: " + block.getIndex());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Data: " + block.getData());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println();
        }
    }
}
