package org.renjin.repl;

import java.io.IOException;
import java.io.PrintWriter;

import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;

import org.renjin.eval.Context;
import org.renjin.eval.EvalException;
import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.parser.ParseOptions;
import org.renjin.parser.ParseState;
import org.renjin.parser.RLexer;
import org.renjin.parser.RParser;
import org.renjin.parser.RParser.StatusResult;
import org.renjin.primitives.Warning;
import org.renjin.sexp.FunctionCall;
import org.renjin.sexp.Promise;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.Symbol;

import com.google.common.base.Strings;

/**
 * A Read-Eval-Print Loop that uses Jline for 
 * reading lines
 */
public class JlineRepl {

  /**
   * Echo lines to standard out.
   */
  private boolean echo;

  /**
   * Whether to abort evaluation if an error is encountered
   */
  private boolean stopOnError;

  public static void main(String[] args) throws Exception {
    JlineRepl repl = new JlineRepl(SessionBuilder.buildDefault());
    repl.run();
  }
  
  private Context topLevelContext;
  private ConsoleReader reader;

  public JlineRepl(Session session) throws Exception {
    
    if(Strings.nullToEmpty(System.getProperty("os.name")).startsWith("Windows")) {
      // AnsiWindowsTerminal does not work properly in WIndows 7
      // so disabling across the board for now
      reader = new ConsoleReader(System.in, System.out, new UnsupportedTerminal());
    } else {
      reader = new ConsoleReader();
    }

    session.setSessionController(new JlineSessionController(reader.getTerminal()));
    this.topLevelContext = session.getTopLevelContext();

  }

  public JlineRepl(Session session, ConsoleReader reader) throws IOException {
    session.setSessionController(new JlineSessionController(reader.getTerminal()));
    this.topLevelContext = session.getTopLevelContext();
    this.reader = reader;
  }


  public void setEcho(boolean echo) {
    this.echo = echo;
  }

  public boolean isEcho() {
    return echo;
  }

  public boolean isStopOnError() {
    return stopOnError;
  }

  public void setStopOnError(boolean stopOnError) {
    this.stopOnError = stopOnError;
  }

  public void run() throws Exception {

    reader.getTerminal().init();
    try {
      while(readExpression()) { }

    } finally {
      reader.getTerminal().restore();
    }
  }


  private boolean readExpression() throws Exception {

    reader.setPrompt("> ");

    ParseOptions options = new ParseOptions();
    ParseState parseState = new ParseState();
    JlineReader lineReader = new JlineReader(reader);
    lineReader.setEcho(echo);

    RLexer lexer = new RLexer(options, parseState, lineReader);
    if(lexer.isEof()) {
      return false;
    }

    RParser parser = new RParser(options, parseState, lexer);
    while(!parser.parse()) {
      if(lexer.errorEncountered()) {
        reader.getOutput().append("Syntax error at " + lexer.getErrorLocation() + ": " + lexer.getErrorMessage() + "\n");
      }
    }


    SEXP exp = parser.getResult();
    if(parser.getResultStatus() == StatusResult.EOF) {
      return true;
    } else if(exp == null) {
      return true;
    }
    
    // clean up last warnings from any previous run
    clearWarnings();

    try {
      SEXP result = topLevelContext.evaluate(exp, topLevelContext.getGlobalEnvironment());

      if(!topLevelContext.getSession().isInvisible()) {
        topLevelContext.evaluate(FunctionCall.newCall(Symbol.get("print"), Promise.repromise(result)));
      }

      printWarnings();
    } catch(EvalException e) {
      if(stopOnError) {
        throw e;
      }
      reader.getOutput().append(e.getMessage());
      reader.getOutput().append("\n");
    } catch(QuitException e) {
      return false;
    } catch(Exception e) {
      if(stopOnError) {
        throw e;
      }
      e.printStackTrace(new PrintWriter(reader.getOutput()));
    }
    return true;
  }


  private void printWarnings() {
    SEXP warnings = topLevelContext.getEnvironment().getBaseEnvironment().getVariable(Warning.LAST_WARNING);
    if(warnings != Symbol.UNBOUND_VALUE) {
      topLevelContext.evaluate( FunctionCall.newCall(Symbol.get("print.warnings"), warnings),
        topLevelContext.getEnvironment().getBaseEnvironment());
    }
  }

  private void clearWarnings() {
    topLevelContext.getEnvironment().getBaseEnvironment().remove(Warning.LAST_WARNING);
  }

}
