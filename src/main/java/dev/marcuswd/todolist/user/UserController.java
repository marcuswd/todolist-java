package dev.marcuswd.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Os modificadores podem ser:
 * public
 * private
 * protectd
 **/

// Depois vem o tipo (que no caso foi uma class)
@RestController
@RequestMapping("/users")
public class UserController {

  /**
   * Tipos de Retorno
   * String (texto)
   * Interger (int) números interios
   * Double (double) Números 0.000
   * Float (float) Números 0.000
   * char
   * Date (datas)
   * void (quando não tem um retorno, só uma lógica sendo executada)
   **/
  /* O request body quer dizer que a informação vem no body da requisição */
  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel) {
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if (user != null) {
      System.out.println("Usuário já existe");
      // Caso o usuário já existe precisa retornar a mensagem de erro e o status code
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
    }

    var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(passwordHashred);

    var userCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(HttpStatus.OK).body(userCreated);
  }
}
