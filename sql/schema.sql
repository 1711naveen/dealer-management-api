-- Database: dealer_management

-- Create database (run this first)
-- CREATE DATABASE dealer_management;

-- Dealers table
CREATE TABLE IF NOT EXISTS dealers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    subscription_type VARCHAR(20) NOT NULL CHECK (subscription_type IN ('BASIC', 'PREMIUM')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGSERIAL PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    model VARCHAR(255) NOT NULL,
    price DECIMAL(19,2) NOT NULL CHECK (price > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'SOLD')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dealer_id) REFERENCES dealers(id) ON DELETE CASCADE
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL CHECK (amount > 0),
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('UPI', 'CARD', 'NETBANKING')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    transaction_id VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dealer_id) REFERENCES dealers(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_dealers_email ON dealers(email);
CREATE INDEX idx_dealers_subscription_type ON dealers(subscription_type);
CREATE INDEX idx_vehicles_dealer_id ON vehicles(dealer_id);
CREATE INDEX idx_vehicles_status ON vehicles(status);
CREATE INDEX idx_payments_dealer_id ON payments(dealer_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);

-- Sample data insert statements
INSERT INTO dealers (name, email, subscription_type) VALUES
('Premium Motors', 'premium@example.com', 'PREMIUM'),
('Basic Cars', 'basic@example.com', 'BASIC'),
('Elite Vehicles', 'elite@example.com', 'PREMIUM'),
('Standard Auto', 'standard@example.com', 'BASIC');

INSERT INTO vehicles (dealer_id, model, price, status) VALUES
(1, 'BMW X5', 55000.00, 'AVAILABLE'),
(1, 'Mercedes C-Class', 45000.00, 'AVAILABLE'),
(1, 'Audi A4', 42000.00, 'SOLD'),
(2, 'Honda Civic', 25000.00, 'AVAILABLE'),
(2, 'Toyota Camry', 28000.00, 'SOLD'),
(3, 'Porsche 911', 85000.00, 'AVAILABLE'),
(3, 'Jaguar F-Type', 65000.00, 'AVAILABLE'),
(4, 'Ford Focus', 22000.00, 'AVAILABLE'),
(4, 'Nissan Altima', 24000.00, 'AVAILABLE');

-- Useful queries
-- Get all vehicles from PREMIUM dealers only
SELECT v.*, d.name as dealer_name, d.email as dealer_email 
FROM vehicles v 
JOIN dealers d ON v.dealer_id = d.id 
WHERE d.subscription_type = 'PREMIUM';

-- Get dealer with vehicle count
SELECT d.*, COUNT(v.id) as vehicle_count 
FROM dealers d 
LEFT JOIN vehicles v ON d.id = v.dealer_id 
GROUP BY d.id, d.name, d.email, d.subscription_type;

-- Get payment summary by dealer
SELECT d.name, d.subscription_type, 
       COUNT(p.id) as total_payments,
       SUM(p.amount) as total_amount,
       COUNT(CASE WHEN p.status = 'SUCCESS' THEN 1 END) as successful_payments
FROM dealers d
LEFT JOIN payments p ON d.id = p.dealer_id
GROUP BY d.id, d.name, d.subscription_type;
