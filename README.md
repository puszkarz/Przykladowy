# Blood Donor

## Overview
Blood Donor is a an Android application for blood donors used to gather locally information about their previous donations, helping to find closest Blood Donation Center and notifying about end of blood regeneration period.

## Detailed description (in Polish)
### Opis
Blood donor to aplikacja dla dawców krwi. Jej podstawową funkcją jest zapisywanie donacji użytkownika.
Aplikacja ma SQLitową bazę danych, w której przechowuje wszystkie wprowadzone informacje (data, typ donacji, ilość, punkt w którym się oddawało krew).
Aplikacja notyfikuje użytkownika, jeżeli od ostatniej donacji minął miesiąc i dawca może po raz kolejny oddać krew.
Użytkownik tworzy swoje konto poprzez wpisanie nicku i podanie grupy krwi (można to potem edytować w ustawieniach).
Na stronie głównej jest wskaźnik pokazujący ile brakuje jeszcze użytkownikowi (w przeliczeniu w zależności od typów donacji)
do zdobycia kolejnej odznaki honorowego dawcy krwi. W action barze jest menu ze wszystkimi działaniami, które można wykonywać w aplikacji.
Można obejrzeć listę donacji i je ewentualnie usuwać. Baza danych zawiera również stacje krwiodawstwa (wpisane jest kilka przykładowych).
W menu można obejrzeć mapę z zaznaczoną swoją lokalizacją i stacjami. Jest też zakładka wypisująca listę wszystkich stacji oraz zakładka
najbliższych stacji wraz z odległością od obecnego miejsca. Stworzyliśmy prostą ikonkę dla naszej aplikacji.

###Pomysły na rozwijanie projektu:
* udostępnianie donacji na Facebooku,
* dopasowanie limitów oddawania krwi pod profil użytkownika,
* dodanie możliwości posiadania wileu kont użytkowników w jednej aplikacji,
* stworzenie prostej zakładki, w której opisane byłyby wszystkie informacje na temat oddawania krwi (ulgi, dyskwalifikacje etc.).