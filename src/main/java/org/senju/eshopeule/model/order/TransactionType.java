package org.senju.eshopeule.model.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    COD("Cash on delivery"),
    BANKING("Banking");

    private final String name;
}
