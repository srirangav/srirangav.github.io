/* File: scheduler.h
 * Desc: header file for scheduler functions
 * Author: Sriranga Veeraraghavan <srv@cisco.com>
 * Revision: $Id: scheduler.h 1023 2008-11-28 07:31:02Z ranga $
 */

#include <stdio.h>
#include <unistd.h>
#include "projlib.h"
#include "pqops.h"

/* defines for the different types of commands */

#define RUN  'R'
#define KILL 'K'
#define NICE 'N'
#define TOP  'T'

/* global variables */

binary_heap my_heap;
int max_id;

/* function prototypes */

extern void executeCommand(void);
extern void ageProcesses(binary_heap *a);
