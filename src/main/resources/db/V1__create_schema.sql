drop table if exists borrower;
drop table if exists loan;

create table borrower
(
    guid       varchar(36)    not null primary key,
    name       varchar(72)    not null,
    age        smallint       null,
    income     decimal(19, 4) not null,
    debt       decimal(19, 4) not null default 0.0,
    delinquent tinyint(1)     not null default 0
);

create table loan
(
    guid          varchar(36)    not null primary key,
    amount        decimal(19, 4) not null,
    term_months   int            not null,
    interest      decimal(19, 4) not null,
    borrower_guid varchar(36)    not null,
    constraint loan_borrower_guid
        foreign key (borrower_guid) references borrower (guid)
);