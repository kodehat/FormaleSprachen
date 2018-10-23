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
