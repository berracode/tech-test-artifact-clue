package co.com.bancolombia.galatea.dto;

public record StatsResponse(
        int countClueFound,
        int countNoClue,
        double ratio
) {
}

