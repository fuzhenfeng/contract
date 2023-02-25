package org.example.controller.component;

import org.example.controller.remote.ExampleRemote;

public class ExampleComponent {
    ExampleRemote remote;

    public String get(String body) {
        return remote.get(body);
    }
}
