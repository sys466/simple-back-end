create table docs.users
(
	username text not null
		constraint users_pk
			primary key,
	hash_password text not null,
	full_name text not null
);

alter table docs.users owner to "user";

INSERT INTO docs.users (username, hash_password, full_name)
VALUES ('smithf', '$2y$12$XiS2gld1gviF0/ydLlKAaeJ.aH1Os1DIiGwaHV9Kx7IgEFhG0Vwdy', 'Frank Smith');
-- decrypted password: password

INSERT INTO docs.users (username, hash_password, full_name)
VALUES ('jonesl', '$2y$12$syHOQtd21z4IMYemqoq4U.I1WYhi9.nytmFxKawYaj/5tcfKSon/m', 'Linda Jones');
-- decrypted password: 12345678
