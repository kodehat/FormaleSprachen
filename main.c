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
