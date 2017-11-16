##############################################################
README for Project 4: File Systems
CS140 Winter 1999
12 March 1998

Sriranga Veeraraghavan <srv@cisco.com> <r1sh11@leland.stanford.edu>
Vaibhav Natu <vnatu@cisco.com> <vnatu1@leland.stanford.edu>
##############################################################

 NOTE: 

 To start using nachos please use the following build procedure:

  1 $ cd <root dir of project>/filesys
  2 $ gmake depend && gmake sun
  3 $ cd ../test
  4 $ gmake sun
  5 $ cp ../filesys/SUN/nachos SUN
  6 $ cd ../filesys
  7 $ ../test/SUN/nachos -f
  8 $ ../test/SUN/nachos

 Step 7 creates the file DISK and formats it. It also creates the
 several directories and copies in some user programs into the nachos
 file system.  You need to be in the filesys directory or the userprog
 directory when running this step (nachos -f) since our implementation
 expects to find the userprogs to load into the filesystem in the
 directory ../test/SUN, instead of '.'.

 To add your own userprogs from UNIX to nachos please modify the method
 FileSystem::Init().

 PROBLEM 1 & 2
 
 We use double level indirection in FileHeader in order to support
 large and extensible files.

 We initialize new file headers using the FileHeader::Initialize().
 This routine creates files whose initial size is 0. We allocate
 Sectors (and if applicable the indirect pointers) during the first
 Write on the file. All blocks held by the file are returned to free
 state when the file is removed.

 Large files are implemented (like UNIX) with double level of
 indirection in FileHeader. The direct blocks are contained in the
 FileHeader itself. FileHeader also contains a pointer to a 'level 1'
 block of pointers. Finally, there is also a 'level 2' pointer to
 additional pointers to data.

 FileHeader also allows for files to be shared, since a file can be
 open by more than one process, FileHeader's are shared among
 processes (the destructors is private so no one can accidentally
 delete a FileHeader). The callers uses the static methods Get(),Put()
 to retrieve and release instances of FileHeader.

 In order to synchronize parallel access to shared FileHeaders, the
 FileHeader contains synchronization locks. We keep the locks in the
 FileHeader can be shared by all users. FileHeader has two instances
 of Read/Write Lock (see thread.cc and thread.h):

 1. FileLock is used for ordinary I/O operations and it is held for
    one I/O call at a time only. It is used by OpenFile().
 2. DirLock is used for directory access. The file creation/deletion
    routines need to more than one single I/O operation, so we
    surround them with DirLock.

 PROBLEM 3

 We have implemented a hierarchical file system that allows nesting
 one directory with one another, (ie bin in /usr is /usr/bin). You can
 move up the directory tree using the .. entry in each directory. In
 the root directory, change to the parent .. directory has no effect.

 When you initalize the nachos file system with nachos -f, it creates
 the disk, formats it and adds several directories and user programs 
 to the disk. The directories that are created are:

   /        -> The root directory of the filesystem.
   /bin     -> This directory stores the system binaries like ls, 
               exit, shell and halt.
   /swapdir -> This directory is used to store the VM swap file 
               nachos.swap
   /usr     -> This directory stores user files
   /usr/bin -> This directory stores user binaries. We use it to
               store some test programs.
   /var     -> This directory stores variable (volatile) files
   /var/tmp -> This directory stores scratch files (like /tmp)

 The directories /usr/bin and /var/tmp are created to demonstrate
 the hierarchical file system and the ability to create and execute
 files from any point within the directory tree. 

 The file nachos.swap is stored in the directory /swapdir in order
 to demonstrate that our VM works with our file system.

 In order to support file manipulation in hierarchical file systems we
 have implemented the following system calls (predefined in
 syscall.h):

   1. Chdir(char *)

      Changes to the current working directory to the specified
      directory. Returns -1 if the working directory could not be
      changed.

   2. Lsdir()

      List the contents of the current working directory (including
      . and ..).

   3. Mkdir(char *)

      Makes the specified directory. If the directory could not be 
      created returns -1.

   4. Remove(char *)

      Removes the specified file.

 In these problems the lack of stdio became a problem for us as we
 heavily relied on stdio for debugging. To aid in debugging and for
 greater flexibility in userprograms we implemented the following new
 system calls (in syscall.h and start.s):

   1. Print(char *) 

   Prints out the given message to STDOUT.

   2. ReadCh()

   Reads a char from STDIN and returns it. User programs can use this
   syscall to read "raw" (char by char) input from the terminal.

   3. Pwd() 

   This syscall prints out the name of the present working directory.
 
 We have also added minor tweaks to the shell (now /bin/shell in
 nachos file system):

   1. Used the Readch and Print syscalls to interact with the user.

   2. Common commands implemented as shell built-ins instead of separate
      programs:

      cd      -> changes to the specified directory
      mkdir   -> makes the specified directory
      rm      -> removes the specified file 
      create  -> creates a given file
      pwd     -> prints out the current working directory
      ls      -> lists the current directories contents (also available as 
                 /bin/ls).

 PROBLEM 4

 We use a disk buffer cache, with synchronous I/O, Asynchronus I/O and
 delayed writes. Asynchronous I/O is implemented using a background
 thread which handles these requests. Blocks in the cache are acquired with
 with the Read()/Get() methods in diskcache.cc. Blocks are released
 from the cache using Release()/WriteDelayed()/WriteAsync().

  Read(sector): three possible cases:
  	- block in cache, free: mark it busy, return to caller.
  	- block in cache, busy: must wait on it, until it gets released.
  	- block not in cache: Evict() is called to evict another non-inuse
  	  buffer, possibly writing it to disk if dirty.
  Get(sector): this method is similar to read: get a block,
      without reading from disk (of course, if the block is already in cache,
      it will be returned. This method is used when a block
      will be written (so we don't need its old contents).

  On WriteDelayed(), the cache manager will mark the block as dirty. A
  write occurs when the block is needed. When a block is Released or
  Written, is put in the front of the list if willReuse is FALSE,
  otherwise at the end. Free blocks are obtained from the front of the
  list.
  
  On WriteAsync(), the cache manager will immediately start an
  asynchronous write of the block. This call is made when the block
  will not be reused soon, so in the case the block will be put at the
  end of the free list. This work is handled by a background thread.

 KNOWN BUGS

 1.  nachos program formats the disk when run with arg "-f" (nachos -f)
     This correctly formats the disk, however after doing this the
     program terminates. After this point you can launch nachos again
     without the "-f" argument. This works fine.
     
 2.  As in our Homework 2, the shell seems to lose a character from
     user input. For example, if your current working directory is /usr/bin,
     and you run "/bin/ls", this fails because the shell tries to run
     "bin/ls" instead of "/bin/ls".

     Workaround: type in one or more leading blanks.
     For example, " /bin/ls" runs correctly from any directory.

 3.  We have not implemented user program's parameter passing using
     stack. Our implementation takes in the entire the string (program
     name & args), and tries to run it. For well-known programs, like
     "cd", we implement parameter passing by looking into the string

     We have had this part incomplete in the previous homework, and
     never got around to completing it. This implementation, though less
     than perfect, clearly demonstrates our working filesystem
     implementation.

 4.  ChDir()/cd do not set the current working directory (cwd) for
     relative paths. For example, if your cwd is /bin, and if you
     execute "cd ../usr/bin", the cwd (as returned by the Pwd system
     call) is set to "../usr/bin" instead of "/usr/bin".
