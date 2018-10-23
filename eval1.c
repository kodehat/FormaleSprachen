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
