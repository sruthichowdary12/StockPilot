
package com.inventory.stresstester.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class LeadTimeScenarioGenerator {

    public int generateLeadTime(
            double averageLeadTime,
            double standardDeviation,
            double multiplier,
            Random random) {

        if (averageLeadTime < 0 || standardDeviation < 0
                || multiplier < 0) {
            throw new IllegalArgumentException(
                    "Lead-time parameters cannot be negative");
        }

        double adjustedMean = averageLeadTime * multiplier;
        double adjustedDeviation = standardDeviation * multiplier;

        double generated = adjustedMean
                + random.nextGaussian() * adjustedDeviation;

        return Math.max(1, (int) Math.ceil(generated));
    }
}
