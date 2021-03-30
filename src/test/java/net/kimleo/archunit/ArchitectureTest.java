package net.kimleo.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringOnlyDependenciesInAnyPackage;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static org.junit.Assert.assertNotNull;

public class ArchitectureTest {


    public static final String BASE_PACKAGE = ArchitectureTest.class.getPackageName();


    private static JavaClasses allClasses;

    public String subPackage(String extension) {
        return String.format("%s.%s", BASE_PACKAGE, extension);
    }

    @BeforeClass
    public static void setUp() {
        allClasses = new ClassFileImporter().importPackages(BASE_PACKAGE)
                .that(DescribedPredicate.not(simpleNameEndingWith("Test")));
    }

    @Test
    public void layered_architecture() {

        ArchRule rule = layeredArchitecture()
                .layer("Service").definedBy(subPackage("service"))
                .layer("Persistence").definedBy(subPackage("repository"))

                .whereLayer("Service").mayNotBeAccessedByAnyLayer()
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");


        rule.check(allClasses);
    }

    @Test
    public void layered_architecture_by_plantUML() {
        URL archunitDiagram = getClass().getClassLoader().getResource("archunit.puml");

        assertNotNull(archunitDiagram);

        ArchRule rule = classes().should(adhereToPlantUmlDiagram(archunitDiagram, consideringOnlyDependenciesInAnyPackage(subPackage("."))));
        rule.check(allClasses);
    }
}
