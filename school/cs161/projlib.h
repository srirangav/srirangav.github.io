#ifndef CS161_PROJLIB
#define CS161_PROJLIB

typedef unsigned long long PriType;

extern PriType newPriority(int CPUusage,int nice);
extern int ageCPUusage(int CPUusage,int nice);
extern int compare(PriType a,PriType b);
extern void initialize(void);
extern void finalize(void);

extern PriType strtopri(char *s);
extern char *pritostr(PriType p);
extern void die(char *s);
extern void warn(char *s);
extern void readCommand(void);
extern void printTop(int jobid,int duration,int nice,int heapsize);

extern char cmdletter;
extern int cmdtick,cmdjobid,cmdduration,cmdnice;
extern int tick;

#define CPU_PER_TICK 100
#define TICKS_UNTIL_AGING 100

#endif
