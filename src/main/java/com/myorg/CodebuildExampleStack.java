package com.myorg;

import software.amazon.awscdk.services.codebuild.*;
import software.amazon.awscdk.services.iam.AnyPrincipal;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;


public class CodebuildExampleStack extends Stack {
    public CodebuildExampleStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CodebuildExampleStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var gitHubSource = Source.gitHub(GitHubSourceProps.builder()
                .owner("martinKindall")
                .repo("aws_spring_cloud_sandbox")
                .webhook(true)
                .webhookFilters(List.of(FilterGroup.inEventOf(EventAction.PUSH).andBranchIs("main").andCommitMessageIs(".*version.*")))
                .build());

        var build = Project.Builder.create(this, "MyProject")
                .environment(BuildEnvironment.builder()
                        .computeType(ComputeType.SMALL)
                        .buildImage(LinuxBuildImage.fromCodeBuildImageId("aws/codebuild/standard:5.0"))
                        .build())
                .source(gitHubSource)
                .build();

        PolicyStatement policyStatement = PolicyStatement.Builder.create()
                .actions(List.of(
                        "ecr:BatchCheckLayerAvailability",
                        "ecr:CompleteLayerUpload",
                        "ecr:GetAuthorizationToken",
                        "ecr:InitiateLayerUpload",
                        "ecr:PutImage",
                        "ecr:UploadLayerPart"
                ))
                .resources(List.of("*"))
                .effect(Effect.ALLOW)
                .build();

        build.addToRolePolicy(policyStatement);
    }
}
