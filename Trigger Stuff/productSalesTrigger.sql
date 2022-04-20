mysql>delimiter /
create trigger updatePurchased
after insert on subscribe_product
for each row
BEGIN
update product set purchased = purchased + 1 where name = NEW.name_product;
update salesReport set productPurchased = productPurchased + 1 where id_package IN (select id_package from subscription where id = NEW.id_subscribe);
END;
/
