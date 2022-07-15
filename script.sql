create table product
(
    id    bigserial
        constraint product_pk
            primary key,
    name  varchar(255)      not null,
    price numeric           not null,
    count integer default 0 not null
);

alter table product
    owner to postgres;

create unique index product_name_uindex
    on product (name);

create unique index product_id_uindex
    on product (id);


