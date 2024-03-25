--liquibase formatted sql

--changeset dholubeuu:001-create-tables

set schema 'rides_schema';

create table rides
(
    id bigserial,
    address_from varchar(200) not null,
    address_to varchar(200) not null,
    passenger_id bigint not null,
    driver_id bigint,
    payment_method varchar(20) not null,
    destination decimal(10, 1),
    demand_value decimal(1, 1),
    cost decimal(10,1),
    status varchar(30) not null,
    passenger_rating real default 0.0,
    driver_rating real default 0.0,
    promocode varchar(100),
    primary key (id)
);