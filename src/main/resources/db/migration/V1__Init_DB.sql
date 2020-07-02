create table chapter
(
    id           bigint not null auto_increment,
    chapter_name varchar(255),
    content      varchar(255),
    image_id     bigint,
    post_id      bigint,
    primary key (id)
) engine = InnoDB;
create table comment
(
    comment_id  bigint not null auto_increment,
    body        varchar(255),
    create_date varchar(255),
    user_id     bigint,
    primary key (comment_id)
) engine = InnoDB;
create table genre
(
    id         bigint not null auto_increment,
    genre_name varchar(255),
    primary key (id)
) engine = InnoDB;
create table genre_post
(
    post_id  bigint not null,
    genre_id bigint not null,
    primary key (genre_id, post_id)
) engine = InnoDB;
create table image
(
    id   bigint not null auto_increment,
    name varchar(255),
    path varchar(255),
    primary key (id)
) engine = InnoDB;
create table post
(
    id            bigint not null auto_increment,
    comment       varchar(255),
    rating        integer,
    short_content varchar(255),
    title         varchar(255),
    user_id       bigint,
    primary key (id)
) engine = InnoDB;
create table roles
(
    id   integer not null auto_increment,
    name varchar(20),
    primary key (id)
) engine = InnoDB;
create table user
(
    id              bigint not null auto_increment,
    activation_code varchar(255),
    active          bit    not null,
    email           varchar(255),
    password        varchar(255),
    username        varchar(255),
    avatar_id       bigint,
    primary key (id)
) engine = InnoDB;
create table user_roles
(
    user_id bigint  not null,
    role_id integer not null,
    primary key (user_id, role_id)
) engine = InnoDB;
alter table user
    add constraint UKsb8bbouer5wak8vyiiy4pf2bx unique (username);
alter table user
    add constraint UKob8kqyqqgmefl0aco34akdtpe unique (email);
alter table chapter
    add constraint FK9yr80gn38bp788asrujqng5k2 foreign key (image_id) references image (id);
alter table chapter
    add constraint FKr16n03cf36c8044xbptedy6q1 foreign key (post_id) references post (id);
alter table comment
    add constraint FK8kcum44fvpupyw6f5baccx25c foreign key (user_id) references user (id);
alter table genre_post
    add constraint FK7yuy8jufqcxm4ntvr2b1mcvlp foreign key (genre_id) references genre (id);
alter table genre_post
    add constraint FKrkcoxpbxv535ar1oxv2xht7au foreign key (post_id) references post (id);
alter table post
    add constraint FK72mt33dhhs48hf9gcqrq4fxte foreign key (user_id) references user (id);
alter table user
    add constraint FK463dlh5j2p2mnn5jxcmtjre0e foreign key (avatar_id) references image (id);
alter table user_roles
    add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles (id);
alter table user_roles
    add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);
