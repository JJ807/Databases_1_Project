
CREATE SEQUENCE filmweb_alfa.jezyk_id_jezyk_seq;

CREATE TABLE filmweb_alfa.Jezyk (
                id_jezyk INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.jezyk_id_jezyk_seq'),
                jezyk_mowiony VARCHAR NOT NULL,
                CONSTRAINT id_jezyk PRIMARY KEY (id_jezyk)
);


ALTER SEQUENCE filmweb_alfa.jezyk_id_jezyk_seq OWNED BY filmweb_alfa.Jezyk.id_jezyk;

CREATE SEQUENCE filmweb_alfa.uzytkownik_id_uzytkownik_seq;

CREATE TABLE filmweb_alfa.Uzytkownik (
                id_uzytkownik INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.uzytkownik_id_uzytkownik_seq'),
                nazwa_uzytkownika VARCHAR(30) NOT NULL,
                CONSTRAINT id_uzytkownik PRIMARY KEY (id_uzytkownik)
);


ALTER SEQUENCE filmweb_alfa.uzytkownik_id_uzytkownik_seq OWNED BY filmweb_alfa.Uzytkownik.id_uzytkownik;

CREATE SEQUENCE filmweb_alfa.wytwornia_filmowa_id_wytwornia_seq;

CREATE TABLE filmweb_alfa.Wytwornia_filmowa (
                id_wytwornia INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.wytwornia_filmowa_id_wytwornia_seq'),
                nazwa_wytworni VARCHAR NOT NULL,
                CONSTRAINT id_wytwornia PRIMARY KEY (id_wytwornia)
);


ALTER SEQUENCE filmweb_alfa.wytwornia_filmowa_id_wytwornia_seq OWNED BY filmweb_alfa.Wytwornia_filmowa.id_wytwornia;

CREATE SEQUENCE filmweb_alfa.kraj_id_kraj_seq;

CREATE TABLE filmweb_alfa.Kraj (
                id_kraj INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.kraj_id_kraj_seq'),
                nazwa_kraju VARCHAR NOT NULL,
                CONSTRAINT id_kraj PRIMARY KEY (id_kraj)
);


ALTER SEQUENCE filmweb_alfa.kraj_id_kraj_seq OWNED BY filmweb_alfa.Kraj.id_kraj;

CREATE SEQUENCE filmweb_alfa.gatunek_id_gatunek_seq;

CREATE TABLE filmweb_alfa.Gatunek (
                id_gatunek INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.gatunek_id_gatunek_seq'),
                nazwa_gatunku VARCHAR NOT NULL,
                CONSTRAINT id_gatunek PRIMARY KEY (id_gatunek)
);


ALTER SEQUENCE filmweb_alfa.gatunek_id_gatunek_seq OWNED BY filmweb_alfa.Gatunek.id_gatunek;

CREATE SEQUENCE filmweb_alfa.aktor_id_aktor_seq;

CREATE TABLE filmweb_alfa.Aktor (
                id_aktor INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.aktor_id_aktor_seq'),
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                CONSTRAINT id_aktor PRIMARY KEY (id_aktor)
);


ALTER SEQUENCE filmweb_alfa.aktor_id_aktor_seq OWNED BY filmweb_alfa.Aktor.id_aktor;

CREATE SEQUENCE filmweb_alfa.rezyser_id_rezyser_seq;

CREATE TABLE filmweb_alfa.Rezyser (
                id_rezyser INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.rezyser_id_rezyser_seq'),
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                CONSTRAINT id_rezyser PRIMARY KEY (id_rezyser)
);


ALTER SEQUENCE filmweb_alfa.rezyser_id_rezyser_seq OWNED BY filmweb_alfa.Rezyser.id_rezyser;

CREATE SEQUENCE filmweb_alfa.film_id_film_seq;

CREATE TABLE filmweb_alfa.Film (
                id_film INTEGER NOT NULL DEFAULT nextval('filmweb_alfa.film_id_film_seq'),
                tytul VARCHAR NOT NULL,
                rok NUMERIC(4) NOT NULL,
                data_wydania DATE NOT NULL,
                czas_trwania INTEGER NOT NULL,
                opis VARCHAR NOT NULL,
                CONSTRAINT id_film PRIMARY KEY (id_film)
);


ALTER SEQUENCE filmweb_alfa.film_id_film_seq OWNED BY filmweb_alfa.Film.id_film;

CREATE TABLE filmweb_alfa.Ocena (
                id_uzytkownik INTEGER NOT NULL,
                ocena NUMERIC(10),
                id_film INTEGER NOT NULL,
                CONSTRAINT id_ocena PRIMARY KEY (id_uzytkownik)
);


CREATE TABLE filmweb_alfa.Osoby_filmu (
                id_film INTEGER NOT NULL,
                id_aktor INTEGER NOT NULL,
                id_rezyser INTEGER NOT NULL,
                CONSTRAINT id_osoby PRIMARY KEY (id_film)
);


CREATE TABLE filmweb_alfa.Szczegoly_filmu (
                id_film INTEGER NOT NULL,
                id_gatunek INTEGER NOT NULL,
                id_kraj INTEGER NOT NULL,
                id_wytwornia INTEGER NOT NULL,
                id_jezyk INTEGER NOT NULL,
                CONSTRAINT id_szczegoly PRIMARY KEY (id_film)
);


ALTER TABLE filmweb_alfa.Szczegoly_filmu ADD CONSTRAINT jezyk_szczegoly_filmu_fk
FOREIGN KEY (id_jezyk)
REFERENCES filmweb_alfa.Jezyk (id_jezyk)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Ocena ADD CONSTRAINT uzytkownik_ocena_fk
FOREIGN KEY (id_uzytkownik)
REFERENCES filmweb_alfa.Uzytkownik (id_uzytkownik)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Szczegoly_filmu ADD CONSTRAINT wytwornia_filmowa_szczegoly_filmu_fk
FOREIGN KEY (id_wytwornia)
REFERENCES filmweb_alfa.Wytwornia_filmowa (id_wytwornia)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Szczegoly_filmu ADD CONSTRAINT kraj_szczegoly_filmu_fk
FOREIGN KEY (id_kraj)
REFERENCES filmweb_alfa.Kraj (id_kraj)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Szczegoly_filmu ADD CONSTRAINT gatunek_szczegoly_filmu_fk
FOREIGN KEY (id_gatunek)
REFERENCES filmweb_alfa.Gatunek (id_gatunek)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Osoby_filmu ADD CONSTRAINT aktor_osoby_filmu_fk
FOREIGN KEY (id_aktor)
REFERENCES filmweb_alfa.Aktor (id_aktor)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Osoby_filmu ADD CONSTRAINT rezyser_osoby_filmu_fk
FOREIGN KEY (id_rezyser)
REFERENCES filmweb_alfa.Rezyser (id_rezyser)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Szczegoly_filmu ADD CONSTRAINT film_szczegoly_filmu_fk
FOREIGN KEY (id_film)
REFERENCES filmweb_alfa.Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Osoby_filmu ADD CONSTRAINT film_osoby_filmu_fk
FOREIGN KEY (id_film)
REFERENCES filmweb_alfa.Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE filmweb_alfa.Ocena ADD CONSTRAINT film_ocena_fk
FOREIGN KEY (id_film)
REFERENCES filmweb_alfa.Film (id_film)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
