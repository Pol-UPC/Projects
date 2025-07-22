
#include <libc.h>


char buff[24];

static unsigned long fps_last = 0;
static int fps_count = 0;
static int fps = 0;
float get_fps()
{
  float seconds = (float)gettime()/18.0f;
  return fps/seconds;
}

int pid;

extern char Keyboard_state [];

typedef unsigned short Word;
typedef struct { int x, y; } Point;

#define NUM_COLUMNS 80
#define SCANCODE_SPACE  0x39   
#define NUM_ROWS    25
#define SCANCODE_W 0x11   
#define SCANCODE_A 0x1E  
#define SCANCODE_S 0x1F   
#define SCANCODE_D 0x20   
#define WIN_LENGTH 10   
#define NUM_KEYS       98
#define SCANCODE_SPACE 0x39
extern void *StartScreen(void);
void *screen;


static const Word fruit_attr = (Word)('@') | 0xC00;
static unsigned int lcg_seed = 1;  
static int lcg_rand(void) {
  lcg_seed = lcg_seed * 1103515245 + 12345;
  return (lcg_seed >> 16) & 0x7FFF; 
}

void * screen;
int lose = 0;
int win = 0;

Point snake[200];
int snake_len;
Point fruit;
int dir; // 0=up,1=right,2=down,3=left

char kb[98];
static const Word blank_attr   = (Word)(' ') | 0xA00;
static const Word head_attr    = (Word)('O') | 0xB00;
static const Word body_attr    = (Word)('o') | 0xB00;

static int snake_dir = 1;  // 0=up,1=right,2=down,3=left

void print_fps(void *video_mem) {
  Word *vga = (Word*)video_mem;
  Word attr = 0xF00;  // texto blanco sobre negro

  // “FPS:”
  vga[0*NUM_COLUMNS + 2] = (Word)('F') | attr;
  vga[0*NUM_COLUMNS + 3] = (Word)('P') | attr;
  vga[0*NUM_COLUMNS + 4] = (Word)('S') | attr;
  vga[0*NUM_COLUMNS + 5] = (Word)(':') | attr;

  // dos dígitos de fps
  int d1 = (fps / 10) % 10;
  int d2 = fps % 10;
  vga[0*NUM_COLUMNS + 6] = (Word)('0' + d1) | attr;
  vga[0*NUM_COLUMNS + 7] = (Word)('0' + d2) | attr;
}



void print_text(void *video_mem, const char *msg, int row) {
  Word *vga    = (Word*)video_mem;
  int len      = strlen(msg);
  int start    = (NUM_COLUMNS - len) / 2;
  Word attr    = (Word)(' ') | 0xA00;  
  for (int i = 0; i < len; ++i) {
      if (win && lose) vga[row*NUM_COLUMNS + start + i] = (Word)(msg[i]) | 0xC00;
      else if(win) vga[row*NUM_COLUMNS + start + i] = (Word)(msg[i]) | 0xE00;
      else if(lose) vga[row*NUM_COLUMNS + start + i] = (Word)(msg[i]) | 0x400;

  }
}



void reset_snake(void *video_mem) {
  Word *vga = (Word*)video_mem;
  //  limpia pantalla de juego
  for (int i = 0; i < NUM_ROWS; ++i) {
    for (int j = 0; j < NUM_COLUMNS; ++j) {
      if (i == 0 || i == NUM_ROWS-1 ||
          j == 0 || j == NUM_COLUMNS-1)
        vga[i*NUM_COLUMNS + j] = (Word)('#') | 0xD00;
      else
        vga[i*NUM_COLUMNS + j] = blank_attr;
    }
  }
  
  snake_len = 3;
  int start_x = NUM_COLUMNS/2 - 1;
  int start_y = NUM_ROWS/2 + 2;
  for (int i = 0; i < snake_len; ++i) {
    snake[i].x = start_x - i;
    snake[i].y = start_y;
    vga[ start_y*NUM_COLUMNS + (start_x - i) ] =
      (i == 0 ? head_attr : body_attr);
  }
}

void reset_game(void *video_mem) {
  lose = win = 0;
  snake_dir = 1;           // dirección inicial
  reset_snake(video_mem);  // marco, fondo y serpiente
  genera_fruta(video_mem);  // primera manzana
}

void pantalla_splash(void *video_mem) {
    Word *vga = (Word*)video_mem;
    Word border = (Word)('#') | 0xA00;
    Word blank  = (Word)(' ') | 0xA00;
    Word ch0    = (Word)('_') | 0xA00;
    Word ch1    = (Word)('S') | 0xA00;
    Word ch2    = (Word)('N') | 0xA00;
    Word ch3    = (Word)('A') | 0xA00;
    Word ch4    = (Word)('K') | 0xA00;
    Word ch5    = (Word)('E') | 0xA00;

    int row   = NUM_ROWS/2;
    int start = (NUM_COLUMNS-5)/2;

    for (int i = 0; i < NUM_ROWS; ++i) {
      for (int j = 0; j < NUM_COLUMNS; ++j) {
        if (i==0||i==NUM_ROWS-1||j==0||j==NUM_COLUMNS-1)
          vga[i*NUM_COLUMNS+j] = border;
        else
          vga[i*NUM_COLUMNS+j] = blank;
      }
    }
    for (int j = start; j < start+5; ++j) {
      vga[row*NUM_COLUMNS + j] = ch1 + (j-start); 
    }

    for (int j = start; j < start+5; ++j)
      vga[(row+1)*NUM_COLUMNS + j] = ch0;

    print_text(video_mem, "Pulse SPACE!", row+3);
}



static int choca_con_serpiente(Point p) {
  for (int i = 0; i < snake_len; ++i) {
      if (snake[i].x == p.x && snake[i].y == p.y)
          return 1;
  }
  return 0;
}

void genera_fruta(void *video_mem) {
  Word *vga = (Word*)video_mem;
  Point p;
  do {
      p.x = (lcg_rand() % (NUM_COLUMNS - 2)) + 1;
      p.y = (lcg_rand() % (NUM_ROWS    - 2)) + 1;
  } while (choca_con_serpiente(p));
  fruit = p;
  // Dibujarla
  vga[ p.y * NUM_COLUMNS + p.x ] = fruit_attr;
}


void espera_space(void) {
  while (1) {
      if (GetKeyboardState(kb) < 0) {
          continue;   
      }
      
      if(kb[SCANCODE_SPACE]){
        return 1;
      }
      pause(5);
  }

}
void screen_snake_inicial(void *dir) {
  Word *screen    = (Word*)dir;
  Word border     = (Word)('#')   | 0xA00	;  
  Word blank_attr = (Word)(' ')   | 0xA00	; 
  Word ch0        = (Word)('_')   | 0xA00	;  
  Word ch1        = (Word)('S')   | 0xA00	;
  Word ch2        = (Word)('N')   | 0xA00	;
  Word ch3        = (Word)('A')   | 0xA00	;
  Word ch4        = (Word)('K')   | 0xA00	;
  Word ch5        = (Word)('E')   | 0xA00	;

  int row   = NUM_ROWS / 2;
  int start = (NUM_COLUMNS - 5) / 2;  

  for (int i = 0; i < NUM_ROWS; ++i) {
      for (int j = 0; j < NUM_COLUMNS; ++j) {
          // 1) borde perimetral
          if (i == 0 || i == NUM_ROWS-1 || j == 0 || j == NUM_COLUMNS-1) {
              screen[i*NUM_COLUMNS + j] = border;
          }
          // 2) fila central con “_SNAKE_”
          else if (i == row && j >= start && j <= start+4) {
              int k = j - start;
              switch (k) {
                  case 0: screen[i*NUM_COLUMNS+j] = ch1; break;
                  case 1: screen[i*NUM_COLUMNS+j] = ch2; break;
                  case 2: screen[i*NUM_COLUMNS+j] = ch3; break;
                  case 3: screen[i*NUM_COLUMNS+j] = ch4; break;
                  case 4: screen[i*NUM_COLUMNS+j] = ch5; break;
              }
          }
         
          else {
              screen[i*NUM_COLUMNS + j] = blank_attr;
          }
      }
  }

    snake_len = 3;
    int start_x = NUM_COLUMNS/2 - 1;
    int start_y = NUM_ROWS/2 + 2;  

    for (int i = 0; i < snake_len; ++i) {
        snake[i].x = start_x - i;   //  head en [0], cuerpo detrás
        snake[i].y = start_y;
        screen[start_y * NUM_COLUMNS + (start_x - i)] = 
            (i == 0 ? head_attr : body_attr);
    }

    fps_last  = gettime();
    fps_count = fps = 0;
    
}

void actualitza_pantalla(void *video_mem) {
  Word *vga = (Word*)video_mem;
  Point new_head = snake[0];

  fps_count++;
  unsigned long now = gettime();
  if (now - fps_last >= 1000) {
      fps = fps_count;
      fps_count = 0;
      fps_last += 1000;
      print_fps(video_mem);
  }


  switch (snake_dir) {
      case 0: new_head.y--; break;  // arriba
      case 1: new_head.x++; break;  // derecha
      case 2: new_head.y++; break;  // abajo
      case 3: new_head.x--; break;  // izquierda
  }

  if (new_head.x <= 0 || new_head.x >= NUM_COLUMNS-1 ||
      new_head.y <= 0 || new_head.y >= NUM_ROWS-1) {
      lose = 1;
      return;
  }

  int ate = (new_head.x == fruit.x && new_head.y == fruit.y);
  if (ate) {
      if (snake_len < 200) snake_len++;
      if (snake_len >= WIN_LENGTH) {
        win = 1;
        return;   // 
    }

      genera_fruta(video_mem);
  } else {
      Point tail = snake[snake_len - 1];
      vga[ tail.y * NUM_COLUMNS + tail.x ] = blank_attr;
  }

  for (int i = snake_len - 1; i > 0; --i) {
      snake[i] = snake[i - 1];
  }
  snake[0] = new_head;

  vga[ new_head.y * NUM_COLUMNS + new_head.x ] = head_attr;

  for (int i = 1; i < snake_len; ++i) {
      Point p = snake[i];
      vga[ p.y * NUM_COLUMNS + p.x ] = body_attr;
  }

  

  if (snake_dir == 0 || snake_dir == 2) pause(150);
  else pause(100);
}





void pantalla_perdedora(void *dir) {
  Word *screen      = (Word*)dir;
  Word border_attr  = (Word)('#') | 0x702;   
  Word blank_attr   = (Word)(' ') | 0x700;   
  Word ch0          = (Word)('_') | 0xC00;  
  Word chG          = (Word)('G') | 0xC00;
  Word chA          = (Word)('A') | 0xC00;
  Word chM          = (Word)('M') | 0xC00;
  Word chE          = (Word)('E') | 0xC00;
  Word ch_space     = (Word)(' ') | 0xC00;
  Word chO          = (Word)('O') | 0xC00;
  Word chV          = (Word)('V') | 0xC00;
  Word chR          = (Word)('R') | 0xC00;
  Word chEx         = (Word)('!') | 0xC00;

  const int LEN = 10;
  int row   = NUM_ROWS / 2;
  int start = (NUM_COLUMNS - LEN) / 2;

  for (int i = 0; i < NUM_ROWS; ++i) {
      for (int j = 0; j < NUM_COLUMNS; ++j) {
          if (i == 0 || i == NUM_ROWS - 1 ||
              j == 0 || j == NUM_COLUMNS - 1) {
              screen[i*NUM_COLUMNS + j] = border_attr;
          } else {
              screen[i*NUM_COLUMNS + j] = blank_attr;
          }
      }
  }

  for (int j = start; j < start + LEN; ++j) {
      screen[row*NUM_COLUMNS + j] = ch0;
  }

  screen[row*NUM_COLUMNS + (start + 0)] = chG;
  screen[row*NUM_COLUMNS + (start + 1)] = chA;
  screen[row*NUM_COLUMNS + (start + 2)] = chM;
  screen[row*NUM_COLUMNS + (start + 3)] = chE;
  screen[row*NUM_COLUMNS + (start + 4)] = ch_space;
  screen[row*NUM_COLUMNS + (start + 5)] = chO;
  screen[row*NUM_COLUMNS + (start + 6)] = chV;
  screen[row*NUM_COLUMNS + (start + 7)] = chE;
  screen[row*NUM_COLUMNS + (start + 8)] = chR;
  screen[row*NUM_COLUMNS + (start + 9)] = chEx;

  //terminatethread(); si no tinguesim el reset ho fariem
}

void pantalla_juego_inicial(void *dir) {
  Word *vga       = (Word*)dir;
  Word border_attr= (Word)('#') | 0xA00;
  Word blank_attr = (Word)(' ') | 0xA00;

  for (int i = 0; i < NUM_ROWS; ++i) {
      for (int j = 0; j < NUM_COLUMNS; ++j) {
          if (i == 0 || i == NUM_ROWS-1 ||
              j == 0 || j == NUM_COLUMNS-1) {
              vga[i*NUM_COLUMNS + j] = border_attr;
          } else {
              vga[i*NUM_COLUMNS + j] = blank_attr;
          }
      }
  }
}


void pantalla_ganadora(void *dir) {
  Word *screen      = (Word*)dir;
  Word border_attr  = (Word)('#') | 0xF00;   
  Word blank_attr   = (Word)(' ') | 0xF00;   
  Word ch0          = (Word)('_') | 0xE00;   
  Word chW          = (Word)('W') | 0xE00;  
  Word chI          = (Word)('I') | 0xE00;   
  Word chN          = (Word)('N') | 0xE00;   
  Word chEx         = (Word)('!') | 0xE00;   

  const int LEN    = 4;                      // "WIN!"
  int row          = NUM_ROWS / 2;
  int start        = (NUM_COLUMNS - LEN) / 2;

  for (int i = 0; i < NUM_ROWS; ++i) {
      for (int j = 0; j < NUM_COLUMNS; ++j) {
          if (i == 0 || i == NUM_ROWS - 1 ||
              j == 0 || j == NUM_COLUMNS - 1) {
              screen[i*NUM_COLUMNS + j] = border_attr;
          } else {
              screen[i*NUM_COLUMNS + j] = blank_attr;
          }
      }
  }

  for (int j = start; j < start + LEN; ++j) {
      screen[row*NUM_COLUMNS + j] = ch0;
  }

  screen[row*NUM_COLUMNS + (start + 0)] = chW;
  screen[row*NUM_COLUMNS + (start + 1)] = chI;
  screen[row*NUM_COLUMNS + (start + 2)] = chN;
  screen[row*NUM_COLUMNS + (start + 3)] = chEx;

  //terminatethread(); sino reset ho tindriem
}




void pantalla(void* arg) {
  screen_snake_inicial(screen);  // solo dibuja “SNAKE” y texto
  print_text(screen, "Pulse SPACE!", NUM_ROWS/2 + 3);
  
  espera_space();

  while (1) {
      pantalla_juego_inicial(screen);

      reset_snake(screen);

      genera_fruta(screen);

      lose = win = 0;  
      while (!lose && !win) {
          actualitza_pantalla(screen);
      }

      if (lose)    pantalla_perdedora(screen);
      else          pantalla_ganadora(screen);

        print_text(screen, "Pulse SPACE!", NUM_ROWS/2 + 3);
      espera_space();
  }
}


void logic(void* arg) {
  while (1) {
      // 1) Esperar a que empiece la partida
      while (lose || win) {
          pause(20);
      }
      // 2) Bucle de lectura de flechas/WASD mientras dura la partida
      while (!lose && !win) {
          if (GetKeyboardState(kb) == 0) {
              if (kb[SCANCODE_W] && snake_dir != 2)
               snake_dir = 0;
            
              if (kb[SCANCODE_D] && snake_dir != 3)
                snake_dir = 1;
              
              if (kb[SCANCODE_S] && snake_dir != 0)
                snake_dir = 2;
              
              if (kb[SCANCODE_A] && snake_dir != 1)
                snake_dir = 3;
            
          pause(20);
      }
      while (lose || win) {
          pause(20);
      }
  }
}
}

int __attribute__ ((__section__(".text.main")))

  main(void)

{

  int id = sem_init(1); //Inicialitzem semafor
  
  screen = StartScreen();
  int thread_pantalla = pt_create(pantalla,&id, 1024);
  int thread_logico   = pt_create(logic,&id, 1024);

  while(1){

  }

}
