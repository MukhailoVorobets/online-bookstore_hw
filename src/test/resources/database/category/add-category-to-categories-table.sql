DELETE FROM categories;

INSERT INTO categories (id, name, description, is_deleted) VALUE
    (1, 'Test name one', 'Description test one', false),
    (2, 'Test name two', 'Description test two', false),
    (3, 'Test name three', 'Description test three', false);
