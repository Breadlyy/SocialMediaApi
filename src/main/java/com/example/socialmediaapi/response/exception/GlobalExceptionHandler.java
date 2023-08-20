//package com.example.socialmediaapi.response.exception;
//
//import com.example.socialmediaapi.response.error.DuplicateUserException;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(DuplicateUserException.class)
//    public String handleDuplicateUserException(DuplicateUserException e, Model model) {
//        model.addAttribute("errorMessage", e.getMessage());
//        return "registration"; // Имя вашего шаблона страницы регистрации
//    }
//}
//
