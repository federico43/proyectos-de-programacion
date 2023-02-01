#include <stdio.h>
#include <string.h>

/* 
Problema:
- Se tiene un GYM que requiere un sistema informativo
para capturar los datos de sus clientes

caracteristicas:
-se crea un struct para hacer el tipo de dato que caputure los datos
-Se presenta  que para cada dato, se imprime la pregunta y se captura.
*/
struct direc
{
	char* mm[100];
};
struct string
{
	char m[100];
};

struct datos{
  struct string Nombre[30];
  int edad;
  int cc;
  
  struct string correo[100];
  int movil;
  int c_hijos;
  char direc[35];
  float masa_corporal;
  struct string genero;
  
};

int main(void)
{
	/*
	int r;
	printf("cuantos Usuarios quiere registrar?: ");
	scanf("%i",&r);
	printf("\n%i",r);
	*/
	
	struct datos gym;
	
	printf("direccion: ");
	gets(gym.direc);
	
	printf("nombre:");
	scanf("%s",&gym.Nombre);
	
	printf("edad: ");
	scanf("%i",&gym.edad);
	
	printf("cc:");
	scanf("%i",&gym.cc);
	
	printf("Genero: ");
	scanf("%s",&gym.genero);
	
	printf("correo: ");
	scanf("%s",&gym.correo);
	
	printf("movil: ");
	scanf("%i",&gym.movil);
	
	printf("c_hijos:");
	scanf("%i",&gym.c_hijos);
	
	printf("masa corporal:");
	scanf("%f",&gym.masa_corporal);
	
	
	printf("\n---------\n");
	

	printf("nombre ->>>%s\n",gym.Nombre);
	
	printf("edad ->>>%i\n",gym.edad);
	
	printf("cc ->>> %i\n",gym.cc);
	
	printf("masa_coporal ->>>%f\n",gym.masa_corporal);
	
	printf("genero ->>> %s\n",gym.genero );
	
	printf("correo ->>> %s\n",gym.correo);
	
	printf("movil ->>> %i\n",gym.movil);
	
	printf("c_hijos ->>> %i\n",gym.c_hijos);
	
	printf("direc ->>> %s\n",gym.direc);
			
}

