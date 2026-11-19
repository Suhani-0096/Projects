use bhagya_swapn_jewels;
show tables;


SELECT id, customer_name, status, created_at
FROM bookings;

USE bhagya_swapn_jewels;

SELECT
    id,
    customer_name,
    mobile,
    status,
    created_at
FROM bookings
ORDER BY id DESC;

SELECT
    booking_id,
    product_id,
    product_name,
    quantity,
    unit_price
FROM booking_items
WHERE booking_id = 1;

SELECT
    p.id,
    p.name,
    p.stock_quantity
FROM products p
JOIN booking_items bi ON bi.product_id = p.id
WHERE bi.booking_id = 1;

SELECT
    b.id AS booking_id,
    b.status,
    bi.product_id,
    bi.product_name,
    bi.quantity AS approved_quantity,
    p.stock_quantity AS current_stock
FROM bookings b
JOIN booking_items bi ON bi.booking_id = b.id
JOIN products p ON p.id = bi.product_id
WHERE b.id = 1;

SELECT id, name, stock_quantity
FROM products
WHERE id = 1;


SELECT id, status
FROM bookings
WHERE id = 1;

USE bhagya_swapn_jewels;

SELECT id, customer_name, status, created_at
FROM bookings
ORDER BY id DESC
LIMIT 5;

SELECT product_id, product_name, quantity, unit_price
FROM booking_items;

SELECT id, name, stock_quantity
FROM products;

ALTER TABLE bookings
ADD COLUMN overdue BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN owner_reminder_sent BOOLEAN NOT NULL DEFAULT FALSE;

DESCRIBE bookings;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    message VARCHAR(500) NOT NULL,
    type VARCHAR(50) NOT NULL,
    booking_id BIGINT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL
);

DESCRIBE notifications;

SELECT * FROM notifications
ORDER BY created_at DESC;

SELECT id, status, created_at, overdue, owner_reminder_sent
FROM bookings
WHERE status = 'PENDING';

UPDATE bookings
SET created_at = DATE_SUB(NOW(), INTERVAL 3 HOUR)
WHERE id = 2
AND status = 'PENDING';

SELECT id, status, overdue, owner_reminder_sent
FROM bookings;

SELECT *
FROM notifications
ORDER BY created_at DESC;

USE bhagya_swapn_jewels;

SELECT id, title, is_read
FROM notifications
ORDER BY id DESC;

USE bhagya_swapn_jewels;

UPDATE notifications
SET is_read = 0
WHERE id = 3;

SELECT id, title, is_read
FROM notifications
ORDER BY id DESC;

SELECT COUNT(*) AS unread_count
FROM notifications
WHERE is_read = 0;