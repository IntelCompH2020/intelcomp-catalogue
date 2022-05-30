package eu.intelcomp.catalogue.controller;

import eu.intelcomp.catalogue.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    public UserController() {

    }

    @GetMapping("info")
    public ResponseEntity<User> getInfo(@ApiIgnore Authentication authentication) {
        return new ResponseEntity<>(User.of(authentication), HttpStatus.OK);
    }
}
