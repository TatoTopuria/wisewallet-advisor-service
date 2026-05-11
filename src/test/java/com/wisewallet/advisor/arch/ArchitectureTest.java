package com.wisewallet.advisor.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(
        packages = "com.wisewallet.advisor",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class ArchitectureTest {

    @ArchTest
    static final ArchRule layeringRule = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("com.wisewallet.advisor.domain..")
            .layer("Application").definedBy("com.wisewallet.advisor.application..")
            .layer("Infrastructure").definedBy("com.wisewallet.advisor.infrastructure..")
            .layer("Presentation").definedBy("com.wisewallet.advisor.presentation..")
            .whereLayer("Domain").mayNotAccessAnyLayer()
            .whereLayer("Application").mayOnlyAccessLayers("Domain", "Presentation")
            .whereLayer("Infrastructure").mayOnlyAccessLayers("Domain", "Application")
            .whereLayer("Presentation").mayOnlyAccessLayers("Application", "Domain");
}
