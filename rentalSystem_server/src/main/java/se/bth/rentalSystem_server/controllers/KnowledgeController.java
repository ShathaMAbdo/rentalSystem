package se.bth.rentalSystem_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bth.rentalSystem_server.models.Knowledge;
import se.bth.rentalSystem_server.repository.KnowledgeRepository;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class KnowledgeController {
    private final Logger log = LoggerFactory.getLogger(KnowledgeController.class);
    @Autowired
    private KnowledgeRepository knowledgeRepo;

    public KnowledgeController(KnowledgeRepository knowledgeRepo) {
        this.knowledgeRepo = knowledgeRepo;
    }
    @GetMapping("/knowledges")
    List<Knowledge> knowledges() {
        return knowledgeRepo.findAll();
    }

    @GetMapping("/knowledge/{id}")
    ResponseEntity<?> getKnowledge(@PathVariable Long id) {
        Optional<Knowledge> knowledge = knowledgeRepo.findById(id);
        return knowledge.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/knowledge",consumes= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Knowledge> createKnowledge(@Valid @RequestBody Knowledge knowledge) throws URISyntaxException {
        log.info("Request to create knowledge: {}", knowledge);
        Knowledge result = knowledgeRepo.save(knowledge);
        return ResponseEntity.created(new URI("/api/knowledge/" + result.getId())).body(result);
    }

    @PutMapping("/knowledge")
    ResponseEntity<Knowledge> updateKnowledge(@Valid @RequestBody Knowledge knowledge,boolean ok) {
        log.info("Request to update knowledge: {}", knowledge);
        knowledge.resource_returned(ok);
        Knowledge result = knowledgeRepo.save(knowledge);
        return ResponseEntity.ok().body(result);
    }


    @DeleteMapping("/knowledge/{id}")
    public ResponseEntity<?> deleteKnowledge(@PathVariable Long id) {
       log.info("Request to delete knowledge: {}", id);
        knowledgeRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
