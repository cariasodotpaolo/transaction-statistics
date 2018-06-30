package com.n26.exam.service;

import com.n26.exam.model.Transaction;
import java.time.ZonedDateTime;
import java.util.List;
import com.n26.exam.api.exception.NotFoundException;

public interface TransactionService {

    ZonedDateTime add(Transaction transaction);

}
