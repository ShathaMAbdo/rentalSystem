package se.bth.rentalSystem_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.bth.rentalSystem_server.models.Skill;
import se.bth.rentalSystem_server.repository.SkillRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SkillController {
    private final Logger log = LoggerFactory.getLogger(SkillController.class);
    @Autowired
    private SkillRepository skillRepo;

    public SkillController(SkillRepository skillRepo) {
        this.skillRepo = skillRepo;
    }
    @GetMapping("/skills")
    Collection<Skill> skills() {
        return skillRepo.findAll();
    }

    @GetMapping("/skill/{id}")
    ResponseEntity<?> getSkill(@PathVariable Long id) {
        Optional<Skill> skill = skillRepo.findById(id);
        return skill.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/skill",consumes= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws URISyntaxException {
        log.info("Request to create skill: {}", skill);
        Skill result = skillRepo.save(skill);
        return ResponseEntity.created(new URI("/api/skill/" + result.getId())).body(result);
    }

    @PutMapping("/skill")
    ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill,boolean ok) {
        log.info("Request to update skill: {}", skill);
        skill.resource_returned(ok);
        Skill result = skillRepo.save(skill);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
       log.info("Request to delete skill: {}", id);
        skillRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
