package io.aleksander.mchat;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "io.aleksander.mchat")
public class LayeredArchitectureTest {
  @ArchTest
  static final ArchRule layerDependencyRulesAreRespected = Architectures.layeredArchitecture()
      .layer("Controllers").definedBy("..controller..")
      .whereLayer("Controllers").mayNotBeAccessedByAnyLayer();
}
