create table deps.statements
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
	received timestamp not null
);

alter table deps.statements owner to "user";


