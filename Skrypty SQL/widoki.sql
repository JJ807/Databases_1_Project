create or replace view osoba_info
            (id_osoba, imie_i_nazwisko, data_narodzin, data_smierci, bio, rola, tytul, rok,
             postac_z_filmu) as
SELECT o.id_osoba,
       o.imie_i_nazwisko,
       o.data_narodzin,
       o.data_smierci,
       o.bio,
       r.rola,
       f.tytul,
       f.rok,
       ro.postac_z_filmu
FROM "Osoba" o
         JOIN "Rola_osoby" ro ON o.id_osoba::text = ro.id_osoba::text
         JOIN "Rola" r ON r.id_rola = ro.id_rola
         JOIN "Film" f ON ro.id_film::text = f.id_film::text
ORDER BY o.imie_i_nazwisko;

alter table osoba_info
    owner to npbkqkcz;

create or replace view film_info
            (id_film, tytul, rok, srednia, liczba_ocen, opis, nazwa_gatunku, nazwa_kraju,
             data_wydania, czas_trwania, nazwa_wytworni, jezyk_mowiony, imie_i_nazwisko, id_rola)
as
SELECT f.id_film,
       f.tytul,
       f.rok,
       oc.srednia,
       oc.liczba_ocen,
       f.opis,
       g.nazwa_gatunku,
       k.nazwa_kraju,
       f.data_wydania,
       f.czas_trwania,
       w.nazwa_wytworni,
       j.jezyk_mowiony,
       o.imie_i_nazwisko,
       ro.id_rola
FROM "Film" f
         JOIN "Film_kraj" fk USING (id_film)
         JOIN "Kraj" k USING (id_kraj)
         JOIN "Film_gatunek" fg ON f.id_film::text = fg.id_film::text
         JOIN "Gatunek" g USING (id_gatunku)
         JOIN "Film_jezyk" fj ON f.id_film::text = fj.id_film::text
         JOIN "Jezyk" j USING (id_jezyk)
         JOIN "Filmy_wytworni" fw ON f.id_film::text = fw.id_film::text
         JOIN "Wytwornia" w USING (id_wytwornia)
         LEFT JOIN "Rola_osoby" ro ON
    CASE
        WHEN ro.id_film::text = f.id_film::text AND
             (ro.id_rola = 1 OR ro.id_rola = 2 OR ro.id_rola = 3) AND ro.id_rola IS NOT NULL
            THEN true
        ELSE false
        END
         LEFT JOIN "Osoba" o ON
    CASE
        WHEN ro.id_osoba::text = o.id_osoba::text AND o.id_osoba IS NULL THEN false
        WHEN ro.id_osoba::text = o.id_osoba::text AND o.id_osoba IS NOT NULL THEN true
        ELSE NULL::boolean
        END
         LEFT JOIN "Oceny_filmow" oc ON f.id_film::text = oc.id_film::text;

alter table film_info
    owner to npbkqkcz;


