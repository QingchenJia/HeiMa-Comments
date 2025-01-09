package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.service.BlogCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog-comments")
public class BlogCommentsController {
    @Autowired
    private BlogCommentsService blogCommentsService;
}
