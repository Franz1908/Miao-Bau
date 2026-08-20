USE miao_bau;

-- ============================================================
-- PULIZIA (rende il file ri-eseguibile)
-- Si svuota in ordine inverso alle dipendenze: prima le tabelle
-- che referenziano, poi quelle referenziate.
-- ============================================================
DELETE FROM order_item;
DELETE FROM orders;
DELETE FROM product;
DELETE FROM category;
DELETE FROM species;

-- Riporta i contatori AUTO_INCREMENT a 1, così gli id ripartono puliti
ALTER TABLE product  AUTO_INCREMENT = 1;
ALTER TABLE category AUTO_INCREMENT = 1;
ALTER TABLE species  AUTO_INCREMENT = 1;

-- ============================================================
-- SPECIE
-- ============================================================
INSERT INTO species (species_name) VALUES
                                       ('Cane'),
                                       ('Gatto');

-- ============================================================
-- CATEGORIE
-- ============================================================
INSERT INTO category (category_name) VALUES
                                         ('Cibo secco'),
                                         ('Cibo umido'),
                                         ('Snack'),
                                         ('Guinzagli'),
                                         ('Giochi'),
                                         ('Cucce'),
                                         ('Ciotole');

-- ============================================================
-- PRODOTTI
-- Nota: category_id e species_id fanno riferimento all'ordine
-- di inserimento sopra.
--   Specie:   1=Cane, 2=Gatto
--   Categoria: 1=Cibo secco, 2=Cibo umido, 3=Snack,
--              4=Guinzagli, 5=Giochi, 6=Cucce, 7=Ciotole
-- IVA 22% (cibo per animali in Italia).
-- I CIBI valorizzano peso/ingredienti; taglia/colore/materiale = NULL.
-- Gli ACCESSORI valorizzano taglia/colore/materiale; peso/ingredienti = NULL.
-- ============================================================
INSERT INTO product
(category_id, species_id, name, description, price, vat,
 on_sale, discount_percentage, image, brand, is_deleted,
 weight, ingredients, size, color, material)
VALUES
-- --- CIBI PER CANE ---
(1, 1, 'Crocchette Adult Pollo 5 kg',
 'Crocchette complete per cani adulti al pollo.',
 24.90, 22.00, FALSE, NULL, 'img/crocchette_pollo_5kg.jpg', 'NaturPet', FALSE,
 5.00, 'Pollo, riso, mais, grassi animali, vitamine', NULL, NULL, NULL),

(1, 1, 'Crocchette Puppy Agnello 3 kg',
 'Crocchette per cuccioli con agnello e riso, altamente digeribili.',
 19.50, 22.00, TRUE, 15.00, 'img/crocchette_agnello_3kg.jpg', 'NaturPet', FALSE,
 3.00, 'Agnello, riso, orzo, olio di pesce, vitamine', NULL, NULL, NULL),

(2, 1, 'Bocconcini Manzo 400 g',
 'Cibo umido per cani con bocconcini di manzo in salsa.',
 2.30, 22.00, FALSE, NULL, 'img/bocconcini_manzo.jpg', 'DogGourmet', FALSE,
 0.40, 'Manzo, brodo, verdure, gelificanti', NULL, NULL, NULL),

-- --- CIBI PER GATTO ---
(1, 2, 'Crocchette Gatto Sterilizzato 2 kg',
 'Crocchette per gatti sterilizzati, controllo del peso.',
 15.90, 22.00, FALSE, NULL, 'img/crocchette_gatto_steril.jpg', 'FelixCare', FALSE,
 2.00, 'Pollo, riso, fibre vegetali, taurina, vitamine', NULL, NULL, NULL),

(2, 2, 'Patè Salmone 85 g',
 'Cibo umido per gatti, patè al salmone.',
 1.20, 22.00, TRUE, 20.00, 'img/pate_salmone.jpg', 'FelixCare', FALSE,
 0.085, 'Salmone, brodo di pesce, oli, vitamine', NULL, NULL, NULL),

-- --- SNACK ---
(3, 1, 'Snack Dentale Cane 200 g',
 'Bastoncini dentali per l igiene orale del cane.',
 4.90, 22.00, FALSE, NULL, 'img/snack_dentale.jpg', 'DogGourmet', FALSE,
 0.20, 'Cereali, glicerina vegetale, menta', NULL, NULL, NULL),

(3, 2, 'Snack Malto Gatto 50 g',
 'Pasta al malto per gatti, favorisce l espulsione dei boli di pelo.',
 3.50, 22.00, FALSE, NULL, 'img/snack_malto.jpg', 'FelixCare', FALSE,
 0.05, 'Malto, oli vegetali, estratti', NULL, NULL, NULL),

-- --- ACCESSORI: GUINZAGLI ---
(4, 1, 'Guinzaglio Nylon Regolabile',
 'Guinzaglio in nylon resistente, lunghezza regolabile.',
 12.90, 22.00, FALSE, NULL, 'img/guinzaglio_nylon.jpg', 'WalkPet', FALSE,
 NULL, NULL, 'M', 'Nero', 'Nylon'),

(4, 1, 'Guinzaglio Cuoio Premium',
 'Guinzaglio in vera pelle cucita a mano.',
 29.90, 22.00, TRUE, 10.00, 'img/guinzaglio_cuoio.jpg', 'WalkPet', FALSE,
 NULL, NULL, 'L', 'Marrone', 'Cuoio'),

-- --- ACCESSORI: GIOCHI ---
(5, 1, 'Pallina Gomma Resistente',
 'Pallina in gomma naturale per il gioco del riporto.',
 5.90, 22.00, FALSE, NULL, 'img/pallina_gomma.jpg', 'PlayPet', FALSE,
 NULL, NULL, 'M', 'Rosso', 'Gomma'),

(5, 2, 'Topino con Erba Gatta',
 'Gioco per gatti in tessuto con erba gatta all interno.',
 3.20, 22.00, FALSE, NULL, 'img/topino_erba_gatta.jpg', 'PlayPet', FALSE,
 NULL, NULL, 'S', 'Grigio', 'Tessuto'),

-- --- ACCESSORI: CUCCE ---
(6, 2, 'Cuccia Morbida Gatto',
 'Cuccia imbottita e lavabile per gatti.',
 22.50, 22.00, FALSE, NULL, 'img/cuccia_gatto.jpg', 'CosyPet', FALSE,
 NULL, NULL, 'M', 'Beige', 'Poliestere'),

-- --- ACCESSORI: CIOTOLE ---
(7, 1, 'Ciotola Acciaio Inox 1 L',
 'Ciotola in acciaio inox antiscivolo, capacità 1 litro.',
 8.90, 22.00, FALSE, NULL, 'img/ciotola_inox.jpg', 'CosyPet', FALSE,
 NULL, NULL, 'L', 'Argento', 'Acciaio');

-- ============================================================
-- ADMIN
-- ============================================================
INSERT INTO admin (username, email, password_hash) VALUES
                                                  ('admin', 'admin@miaobau.it', 'U/x4jphbBuEEvM3lvqux4Q==:6Vqil8IXH9JV4lRWSuVCztsPKkVfG0CFHj1UtpqzF+M=');