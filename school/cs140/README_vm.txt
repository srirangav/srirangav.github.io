##############################################################
README for Project 3: Virtual Memory
CS140 Winter 1999
8 March 1998

Sriranga Veeraraghavan <srv@cisco.com> <r1sh11@leland.stanford.edu>
Vaibhav Natu <vnatu@cisco.com> <vnatu1@leland.stanford.edu>
##############################################################

NOTE: In our implemenation all changes required to implement VM in
      nachos are contained in the source files within the userprog
      directory. The vm directory only contains the README and
      TESTCASE files.

PROBLEM 1: Implement TLB

We have implemented a software TLB to cache of virtual page to
physical page translations to provide the illusion of fast access to a
large address address space. The machine's TLB is created at the
startup, and all the entries are marked invalid. This TLB is cleared
on every thread-switch.  Machine's real memory is managed by our
MemoryManager class. The MemoryManager is responsible for maintaining
the machines real memory and updating the per-process page table about
changes.

As page faults occur, the page is looked up in the main memory.  This
page is added to the TLB. In case the TLB is already full, we need to
remove an existing entry. Since the page to be removed is already in
the main memory, the required processing is minimal and involves just
overwriting the entry with the new one. We have implemented a simple
recently used based algorithm.

Each time a page in the TLB is used, it is marked as used, the others
are marked as non in used. When we need to find a page to evict from
the TLB, we check the entries for the first page that does not have
the use field marked and evict it.

Please see the file TESTCASE for the test program.

PROBLEM 2: Implement VM

We implmented an IPT (ipt) in IPTManager class. We also implemented a
swap-table (swapt) in this class. The ipt and swapt are implemented as
hash-tables. The both take in objects, as against pointers. So we
modified the hash table and list code in the lib directory to handle
objects as well. To traverse through the hash-tables we use Iterators.

At any time, the free pages in main memory are sotred in freePageList
in MemoryManager, the actively used pages are in ipt, and rest of of
the pages are in swapt. Each process keeps a pagetbale of it's active
pages, and the machine's TLB always points to some active pages of the
current process. As active pages are swapped to the swap file, they
are removed from the ipt, the owner process's pagetable and if
necessary, from the machine's TLB.

The individual TLB lookups are handled by our MemoryManager class. If
a requested page does not exists the process that requested that page
is terminated.

The swap file is created in the same directory as the nachos
executable and is named "nachos.swap".

The test program for PROBLEM 3 tests this functionality.

PROBLEM 3: Implement Memory Mapped Files

We have implemented an interface to "map" files into the virtual
address space of a program so that the program can then use load and
store instructions directly on the file data.

As per the problem description we have included the following system
calls:

1. Mmap()

   This system call maps a given number of bytes from a given file
   into the virtual address space of the calling process. In our
   implemenation it is assumed that the caller provides a page aligned
   address where the mmap should start.

   We have made some modifications to the AddrSpace class in order to
   support Mmap. The main addition is the introduction of a new
   HashTable m_MMAPEntry, which stores instances of a class called
   MMAPEntry. An MMAPEntry object stores the following pieces of
   information:

   * The file descriptor corresponding to the file that was
     mmap'ed. This is required to correctly unmmap the file.

   * The number of the virtual page in the virutal address space of
     of the process where the mmap starts.

   * The length (in bytes) of the mmap.

   The hash table is stored with the key as the starting virtual
   address of a partcular mmap for easy of access from Munmap.

   The bulk of the work in Mmap is handled by a static function doMmap
   in execption.cc. In order to memory map a file we calculate the
   start and finish pages based on the starting virtual address and
   the length of the file. Although, this allows us to mmap in parts
   of a file, instead of the whole thing, the implemenation of Munmap,
   assumes that the starting virtual address corresponded to the start
   of the file.

   We allocate each virtual page required to mmap the file
   individually. This allows us to detect errors with a higher
   granularity. If each virtual page is allocated correctly, and
   all the information is read in correctly, we update the qaddress
   spaces hash table to indicate that a block of its virtual address
   has been mmap'ed. The return value is the starting virtual page
   number of the mmap. For a given process, this number will be unique
   since no instances of mmap will start on the same virtual page.
  
   doMmap() implements several levels of error checking and will
   return the invalid an SegmentID (-1) if the requested mmap could
   not be performed (either user error, or system error). The errors
   that we check for are:

   1. Check that the starting virtual address is inside the address
      space of the current process.

   2. Check that the starting virtual address is not on the stack
      page of the current process.

   3. Check that none of the virtual pages that are being mmap'ed are
      already mmap'ed.

   4. Make sure that the MemoryManager could allocate a page for each
      virtual page.

   5. Make sure that we could read in the contents of the file and
      write them to memory.

2. Munmap()

   The Munmap system call is responsible for returning the memory used
   to mmap a file to the system. It is also responsible for writing
   any changes made in memory back to the file.

   This system call is passed in a int value, SID, which it uses to 
   index into the per-process hash table of mmap'ed pages. From this
   hash table it determines the length of the memory that needs to
   be written back to disk and the start and ending pages to free.

   When writing changes to a file, it assumes that the beginning
   address of the mmap'ed entry corresponds to position 0 in the
   file. The changes are written page one page at a time.

3. Length()

   The Length system call uses the OpenFile->Length method to
   determine the length of a file in bytes. 

Please see the file TESTCASE for a description of the cntln program
that tests both VM and MMAP.
