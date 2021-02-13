create table if not exists "Gatunek"
(
    nazwa_gatunku varchar,
    id_gatunku    integer not null,
    constraint gatunek_pk
        primary key (id_gatunku)
);

alter table "Gatunek"
    owner to npbkqkcz;

create table if not exists "Jezyk"
(
    jezyk_mowiony varchar,
    id_jezyk      integer not null,
    constraint jezyk_pk
        primary key (id_jezyk)
);

alter table "Jezyk"
    owner to npbkqkcz;

create table if not exists "Kraj"
(
    id_kraj     integer not null,
    nazwa_kraju varchar,
    constraint kraj_pk
        primary key (id_kraj)
);

alter table "Kraj"
    owner to npbkqkcz;

create table if not exists "Wytwornia"
(
    nazwa_wytworni varchar,
    id_wytwornia   integer not null,
    constraint wytwornia_pk
        primary key (id_wytwornia)
);

alter table "Wytwornia"
    owner to npbkqkcz;

create table if not exists "Rola"
(
    rola    varchar(20),
    id_rola integer not null,
    constraint rola_pk
        primary key (id_rola)
);

alter table "Rola"
    owner to npbkqkcz;

create table if not exists "Uzytkownik"
(
    id_uzytkownik     integer not null,
    nazwa_uzytkownika varchar(30),
    haslo             varchar not null,
    constraint uzytkownik_pk
        primary key (id_uzytkownik)
);

alter table "Uzytkownik"
    owner to npbkqkcz;

create table if not exists "Film"
(
    id_film      varchar(10) not null,
    tytul        varchar     not null,
    rok          integer     not null,
    data_wydania varchar     not null,
    czas_trwania integer,
    opis         varchar,
    constraint film_pk
        primary key (id_film)
);

alter table "Film"
    owner to npbkqkcz;

create table if not exists "Film_gatunek"
(
    id_gatunku integer,
    id_film    varchar(10) not null,
    constraint film_gatunek_pk
        primary key (id_film),
    constraint id_gatunku
        foreign key (id_gatunku) references "Gatunek"
            on update cascade on delete cascade,
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade
);

alter table "Film_gatunek"
    owner to npbkqkcz;

create table if not exists "Film_jezyk"
(
    id_film  varchar(10) not null,
    id_jezyk integer     not null,
    constraint film_jezyk_pk
        primary key (id_film),
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade,
    constraint id_jezyk
        foreign key (id_jezyk) references "Jezyk"
            on update cascade on delete cascade
);

alter table "Film_jezyk"
    owner to npbkqkcz;

create table if not exists "Film_kraj"
(
    id_film varchar(10) not null,
    id_kraj integer,
    constraint film_kraj_pk
        primary key (id_film),
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade,
    constraint id_kraj
        foreign key (id_kraj) references "Kraj"
            on update cascade on delete cascade
);

alter table "Film_kraj"
    owner to npbkqkcz;

create table if not exists "Filmy_wytworni"
(
    id_film      varchar(10) not null,
    id_wytwornia integer,
    constraint filmy_wytworni_pk
        primary key (id_film),
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade,
    constraint id_wytwornia
        foreign key (id_wytwornia) references "Wytwornia"
            on update cascade on delete cascade
);

alter table "Filmy_wytworni"
    owner to npbkqkcz;

create table if not exists "Ocena"
(
    id_film       varchar(10) not null,
    id_uzytkownik integer     not null,
    ocena         integer,
    id_ocena      serial      not null,
    constraint ocena_pk
        primary key (id_ocena),
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade,
    constraint id_uzytkownik
        foreign key (id_uzytkownik) references "Uzytkownik"
            on update cascade on delete cascade
);

alter table "Ocena"
    owner to npbkqkcz;

create unique index if not exists ocena_id_ocena_uindex
    on "Ocena" (id_ocena);

create trigger dodajtrigger
    before insert
    on "Ocena"
    for each row
execute procedure dodaj();

create trigger dodajtrigger2
    before update
    on "Ocena"
    for each row
execute procedure dodaj();

create trigger odejmijtrigger
    before update
    on "Ocena"
    for each row
execute procedure odejmij();

create table if not exists "Osoba"
(
    id_osoba        varchar(15) not null,
    imie_i_nazwisko varchar,
    data_narodzin   date,
    data_smierci    date,
    bio             varchar,
    constraint osoba_1_pk
        primary key (id_osoba)
);

alter table "Osoba"
    owner to npbkqkcz;

create table if not exists "Rola_osoby"
(
    id_film        varchar(10),
    id_osoba       varchar(15),
    id_rola        integer,
    postac_z_filmu varchar,
    id_rola_osoby  serial not null,
    constraint rola_osoby_pk
        primary key (id_rola_osoby),
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade,
    constraint id_osoba
        foreign key (id_osoba) references "Osoba"
            on update cascade on delete cascade,
    constraint id_rola
        foreign key (id_rola) references "Rola"
            on update cascade on delete cascade
);

alter table "Rola_osoby"
    owner to npbkqkcz;

create table if not exists "Oceny_filmow"
(
    id_film     varchar(10) not null,
    ocena_10    integer,
    ocena_9     integer,
    ocena_8     integer,
    ocena_7     integer,
    ocena_6     integer,
    ocena_5     integer,
    ocena_4     integer,
    ocena_3     integer,
    ocena_2     integer,
    ocena_1     integer,
    srednia     numeric(4, 2),
    liczba_ocen integer,
    constraint oceny_filmow_pk
        primary key (id_film),
    constraint id_film
        foreign key (id_film) references "Film"
            on update cascade on delete cascade
);

alter table "Oceny_filmow"
    owner to npbkqkcz;

create trigger policzsredniaocentrigger
    before update
    on "Oceny_filmow"
    for each row
execute procedure policzsredniaocen();


