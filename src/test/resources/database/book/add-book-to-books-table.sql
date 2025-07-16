DELETE FROM books;

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUE
    (1, 'Title test one', 'Author test one', 'ISBN1234567891', 9.99, 'Description test one', 'https://example.com/tast-cover-image-1.jpg', false),
    (2, 'Title test two', 'Author test two', 'ISBN1234567892', 99.99, 'Description test two', 'https://example.com/tast-cover-image-2.jpg', false),
    (3, 'Title test three', 'Author test three', 'ISBN1234567893', 999.99, 'Description test three', 'https://example.com/tast-cover-image.jpg-3', false);
