package com.tui.proof.model;

import com.tui.proof.common.OrderState;
import com.tui.proof.common.validator.PilotesQuantityInRange;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tb_order",
        indexes = {
                @Index(name = "idx_order_client_fk", columnList = "client_id"),
                @Index(name = "idx_order_address_fk", columnList = "address_id")
        }
)
@NamedEntityGraph(
        name = "order_with_client_and_address_graph",
        attributeNodes = {
                @NamedAttributeNode("client"),
                @NamedAttributeNode("address")
        }
)
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_order_generator")
    @SequenceGenerator(name = "seq_order_generator", sequenceName = "seq_order")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "number", length = 256)
    @NotNull(message = "Number cannot be null")
    @Size(min = 2, max = 256, message = "Length of number should be in range: from 2 to 256")
    private String number;

    @Column(name = "state")
    @NotNull(message = "Order state cannot be null")
    private OrderState state;

    @Column(name = "pilotes")
    private Integer pilotes;

    @Column(name = "order_total")
    private BigDecimal orderTotal;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
