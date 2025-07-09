delete from books;

insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
    value (1, 'Title test1', 'Author test1', 'ISBN1234567891', 9.99, 'Description test1', 'https://example.com/tast-cover-image-1.jpg', false);

insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
    value (2, 'Title test2', 'Author test2', 'ISBN1234567892', 99.99, 'Description test2', 'https://example.com/tast-cover-image-2.jpg', false);

insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
    value (3, 'Title test3', 'Author test3', 'ISBN1234567893', 999.99, 'Description test3', 'https://example.com/tast-cover-image.jpg-3', false);
