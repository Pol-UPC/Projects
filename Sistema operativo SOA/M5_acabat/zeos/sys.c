/*
 * sys.c - Syscalls implementation
 */
#include <devices.h>

#include <utils.h>

#include <io.h>

#include <mm.h>

#include <mm_address.h>

#include <sched.h>

#include <p_stats.h>

#include <errno.h>

#include <interrupt.h>

#define LECTURA 0
#define ESCRIPTURA 1
#define CLONE_PROCESS 1
#define CLONE_THREAD 0
#define KERNEL_STACK_SIZE	1024
#define SEM_MAX 10
#define PROC_MAX 15

#define SHARED_LOG_START (USER_FIRST_PAGE + NUM_PAG_CODE + NUM_PAG_DATA + 1) 


extern char Keyboard_state [];
void * get_ebp();
extern struct list_head sleepqueue;
int global_TID = 1000;
int global_sem = 0;

int check_fd(int fd, int permissions)
{
  if (fd!=1) return -EBADF; 
  if (permissions!=ESCRIPTURA) return -EACCES; 
  return 0;
}

void user_to_system(void)
{
  update_stats(&(current()->p_stats.user_ticks), &(current()->p_stats.elapsed_total_ticks));
}

void system_to_user(void)
{
  update_stats(&(current()->p_stats.system_ticks), &(current()->p_stats.elapsed_total_ticks));
}

int sys_ni_syscall()
{
	return -ENOSYS; 
}

typedef struct { //Struct de semafors
  int count;
  int alive; //info sobre el sem concret
  struct list_head blocked;

} sem_t;

static sem_t sem_table[PROC_MAX][SEM_MAX]; // llista de llistes
static char  row_used[PROC_MAX]; // 0 = fila libre, 1 = ocupada

int comprobacions(int id,int id_fila, int new){

  if (id < 0 || id >= SEM_MAX)return -EINVAL;     
  if (new) {
        if (sem_table[id_fila][id].alive)
            return -1;
    } else {
        if (!sem_table[id_fila][id].alive)
            return -1;
    }
    return 0;
}
static int busca_fila_lliure(struct task_struct * p){

   for (int r = 0; r < PROC_MAX; ++r) {
        if (!row_used[r]) {
            row_used[r] = 1;
            p->id_sem  = r;
            p->sem_alive_cnt = 0;
            //Netejo la fila 
            for (int c = 0; c < SEM_MAX; ++c) {
                sem_table[r][c].alive = 0;
                INIT_LIST_HEAD(&sem_table[r][c].blocked);
            }
            return 0;
        }
    }
    return -1;  
}

int sys_sem_init(int value)
{
  int id;
  struct task_struct *s = current();
  int id_fila = s->id_sem;

  if(id_fila == -1){
    if(busca_fila_lliure(s) <0)return -ENOSPC; //El proces no te una llista de sem assignada
  }
  id_fila = s->id_sem;

    for(id = 0; id < SEM_MAX;++id){
        if(comprobacions(id,id_fila,1) == 0){
        sem_table[id_fila][id].count  = value;
        INIT_LIST_HEAD(&sem_table[id_fila][id].blocked);
        sem_table[id_fila][id].alive   = 1;
        s->sem_alive_cnt++; 
        return id;
      }
  
}
  return -1;
}

int sys_sem_wait(int sem_id)
{
  struct task_struct *s = current();
  int id_fila = s->id_sem;
  if (comprobacions(sem_id, id_fila, 0) != 0) return -1;

  sem_table[id_fila][sem_id].count--;
  if (sem_table[id_fila][sem_id].count < 0){ //Si no pueden entrar mas
    update_process_state_rr(current(), &sem_table[id_fila][sem_id].blocked);
    sched_next_rr(); //otro se planifica
  }

  return 0;
}

int sys_sem_post(int sem_id)
{
  struct task_struct *s = current();
  int id_fila = s->id_sem;
  if(comprobacions(sem_id,id_fila,0) != 0)return -1;

  sem_table[id_fila][sem_id].count++;
  if(sem_table[id_fila][sem_id].count <= 0){ // Si queda gente bloqueada 
    struct list_head *t = list_first(&sem_table[id_fila][sem_id].blocked);//Saco al primero de mis compañeros bloqueados

    struct task_struct *s = list_head_to_task_struct(t);
    update_process_state_rr(s, &readyqueue);
   
  }
  return 0;
}


int sys_sem_destroy(int sem_id)
{
  struct task_struct *s = current();
  int id_fila = s->id_sem;
  if(comprobacions(sem_id,id_fila,0) != 0)return -1;

   while (!list_empty(&sem_table[id_fila][sem_id].blocked)){//Buido tots els bloquejats
    struct list_head * t = list_first(&sem_table[id_fila][sem_id].blocked);
    list_del(t);
    struct task_struct *s = list_head_to_task_struct(t);
    s->dead = 1;
    list_add_tail(t, &readyqueue);
   }
   sem_table[id_fila][sem_id].alive = 0;
   s->sem_alive_cnt--;            

  if (s->sem_alive_cnt == 0) {       
      row_used[id_fila] = 0;         
      s->id_sem         = -1; 
  }        
      return 0;
}

int ret_from_fork()
{
  return 0;
}

int global_PID=1000;



#define CLONE_PROCESS 1
#define CLONE_THREAD  0

extern int global_PID;
extern int global_TID;

/* tu sys_fork() existente */
int sys_fork(void);

int sys_terminatethread(void)
{
    struct task_struct *p = current();
    page_table_entry *pt = get_PT(p);
    unsigned int i, frame;

    /* recupera inicio y páginas de la TCB */
    unsigned int start = p->ustack_start;
    unsigned int pages = p->ustack_pages;

    /* recorre cada página lógica de la pila de usuario */
    for (i = 0; i < pages; ++i) {
        frame = get_frame(pt, start + i);
        if (frame) {
            free_frame(frame);
            del_ss_pag(pt, start + i);
        }
    }

    /* fuerza flush de TLB para que no quede mapeo stale */
    set_cr3(get_DIR(p));

    /* ahora puedes liberar la TCB/hilo */
    list_add_tail(&p->list, &freequeue);

    /* y saltar al siguiente */
    sched_next_rr();
    return 0; /* no llega aquí */
}



int sys_set_priority(int priority){

  if(priority < 0)return -1;

  struct task_struct *p = current();
  p->priority = priority;
  return 1;
}


int sys_clone(int what, void *(*func)(void*), void *param, int stack_size)
{

    //Comú en threads i procesos  
    struct list_head *lhcurrent = NULL;
    union task_union *uchild;
    if (list_empty(&freequeue)) return -ENOMEM;
    lhcurrent=list_first(&freequeue);
    list_del(lhcurrent); 
    uchild=(union task_union*)list_head_to_task_struct(lhcurrent);
    uchild->task.screen_log = NULL;
    copy_data(current(), uchild, sizeof(union task_union));
    uchild->task.screen_log = NULL;
    int paginas_reserva, inici;
    page_table_entry *process_PT;
    unsigned long *bottom_user;
    uchild->task.sleep_ticks=0;

    if(what == CLONE_PROCESS){
      allocate_DIR((struct task_struct*)uchild);
      process_PT = get_PT(&uchild->task);
      paginas_reserva = NUM_PAG_DATA;
      inici = PAG_LOG_INIT_DATA;
    }

    if(what == CLONE_THREAD){

      int num_pages = (stack_size + PAGE_SIZE - 1) / PAGE_SIZE;
      int first_page = -1, consecutive = 0;
      process_PT = get_PT(current());

      // Busca num_pages páginas libres consecutivas…
      for (int i = NUM_PAG_KERNEL + NUM_PAG_CODE + 2*NUM_PAG_DATA; i < TOTAL_PAGES; ++i) {
        if (get_frame(process_PT,i)==0) {
            if (first_page<0) first_page = i;
            if (++consecutive == num_pages) break;
          } else {
            first_page = -1; consecutive = 0;
          }
      }
      if (consecutive < num_pages) return -ENOMEM; 
      paginas_reserva = num_pages;
      inici = first_page;
      bottom_user = (inici + num_pages) << 12;

    }
    int new_ph_pag, pag, i;

    //Alocatem tantes pagines com el tread o el proces necessita
    for (pag=0; pag< paginas_reserva; pag++)
    {
      new_ph_pag=alloc_frame();
      if (new_ph_pag!=-1) 
      {
        set_ss_pag(process_PT, inici +pag, new_ph_pag);
      }
      else 
      {
        for (i=0; i<pag; i++)
        {
          free_frame(get_frame(process_PT, inici+i));
          del_ss_pag(process_PT, inici+i);
        }
        list_add_tail(lhcurrent, &freequeue);
        return -EAGAIN; 
      }
    }



    if(what == CLONE_PROCESS){
      page_table_entry *parent_PT = get_PT(current());
      for (pag=0; pag<NUM_PAG_KERNEL; pag++)
      {
        set_ss_pag(process_PT, pag, get_frame(parent_PT, pag));
      }
      for (pag=0; pag<NUM_PAG_CODE; pag++)
      {
        set_ss_pag(process_PT, PAG_LOG_INIT_CODE+pag, get_frame(parent_PT, PAG_LOG_INIT_CODE+pag));
      }
     
      for (pag=NUM_PAG_KERNEL+NUM_PAG_CODE; pag<NUM_PAG_KERNEL+NUM_PAG_CODE+NUM_PAG_DATA; pag++)
      {

        set_ss_pag(parent_PT, pag+NUM_PAG_DATA, get_frame(process_PT, pag));
        copy_data((void*)(pag<<12), (void*)((pag+NUM_PAG_DATA)<<12), PAGE_SIZE);
        del_ss_pag(parent_PT, pag+NUM_PAG_DATA);
      }

      set_cr3(get_DIR(current()));

      uchild->task.PID=++global_PID;
      uchild->task.state=ST_READY;


      int register_ebp; 
   
      register_ebp = (int) get_ebp();
      register_ebp=(register_ebp - (int)current()) + (int)(uchild);

      uchild->task.register_esp=register_ebp + sizeof(DWord);

      DWord temp_ebp=*(DWord*)register_ebp;

      uchild->task.register_esp-=sizeof(DWord);
      *(DWord*)(uchild->task.register_esp)=(DWord)&ret_from_fork;
      uchild->task.register_esp-=sizeof(DWord);
      *(DWord*)(uchild->task.register_esp)=temp_ebp;


      init_stats(&(uchild->task.p_stats));

      if (current()->screen_log) {
        unsigned int log_pg    = ((unsigned int)current()->screen_log) >> 12;
        int          phys_pg   = get_frame(get_PT(current()), log_pg);
        page_table_entry *pt_c = get_PT(&uchild->task);
        set_ss_pag(pt_c, log_pg, phys_pg);
      }
      uchild->task.state=ST_READY;
      uchild->task.priority = 0;
      list_add_tail(&(uchild->task.list), &readyqueue);
      return uchild->task.PID;
    }
          
      if (what == CLONE_THREAD) {

          uchild->task.ustack_start = inici;
          uchild->task.ustack_pages = paginas_reserva;
      
          unsigned long user_esp, *kstack;
          unsigned long * ustack = bottom_user;
          
          
          ustack[-1] = (unsigned long)param;  
          ustack[-2] = (0);                   
          user_esp = (unsigned long)&ustack[-2];
          
          uchild->stack[KERNEL_STACK_SIZE - 2] = user_esp;
          uchild->stack[KERNEL_STACK_SIZE - 5] = (unsigned long)func;      
          uchild->task.register_esp = &uchild->stack[KERNEL_STACK_SIZE - 18];

          uchild->task.TID   = ++global_TID;
          uchild->task.state = ST_READY;
          uchild->task.priority = 0;
          list_add_tail(&uchild->task.list, &readyqueue);
          return uchild->task.TID;
      }
    return -EINVAL;
}

int sys_GetKeyboardState(char * keyboard){

  if(!access_ok(VERIFY_WRITE, keyboard, NUM_KEYS)) return -1;
  for(int i = 0; i < NUM_KEYS ;++i){
    keyboard[i] = Keyboard_state[i];
  }
  
  return 0;
}

int sys_pause(int ms)
{
  if(ms < 0){
    return -EINVAL;
  }

  int ticks = (ms * 400 + 500) / 1000;

  if(ticks == 0) return 0; //no pause

  struct task_struct *p = current();
  p->sleep_ticks = ticks;

  //bloquejar tasca i sw ctx
  update_process_state_rr(p,&sleepqueue);
  sched_next_rr();
  return 0;

}

void * sys_StartScreen()
{
  
  struct task_struct *p = current();
  int phys_pg, log_pg;

  //Si te pagina assignada
  if (p->screen_log) return (void *)-1;

  phys_pg = alloc_frame();

  //Falla la reserva fisica
  if (phys_pg < 0) return (void *)-1;

  page_table_entry *pt = get_PT(p);

  for (log_pg = PAG_LOG_INIT_DATA + 2*NUM_PAG_DATA; log_pg < TOTAL_PAGES; ++log_pg) {
        if (!(pt[log_pg].bits.present)) break;
    
    }
  //Falla el mapeig 
  set_ss_pag(pt, log_pg, phys_pg);


  set_cr3((get_DIR(p))); //flush de tlb

  p->screen_log =(void*)(log_pg << 12);
  //PAG_LOG_INIT_DATA + NUM_PAG_DATA
 
  return p->screen_log;

}

int sys_getpid()
{
	return current()->PID;
}

int sys_fork(void)
{
  struct list_head *lhcurrent = NULL;
  union task_union *uchild;
  

  if (list_empty(&freequeue)) return -ENOMEM;

  lhcurrent=list_first(&freequeue);
  
  list_del(lhcurrent);
  
  uchild=(union task_union*)list_head_to_task_struct(lhcurrent);
  
  uchild->task.screen_log = NULL;
  

  copy_data(current(), uchild, sizeof(union task_union));

  

  uchild->task.screen_log = NULL;

  allocate_DIR((struct task_struct*)uchild);
  

  int new_ph_pag, pag, i;
  page_table_entry *process_PT = get_PT(&uchild->task);
  for (pag=0; pag<NUM_PAG_DATA; pag++)
  {
    new_ph_pag=alloc_frame();
    if (new_ph_pag!=-1) 
    {
      set_ss_pag(process_PT, PAG_LOG_INIT_DATA+pag, new_ph_pag);
    }
    else 
    {
      for (i=0; i<pag; i++)
      {
        free_frame(get_frame(process_PT, PAG_LOG_INIT_DATA+i));
        del_ss_pag(process_PT, PAG_LOG_INIT_DATA+i);
      }
 
      list_add_tail(lhcurrent, &freequeue);
      

      return -EAGAIN; 
    }
  }

  page_table_entry *parent_PT = get_PT(current());
  for (pag=0; pag<NUM_PAG_KERNEL; pag++)
  {
    set_ss_pag(process_PT, pag, get_frame(parent_PT, pag));
  }
  for (pag=0; pag<NUM_PAG_CODE; pag++)
  {
    set_ss_pag(process_PT, PAG_LOG_INIT_CODE+pag, get_frame(parent_PT, PAG_LOG_INIT_CODE+pag));
  }
 
  for (pag=NUM_PAG_KERNEL+NUM_PAG_CODE; pag<NUM_PAG_KERNEL+NUM_PAG_CODE+NUM_PAG_DATA; pag++)
  {

    set_ss_pag(parent_PT, pag+NUM_PAG_DATA, get_frame(process_PT, pag));
    copy_data((void*)(pag<<12), (void*)((pag+NUM_PAG_DATA)<<12), PAGE_SIZE);
    del_ss_pag(parent_PT, pag+NUM_PAG_DATA);
  }

  set_cr3(get_DIR(current()));



  uchild->task.PID=++global_PID;
  uchild->task.state=ST_READY;

  int register_ebp;	
 
  register_ebp = (int) get_ebp();
  register_ebp=(register_ebp - (int)current()) + (int)(uchild);

  uchild->task.register_esp=register_ebp + sizeof(DWord);

  DWord temp_ebp=*(DWord*)register_ebp;

  uchild->task.register_esp-=sizeof(DWord);
  *(DWord*)(uchild->task.register_esp)=(DWord)&ret_from_fork;
  uchild->task.register_esp-=sizeof(DWord);
  *(DWord*)(uchild->task.register_esp)=temp_ebp;


  init_stats(&(uchild->task.p_stats));

  if (current()->screen_log) {
    unsigned int log_pg    = ((unsigned int)current()->screen_log) >> 12;
    int          phys_pg   = get_frame(get_PT(current()), log_pg);
    page_table_entry *pt_c = get_PT(&uchild->task);
    set_ss_pag(pt_c, log_pg, phys_pg);
  }



  uchild->task.state=ST_READY;
  list_add_tail(&(uchild->task.list), &readyqueue);
  
  return uchild->task.PID;
}

#define TAM_BUFFER 512

int sys_write(int fd, char *buffer, int nbytes) {
  char localbuffer [TAM_BUFFER];
  int bytes_left;
  int ret;

  	if ((ret = check_fd(fd, ESCRIPTURA)))
  		return ret;
  	if (nbytes < 0)
  		return -EINVAL;
  	if (!access_ok(VERIFY_READ, buffer, nbytes))
  		return -EFAULT;
  	
  	bytes_left = nbytes;
  	while (bytes_left > TAM_BUFFER) {
  		copy_from_user(buffer, localbuffer, TAM_BUFFER);
  		ret = sys_write_console(localbuffer, TAM_BUFFER);
  		bytes_left-=ret;
  		buffer+=ret;
  	}
  	if (bytes_left > 0) {
  		copy_from_user(buffer, localbuffer,bytes_left);
  		ret = sys_write_console(localbuffer, bytes_left);
  		bytes_left-=ret;
  	}
  	return (nbytes-bytes_left);
}

extern int zeos_ticks;

int sys_gettime()
{
  return zeos_ticks;
}

void sys_exit()
{  
  int i;

  page_table_entry *process_PT = get_PT(current());

  // Deallocate all the propietary physical pages
  for (i=0; i<NUM_PAG_DATA; i++)
  {
    free_frame(get_frame(process_PT, PAG_LOG_INIT_DATA+i));
    del_ss_pag(process_PT, PAG_LOG_INIT_DATA+i);
  }
  
  /* Free task_struct */
  list_add_tail(&(current()->list), &freequeue);
  
  current()->PID=-1;
  
  /* Restarts execution of the next process */
  sched_next_rr();
}

/* System call to force a task switch */
int sys_yield()
{
  force_task_switch();
  return 0;
}

extern int remaining_quantum;

int sys_get_stats(int pid, struct stats *st)
{
  int i;
  
  if (!access_ok(VERIFY_WRITE, st, sizeof(struct stats))) return -EFAULT; 
  
  if (pid<0) return -EINVAL;
  for (i=0; i<NR_TASKS; i++)
  {
    if (task[i].task.PID==pid)
    {
      task[i].task.p_stats.remaining_ticks=remaining_quantum;
      copy_to_user(&(task[i].task.p_stats), st, sizeof(struct stats));
      return 0;
    }
  }
  return -ESRCH; /*ESRCH */
}




