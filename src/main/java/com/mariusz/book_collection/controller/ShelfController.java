package com.mariusz.book_collection.controller;

import com.mariusz.book_collection.service.ShelfService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/shelfs")
public class ShelfController {

    private ShelfService shelfService;

}
