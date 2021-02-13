
CREATE TABLE Jezyk (
                id_jezyk INT AUTO_INCREMENT NOT NULL,
                jezyk_mowiony VARCHAR NOT NULL,
                PRIMARY KEY (id_jezyk)
);


CREATE TABLE Uzytkownik (
                id_uzytkownik INT AUTO_INCREMENT NOT NULL,
                nazwa_uzytkownika VARCHAR(30) NOT NULL,
                PRIMARY KEY (id_uzytkownik)
);


CREATE TABLE Wytwornia_filmowa (
                id_wytwornia INT AUTO_INCREMENT NOT NULL,
                nazwa_wytworni VARCHAR NOT NULL,
                PRIMARY KEY (id_wytwornia)
);


CREATE TABLE Kraj (
                id_kraj INT AUTO_INCREMENT NOT NULL,
                nazwa_kraju VARCHAR NOT NULL,
                PRIMARY KEY (id_kraj)
);


CREATE TABLE Gatunek (
                id_gatunek INT AUTO_INCREMENT NOT NULL,
                nazwa_gatunku VARCHAR NOT NULL,
                PRIMARY KEY (id_gatunek)
);


CREATE TABLE Aktor (
                id_aktor INT AUTO_INCREMENT NOT NULL,
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                PRIMARY KEY (id_aktor)
);


CREATE TABLE Rezyser (
                id_rezyser INT AUTO_INCREMENT NOT NULL,
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                PRIMARY KEY (id_rezyser)
);


CREATE TABLE Film (
                id_film INT AUTO_INCREMENT NOT NULL,
                tytul VARCHAR NOT NULL,
                rok NUMERIC(4) NOT NULL,
                data_wydania DATE NOT NULL,
                czas_trwania INT NOT NULL,
                opis VARCHAR NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Ocena (
                id_uzytkownik INT NOT NULL,
                ocena NUMERIC(10),
                id_film INT NOT NULL,
                PRIMARY KEY (id_uzytkownik)
);


CREATE TABLE Osoby_filmu (
                id_film INT NOT NULL,
                id_aktor INT NOT NULL,
                id_rezyser INT NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Szczegoly_filmu (
                id_film INT NOT NULL,
                id_gatunek INT NOT NULL,
                id_kraj INT NOT NULL,
                id_wytwornia INT NOT NULL,
                id_jezyk INT NOT NULL,
                PRIMARY KEY (id_film)
);


ALTER TABLE Szczegoly_filmu ADD CONSTRAINT jezyk_szczegoly_filmu_fk
FOREIGN KEY (id_jezyk)
REFERENCES Jezyk (id_jezyk)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Ocena ADD CONSTRAINT uzytkownik_ocena_fk
FOREIGN KEY (id_uzytkownik)
REFERENCES Uzytkownik (id_uzytkownik)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Szczegoly_filmu ADD CONSTRAINT wytwornia_filmowa_szczegoly_filmu_fk
FOREIGN KEY (id_wytwornia)
REFERENCES Wytwornia_filmowa (id_wytwornia)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Szczegoly_filmu ADD CONSTRAINT kraj_szczegoly_filmu_fk
FOREIGN KEY (id_kraj)
REFERENCES Kraj (id_kraj)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Szczegoly_filmu ADD CONSTRAINT gatunek_szczegoly_filmu_fk
FOREIGN KEY (id_gatunek)
REFERENCES Gatunek (id_gatunek)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Osoby_filmu ADD CONSTRAINT aktor_osoby_filmu_fk
FOREIGN KEY (id_aktor)
REFERENCES Aktor (id_aktor)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Osoby_filmu ADD CONSTRAINT rezyser_osoby_filmu_fk
FOREIGN KEY (id_rezyser)
REFERENCES Rezyser (id_rezyser)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Szczegoly_filmu ADD CONSTRAINT film_szczegoly_filmu_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Osoby_filmu ADD CONSTRAINT film_osoby_filmu_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Ocena ADD CONSTRAINT film_ocena_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
