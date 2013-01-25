package mangotiger.topcoder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A TopCoder problem statement.
 * @author tom_gagnier@yahoo.com
 */
final class Problem {
  private String signature;
  private String classname;
  private String returnType;
  private String methodname;
  private Lines statement;
  private Lines notes;
  private final List<Example> examples = new LinkedList<Example>();

  public String getSignature() {
    return signature;
  }

  public void setSignature(final String signature) {
    this.signature = signature;
  }

  public String getClassname() {
    return classname;
  }

  public void setClassname(final String classname) {
    this.classname = classname;
  }

  public String getReturnType() {
    return returnType;
  }

  public void setReturnType(final String returnType) {
    this.returnType = returnType;
  }

  public String getMethodname() {
    return methodname;
  }

  public void setMethodname(final String methodname) {
    this.methodname = methodname;
  }

  public Lines getStatement() {
    return statement;
  }

  public void setStatement(final Lines statement) {
    this.statement = statement;
  }

  public Lines getNotes() {
    return notes;
  }

  public void setNotes(final Lines notes) {
    this.notes = notes;
  }

  public List<Example> getExamples() {
    return new ArrayList<Example>(examples);
  }

  public Parameter[] getParameters() {
    return Parameter.parseSignature(signature);
  }

  public void add(final Example example) {
    examples.add(example);
  }

  @Override public String toString() {
    return "Problem{signature=" + signature + '}';
  }
}
