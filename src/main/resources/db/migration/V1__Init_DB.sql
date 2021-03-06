create table comment
(
    id           serial not null ,
    created_date timestamptz,
    text         varchar(255),
    post_id      bigint,
    user_id      bigint,
    primary key (id)
);
create table genre
(
    id           serial not null,
    created_date timestamptz,
    description  varchar(255),
    name         varchar(255),
    user_user_id bigint,
    primary key (id)
);
create table genre_posts
(
    genre_id      bigint not null,
    posts_post_id bigint not null
);
create table post
(
    post_id      serial not null,
    created_date timestamptz,
    description  text,
    post_name    varchar(255),
    url          varchar(255),
    vote_count   integer,
    id           bigint,
    user_id      bigint,
    primary key (post_id)
);
create table "user"
(
    user_id         serial not null,
    activation_code varchar(255),
    active          bit    not null,
    created         timestamptz,
    email           varchar(255),
    password        varchar(255),
    role            varchar(255),
    username        varchar(255),
    primary key (user_id)
);
create table vote
(
    vote_id   serial not null,
    vote_type integer,
    post_id   bigint,
    user_id   bigint,
    primary key (vote_id)
);
alter table genre_posts
    add constraint UK_lc2shs9qwlqsr5wx43olokmri unique (posts_post_id);
alter table comment
    add constraint FKs1slvnkuemjsq2kj4h3vhx7i1 foreign key (post_id) references post (post_id);
alter table comment
    add constraint FK8kcum44fvpupyw6f5baccx25c foreign key (user_id) references "user" (user_id);
alter table genre
    add constraint FK49bn86xlosbktxkavu8toj4vr foreign key (user_user_id) references "user" (user_id);
alter table genre_posts
    add constraint FKdhr6i4dohypn5tmgpgwp9tror foreign key (posts_post_id) references post (post_id);
alter table genre_posts
    add constraint FKgbnu2lstxivof6d4rv3nqslm9 foreign key (genre_id) references genre (id);
alter table post
    add constraint FKr0ti0bxr5p8s1456apqjqjl4s foreign key (id) references genre (id);
alter table post
    add constraint FK72mt33dhhs48hf9gcqrq4fxte foreign key (user_id) references "user" (user_id);
alter table vote
    add constraint FKl3c067ewaw5xktl5cjvniv3e9 foreign key (post_id) references post (post_id);
alter table vote
    add constraint FKcsaksoe2iepaj8birrmithwve foreign key (user_id) references "user" (user_id);
