CREATE DEFINER=`root`@`localhost` TRIGGER `updateSalesReport` AFTER INSERT ON `subscription` FOR EACH ROW BEGIN
UPDATE salesReport SET TotalPurchase = TotalPurchase + 1, TotalValue = TotalValue + NEW.fee WHERE id_package = NEW.id_package;

IF NEW.validity_period = 12 THEN
UPDATE salesReport SET TotalFor12 = TotalFor12 + 1,
NetValue = NetValue + (SELECT fee12 FROM packages WHERE id = NEW.id_package) WHERE id_package = NEW.id_package;
ELSEIF NEW.validity_period = 24 THEN 
UPDATE salesReport SET TotalFor24 = TotalFor24 + 1,
NetValue = NetValue + (SELECT fee24 FROM packages WHERE id = NEW.id_package)
WHERE id_package = NEW.id_package;
ELSEIF NEW.validity_period = 36 THEN
UPDATE salesReport SET TotalFor36 = TotalFor36 + 1,
NetValue = NetValue + (SELECT fee36 FROM packages WHERE id = NEW.id_package) WHERE id_package = NEW.id_package;
END IF;
END