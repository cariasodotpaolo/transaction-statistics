package com.n26.exam.api.controller;


import com.n26.exam.model.Transaction;
import com.n26.exam.service.TransactionService;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) throws Exception {

        transactionService.add(transaction);

        if(transaction.getTimestamp().isBefore(LocalDateTime.now().minusSeconds(60))) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("", HttpStatus.CREATED);
        }

    }
}
