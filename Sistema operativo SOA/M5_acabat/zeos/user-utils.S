#include <asm.h>

ENTRY(syscall_sysenter)
	push %ecx
	push %edx
	push $SYSENTER_RETURN
	push %ebp
	mov %esp, %ebp
	sysenter
ENTRY(SYSENTER_RETURN)
	pop %ebp
	pop %edx
	pop %edx
	pop %ecx
	ret

/* int write(int fd, char *buffer, int size) */
ENTRY(write)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $4, %eax
	movl 0x8(%ebp), %ebx;	//fd
	movl 0xC(%ebp), %ecx;	//buffer
	movl 0x10(%ebp), %edx;	//size
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -> 
	popl %ebp
	ret

/* Common code for negative return */
nok:
	neg %eax
	mov %eax, errno
	mov $-1, %eax
	popl %ebp
	ret

/* int gettime() */
ENTRY(gettime)
	pushl %ebp
	movl %esp, %ebp
	movl $10, %eax
	call syscall_sysenter
	popl %ebp
	ret

/* int getpid() */
ENTRY(getpid)
	pushl %ebp
	movl %esp, %ebp
	movl $20, %eax
	call syscall_sysenter
	popl %ebp
	ret

/* int fork() */
ENTRY(fork)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	pushl %esi;
	movl $5, %eax
	movl 0x8(%ebp), %ebx; //what 
	movl $0, %ecx; //func
	movl $0 ,%edx; //param
	movl $0,%esi; //stack_size
	call syscall_sysenter
	popl %esi
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -> 
	popl %ebp
	ret

/* void exit() */
ENTRY(exit)
	pushl %ebp
	movl %esp, %ebp
	movl $1, %eax
	call syscall_sysenter
	popl %ebp
	ret

/* int yield() */
ENTRY(yield)
	pushl %ebp
	movl %esp, %ebp
	movl $13, %eax
	call syscall_sysenter
	popl %ebp
	ret

/* int get_stats(int pid, struct stats *st) */
ENTRY(get_stats)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $35, %eax
	movl 0x8(%ebp), %ebx;	//pid
	movl 0xC(%ebp), %ecx;	//st
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -->
	popl %ebp
	ret

/* int GetKeyboardState(char *keyboard) */
ENTRY(GetKeyboardState)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $6, %eax
	movl 0x8(%ebp), %ebx;	//keyboard 
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -> 
	popl %ebp
	ret

ENTRY(StartScreen)
	pushl %ebp
	movl %esp, %ebp
	movl $7, %eax
	call syscall_sysenter
	popl %ebp
	ret

ENTRY(pause)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $8, %eax
	movl 0x8(%ebp), %ebx;	//milisegons 
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -> 
	popl %ebp
	ret

/* int pt_create(int what, void *(*func)(void*), void *param, int stack_size) */
ENTRY(pt_create)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	pushl %esi;
	movl $5, %eax
	movl $0, %ebx; //what 
	movl 0x8(%ebp), %ecx; //func
	movl 0xC(%ebp),%edx; //param
	movl 0x10(%ebp),%esi; //stack_size
	call syscall_sysenter
	popl %esi
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -> 
	popl %ebp
	ret

ENTRY(terminatethread)
	pushl %ebp
	movl %esp, %ebp
	movl $22, %eax 			
	call syscall_sysenter
	test %eax, %eax
	js nok;					// if (eax < 0) -->
	popl %ebp
	ret


/* int SetPriority(int priority);*/
ENTRY(SetPriority)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $11, %eax
	movl 0x8(%ebp), %ebx;	//priority
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok2	// if (eax < 0) -> 
	popl %ebp
	ret

/* Common code for negative return */
nok2:
	neg %eax
	mov %eax, errno
	mov $-1, %eax
	popl %ebp
	ret

/*int sem_init(int value); */
ENTRY(sem_init)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $14, %eax
	movl 0x8(%ebp), %ebx;	//value
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -->
	popl %ebp
	ret

/*int sem_wait(int sem_id); */
ENTRY(sem_wait)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $15, %eax
	movl 0x8(%ebp), %ebx;	//sem_id
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -->
	popl %ebp
	ret

/*int sem_post(int sem_id); */
ENTRY(sem_post)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $16, %eax
	movl 0x8(%ebp), %ebx;	//sem_id
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -->
	popl %ebp
	ret

/*int sem_destroy(int sem_id); */
ENTRY(sem_destroy)
	pushl %ebp
	movl %esp, %ebp
	pushl %ebx;  // Save EBX, ESI and EDI if modified
	movl $17, %eax
	movl 0x8(%ebp), %ebx;	//sem_id
	call syscall_sysenter
	popl %ebx
	test %eax, %eax
	js nok	// if (eax < 0) -->
	popl %ebp
	ret