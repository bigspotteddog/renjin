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

package r.lang;

/**
 * Root interface for "atomic" R vectors.
 *
 * {@code AtomicVector}s, in contrast to the {@code ListVector},
 * are collections of primitive elements of the same type.
 * 
 */
public interface AtomicVector extends Vector {

  /**
   *
   * @param index zero-based index
   * @return the element at {@code index} as an {@code Object},
   * boxing Java-language primitives if necessary.
   */
  Object getElementAsObject(int index);

  /**
   *
   * @return true if the vector contains any {@code NA} values
   */
  boolean containsNA();


  /**
   * @param vector an {@code AtomicVector}
   * @param vectorIndex an index of {@code vector}
   * @return the index of the first element in this vector that equals
   * the element at {@code vectorIndex} in {@code vector}, or -1 if no such element
   * can be found
   */
  int indexOf(AtomicVector vector, int vectorIndex);

  /**
   * @param vector an {@code AtomicVector }
   * @param vectorIndex an index of {@code vector}
   * @return true if this vector contains an element equal to the
   * the element at {@code vectorIndex} in {@code vector}
   */
  boolean contains(AtomicVector vector, int vectorIndex);

  /***
   * @return the index of the first NA element.
   */
  int indexOfNA();

}