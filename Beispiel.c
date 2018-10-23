#include <stdio.h>
#include <string.h>
#include <stdlib.h>
void extract(char* zeichenkette1, char* zeichenkette2, int
							anfang, int nb);
int eval1(char * ch)
{
	int i;
	int wert1, wert2;
	int lgval2;
	char *val1, *val2;
	char operation;
	int resultat;
	/* Suche des Operators und seiner Position */
	for( i=0 ; *(ch+i) !='+' && *(ch+i) !='-' && *(ch+i) !='*' && 
							*(ch+i) !='/' && *(ch+i) !='\0'; i++)
	{
	}
	/* Fehlerbehandlung */
	if(i==0) /* der erste Operand fehlt */
	{
		printf("Error: kein <wert1>");
		exit(0);
	}
	else if(i==strlen(ch)-1) /* der zweite Operand fehlt */
	{
		printf("Error: kein <wert2>");
		exit(0);
	}
	else if(i==strlen(ch)) /* kein Operator vorhanden */
	{
		printf("Error: kein <operator>");
		exit(0);
	}
	/* Zeichenkette fuer den ersten Operanden */
	val1=(char*) malloc((i+1)*sizeof(char));
	extract(ch,val1,0,i);
	/* Umwandlung der Zeichenkette */
	sscanf(val1,"%d",& wert1);
	/* Erfassung des Operators */
	operation=*(ch+i);
	/* Zeichenkette fuer den zweiten Operanden*/
	lgval2=strlen(ch)-(i+1);
	val2=(char*) malloc((lgval2+1)*sizeof(char));
	extract(ch,val2,i+1,lgval2);
	/* Umwandlung der Zeichenkette */
	sscanf(val2,"%d",&wert2);
	/* Berechnung */
	switch(operation)
	{
		case '+':
			resultat=wert1+wert2;
			break;
		case '-':
			resultat=wert1-wert2;
			break;
		case '*':
			resultat=wert1*wert2;
			break;
		case '/':
			if(wert2 != 0)
			resultat=wert1/wert2;
			else
		{
			resultat=0;
			printf("Error: Division durch 0 nicht moeglich");
			exit(0);
		}
	}
	return resultat;
}
/* Funktion zum Extrahieren einer Unterzeichenkette
	von zeichenkette1 in zeichenkette2 */
void extract(char* zeichenkette1, char* zeichenkette2,
		int anfang, int nb)
{
	int i;
	zeichenkette1= zeichenkette1+anfang;
	i=0;
	while(i<nb)
	{
		* zeichenkette2=* zeichenkette1;
		zeichenkette1++;
		zeichenkette2++;
		i++;
	}
	* zeichenkette2='\n';
}
int main(int argc, char** argv)
{
	int res;
	if(argc!=2)
	{
		printf("Error, Nutzung des Programms : eval1
								<expression>");
	}
	else
	{
		res=eval1(argv[1]);
		printf("Das Ergebnis der Berechnung ist : %d",res);
	}
}
