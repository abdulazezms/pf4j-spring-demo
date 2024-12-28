package com.aziz.pf4jspringpaymentplugins.api.controller;

import com.aziz.pf4jspringpaymentplugins.api.plugin.BankPluginManager;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/plugins")
@RequiredArgsConstructor
public class PluginsController {

    private final BankPluginManager bankPluginManager;

    @PostMapping(value = "/{bankId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addBankPlugin(@Parameter(description = "The unique identifier of the bank", required = true, example = "first-bank") @PathVariable String bankId,
                                                @Parameter(description = "The plugin JAR file to be uploaded", required = true, example = "first-bank-plugin-0.0.1-SNAPSHOT.jar") @RequestParam("file") MultipartFile file) {
        bankPluginManager.addBankPlugin(bankId, file);
        return new ResponseEntity<>("Plugin added successfully", HttpStatus.CREATED);
    }
}
