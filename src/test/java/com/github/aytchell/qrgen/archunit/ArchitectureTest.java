package com.github.aytchell.qrgen.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

import java.net.URL;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInAnyPackage;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;

@AnalyzeClasses(packages = "com.github.aytchell.qrgen")
class ArchitectureTest {
    private URL loadResource(String filename) {
        return getClass().getResource(filename);
    }

    @ArchTest
    void packageDependenciesAreOk(JavaClasses tsClasses) {
        final URL diagram = loadResource("package-dependencies.puml");
        classes()
                .should(
                        adhereToPlantUmlDiagram(
                                diagram,
                                consideringOnlyDependenciesInAnyPackage("com.github.aytchell.qrgen..")
                        )
                ).check(tsClasses);
    }
}
