
package com.inventory.stresstester.dto.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySimulationResponse(

    int simulationDay,
    LocalDate date,
    int openingInventory,
    int incomingQuantity,
    int demandQuantity,
    int fulfilledQuantity,
    int stockoutQuantity,
    int backorderQuantity,
    int closingInventory,
    BigDecimal holdingCost,
    BigDecimal stockoutCost

) {}
