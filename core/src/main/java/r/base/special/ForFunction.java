/*
 * R : A Computer Language for Statistical Data Analysis
 * Copyright (C) 1995, 1996  Robert Gentleman and Ross Ihaka
 * Copyright (C) 1997--2008  The R Development Core Team
 * Copyright (C) 2003, 2004  The R Foundation
 * Copyright (C) 2010 bedatadriven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package r.base.special;

import r.lang.*;

public class ForFunction extends SpecialFunction {

  @Override
  public String getName() {
    return "for";
  }

  @Override
  public EvalResult apply(Context context, Environment rho, FunctionCall call, PairList _args_unused) {
   PairList args = call.getArguments();
    Symbol symbol = (Symbol) args.getElementAsSEXP(0);
    Vector elements = (Vector) args.getElementAsSEXP(1).evalToExp(context, rho);
    SEXP statement = args.getElementAsSEXP(2);

    for(int i=0; i!=elements.length(); ++i) {
      try {
        rho.setVariable(symbol, elements.getElementAsSEXP(i));
        statement.evaluate(context, rho);
      } catch (BreakException e) {
        break;
      } catch (NextException e) {
        // next iteration
      }
    }
    return EvalResult.NON_PRINTING_NULL;
  }
}