package controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Controller
@Scope("session")
public class RefreshRepositoriesController {
    @RequestMapping(value = "/getRepositories", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> listAllUsers(HttpServletRequest request) {
        Path directory = (Path) request.getSession().getAttribute("directory");

        if(directory == null) {
            System.out.println("LOSHO");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        List<String> files = Arrays.asList(directory.toFile().list());
        files.forEach(it -> System.out.println(it));
        if (files.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<String>>(files, HttpStatus.OK);
    }
}
