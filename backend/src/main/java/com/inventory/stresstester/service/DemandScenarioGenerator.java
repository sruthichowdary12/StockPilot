
package com.inventory.stresstester.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DemandScenarioGenerator {

    public int generateDemand(
            double averageDemand,
            double standardDeviation,
            double demandMultiplier,
            Random random) {

        if (averageDemand < 0 || standardDeviation < 0
                || demandMultiplier < 0) {
            throw new IllegalArgumentException(
                    "Demand parameters cannot be negative");
        }

        double mean = averageDemand * demandMultiplier;
        double deviation = standardDeviation * demandMultiplier;

        double generated = mean
                + random.nextGaussian() * deviation;

        return Math.max(0, (int) Math.round(generated));
    }
}
