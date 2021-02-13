create or replace function dodaj() returns trigger
    language plpgsql
as
$$
BEGIN
    RAISE NOTICE 'Nowa ocena: %', NEW.ocena;
    IF NEW.ocena = 1 THEN
        UPDATE "Oceny_filmow" set ocena_1 = ocena_1 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 2 THEN
        UPDATE "Oceny_filmow" set ocena_2 = ocena_2 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 3 THEN
        UPDATE "Oceny_filmow" set ocena_3 = ocena_3 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 4 THEN
        UPDATE "Oceny_filmow" set ocena_4 = ocena_4 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 5 THEN
        UPDATE "Oceny_filmow" set ocena_5 = ocena_5 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 6 THEN
        UPDATE "Oceny_filmow" set ocena_6 = ocena_6 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 7 THEN
        UPDATE "Oceny_filmow" set ocena_7 = ocena_7 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 8 THEN
        UPDATE "Oceny_filmow" set ocena_8 = ocena_8 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 9 THEN
        UPDATE "Oceny_filmow" set ocena_9 = ocena_9 + 1 where id_film = NEW.id_film;
    ELSIF NEW.ocena = 10 THEN
        UPDATE "Oceny_filmow" set ocena_10 = ocena_10 + 1 where id_film = NEW.id_film;
    END IF;

    RETURN NEW;
END;
$$;

alter function dodaj() owner to npbkqkcz;

create or replace function policzsredniaocen() returns trigger
    language plpgsql
as
$$
BEGIN
    NEW.liczba_ocen = NEW.ocena_10 + NEW.ocena_9 + NEW.ocena_8 + NEW.ocena_7 + NEW.ocena_6 +
                      NEW.ocena_5 + NEW.ocena_4 + NEW.ocena_3 + NEW.ocena_2 +
                      NEW.ocena_1;
    IF NEW.liczba_ocen = 0 THEN
        NEW.liczba_ocen = 1;
    END IF;

    NEW.srednia :=
            (SELECT (NEW.ocena_10 * 10 + NEW.ocena_9 * 9 + NEW.ocena_8 * 8 + NEW.ocena_7 * 7 +
                     NEW.ocena_6 * 6 +
                     NEW.ocena_5 * 5 + NEW.ocena_4 * 4 + NEW.ocena_3 * 3 + NEW.ocena_2 * 2 +
                     NEW.ocena_1 * 1) /
                    CAST(NEW.liczba_ocen AS NUMERIC(4, 1))
             FROM "Oceny_filmow"
             where id_film = NEW.id_film);

    RAISE NOTICE 'Srednia nowa: %, Srednia stara: %, id_film: %', NEW.srednia, OLD.srednia, NEW.id_film;
    RAISE NOTICE 'Liczba ocen nowa: %, Liczba ocen stara: %, id_film: %', NEW.liczba_ocen, OLD.liczba_ocen, NEW.id_film;
    RETURN NEW;
END ;
$$;

alter function policzsredniaocen() owner to npbkqkcz;

create or replace function odejmij() returns trigger
    language plpgsql
as
$$
BEGIN
    RAISE NOTICE 'Stara ocena: %', OLD.ocena;
    RAISE NOTICE 'Nowa ocena: %', NEW.ocena;
    IF OLD.ocena = 1 THEN
        UPDATE "Oceny_filmow" set ocena_1 = ocena_1 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 2 THEN
        UPDATE "Oceny_filmow" set ocena_2 = ocena_2 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 3 THEN
        UPDATE "Oceny_filmow" set ocena_3 = ocena_3 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 4 THEN
        UPDATE "Oceny_filmow" set ocena_4 = ocena_4 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 5 THEN
        UPDATE "Oceny_filmow" set ocena_5 = ocena_5 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 6 THEN
        UPDATE "Oceny_filmow" set ocena_6 = ocena_6 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 7 THEN
        UPDATE "Oceny_filmow" set ocena_7 = ocena_7 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 8 THEN
        UPDATE "Oceny_filmow" set ocena_8 = ocena_8 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 9 THEN
        UPDATE "Oceny_filmow" set ocena_9 = ocena_9 - 1 where id_film = NEW.id_film;
    ELSIF OLD.ocena = 10 THEN
        UPDATE "Oceny_filmow" set ocena_10 = ocena_10 - 1 where id_film = NEW.id_film;
    END IF;
    RETURN NEW;
END;
$$;

alter function odejmij() owner to npbkqkcz;


