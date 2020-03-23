package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.Answer;
import com.example.postgresdemo.repository.AnswerRepository;
import com.example.postgresdemo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.ResourceBundle;

@RestController
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions/{questionId}/answers")
    public List<Answer> getAnswersByQuestionId(@PathVariable Long questionId){
        return answerRepository.findByQuestionId(questionId);
    }

    @PostMapping("/questions/{questionId}/answers")
    public Answer addAnswer(@PathVariable Long questionId, @Valid @RequestBody Answer answer){
        return questionRepository.findById(questionId).map(question -> {
            answer.setQuestion(question);
            return answerRepository.save(answer);
        }).orElseThrow(()-> new ResourceNotFoundException("Question Id not found to save answer."));
    }

    @PutMapping("questions/{questionId}/answers/{answerId}")
    public Answer updateAnswer(@PathVariable("questionId") Long qId, @PathVariable(name = "answerId") Long aId, @Valid @RequestBody Answer requestAnswer){

        return answerRepository.findById(aId).map(answer -> {
            answer.setText(requestAnswer.getText());
            //set all fields which can be modified
            return answerRepository.save(answer);
        }).orElseThrow(()-> new ResourceNotFoundException("Answer not found for update."));
    }

    @DeleteMapping("questions/{questionId}/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("answerId") Long aId, @PathVariable("questionId") Long qId){
        return answerRepository.findById(aId).map(answer -> {
            answerRepository.delete(answer);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException("Answer for delete does not exists."));
    }

}
