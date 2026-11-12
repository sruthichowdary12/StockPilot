
package com.inventory.stresstester.dto.simulation;

public record PolicyComparisonResponse(

    SimulationResponse firstPolicyResult,
    SimulationResponse secondPolicyResult,
    String recommendedPolicy,
    String recommendationReason

) {}
