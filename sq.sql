-- SQLite
SELECT country FROM countries;

SELECt city FROM cities WHERE country = 'x';

SELECT gezi_id, gezi_adi, gezi_ulke, gezi_sehir, gezi_baslangic_tarih, gezi_bitis_tarih, gezi_not FROM gezi;

SELECT gezi_id, gorsel FROM gezi_galeri;

INSERT INTO gezi (gezi_id, gezi_adi, gezi_ulke, gezi_sehir, gezi_baslangic_tarih, gezi_bitis_tarih, gezi_not) VALUES();

INSERT INTO gezi_galeri (gezi_id, gorsel) VALUES(0, ?);

UPDATE gezi SET gezi_adi='', gezi_ulke='', gezi_sehir='', gezi_baslangic_tarih='', gezi_bitis_tarih='', gezi_not='' WHERE gezi_id=0;

UPDATE gezi_galeri SET  WHERE gezi_id=0 AND gorsel=?;

DELETE FROM gezi WHERE gezi_id=0;

DELETE FROM gezi_galeri WHERE gezi_id=0 AND gorsel=?;