DELETE FROM books;

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUE
    (1, 'Title test1', 'Author test1', 'ISBN1234567891', 9.99, 'Description test1', 'https://example.com/tast-cover-image-1.jpg', false),
    (2, 'Title test2', 'Author test2', 'ISBN1234567892', 99.99, 'Description test2', 'https://example.com/tast-cover-image-2.jpg', false),
    (3, 'Title test3', 'Author test3', 'ISBN1234567893', 999.99, 'Description test3', 'https://example.com/tast-cover-image.jpg-3', false);
