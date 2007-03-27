insert into payment_terms values (1,'desc1',CURRENT - INTERVAL(0) HOUR to HOUR,'creationUser1',CURRENT - INTERVAL(0) HOUR to HOUR,'modificationUser1',1,1);
insert into payment_terms values (2,'desc2',CURRENT - INTERVAL(24) HOUR to HOUR,'creationUser2',CURRENT - INTERVAL(24) HOUR to HOUR,'modificationUser2',0,2);
insert into payment_terms values (3,'desc3',CURRENT - INTERVAL(48) HOUR to HOUR,'creationUser3',CURRENT - INTERVAL(48) HOUR to HOUR,'modificationUser3',1,3);
insert into payment_terms values (4,'desc4',CURRENT - INTERVAL(72) HOUR to HOUR,'creationUser4',CURRENT - INTERVAL(72) HOUR to HOUR,'modificationUser4',0,4);
insert into payment_terms values (5,'desc5',CURRENT - INTERVAL(96) HOUR to HOUR,'creationUser5',CURRENT - INTERVAL(96) HOUR to HOUR,'modificationUser5',1,5);
insert into payment_terms values (6,'desc6',CURRENT - INTERVAL(120) HOUR(3) to HOUR,'creationUser6',CURRENT - INTERVAL(120) HOUR(3) to HOUR,'modificationUser6',0,6);
insert into payment_terms values (7,'desc7',CURRENT - INTERVAL(144) HOUR(3) to HOUR,'creationUser7',CURRENT - INTERVAL(144) HOUR(3) to HOUR,'modificationUser7',1,7);
insert into payment_terms values (8,'desc8',CURRENT - INTERVAL(168) HOUR(3) to HOUR,'creationUser8',CURRENT - INTERVAL(168) HOUR(3) to HOUR,'modificationUser8',0,8);
insert into payment_terms values (9,'desc9',CURRENT - INTERVAL(192) HOUR(3) to HOUR,'creationUser9',CURRENT - INTERVAL(192) HOUR(3) to HOUR,'modificationUser9',1,9);
insert into payment_terms values (10,'desc10',CURRENT - INTERVAL(216) HOUR(3) to HOUR,'creationUser10',CURRENT - INTERVAL(216) HOUR(3) to HOUR,'modificationUser10',0,10);
insert into id_sequences (name, next_block_start, block_size, exhausted) values ('PaymentTermGenerator', 11, 3, 0);