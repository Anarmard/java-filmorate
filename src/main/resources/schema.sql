create table if not exists RATING_MPA
(
    RATING_MPA  INTEGER auto_increment,
    DESCRIPTION CHARACTER VARYING(10) not null,
    constraint RATING_MPA_PK_RATING_MPA
        primary key (RATING_MPA)
);

create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER VARYING(300),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_MPA   INTEGER,
    RATE         INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_FK_TO_RATING_MPA
        foreign key (RATING_MPA) references RATING_MPA
);

create unique index if not exists FILMS_NAME_UNQ
    on FILMS (FILM_NAME);

create table if not exists USERS
(
    USER_ID   INTEGER auto_increment,
    USER_NAME CHARACTER VARYING(100),
    LOGIN     CHARACTER VARYING(50)  not null,
    EMAIL     CHARACTER VARYING(200) not null,
    BIRTHDAY  DATE,
    constraint USERS_PK_KEY_NAME
        primary key (USER_ID)
);

create unique index if not exists USERS_EMAIL_UNQ
    on USERS (EMAIL);

create unique index if not exists USERS_LOGIN_UNQ
    on USERS (LOGIN);

create table if not exists GENRES
(
    GENRE_ID   INTEGER               not null,
    GENRE_NAME CHARACTER VARYING(30) not null,
    constraint GENRES_PK
    primary key (GENRE_ID)
    );

create table if not exists FILMS_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILMS_GENRES_PK
    primary key (FILM_ID),
    constraint FILMS_GENRES_FK_TO_FILMS
    foreign key (FILM_ID) references FILMS,
    constraint FILMS_GENRES_FK_TO_FILMS_GENRES
    foreign key (GENRE_ID) references GENRES
    );


create table if not exists FRIENDLISTS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDLISTS_PK
    primary key (USER_ID),
    constraint FRIENDLISTS_FK_FRIEND_ID
    foreign key (FRIEND_ID) references USERS,
    constraint FRIENDLISTS_FK_USER_ID
    foreign key (USER_ID) references USERS
    );

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_PK
    primary key (FILM_ID),
    constraint LIKES_FK_FILM_ID
    foreign key (FILM_ID) references FILMS,
    constraint LIKES_FK_USER_ID
    foreign key (USER_ID) references USERS
    );
