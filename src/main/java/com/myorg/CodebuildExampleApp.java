package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;


public class CodebuildExampleApp {
    public static void main(final String[] args) {
        App app = new App();

        new CodebuildExampleStack(app, "CodebuildExampleStack", StackProps.builder()
                .env(Environment.builder()
                        .region("us-east-1")
                        .build())
                .build());

        app.synth();
    }
}

