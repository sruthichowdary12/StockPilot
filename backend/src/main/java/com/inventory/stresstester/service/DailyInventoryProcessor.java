
package com.inventory.stresstester.service;

import org.springframework.stereotype.Component;

@Component
public class DailyInventoryProcessor {

    public DailyResult process(
            int openingInventory,
            int incomingQuantity,
            int currentDemand,
            int previousBackorders) {

        int available = openingInventory + incomingQuantity;

        // Fulfil previous backorders first.
        int fulfilledBackorders =
                Math.min(available, previousBackorders);

        available -= fulfilledBackorders;

        // Fulfil the current day's demand.
        int fulfilledToday =
                Math.min(available, currentDemand);

        int unmetDemand = currentDemand - fulfilledToday;

        int closingInventory = available - fulfilledToday;

        int remainingBackorders =
                previousBackorders - fulfilledBackorders
                + unmetDemand;

        return new DailyResult(
                openingInventory,
                incomingQuantity,
                currentDemand,
                fulfilledToday,
                unmetDemand,
                remainingBackorders,
                closingInventory,
                fulfilledBackorders
        );
    }

    public record DailyResult(
            int openingInventory,
            int incomingQuantity,
            int demand,
            int fulfilledToday,
            int unmetDemand,
            int remainingBackorders,
            int closingInventory,
            int fulfilledBackorders) {
    }
}
