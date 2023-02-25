package org.example.controller;

import org.example.controller.component.ExampleComponent;

import java.util.Date;

public class ExampleController {

    ExampleComponent exampleComponent;

    public String get(String body) {
        return exampleComponent.get(body);
    }

    public String post(String body) {
        return new Date().toString();
    }
}
