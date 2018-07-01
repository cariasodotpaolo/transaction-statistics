package com.n26.exam.service;

import com.n26.exam.model.Transaction;
import java.time.ZonedDateTime;
import com.n26.exam.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {


    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Async
    public ZonedDateTime add(Transaction transaction) {

        return transactionRepository.add(transaction);
    }

}
