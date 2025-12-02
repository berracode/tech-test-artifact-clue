package co.com.bancolombia.galatea.controller;

import co.com.bancolombia.galatea.dto.ManuscriptRequest;
import co.com.bancolombia.galatea.dto.StatsResponse;
import co.com.bancolombia.galatea.service.ArtifactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clue")
@RequiredArgsConstructor
public class ArtifactController {

    private final ArtifactService artifactService;

    @PostMapping()
    public ResponseEntity<Object> validateManuscript(@RequestBody @Valid ManuscriptRequest manuscriptRequest) {
        var result = artifactService.analyzeManuscript(manuscriptRequest);
        if (result) {
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.status(403).build();

    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> stats() {
        return ResponseEntity.ok(artifactService.getStats());
    }

}
