BEGIN;

CREATE TABLE public.user_account (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    age INT,
    address TEXT
);

CREATE TABLE public.ticket (
   id SERIAL PRIMARY KEY,
   user_id INTEGER NOT NULL,
   price INTEGER,
   currency TEXT,
   source TEXT,
   destination TEXT,

   CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account(id)
);


INSERT INTO public.user_account (id, name, phone, age, address) VALUES (1, 'Darlene N Greene', '760-366-0220', 48, '2510  Carriage Court');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (2, 'Judy E Francis', '973-878-1568', 28, '2140  Hilltop Haven Drive');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (3, 'Gregory C Polster', '831-731-0446', 42, '4150  Cemetery Street');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (4, 'Larry V Ruelas', '202-359-7317', 64, '3543  Rhode Island Avenue');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (5, 'Tiffany R Byrd', '707-745-0241', 62, '1610  Davis Avenue');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (6, 'Gayle W Demoss', '530-824-3969', 49, '2367  Byers Lane');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (7, 'Arthur G Jones', '415-386-1295', 64, '1260  Roosevelt Street');
INSERT INTO public.user_account (id, name, phone, age, address) VALUES (8, 'Angelia R Dunn', '209-756-5561', 54, '87  Freed Drive');

INSERT INTO public.ticket (id, user_id, price, currency, source, destination) VALUES (1, 8, 2900, 'usd', 'Los Angeles', 'Marysville');
INSERT INTO public.ticket (id, user_id, price, currency, source, destination) VALUES (2, 7, 2900, 'usd', 'Amador City', 'El Centro');
INSERT INTO public.ticket (id, user_id, price, currency, source, destination) VALUES (3, 2, 1499, 'usd', 'Los Angeles', 'Marysville');
INSERT INTO public.ticket (id, user_id, price, currency, source, destination) VALUES (4, 1, 1999, 'usd', 'Los Angeles', 'San Francisco');

COMMIT;
