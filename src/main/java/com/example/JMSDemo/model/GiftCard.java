package com.example.JMSDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCard {

    private String giftCardId;
    private boolean isReserved;
    private Instant timestamp;
}
