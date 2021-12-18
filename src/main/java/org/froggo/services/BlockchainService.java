package org.froggo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.froggo.model.Block;
import org.froggo.model.TransactionData;
import org.froggo.model.ValidationRequest;
import org.froggo.model.ValidationResponse;
import org.froggo.model.VerificationData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockchainService {

    private final static int PREFIX = 4;
    private final List<Block> blockchain = new ArrayList<>();

    public Block addNewBlockToBlockchain(VerificationData verificationData) {
        TransactionData transactionData = formTransactionData(verificationData);
        String prefixString = getPrefixString();

        Block newBlock = new Block(
                transactionData,
                blockchain.isEmpty() ? "0" : blockchain.get(blockchain.size() - 1).getHash(),
                new Date().getTime());
        newBlock.mineBlock(PREFIX);
        if (!newBlock.getHash().substring(0, PREFIX).equals(prefixString)) {
            throw new RuntimeException("Can't add new block");
        }
        blockchain.add(newBlock);
        return newBlock;
    }

    public boolean validateBlockchain() {
        String prefixString = getPrefixString();

        boolean flag = true;
        for (int i = 0; i < blockchain.size(); i++) {
            String previousHash = i == 0 ? "0" : blockchain.get(i - 1).getHash();
            flag = blockchain.get(i).getHash().equals(blockchain.get(i).calculateBlockHash())
                    && previousHash.equals(blockchain.get(i).getPreviousHash())
                    && blockchain.get(i).getHash().substring(0, PREFIX).equals(prefixString);
            if (!flag) break;
        }
        return flag;
    }

    public ValidationResponse validate(ValidationRequest validationRequest) {
        String did = validationRequest.getDid();
        Set<String> statementHashes = validationRequest.getStatementHashes();
        if (StringUtils.isEmpty(did) || statementHashes == null || statementHashes.isEmpty()) {
            return new ValidationResponse(false);
        }
        Optional<Block> optionalBlock = findBlockInBlockchain(did, statementHashes);
        if (optionalBlock.isPresent()) {
            Block block = optionalBlock.get();
            boolean valid = block.getData().getConsentForDataSharing() != null;
            return new ValidationResponse(valid);
        } else {
            return new ValidationResponse(false);
        }
    }

    private String getPrefixString() {
        return new String(new char[PREFIX]).replace('\0', '0');
    }

    private TransactionData formTransactionData(VerificationData verificationData) {
        TransactionData transactionData = new TransactionData();
        transactionData.setDid(verificationData.getDid());
        transactionData.setStatementHashes(verificationData.getStatementsHash());
        transactionData.setConsentForDataSharing(verificationData.getConsentForDataSharing());
        return transactionData;
    }

    private Optional<Block> findBlockInBlockchain(String did, Set<String> statementHashes) {
        return blockchain.stream()
                .filter(block -> block.getData() != null &&
                        block.getData().getDid().equals(did) &&
                        block.getData().getStatementHashes().containsAll(statementHashes))
                .findFirst();
    }
}
