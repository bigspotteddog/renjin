/*
 *  R : A Computer Language for Statistical Data Analysis
 *  Copyright (C) 1998-2007    Robert Gentleman, Ross Ihaka 
 *                             and the R Development Core Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, a copy is available at
 *  http://www.r-project.org/Licenses/
 *
 *
 * Memory Allocation (garbage collected) --- INCLUDING S compatibility ---
 */

#ifndef R_EXT_MEMORY_H_
#define R_EXT_MEMORY_H_

#ifndef NO_C_HEADERS
# include <stddef.h> /* for size_t */
#endif

#ifdef  __cplusplus
extern "C" {
#endif

void*	vmaxget(void);
void	vmaxset(const void *);

void	R_gc(void);

// For renjin, we just treat this as malloc as malloc is handled
// by the JVM's garbage collector anyway
extern void *malloc (size_t __size);
#define	R_alloc(numItems, sizeOfItem) malloc((numItems)*(sizeOfItem))
#define S_alloc(numItems, sizeOfItem) malloc(numItems*sizeOfItem);

char*	S_realloc(char *, long, long, int);

#ifdef  __cplusplus
}
#endif

#endif /* R_EXT_MEMORY_H_ */
