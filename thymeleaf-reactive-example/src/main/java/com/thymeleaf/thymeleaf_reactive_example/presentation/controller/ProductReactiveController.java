package com.thymeleaf.thymeleaf_reactive_example.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import com.thymeleaf.thymeleaf_reactive_example.presentation.dto.ProductDto;
import com.thymeleaf.thymeleaf_reactive_example.service.interfaces.ICommonService;

/*
 * Thymeleaf only works with @Controller
*/
// @RestController
// @RequestMapping("/api/product")
@Controller
public class ProductReactiveController {

    @Autowired
    private ICommonService<ProductDto> service;

    // @GetMapping
    @RequestMapping("/list")
    // public ResponseEntity<?> findAll(Model model) {
    public String findAll(Model model) {
        // Reactive variable
        IReactiveDataDriverContextVariable reactiveList = new ReactiveDataDriverContextVariable(this.service.findAll(), 1);

        model.addAttribute("products", reactiveList);

        return "list"; // Return html page
        // return ResponseEntity.ok("list"); // Return html page
    } 
    
}
