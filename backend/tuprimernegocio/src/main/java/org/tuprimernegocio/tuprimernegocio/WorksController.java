package org.tuprimernegocio.tuprimernegocio;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorksController {
    @RequestMapping("/")
    public String works(){
        return "It works";
    }
}
