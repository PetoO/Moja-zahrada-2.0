Trieda: TPlan
	paintBackground - upravena metoda
	mouseClicked - upravena metoda
	mousePressed - upravena metoda
	mouseReleased - upravena metoda
	mouseDragged - upravena metoda
	kresliMierku - metoda kresli mierku v v pravej a dolnej casti obrazovky, v metroch
	zmenPozadie - vyvola dialogove okno pre zmenu pozadia
	zmenPozadieNa - zmeni premennu poz na nove pozadie, nastavi typ vykreslovania, a pripadne zmeni velkost planika
	nastavKolizie - metoda zisti a nastavi kolizie medzi bodovymi prvkami
	nastavSadenie - nastavi typ sadenia
	zruskoliziu - zrusi koliziu
	pripoj - podla typu sadenia pomaha pouzivatelovi nastavit zarovnanie podla najblizsieho prvku
	otoc - opraveny bug 
 
Trieda: TBod
	TBod - zmeneny konstruktor, pridany typ sadenia
	draw - pridane vykreslovanie hranice, zmeneny sposob vykreslovania podla zadanych suradnic
	isNear - upravena metoda kvoli novemu sposobu vykreslovania

Trieda: TabulkaPrvkov - nova trieda, len dialogove okno v ktorom sa zobrazia prvky ktore sa nachadzaju v planiku
	TabulkaPrvkov - konstruktor,vytvori samotny objekt tabulky, a naplni ju udajmi z planika

Trieda:NewJFrame - pridane potrebne tlacidla, v menu a na ploche, a info panel

Trieda:TCiara 
	getLength - vrati dlzku ciaroveho prvku

Trieda:TPlocha
	getArea - vrati rozlohu plosneho prvku

Trieda:ZmenaPozadia - nova trieda, len dialogove okno pre vyber a nastavenie pozadia

Trieda:JInfo - pridane tlacidlo pre tlac, panel pre zobrazenie poctu prvkov daneho druhu na planiku, panel pre neznasanlive rastliny a panel pre dalsie informacie
	setInfo - doplnena funckia pre zobraznie dalsich informacii a neznasanlivych rastlin