package engine.controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import engine.model.completeTime.CompleteTime;
import engine.model.completeTime.CompleteTimeService;
import engine.model.quiz.Quiz;
import engine.model.quiz.QuizService;
import engine.model.user.User;
import engine.model.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QuizController {
    @Autowired
    private Gson gson;

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompleteTimeService completeTimeService;

    @PostMapping("/api/quizzes")
    public ResponseEntity<String> addQuiz(@Valid @RequestBody Quiz quiz, Authentication authentication, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByEmail(authentication.getName());
        quiz.setUser(user);
        quiz = quizService.addQuiz(quiz);
        JsonObject object = gson.toJsonTree(quiz).getAsJsonObject();
        object.addProperty("user", user.getEmail());
        return new ResponseEntity<>(gson.toJson(object), HttpStatus.OK);
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<String> getQuiz(@PathVariable long id) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Quiz quiz = quizService.getById(id);
        return new ResponseEntity<>(gson.toJson(quiz), HttpStatus.OK);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<String> getQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int pageSize) {
        return new ResponseEntity<>(gson.toJson(quizService.getAllPaged(page, pageSize)), HttpStatus.OK);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<String> solveQuiz(@PathVariable long id, @RequestBody String answer, Authentication authentication) {

        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (!JsonParser.parseString(answer)
                .getAsJsonObject()
                .has("answer")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        JsonArray array = JsonParser.parseString(answer)
                .getAsJsonObject()
                .getAsJsonArray("answer");
        Type listOfInteger = new TypeToken<List<Integer>>() {} .getType();
        List<Integer> answers = gson.fromJson(array,listOfInteger);
        if (answers == null) {
            answers = new ArrayList<>();
        }

        Quiz quiz = quizService.getById(id);
        JsonObject check = new JsonObject();
        List<Integer> correctAnswers = quiz.getAnswer();

        if (quiz.getAnswer() == null) {
            correctAnswers = new ArrayList<>();
        }
        correctAnswers.sort(Integer::compareTo);
        answers.sort(Integer::compareTo);

        if (answers.equals(correctAnswers)) {
            check.addProperty("success", true);
            check.addProperty("feedback", "Congratulations, you're right!");
            completeTimeService.saveResult(id, authentication.getName());
        } else {
            check.addProperty("success", false);
            check.addProperty("feedback", "Wrong answer! Please, try again.");
        }
        return new ResponseEntity<>(gson.toJson(check), HttpStatus.OK);
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable long id, Authentication authentication) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (!quizService.getById(id).getUser()
                .getEmail().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizService.deleteQuiz(quizService.getById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/quizzes/completed")
    public ResponseEntity<String> getTimeOfCompletedQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int pageSize,
            Authentication authentication) {
        Page<CompleteTime> completedQuizPage = completeTimeService.getAllPaged(page, pageSize, "completedAt", authentication.getName());
        Gson gson1 = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonArray array = new JsonArray();
        completedQuizPage.getContent().forEach(completeTime -> {
            JsonObject object = gson1.toJsonTree(completeTime).getAsJsonObject();
            object.addProperty("completedAt", completeTime.getCompletedAt().toString());
            array.add(object);
        });
        JsonObject jsonPage = gson1.toJsonTree(completedQuizPage).getAsJsonObject();
        jsonPage.add("content", array);
        return new ResponseEntity<>(gson.toJson(jsonPage), HttpStatus.OK);
    }
}
