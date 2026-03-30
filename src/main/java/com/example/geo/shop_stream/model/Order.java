package com.example.geo.shop_stream.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    @Column(nullable = false)
    private String customerId;

    /*
    Store as JSON string in a single TEXT column.
    Simpler, readable, and perfectly fine when you never need
    to query "give me all orders containing product X".

     * We use Option B here via a @Converter. Good learning moment: JPA
     * lets you plug in custom type conversions between Java and the DB column.
     */
    @Convert(converter = StringListConverter.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private List<String> productIds;

    /*
     * BigDecimal, NOT double or float.
     * Floating point types cannot represent money exactly:
     *   0.1 + 0.2 = 0.30000000000000004 in double.
     * BigDecimal is exact. Always use it for currency.
     * precision=19 scale=4 → up to 999,999,999,999,999.9999
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    /*
     * @Enumerated(EnumType.STRING) stores "PENDING", "CONFIRMED", "FAILED"
     * as readable strings in the DB.
     *
     * Never use EnumType.ORDINAL — it stores 0, 1, 2.
     * If you ever reorder your enum values, all existing rows silently
     * get the wrong status. STRING is safe to reorder and readable in SQL.
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /*
     * Instant is UTC — always store timestamps in UTC, convert to
     * local timezone only in the presentation layer.
     * columnDefinition = "TIMESTAMPTZ" tells Postgres to use
     * timestamp with time zone.
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private Instant updatedAt;


    // JPA requires a no-arg constructor — keep it protected so your
    // own code is forced to use the parameterized one or a builder
    protected Order() {}

    public Order(String id,
                 String customerId,
                 List<String> productIds,
                 BigDecimal totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING;     // always starts as PENDING
        this.createdAt = Instant.now();
    }

    // --- getters ---

    public String getId()                  { return id; }
    public String getCustomerId()          { return customerId; }
    public List<String> getProductIds()    { return productIds; }
    public BigDecimal getTotalAmount()     { return totalAmount; }
    public OrderStatus getStatus()         { return status; }
    public Instant getCreatedAt()          { return createdAt; }
    public Instant getUpdatedAt()          { return updatedAt; }

    // --- only the fields that change after creation have setters ---

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
