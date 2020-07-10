create table comment
(
    id           bigint not null auto_increment,
    created_date datetime(6),
    text         varchar(255),
    post_id      bigint,
    user_id      bigint,
    primary key (id)
) engine = InnoDB;
create table genre
(
    id           bigint not null auto_increment,
    created_date datetime(6),
    description  varchar(255),
    name         varchar(255),
    user_id      bigint,
    primary key (id)
) engine = InnoDB;
create table genre_posts
(
    genre_id bigint not null,
    posts_id bigint not null
) engine = InnoDB;
create table post
(
    id           bigint not null auto_increment,
    created_date datetime(6),
    description  longtext,
    post_name    varchar(255),
    url          varchar(255),
    vote_count   integer,
    genre_id     bigint,
    user_id      bigint,
    primary key (id)
) engine = InnoDB;
create table roles
(
    id   integer not null auto_increment,
    name varchar(20),
    primary key (id)
) engine = InnoDB;
create table token
(
    id          bigint not null auto_increment,
    expiry_date datetime(6),
    token       varchar(255),
    user_id     bigint,
    primary key (id)
) engine = InnoDB;
create table user
(
    id              bigint not null auto_increment,
    activation_code varchar(255),
    active          bit    not null,
    created         datetime(6),
    email           varchar(255),
    password        varchar(255),
    username        varchar(255),
    primary key (id)
) engine = InnoDB;
create table user_roles
(
    user_id bigint  not null,
    role_id integer not null,
    primary key (user_id, role_id)
) engine = InnoDB;
create table vote
(
    vote_id   bigint not null auto_increment,
    vote_type integer,
    post_id   bigint,
    user_id   bigint,
    primary key (vote_id)
) engine = InnoDB;
alter table genre_posts
    add constraint UK_sqmglw3qs30pqn9a0c2ejft5s unique (posts_id);
alter table comment
    add constraint FKs1slvnkuemjsq2kj4h3vhx7i1 foreign key (post_id) references post (id);
alter table comment
    add constraint FK8kcum44fvpupyw6f5baccx25c foreign key (user_id) references user (id);
alter table genre
    add constraint FKqd3q4q4ou61x3j8l78vamimf foreign key (user_id) references user (id);
alter table genre_posts
    add constraint FKdqmg6204g24vmsiw6oqkqgiss foreign key (posts_id) references post (id);
alter table genre_posts
    add constraint FKgbnu2lstxivof6d4rv3nqslm9 foreign key (genre_id) references genre (id);
alter table post
    add constraint FKgahuxm7ta53x21emejci46w7f foreign key (genre_id) references genre (id);
alter table post
    add constraint FK72mt33dhhs48hf9gcqrq4fxte foreign key (user_id) references user (id);
alter table token
    add constraint FKe32ek7ixanakfqsdaokm4q9y2 foreign key (user_id) references user (id);
alter table user_roles
    add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles (id);
alter table user_roles
    add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);
alter table vote
    add constraint FKl3c067ewaw5xktl5cjvniv3e9 foreign key (post_id) references post (id);
alter table vote
    add constraint FKcsaksoe2iepaj8birrmithwve foreign key (user_id) references user (id);
