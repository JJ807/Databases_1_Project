
CREATE TABLE Rola (
                id_rola INT NOT NULL,
                rola NVARCHAR NOT NULL,
                PRIMARY KEY (id_rola)
);


CREATE TABLE Osoba (
                id_osoba VARCHAR NOT NULL,
                imie_i_nazwisko NVARCHAR(100) NOT NULL,
                data_narodzin DATE NOT NULL,
                data_smierci DATE NOT NULL,
                bio VARCHAR NOT NULL,
                PRIMARY KEY (id_osoba)
);


CREATE TABLE Jezyk (
                id_jezyk INT AUTO_INCREMENT NOT NULL,
                jezyk_mowiony NVARCHAR NOT NULL,
                PRIMARY KEY (id_jezyk)
);


CREATE TABLE Uzytkownik (
                id_uzytkownik INT AUTO_INCREMENT NOT NULL,
                nazwa_uzytkownika NVARCHAR(30) NOT NULL,
                haslo VARCHAR NOT NULL,
                PRIMARY KEY (id_uzytkownik)
);


CREATE TABLE Wytwornia (
                id_wytwornia INT AUTO_INCREMENT NOT NULL,
                nazwa_wytworni NVARCHAR NOT NULL,
                PRIMARY KEY (id_wytwornia)
);


CREATE TABLE Kraj (
                id_kraj INT AUTO_INCREMENT NOT NULL,
                nazwa_kraju NVARCHAR NOT NULL,
                PRIMARY KEY (id_kraj)
);


CREATE TABLE Gatunek (
                id_gatunek INT AUTO_INCREMENT NOT NULL,
                nazwa_gatunku NVARCHAR NOT NULL,
                PRIMARY KEY (id_gatunek)
);


CREATE TABLE Film (
                id_film VARCHAR NOT NULL,
                tytul NVARCHAR NOT NULL,
                rok NUMERIC(4) NOT NULL,
                data_wydania DATE NOT NULL,
                czas_trwania INT NOT NULL,
                opis NVARCHAR NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Oceny_filmow (
                id_film VARCHAR NOT NULL,
                ocena_10 INT NOT NULL,
                ocena_9 INT NOT NULL,
                ocena_8 INT NOT NULL,
                ocena_7 INT NOT NULL,
                ocena_6 INT NOT NULL,
                ocena_5 INT NOT NULL,
                ocena_4 INT NOT NULL,
                ocena_3 INT NOT NULL,
                ocena_2 INT NOT NULL,
                ocena_1 INT NOT NULL,
                srednia NUMERIC(2,4) NOT NULL,
                liczba_ocen INT NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Film_jezyk (
                id_film VARCHAR NOT NULL,
                id_jezyk INT NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Film_kraj (
                id_film VARCHAR NOT NULL,
                id_kraj INT NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Film_gatunek (
                id_film VARCHAR NOT NULL,
                id_gatunek INT NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Filmy_wytworni (
                id_film VARCHAR NOT NULL,
                id_wytwornia INT NOT NULL,
                PRIMARY KEY (id_film)
);


CREATE TABLE Rola_osoby (
                id_rola_osoby INT NOT NULL,
                id_film VARCHAR NOT NULL,
                id_osoba VARCHAR NOT NULL,
                id_rola INT NOT NULL,
                postac_z_filmu VARCHAR(200) NOT NULL,
                PRIMARY KEY (id_rola_osoby)
);


CREATE TABLE Ocena (
                id_ocena INT NOT NULL,
                id_film VARCHAR NOT NULL,
                id_uzytkownik INT NOT NULL,
                ocena INT DEFAULT 0 NOT NULL,
                PRIMARY KEY (id_ocena)
);


ALTER TABLE Rola_osoby ADD CONSTRAINT rola_rola_osoby_fk
FOREIGN KEY (id_rola)
REFERENCES Rola (id_rola)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Rola_osoby ADD CONSTRAINT osoba_rola_osoby_fk
FOREIGN KEY (id_osoba)
REFERENCES Osoba (id_osoba)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Film_jezyk ADD CONSTRAINT jezyk_film_jezyk_fk
FOREIGN KEY (id_jezyk)
REFERENCES Jezyk (id_jezyk)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Ocena ADD CONSTRAINT uzytkownik_ocena_fk
FOREIGN KEY (id_uzytkownik)
REFERENCES Uzytkownik (id_uzytkownik)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Filmy_wytworni ADD CONSTRAINT wytwornia_wytwornia_filmowa_fk
FOREIGN KEY (id_wytwornia)
REFERENCES Wytwornia (id_wytwornia)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Film_kraj ADD CONSTRAINT kraj_film_kraj_fk
FOREIGN KEY (id_kraj)
REFERENCES Kraj (id_kraj)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Film_gatunek ADD CONSTRAINT gatunek_film_gatunek_fk
FOREIGN KEY (id_gatunek)
REFERENCES Gatunek (id_gatunek)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Ocena ADD CONSTRAINT film_ocena_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Rola_osoby ADD CONSTRAINT film_rola_osoby_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Filmy_wytworni ADD CONSTRAINT film_wytwornia_filmowa_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Film_gatunek ADD CONSTRAINT film_film_gatunek_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Film_kraj ADD CONSTRAINT film_film_kraj_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Film_jezyk ADD CONSTRAINT film_film_jezyk_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Oceny_filmow ADD CONSTRAINT film_oceny_filmow_fk
FOREIGN KEY (id_film)
REFERENCES Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
