/* File: scheduler.c
 * Desc: source file for scheduler functions
 * Author: Sriranga Veeraraghavan <srv@cisco.com>
 * Revision: $Id: scheduler.c 1023 2008-11-28 07:31:02Z ranga $
 */

#include "scheduler.h"

int main() {
  node cur_job;

  initialize();

  /* read the max job id */

  if (scanf("%d",&max_id)<1) die("Couldn't read maximum job id");

  /* initalize the heap based on the max id */

  heap_initalize(&my_heap,max_id);

  /* read the first command and keep running as long as there are
   * commands or jobs left to process */

  readCommand();

  do {

    while (cmdletter != '\0' && tick >= cmdtick) {
      executeCommand();
      readCommand();
    }

    /* get the top job from the heap and running */

    cur_job = heap_extract_max(&my_heap);
    
    if (cur_job.sentinel == EMPTY) {

      /* if there is no top job, fastforward to the next command time */

      tick = cmdtick;

    } else {

      /* when a job is found, increase the tick (time has passed),
       * reduce its duration (it has run for a tick) and increase its
       * cpu_usage (it has used cpu) */

      tick++;
      cur_job.duration--;
      cur_job.cpu_usage += CPU_PER_TICK;

      if (cur_job.duration <= 0) {

	/* this job is done, print out top and continue */

	printTop(cur_job.id,cur_job.duration,cur_job.niceness,my_heap.heap_size);

      } else {

	/* reset priority and reinsert it into the heap */
	  
	cur_job.p = newPriority(cur_job.cpu_usage,cur_job.niceness);
	heap_insert(&my_heap,cur_job);

      }

      /* at the appropriate time age each process */

      if (tick % TICKS_UNTIL_AGING == 0) ageProcesses(&my_heap);

    }

  } while (my_heap.heap_size > 0 || cmdletter != '\0');

  /* free the memory occupied by the heap */

  heap_finalize(&my_heap);

  finalize();

  return 0;
}

/* function to execute a command that was read in */

void executeCommand() {
  register int node_num;
  node_ptr top;

  switch(cmdletter) {

  case RUN: /* [tick] RUN [job id] [duration] [niceness] */

    /* check the jobid to make sure that it is smaller than the max
     * job id */

    if (cmdjobid <= max_id) {
	heap_insert(&my_heap,node_create(cmdjobid,newPriority(0,cmdnice),
					 cmdduration,cmdnice,0));
    } else {
      sprintf(messages,"Requested job id %d > max job id %d.",cmdjobid,max_id);
      print_error(messages);
    }

    break;

  case KILL: /* [tick] KILL [job id] */

    /* if the requested id is in the heap, delete it */

    node_num = node_find(my_heap,cmdjobid); 
    heap_delete(&my_heap,node_num);
    break;

  case NICE: /* [tick] NICE [job id] [niceness] */

    /* if the requested node is found in the heap, set its niceness to
     * the given niceness, and then recalculate its priority */

    node_num = node_find(my_heap,cmdjobid);
    my_heap.elements[node_num].niceness = cmdnice;
    heap_increase_key(&my_heap,node_num,
		      newPriority(my_heap.elements[node_num].cpu_usage,
			          cmdnice));
    break;

  case TOP: /* [tick] TOP */

    /* if the heap has at least one item, print out the info about the
     * item at the top, otherwise print out zeros's */

    top = heap_max(&my_heap);
    (top) ?
      printTop(top->id,top->duration,top->niceness,my_heap.heap_size) :
      printTop(0,0,0,0);
    break;
  }
  
}

/* function to age each processes based on its cpu usage */

void ageProcesses(binary_heap *a) { 
  register int i,half_size;

  /* for each process, get a new estimate of its cpu usage and its
   * priority */

  for (i = 1; i<= a->heap_size; i++) {
    (a->elements[i]).cpu_usage = ageCPUusage((a->elements[i]).cpu_usage,
					     (a->elements[i]).niceness);
    (a->elements[i]).p = newPriority((a->elements[i]).cpu_usage,
				     (a->elements[i]).niceness);
  }

  /* at this point the heap will just be a disordered array, so redo
   * the heap, worst case running time O(n). Adapted from Introduction
   * to Algorithms (Cormen, Leiserson, Rivest 1990) page 145 */

   half_size = (a->heap_size)/2; 
   for (i = half_size ; i >= 1 ; i--)
     heapify(a,i); 
}




