package mangotiger.topcoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Write both the solution and JUnit java files given a ProblemStatement and an output directory.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"StringContatenationInLoop"})
final class Writer {
  private PrintStream out;
  private final File directory;
  private final Problem problem;
  private String tabstop = "";
  private static final String EXTENSION = "java";
  private static final String TAB = "  ";

  Writer(final File directory, final Problem problem) {
    this.directory = directory;
    this.problem = problem;
  }

  public void write() throws FileNotFoundException {
    printSolution();
    printJunit();
  }

  private void printSolution() throws FileNotFoundException {
    open("");
    out.println("/** @author tom_gagnier@yahoo.com */");
    out.println("@SuppressWarnings({\"ClassWithoutToString\", \"ClassWithoutPackageStatement\"})");
    out.println("public class " + problem.getClassname() + " {");
    out.println(ts(1) + "public " + problem.getSignature().replaceAll(" ?([(,])", "$1 final ") + " {");
    out.print(ts(2) + "final " + implName() + " impl = new " + implName() + '(');
    for (int i = 0; i < problem.getParameters().length; ++i) {
      out.print(problem.getParameters()[i].getName());
      out.print(isLastParameter(i) ? ");\n" : ", ");
    }
    out.println(ts(2) + "return impl.result();");
    out.println(ts(1) + '}');
    writeImpl();
    out.println("}");
    out.println();
    out.close();
  }

  private String implName() {return problem.getClassname() + "Impl";}

  private void writeImpl() {
    final int tab = 1;
    out.println(ts(tab) + "static class " + implName() + " {");
    for (int i = 0; i < problem.getParameters().length; ++i) {
      final Parameter p = problem.getParameters()[i];
      out.println(ts(tab + 1) + "final " + p.getType() + ' ' + p.getName() + ';');
    }
    out.println(ts(tab + 1) + problem.getReturnType() + " result;");
    out.println();
    out.print(ts(tab + 1) + problem.getClassname() + "Impl(");
    for (int i = 0; i < problem.getParameters().length; ++i) {
      final Parameter p = problem.getParameters()[i];
      out.print("final " + p.getType() + ' ' + p.getName());
      out.print(isLastParameter(i) ? ") {\n" : ", ");
    }
    for (int i = 0; i < problem.getParameters().length; ++i) {
      final Parameter p = problem.getParameters()[i];
      out.println(ts(tab + 1 + 1) + "this." + p.getName() + " = " + p.getName() + ';');
    }
    out.println(ts(tab + 1) + '}');
    out.println();
    out.println(ts(tab + 1) + problem.getReturnType() + " result() {");
    out.println(ts(tab + 1 + 1) + "return result;");
    out.println(ts(tab + 1) + '}');
    out.println("}");
  }

  private boolean isLastParameter(final int i) {
    return i == problem.getParameters().length - 1;
  }

  private void printJunit() throws FileNotFoundException {
    open("Test");
    out.println("import mangotiger.topcoder.TopCoderTestCase;");
    out.println();
    out.println("/**");
    problem.getStatement().print(out);
    out.println(" *");
    problem.getNotes().print(out);
    out.println(" */");
    out.println("@SuppressWarnings({\"ClassWithoutToString\", \"ClassWithoutPackageStatement\", \"MagicNumber\"})");
    out.println("public class " + problem.getClassname() + "Test extends TopCoderTestCase {");
    out.println(ts(1) + problem.getClassname() + ' ' + var(problem.getClassname()) + " = new " +
                problem.getClassname() + "();");
    final List<Example> examples = problem.getExamples();
    for (Example example : examples) {
      printExample(example);
    }
    out.println();
    out.println("}");
    out.close();
  }

  private void printExample(final Example example) {
    out.println();
    printComments(example);
    out.println(ts(1) + "public void test" + example.getNumber() + "() throws Exception {");
    out.println(ts(2) + "final " + problem.getReturnType() + " expect = " +
                value(problem.getReturnType(), example.getExpect()) + ';');
    final Parameter[] parameters = problem.getParameters();
    printParameterAssignments(parameters, example);
    printActualAssignment(parameters);
    out.println(tabstop + "assertEquals(expect, actual);");
    out.println(ts(1) + '}');
  }

  private void printComments(final Example example) {
    if (example.getComments().end() > 0) {
      out.println(ts(1) + "/**");
      example.getComments().print(out, tabstop);
      out.println(tabstop + " */");
    }
  }

  private void printActualAssignment(final Parameter[] parameters) {
    out.print(tabstop + "final " + problem.getReturnType() + " actual = " + var(problem.getClassname()) + '.' +
              problem.getMethodname() + '(');
    for (int j = 0; j < parameters.length; ++j) {
      out.print(j == 0 ? "" : ", ");
      out.print(parameters[j].getName());
    }
    out.println(");");
  }

  private void printParameterAssignments(final Parameter[] parameters, final Example example) {
    for (int i = 0; i < parameters.length; ++i) {
      out.print(tabstop + "final " + parameters[i] + " = ");
      if (isArray(parameters[i].getType())) {
        out.print("new " + parameters[i].getType());
      }
      out.println(example.getArgs().get(i) + ';');
    }
  }

  private void open(final String postfix) throws FileNotFoundException {
    directory.mkdirs();
    final File file = new File(directory, problem.getClassname() + postfix + '.' + EXTENSION);
    out = new PrintStream(new FileOutputStream(file));
  }

  private String ts(final int ts) {
    final StringBuffer buf = new StringBuffer(ts * TAB.length());
    for (int i = 0; i < ts; ++i) {
      buf.append(TAB);
    }
    tabstop = buf.toString();
    return tabstop;
  }

  private static boolean isArray(final String type) {
    return type.indexOf('[') >= 0;
  }

  private static String var(final String name) {
    return name.substring(0, 1).toLowerCase() + name.substring(1);
  }

  private static String value(final String type, final String value) {
    return isArray(type) ? "new " + type + ' ' + value : value;
  }

  @Override public String toString() {
    return "Writer{out=" + out + ",directory=" + directory + '}';
  }
}