package fil.iagl.opl.rendu.two.processors;

import java.util.stream.Collectors;

import org.fest.assertions.Assertions;
import org.junit.Test;

import fil.iagl.opl.rendu.two.insert.Insertion;
import fil.iagl.opl.rendu.two.insert.impl.MethodInsert;
import fil.iagl.opl.rendu.two.tools.ContainsSameElementFilter;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.NameFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodInsertTest {
  @Test
  public void instrumentMethodTest() throws Exception {
    Launcher l = new Launcher();

    l.addInputResource("src/test/java");
    l.buildModel();

    CtClass<?> sample = (CtClass<?>) l.getFactory().Package().getRootPackage().getElements(new NameFilter<>("MethodSample")).get(0);

    Integer nbMethod = 1;
    Integer nbStatementToInsert = 1;
    Insertion insertionStrategy = new MethodInsert();
    CtStatement statementToInsert = l.getFactory().Code().createCodeSnippetStatement("TO BE INSERT");
    Assertions.assertThat(
      sample.getElements(new TypeFilter<CtElement>(CtElement.class))
        .stream().filter(insertionStrategy::match).collect(Collectors.toList()))
      .hasSize(nbMethod);

    sample.getElements(new TypeFilter<CtElement>(CtElement.class))
      .stream().filter(insertionStrategy::match).forEach(element -> insertionStrategy.apply(element, statementToInsert));

    System.out.println(sample);
    Assertions.assertThat(
      sample.getElements(new ContainsSameElementFilter(statementToInsert)))
      .hasSize(nbStatementToInsert);
  }
}