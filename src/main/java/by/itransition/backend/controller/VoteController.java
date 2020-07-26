package by.itransition.backend.controller;

import by.itransition.backend.dto.VoteDto;
import by.itransition.backend.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://frontend-fanfic.herokuapp.com/")
@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        if (voteDto.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
