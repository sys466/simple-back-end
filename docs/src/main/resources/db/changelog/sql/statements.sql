create table docs.statements
(
	id integer generated by default as identity
		constraint statements_pk
			primary key,
	number text not null,
	type_code text not null,
	department_code text not null,
	status_code text not null,
	passport text not null,
	full_name text not null,
	username text not null
);

alter table docs.statements owner to "user";


