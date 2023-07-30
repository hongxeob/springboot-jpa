package com.example.springbootjpa.domain.customer;

import com.example.springbootjpa.domain.order.Order;
import com.example.springbootjpa.golbal.ErrorCode;
import com.example.springbootjpa.golbal.exception.InvalidDomainConditionException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.springbootjpa.golbal.ErrorCode.INVALID_USERNAME;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    private static final Pattern STRING_REGEX_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]+$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Customer(Long id, String username, String address) {
        this.id = id;
        validateUsername(username);
        this.username = username;
        validateAddress(address);
        this.address = address;
    }

    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username;
    }

    public void updateAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)
                || !STRING_REGEX_PATTERN.matcher(username).matches()) {
            throw new InvalidDomainConditionException(INVALID_USERNAME);
        }
    }

    private void validateAddress(String address) {
        if (!StringUtils.hasText(address)
                || !STRING_REGEX_PATTERN.matcher(address).matches()) {
            throw new InvalidDomainConditionException(ErrorCode.INVALID_ADDRESS);
        }
    }
}