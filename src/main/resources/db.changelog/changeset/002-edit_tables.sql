--liquibase formatted sql

--changeset dholubeu:002_edit_tables

alter table rides add column date_time timestamp;

alter table rides alter column demand_value type decimal(3, 1);