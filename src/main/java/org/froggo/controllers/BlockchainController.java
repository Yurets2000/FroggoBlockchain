package org.froggo.controllers;

import lombok.RequiredArgsConstructor;
import org.froggo.model.Block;
import org.froggo.model.ValidationRequest;
import org.froggo.model.ValidationResponse;
import org.froggo.model.VerificationData;
import org.froggo.services.BlockchainService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    @PostMapping("/add")
    public Block addNewBlockToBlockchain(@RequestBody VerificationData verificationData) {
        return blockchainService.addNewBlockToBlockchain(verificationData);
    }

    @GetMapping("/validateBlockchain")
    public boolean validateBlockchain() {
        return blockchainService.validateBlockchain();
    }

    @GetMapping("/validateRequest")
    public ValidationResponse validate(ValidationRequest validationRequest) {
        return blockchainService.validate(validationRequest);
    }
}