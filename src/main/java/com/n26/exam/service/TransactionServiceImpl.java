package com.n26.exam.service;

import com.n26.exam.model.Transaction;
import java.time.ZonedDateTime;
import java.util.List;
import com.n26.exam.api.exception.NotFoundException;
import com.n26.exam.data.TransactionContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {


    private TransactionContainer transactionContainer;

    @Autowired
    public TransactionServiceImpl(TransactionContainer transactionContainer) {
        this.transactionContainer = transactionContainer;
    }

    @Override
    @Async
    public ZonedDateTime add(Transaction transaction) {

        return transactionContainer.add(transaction);
    }

}
