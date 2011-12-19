package r.compiler.ir.tac.expressions;

import java.util.Collections;
import java.util.Set;

import r.lang.Context;


public class Constant implements SimpleExpression {

  private Object value;
  
  public Constant(Object value) {
    this.value = value;
  }
  
  public Object getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public Object retrieveValue(Context context, Object[] temps) {
    return value;
  }

  @Override
  public Set<Variable> variables() {
    return Collections.emptySet();
  }

  @Override
  public Constant replaceVariable(Variable name, Variable newName) {
    return this;
  }
}